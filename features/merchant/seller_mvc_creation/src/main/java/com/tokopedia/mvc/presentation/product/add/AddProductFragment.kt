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
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.campaign.entity.LoadingItem
import com.tokopedia.campaign.utils.extension.applyPaddingToLastItem
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.seller_mvc_creation.R
import com.tokopedia.seller_mvc_creation.databinding.SmvcFragmentAddProductBinding
import com.tokopedia.sortfilter.SortFilterItem
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

    private val productAdapter by lazy {
        CompositeAdapter.Builder()
            .add(ProductDelegateAdapter(onItemClick))
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

        viewModel.processEvent(
            AddProductEvent.FetchRequiredData(
                GetInitiateVoucherPageUseCase.Param.Action.CREATE,
                GetInitiateVoucherPageUseCase.Param.PromoType.CASHBACK
            )
        )
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
            adapter = productAdapter

            attachPaging(this, pagingConfig) { page, _ ->
                viewModel.processEvent(AddProductEvent.LoadPage(0L, page, "DEFAULT", "DESC"))
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
            uiState.selectedProducts.size,
            uiState.voucherCreationMetadata?.maxProduct.orZero()
        )
        binding?.btnAddProduct?.isEnabled = uiState.selectedProducts.isNotEmpty()
    }

    /*private fun renderSortFilter(uiState: FlashSaleListUiState) {
        if (!uiState.isFilterActive && uiState.allItems.isEmpty()) {
            binding?.sortFilter?.gone()
        } else {
            binding?.sortFilter?.visible()
        }
        renderSortChips(uiState.selectedSort)
        renderCategoryFilterChips(uiState.selectedCategoryIds)
        renderStatusChips(uiState.selectedStatusIds)
    }

    private fun renderSortChips(selectedSort: SingleSelectionItem) {
        if (selectedSort.id == "DEFAULT_VALUE_PLACEHOLDER") {
            sortChips.type = ChipsUnify.TYPE_NORMAL
            sortChips.selectedItem = arrayListOf(getString(R.string.stfs_sort))
        } else {
            sortChips.type = ChipsUnify.TYPE_SELECTED
            sortChips.selectedItem = arrayListOf(selectedSort.name)
        }
    }


    private fun renderCategoryFilterChips(selectedCategoryIds: List<Long>) {
        if (selectedCategoryIds.isEmpty()) {
            categoryChips.type = ChipsUnify.TYPE_NORMAL
            categoryChips.selectedItem = arrayListOf(getString(R.string.stfs_all_category))
        } else {
            categoryChips.type = ChipsUnify.TYPE_SELECTED
            categoryChips.selectedItem = arrayListOf(
                getString(
                    R.string.stfs_placeholder_selected_category_count,
                    selectedCategoryIds.size
                )
            )
        }
    }

    private fun renderStatusChips(selectedStatusIds: List<String>) {
        if (selectedStatusIds.isEmpty()) {
            statusChips.type = ChipsUnify.TYPE_NORMAL
            statusChips.selectedItem = arrayListOf(getString(R.string.stfs_all_status))
        } else {
            statusChips.type = ChipsUnify.TYPE_SELECTED
            statusChips.selectedItem = arrayListOf(
                getString(
                    R.string.stfs_placeholder_selected_status_count,
                    selectedStatusIds.size
                )
            )
        }
    }*/

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

        val items = arrayListOf(locationChips, categoryChips, showcaseChips)

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
}
