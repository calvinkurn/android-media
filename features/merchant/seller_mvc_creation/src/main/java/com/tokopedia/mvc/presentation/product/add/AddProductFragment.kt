package com.tokopedia.mvc.presentation.product.add

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.components.bottomsheet.selection.entity.MultipleSelectionItem
import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem
import com.tokopedia.campaign.components.bottomsheet.selection.multiple.MultipleSelectionBottomSheet
import com.tokopedia.campaign.components.bottomsheet.selection.single.SingleSelectionBottomSheet
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.campaign.utils.extension.applyPaddingToLastItem
import com.tokopedia.campaign.utils.extension.attachDividerItemDecoration
import com.tokopedia.campaign.utils.extension.enable
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentAddProductBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductCategoryOption
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.Warehouse
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.WarehouseType
import com.tokopedia.mvc.presentation.product.add.adapter.ProductAdapter
import com.tokopedia.mvc.presentation.product.add.adapter.filter.CategoryFilterAdapter
import com.tokopedia.mvc.presentation.product.add.adapter.filter.ProductSortAdapter
import com.tokopedia.mvc.presentation.product.add.adapter.filter.WarehouseFilterAdapter
import com.tokopedia.mvc.presentation.product.add.bottomsheet.ShowcaseFilterBottomSheet
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductEffect
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductEvent
import com.tokopedia.mvc.presentation.product.add.uimodel.AddProductUiState
import com.tokopedia.mvc.presentation.product.list.ProductListActivity
import com.tokopedia.mvc.presentation.product.variant.dialog.ConfirmationDialog
import com.tokopedia.mvc.presentation.product.variant.select.SelectVariantBottomSheet
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.mvc.util.tracker.AddProductTracker
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject


class AddProductFragment : BaseDaggerFragment(), HasPaginatedList by HasPaginatedListImpl() {

