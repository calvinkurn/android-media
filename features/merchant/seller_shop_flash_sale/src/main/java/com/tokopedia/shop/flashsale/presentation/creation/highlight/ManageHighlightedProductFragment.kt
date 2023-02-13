package com.tokopedia.shop.flashsale.presentation.creation.highlight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.FragmentSsfsManageHighlightedProductBinding
import com.tokopedia.shop.flashsale.common.constant.BundleConstant
import com.tokopedia.shop.flashsale.common.extension.doOnDelayFinished
import com.tokopedia.shop.flashsale.common.extension.setFragmentToUnifyBgColor
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.common.extension.showLoading
import com.tokopedia.shop.flashsale.common.extension.showToaster
import com.tokopedia.shop.flashsale.common.extension.slideDown
import com.tokopedia.shop.flashsale.common.extension.slideUp
import com.tokopedia.shop.flashsale.common.extension.smoothSnapToPosition
import com.tokopedia.shop.flashsale.common.extension.stopLoading
import com.tokopedia.shop.flashsale.common.preference.SharedPreferenceDataStore
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct
import com.tokopedia.shop.flashsale.domain.entity.ProductSubmissionResult
import com.tokopedia.shop.flashsale.domain.entity.enums.PageMode
import com.tokopedia.shop.flashsale.presentation.creation.highlight.adapter.HighlightedProductAdapter
import com.tokopedia.shop.flashsale.presentation.creation.highlight.bottomsheet.ManageHighlightedProductInfoBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.highlight.decoration.ProductListItemDecoration
import com.tokopedia.shop.flashsale.presentation.creation.rule.CampaignRuleActivity
import com.tokopedia.shop.flashsale.presentation.list.container.CampaignListActivity
import com.tokopedia.shop.flashsale.presentation.list.list.listener.RecyclerViewScrollListener
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ManageHighlightedProductFragment : BaseDaggerFragment() {

    companion object {
        private const val PAGE_SIZE = 10
        private const val ONE_PAGE = 1
        private const val ONE_PRODUCT = 1
        private const val FIRST_PAGE = 1
        private const val THIRD_STEP = 3
        private const val MAX_PRODUCT_SELECTION = 5
        private const val SCROLL_DISTANCE_DELAY_IN_MILLIS: Long = 100

        @JvmStatic
        fun newInstance(campaignId: Long, pageMode: PageMode): ManageHighlightedProductFragment {
            val fragment = ManageHighlightedProductFragment()
            fragment.arguments = Bundle().apply {
                putLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
                putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
            }
            return fragment
        }

    }

    private var binding by autoClearedNullable<FragmentSsfsManageHighlightedProductBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var preferenceDataStore: SharedPreferenceDataStore

    private val campaignId by lazy {
        arguments?.getLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID).orZero()
    }

    private val pageMode by lazy {
        arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) ?: PageMode.CREATE
    }

    private var isFirstLoad = true
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ManageHighlightedProductViewModel::class.java) }
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    private var coachMark : CoachMark2? = null

    private val productAdapter by lazy {
        HighlightedProductAdapter(onProductSelectionChange)
    }

    override fun getScreenName(): String = ManageHighlightedProductFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSsfsManageHighlightedProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        handlePageMode()
        setFragmentToUnifyBgColor()
        observeProducts()
        observeSubmitHighlightedProducts()
        observeSaveDraft()
        getProducts(FIRST_PAGE)
    }

    private fun setupView() {
        setupRecyclerView()
        setupButton()
        setupToolbar()
        setupSearchBar()
        setupScrollListener()
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.run {
            layoutManager = LinearLayoutManager(activity ?: return, LinearLayoutManager.VERTICAL, false)
            adapter = productAdapter

            val itemDecoration = ProductListItemDecoration(activity ?: return)
            addItemDecoration(itemDecoration)

            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    productAdapter.showLoading()
                    getProducts(page)
                }
            }
            addOnScrollListener(endlessRecyclerViewScrollListener ?: return)
        }
    }

    private fun handlePageMode() {
        if (pageMode == PageMode.UPDATE || pageMode == PageMode.DRAFT) {
            binding?.btnDraft?.gone()
        }
    }

    private fun setupSearchBar() {
        binding?.run {
            searchBar.searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    endlessRecyclerViewScrollListener?.resetState()
                    viewModel.setIsFirstLoad(false)
                    productAdapter.submit(emptyList())
                    binding?.groupNoSearchResult?.gone()
                    getProducts(FIRST_PAGE)
                    return@setOnEditorActionListener false
                }
                return@setOnEditorActionListener false
            }
            searchBar.clearListener = { clearSearchBar() }
        }
    }

    private fun setupToolbar() {
        binding?.run {
            header.setOnClickListener { activity?.onBackPressed() }
            header.addRightIcon(R.drawable.ic_sfs_info)
            header.rightIcons?.firstOrNull()?.setOnClickListener { ManageHighlightedProductInfoBottomSheet().show(childFragmentManager) }
            header.headerSubTitle = String.format(getString(R.string.sfs_placeholder_step_counter), THIRD_STEP)
        }
    }

    private fun setupButton() {
        binding?.run {
            btnProceed.setOnClickListener {
                binding?.btnProceed?.showLoading()
                viewModel.submitHighlightedProducts(campaignId, productAdapter.getItems())
            }
            btnDraft.setOnClickListener {
                binding?.btnDraft.showLoading()
                viewModel.saveDraft(campaignId, productAdapter.getItems())
            }
        }
    }

    private fun setupScrollListener() {
        binding?.run {
            imgScrollUp.setOnClickListener { recyclerView.smoothSnapToPosition(0)  }
            recyclerView.addOnScrollListener(
                RecyclerViewScrollListener(
                    onScrollDown = {
                        doOnDelayFinished(SCROLL_DISTANCE_DELAY_IN_MILLIS) {
                            handleScrollDownEvent()
                        }
                    },
                    onScrollUp = {
                        doOnDelayFinished(SCROLL_DISTANCE_DELAY_IN_MILLIS) {
                            handleScrollUpEvent()
                        }
                    }
                )
            )
        }
    }


    private fun observeProducts() {
        viewModel.products.observe(viewLifecycleOwner) { result ->
            binding?.loader?.gone()

            when (result) {
                is Success -> {
                    handleCoachMark(result.data)
                    isFirstLoad = false
                    showContent()
                    handleProducts(result.data)
                }
                is Fail -> {
                    hideContent()
                    binding?.cardView showError result.throwable
                }
            }
        }
    }

    private fun observeSubmitHighlightedProducts() {
        viewModel.submit.observe(viewLifecycleOwner) { result ->
            binding?.btnProceed.stopLoading()

            when (result) {
                is Success -> {
                    handleProductSubmissionResult(result.data)
                }
                is Fail -> {
                    binding?.cardView showError result.throwable
                }
            }
        }
    }

    private fun observeSaveDraft() {
        viewModel.saveDraft.observe(viewLifecycleOwner) { result ->
            binding?.btnDraft.stopLoading()

            when (result) {
                is Success -> {
                    handleSaveDraftResult(result.data)
                }
                is Fail -> {
                    binding?.cardView showError result.throwable
                }
            }
        }
    }

    private fun handleProducts(data: List<HighlightableProduct>) {
        val products = productAdapter.getItems()
        val hasData = products.size.isMoreThanZero()

        if (data.isEmpty() && !hasData) {
            binding?.groupNoSearchResult?.visible()
            binding?.recyclerView?.gone()

        } else {
            renderList(data, data.size >= PAGE_SIZE)
            binding?.recyclerView?.visible()
            binding?.groupNoSearchResult?.gone()
            refreshButton()
        }

        refreshCounter()
    }


    private val onProductSelectionChange: (HighlightableProduct, Boolean) -> Unit =
        { selectedProduct, isSelected ->
            coachMark?.dismissCoachMark()
            val currentSelectedProductCount = viewModel.getSelectedProductIds().size
            handleAddProductToSelection(currentSelectedProductCount, selectedProduct, isSelected)
        }

    private fun handleAddProductToSelection(
        currentSelectedProductCount: Int,
        selectedProduct: HighlightableProduct,
        isSelected: Boolean
    ) {
        when {
            isSelected && currentSelectedProductCount == (MAX_PRODUCT_SELECTION - ONE_PRODUCT) -> {
                binding?.cardView showToaster getString(R.string.sfs_successfully_highlighted)
                viewModel.addProductIdToSelection(selectedProduct)
                selectProduct()
            }
            isSelected && currentSelectedProductCount < MAX_PRODUCT_SELECTION -> {
                binding?.cardView showToaster getString(R.string.sfs_successfully_highlighted)
                viewModel.addProductIdToSelection(selectedProduct)
                selectProduct()
            }
            !isSelected -> {
                viewModel.removeProductIdFromSelection(selectedProduct)
                unselectProduct(selectedProduct)
            }
        }

        refreshButton()
    }

    private fun selectProduct() {
        val products = productAdapter.getItems()
        val updatedProducts = viewModel.markAsSelected(products)
        productAdapter.submit(updatedProducts)
        refreshCounter()
    }

    private fun unselectProduct(selectedProduct: HighlightableProduct) {
        val products = productAdapter.getItems()
        val updatedProducts = viewModel.markAsUnselected(selectedProduct, products)
        productAdapter.submit(updatedProducts)
        refreshCounter()
    }

    private fun refreshCounter() {
        val selectedProductCount = viewModel.getSelectedProductIds().size
        binding?.tpgSelectedProductCount?.isVisible = selectedProductCount.isMoreThanZero()
        binding?.tpgSelectedProductCount?.text = String.format(getString(R.string.sfs_placeholder_selected_product_count), selectedProductCount)
    }

    private fun clearSearchBar() {
        endlessRecyclerViewScrollListener?.resetState()
        doFreshSearch()
        viewModel.setIsFirstLoad(false)
    }

    private fun refreshButton() {
        val selectedProductCount = viewModel.getSelectedProductIds().size
        binding?.btnProceed?.isEnabled = selectedProductCount > Int.ZERO
    }

    private fun handleScrollDownEvent() {
        binding?.searchBar?.slideDown()
        binding?.cardView.slideDown()
        binding?.imgScrollUp.slideUp()

        if (viewModel.getSelectedProductIds().size.isMoreThanZero()) {
            binding?.tpgSelectedProductCount.slideDown()
        }

    }

    private fun handleScrollUpEvent() {
        binding?.searchBar?.slideUp()
        binding?.cardView.slideUp()
        binding?.imgScrollUp.slideDown()

        if (viewModel.getSelectedProductIds().size.isMoreThanZero()) {
            binding?.tpgSelectedProductCount.slideUp()
        }

    }

    private fun doFreshSearch() {
        productAdapter.submit(emptyList())
        binding?.groupNoSearchResult?.gone()
        viewModel.getProducts(campaignId, "", PAGE_SIZE, offset = 0)
    }

    private fun renderList(list: List<HighlightableProduct>, hasNextPage: Boolean) {
        productAdapter.hideLoading()

        val allItems = productAdapter.getItems() + list
        val allItemsSorted = allItems.sortedByDescending { it.isSelected }
        productAdapter.submit(allItemsSorted)

        endlessRecyclerViewScrollListener?.updateStateAfterGetData()
        endlessRecyclerViewScrollListener?.setHasNextPage(hasNextPage)

        if (productAdapter.itemCount.orZero() < PAGE_SIZE && hasNextPage) {
            endlessRecyclerViewScrollListener?.loadMoreNextPage()
        }
    }

    private fun getProducts(page: Int) {
        val offset = if (page == FIRST_PAGE) {
            0
        } else {
            (page - ONE_PAGE) * PAGE_SIZE
        }

        val searchKeyword = binding?.searchBar?.searchBarTextField?.text.toString().trim()
        viewModel.getProducts(campaignId, searchKeyword, PAGE_SIZE, offset)
    }

    private fun showContent() {
        binding?.tpgSelectedProductCount?.visible()
        binding?.recyclerView?.visible()
        binding?.cardView?.visible()
        binding?.searchBar?.visible()
    }

    private fun hideContent() {
        binding?.tpgSelectedProductCount?.gone()
        binding?.recyclerView?.gone()
        binding?.cardView?.gone()
        binding?.searchBar?.gone()
    }

    private fun handleProductSubmissionResult(result: ProductSubmissionResult) {
        val isSuccess = result.isSuccess
        if (isSuccess) {
            CampaignRuleActivity.start(activity ?: return, campaignId, pageMode)
        } else {
            displayError(result)
        }
    }

    private fun handleSaveDraftResult(result: ProductSubmissionResult) {
        val isSuccess = result.isSuccess
        if (isSuccess) {
            routeToCampaignListPage()
        } else {
            displayError(result)
        }
    }

    private fun routeToCampaignListPage() {
        val context = context ?: return
        CampaignListActivity.start(context, isSaveDraft = true, previousPageMode = pageMode)
    }

    private fun displayError(result: ProductSubmissionResult) {
        val firstErrorMessage = result.failedProducts.firstOrNull()
        val errorMessage = firstErrorMessage?.message.orEmpty()
        val updatedErrorMessage = errorMessage.ifEmpty { result.errorMessage }
        binding?.cardView showError updatedErrorMessage
    }

    private fun handleCoachMark(products : List<HighlightableProduct>) {
        val hasProducts = products.isNotEmpty()
        val shouldShowCoachMark = !preferenceDataStore.isHighlightCampaignProductDismissed()

        if (isFirstLoad && hasProducts && shouldShowCoachMark) {
            showCoachMark()
        }
    }

    private fun showCoachMark() {
        binding?.recyclerView?.post {
            val firstProductView = binding?.recyclerView?.findViewHolderForAdapterPosition(0)?.itemView
            val firstProductSwitch = firstProductView?.findViewById<SwitchUnify>(R.id.switchUnify)

            coachMark = CoachMark2(firstProductSwitch?.context ?: return@post)
            coachMark?.onFinishListener = { preferenceDataStore.markHighlightCampaignProductComplete() }
            coachMark?.onDismissListener = { preferenceDataStore.markHighlightCampaignProductComplete() }
            coachMark?.showCoachMark(populateCoachMarkItems(firstProductSwitch), null)
        }
    }

    private fun populateCoachMarkItems(anchorView : View): ArrayList<CoachMark2Item> {
        return arrayListOf(
            CoachMark2Item(
                anchorView,
                anchorView.context.getString(R.string.sfs_highlight_product_first_coachmark),
                "",
                CoachMark2.POSITION_BOTTOM
            )
        )
    }
}