package com.tokopedia.content.common.producttag.view.fragment.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.RouteManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.FragmentProductTagParentBinding
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.producttag.util.extension.currentSource
import com.tokopedia.content.common.producttag.util.extension.isAutocomplete
import com.tokopedia.content.common.producttag.util.extension.withCache
import com.tokopedia.content.common.producttag.util.getAutocompleteApplink
import com.tokopedia.content.common.producttag.util.preference.ProductTagPreference
import com.tokopedia.content.common.producttag.view.bottomsheet.ProductTagSourceBottomSheet
import com.tokopedia.content.common.producttag.view.fragment.ContentAutocompleteFragment
import com.tokopedia.content.common.producttag.view.fragment.GlobalSearchFragment
import com.tokopedia.content.common.producttag.view.fragment.LastPurchasedProductFragment
import com.tokopedia.content.common.producttag.view.fragment.LastTaggedProductFragment
import com.tokopedia.content.common.producttag.view.fragment.MyShopProductFragment
import com.tokopedia.content.common.producttag.view.fragment.ShopProductFragment
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.SearchParamUiModel
import com.tokopedia.content.common.producttag.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.common.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.content.common.producttag.view.uimodel.state.ProductTagSourceUiState
import com.tokopedia.content.common.producttag.view.uimodel.state.ProductTagUiState
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.content.common.producttag.view.viewmodel.factory.ProductTagViewModelFactory
import com.tokopedia.content.common.util.getParentFragmentByInstance
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.abstraction.R as abstractionR

