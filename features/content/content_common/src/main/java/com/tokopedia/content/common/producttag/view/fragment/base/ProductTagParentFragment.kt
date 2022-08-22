package com.tokopedia.content.common.producttag.view.fragment.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.content.common.R
import com.tokopedia.content.common.databinding.FragmentProductTagParentBinding
import com.tokopedia.content.common.producttag.analytic.product.ProductTagAnalytic
import com.tokopedia.content.common.producttag.util.extension.currentSource
import com.tokopedia.content.common.producttag.util.extension.withCache
import com.tokopedia.content.common.producttag.util.getAutocompleteApplink
import com.tokopedia.content.common.producttag.view.bottomsheet.ProductTagSourceBottomSheet
import com.tokopedia.content.common.producttag.view.fragment.*
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.content.common.producttag.view.uimodel.state.ProductTagSourceUiState
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.content.common.producttag.view.viewmodel.factory.ProductTagViewModelFactory
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import com.tokopedia.abstraction.R as abstractionR

/**
* Created By : Jonathan Darwin on April 25, 2022
*/
class ProductTagParentFragment @Inject constructor(
    private val userSession: UserSessionInterface,
    private val viewModelFactoryCreator: ProductTagViewModelFactory.Creator,
    private val analytic: ProductTagAnalytic,
) : TkpdBaseV4Fragment() {

    override fun getScreenName(): String = "ProductTagParentFragment"

    private lateinit var viewModel: ProductTagViewModel
    private var mListener: Listener? = null

    private var _binding: FragmentProductTagParentBinding? = null
    private val binding: FragmentProductTagParentBinding
        get() = _binding!!

    private var coachmark: CoachMark2? = null

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
        }

        when(childFragment) {
            is ProductTagSourceBottomSheet -> {
                childFragment.apply {
                    setListener(object : ProductTagSourceBottomSheet.Listener {
                        override fun onSelectProductTagSource(source: ProductTagSource) {
                            analytic.clickProductTagSource(source)
                            viewModel.submitAction(ProductTagAction.SelectProductTagSource(source))
                        }
                    })
                    setData(viewModel.productTagSourceList, viewModel.shopBadge)
                }
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

    private fun setupView() {
        binding.icCcProductTagBack.setOnClickListener {
            analytic.clickBackButton(viewModel.selectedTagSource)
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

        showBreadcrumb(viewModel.isUser)
        showCoachmarkGlobalTag(viewModel.isShowCoachmarkGlobalTag)
    }

    private fun setupObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderSelectedProductTagSource(it.prevValue?.productTagSource, it.value.productTagSource)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect {
                when(it) {
                    is ProductTagUiEvent.ProductSelected -> {
                        val product = it.product

                        val data = Intent().apply {
                            putExtra(RESULT_PRODUCT_ID, product.id)
                            putExtra(RESULT_PRODUCT_NAME, product.name)
                            putExtra(RESULT_PRODUCT_PRICE, if(product.isDiscount) product.priceDiscountFmt else product.priceFmt)
                            putExtra(RESULT_PRODUCT_IMAGE, product.coverURL)
                            putExtra(RESULT_PRODUCT_PRICE_ORIGINAL_FMT, product.priceOriginalFmt)
                            putExtra(RESULT_PRODUCT_PRICE_DISCOUNT_FMT, product.discountFmt)
                            putExtra(RESULT_PRODUCT_IS_DISCOUNT, product.isDiscount)
                        }
                        requireActivity().setResult(Activity.RESULT_OK, data)
                        requireActivity().finish()
                    }
                    is ProductTagUiEvent.ShowSourceBottomSheet -> {
                        ProductTagSourceBottomSheet.getFragment(
                            childFragmentManager,
                            requireActivity().classLoader
                        ).showNow(childFragmentManager)
                    }
                    is ProductTagUiEvent.OpenAutoCompletePage -> {
                        RouteManager.route(requireContext(), getAutocompleteApplink(it.query))
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
                }
            }
        }

    }

    private fun renderSelectedProductTagSource(
        prevState: ProductTagSourceUiState?,
        currState: ProductTagSourceUiState,
    ) {
        if(prevState == currState) return

        updateBreadcrumb(currState.productTagSourceStack)
        updateFragmentContent(prevState?.productTagSourceStack ?: emptySet(), currState.productTagSourceStack)
    }

    private fun updateBreadcrumb(productTagSourceStack: Set<ProductTagSource>) {
        if(viewModel.isUser) {

            if(productTagSourceStack.isNotEmpty() && productTagSourceStack.last() == ProductTagSource.Autocomplete) {
                showBreadcrumb(false)
                return
            }

            showBreadcrumb(true)

            /** Update the First Part */
            if(productTagSourceStack.isNotEmpty()) {
                val firstSource = productTagSourceStack.first()

                binding.icCcProductTagChevron1.setImage(IconUnify.CHEVRON_DOWN)
                binding.tvCcProductTagProductSource.text = getProductTagSourceText(firstSource)
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
            childFragmentManager.popBackStack()
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
                getStringArgument(EXTRA_PRODUCT_TAG_LIST),
                getStringArgument(EXTRA_SHOP_BADGE),
                getStringArgument(EXTRA_AUTHOR_ID),
                getStringArgument(EXTRA_AUTHOR_TYPE),
                getStringArgument(EXTRA_PAGE_SOURCE),
            )
        )
    }

    private fun getStringArgument(key: String): String {
        return arguments?.getString(key) ?: ""
    }

    private fun clickBreadcrumb() {
        analytic.clickBreadcrumb(viewModel.selectedTagSource == ProductTagSource.Shop)
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
    }

    private fun showCoachmarkGlobalTag(isShow: Boolean) {
        if(isShow) {
            coachmark = CoachMark2(activity as Context)

            coachmark?.showCoachMark(arrayListOf(
                CoachMark2Item(
                    binding.tvCcProductTagProductSource,
                    getString(R.string.content_creation_search_coachmark_header),
                    getString(R.string.content_creation_search_coachmark_desc),
                    CoachMark2.POSITION_BOTTOM
                )
            ))
        }
    }

    fun onNewIntent(source: ProductTagSource, query: String, shopId: String, componentId: String) {
        viewModel.submitAction(ProductTagAction.SetDataFromAutoComplete(source, query, shopId, componentId))
    }

    fun onBackPressed() {
        viewModel.submitAction(ProductTagAction.BackPressed)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    companion object {
        const val TAG = "ProductTagParentFragment"
        private const val EXTRA_PRODUCT_TAG_LIST = "EXTRA_PRODUCT_TAG_LIST"
        private const val EXTRA_SHOP_BADGE = "EXTRA_SHOP_BADGE"
        private const val EXTRA_AUTHOR_ID = "EXTRA_AUTHOR_ID"
        private const val EXTRA_AUTHOR_TYPE = "EXTRA_AUTHOR_TYPE"
        private const val EXTRA_PAGE_SOURCE = "EXTRA_PAGE_SOURCE"

        const val RESULT_PRODUCT_ID = "RESULT_PRODUCT_ID"
        const val RESULT_PRODUCT_NAME = "RESULT_PRODUCT_NAME"
        const val RESULT_PRODUCT_PRICE = "RESULT_PRODUCT_PRICE"
        const val RESULT_PRODUCT_IMAGE = "RESULT_PRODUCT_IMAGE"
        const val RESULT_PRODUCT_PRICE_ORIGINAL_FMT = "RESULT_PRODUCT_PRICE_ORIGINAL_FMT"
        const val RESULT_PRODUCT_PRICE_DISCOUNT_FMT = "RESULT_PRODUCT_PRICE_DISCOUNT_FMT"
        const val RESULT_PRODUCT_IS_DISCOUNT = "RESULT_PRODUCT_IS_DISCOUNT"

        private const val PAGE_SOURCE_FEED = "feed"
        private const val PAGE_SOURCE_PLAY = "play"

        fun findFragment(fragmentManager: FragmentManager): ProductTagParentFragment? {
            return fragmentManager.findFragmentByTag(TAG) as? ProductTagParentFragment
        }

        fun getFragmentWithFeedSource(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            productTagSource: String,
            shopBadge: String,
            authorId: String,
            authorType: String,
        ): ProductTagParentFragment {
            val oldInstance = findFragment(fragmentManager)
            return oldInstance ?: createFragment(fragmentManager, classLoader, productTagSource, shopBadge, authorId, authorType, PAGE_SOURCE_FEED)
        }

        fun getFragmentWithPlaySource(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            productTagSource: String,
            shopBadge: String,
            authorId: String,
            authorType: String,
        ): ProductTagParentFragment {
            val oldInstance = findFragment(fragmentManager)
            return oldInstance ?: createFragment(fragmentManager, classLoader, productTagSource, shopBadge, authorId, authorType, PAGE_SOURCE_PLAY)
        }

        private fun createFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            productTagSource: String,
            shopBadge: String,
            authorId: String,
            authorType: String,
            pageSource: String,
        ): ProductTagParentFragment {
            return (
                fragmentManager.fragmentFactory.instantiate(
                    classLoader,
                    ProductTagParentFragment::class.java.name
                ) as ProductTagParentFragment
            ).apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PRODUCT_TAG_LIST, productTagSource)
                    putSerializable(EXTRA_SHOP_BADGE, shopBadge)
                    putSerializable(EXTRA_AUTHOR_ID, authorId)
                    putSerializable(EXTRA_AUTHOR_TYPE, authorType)
                    putString(EXTRA_PAGE_SOURCE, pageSource)
                }
            }
        }
    }

    interface Listener {
        fun onCloseProductTag()
    }
}