    companion object {
        const val PAGE_SIZE = 20
        private const val ONE_FILTER_SELECTED = 1

        @JvmStatic
        fun newInstance(
            pageMode: PageMode,
            voucherConfiguration: VoucherConfiguration,
            products: List<Product>
        ): AddProductFragment {
            return AddProductFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
                    putParcelable(
                        BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION,
                        voucherConfiguration
                    )
                    putParcelableArrayList(
                        BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS,
                        ArrayList(products)
                    )
                }
            }
        }

    }

    private val pageMode by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val voucherConfiguration by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration }
    private val selectedParentProducts by lazy { arguments?.getParcelableArrayList<Product>(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS) }

    private var binding by autoClearedNullable<SmvcFragmentAddProductBinding>()
    private val locationChips by lazy { SortFilterItem(getString(R.string.smvc_location)) }
    private val categoryChips by lazy { SortFilterItem(getString(R.string.smvc_category)) }
    private val showcaseChips by lazy { SortFilterItem(getString(R.string.smvc_showcase)) }
    private val sortChips by lazy { SortFilterItem(getString(R.string.smvc_sort)) }

    private val productAdapter by lazy {
        ProductAdapter(
            onCheckboxClick = onCheckboxClick,
            onCtaChangeVariantClick = onVariantClick,
            onVariantClick = onVariantClick
        )
    }


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession : UserSessionInterface

    @Inject
    lateinit var tracker: AddProductTracker

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
        setupView()
        observeUiEffect()
        observeUiState()

        viewModel.processEvent(
            AddProductEvent.FetchRequiredData(
                pageMode ?: return,
                voucherConfiguration ?: return,
                selectedParentProducts?.toList().orEmpty()
            )
        )
        registerBackPressEvent()
    }

    private fun registerBackPressEvent() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                tracker.sendClickButtonBackToPreviousPageEvent(
                    pageMode ?: return,
                    voucherConfiguration?.voucherId.orZero()
                )
                activity?.finish()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupView() {
        setupCheckbox()
        setupSortFilter()
        setupPaging()
        setupSearchBar()
        setupButton()
        setupToolbar()
        binding?.cardUnify2?.cardType = CardUnify2.TYPE_CLEAR
    }

    private fun setupToolbar() {
        binding?.header?.setNavigationOnClickListener {
            tracker.sendClickToolbarBackButtonEvent(
                pageMode ?: return@setNavigationOnClickListener,
                voucherConfiguration?.voucherId.orZero()
            )
            activity?.finish()
        }
        binding?.header?.headerSubTitle = getString(R.string.smvc_add_product_subtitle)
    }

    private fun setupButton() {
        binding?.btnAddProduct?.setOnClickListener {
            tracker.sendClickAddProductButtonEvent(
                pageMode ?: return@setOnClickListener,
                voucherConfiguration?.voucherId.orZero()
            )

            if (pageMode == PageMode.CREATE) {
                viewModel.processEvent(AddProductEvent.ConfirmAddProduct)
            } else {
                viewModel.processEvent(AddProductEvent.AddNewProducts)
            }
        }
    }

    private fun setupCheckbox() {
        binding?.checkbox?.setOnCheckedChangeListener { view, isChecked ->
            if (view.isClickTriggeredByUserInteraction()) {
                if (isChecked) {
                    viewModel.processEvent(AddProductEvent.EnableSelectAllCheckbox)
                } else {
                    viewModel.processEvent(AddProductEvent.DisableSelectAllCheckbox)
                }
            }
        }
    }

    private fun setupSearchBar() {
        binding?.searchBar?.searchBarTextField?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                resetPaging()
                val searchKeyword = binding?.searchBar?.searchBarTextField?.text.toString().trim()
                viewModel.processEvent(AddProductEvent.SearchProduct(searchKeyword))
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener false
        }
        binding?.searchBar?.clearListener = { viewModel.processEvent(AddProductEvent.ClearSearchBar) }
        binding?.searchBar?.searchBarPlaceholder = getString(R.string.smvc_search_product)
    }


    private fun setupPaging() {
        val pagingConfig = HasPaginatedList.Config(pageSize = PAGE_SIZE)

        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            applyPaddingToLastItem()
            attachDividerItemDecoration()
            addOnScrollListener(
                RecyclerViewScrollListener(
                    onScrollDown = { binding?.sortFilter?.visible() },
                    onScrollUp = { binding?.sortFilter?.gone() }
                )
            )
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
                val hasNextPage = effect.allItemCount < effect.totalProductCount
                notifyLoadResult(hasNextPage)
            }
            is AddProductEffect.ShowSortBottomSheet -> {
                showSortBottomSheet(effect.selectedSort, effect.sortOptions)
            }
            is AddProductEffect.ShowProductCategoryBottomSheet -> {
                showCategorySheet(effect.selectedCategories, effect.categories)
            }
            is AddProductEffect.ShowShowcasesBottomSheet -> {
                showShopShowcasesBottomSheet(effect.selectedShowcaseIds, effect.showcases)
            }
            is AddProductEffect.ShowWarehouseLocationBottomSheet -> {
                showWarehouseBottomSheet(effect.selectedWarehouse, effect.warehouses)
            }
            is AddProductEffect.ShowVariantBottomSheet -> {
                displayVariantBottomSheet(effect.selectedParentProduct)
            }
            is AddProductEffect.ProductConfirmed -> {
                ProductListActivity.start(
                    activity ?: return,
                    effect.voucherConfiguration,
                    effect.selectedProducts,
                    effect.selectedWarehouseId
                )
            }
            is AddProductEffect.AddNewProducts -> {
                returnSelectedProductsResultToProductListActivity(effect.selectedProducts)
            }
            is AddProductEffect.ShowError -> {
                binding?.cardUnify2?.showToasterError(effect.error)
            }
            is AddProductEffect.ShowChangeWarehouseDialogConfirmation -> {
                if (!isAdded) return

                ConfirmationDialog.show(
                    context = activity ?: return,
                    title = getString(R.string.smvc_change_location),
                    description = getString(R.string.smvc_change_location_description),
                    primaryButtonTitle = getString(R.string.smvc_confirm_change_location),
                    onPrimaryButtonClick = {
                        viewModel.processEvent(
                            AddProductEvent.ConfirmChangeWarehouseLocationFilter(effect.selectedWarehouseLocation)
                        )
                    }
                )
            }
        }
    }


    private fun handleUiState(uiState: AddProductUiState) {
        renderLoadingState(uiState.isLoading)

        renderSelectAllCheckbox(uiState.checkboxState, uiState.selectedProductCount, uiState.totalProducts)
        renderMaxProductSelection(uiState.maxProductSelection)

        //Filter
        renderSortChips(uiState.selectedSort)
        renderWarehouseLocationChips(uiState.selectedWarehouseLocation, uiState.defaultWarehouseLocationId)
        renderCategoryChips(uiState.selectedCategories)
        renderShopShowcaseChips(uiState.selectedShopShowcase)

        renderList(uiState.products)
        renderEmptyState(uiState.totalProducts, uiState.isLoading, uiState.isFilterActive, uiState.searchKeyword.isNotEmpty())

        renderBottomSection(uiState)
    }

    private fun renderMaxProductSelection(maxProductSelection : Int) {
        binding?.tpgMaxProductSelection?.text = getString(
            R.string.smvc_placeholder_max_selected_product,
            maxProductSelection
        )

    }

    private fun renderLoadingState(isLoading: Boolean) {
        binding?.loader?.isVisible = isLoading
    }

    private fun renderList(products: List<Product> ) {
        productAdapter.submit(products)

        if (products.isEmpty()) {
            resetPaging()
        }
    }

    private fun renderBottomSection(uiState: AddProductUiState) {
        binding?.tpgSelectedProductCount?.text = getString(
            R.string.smvc_placeholder_selected_product_count,
            uiState.selectedProductsIds.size,
            uiState.maxProductSelection.orZero()
        )
        binding?.btnAddProduct?.isEnabled = uiState.selectedProductsIds.isNotEmpty()
    }

    private fun renderEmptyState(
        totalProducts: Int,
        isLoading: Boolean,
        isFilterActive: Boolean,
        isOnSearchMode: Boolean
    ) {
        binding?.recyclerView?.isVisible = totalProducts.isMoreThanZero()
        binding?.cardUnify2?.isVisible = totalProducts.isMoreThanZero()
        binding?.checkbox?.isVisible = totalProducts.isMoreThanZero()
        binding?.dividerList?.isVisible = totalProducts.isMoreThanZero()
        binding?.tpgSelectAll?.isVisible = totalProducts.isMoreThanZero()
        binding?.tpgMaxProductSelection?.isVisible = totalProducts.isMoreThanZero()

        if (!isLoading){
            val emptySearchResult = totalProducts.isZero() && (isFilterActive || isOnSearchMode)
            binding?.emptyStateSearchResultNotFound?.isVisible = emptySearchResult

            val noRegisteredProduct = totalProducts.isZero() && !isFilterActive && !isOnSearchMode
            binding?.emptyStateNoRegisteredProduct?.isVisible = noRegisteredProduct
        }
    }

    private fun renderSelectAllCheckbox(
        checkboxState: AddProductUiState.CheckboxState,
        selectedProductCount: Int,
        totalProductCount: Int
    ) {
        when (checkboxState) {
            AddProductUiState.CheckboxState.CHECKED -> {
                binding?.checkbox?.isChecked = true
                binding?.checkbox?.setIndeterminate(false)
                binding?.checkbox?.skipAnimation()
            }
            AddProductUiState.CheckboxState.UNCHECKED -> {
                binding?.checkbox?.isChecked = false
                binding?.checkbox?.setIndeterminate(false)
                binding?.checkbox?.skipAnimation()
            }
            AddProductUiState.CheckboxState.INDETERMINATE -> {
                binding?.checkbox?.setIndeterminate(true)
                binding?.checkbox?.isChecked = true
                binding?.checkbox?.skipAnimation()
            }
        }

        val checkboxWording = if (checkboxState == AddProductUiState.CheckboxState.UNCHECKED) {
            getString(R.string.smvc_select_all)
        } else {
            getString(
                R.string.smvc_placeholder_check_all_selected_product_count,
                selectedProductCount,
                totalProductCount
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

    private fun renderWarehouseLocationChips(
        selectedWarehouse: Warehouse,
        defaultWarehouseId: Long
    ) {
        locationChips.type = ChipsUnify.TYPE_SELECTED

        val warehouseName = if (selectedWarehouse.warehouseId == defaultWarehouseId) {
            getString(R.string.smvc_seller_location)
        } else {
            selectedWarehouse.warehouseName
        }
        locationChips.selectedItem = arrayListOf(warehouseName)

    }

    private fun renderShopShowcaseChips(selectedShowcases: List<ShopShowcase>) {
        if (selectedShowcases.isEmpty()) {
            showcaseChips.type = ChipsUnify.TYPE_NORMAL
            showcaseChips.selectedItem = arrayListOf(getString(R.string.smvc_showcase))
        } else if (selectedShowcases.size == ONE_FILTER_SELECTED) {
            val selectedCategory = selectedShowcases.first()
            showcaseChips.type = ChipsUnify.TYPE_SELECTED
            showcaseChips.selectedItem = arrayListOf(selectedCategory.name)
        } else {
            showcaseChips.type = ChipsUnify.TYPE_SELECTED
            showcaseChips.selectedItem = arrayListOf(
                getString(
                    R.string.smvc_placeholder_selected_showcase_count,
                    selectedShowcases.size
                )
            )
        }
    }

    private fun renderCategoryChips(selectedCategories: List<ProductCategoryOption>) {
        if (selectedCategories.isEmpty()) {
            categoryChips.type = ChipsUnify.TYPE_NORMAL
            categoryChips.selectedItem = arrayListOf(getString(R.string.smvc_category))
        } else if (selectedCategories.size == ONE_FILTER_SELECTED) {
            val selectedCategory = selectedCategories.first()
            categoryChips.type = ChipsUnify.TYPE_SELECTED
            categoryChips.selectedItem = arrayListOf(selectedCategory.name)
        } else {
            categoryChips.type = ChipsUnify.TYPE_SELECTED
            categoryChips.selectedItem = arrayListOf(
                getString(
                    R.string.smvc_placeholder_selected_category_count,
                    selectedCategories.size
                )
            )
        }
    }

    private fun setupSortFilter() {
        val onLocationClicked = { viewModel.processEvent(AddProductEvent.TapWarehouseLocationFilter) }
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

    private val onCheckboxClick: (Int, Boolean) -> Unit = { selectedItemPosition, isChecked ->
        val selectedItem = productAdapter.snapshot()[selectedItemPosition]

        if (isChecked) {
            viewModel.processEvent(AddProductEvent.AddProductToSelection(selectedItem.id))
        } else {
            viewModel.processEvent(AddProductEvent.RemoveProductFromSelection(selectedItem.id))
        }
    }

    private val onVariantClick: (Int) -> Unit = { selectedItemPosition ->
        val selectedItem = productAdapter.snapshot()[selectedItemPosition]
        val selectedParentProduct = (selectedItem as? Product)

        selectedParentProduct?.run {
            viewModel.processEvent(AddProductEvent.TapVariant(this))
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

            viewModel.processEvent(AddProductEvent.ApplySortFilter(ProductSortOptions(newItem.id, newItem.name, newItem.direction)))

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

            val warehouseType = if (newItem.name == "Shop Location") {
                WarehouseType.DEFAULT_WAREHOUSE_LOCATION
            } else {
                WarehouseType.WAREHOUSE
            }

            val newlySelectedWarehouse = Warehouse(newItem.id.toLong(), newItem.name, warehouseType)
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

    private fun showCategorySheet(
        selectedCategories: List<ProductCategoryOption>,
        remoteCategory: List<ProductCategoryOption>
    ) {
        if (!isAdded) return

        val categoryFilterAdapter = CategoryFilterAdapter()

        val selectedCategoryIds = selectedCategories.map { category -> category.id }
        val categoriesOptions = remoteCategory.map { category ->
            MultipleSelectionItem(category.id, category.name, category.id in selectedCategoryIds)
        }

        val bottomSheet = MultipleSelectionBottomSheet.newInstance(selectedCategoryIds, categoriesOptions)

        bottomSheet.setBottomSheetTitle(getString(R.string.smvc_category))

        categoryFilterAdapter.setOnItemClicked { newItem ->
            bottomSheet.getBottomsheetView()?.btnApply?.enable()
            categoryFilterAdapter.markAsSelected(newItem)
        }

        bottomSheet.setOnApplyButtonClick {
            val selectedItem = categoryFilterAdapter.getSelectedItems()
            val newlySelectedCategories = selectedItem.map { ProductCategoryOption(it.id, it.name, it.name) }
            val event = AddProductEvent.ApplyCategoryFilter(newlySelectedCategories)
            viewModel.processEvent(event)
        }

        bottomSheet.setCustomAppearance {
            recyclerView.adapter = categoryFilterAdapter
            categoryFilterAdapter.submit(categoriesOptions)
        }

        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun showShopShowcasesBottomSheet(
        selectedShowcaseIds: List<Long>,
        remoteShopShowcases: List<ShopShowcase>
    ) {
        if (!isAdded) return

        val bottomSheet = ShowcaseFilterBottomSheet.newInstance(
            selectedShowcaseIds,
            remoteShopShowcases
        )
        bottomSheet.setOnApplyButtonClick { selectedShowcases ->
            val event = AddProductEvent.ApplyShowCaseFilter(selectedShowcases)
            viewModel.processEvent(event)
        }

        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun displayVariantBottomSheet(selectedParentProduct: Product) {
        val bottomSheet = SelectVariantBottomSheet.newInstance(selectedParentProduct)
        bottomSheet.setOnSelectButtonClick { selectedVariantIds ->
            viewModel.processEvent(AddProductEvent.VariantUpdated(selectedParentProduct.id, selectedVariantIds))
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun CompoundButton.isClickTriggeredByUserInteraction() : Boolean {
        return isPressed
    }

    private fun returnSelectedProductsResultToProductListActivity(selectedProducts: List<Product>) {
        val returnIntent = Intent()
        returnIntent.putParcelableArrayListExtra(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS, ArrayList(selectedProducts))
        activity?.setResult(Activity.RESULT_OK, returnIntent)
        activity?.finish()
    }

    private class RecyclerViewScrollListener(
        private val onScrollDown: () -> Unit = {},
        private val onScrollUp: () -> Unit = {}
    ) : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (dy.isScrollUp()) {
                onScrollUp()
            } else {
                onScrollDown()
            }
        }

        private fun Int.isScrollUp() : Boolean {
            return this < 0
        }
    }
}
