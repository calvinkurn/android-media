package com.tokopedia.vouchercreation.product.list.view.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.setFragmentToUnifyBgColor
import com.tokopedia.vouchercreation.databinding.FragmentMvcAddProductBinding
import com.tokopedia.vouchercreation.product.list.view.adapter.ProductListAdapter
import com.tokopedia.vouchercreation.product.list.view.bottomsheet.CategoryBottomSheet
import com.tokopedia.vouchercreation.product.list.view.bottomsheet.LocationBottomSheet
import com.tokopedia.vouchercreation.product.list.view.bottomsheet.ShowCaseBottomSheet
import com.tokopedia.vouchercreation.product.list.view.bottomsheet.SortBottomSheet
import com.tokopedia.vouchercreation.product.list.view.model.*
import com.tokopedia.vouchercreation.product.list.view.viewholder.ProductItemVariantViewHolder
import com.tokopedia.vouchercreation.product.list.view.viewholder.ProductItemViewHolder
import com.tokopedia.vouchercreation.product.list.view.viewmodel.AddProductViewModel
import com.tokopedia.vouchercreation.product.list.view.viewmodel.AddProductViewModel.Companion.SELLER_LOCATION_ID
import javax.inject.Inject

class AddProductFragment : BaseDaggerFragment(),
        ProductItemViewHolder.OnProductItemClickListener,
        ProductItemVariantViewHolder.OnVariantItemClickListener,
        LocationBottomSheet.OnApplyButtonClickListener,
        ShowCaseBottomSheet.OnApplyButtonClickListener, CategoryBottomSheet.OnApplyButtonClickListener, SortBottomSheet.OnApplyButtonClickListener {

    companion object {

        private const val NO_BACKGROUND: Int = 0

        @JvmStatic
        fun createInstance() = AddProductFragment()
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

    private var locationBottomSheet: LocationBottomSheet? = null
    private var showCaseBottomSheet: ShowCaseBottomSheet? = null
    private var categoryBottomSheet: CategoryBottomSheet? = null
    private var sortBottomSheet: SortBottomSheet? = null

    private var binding: FragmentMvcAddProductBinding? = null
    private var adapter: ProductListAdapter? = null
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        val shopId = userSession.shopId
        // get initial product list
        viewModel.setWarehouseLocationId(SELLER_LOCATION_ID)
        viewModel.getProductList(shopId = shopId, warehouseLocationId = SELLER_LOCATION_ID)
        // get warehouse locations
        val shopIdInt = shopId.toIntOrNull()
        shopIdInt?.run { viewModel.getWarehouseLocations(this) }
        // get shop showcases
        viewModel.getShopShowCases(shopId)
        // get sort and categories
        viewModel.getProductListMetaData(shopId = shopId, warehouseLocationId = SELLER_LOCATION_ID)
    }

    private fun setupView(binding: FragmentMvcAddProductBinding?) {
        setupSearchBar(binding)
        setupProductListFilter(binding)
        setupSelectionBar(binding)
        setupProductListView(binding)
    }

    private fun setupSearchBar(binding: FragmentMvcAddProductBinding?) {
        binding?.sbuProductList?.searchBarTextField?.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val keyword = textView.text.toString().lowercase()
                viewModel.setSearchKeyword(keyword)
                viewModel.getProductList(
                        keyword = keyword,
                        shopId = userSession.shopId,
                        warehouseLocationId = viewModel.getWarehouseLocationId(),
                        shopShowCaseIds = viewModel.getSelectedShopShowCaseIds(),
                        categoryList = viewModel.getSelectedCategoryIds(),
                        sort = viewModel.getSelectedSort()
                )
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
            // reset all search criteria
            viewModel.setSearchKeyword("")
            viewModel.setWarehouseLocationId(SELLER_LOCATION_ID)
            viewModel.setSelectedShowCases(listOf())
            viewModel.getProductList(
                    shopId = userSession.shopId,
                    warehouseLocationId = viewModel.getWarehouseLocationId()
            )
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

    private fun setupMaxLimitTicker(binding: FragmentMvcAddProductBinding?) {
        binding?.tickerMaxProductWording
    }

    private fun setupSelectionBar(binding: FragmentMvcAddProductBinding?) {
        binding?.cbuSelectAllProduct?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // TODO: implement proper string formatting
                adapter?.selectAllProduct()
                viewModel.setSelectedProduct(adapter?.getSelectedProducts() ?: listOf())
                binding.tpgSelectAll.text = adapter?.getSelectedProducts()?.size.toString() + " " + "dipilih"
                binding.selectionBar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mvc_grey_f3f4f5))
                binding.deleteProductLayout.visible()
            } else {
                binding.tpgSelectAll.text = getString(R.string.mvc_select_all)
                binding.selectionBar.setBackgroundResource(NO_BACKGROUND)
                binding.deleteProductLayout.gone()
            }
        }
    }

    private fun setupProductListView(binding: FragmentMvcAddProductBinding?) {
        adapter = ProductListAdapter(this, this)
        binding?.rvProductList?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeLiveData() {
        viewModel.productListResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val productData = result.data.productList.data
                    val productUiModels = viewModel.mapProductDataToProductUiModel(productData)
                    adapter?.setProductList(productUiModels)
                    // TODO : handle empty product list
                }
                is Fail -> {
                    // TODO : handle negative case
                }
            }
        })
        viewModel.getProductVariantsResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val variantData = result.data.product.variant
                    val adapterPosition = viewModel.getClickedAdapterPosition()
                    val productUiModel = adapterPosition?.run { adapter?.getProductUiModel(this) }
                    productUiModel?.run {
                        val variantUiModels = viewModel.mapVariantDataToVariantUiModel(variantData, productUiModel)
                        adapter?.updateProductVariant(adapterPosition, variantUiModels)
                    }
                }
                is Fail -> {
                    // TODO : handle negative case
                }
            }
        })
        viewModel.getSellerLocationsResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val warehouseLocations = result.data.ShopLocGetWarehouseByShopIDs.warehouses
                    val locationSelections = viewModel.mapWarehouseLocationToSelections(warehouseLocations)
                    setupWarehouseLocationBottomSheet(locationSelections)
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
                    val sortSelections = viewModel.mapSortListToSortSelections(sort)
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

    override fun onProductCheckBoxClicked(isSelected: Boolean, productUiModel: ProductUiModel, adapterPosition: Int) {
        if (isSelected) viewModel.addSelectedProduct(productUiModel)
        else viewModel.removeSelectedProduct(productUiModel)
        adapter?.updateSelectionState(isSelectAll = false, adapterPosition = adapterPosition)
    }

    override fun onVariantAccordionClicked(isVariantEmpty: Boolean, productId: String, adapterPosition: Int) {
        viewModel.setClickedAdapterPosition(adapterPosition)
        viewModel.getProductVariants(isVariantEmpty, productId)
    }

    override fun onVariantCheckBoxClicked(isSelected: Boolean, productVariant: VariantUiModel) {
//        if (isSelected) viewModel.removeSelectedProduct(productVariant.variantId)
//        else viewModel.removeSelectedProduct(productVariant.variantId)
    }

    override fun onApplyWarehouseLocationFilter(selectedWarehouseLocation: WarehouseLocationSelection) {
        viewModel.setWarehouseLocationId(selectedWarehouseLocation.warehouseId)
        viewModel.getProductList(
                keyword = viewModel.getSearchKeyWord(),
                shopId = userSession.shopId,
                warehouseLocationId = selectedWarehouseLocation.warehouseId,
                shopShowCaseIds = viewModel.getSelectedShopShowCaseIds(),
                categoryList = viewModel.getSelectedCategoryIds(),
                sort = viewModel.getSelectedSort()
        )
    }

    override fun onApplyShowCaseFilter(selectedShowCases: List<ShowCaseSelection>) {
        if (selectedShowCases.isNotEmpty()) showCaseFilter?.type = ChipsUnify.TYPE_SELECTED
        else showCaseFilter?.type = ChipsUnify.TYPE_NORMAL
        viewModel.setSelectedShowCases(selectedShowCases)
        viewModel.getProductList(
                keyword = viewModel.getSearchKeyWord(),
                shopId = userSession.shopId,
                warehouseLocationId = viewModel.getWarehouseLocationId(),
                shopShowCaseIds = viewModel.getSelectedShopShowCaseIds(),
                categoryList = viewModel.getSelectedCategoryIds(),
                sort = viewModel.getSelectedSort()
        )
    }

    override fun onApplyCategoryFilter(selectedCategories: List<CategorySelection>) {
        if (selectedCategories.isNotEmpty()) categoryFilter?.type = ChipsUnify.TYPE_SELECTED
        else categoryFilter?.type = ChipsUnify.TYPE_NORMAL
        viewModel.setSelectedCategories(selectedCategories)
        viewModel.getProductList(
                keyword = viewModel.getSearchKeyWord(),
                shopId = userSession.shopId,
                warehouseLocationId = viewModel.getWarehouseLocationId(),
                shopShowCaseIds = viewModel.getSelectedShopShowCaseIds(),
                categoryList = viewModel.getSelectedCategoryIds(),
                sort = viewModel.getSelectedSort()
        )
    }

    override fun onApplySortFilter(selectedSort: List<SortSelection>) {
        if (selectedSort.isNotEmpty()) sortFilter?.type = ChipsUnify.TYPE_SELECTED
        else sortFilter?.type = ChipsUnify.TYPE_NORMAL
        viewModel.setSelectedSort(selectedSort)
        viewModel.getProductList(
                keyword = viewModel.getSearchKeyWord(),
                shopId = userSession.shopId,
                warehouseLocationId = viewModel.getWarehouseLocationId(),
                shopShowCaseIds = viewModel.getSelectedShopShowCaseIds(),
                categoryList = viewModel.getSelectedCategoryIds(),
                sort = viewModel.getSelectedSort()
        )
    }
}