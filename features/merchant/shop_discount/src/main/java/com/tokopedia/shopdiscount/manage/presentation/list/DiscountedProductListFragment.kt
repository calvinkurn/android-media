package com.tokopedia.shopdiscount.manage.presentation.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isVisibleOnTheScreen
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.cancel.CancelDiscountDialog
import com.tokopedia.shopdiscount.databinding.FragmentDiscountedProductListBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.manage.domain.entity.ProductData
import com.tokopedia.shopdiscount.manage.presentation.container.DiscountedProductManageFragment
import com.tokopedia.shopdiscount.manage.presentation.container.RecyclerViewScrollListener
import com.tokopedia.shopdiscount.manage_discount.presentation.view.activity.ShopDiscountManageActivity
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.more_menu.MoreMenuBottomSheet
import com.tokopedia.shopdiscount.product_detail.presentation.bottomsheet.ShopDiscountProductDetailBottomSheet
import com.tokopedia.shopdiscount.search.presentation.SearchProductActivity
import com.tokopedia.shopdiscount.select.presentation.SelectProductActivity
import com.tokopedia.shopdiscount.subsidy.model.mapper.ShopDiscountProgramInformationDetailMapper
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountManageProductSubsidyUiModel
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountProgramInformationDetailUiModel
import com.tokopedia.shopdiscount.subsidy.presentation.bottomsheet.ShopDiscountOptOutSingleProductSubsidyBottomSheet
import com.tokopedia.shopdiscount.subsidy.presentation.bottomsheet.ShopDiscountOptOutMultipleProductSubsidyBottomSheet
import com.tokopedia.shopdiscount.subsidy.presentation.bottomsheet.ShopDiscountSubsidyOptOutReasonBottomSheet
import com.tokopedia.shopdiscount.subsidy.presentation.bottomsheet.ShopDiscountSubsidyProgramInformationBottomSheet
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.shopdiscount.utils.constant.ZERO
import com.tokopedia.shopdiscount.utils.extension.*
import com.tokopedia.shopdiscount.utils.layoutmanager.NonPredictiveLinearLayoutManager
import com.tokopedia.shopdiscount.utils.paging.BaseSimpleListFragment
import com.tokopedia.shopdiscount.utils.preference.SharedPreferenceDataStore
import com.tokopedia.shopdiscount.utils.tracker.ShopDiscountTracker
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class DiscountedProductListFragment : BaseSimpleListFragment<ProductAdapter, Product>() {

    companion object {
        private const val BUNDLE_KEY_DISCOUNT_STATUS_NAME = "status_name"
        private const val BUNDLE_KEY_DISCOUNT_STATUS_ID = "status_id"
        private const val BUNDLE_KEY_PRODUCT_COUNT = "product_count"
        private const val PAGE_SIZE = 10
        private const val MAX_PRODUCT_SELECTION = 5
        private const val ONE_PRODUCT = 1
        private const val SCROLL_DISTANCE_DELAY_IN_MILLIS: Long = 300
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/slash_price/empty_product_with_discount.png"

        @JvmStatic
        fun newInstance(
            discountStatusName: String,
            discountStatusId: Int,
            productCount: Int,
            onDiscountRemoved: (Int, Int) -> Unit = { _, _ -> }
        ): DiscountedProductListFragment {
            val fragment = DiscountedProductListFragment()
            fragment.arguments = Bundle().apply {
                putString(BUNDLE_KEY_DISCOUNT_STATUS_NAME, discountStatusName)
                putInt(BUNDLE_KEY_DISCOUNT_STATUS_ID, discountStatusId)
                putInt(BUNDLE_KEY_PRODUCT_COUNT, productCount)
            }
            fragment.onDiscountRemoved = onDiscountRemoved
            return fragment
        }
    }

    private var optOutSuccessMessage: String = ""
    private val discountStatusName by lazy {
        arguments?.getString(BUNDLE_KEY_DISCOUNT_STATUS_NAME).orEmpty()
    }

    private val discountStatusId by lazy {
        arguments?.getInt(BUNDLE_KEY_DISCOUNT_STATUS_ID).orZero()
    }

    private val productCount by lazy {
        arguments?.getInt(BUNDLE_KEY_PRODUCT_COUNT).orZero()
    }

    private var binding by autoClearedNullable<FragmentDiscountedProductListBinding>()

    private var listParentProductIdWithSubsidy: MutableList<String> = mutableListOf()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var tracker: ShopDiscountTracker

    @Inject
    lateinit var preferenceDataStore: SharedPreferenceDataStore

    private var coachMarkSubsidyInfo: CoachMark2? = null

    private val loaderDialog by lazy { LoaderDialog(requireActivity()) }
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(DiscountedProductListViewModel::class.java) }
    private var onDiscountRemoved: (Int, Int) -> Unit = { _, _ -> }
    private var onSwipeRefresh: () -> Unit = {}
    private var isFirstLoad = true

    private val productAdapter by lazy {
        ProductAdapter(
            onProductClicked,
            onProductImageClicked,
            onUpdateDiscountClicked,
            onOverflowMenuClicked,
            onVariantInfoClicked,
            onProductSelectionChange,
            onSubsidyInformationClicked
        )
    }
    private var onScrollDown: () -> Unit = {}
    private var onScrollUp: () -> Unit = {}

    override fun getScreenName(): String =
        DiscountedProductListFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscountedProductListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentToUnifyBgColor()
        setupView()
        observeProducts()
        observeDeleteDiscount()
        observeReserveProducts()
        observeManageProductSubsidyUiModelLiveData()
    }

    private fun observeManageProductSubsidyUiModelLiveData() {
        viewModel.manageProductSubsidyUiModelLiveData.observe(viewLifecycleOwner) {
            it?.let {
                dismissLoaderDialog()
                when (it) {
                    is Success -> {
                        onSuccessGetListProductSubsidy(it.data)
                    }
                    is Fail -> {
                        onErrorGetListProductSubsidy(it.throwable)
                    }
                }
            }
        }
    }

    private fun onErrorGetListProductSubsidy(throwable: Throwable) {
        binding?.root showError throwable
    }

    private fun onSuccessGetListProductSubsidy(data: ShopDiscountManageProductSubsidyUiModel) {
        when(data.mode){
            ShopDiscountManageDiscountMode.DELETE, ShopDiscountManageDiscountMode.UPDATE -> {
                if(data.getTotalProductWithSubsidy() == Int.ONE){
                    showOptOutSingleProductSubsidyBottomSheet(data)
                } else {
                    showOptOutMultipleProductSubsidyBottomSheet(data)
                }
            }
            ShopDiscountManageDiscountMode.OPT_OUT_SUBSIDY -> {
                showBottomSheetOptOutReason(data)
            }
        }
    }

    private fun setupView() {
        setupSearchBar()
        setupMultiSelection()
        setupScrollListener()
        setupButton()
        setupTabChangeListener()
        initCoachMark()
    }

    private fun initCoachMark() {
        coachMarkSubsidyInfo = context?.let { CoachMark2(it) }
    }

    private fun setupSearchBar() {
        binding?.run {
            searchBar.searchBarPlaceholder =
                String.format(getString(R.string.sd_search_at), discountStatusName)
            searchBar.searchBarTextField.isFocusable = false
            searchBar.searchBarTextField.setOnClickListener { navigateToSearchProductPage() }
            searchBar.setOnClickListener { navigateToSearchProductPage() }
        }
    }

    private fun setupScrollListener() {
        binding?.run {
            recyclerView.addOnScrollListener(
                RecyclerViewScrollListener(
                    onScrollDown = {
                        onDelayFinished {
                            onScrollDown()
                            handleScrollDownEvent()
                            checkShouldShowCoachMarkSubsidy()
                        }
                    },
                    onScrollUp = {
                        onDelayFinished {
                            onScrollUp()
                            handleScrollUpEvent()
                            checkShouldShowCoachMarkSubsidy()
                        }
                    }
                )
            )
            imgScrollUp.setOnClickListener { recyclerView.smoothSnapToPosition(0) }
        }
    }

    private fun checkShouldShowCoachMarkSubsidy() {
        val layoutManager = recyclerView?.layoutManager as? LinearLayoutManager
        val startIndex = layoutManager?.findFirstVisibleItemPosition().orZero()
        val endIndex = layoutManager?.findLastVisibleItemPosition().orZero()
        for (i in startIndex..endIndex) {
            val product = adapter?.getItems()?.getOrNull(i)
            if (product?.isSubsidy == true) {
                val viewHolder = recyclerView?.findViewHolderForAdapterPosition(i)
                if (viewHolder is ProductViewHolder) {
                    val anchoredView = viewHolder.itemView.findViewById<View>(R.id.text_subsidy_status)
                    anchoredView.isVisibleOnTheScreen(
                        onViewVisible = {
                            showCoachMarkSubsidyInfo(anchoredView, product)
                        },
                        onViewNotVisible = {
                            if(coachMarkSubsidyInfo?.isShowing == true) {
                                coachMarkSubsidyInfo?.dismissCoachMark()
                            }
                        }
                    )
                }
                break
            }
        }
    }

    private fun setupButton() {
        binding?.run {
            btnCreateDiscount.setOnClickListener {
                tracker.sendClickAddProductEvent()
                SelectProductActivity.start(requireActivity(), discountStatusId)
            }
            btnBulkManage.setOnClickListener {
                configBulkManageProduct()
            }
            btnBulkDelete.setOnClickListener {
                configBulkDeleteProduct()
            }
            btnBulkOptOut.setOnClickListener {
                sendClickOptOutSubsidyBulkTracker()
                configBulkOptOutProduct()
            }
        }
    }

    private fun sendClickOptOutSubsidyBulkTracker() {
        val listSelectedProduct = viewModel.getSelectedProducts()
        tracker.sendClickOptOutSubsidyBulkEvent(
            listSelectedProduct.size,
            listSelectedProduct.map { it.id }
        )
    }

    private fun configBulkOptOutProduct() {
        getListProductSubsidy(
            viewModel.getSelectedProductIds(),
            discountStatusId,
            ShopDiscountManageDiscountMode.OPT_OUT_SUBSIDY
        )
    }

    private fun showBottomSheetOptOutReason(data: ShopDiscountManageProductSubsidyUiModel) {
        val bottomSheet = ShopDiscountSubsidyOptOutReasonBottomSheet.newInstance(data)
        bottomSheet.setOnDismissBottomSheetAfterFinishActionListener { dataModel, optOutSuccessMessage ->
            onOptOutProductSubsidyBottomSheetSuccess(data, optOutSuccessMessage)
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun configBulkDeleteProduct() {
        val selectedProductIds = viewModel.getSelectedProductIds()
        if (viewModel.anySubsidyOnSelectedProducts()) {
            getListProductSubsidy(
                selectedProductIds,
                discountStatusId,
                ShopDiscountManageDiscountMode.DELETE
            )
        } else {
            displayBulkDeleteConfirmationDialog()
        }
    }

    private fun getListProductSubsidy(
        selectedProductIds: List<String>,
        discountStatusId: Int,
        mode: String
    ) {
        showLoaderDialog()
        viewModel.getListProductDetailForManageSubsidy(
            selectedProductIds,
            discountStatusId,
            mode
        )
    }

    private fun configBulkManageProduct() {
        val selectedProductIds = viewModel.getSelectedProductIds()
        if (viewModel.anySubsidyOnSelectedProducts()) {
            getListProductSubsidy(
                selectedProductIds,
                discountStatusId,
                ShopDiscountManageDiscountMode.UPDATE
            )
        } else {
            reserveProduct(viewModel.getRequestId(), selectedProductIds)
        }
    }

    private fun setupTabChangeListener() {
        val listener = object : DiscountedProductManageFragment.TabChangeListener {
            override fun onTabChanged() {
                handleEmptyState(productCount)
            }
        }
        (parentFragment as? DiscountedProductManageFragment)?.setTabChangeListener(listener)
    }

    private fun observeProducts() {
        viewModel.products.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    displayProducts(it.data)
                    viewModel.setTotalProduct(it.data.totalProduct)

                    if (!viewModel.isOnMultiSelectMode()) {
                        binding?.tpgTotalProduct?.text =
                            String.format(
                                getString(R.string.sd_total_product),
                                it.data.totalProduct
                            )
                    }

                    binding?.swipeRefresh?.isRefreshing = false
                }

                is Fail -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    binding?.root showError it.throwable
                    binding?.searchBar?.gone()
                }
            }
        }
    }

    private fun observeDeleteDiscount() {
        viewModel.deleteDiscount.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    dismissLoaderDialog()
                    handleDeleteDiscountResult(it.data)
                }

                is Fail -> {
                    dismissLoaderDialog()
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun observeReserveProducts() {
        viewModel.reserveProduct.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val isReservationSuccess = it.data
                    if (isReservationSuccess) {
                        redirectToUpdateDiscountPage()
                    } else {
                        dismissLoaderDialog()
                        binding?.btnBulkManage?.isLoading = false
                        binding?.root showError getString(R.string.sd_error_reserve_product)
                    }
                }

                is Fail -> {
                    dismissLoaderDialog()
                    binding?.btnBulkManage?.isLoading = false
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun reserveProduct(requestId: String, productIds: List<String>) {
        binding?.btnBulkManage?.isLoading = true
        binding?.btnBulkManage?.loadingText = getString(R.string.sd_please_wait)
        viewModel.reserveProduct(requestId, productIds)
    }

    private fun displayProducts(data: ProductData) {
        if (data.totalProduct == ZERO) {
            handleEmptyState(data.totalProduct)
        } else {
            if (isFirstLoad) {
                binding?.searchBar?.visible()
                binding?.tpgMultiSelect?.visible()
            }
            renderList(data.products, data.products.size == getPerPage())
        }

        isFirstLoad = false
    }

    private fun handleDeleteDiscountResult(isDeletionSuccess: Boolean) {
        if (isDeletionSuccess) {
            val deletionWording = if (viewModel.isOnMultiSelectMode()) {
                val deletedProductCount = viewModel.getSelectedProductCount()
                String.format(getString(R.string.sd_bulk_discount_deleted), deletedProductCount)
            } else {
                getString(R.string.sd_discount_deleted)
            }

            val updatedTotalProduct: Int
            if (viewModel.isOnMultiSelectMode()) {
                val listProductIdSafeToDelete =
                    viewModel.getSelectedProductIds().toMutableList().apply {
                        removeAll(
                            listParentProductIdWithSubsidy
                        )
                    }
                productAdapter.bulkDelete(listProductIdSafeToDelete)
                updatedTotalProduct = viewModel.getTotalProduct() - listProductIdSafeToDelete.size
            } else {
                updatedTotalProduct =
                    if (viewModel.getSelectedProduct()?.id !in listParentProductIdWithSubsidy) {
                        productAdapter.delete(viewModel.getSelectedProduct() ?: return)
                        viewModel.getTotalProduct() - ONE_PRODUCT
                    } else {
                        viewModel.getTotalProduct()
                    }
            }
            listParentProductIdWithSubsidy.clear()
            binding?.recyclerView showToaster deletionWording
            binding?.tpgTotalProduct?.text =
                String.format(getString(R.string.sd_total_product), updatedTotalProduct)
            binding?.cardViewMultiSelect?.gone()
            viewModel.removeAllProductFromSelection()
            viewModel.setTotalProduct(updatedTotalProduct)
            viewModel.setInMultiSelectMode(false)
            viewModel.setDisableProductSelection(false)
            disableMultiSelect()
            onDiscountRemoved(discountStatusId, updatedTotalProduct)
            handleEmptyState(updatedTotalProduct)
        } else {
            binding?.root showError getString(R.string.sd_error_delete_discount)
        }
    }

    private fun handleScrollDownEvent() {
        binding?.searchBar.slideDown()

        if (viewModel.isOnMultiSelectMode()) {
            binding?.imgScrollUp.slideDown()
        } else {
            binding?.cardViewCreateDiscount.slideDown()
            binding?.imgScrollUp.slideUp()
        }
    }

    private fun handleScrollUpEvent() {
        binding?.searchBar.slideUp()
        binding?.imgScrollUp.slideDown()

        if (!viewModel.isOnMultiSelectMode()) {
            binding?.cardViewCreateDiscount.slideUp()
        }
    }

    private fun displayError(errorMessage: String) {
        binding?.run {
            globalError.visible()
            globalError.setType(GlobalError.SERVER_ERROR)
            globalError.setActionClickListener { loadInitialData() }
            root showError errorMessage
        }
    }

    private fun displayMoreMenuBottomSheet(product: Product) {
        val bottomSheet = MoreMenuBottomSheet.newInstance(product.productRule)
        bottomSheet.setOnDeleteMenuClicked {
            onDeleteOptionClicked(product)
        }
        bottomSheet.setOnOptOutSubsidyMenuClicked{
            onOptOutSubsidyOptionClicked(product)
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun sendClickOptOutSubsidyNonBulkTracker(product: Product) {
        tracker.sendClickOptOutSubsidyNonBulkEvent(product.id)
    }

    private fun onOptOutSubsidyOptionClicked(product: Product) {
        sendClickOptOutSubsidyNonBulkTracker(product)
        getListProductSubsidy(
            listOf(product.id),
            discountStatusId,
            ShopDiscountManageDiscountMode.OPT_OUT_SUBSIDY
        )
    }

    private fun onDeleteOptionClicked(product: Product) {
        if (product.isSubsidy) {
            getListProductSubsidy(
                listOf(product.id),
                discountStatusId,
                ShopDiscountManageDiscountMode.DELETE
            )
        } else {
            displayDeleteConfirmationDialog(product)
        }
    }

    private fun displayDeleteConfirmationDialog(product: Product) {
        val title = getString(R.string.sd_delete_confirmation_title)
        val dialog = CancelDiscountDialog(requireContext())
        dialog.setOnDeleteConfirmed {
            viewModel.deleteDiscount(discountStatusId, listOf(product.id))
        }
        dialog.show(title)
    }

    fun setOnScrollDownListener(onScrollDown: () -> Unit = {}) {
        this.onScrollDown = onScrollDown
    }

    fun setOnScrollUpListener(onScrollUp: () -> Unit = {}) {
        this.onScrollUp = onScrollUp
    }

    fun setOnSwipeRefresh(onSwipeRefresh: () -> Unit) {
        this.onSwipeRefresh = onSwipeRefresh
    }

    private fun showProductDetailBottomSheet(product: Product, position: Int) {
        val bottomSheet = ShopDiscountProductDetailBottomSheet.newInstance(
            product.id,
            product.name,
            discountStatusId,
            position
        )
        bottomSheet.setListener(object : ShopDiscountProductDetailBottomSheet.Listener {
            override fun deleteParentProduct(productId: String) {
                showToaster(getString(R.string.sd_discount_deleted))
                deleteSingleProduct(productId)
            }
        })
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showToaster(message: String) {
        binding?.root showToaster message
    }

    private fun deleteSingleProduct(productId: String) {
        val totalCountProduct = viewModel.getTotalProduct() - ONE_PRODUCT
        val getDeletedProduct = adapter?.getProductBasedOnId(productId)
        getDeletedProduct?.let {
            productAdapter.delete(it)
            onDiscountRemoved(discountStatusId, totalCountProduct)
        }
    }

    private fun redirectToProductDetailPage(product: Product) {
        val imageUrl = arrayListOf(product.imageUrl)
        val intent = ImagePreviewActivity.getCallingIntent(
            context = requireActivity(),
            imageUris = imageUrl,
            disableDownloadButton = true
        )
        startActivity(intent)
    }

    private fun setupMultiSelection() {
        binding?.run {
            tpgMultiSelect.setOnClickListener {
                viewModel.setRequestId(generateRequestId())
                viewModel.removeAllProductFromSelection()
                viewModel.setInMultiSelectMode(true)
                enableMultiSelect()
                binding?.tpgTotalProduct?.text =
                    String.format(getString(R.string.sd_selected_product_counter), ZERO)
                binding?.imgScrollUp?.gone()
            }
            tpgCancelMultiSelect.setOnClickListener {
                viewModel.removeAllProductFromSelection()
                viewModel.setInMultiSelectMode(false)
                viewModel.setDisableProductSelection(false)
                disableMultiSelect()
                binding?.tpgTotalProduct?.text =
                    String.format(getString(R.string.sd_total_product), viewModel.getTotalProduct())
            }
        }
    }

    private fun enableMultiSelect() {
        binding?.cardViewCreateDiscount.slideDown()
        binding?.tpgMultiSelect?.gone()
        binding?.tpgCancelMultiSelect?.visible()

        val currentItems = adapter?.getItems() ?: emptyList()
        val enabledMultiSelect = viewModel.enableMultiSelect(currentItems)
        productAdapter.updateAll(enabledMultiSelect)
    }

    private fun disableMultiSelect() {
        binding?.tpgMultiSelect?.visible()
        binding?.tpgCancelMultiSelect?.gone()

        val currentItems = adapter?.getItems() ?: emptyList()
        val disabledMultiSelect = viewModel.disableMultiSelect(currentItems)
        productAdapter.updateAll(disabledMultiSelect)
        binding?.cardViewMultiSelect?.gone()
        binding?.cardViewCreateDiscount.slideUp()
    }

    private fun disableProductSelection(products: List<Product>) {
        val toBeDisabledProducts = viewModel.disableProducts(products)
        adapter?.refresh(toBeDisabledProducts)
    }

    private fun enableProductSelection(products: List<Product>) {
        val toBeEnabledProducts = viewModel.enableProduct(products)
        adapter?.refresh(toBeEnabledProducts)
    }

    private fun displayBulkDeleteConfirmationDialog() {
        val title = getString(R.string.sd_bulk_delete_confirmation_title)
        val dialog = CancelDiscountDialog(requireContext())
        val toBeDeletedProductIds = viewModel.getSelectedProductIds()
        val dialogTitle = String.format(title, toBeDeletedProductIds.size)

        dialog.setOnDeleteConfirmed {
            showLoaderDialog()
            viewModel.deleteDiscount(discountStatusId, toBeDeletedProductIds)
        }

        dialog.show(dialogTitle)
    }

    private val onProductSelectionChange: (Product, Boolean) -> Unit =
        { selectedProduct, isSelected ->
            if (isSelected) {
                viewModel.addProductToSelection(selectedProduct)
            } else {
                viewModel.removeProductFromSelection(selectedProduct)
            }

            val updatedProduct = selectedProduct.copy(isCheckboxTicked = isSelected)
            adapter?.update(selectedProduct, updatedProduct)

            val items = adapter?.getItems() ?: emptyList()
            val selectedProductCount = viewModel.getSelectedProductCount()

            val shouldDisableSelection = selectedProductCount >= MAX_PRODUCT_SELECTION
            viewModel.setDisableProductSelection(shouldDisableSelection)

            binding?.cardViewMultiSelect?.isVisible = selectedProductCount > 0
            binding?.cardViewCreateDiscount?.isVisible = selectedProductCount == 0

            if (selectedProductCount == ZERO) {
                binding?.cardViewCreateDiscount?.gone()
                binding?.tpgTotalProduct?.text =
                    String.format(getString(R.string.sd_total_product), viewModel.getTotalProduct())
            } else {
                binding?.tpgTotalProduct?.text =
                    String.format(
                        getString(R.string.sd_selected_product_counter),
                        selectedProductCount
                    )
            }

            if (shouldDisableSelection) {
                disableProductSelection(items)
            } else {
                enableProductSelection(items)
            }
        }

    private val onSubsidyInformationClicked: (Product) -> Unit = { product ->
        sendSlashPriceClickSubsidyInformationTracker(product)
        showSubsidyProgramInformationBottomSheet(product)
    }

    private fun sendSlashPriceClickSubsidyInformationTracker(product: Product) {
        tracker.sendSlashPriceClickSubsidyInformationListProductEvent(
            product.hasVariant,
            product.id
        )
    }

    private fun showCoachMarkSubsidyInfo(view: View, product: Product) {
        if (!preferenceDataStore.isCoachMarkSubsidyInfoOnParentAlreadyShown()) {
            val coachMarks = ArrayList<CoachMark2Item>()
            val coachMarkDesc = if (product.hasVariant) {
                getString(R.string.sd_subsidy_coach_mark_variant_desc)
            } else {
                getString(R.string.sd_subsidy_coach_mark_non_variant_desc)
            }
            coachMarks.add(
                CoachMark2Item(
                    view,
                    "",
                    coachMarkDesc
                )
            )
            coachMarkSubsidyInfo?.showCoachMark(coachMarks)
            sendSlashPriceSubsidyImpressionCoachMarkTracker(product)
            preferenceDataStore.setCoachMarkSubsidyInfoOnParentAlreadyShown()
        }
    }

    private fun sendSlashPriceSubsidyImpressionCoachMarkTracker(product: Product) {
        tracker.sendImpressionSlashPriceSubsidyCoachMarkListProductEvent(
            product.hasVariant,
            product.id
        )
    }

    private fun showSubsidyProgramInformationBottomSheet(product: Product) {
        parentFragment?.childFragmentManager?.let {
            val programDetailInfo = getProgramDetailInfoModel(product)
            val bottomSheet = ShopDiscountSubsidyProgramInformationBottomSheet.newInstance(
                programDetailInfo
            )
            bottomSheet.show(it, bottomSheet.tag)
        }
    }

    private fun getProgramDetailInfoModel(product: Product): ShopDiscountProgramInformationDetailUiModel {
        return ShopDiscountProgramInformationDetailMapper.map(
            isVariant = product.hasVariant,
            formattedOriginalPrice = product.formattedOriginalMaxPrice,
            formattedFinalDiscountedPrice = product.formattedDiscountMaxPrice,
            formattedFinalDiscountedPercentage = product.formattedDiscountMaxPercentage,
            mainStock = product.totalStock,
            maxOrder = product.maxOrder,
            productId = product.id,
            isBottomSheet = false,
            subsidyInfo = product.subsidyInfo
        )
    }

    private val onUpdateDiscountClicked: (Product) -> Unit = { product ->
        if (product.isSubsidy) {
            getListProductSubsidy(
                listOf(product.id),
                discountStatusId,
                ShopDiscountManageDiscountMode.UPDATE
            )
        } else {
            showLoaderDialog()
            viewModel.setSelectedProduct(product)
            val requestId = generateRequestId()
            viewModel.setRequestId(requestId)
            reserveProduct(requestId, listOf(product.id))
        }
    }

    private fun showOptOutMultipleProductSubsidyBottomSheet(
        data: ShopDiscountManageProductSubsidyUiModel,
    ) {
        val bottomSheet = ShopDiscountOptOutMultipleProductSubsidyBottomSheet.newInstance(data)
        bottomSheet.setOnDismissBottomSheetAfterFinishActionListener { dataModel, optOutSuccessMessage ->
            onOptOutProductSubsidyBottomSheetSuccess(
                data,
                optOutSuccessMessage
            )
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun onOptOutProductSubsidyBottomSheetSuccess(
        data: ShopDiscountManageProductSubsidyUiModel,
        optOutSuccessMessage: String
    ) {
        this.optOutSuccessMessage = optOutSuccessMessage
        when(data.mode){
            ShopDiscountManageDiscountMode.DELETE -> {
                showLoaderDialog()
                listParentProductIdWithSubsidy = data.getListProductParentIdWithSubsidyVariant().toMutableList()
                viewModel.deleteDiscount(discountStatusId, data.getListProductIdVariantNonSubsidy())
            }
            ShopDiscountManageDiscountMode.UPDATE -> {
                if(data.isAllSelectedProductFullSubsidy()){
                    binding?.recyclerView showToaster "Produk berhasil dihapus dari Diskon Toko"
                    loadInitialData()
                }else {
                    val requestId = generateRequestId()
                    viewModel.setRequestId(requestId)
                    reserveProduct(requestId, data.getListProductParentIdWithNonSubsidyVariant())
                }
            }
            ShopDiscountManageDiscountMode.OPT_OUT_SUBSIDY -> {
                binding?.recyclerView showToaster optOutSuccessMessage
                binding?.cardViewMultiSelect?.gone()
                viewModel.removeAllProductFromSelection()
                viewModel.setInMultiSelectMode(false)
                viewModel.setDisableProductSelection(false)
                disableMultiSelect()
                loadInitialData()
            }
        }
    }

    private fun showOptOutSingleProductSubsidyBottomSheet(
        data: ShopDiscountManageProductSubsidyUiModel
    ) {
        val bottomSheet = ShopDiscountOptOutSingleProductSubsidyBottomSheet.newInstance(data)
        bottomSheet.setOnDismissBottomSheetAfterFinishActionListener { dataModel, optOutSuccessMessage ->
            onOptOutProductSubsidyBottomSheetSuccess(
                data,
                optOutSuccessMessage
            )
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private val onProductImageClicked: (Product) -> Unit = { product ->
        viewModel.setSelectedProduct(product)
        guard(product.disableClick) {
            redirectToProductDetailPage(product)
        }
    }

    private val onProductClicked: (Product, Int) -> Unit = { product, position ->
        viewModel.setSelectedProduct(product)
        guard(product.disableClick) {
            showProductDetailBottomSheet(product, position)
        }
    }

    private val onVariantInfoClicked: (Product, Int) -> Unit = { product, position ->
        viewModel.setSelectedProduct(product)
        guard(product.disableClick) {
            showProductDetailBottomSheet(product, position)
        }
    }

    private val onOverflowMenuClicked: (Product) -> Unit = { product ->
        viewModel.setSelectedProduct(product)
        guard(product.disableClick) {
            displayMoreMenuBottomSheet(product)
        }
    }

    private fun guard(disableClick: Boolean, block: () -> Unit) {
        if (disableClick) {
            Toaster.build(
                binding?.recyclerView ?: return,
                getString(R.string.sd_max_product_selection_reached),
                Snackbar.LENGTH_SHORT,
                Toaster.TYPE_ERROR,
                getString(R.string.sd_ok)
            ).show()
        } else {
            block()
        }
    }

    override fun createAdapter(): ProductAdapter {
        return productAdapter
    }

    override fun getRecyclerView(view: View): RecyclerView? {
        return if (activity == null) {
            binding?.recyclerView
        } else {
            binding?.recyclerView?.apply {
                layoutManager = NonPredictiveLinearLayoutManager(requireActivity())
            }
        }
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return binding?.swipeRefresh
    }

    override fun getPerPage(): Int {
        return PAGE_SIZE
    }

    override fun addElementToAdapter(list: List<Product>) {
        adapter?.addData(list)
    }

    override fun loadData(page: Int) {
        binding?.globalError?.gone()

        viewModel.getSlashPriceProducts(
            page,
            discountStatusId,
            EMPTY_STRING,
            viewModel.isOnMultiSelectMode(),
            viewModel.shouldDisableProductSelection()
        )
    }

    override fun clearAdapterData() {
        adapter?.clearData()
    }

    override fun onShowLoading() {
        adapter?.showLoading()
    }

    override fun onHideLoading() {
        adapter?.hideLoading()
    }

    override fun onDataEmpty() {
    }

    override fun onGetListError(message: String) {
        displayError(message)
    }

    private fun generateRequestId(): String {
        return userSession.shopId + Date().time
    }

    private fun redirectToUpdateDiscountPage() {
        binding?.btnBulkManage?.isLoading = false
        dismissLoaderDialog()
        ShopDiscountManageActivity.start(
            requireActivity(),
            viewModel.getRequestId(),
            discountStatusId,
            ShopDiscountManageDiscountMode.UPDATE,
            optOutSuccessMessage
        )
    }

    override fun onSwipeRefreshPulled() {
        onSwipeRefresh()
    }

    private fun handleEmptyState(totalProduct: Int) {
        if (totalProduct == ZERO) {
            showEmptyState(discountStatusId)
        } else {
            hideEmptyState()
        }
    }

    private fun showEmptyState(discountStatusId: Int) {
        guardFragmentIsAttached {
            val title = if (discountStatusId == DiscountStatus.PAUSED) {
                getString(R.string.sd_no_paused_discount_title)
            } else {
                getString(R.string.sd_no_discount_title)
            }

            val description = if (discountStatusId == DiscountStatus.PAUSED) {
                getString(R.string.sd_no_paused_discount_description)
            } else {
                getString(R.string.sd_no_discount_description)
            }

            binding?.tpgTotalProduct?.gone()
            binding?.tpgMultiSelect?.gone()
            binding?.tpgCancelMultiSelect?.gone()
            binding?.searchBar?.gone()
            binding?.recyclerView?.gone()

            binding?.emptyState?.visible()
            binding?.emptyState?.setImageUrl(EMPTY_STATE_IMAGE_URL)
            binding?.emptyState?.setTitle(title)
            binding?.emptyState?.setDescription(description)
        }
    }

    private fun hideEmptyState() {
        binding?.emptyState?.gone()
    }

    private fun showLoaderDialog() {
        loaderDialog.setLoadingText(getString(R.string.sd_wait))
        loaderDialog.show()
    }

    private fun dismissLoaderDialog() {
        loaderDialog.dialog.dismiss()
    }

    private fun navigateToSearchProductPage() {
        SearchProductActivity.start(
            requireActivity(),
            discountStatusName,
            discountStatusId
        )
    }

    private fun onDelayFinished(block: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(SCROLL_DISTANCE_DELAY_IN_MILLIS)
            block()
        }
    }

    private fun guardFragmentIsAttached(operation: Context.() -> Unit) {
        if (isAdded && context != null) {
            operation(requireContext())
        }
    }
}
