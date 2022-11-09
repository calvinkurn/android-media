package com.tokopedia.mvc.presentation.product.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.campaign.utils.extension.enable
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
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
                val nextPage = page + 1
                viewModel.processEvent(AddProductEvent.LoadPage(0L, nextPage, "DEFAULT", "DESC"))
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
        renderCheckbox(uiState)
    }

    private fun renderCheckbox(uiState: AddProductUiState) {
        val checkboxWording = if (!uiState.isSelectAllActive) {
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
        bottomSheet.setOnApplyButtonClick {
            val selectedItem = sortAdapter.getSelectedItem() ?: return@setOnApplyButtonClick
            viewModel.processEvent(AddProductEvent.ApplySortFilter(ProductSortOptions(selectedItem.id, selectedItem.name, selectedItem.direction)))
        }

        sortAdapter.setOnItemClicked { newItem ->
            sortAdapter.markAsSelected(newItem)
            bottomSheet.getBottomsheetView()?.btnApply?.enable()
        }

        bottomSheet.setCustomAppearance {
            recyclerView.adapter = sortAdapter
            sortAdapter.submit(sortOptions)
        }
        
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }
}
