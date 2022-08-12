package com.tokopedia.shopdiscount.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.cancel.CancelDiscountDialog
import com.tokopedia.shopdiscount.databinding.FragmentSearchProductBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.manage.domain.entity.ProductData
import com.tokopedia.shopdiscount.manage.presentation.list.ProductAdapter
import com.tokopedia.shopdiscount.manage_discount.presentation.view.activity.ShopDiscountManageActivity
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.more_menu.MoreMenuBottomSheet
import com.tokopedia.shopdiscount.product_detail.presentation.bottomsheet.ShopDiscountProductDetailBottomSheet
import com.tokopedia.shopdiscount.utils.constant.DiscountStatus
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.shopdiscount.utils.constant.ZERO
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.extension.showToaster
import com.tokopedia.shopdiscount.utils.layoutmanager.NonPredictiveLinearLayoutManager
import com.tokopedia.shopdiscount.utils.paging.BaseSimpleListFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class SearchProductFragment : BaseSimpleListFragment<ProductAdapter, Product>() {

    companion object {
        private const val BUNDLE_KEY_DISCOUNT_STATUS_NAME = "discount-status-name"
        private const val BUNDLE_KEY_DISCOUNT_STATUS_ID = "discount-status-id"
        private const val PAGE_SIZE = 10
        private const val FIRST_PAGE = 1
        private const val MAX_PRODUCT_SELECTION = 5
        private const val ONE_PRODUCT = 1
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/slash_price/search_not_found.png"

        @JvmStatic
        fun newInstance(
            discountStatusName : String,
            discountStatusId: Int
        ): SearchProductFragment {
            val fragment = SearchProductFragment()
            fragment.arguments = Bundle().apply {
                putString(BUNDLE_KEY_DISCOUNT_STATUS_NAME, discountStatusName)
                putInt(BUNDLE_KEY_DISCOUNT_STATUS_ID, discountStatusId)
            }
            return fragment
        }

    }
    private val discountStatusName by lazy {
        arguments?.getString(BUNDLE_KEY_DISCOUNT_STATUS_NAME).orEmpty()
    }

    private val discountStatusId by lazy {
        arguments?.getInt(BUNDLE_KEY_DISCOUNT_STATUS_ID).orZero()
    }
    private var binding by autoClearedNullable<FragmentSearchProductBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession : UserSessionInterface

    private val loaderDialog by lazy { LoaderDialog(requireActivity()) }
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(SearchProductViewModel::class.java) }
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

    override fun getScreenName(): String = SearchProductFragment::class.java.canonicalName.orEmpty()
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
        binding = FragmentSearchProductBinding.inflate(inflater, container, false)
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
        setupMultiSelection()
        setupToolbar()
        setupSearchBar()
    }

    private fun setupSearchBar() {
        binding?.run {
            searchBar.searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loadInitialData()
                    return@setOnEditorActionListener false
                }
                return@setOnEditorActionListener false
            }
            searchBar.clearListener = { clearSearchBar() }
            searchBar.searchBarPlaceholder = String.format(getString(R.string.sd_search_at), discountStatusName)
        }
    }

    private fun setupToolbar() {
        binding?.run {
            imgBack.setOnClickListener { activity?.onBackPressed() }
        }
    }

    private fun setupMultiSelection() {
        binding?.run {
            tpgMultiSelect.setOnClickListener {
                viewModel.setRequestId(generateRequestId())
                viewModel.removeAllProductFromSelection()
                viewModel.setInMultiSelectMode(true)
                enableMultiSelect()
            }
            tpgCancelMultiSelect.setOnClickListener {
                viewModel.removeAllProductFromSelection()
                viewModel.setInMultiSelectMode(false)
                disableMultiSelect()
                binding?.tpgTotalProduct?.text =
                    String.format(getString(R.string.sd_total_product), viewModel.getTotalProduct())
            }
            btnManage.setOnClickListener {
                val selectedProductIds = viewModel.getSelectedProductIds()
                reserveProduct(viewModel.getRequestId(), selectedProductIds)
            }
            btnDelete.setOnClickListener { displayBulkDeleteConfirmationDialog() }
        }
    }


    private fun observeProducts() {
        viewModel.products.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    binding?.groupContent?.visible()
                    binding?.shimmer?.content?.gone()
                    handleProducts(it.data)
                    viewModel.setTotalProduct(it.data.totalProduct)
                    updateCounter(it.data.totalProduct)
                }
                is Fail -> {
                    binding?.groupContent?.gone()
                    binding?.shimmer?.content?.gone()
                    displayError()
                    binding?.root showError it.throwable
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
                        binding?.btnManage?.isLoading = false
                        binding?.root showError getString(R.string.sd_error_reserve_product)
                    }
                }
                is Fail -> {
                    binding?.btnManage?.isLoading = false
                    binding?.root showError it.throwable
                }
            }
        }
    }

    private fun handleProducts(data: ProductData) {
        if (data.totalProduct == Int.ZERO) {
            binding?.emptyState?.setImageUrl(EMPTY_STATE_IMAGE_URL)
            binding?.emptyState?.setTitle(getString(R.string.sd_search_result_not_found_title))
            binding?.emptyState?.setDescription(getString(R.string.sd_search_result_not_found_description))

            binding?.recyclerView?.gone()
            binding?.emptyState?.visible()

            binding?.tpgMultiSelect?.isEnabled = false
        } else {
            binding?.recyclerView?.visible()
            binding?.emptyState?.gone()

            binding?.tpgMultiSelect?.isEnabled = true

            renderList(data.products, data.products.size == getPerPage())
        }
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
            binding?.cardView?.gone()
            viewModel.removeAllProductFromSelection()
            viewModel.setTotalProduct(updatedTotalProduct)
            viewModel.setInMultiSelectMode(false)
            viewModel.setDisableProductSelection(false)
            disableMultiSelect()
            handleEmptyState(updatedTotalProduct)
        } else {
            binding?.root showError getString(R.string.sd_error_delete_discount)
        }
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

    private val onUpdateDiscountClicked: (Product) -> Unit = { product ->
        showLoaderDialog()
        viewModel.setSelectedProduct(product)
        val requestId = generateRequestId()
        viewModel.setRequestId(requestId)
        reserveProduct(requestId, listOf(product.id))
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

    private fun reserveProduct(requestId : String, productIds : List<String>) {
        binding?.btnManage?.isLoading = true
        binding?.btnManage?.loadingText = getString(R.string.sd_please_wait)
        viewModel.reserveProduct(requestId, productIds)
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

        binding?.cardView?.isVisible = selectedProductCount > 0

        if (selectedProductCount == 0) {
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

    private fun disableProductSelection(products : List<Product>) {
        val toBeDisabledProducts = viewModel.disableProducts(products)
        adapter?.refresh(toBeDisabledProducts)
    }

    private fun enableProductSelection(products : List<Product>) {
        val toBeEnabledProducts = viewModel.enableProduct(products)
        adapter?.refresh(toBeEnabledProducts)
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
        return if (activity == null) {
            binding?.recyclerView
        } else {
            binding?.recyclerView?.apply {
                layoutManager = NonPredictiveLinearLayoutManager(requireActivity())
            }
        }
    }
    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return null
    }

    override fun getPerPage(): Int {
        return PAGE_SIZE
    }

    override fun addElementToAdapter(list: List<Product>) {
        adapter?.addData(list)
    }

    override fun loadData(page: Int) {
        binding?.globalError?.gone()
        binding?.emptyState?.gone()
        val keyword = binding?.searchBar?.searchBarTextField?.text.toString().trim()
        viewModel.getSlashPriceProducts(
            page,
            discountStatusId,
            keyword,
            viewModel.isOnMultiSelectMode(),
            viewModel.shouldDisableProductSelection()
        )
    }

    override fun clearAdapterData() {
        adapter?.clearData()
    }

    override fun onShowLoading() {
        binding?.emptyState?.gone()
        adapter?.showLoading()
    }

    override fun onHideLoading() {
        binding?.emptyState?.gone()
        adapter?.hideLoading()
    }

    override fun onDataEmpty() {
        binding?.emptyState?.visible()
    }

    override fun onGetListError(message: String) {
        displayError()
    }

    private fun displayError() {
        binding?.run {
            globalError.visible()
            globalError.setType(GlobalError.SERVER_ERROR)
            globalError.setActionClickListener { loadInitialData() }
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

    private fun showProductDetailBottomSheet(product: Product, position: Int) {
        val bottomSheet = ShopDiscountProductDetailBottomSheet.newInstance(
            product.id,
            product.name,
            discountStatusId,
            position
        )
        bottomSheet.setListener(object: ShopDiscountProductDetailBottomSheet.Listener{
            override fun deleteParentProduct(productId: String) {
                deleteSingleProduct(productId)
            }

        })
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun deleteSingleProduct(productId: String) {
        val totalCountProduct = viewModel.getTotalProduct() - ONE_PRODUCT
        val getDeletedProduct = adapter?.getProductBasedOnId(productId)
        getDeletedProduct?.let{
            productAdapter.delete(it)
            binding?.tpgTotalProduct?.text =
                String.format(getString(R.string.sd_total_product), totalCountProduct)
        }
    }

    private fun clearSearchBar() {
        clearAllData()
        onShowLoading()
        viewModel.getSlashPriceProducts(
            FIRST_PAGE,
            discountStatusId,
            EMPTY_STRING,
            viewModel.isOnMultiSelectMode(),
            viewModel.shouldDisableProductSelection()
        )
    }

    private fun updateCounter(totalProduct: Int) {
        if (viewModel.isOnMultiSelectMode()) {
            binding?.tpgTotalProduct?.text =
                String.format(getString(R.string.sd_selected_product_counter), viewModel.getSelectedProductCount())
        } else {
            binding?.tpgTotalProduct?.text =
                String.format(getString(R.string.sd_total_product), totalProduct)
        }
    }

    private fun enableMultiSelect() {
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
        binding?.cardView?.gone()
    }

    private fun generateRequestId(): String {
        return userSession.shopId + Date().time
    }

    private fun redirectToUpdateDiscountPage() {
        binding?.btnManage?.isLoading = false
        ShopDiscountManageActivity.start(
            requireActivity(),
            viewModel.getRequestId(),
            discountStatusId,
            ShopDiscountManageDiscountMode.UPDATE
        )
    }

    override fun onSwipeRefreshPulled() {

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
            getString(R.string.sd_no_paused_discount_description)
        }

        val description = if (discountStatusId == DiscountStatus.PAUSED) {
            getString(R.string.sd_no_discount_title)
        } else {
            getString(R.string.sd_no_discount_description)
        }

        binding?.tpgTotalProduct?.gone()
        binding?.tpgMultiSelect?.gone()
        binding?.tpgCancelMultiSelect?.gone()

        binding?.emptyState?.visible()
        binding?.emptyState?.setImageUrl(EMPTY_STATE_IMAGE_URL)
        binding?.emptyState?.setTitle(title)
        binding?.emptyState?.setDescription(description)
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

     private fun redirectToProductDetailPage(product: Product) {
        val imageUrl = arrayListOf(product.imageUrl)
        val intent = ImagePreviewActivity.getCallingIntent(
            context = requireActivity(),
            imageUris = imageUrl,
            disableDownloadButton = true
        )
        startActivity(intent)
    }

}