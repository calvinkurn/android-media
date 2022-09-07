package com.tokopedia.tkpd.flashsale.presentation.chooseproduct.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentChooseProductBinding
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.presentation.bottomsheet.CommonBottomSheetInitializer
import com.tokopedia.tkpd.flashsale.presentation.bottomsheet.DetailCategoryFlashSaleBottomSheet
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.adapter.CriteriaSelectionAdapter
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.FILTER_PRODUCT_CRITERIA_PASSED
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.MAX_PER_PAGE
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.viewmodel.ChooseProductViewModel
import com.tokopedia.tkpd.flashsale.presentation.common.constant.BundleConstant
import com.tokopedia.tkpd.flashsale.util.BaseSimpleListFragment
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ChooseProductFragment : BaseSimpleListFragment<CompositeAdapter, ChooseProductItem>(),
    ChooseProductDelegateAdapter.ChooseProductListener,
    CriteriaSelectionAdapter.CriteriaSelectionAdapterListener {

    companion object {
        @JvmStatic
        fun newInstance(campaignId: Long): ChooseProductFragment {
            val fragment = ChooseProductFragment()
            val bundle = Bundle()
            bundle.putLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
            fragment.arguments = bundle
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: ChooseProductViewModel
    @Inject
    lateinit var commonBottomSheetInitializer: CommonBottomSheetInitializer

    private var binding by autoClearedNullable<StfsFragmentChooseProductBinding>()
    private val campaignId by lazy {
        arguments?.getLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID).orZero()
    }
    private val chooseProductAdapter = ChooseProductAdapter()
    private val criteriaSelectionAdapter = CriteriaSelectionAdapter(this)
    private val filterData = ArrayList<SortFilterItem>()
    private val filterCriteria = SortFilterItem("Memenuhi Kriteria")
    private val filterCategory = SortFilterItem("Semua Kategori")
    private var criteriaFilterBottomSheet: SingleSelectionBottomSheet? = null
    private var categoryFilterBottomSheet: MultipleSelectionBottomSheet? = null

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
        viewModel.campaignId = campaignId
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupHeader()
        setupSearchBar()
        setupCategorySelection()
        setupFilterData()
    }

    override fun onChooseProductClicked(index: Int, item: ChooseProductItem, selected: Boolean) {
        println(item)
    }

    override fun onDetailClicked(index: Int) {
        println("click $index")
    }

    override fun createAdapter(): CompositeAdapter = chooseProductAdapter.getRecyclerViewAdapter()

    override fun getRecyclerView(view: View): RecyclerView? = binding?.rvBundle

    override fun getPerPage(): Int = MAX_PER_PAGE

    override fun addElementToAdapter(list: List<ChooseProductItem>) {
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
        view?.showToasterError("Data Kosong")
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
        binding?.sortFilter?.addItem(filterData)
        binding?.sortFilter?.parentListener = {
            filterCriteria.selectedItem = arrayListOf()
            filterCategory.selectedItem = arrayListOf()
            viewModel.filterCategory = emptyList()
            viewModel.filterCriteria = FILTER_PRODUCT_CRITERIA_PASSED
            loadInitialData()
        }

        setupFilterCriteria()
        setupFilterCategory()
    }

    private fun setupFilterCategory() {
        filterCategory.refChipUnify.setChevronClickListener {
            val categoryData = viewModel.categoryAllList.value.orEmpty()
            val selectedData = viewModel.filterCategory.map { it.toString() }
            categoryFilterBottomSheet = commonBottomSheetInitializer.initFilterCategoryBottomSheet(selectedData, categoryData)
            categoryFilterBottomSheet?.apply {
                setOnApplyButtonClick {
                    viewModel.filterCategory = getAllSelectedItems().map { it.id.toLongOrZero() }
                    filterCategory.selectedItem = ArrayList(getAllSelectedItems().map { it.name })
                    filterCategory.refreshHighlight()
                    loadInitialData()
                }
            }
            categoryFilterBottomSheet?.show(childFragmentManager, "")
        }
    }

    private fun setupFilterCriteria() {
        filterCriteria.refChipUnify.setChevronClickListener {
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
    }

    private fun setupCategorySelection() {
        binding?.rvCategory?.apply {
            adapter = criteriaSelectionAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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
            criteriaSelectionAdapter.setDataList(it)
        }
        viewModel.categoryAllList.observe(viewLifecycleOwner) {
            categoryFilterBottomSheet = commonBottomSheetInitializer.initFilterCategoryBottomSheet(emptyList(), it)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            showGetListError(it)
        }
    }

    private fun getSearchBarText(): String {
        return binding?.searchBar?.searchBarTextField?.text?.toString().orEmpty()
    }

    private fun SortFilterItem.refreshHighlight() {
        type = if(selectedItem.isNotEmpty()) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
    }
}