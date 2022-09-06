package com.tokopedia.tokofood.feature.search.searchresult.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodErrorStateViewHolder
import com.tokopedia.tokofood.databinding.FragmentSearchResultBinding
import com.tokopedia.tokofood.feature.search.container.presentation.listener.SearchResultViewUpdateListener
import com.tokopedia.tokofood.feature.search.searchresult.di.component.DaggerSearchResultComponent
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.TokofoodSearchResultAdapterTypeFactory
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.TokofoodSearchResultDiffer
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.TokofoodSearchResultPageAdapter
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder.MerchantSearchEmptyWithFilterViewHolder
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder.MerchantSearchEmptyWithoutFilterViewHolder
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder.MerchantSearchResultViewHolder
import com.tokopedia.tokofood.feature.search.searchresult.presentation.customview.TokofoodSearchFilterTab
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSearchUiEvent
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSortFilterItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel.TokofoodSearchResultPageViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SearchResultFragment : BaseDaggerFragment(), TokofoodSearchFilterTab.Listener,
    MerchantSearchResultViewHolder.TokoFoodMerchantSearchResultListener,
    TokoFoodErrorStateViewHolder.TokoFoodErrorStateListener,
    MerchantSearchEmptyWithFilterViewHolder.Listener,
    MerchantSearchEmptyWithoutFilterViewHolder.Listener,
    SortFilterBottomSheet.Callback,
    FilterGeneralDetailBottomSheet.Callback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokofoodSearchResultPageViewModel::class.java)
    }
    private val adapterTypeFactory by lazy(LazyThreadSafetyMode.NONE) {
        TokofoodSearchResultAdapterTypeFactory(this, this, this, this)
    }
    private val merchantResultAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val differ = TokofoodSearchResultDiffer()
        TokofoodSearchResultPageAdapter(adapterTypeFactory, differ)
    }
    private val loadMoreListener by lazy(LazyThreadSafetyMode.NONE) {
        createLoadMoreListener()
    }

    private var binding by autoClearedNullable<FragmentSearchResultBinding>()
    private var searchResultViewUpdateListener: SearchResultViewUpdateListener? = null

    private var tokofoodSearchFilterTab: TokofoodSearchFilterTab? = null
    private var searchParameter: SearchParameter? = null
    private var sortFilterBottomSheet: SortFilterBottomSheet? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
        collectFlows()
        setLocalCacheModel()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            DaggerSearchResultComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun onOpenFullFilterBottomSheet() {
        viewModel.openDetailFilterBottomSheet()
    }

    override fun onOpenQuickFilterBottomSheet(sortList: List<Sort>) {
        TODO("Not yet implemented")
    }

    override fun onOpenQuickFilterBottomSheet(filter: Filter) {
        showQuickFilterBottomSheet(filter)
    }

    override fun onSelectSortChip(sort: Sort, isSelected: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onSelectFilterChip(filter: Filter) {
        viewModel.applyFilter(filter)
    }

    override fun onClickRetryError() {
        TODO("Not yet implemented")
    }

    override fun onClickMerchant(merchant: Merchant, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onImpressMerchant(merchant: Merchant, position: Int) {
        // TODO: Add tracker
    }

    override fun onBranchButtonClicked(branchApplink: String) {
        TODO("Not yet implemented")
    }

    override fun onResetFilterButtonClicked() {
        viewModel.getInitialMerchantSearchResult(searchParameter)
    }

    override fun onCheckKeywordButtonClicked() {
        TODO("Not yet implemented")
    }

    override fun onSearchInTokopediaButtonClicked() {
        TODO("Not yet implemented")
    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        viewModel.resetParams(applySortFilterModel.selectedFilterMapParameter + applySortFilterModel.selectedSortMapParameter)
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        showDetailFilterApplyButton()
    }

    override fun onApplyButtonClicked(optionList: List<Option>?) {
        optionList?.let {
            viewModel.applyOptions(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tokofoodSearchFilterTab = null
        searchParameter = null
        sortFilterBottomSheet = null
    }

    fun setSearchResultViewUpdateListener(searchResultViewUpdateListener: SearchResultViewUpdateListener) {
        this.searchResultViewUpdateListener = searchResultViewUpdateListener
    }

    fun showSearchResultState(keyword: String) {
        this.searchResultViewUpdateListener?.showSearchResultView()
        viewModel.setKeyword(keyword)
    }

    private fun setupLayout() {
        setupAdapter()
    }

    private fun setupAdapter() {
        binding?.rvTokofoodSearchResult?.run {
            adapter = merchantResultAdapter
            context?.let {
                layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun collectFlows() {
        collectSearchParameters()
        collectSortFilterUiModels()
        collectVisitables()
        collectUiEvent()
        collectActiveFilterCount()
    }

    private fun collectSearchParameters() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.searchParameters.collect {
                searchParameter = it
                if (it != null) {
                    viewModel.loadQuickSortFilter(it)
                    viewModel.getInitialMerchantSearchResult(it)
                }
            }
        }
    }

    private fun collectSortFilterUiModels() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.sortFilterUiModel.collect { result ->
                when (result) {
                    is Success -> {
                        applySearchFilterTab(result.data)
                    }
                    is Fail -> {
                        showToasterError(result.throwable.message.orEmpty())
                    }
                }
            }
        }
    }

    private fun collectVisitables() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.visitables.collect { result ->
                removeScrollListener()
                when(result) {
                    is Success -> {
                        updateAdapterVisitables(result.data)
                    }
                    is Fail -> {
                        showToasterError(result.throwable.message.orEmpty())
                    }
                }
                addScrollListener()
            }
        }
    }

    private fun collectUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiEventFlow.collect { event ->
                when(event.state) {
                    TokofoodSearchUiEvent.EVENT_OPEN_DETAIL_BOTTOMSHEET -> {
                        onOpenDetailFilterBottomSheet(event.data)
                    }
                    TokofoodSearchUiEvent.EVENT_SUCCESS_LOAD_DETAIL_FILTER -> {
                        onSuccessLoadDetailFilter(event.data)
                    }
                    TokofoodSearchUiEvent.EVENT_FAILED_LOAD_DETAIL_FILTER -> {
                        onFailedLoadDetailFilter(event.throwable)
                    }
                }
            }
        }
    }

    private fun collectActiveFilterCount() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.appliedFilterCount.collect { count ->
                binding?.filterTokofoodSearchResult?.indicatorCounter = count
            }
        }
    }

    private fun setLocalCacheModel() {
        context?.let {
            val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it)
            viewModel.setLocalCacheModel(localCacheModel)
        }
    }

    private fun applySearchFilterTab(uiModels: List<TokofoodSortFilterItemUiModel>) {
        if (tokofoodSearchFilterTab == null && uiModels.isNotEmpty()) {
            binding?.filterTokofoodSearchResult?.let { sortFilter ->
                tokofoodSearchFilterTab = TokofoodSearchFilterTab(
                    sortFilter,
                    this
                )
            }
        } else {
            // TODO: Create method to change tab values
        }
        tokofoodSearchFilterTab?.setQuickFilter(uiModels)
    }

    private fun updateAdapterVisitables(visitables: List<Visitable<*>>) {
        binding?.rvTokofoodSearchResult?.post {
            merchantResultAdapter.submitList(visitables)
        }
    }

    private fun showToasterError(message: String) {
        view?.let {
            Toaster.build(
                it,
                message,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR
            ).show()
        }
    }

    private fun createLoadMoreListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScrollProductList()
            }
        }
    }

    private fun addScrollListener() {
        binding?.rvTokofoodSearchResult?.addOnScrollListener(loadMoreListener)
    }

    private fun removeScrollListener() {
        binding?.rvTokofoodSearchResult?.removeOnScrollListener(loadMoreListener)
    }

    private fun onScrollProductList() {
        val layoutManager = binding?.rvTokofoodSearchResult?.layoutManager as? LinearLayoutManager
        val lastVisibleItemIndex = layoutManager?.findLastVisibleItemPosition().orZero()
        val itemCount = layoutManager?.itemCount.orZero()
        viewModel.onScrollProductList(lastVisibleItemIndex, itemCount)
    }

    private fun onOpenDetailFilterBottomSheet(data: Any?) {
        val dynamicFilterModel = data as? DynamicFilterModel
        showDetailFilterBottomSheet(dynamicFilterModel)
    }

    private fun onSuccessLoadDetailFilter(data: Any?) {
        (data as? DynamicFilterModel)?.let { dynamicFilterModel ->
            sortFilterBottomSheet?.setDynamicFilterModel(dynamicFilterModel)
        }
    }

    private fun onFailedLoadDetailFilter(throwable: Throwable?) {
        sortFilterBottomSheet?.dismiss()
        throwable?.message?.let {
            showToasterError(it)
        }
    }

    private fun showDetailFilterBottomSheet(dynamicFilterModel: DynamicFilterModel?) {
        if (!isAdded) return
        if (sortFilterBottomSheet == null) {
            sortFilterBottomSheet = SortFilterBottomSheet()
        }
        sortFilterBottomSheet?.show(
            parentFragmentManager,
            searchParameter?.getSearchParameterHashMap(),
            dynamicFilterModel,
            this
        )
    }

    private fun showQuickSortBottomSheet(sort: List<Sort>) {

    }

    private fun showQuickFilterBottomSheet(filter: Filter) {
        FilterGeneralDetailBottomSheet().show(
            parentFragmentManager,
            filter,
            this
        )
    }

    private fun showDetailFilterApplyButton() {
        sortFilterBottomSheet?.setResultCountText(getApplyButtonText())
    }

    private fun getApplyButtonText(): String {
        return context?.getString(com.tokopedia.tokofood.R.string.search_srp_filter_apply).orEmpty()
    }

}