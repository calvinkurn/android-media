package com.tokopedia.tkpd.flashsale.presentation.chooseproduct.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.campaign.components.adapter.ChooseProductAdapter
import com.tokopedia.campaign.components.adapter.ChooseProductDelegateAdapter
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.components.bottomsheet.selection.multiple.MultipleSelectionBottomSheet
import com.tokopedia.campaign.components.bottomsheet.selection.single.SingleSelectionBottomSheet
import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.campaign.utils.extension.slideDown
import com.tokopedia.campaign.utils.extension.slideUp
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentChooseProductBinding
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.ProductReserveResult
import com.tokopedia.tkpd.flashsale.presentation.bottomsheet.CampaignCriteriaCheckBottomSheet
import com.tokopedia.tkpd.flashsale.presentation.bottomsheet.CommonBottomSheetInitializer
import com.tokopedia.tkpd.flashsale.presentation.bottomsheet.DetailCategoryFlashSaleBottomSheet
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.adapter.CriteriaSelectionAdapter
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.FILTER_ALL_PRODUCTS
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.FILTER_PRODUCT_CRITERIA_PASSED
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.MAX_PER_PAGE
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.viewmodel.ChooseProductViewModel
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant
import com.tokopedia.tkpd.flashsale.presentation.manageproductlist.FlashSaleManageProductListActivity
import com.tokopedia.tkpd.flashsale.util.BaseSimpleListFragment
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class ChooseProductFragment : BaseSimpleListFragment<CompositeAdapter, ChooseProductItem>(),
    ChooseProductDelegateAdapter.ChooseProductListener,
    CriteriaSelectionAdapter.CriteriaSelectionAdapterListener {

    companion object {
        @JvmStatic
        fun newInstance(flashSaleId: Long, tabName: String): ChooseProductFragment {
            val fragment = ChooseProductFragment()
            val bundle = Bundle()
            bundle.putLong(BundleConstant.BUNDLE_FLASH_SALE_ID, flashSaleId)
            bundle.putString(BundleConstant.BUNDLE_KEY_TAB_NAME, tabName)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: ChooseProductViewModel
    @Inject
    lateinit var commonBottomSheetInitializer: CommonBottomSheetInitializer

    private val flashSaleId by lazy {
        arguments?.getLong(BundleConstant.BUNDLE_FLASH_SALE_ID).orZero()
    }
    private val tabName by lazy {
        arguments?.getString(BundleConstant.BUNDLE_KEY_TAB_NAME).orEmpty()
    }
    private val chooseProductAdapter = ChooseProductAdapter()
    private val criteriaSelectionAdapter = CriteriaSelectionAdapter(this)
    private val filterData = ArrayList<SortFilterItem>()
    private val filterCriteria by lazy { SortFilterItem(getString(R.string.chooseproduct_filter_criteria_text)) }
    private val filterCategory by lazy { SortFilterItem(getString(R.string.chooseproduct_filter_category_text)) }
    private var binding by autoClearedNullable<StfsFragmentChooseProductBinding>()
    private var criteriaCheckBottomSheet = CampaignCriteriaCheckBottomSheet()
    private var criteriaFilterBottomSheet: SingleSelectionBottomSheet? = null
    private var categoryFilterBottomSheet: MultipleSelectionBottomSheet? = null
    private var maxSelectedProductCount: Int = 0
    private var selectedProductCount: Int = 0

    override fun getScreenName(): String = ChooseProductFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StfsFragmentChooseProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.campaignId = flashSaleId
        viewModel.tabName = tabName
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        setupObservers()
        setupHeader()
        setupSearchBar()
        setupCategorySelection()
        setupFilterData()
        recyclerView?.attachOnScrollListener(onScrollDown = {
            binding?.layoutSearch?.slideDown()
        }, onScrollUp = {
            binding?.layoutSearch?.slideUp()
        })
        binding?.btnAddProduct?.setOnClickListener {
            viewModel.reserveProduct()
            viewModel.onAddButtonClicked(flashSaleId.toString())
        }
    }

    override fun onChooseProductClicked(index: Int, item: ChooseProductItem) {
        viewModel.setSelectedProduct(item)
        viewModel.updateCriteriaList(item)
    }

    override fun onDetailClicked(index: Int, item: ChooseProductItem) {
        criteriaCheckBottomSheet.setProductPreview(item.productName, item.imageUrl)
        viewModel.checkCriteria(item)
        viewModel.onCheckDetailButtonClicked(flashSaleId.toString(), item.productId)
    }

    override fun createAdapter(): CompositeAdapter {
        chooseProductAdapter.setListener(this)
        return chooseProductAdapter.getRecyclerViewAdapter()
    }

    override fun getRecyclerView(view: View): RecyclerView? = binding?.rvBundle

    override fun getPerPage(): Int = MAX_PER_PAGE

    override fun addElementToAdapter(list: List<ChooseProductItem>) {
        binding?.emptyStateProduct?.gone()
        chooseProductAdapter.addItems(list)
    }

    override fun loadData(page: Int, offset: Int) {
        viewModel.getProductList(page, getPerPage(), getSearchBarText())
    }

    override fun clearAdapterData() {
        chooseProductAdapter.submit(listOf())
    }

    override fun onShowLoading() {
        chooseProductAdapter.setLoading(true)
    }

    override fun onHideLoading() {
        chooseProductAdapter.setLoading(false)
    }

    override fun onDataEmpty() {
        binding?.emptyStateProduct?.visible()
    }

    override fun onGetListError(message: String) {
        view?.showToasterError(message)
    }

    override fun onCriteriaMoreClicked(
        categoryTitleComplete: String,
        selectionCount: Int,
        selectionCountMax: Int,
    ) {
        val bottomsheet = DetailCategoryFlashSaleBottomSheet.newInstance(categoryTitleComplete,
            selectionCount, selectionCountMax)
        bottomsheet.show(childFragmentManager, "")
    }

    private fun setupFilterData() {
        filterData.add(filterCriteria)
        filterData.add(filterCategory)
        binding?.sortFilter?.apply {
            addItem(filterData)
            parentListener = {
                filterCriteria.selectedItem = arrayListOf()
                filterCategory.selectedItem = arrayListOf()
                viewModel.filterCategory = emptyList()
                viewModel.filterCriteria = FILTER_PRODUCT_CRITERIA_PASSED
                loadInitialData()
            }
            dismissListener = parentListener
        }

        setupFilterCriteria()
        setupFilterCategory()
    }

    private fun setupFilterCategory() {
        val action = {
            val categoryData = viewModel.categoryAllList.value.orEmpty()
            val selectedData = viewModel.filterCategory.map { it.toString() }
            categoryFilterBottomSheet = commonBottomSheetInitializer.initFilterCategoryBottomSheet(selectedData, categoryData)
            categoryFilterBottomSheet?.apply {
                setOnApplyButtonClick {
                    viewModel.filterCategory = getAllSelectedItems().map { it.id.toLongOrZero() }
                    filterCategory.selectedItem = if (getAllSelectedItems().isEmpty()) {
                        arrayListOf(getString(R.string.stfs_all_category))
                    } else {
                        arrayListOf(getString(
                            R.string.stfs_placeholder_selected_category_count,
                            getAllSelectedItems().size)
                        )
                    }
                    filterCategory.refreshHighlight()
                    loadInitialData()
                }
            }
            categoryFilterBottomSheet?.show(childFragmentManager, "")
        }
        filterCategory.listener = { action() }
        filterCategory.refChipUnify.setChevronClickListener { action() }
    }

    private fun setupFilterCriteria() {
        val action = {
            criteriaFilterBottomSheet = commonBottomSheetInitializer.initCategoryFilterBottomSheet(viewModel.filterCriteria)
            criteriaFilterBottomSheet?.apply {
                setOnApplyButtonClick {
                    viewModel.filterCriteria = getSelectedItem()?.id.orEmpty()
                    filterCriteria.selectedItem = arrayListOf(getSelectedItem()?.name.orEmpty())
                    filterCriteria.refreshHighlight()
                    loadInitialData()
                }
            }
            criteriaFilterBottomSheet?.show(childFragmentManager, "")
        }
        filterCriteria.listener = { action() }
        filterCriteria.refChipUnify.setChevronClickListener { action() }
    }

    private fun setupCategorySelection() {
        binding?.rvCategory?.apply {
            adapter = criteriaSelectionAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        viewModel.getCriteriaList()
    }

    private fun setupSearchBar() {
        binding?.searchBar?.searchBarTextField?.apply {
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) loadInitialData()
                return@setOnEditorActionListener actionId == EditorInfo.IME_ACTION_SEARCH
            }
        }
    }

    private fun setupHeader() {
        binding?.header?.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setupObservers() {
        viewModel.productList.observe(viewLifecycleOwner) {
            renderList(it, viewModel.hasNextPage)
        }
        viewModel.criteriaList.observe(viewLifecycleOwner) {
            binding?.tvTitleCriteria?.isVisible = it.isNotEmpty()
            binding?.rvCategory?.isVisible = it.isNotEmpty()
            criteriaSelectionAdapter.setDataList(it)
        }
        viewModel.categoryAllList.observe(viewLifecycleOwner) {
            categoryFilterBottomSheet = commonBottomSheetInitializer.initFilterCategoryBottomSheet(emptyList(), it)
        }
        viewModel.isCriteriaEmpty.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.filterCriteria = FILTER_ALL_PRODUCTS
                filterCriteria.selectedItem = arrayListOf(getString(R.string.chooseproduct_filter_criteria_unfiltered_text))
                filterCriteria.refreshHighlight()
                loadInitialData()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            showGetListError(it)
        }
        viewModel.selectedProductCount.observe(viewLifecycleOwner) {
            selectedProductCount = it
            updateSelectionCount()
        }
        viewModel.maxSelectedProduct.observe(viewLifecycleOwner) {
            maxSelectedProductCount = it
            updateSelectionCount()
        }
        viewModel.productReserveResult.observe(viewLifecycleOwner) {
            handleReserveResult(it)
        }
        viewModel.criteriaCheckingResult.observe(viewLifecycleOwner) {
            showCriteriaCheckBottomSheet(it)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.validationResult.collectLatest {
                binding?.btnAddProduct?.isEnabled = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.selectionValidationResult.collectLatest {
                if (it.isExceedMaxQuota) chooseProductAdapter.disable(getString(R.string.chooseproduct_error_max_quota_item))
                else if (it.isExceedMaxProduct) chooseProductAdapter.disable(getString(R.string.chooseproduct_error_max_product_item))
                else chooseProductAdapter.enable()

                chooseProductAdapter.disableByCriteria(it.disabledCriteriaIds, getString(R.string.chooseproduct_error_max_criteria_item))
                chooseProductAdapter.forceEnableProduct(viewModel.submittedProductIds.map { id -> id.toString() })
            }
        }
    }

    private fun handleReserveResult(reserveResult: ProductReserveResult) {
        if (reserveResult.isSuccess) {
            redirectToFlashSaleManageProductListPage(reserveResult.reservationId)
        } else {
            view?.showToasterError(reserveResult.errorMessage)
        }
    }

    private fun redirectToFlashSaleManageProductListPage(reservationId: String) {
        activity?.finish()
        context?.let {
            FlashSaleManageProductListActivity.start(
                it,
                reservationId,
                flashSaleId.toString(),
                tabName
            )
        }
    }

    private fun updateSelectionCount() {
        binding?.tfSelectedProductCount?.text = "$selectedProductCount/$maxSelectedProductCount"
    }

    private fun getSearchBarText(): String {
        return binding?.searchBar?.searchBarTextField?.text?.toString().orEmpty()
    }

    private fun showCriteriaCheckBottomSheet(list: List<CriteriaCheckingResult>) {
        criteriaCheckBottomSheet.show(list, childFragmentManager, "")
    }

    private fun SortFilterItem.refreshHighlight() {
        type = if(selectedItem.isNotEmpty()) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
    }
}
