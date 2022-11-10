package com.tokopedia.mvc.presentation.product.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.components.adapter.LoadingDelegateAdapter
import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem
import com.tokopedia.campaign.components.bottomsheet.selection.single.SingleSelectionBottomSheet
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.campaign.entity.LoadingItem
import com.tokopedia.campaign.utils.extension.applyPaddingToLastItem
import com.tokopedia.campaign.utils.extension.attachDividerItemDecoration
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.ProductCategoryOption
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.mvc.domain.entity.Warehouse
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.entity.enums.WarehouseType
import com.tokopedia.mvc.presentation.product.add.adapter.ProductDelegateAdapter
import com.tokopedia.mvc.presentation.product.add.adapter.ProductSortAdapter
import com.tokopedia.mvc.presentation.product.add.adapter.WarehouseFilterAdapter
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductEffect
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductEvent
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductUiState
import com.tokopedia.mvc.util.constant.NumberConstant
import com.tokopedia.seller_mvc_creation.R
import com.tokopedia.seller_mvc_creation.databinding.SmvcFragmentAddProductBinding
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class AddProductFragment : BaseDaggerFragment(), HasPaginatedList by HasPaginatedListImpl() {

    companion object {
        const val PAGE_SIZE = 10
        private const val BUNDLE_KEY_COUPON_ID = "couponId"

        @JvmStatic
        fun newInstance(couponId: Long): AddProductFragment {
            return AddProductFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }

    }

    private val couponId by lazy { arguments?.getLong(BUNDLE_KEY_COUPON_ID, 0) }
    private var binding by autoClearedNullable<SmvcFragmentAddProductBinding>()
    private val locationChips by lazy { SortFilterItem(getString(R.string.smvc_location)) }
    private val categoryChips by lazy { SortFilterItem(getString(R.string.smvc_category)) }
    private val showcaseChips by lazy { SortFilterItem(getString(R.string.smvc_showcase)) }
    private val sortChips by lazy { SortFilterItem(getString(R.string.smvc_sort)) }

    private val productAdapter by lazy {
        CompositeAdapter.Builder()
            .add(ProductDelegateAdapter(onItemClick, onCheckboxClick))
            .add(LoadingDelegateAdapter())
            .build()
    }


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(AddProductViewModel::class.java) }


    override fun getScreenName(): String = AddProductFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentAddProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        setupView()
        observeUiEffect()
        observeUiState()

        viewModel.processEvent(AddProductEvent.FetchRequiredData(VoucherAction.CREATE, PromoType.CASHBACK))
    }

    private fun setupView() {
        setupCheckbox()
        setupSortFilter()
        setupPaging()
        setupSearchBar()
    }

    private fun setupCheckbox() {
        binding?.checkbox?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.processEvent(AddProductEvent.EnableSelectAllCheckbox)
            } else {
                viewModel.processEvent(AddProductEvent.DisableSelectAllCheckbox)
            }
        }
    }

    private fun setupSearchBar() {
        binding?.searchBar?.searchBarTextField?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                resetPaging()
                viewModel.processEvent(AddProductEvent.LoadPage(NumberConstant.FIRST_PAGE))
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener false
        }
        binding?.searchBar?.clearListener = { viewModel.processEvent(AddProductEvent.ClearSearchBar) }
        binding?.searchBar?.searchBarPlaceholder = getString(R.string.smvc_search_product)
    }


    private fun setupPaging() {
        val pagingConfig = HasPaginatedList.Config(
            pageSize = PAGE_SIZE,
            onLoadNextPage = {
                productAdapter.addItem(LoadingItem)
            }, onLoadNextPageFinished = {
                productAdapter.removeItem(LoadingItem)
            })

        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            applyPaddingToLastItem()
            attachDividerItemDecoration()
            adapter = productAdapter

            attachPaging(this, pagingConfig) { page, _ ->
                val nextPage = page.inc()
                viewModel.processEvent(AddProductEvent.LoadPage(nextPage))
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state -> handleUiState(state) }
        }
    }

    private fun observeUiEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEffect.collect { event -> handleEffect(event) }
        }
    }

    private fun handleEffect(effect: AddProductEffect) {
        when (effect) {
            is AddProductEffect.LoadNextPageSuccess -> {
                val hasNextPage = effect.currentPageItems.size == PAGE_SIZE
                notifyLoadResult(hasNextPage)
                productAdapter.submit(effect.allItems)
            }
            is AddProductEffect.ShowSortBottomSheet -> {
                showSortBottomSheet(effect.selectedSort, effect.sortOptions)
            }
            is AddProductEffect.ShowProductCategoryBottomSheet -> {}
            is AddProductEffect.ShowShowcasesBottomSheet -> {}
            is AddProductEffect.ShowWarehouseLocationBottomSheet -> {
                showWarehouseBottomSheet(effect.selectedWarehouse, effect.warehouses)
            }
        }
    }


    private fun handleUiState(uiState: AddProductUiState) {
        binding?.loader?.isVisible = uiState.isLoading
        binding?.tpgMaxProductSelection?.text = getString(
            R.string.smvc_placeholder_max_selected_product,
            uiState.voucherCreationMetadata?.maxProduct.orZero()
        )
        binding?.tpgSelectedProductCount?.text = getString(
            R.string.smvc_placeholder_selected_product_count,
            uiState.selectedProductsIds.size,
            uiState.totalProducts.orZero()
        )
        binding?.btnAddProduct?.isEnabled = uiState.selectedProductsIds.isNotEmpty()

        productAdapter.submit(uiState.products)

        renderSortChips(uiState.selectedSort)
        renderWarehouseLocationChips(uiState.selectedWarehouseLocation)
        renderCategoryChips(uiState.selectedCategory)
        renderShopShowcaseChips(uiState.selectedShopShowcase)
        renderCheckbox(uiState)
    }

    private fun renderCheckbox(uiState: AddProductUiState) {
        val checkboxWording = if (uiState.selectedProductsIds.isEmpty()) {
            getString(R.string.smvc_select_all)
        } else {
            getString(
                R.string.smvc_placeholder_check_all_selected_product_count,
                uiState.selectedProductsIds.size,
                uiState.totalProducts
            )
        } 
        binding?.tpgSelectAll?.text = checkboxWording
    }

    private fun renderSortChips(selectedSort: ProductSortOptions) {
        if (selectedSort.id == "DEFAULT") {
            sortChips.type = ChipsUnify.TYPE_NORMAL
            sortChips.selectedItem = arrayListOf(getString(R.string.smvc_sort))
        } else {
            sortChips.type = ChipsUnify.TYPE_SELECTED
            sortChips.selectedItem = arrayListOf(selectedSort.name)
        }
    }

    private fun renderWarehouseLocationChips(selectedWarehouse: Warehouse) {
        if (selectedWarehouse.warehouseName.isEmpty()) {
            locationChips.type = ChipsUnify.TYPE_NORMAL
            locationChips.selectedItem = arrayListOf(getString(R.string.smvc_sort))
        } else {
            locationChips.type = ChipsUnify.TYPE_SELECTED
            val warehouseName = if (selectedWarehouse.warehouseType == WarehouseType.DEFAULT_WAREHOUSE_LOCATION) {
                getString(R.string.smvc_location)
            } else {
                selectedWarehouse.warehouseName
            }
            locationChips.selectedItem = arrayListOf(warehouseName)
        }
    }

    private fun renderShopShowcaseChips(selectedShowcase: ShopShowcase) {
        if (selectedShowcase.name.isEmpty()) {
            showcaseChips.type = ChipsUnify.TYPE_NORMAL
            showcaseChips.selectedItem = arrayListOf(getString(R.string.smvc_showcase))
        } else {
            showcaseChips.type = ChipsUnify.TYPE_SELECTED
            showcaseChips.selectedItem = arrayListOf(selectedShowcase.name)
        }
    }

    private fun renderCategoryChips(selectedCategory: ProductCategoryOption) {
        if (selectedCategory.name.isEmpty()) {
            categoryChips.type = ChipsUnify.TYPE_NORMAL
            categoryChips.selectedItem = arrayListOf(getString(R.string.smvc_category))
        } else {
            categoryChips.type = ChipsUnify.TYPE_SELECTED
            categoryChips.selectedItem = arrayListOf(selectedCategory.name)
        }
    }

    private fun setupSortFilter() {
        val onLocationClicked = { viewModel.processEvent(AddProductEvent.TapLocationFilter) }
        locationChips.listener = { onLocationClicked() }
        locationChips.chevronListener = { onLocationClicked() }

        val onCategoryClicked = { viewModel.processEvent(AddProductEvent.TapCategoryFilter) }
        categoryChips.listener = { onCategoryClicked() }
        categoryChips.chevronListener = { onCategoryClicked() }


        val onShowCaseClicked = { viewModel.processEvent(AddProductEvent.TapShowCaseFilter) }
        showcaseChips.listener = { onShowCaseClicked() }
        showcaseChips.chevronListener = { onShowCaseClicked() }

        val onSortClicked = { viewModel.processEvent(AddProductEvent.TapSortFilter) }
        sortChips.listener = { onSortClicked() }
        sortChips.chevronListener = { onSortClicked() }

        val items = arrayListOf(locationChips, categoryChips, showcaseChips, sortChips)

        binding?.sortFilter?.addItem(items)
        binding?.sortFilter?.parentListener = {}
        binding?.sortFilter?.dismissListener = {
           viewModel.processEvent(AddProductEvent.ClearFilter)
        }
    }

    private val onItemClick: (Int) -> Unit = { selectedItemPosition ->
        val selectedItem = productAdapter.getItems()[selectedItemPosition]
        val selectedItemId = (selectedItem.id() as? Long).orZero()
    }


    private val onCheckboxClick: (Int, Boolean) -> Unit = { selectedItemPosition, isChecked ->
        val selectedItem = productAdapter.getItems()[selectedItemPosition]
        val selectedItemId = (selectedItem.id() as? Long).orZero()

        if (isChecked) {
            viewModel.processEvent(AddProductEvent.AddProductToSelection(selectedItemId))
        } else {
            viewModel.processEvent(AddProductEvent.RemoveProductFromSelection(selectedItemId))
        }
    }

    private fun showSortBottomSheet(
        selectedSort: ProductSortOptions,
        remoteSortOptions: List<ProductSortOptions>
    ) {
        if (!isAdded) return
        val sortAdapter = ProductSortAdapter()

        val sortOptions = remoteSortOptions.map { sort ->
            val isSelected = sort.id == selectedSort.id
            SingleSelectionItem(sort.id, sort.name, isSelected, sort.value)
        }

        val bottomSheet = SingleSelectionBottomSheet.newInstance(selectedSort.id, sortOptions)

        bottomSheet.setBottomSheetTitle(getString(R.string.smvc_sort))

        sortAdapter.setOnItemClicked { newItem ->
            sortAdapter.markAsSelected(newItem)

            val selectedItem = sortAdapter.getSelectedItem() ?: return@setOnItemClicked
            viewModel.processEvent(AddProductEvent.ApplySortFilter(ProductSortOptions(selectedItem.id, selectedItem.name, selectedItem.direction)))

            bottomSheet.dismiss()
        }

        bottomSheet.setCustomAppearance {
            recyclerView.adapter = sortAdapter
            button.gone()
            sortAdapter.submit(sortOptions)
        }
        
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showWarehouseBottomSheet(selectedWarehouse: Warehouse, remoteWarehouseOptions: List<Warehouse>) {
        if (!isAdded) return

        val warehouseFilterAdapter = WarehouseFilterAdapter()

        val warehouseOptions = remoteWarehouseOptions.map { sort ->
            val isSelected = sort.warehouseId == selectedWarehouse.warehouseId
            val warehouseName = if (sort.warehouseType == WarehouseType.DEFAULT_WAREHOUSE_LOCATION) {
                getString(R.string.smvc_seller_location)
            } else {
                sort.warehouseName
            }

            SingleSelectionItem(sort.warehouseId.toString(), warehouseName, isSelected, "")
        }

        val bottomSheet = SingleSelectionBottomSheet.newInstance(
            selectedWarehouse.warehouseId.toString(),
            warehouseOptions
        )

        bottomSheet.setBottomSheetTitle(getString(R.string.smvc_location))

        warehouseFilterAdapter.setOnItemClicked { newItem ->
            warehouseFilterAdapter.markAsSelected(newItem)

            val selectedItem = warehouseFilterAdapter.getSelectedItem() ?: return@setOnItemClicked
            val warehouseType = if (selectedItem.name == "Shop Location") {
                WarehouseType.DEFAULT_WAREHOUSE_LOCATION
            } else {
                WarehouseType.WAREHOUSE
            }

            val newlySelectedWarehouse = Warehouse(selectedItem.id.toLong(), selectedItem.name, warehouseType)
            val event = AddProductEvent.ApplyWarehouseLocationFilter(newlySelectedWarehouse)
            viewModel.processEvent(event)

            bottomSheet.dismiss()
        }

        bottomSheet.setCustomAppearance {
            recyclerView.adapter = warehouseFilterAdapter
            button.gone()
            warehouseFilterAdapter.submit(warehouseOptions)
        }

        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }
}