/**
* Created By : Jonathan Darwin on April 25, 2022
*/
class ProductTagParentFragment @Inject constructor(
    private val userSession: UserSessionInterface,
    private val viewModelFactoryCreator: ProductTagViewModelFactory.Creator,
    private val dispatchers: CoroutineDispatchers,
    private val sharedPref: ProductTagPreference,
) : TkpdBaseV4Fragment() {

    override fun getScreenName(): String = "ProductTagParentFragment"

    private val fragmentArgument: ContentProductTagArgument by lazy(LazyThreadSafetyMode.NONE) {
        getProductTagArgument()
    }

    private lateinit var viewModel: ProductTagViewModel

    private var mListener: Listener? = null
    private var mDataSource: DataSource? = null
    private var mAnalytic: ContentProductTagAnalytic? = null

    private var _binding: FragmentProductTagParentBinding? = null
    private val binding: FragmentProductTagParentBinding
        get() = _binding!!

    private var coachmark: CoachMark2? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(fragmentArgument.isAutoHandleBackPressed) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = createViewModelProvider()[ProductTagViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductTagParentBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false,
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserve()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if(childFragment is BaseProductTagChildFragment) {
            childFragment.createViewModelProvider(createViewModelProvider())
            childFragment.setAnalytic(mAnalytic)
        }

        when(childFragment) {
            is ProductTagSourceBottomSheet -> {
                childFragment.setListener(object : ProductTagSourceBottomSheet.Listener {
                    override fun onSelectProductTagSource(source: ProductTagSource) {
                        mAnalytic?.clickProductTagSource(source, viewModel.authorId, viewModel.authorType)
                        viewModel.submitAction(ProductTagAction.SelectProductTagSource(source))
                    }
                })
                childFragment.setData(
                    viewModel.productTagSourceList,
                    viewModel.shopBadge,
                    viewModel.authorId,
                    viewModel.authorType,
                )
                childFragment.setAnalytic(mAnalytic)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        coachmark?.hideCoachMark()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setLoading(isLoading: Boolean) {
        viewModel.submitAction(ProductTagAction.LoadingSubmitProduct(isLoading))
    }

    private fun setupView() {
        binding.icCcProductTagBack.setImage(
            newIconId = when(viewModel.backButton) {
                ContentProductTagConfig.BackButton.Back -> {
                    IconUnify.ARROW_BACK
                }
                ContentProductTagConfig.BackButton.Close -> {
                    IconUnify.CLOSE
                }
            }
        )

        binding.icCcProductTagBack.setOnClickListener {
            mAnalytic?.clickBackButton(viewModel.selectedTagSource)
            viewModel.submitAction(ProductTagAction.BackPressed)
        }

        binding.tvCcProductTagProductSource.setOnClickListener {
            clickBreadcrumb()
        }

        binding.icCcProductTagChevron1.setOnClickListener {
            clickBreadcrumb()
        }

        binding.icCcProductTagChevron2.setOnClickListener {
            clickBreadcrumb()
        }

        binding.tvCcProductTagProductSource2.setOnClickListener {
            clickBreadcrumb()
        }

        binding.btnSave.setOnClickListener {
            viewModel.submitAction(ProductTagAction.ClickSaveButton)
        }

        showBreadcrumb(viewModel.isUser)
        showCoachmarkGlobalTag(viewModel.isShowCoachmarkGlobalTag)
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderSelectedProductTagSource(it.prevValue?.productTagSource, it.value.productTagSource)
                renderActionBar(it.prevValue, it.value)
                renderSaveButton(it.prevValue, it.value)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when(it) {
                    is ProductTagUiEvent.FinishProductTag -> {
                        mAnalytic?.clickSaveProduct(viewModel.selectedTagSource)
                        mListener?.onFinishProductTag(it.products)
                    }
                    is ProductTagUiEvent.ShowSourceBottomSheet -> {
                        ProductTagSourceBottomSheet.getFragment(
                            childFragmentManager,
                            requireActivity().classLoader
                        ).showNow(childFragmentManager)
                    }
                    is ProductTagUiEvent.OpenAutoCompletePage -> {
                        RouteManager.route(requireContext(), getAutocompleteApplink(it.query, viewModel.appLinkAfterAutocomplete))
                    }
                    is ProductTagUiEvent.ShowError -> {
                        Toaster.build(
                            binding.root,
                            text = getString(abstractionR.string.default_request_error_unknown),
                            type = Toaster.TYPE_ERROR,
                            duration = Toaster.LENGTH_LONG,
                            actionText = if(it.action != null) getString(R.string.feed_content_coba_lagi_text) else "",
                            clickListener = { view -> it.action?.invoke() }
                        ).show()
                    }
                    ProductTagUiEvent.MaxSelectedProductReached -> {
                        mListener?.onMaxSelectedProductReached()
                    }
                }
            }
        }
    }

    private fun renderSelectedProductTagSource(
        prevState: ProductTagSourceUiState?,
        currState: ProductTagSourceUiState,
    ) {
        if(prevState == currState) return

        updateFragmentContent(prevState?.productTagSourceStack ?: emptySet(), currState.productTagSourceStack)
        updateBreadcrumb(currState.productTagSourceStack)
    }

    private fun renderActionBar(
        prevState: ProductTagUiState?,
        currState: ProductTagUiState,
    ) {
        if(prevState?.selectedProduct == currState.selectedProduct &&
            prevState.productTagSource == currState.productTagSource
        ) return

        updateActionBar(currState.productTagSource.productTagSourceStack)
        updateTitle(currState.selectedProduct)
    }

    private fun renderSaveButton(
        prevState: ProductTagUiState?,
        currState: ProductTagUiState,
    ) {
        if(prevState?.selectedProduct == currState.selectedProduct &&
            prevState.productTagSource == currState.productTagSource &&
            prevState.isSubmitting == currState.isSubmitting
        ) return

        binding.flBtnSave.showWithCondition(
            !currState.productTagSource.productTagSourceStack.isAutocomplete &&
                    viewModel.isMultipleSelectionProduct
        )

        binding.btnSave.isEnabled = currState.selectedProduct.isNotEmpty() && !viewModel.isSameAsInitialSelectedProduct
        binding.btnSave.isLoading = currState.isSubmitting
    }

    private fun updateActionBar(productTagSourceStack: Set<ProductTagSource>) {
        val isShowActionBar = !productTagSourceStack.isAutocomplete

        binding.icCcProductTagBack.showWithCondition(isShowActionBar)
        binding.tvCcProductTagPageTitle.showWithCondition(isShowActionBar)
        binding.viewCcProductTagDivider.showWithCondition(isShowActionBar && viewModel.isShowActionBarDivider)
    }

    private fun updateTitle(selectedProduct: List<SelectedProductUiModel>) {
        val title = if(viewModel.isMultipleSelectionProduct)
            getString(R.string.content_creation_multiple_product_tag_title).format(selectedProduct.size, viewModel.maxSelectedProduct)
        else getString(R.string.content_creation_product_tag_title)

        binding.tvCcProductTagPageTitle.text = title
    }

    private fun updateFragmentContent(prevStack: Set<ProductTagSource>, currStack: Set<ProductTagSource>) {
        if(currStack.isEmpty()) {
            mListener?.onCloseProductTag() ?: run {
                requireActivity().finish()
            }
            return
        }

        val selectedSource = currStack.currentSource
        val (fragment, tag) = getFragmentAndTag(selectedSource)
        if(currStack.size >= prevStack.size) {
            childFragmentManager.beginTransaction()
                .replace(binding.flCcProductTagContainer.id, fragment, tag)
                .apply {
                    if(currStack.size > prevStack.size)
                        addToBackStack(null)
                }
                .commit()
        }
        else {
            repeat(prevStack.size - currStack.size) {
                childFragmentManager.popBackStackImmediate()
            }

            childFragmentManager.beginTransaction()
                .replace(binding.flCcProductTagContainer.id, fragment, tag)
                .commitNow()
        }
    }

    private fun updateBreadcrumb(productTagSourceStack: Set<ProductTagSource>) {
        if(viewModel.isUser) {

            if(productTagSourceStack.isAutocomplete) {
                showBreadcrumb(false)
                return
            }

            /** Update the First Part */
            if(productTagSourceStack.isNotEmpty()) {
                val firstSource = productTagSourceStack.first()

                binding.icCcProductTagChevron1.setImage(IconUnify.CHEVRON_DOWN)
                binding.tvCcProductTagProductSource.text = getProductTagSourceText(firstSource)

                binding.tvCcProductTagProductSourceLabel.show()
                binding.tvCcProductTagProductSource.show()
                binding.icCcProductTagChevron1.show()

                if(firstSource == ProductTagSource.MyShop && viewModel.shopBadge.isNotEmpty()) {
                    binding.imgCcProductTagShopBadge1.setImageUrl(viewModel.shopBadge)
                    binding.imgCcProductTagShopBadge1.show()
                    binding.icCcProductTagShopBadge1.hide()
                }
                else if(firstSource == ProductTagSource.Shop) {
                    binding.icCcProductTagShopBadge1.setImage(viewModel.selectedShop.badge)
                    binding.icCcProductTagShopBadge1.showWithCondition(viewModel.selectedShop.isShopHasBadge)
                    binding.imgCcProductTagShopBadge1.hide()
                }
                else {
                    binding.imgCcProductTagShopBadge1.hide()
                    binding.icCcProductTagShopBadge1.hide()
                }
            }

            /** Update the Last Part */
            val hasLastPart = productTagSourceStack.size == 2
            binding.icCcProductTagShopBadge2.showWithCondition(hasLastPart)
            binding.tvCcProductTagProductSource2.showWithCondition(hasLastPart)
            binding.icCcProductTagChevron2.showWithCondition(hasLastPart)

            if(hasLastPart) {
                val lastSource = productTagSourceStack.last()

                binding.icCcProductTagChevron1.setImage(IconUnify.CHEVRON_RIGHT)
                binding.tvCcProductTagProductSource2.text = getProductTagSourceText(lastSource)
                binding.icCcProductTagShopBadge2.apply {
                    shouldShowWithAction(viewModel.selectedShop.isShopHasBadge) {
                        setImage(viewModel.selectedShop.badge)
                    }
                }
            }
        }
    }

    private fun getFragmentAndTag(productTagSource: ProductTagSource): Pair<BaseProductTagChildFragment, String> {
        val classLoader = requireActivity().classLoader
        return when(productTagSource) {
            ProductTagSource.LastTagProduct -> LastTaggedProductFragment.getFragmentPair(childFragmentManager, classLoader)
            ProductTagSource.LastPurchase -> LastPurchasedProductFragment.getFragmentPair(childFragmentManager, classLoader)
            ProductTagSource.MyShop -> MyShopProductFragment.getFragmentPair(childFragmentManager, classLoader)
            ProductTagSource.GlobalSearch -> GlobalSearchFragment.getFragmentPair(childFragmentManager, classLoader)
            ProductTagSource.Shop -> ShopProductFragment.getFragmentPair(childFragmentManager, classLoader)
            ProductTagSource.Autocomplete -> ContentAutocompleteFragment.getFragmentPair(childFragmentManager, classLoader)
            else -> {
                if(viewModel.isSeller) MyShopProductFragment.getFragmentPair(childFragmentManager, classLoader)
                else LastTaggedProductFragment.getFragmentPair(childFragmentManager, classLoader)
            }
        }
    }

    private fun getProductTagSourceText(source: ProductTagSource): String {
        return when(source) {
            ProductTagSource.LastPurchase -> getString(R.string.content_creation_search_bs_item_last_purchase)
            ProductTagSource.Wishlist -> getString(R.string.content_creation_search_bs_item_wishlist)
            ProductTagSource.MyShop -> userSession.shopName
            ProductTagSource.Shop -> viewModel.selectedShop.shopName
            else -> getString(R.string.content_creation_search_bs_item_tokopedia)
        }
    }

    private fun createViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(
            this,
            viewModelFactoryCreator.create(
                this,
                productTagSourceRaw = fragmentArgument.productTagSource,
                shopBadge = fragmentArgument.shopBadge,
                authorId = fragmentArgument.authorId,
                authorType = fragmentArgument.authorType,
                initialSelectedProduct = mDataSource?.getInitialSelectedProduct() ?: emptyList(),
                productTagConfig = ContentProductTagConfig(
                    isMultipleSelectionProduct = fragmentArgument.isMultipleSelectionProduct,
                    isFullPageAutocomplete = fragmentArgument.isFullPageAutocomplete,
                    maxSelectedProduct = fragmentArgument.maxSelectedProduct,
                    backButton = fragmentArgument.backButton,
                    isShowActionBarDivider = fragmentArgument.isShowActionBarDivider,
                    appLinkAfterAutocomplete = fragmentArgument.appLinkAfterAutocomplete,
                )
            )
        )
    }

    private fun getProductTagArgument(): ContentProductTagArgument {
        return ContentProductTagArgument.mapFromString(getStringArgument(EXTRA_QUERY))
    }

    private fun getStringArgument(key: String): String {
        return arguments?.getString(key) ?: ""
    }

    private fun clickBreadcrumb() {
        mAnalytic?.clickBreadcrumb(viewModel.selectedTagSource == ProductTagSource.Shop)
        viewModel.submitAction(ProductTagAction.ClickBreadcrumb)
    }

    private fun showBreadcrumb(isShow: Boolean) {
        binding.apply {
            tvCcProductTagProductSourceLabel.showWithCondition(isShow)
            tvCcProductTagProductSource.showWithCondition(isShow)
            tvCcProductTagProductSource2.showWithCondition(isShow)

            icCcProductTagShopBadge1.showWithCondition(isShow)
            icCcProductTagShopBadge2.showWithCondition(isShow)

            icCcProductTagChevron1.showWithCondition(isShow)
            icCcProductTagChevron2.showWithCondition(isShow)

            imgCcProductTagShopBadge1.showWithCondition(isShow)
        }

        if(!isShow) coachmark?.hideCoachMark()
    }

    private fun showCoachmarkGlobalTag(isShow: Boolean) {
        if(isShow) {
            coachmark = CoachMark2(activity as Context)

            viewLifecycleOwner.lifecycleScope.launch(dispatchers.main) {
                val isParentBottomSheet = getParentFragmentByInstance<BottomSheetUnify>() != null
                if(isParentBottomSheet) {
                    withContext(dispatchers.computation) {
                        delay(COACHMARK_DELAY)
                    }
                }

                coachmark?.showCoachMark(arrayListOf(
                    CoachMark2Item(
                        binding.tvCcProductTagProductSource,
                        getString(R.string.content_creation_search_coachmark_header),
                        getString(R.string.content_creation_search_coachmark_desc),
                        CoachMark2.POSITION_BOTTOM
                    )
                ))

                coachmark?.onDismissListener = {
                    sharedPref.setNotFirstGlobalTag()
                }
            }
        }
    }

    fun onNewIntent(intent: Intent?) {
        val path = intent?.data?.path.toString()
        val source = if(path.contains(PRODUCT)) ProductTagSource.GlobalSearch
        else if(path.contains(SHOP)) ProductTagSource.Shop
        else ProductTagSource.Unknown

        val query = intent?.extras?.getString(SearchParamUiModel.KEY_QUERY).orEmpty()
        val shopId = intent?.data?.lastPathSegment.orEmpty()
        val componentId = intent?.extras?.getString(SearchParamUiModel.KEY_COMPONENT_ID).orEmpty()

        viewModel.submitAction(ProductTagAction.SetDataFromAutoComplete(source, query, shopId, componentId))
    }

    fun onBackPressed() {
        viewModel.submitAction(ProductTagAction.BackPressed)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setDataSource(dataSource: DataSource) {
        mDataSource = dataSource
    }

    fun setAnalytic(analytic: ContentProductTagAnalytic?) {
        mAnalytic = analytic
    }

    companion object {
        const val TAG = "ProductTagParentFragment"
        private const val EXTRA_QUERY = "EXTRA_QUERY"

        private const val PRODUCT = "product"
        private const val SHOP = "shop"

        private const val COACHMARK_DELAY = 1000L

        fun findFragment(fragmentManager: FragmentManager): ProductTagParentFragment? {
            return fragmentManager.findFragmentByTag(TAG) as? ProductTagParentFragment
        }

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            argumentBuilder: ContentProductTagArgument.Builder,
        ): ProductTagParentFragment {
            val oldInstance = findFragment(fragmentManager)
            return oldInstance ?: createFragment(fragmentManager, classLoader, argumentBuilder)
        }

        private fun createFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            argumentBuilder: ContentProductTagArgument.Builder,
        ): ProductTagParentFragment {
            return (
                fragmentManager.fragmentFactory.instantiate(
                    classLoader,
                    ProductTagParentFragment::class.java.name
                ) as ProductTagParentFragment
            ).apply {
                arguments = Bundle().apply {
                    putString(EXTRA_QUERY, argumentBuilder.build())
                }
            }
        }
    }

    interface Listener {
        fun onCloseProductTag()
        fun onFinishProductTag(products: List<SelectedProductUiModel>)
        fun onMaxSelectedProductReached()
    }

    interface DataSource {
        fun getInitialSelectedProduct(): List<SelectedProductUiModel>
    }
}
