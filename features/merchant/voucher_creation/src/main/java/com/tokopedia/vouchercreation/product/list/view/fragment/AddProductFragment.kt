package com.tokopedia.vouchercreation.product.list.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.base.BaseSimpleListFragment
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.setFragmentToUnifyBgColor
import com.tokopedia.vouchercreation.databinding.FragmentMvcAddProductBinding
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.list.view.adapter.ProductListAdapter
import com.tokopedia.vouchercreation.product.list.view.bottomsheet.CategoryBottomSheet
import com.tokopedia.vouchercreation.product.list.view.bottomsheet.LocationBottomSheet
import com.tokopedia.vouchercreation.product.list.view.bottomsheet.ShowCaseBottomSheet
import com.tokopedia.vouchercreation.product.list.view.bottomsheet.SortBottomSheet
import com.tokopedia.vouchercreation.product.list.view.model.*
import com.tokopedia.vouchercreation.product.list.view.viewmodel.AddProductViewModel
import javax.inject.Inject

class AddProductFragment : BaseSimpleListFragment<ProductListAdapter, ProductUiModel>(),
        ProductListAdapter.OnProductItemClickListener,
        LocationBottomSheet.OnApplyButtonClickListener,
        ShowCaseBottomSheet.OnApplyButtonClickListener,
        CategoryBottomSheet.OnApplyButtonClickListener,
        SortBottomSheet.OnApplyButtonClickListener {

    interface ProductSelectionListener {
        fun onProductSelectionChanged(productCount: Int, maxProductLimit: Int)
    }

    companion object {
        private const val ZERO = 0
        private const val NO_BACKGROUND: Int = 0
        private const val PAGE_SIZE = 10
        const val BUNDLE_KEY_MAX_PRODUCT_LIMIT = "maxProductLimit"
        const val BUNDLE_KEY_COUPON_SETTINGS = "couponSettings"
        const val BUNDLE_KEY_SELECTED_PRODUCTS = "selectedProducts"
        const val BUNDLE_KEY_SELECTED_WAREHOUSE_ID = "selectedWarehouseId"

        @JvmStatic
        fun createInstance(
                selectedWarehouseId: String?,
                maxProductLimit: Int,
                couponSettings: CouponSettings?,
                selectedProducts: ArrayList<ProductUiModel>
        ) = AddProductFragment().apply {
            this.arguments = Bundle().apply {
                putString(BUNDLE_KEY_SELECTED_WAREHOUSE_ID,selectedWarehouseId)
                putInt(BUNDLE_KEY_MAX_PRODUCT_LIMIT, maxProductLimit)
                putParcelable(BUNDLE_KEY_COUPON_SETTINGS, couponSettings)
                putParcelableArrayList(BUNDLE_KEY_SELECTED_PRODUCTS, selectedProducts)
            }
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(AddProductViewModel::class.java)
    }

    private var productSelectionListener: ProductSelectionListener? = null

    private var locationBottomSheet: LocationBottomSheet? = null
    private var showCaseBottomSheet: ShowCaseBottomSheet? = null
    private var categoryBottomSheet: CategoryBottomSheet? = null
    private var sortBottomSheet: SortBottomSheet? = null

    private var binding: FragmentMvcAddProductBinding? = null
    private var warehouseLocationFilter: SortFilterItem? = null
    private var categoryFilter: SortFilterItem? = null
    private var showCaseFilter: SortFilterItem? = null
    private var sortFilter: SortFilterItem? = null

    override fun getScreenName(): String {
        return getString(R.string.add_product)
    }

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        activity?.run { productSelectionListener = this as ProductSelectionListener }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val viewBinding = FragmentMvcAddProductBinding.inflate(inflater, container, false)
        binding = viewBinding
        return viewBinding.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentToUnifyBgColor()
        setupView(binding)
        observeLiveData()

        val selectedWarehouseId = arguments?.getString(BUNDLE_KEY_SELECTED_WAREHOUSE_ID)
        selectedWarehouseId?.run {
            if (this.isNotBlank()) {
                viewModel.setBoundLocationId(selectedWarehouseId.toIntOrNull())
            }
        }

        val maxProductLimit = arguments?.getInt(BUNDLE_KEY_MAX_PRODUCT_LIMIT) ?: ZERO
        viewModel.setMaxProductLimit(maxProductLimit)
        val couponSettings = arguments?.getParcelable<CouponSettings>(BUNDLE_KEY_COUPON_SETTINGS)
        viewModel.setCouponSettings(couponSettings)

        val selectedProducts = arguments?.getParcelableArrayList<ProductUiModel>(BUNDLE_KEY_SELECTED_PRODUCTS)
        val selectedProductIds = viewModel.getSelectedProductIds(selectedProducts?: arrayListOf())
        viewModel.setSelectedProductIds(selectedProductIds)

        val shopId = userSession.shopId
        // get warehouse locations
        val shopIdInt = shopId.toIntOrNull()
        shopIdInt?.run { viewModel.getWarehouseLocations(this) }
        // get shop showcases
        viewModel.getShopShowCases(shopId)
    }

    private fun setupView(binding: FragmentMvcAddProductBinding?) {
        setupSearchBar(binding)
        setupProductListFilter(binding)
        setupButtonAddProduct(binding)
    }

    private fun setupSearchBar(binding: FragmentMvcAddProductBinding?) {
        binding?.sbuProductList?.searchBarTextField?.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event.keyCode == KeyEvent.KEYCODE_ENTER) {

                // prevent crash from selection bar click listener
                resetSelectionBar(binding)
                viewModel.isFiltering = true

                val keyword = textView.text.toString().lowercase()
                viewModel.setSearchKeyword(keyword)
                loadInitialData()
                VoucherCreationTracking.clickSearchProduct(shopId = userSession.shopId, productName = keyword)
                return@setOnEditorActionListener true
            } else return@setOnEditorActionListener false
        }
    }

    private fun setupProductListFilter(binding: FragmentMvcAddProductBinding?) {

        binding?.sfProductList?.indicatorCounter

        binding?.sfProductList?.apply {
            val sellerLocationTitle = getString(R.string.mvc_seller_location)
            warehouseLocationFilter = SortFilterItem(sellerLocationTitle)
            warehouseLocationFilter?.type = ChipsUnify.TYPE_SELECTED
            warehouseLocationFilter?.listener = {
                locationBottomSheet?.show(childFragmentManager)
            }
            val categoryTitle = getString(R.string.mvc_category)
            categoryFilter = SortFilterItem(categoryTitle)
            categoryFilter?.listener = {
                categoryBottomSheet?.show(childFragmentManager)
            }
            val showCaseTitle = getString(R.string.mvc_showcase)
            showCaseFilter = SortFilterItem(showCaseTitle)
            showCaseFilter?.listener = {
                showCaseBottomSheet?.show(childFragmentManager)
            }
            val sortTitle = getString(R.string.mvc_sort)
            sortFilter = SortFilterItem(sortTitle)
            sortFilter?.listener = {
                sortBottomSheet?.show(childFragmentManager)
            }
            val sortFilterItemList = ArrayList<SortFilterItem>()
            warehouseLocationFilter?.run { sortFilterItemList.add(this) }
            categoryFilter?.run { sortFilterItemList.add(this) }
            showCaseFilter?.run { sortFilterItemList.add(this) }
            sortFilter?.run { sortFilterItemList.add(this) }

            addItem(sortFilterItemList)

            warehouseLocationFilter?.refChipUnify?.setChevronClickListener {
                warehouseLocationFilter?.listener?.invoke()
            }
            warehouseLocationFilter?.refChipUnify?.setChevronClickListener {
                warehouseLocationFilter?.listener?.invoke()
            }
            showCaseFilter?.refChipUnify?.setChevronClickListener {
                showCaseFilter?.listener?.invoke()
            }
            sortFilter?.refChipUnify?.setChevronClickListener {
                sortFilter?.listener?.invoke()
            }
        }
        binding?.sfProductList?.parentListener = {
            /* No op. We need to specify this block, otherwise the clear filter chip will do nothing
               when clicked */
        }
        binding?.sfProductList?.dismissListener = {
            resetSelectionBar(binding)
            viewModel.isFiltering = true
            // reset all search criteria
            viewModel.setSearchKeyword("")
            viewModel.setWarehouseLocationId(viewModel.getWarehouseLocationId())
            viewModel.setSelectedShowCases(listOf())
            viewModel.setSelectedSort(listOf())
            viewModel.setSelectedCategories(listOf())
            viewModel.setSelectedProducts(listOf())
            loadInitialData()
        }
    }

    private fun setupWarehouseLocationBottomSheet(warehouseLocationSelections: List<WarehouseLocationSelection>) {
        locationBottomSheet = LocationBottomSheet.createInstance(warehouseLocationSelections, this)
    }

    private fun setupShowCaseBottomSheet(showCaseSelections: List<ShowCaseSelection>) {
        showCaseBottomSheet = ShowCaseBottomSheet.createInstance(showCaseSelections, this)
    }

    private fun setupCategoryBottomSheet(categorySelections: List<CategorySelection>) {
        categoryBottomSheet = CategoryBottomSheet.createInstance(categorySelections, this)
    }

    private fun setupSortBottomSheet(sortSelections: List<SortSelection>) {
        sortBottomSheet = SortBottomSheet.createInstance(sortSelections, this)
    }

    private fun setupSelectionBar(binding: FragmentMvcAddProductBinding?) {
        binding?.cbuSelectAllProduct?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // ignore checkbox click when single selection mode
                val isIndeterminate = binding.cbuSelectAllProduct.getIndeterminate()
                if (isIndeterminate && !viewModel.isSelectAllMode) return@setOnCheckedChangeListener
                if (!viewModel.isFiltering) {
                    adapter?.updateAllProductSelections(isChecked)
                    viewModel.setSelectedProducts(adapter?.getSelectedProducts() ?: listOf())
                } else viewModel.isFiltering = false
            } else {
                binding.cbuSelectAllProduct.setIndeterminate(false)
                if (!viewModel.isFiltering) {
                    adapter?.updateAllProductSelections(isChecked)
                    viewModel.setSelectedProducts(listOf())
                } else viewModel.isFiltering = false
            }
        }
    }

    private fun resetSelectionBar(binding: FragmentMvcAddProductBinding?) {
        binding?.cbuSelectAllProduct?.setOnCheckedChangeListener { _, _ -> }
        binding?.cbuSelectAllProduct?.setIndeterminate(false)
        binding?.cbuSelectAllProduct?.isSelected = false
    }

    private fun setupButtonAddProduct(binding: FragmentMvcAddProductBinding?) {
        binding?.buttonAddProduct?.setOnClickListener {
            val selectedProducts = adapter?.getSelectedProducts() ?: listOf()
            if (viewModel.isMaxProductLimitReached(selectedProducts.size)) {
                binding.tickerMaxProductWording.show()
                adapter?.isProductListEnabled(false)
                binding.buttonAddProduct.isEnabled = false
            } else {
                binding.tickerMaxProductWording.hide()
                val extraSelectedProducts = ArrayList<ProductUiModel>()
                extraSelectedProducts.addAll(selectedProducts)
                val resultIntent = Intent().apply {
                    putParcelableArrayListExtra(BUNDLE_KEY_SELECTED_PRODUCTS, extraSelectedProducts)
                    putExtra(BUNDLE_KEY_SELECTED_WAREHOUSE_ID, viewModel.getWarehouseLocationId()?.toString())
                }
                this.activity?.setResult(Activity.RESULT_OK, resultIntent)
                VoucherCreationTracking.clickAddProduct(shopId = userSession.shopId, products = extraSelectedProducts)
                this.activity?.finish()
            }
        }
    }

    private fun observeLiveData() {
        viewModel.selectedProductListLiveData.observe(viewLifecycleOwner, { selectedProducts ->
            if (selectedProducts.isEmpty()) {
                viewModel.isSelectAllMode = false
                binding?.cbuSelectAllProduct?.isChecked = false
                binding?.buttonAddProduct?.isEnabled = false
                val isIndeterminate = binding?.cbuSelectAllProduct?.getIndeterminate() ?: false
                if (isIndeterminate) binding?.cbuSelectAllProduct?.setIndeterminate(false)
                binding?.tpgSelectAll?.gone()
                binding?.selectionBar?.setBackgroundResource(NO_BACKGROUND)
                binding?.buttonAddProduct?.text = getString(R.string.add_product)
                binding?.tickerSellerLocationChange?.hide()
                productSelectionListener?.onProductSelectionChanged(0, viewModel.getMaxProductLimit())
            } else {
                val size = selectedProducts.size
                if (binding?.tickerSellerLocationChange?.isVisible == false) {
                    binding?.buttonAddProduct?.isEnabled = true
                }
                binding?.buttonAddProduct?.text = "Tambah $size Produk"
                binding?.tpgSelectAll?.visible()
                binding?.tpgSelectAll?.text = "$size Produk dipilih"
                binding?.selectionBar?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mvc_grey_f3f4f5))
                productSelectionListener?.onProductSelectionChanged(size, viewModel.getMaxProductLimit())
            }
        })
        viewModel.productListResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val productData = result.data.productList.data
                    val productUiModels = viewModel.mapProductDataToProductUiModel(productData)
                    val productList = viewModel.excludeSelectedProducts(productUiModels, viewModel.getSelectedProductIds())
                    viewModel.setProductUiModels(productList)
                    if (productList.isNotEmpty()) {
                        binding?.selectionBar?.show()
                        binding?.emptyProductsLayout?.hide()
                        viewModel.getCouponSettings()?.run {
                            viewModel.validateProductList(
                                    benefitType = viewModel.getBenefitType(this),
                                    couponType = viewModel.getCouponType(this),
                                    benefitIdr = viewModel.getBenefitIdr(this),
                                    benefitMax = viewModel.getBenefitMax(this),
                                    benefitPercent = viewModel.getBenefitPercent(this),
                                    minPurchase = viewModel.getMinimumPurchase(this),
                                    productIds = viewModel.getIdsFromProductList(productList)
                            )
                        }
                    } else {
                        if (viewModel.isInitialLoad(viewModel.getPagingIndex())) {
                            binding?.selectionBar?.hide()
                            binding?.rvProductList?.hide()
                            binding?.emptyProductsLayout?.show()
                            binding?.buttonAddProduct?.isEnabled = false
                        }
                    }
                }
                is Fail -> {
                    // TODO : handle negative case
                }
            }
        })
        viewModel.validateVoucherResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val validationResults = result.data.response.voucherValidationData.validationPartial
                    val productList = viewModel.getProductUiModels()
                    val isIndeterminate = binding?.cbuSelectAllProduct?.getIndeterminate() ?: false
                    val isSelectAll = binding?.cbuSelectAllProduct?.isChecked ?: false && !isIndeterminate
                    val updatedProductList = viewModel.applyValidationResult(
                            isSelectAll,
                            productList = productList,
                            validationResults = validationResults
                    )

                    val hasNextPage = updatedProductList.isNotEmpty()
                    renderList(updatedProductList, hasNextPage)
                    setupSelectionBar(binding)

                    if (isSelectAll) {
                        viewModel.setSelectedProducts(adapter?.getSelectedProducts() ?: listOf())
                    }

                    val origin = viewModel.getBoundLocationId()?: viewModel.getSellerWarehouseId()
                    viewModel.isSelectionChanged = viewModel.isSelectionChanged(origin, viewModel.getWarehouseLocationId())
                    val addedProducts = viewModel.getSelectedProductIds()
                    if (addedProducts.isNotEmpty() && viewModel.isSelectionChanged) {
                        binding?.tickerSellerLocationChange?.show()
                        adapter?.disableAllProductSelections()
                        binding?.cbuSelectAllProduct?.isClickable = false
                        binding?.buttonAddProduct?.isEnabled = false
                    } else if (viewModel.getSelectedProducts().isNotEmpty() && viewModel.isSelectionChanged) {
                        binding?.tickerSellerLocationChange?.show()
                        adapter?.disableAllProductSelections()
                        binding?.cbuSelectAllProduct?.isClickable = false
                        binding?.buttonAddProduct?.isEnabled = false
                    } else {
                        binding?.tickerSellerLocationChange?.hide()
                        binding?.cbuSelectAllProduct?.isClickable = true
                        if (viewModel.getSelectedProducts().isNotEmpty()) binding?.buttonAddProduct?.isEnabled = true
                    }

                }
                is Fail -> {
                    // TODO : handle negative case
                }
            }
        })
        viewModel.getWarehouseLocationsResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val warehouseLocations = result.data.ShopLocGetWarehouseByShopIDs.warehouses
                    var selectedWarehouseId = viewModel.getBoundLocationId()
                    val locationSelections = viewModel.mapWarehouseLocationToSelections(warehouseLocations, selectedWarehouseId)
                    setupWarehouseLocationBottomSheet(locationSelections)
                    val sellerWarehouseId = viewModel.getSellerWarehouseId(warehouseLocations)
                    viewModel.setSellerWarehouseId(sellerWarehouseId)
                    if (selectedWarehouseId == null) {
                        viewModel.setWarehouseLocationId(sellerWarehouseId)
                        selectedWarehouseId = sellerWarehouseId
                    } else {
                        viewModel.setWarehouseLocationId(selectedWarehouseId)
                    }

                    loadInitialData()

                    // get sort and categories
                    viewModel.getProductListMetaData(shopId = userSession.shopId, warehouseLocationId = selectedWarehouseId)
                }
                is Fail -> {
                    // TODO : handle negative case
                }
            }
        })
        viewModel.getShowCasesByIdResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val shopShowCases = result.data.shopShowcasesByShopId.result
                    val showCaseSelections = viewModel.mapShopShowCasesToSelections(shopShowCases)
                    setupShowCaseBottomSheet(showCaseSelections)
                }
                is Fail -> {
                    // TODO : handle negative case
                }
            }
        })
        viewModel.getProductListMetaDataResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val sort = result.data.response.data.sort
                    val categories = result.data.response.data.category
                    val filteredSort = viewModel.excludeDefaultSortSelection(sort)
                    val sortSelections = viewModel.mapSortListToSortSelections(filteredSort)
                    setupSortBottomSheet(sortSelections)
                    val categorySelections = viewModel.mapCategoriesToCategorySelections(categories)
                    setupCategoryBottomSheet(categorySelections)
                }
                is Fail -> {
                    // TODO : handle negative case
                }
            }
        })
    }

    override fun onProductCheckBoxClicked(isSelected: Boolean) {
        if (viewModel.isFiltering) return
        viewModel.isSelectAllMode = false
        if (isSelected) {
            // implement selection bar ux
            val isIndeterminate = binding?.cbuSelectAllProduct?.getIndeterminate() ?: false
            if (!isIndeterminate) binding?.cbuSelectAllProduct?.setIndeterminate(true)
            val isChecked = binding?.cbuSelectAllProduct?.isChecked ?: false
            if (!isChecked) binding?.cbuSelectAllProduct?.isChecked = true
        }
        viewModel.setSelectedProducts(adapter?.getSelectedProducts() ?: listOf())
    }

    override fun onApplyWarehouseLocationFilter(selectedWarehouseLocation: WarehouseLocationSelection) {
        adapter?.enableAllProductSelections()
        viewModel.setWarehouseLocationId(selectedWarehouseLocation.warehouseId)
        resetSelectionBar(binding)
        viewModel.isFiltering = true
        loadInitialData()
        VoucherCreationTracking.clickFilterLocation(shopId = userSession.shopId, location = selectedWarehouseLocation.warehouseName)
    }

    override fun onApplyShowCaseFilter(selectedShowCases: List<ShowCaseSelection>) {
        if (selectedShowCases.isNotEmpty()) showCaseFilter?.type = ChipsUnify.TYPE_SELECTED
        else showCaseFilter?.type = ChipsUnify.TYPE_NORMAL
        viewModel.setSelectedShowCases(selectedShowCases)
        resetSelectionBar(binding)
        viewModel.isFiltering = true
        loadInitialData()
        viewModel.setSelectedProducts(listOf())
         VoucherCreationTracking.clickFilterEtalase(shopId = userSession.shopId, etalaseName = viewModel.getSelectedShopShowcaseNames())
    }

    override fun onApplyCategoryFilter(selectedCategories: List<CategorySelection>) {
        if (selectedCategories.isNotEmpty()) categoryFilter?.type = ChipsUnify.TYPE_SELECTED
        else categoryFilter?.type = ChipsUnify.TYPE_NORMAL
        viewModel.setSelectedCategories(selectedCategories)
        resetSelectionBar(binding)
        viewModel.isFiltering = true
        loadInitialData()
        viewModel.setSelectedProducts(listOf())
        VoucherCreationTracking.clickFilterProductCategory(shopId = userSession.shopId, categoryName = viewModel.getSelectedCategoryNames())
    }

    override fun onApplySortFilter(selectedSort: List<SortSelection>) {
        if (selectedSort.isNotEmpty()) sortFilter?.type = ChipsUnify.TYPE_SELECTED
        else sortFilter?.type = ChipsUnify.TYPE_NORMAL
        viewModel.setSelectedSort(selectedSort)
        resetSelectionBar(binding)
        viewModel.isFiltering = true
        loadInitialData()
        viewModel.setSelectedProducts(listOf())
        VoucherCreationTracking.clickSortProductLabel(shopId = userSession.shopId, productLabel = viewModel.getSelectedSortName() ?: "")
    }

    override fun createAdapter() = ProductListAdapter(this)

    override fun getRecyclerView(view: View): RecyclerView? = binding?.rvProductList

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? = binding?.productListSwipeLayout

    override fun getPerPage() = PAGE_SIZE

    override fun addElementToAdapter(list: List<ProductUiModel>) {
        adapter?.addProducts(list)
    }

    override fun loadData(page: Int) {
        viewModel.setPagingIndex(page)
        viewModel.getWarehouseLocationId()?.run {
            viewModel.getProductList(
                    page = page,
                    keyword = viewModel.getSearchKeyWord(),
                    shopId = userSession.shopId,
                    warehouseLocationId = viewModel.getWarehouseLocationId(),
                    shopShowCaseIds = viewModel.getSelectedShopShowCaseIds(),
                    categoryList = viewModel.getSelectedCategoryIds(),
                    sort = viewModel.getSelectedSort()
            )
        }
    }

    override fun clearAdapterData() {
        adapter?.clearData()
    }

    override fun onShowLoading() {

    }

    override fun onHideLoading() {

    }

    override fun onDataEmpty() {
        binding?.selectionBar?.hide()
        binding?.rvProductList?.hide()
        binding?.emptyProductsLayout?.show()
    }

    override fun onGetListError(message: String) {

    }
}