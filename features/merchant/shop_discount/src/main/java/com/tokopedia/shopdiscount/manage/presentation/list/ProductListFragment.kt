package com.tokopedia.shopdiscount.manage.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.cancel.CancelDiscountDialog
import com.tokopedia.shopdiscount.databinding.FragmentProductListBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.manage.domain.entity.ProductData
import com.tokopedia.shopdiscount.manage.presentation.container.RecyclerViewScrollListener
import com.tokopedia.shopdiscount.manage_discount.presentation.view.activity.ShopDiscountManageDiscountActivity
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.more_menu.MoreMenuBottomSheet
import com.tokopedia.shopdiscount.product_detail.presentation.bottomsheet.ShopDiscountProductDetailBottomSheet
import com.tokopedia.shopdiscount.search.presentation.SearchProductActivity
import com.tokopedia.shopdiscount.select.presentation.SelectProductActivity
import com.tokopedia.shopdiscount.utils.animator.ViewAnimator
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.shopdiscount.utils.constant.ZERO
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.extension.showToaster
import com.tokopedia.shopdiscount.utils.extension.smoothSnapToPosition
import com.tokopedia.shopdiscount.utils.paging.BaseSimpleListFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class ProductListFragment : BaseSimpleListFragment<ProductAdapter, Product>() {

    companion object {
        private const val BUNDLE_KEY_DISCOUNT_STATUS_NAME = "status_name"
        private const val BUNDLE_KEY_DISCOUNT_STATUS_ID = "status_id"
        private const val PAGE_SIZE = 10
        private const val MAX_PRODUCT_SELECTION = 5
        private const val ONE_PRODUCT = 1
        private const val PAGE_REDIRECTION_DELAY_IN_MILLIS : Long = 1000
        private const val SCROLL_DISTANCE_DELAY_IN_MILLIS: Long = 300
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/slash_price/empty_product_with_discount.png"

        @JvmStatic
        fun newInstance(
            discountStatusName : String,
            discountStatusId: Int,
            onDiscountRemoved: (Int, Int) -> Unit = { _, _ -> }
        ): ProductListFragment {
            val fragment = ProductListFragment()
            fragment.arguments = Bundle().apply {
                putString(BUNDLE_KEY_DISCOUNT_STATUS_NAME, discountStatusName)
                putInt(BUNDLE_KEY_DISCOUNT_STATUS_ID, discountStatusId)
            }
            fragment.onDiscountRemoved = onDiscountRemoved
            return fragment
        }

    }

    private val discountStatusName by lazy {
        arguments?.getString(BUNDLE_KEY_DISCOUNT_STATUS_NAME).orEmpty()
    }

    private val discountStatusId by lazy {
        arguments?.getInt(BUNDLE_KEY_DISCOUNT_STATUS_ID).orZero()
    }

    private var binding by autoClearedNullable<FragmentProductListBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession : UserSessionInterface

    @Inject
    lateinit var viewAnimator: ViewAnimator

    private val loaderDialog by lazy { LoaderDialog(requireActivity()) }
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ProductListViewModel::class.java) }
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
            onProductSelectionChange
        )
    }
    private var onScrollDown: () -> Unit = {}
    private var onScrollUp: () -> Unit = {}

    override fun getScreenName(): String = ProductListFragment::class.java.canonicalName.orEmpty()
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
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeProducts()
        observeDeleteDiscount()
        observeReserveProducts()
    }

    private fun setupView() {
        setupSearchBar()
        setupMultiSelection()
        setupScrollListener()
        setupButton()
    }


    private fun setupSearchBar() {
        binding?.run {
            searchBar.searchBarPlaceholder = String.format(getString(R.string.sd_search_at), discountStatusName)
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
                            hideView()
                        }
                    },
                    onScrollUp = {
                        onDelayFinished {
                            onScrollUp()
                            showView()
                        }
                    }
                )
            )
            imgScrollUp.setOnClickListener { recyclerView.smoothSnapToPosition(0) }
        }
    }

    private fun setupButton() {
        binding?.run {
            btnCreateDiscount.setOnClickListener {
                SelectProductActivity.start(requireActivity(), discountStatusId)
            }
            btnBulkManage.setOnClickListener {
                val selectedProductIds = viewModel.getSelectedProductIds()
                reserveProduct(viewModel.getRequestId(), selectedProductIds)
            }
            btnBulkDelete.setOnClickListener { displayBulkDeleteConfirmationDialog() }
        }
    }

    private fun observeProducts() {
        viewModel.products.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    handleProducts(it.data)
                    viewModel.setTotalProduct(it.data.totalProduct)
                    binding?.tpgTotalProduct?.text =
                        String.format(getString(R.string.sd_total_product), it.data.totalProduct)
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

    private fun reserveProduct(requestId: String, productIds : List<String>) {
        binding?.btnBulkManage?.isLoading = true
        binding?.btnBulkManage?.loadingText = getString(R.string.sd_please_wait)
        viewModel.reserveProduct(requestId, productIds)
    }

    private fun handleProducts(data: ProductData) {
        handleEmptyState(data.totalProduct)

        if (data.totalProduct == ZERO) {
            binding?.recyclerView?.gone()
            binding?.tpgTotalProduct?.gone()
            binding?.tpgMultiSelect?.gone()
            binding?.emptyState?.visible()
        } else {
            if (isFirstLoad) binding?.tpgMultiSelect?.visible()
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

            if (viewModel.isOnMultiSelectMode()) {
                productAdapter.bulkDelete(viewModel.getSelectedProductIds())
            } else {
                productAdapter.delete(viewModel.getSelectedProduct() ?: return)
            }

            val updatedTotalProduct = if (viewModel.isOnMultiSelectMode()) {
                viewModel.getTotalProduct() - viewModel.getSelectedProductCount()
            } else {
                viewModel.getTotalProduct() - ONE_PRODUCT
            }


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


    private fun hideView() {
        viewAnimator.showWithAnimation(binding?.imgScrollUp)
        viewAnimator.hideWithAnimation(binding?.searchBar)

        if (!viewModel.isOnMultiSelectMode()) {
            viewAnimator.hideWithAnimation(binding?.cardViewCreateDiscount)
        }
    }

    private fun showView() {
        viewAnimator.hideWithAnimation(binding?.imgScrollUp)
        viewAnimator.showWithAnimation(binding?.searchBar)
        if (!viewModel.isOnMultiSelectMode()) {
            viewAnimator.showWithAnimation(binding?.cardViewCreateDiscount)
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
        val bottomSheet = MoreMenuBottomSheet()
        bottomSheet.setOnDeleteMenuClicked { displayDeleteConfirmationDialog(product) }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
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

    fun setOnSwipeRefresh(onSwipeRefresh : () -> Unit) {
        this.onSwipeRefresh = onSwipeRefresh
    }

    private fun showProductDetailBottomSheet(product: Product, position : Int) {
        val bottomSheet = ShopDiscountProductDetailBottomSheet.newInstance(
            product.id,
            product.name,
            discountStatusId,
            position
        )
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
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
            }
            tpgCancelMultiSelect.setOnClickListener {
                viewModel.removeAllProductFromSelection()
                viewModel.setInMultiSelectMode(false)
                disableMultiSelect()
                binding?.tpgTotalProduct?.text =
                    String.format(getString(R.string.sd_total_product), viewModel.getTotalProduct())
            }
        }
    }

    private fun enableMultiSelect() {
        binding?.cardViewCreateDiscount?.gone()
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
        binding?.cardViewCreateDiscount?.visible()
    }

    private fun disableProductSelection(products : List<Product>) {
        val toBeDisabledProducts = viewModel.disableProducts(products)
        adapter?.refresh(toBeDisabledProducts)
    }

    private fun enableProductSelection(products : List<Product>) {
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


    private val onProductSelectionChange: (Product, Boolean) -> Unit = { selectedProduct, isSelected ->
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
            binding?.tpgTotalProduct?.text =
                String.format(getString(R.string.sd_total_product), viewModel.getTotalProduct())
        } else {
            binding?.tpgTotalProduct?.text =
                String.format(getString(R.string.sd_selected_product_counter), selectedProductCount)
        }


        if (shouldDisableSelection) {
            disableProductSelection(items)
        } else {
            enableProductSelection(items)
        }
    }

    private val onUpdateDiscountClicked: (Product) -> Unit = { product ->
        showLoaderDialog()
        viewModel.setSelectedProduct(product)
        val requestId = generateRequestId()
        viewModel.setRequestId(requestId)
        reserveProduct(requestId, listOf(product.id))
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

    private val onVariantInfoClicked : (Product, Int) -> Unit = { product, position ->
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

    private fun guard(disableClick: Boolean, block : () -> Unit) {
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
        return binding?.recyclerView
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
        CoroutineScope(Dispatchers.Main).launch {
            delay(PAGE_REDIRECTION_DELAY_IN_MILLIS)
            binding?.btnBulkManage?.isLoading = false
            dismissLoaderDialog()
            ShopDiscountManageDiscountActivity.start(
                requireActivity(),
                viewModel.getRequestId(),
                discountStatusId,
                ShopDiscountManageDiscountMode.UPDATE
            )
        }
    }

    override fun onSwipeRefreshPulled() {
        onSwipeRefresh()
    }

    private fun handleEmptyState(totalProduct : Int) {
        if (totalProduct == ZERO) {
            showEmptyState(discountStatusId)
        } else {
            hideEmptyState()
        }
    }

    private fun showEmptyState(discountStatusId : Int) {
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

        binding?.emptyState?.visible()
        binding?.emptyState?.setImageUrl(EMPTY_STATE_IMAGE_URL)
        binding?.emptyState?.setTitle(title)
        binding?.emptyState?.setDescription(description)
    }

    private fun hideEmptyState() {
        binding?.searchBar?.visible()
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

}