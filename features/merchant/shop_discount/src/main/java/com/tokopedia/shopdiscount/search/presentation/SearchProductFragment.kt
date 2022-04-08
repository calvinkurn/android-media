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
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.cancel.CancelDiscountDialog
import com.tokopedia.shopdiscount.databinding.FragmentSearchProductBinding
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.manage.domain.entity.ProductData
import com.tokopedia.shopdiscount.more_menu.MoreMenuBottomSheet
import com.tokopedia.shopdiscount.product_detail.presentation.bottomsheet.ShopDiscountProductDetailBottomSheet
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.shopdiscount.utils.extension.showError
import com.tokopedia.shopdiscount.utils.extension.showToaster
import com.tokopedia.shopdiscount.utils.paging.BaseSimpleListFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class SearchProductFragment : BaseSimpleListFragment<SearchProductAdapter, Product>() {

    companion object {
        private const val BUNDLE_KEY_DISCOUNT_STATUS_NAME = "discount-status-name"
        private const val BUNDLE_KEY_DISCOUNT_STATUS_ID = "discount-status-id"
        private const val PAGE_SIZE = 10
        private const val FIRST_PAGE = 1
        private const val MAX_PRODUCT_SELECTION = 5

        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/slash_price/empty_product_with_discount.png"

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
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(SearchProductViewModel::class.java) }
    private var onDiscountRemoved: (Int, Int) -> Unit = { _, _ -> }
    private val productAdapter by lazy {
        SearchProductAdapter(
            onProductClicked,
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
                viewModel.setMultiSelectEnabled(true)
                tpgMultiSelect.gone()
                tpgCancelMultiSelect.visible()
                val currentItems = adapter?.getItems() ?: emptyList()
                val enabledMultiSelect = viewModel.enableMultiSelect(currentItems)
                adapter?.updateAll(enabledMultiSelect)
            }
            tpgCancelMultiSelect.setOnClickListener {
                viewModel.setMultiSelectEnabled(false)
                tpgMultiSelect.visible()
                tpgCancelMultiSelect.gone()
                val currentItems = adapter?.getItems() ?: emptyList()
                val disabledMultiSelect = viewModel.disableMultiSelect(currentItems)
                adapter?.updateAll(disabledMultiSelect)
                binding?.cardView?.gone()
                binding?.tpgTotalProduct?.text =
                    String.format(getString(R.string.sd_total_product), viewModel.getTotalProduct())
            }
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
                    handleDeleteDiscountResult(it.data)
                }
                is Fail -> {
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
            binding?.recyclerView showToaster getString(R.string.sd_discount_deleted)
            onDiscountRemoved(discountStatusId, viewModel.getTotalProduct())
            productAdapter.delete(viewModel.getSelectedProduct() ?: return)
            val updatedTotalProduct = viewModel.getTotalProduct() - 1
            binding?.tpgTotalProduct?.text =
                String.format(getString(R.string.sd_total_product), updatedTotalProduct)
        } else {
            binding?.root showError getString(R.string.sd_error_delete_discount)
        }
    }

    private val onProductClicked: (Product) -> Unit = { product ->
        viewModel.setSelectedProduct(product)
        guard(product.disableClick) {
            showProductDetailBottomSheet(product)
        }
    }

    private val onUpdateDiscountClicked: (Product) -> Unit = { product ->
        viewModel.setSelectedProduct(product)
    }

    private val onVariantInfoClicked : (Product) -> Unit = { product ->
        viewModel.setSelectedProduct(product)
        guard(product.disableClick) {
            showProductDetailBottomSheet(product)
        }
    }

    private val onOverflowMenuClicked: (Product) -> Unit = { product ->
        viewModel.setSelectedProduct(product)
        guard(product.disableClick) {
            displayMoreMenuBottomSheet(product)
        }
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
        val selectedProductCount = viewModel.findSelectedProducts(items).size

        val shouldDisableSelection = selectedProductCount >= MAX_PRODUCT_SELECTION
        viewModel.setDisableProductSelection(shouldDisableSelection)
        viewModel.setSelectedProductCount(selectedProductCount)

        handleBulkManageButtonVisibility(selectedProductCount)

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

    private fun handleBulkManageButtonVisibility(selectedProductCount : Int) {
        binding?.cardView?.isVisible = selectedProductCount > 0

        val counter = String.format(getString(R.string.sd_manage_with_counter), selectedProductCount)
        binding?.btnManage?.text = counter
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

    override fun createAdapter(): SearchProductAdapter {
        return productAdapter
    }

    override fun getRecyclerView(view: View): RecyclerView? {
        return binding?.recyclerView
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
            viewModel.isMultiSelectEnabled(),
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
        val dialog = CancelDiscountDialog(requireContext())
        dialog.setOnDeleteConfirmed {
            viewModel.deleteDiscount(discountStatusId, product.id)
        }
        dialog.show()
    }

    private fun showProductDetailBottomSheet(product: Product) {
        val bottomSheet = ShopDiscountProductDetailBottomSheet.newInstance(
            product.id,
            product.name,
            discountStatusId
        )
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun clearSearchBar() {
        clearAllData()
        onShowLoading()
        viewModel.getSlashPriceProducts(
            FIRST_PAGE,
            discountStatusId,
            EMPTY_STRING,
            viewModel.isMultiSelectEnabled(),
            viewModel.shouldDisableProductSelection()
        )
    }

    private fun updateCounter(totalProduct: Int) {
        if (viewModel.isMultiSelectEnabled()) {
            binding?.tpgTotalProduct?.text =
                String.format(getString(R.string.sd_selected_product_counter), viewModel.getSelectedProductCount())
        } else {
            binding?.tpgTotalProduct?.text =
                String.format(getString(R.string.sd_total_product), totalProduct)
        }
    }
}