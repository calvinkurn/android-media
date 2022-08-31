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
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.common.presentation.adapter.viewholder.TokoFoodErrorStateViewHolder
import com.tokopedia.tokofood.databinding.FragmentSearchResultBinding
import com.tokopedia.tokofood.feature.search.container.presentation.listener.SearchResultViewUpdateListener
import com.tokopedia.tokofood.feature.search.searchresult.di.component.DaggerSearchResultComponent
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.TokofoodSearchResultAdapterTypeFactory
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.TokofoodSearchResultDiffer
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.TokofoodSearchResultPageAdapter
import com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder.MerchantSearchResultViewHolder
import com.tokopedia.tokofood.feature.search.searchresult.presentation.customview.TokofoodSearchFilterTab
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.TokofoodSortFilterItemUiModel
import com.tokopedia.tokofood.feature.search.searchresult.presentation.viewmodel.TokofoodSearchResultPageViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SearchResultFragment : BaseDaggerFragment(), TokofoodSearchFilterTab.Listener,
    MerchantSearchResultViewHolder.TokoFoodMerchantSearchResultListener,
    TokoFoodErrorStateViewHolder.TokoFoodErrorStateListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokofoodSearchResultPageViewModel::class.java)
    }

    private val filterController by lazy(LazyThreadSafetyMode.NONE) {
        FilterController()
    }

    private val adapterTypeFactory by lazy(LazyThreadSafetyMode.NONE) {
        TokofoodSearchResultAdapterTypeFactory(this, this)
    }

    private val merchantResultAdapter by lazy(LazyThreadSafetyMode.NONE) {
        val differ = TokofoodSearchResultDiffer()
        TokofoodSearchResultPageAdapter(adapterTypeFactory, differ)
    }

    private var binding by autoClearedNullable<FragmentSearchResultBinding>()
    private var searchResultViewUpdateListener: SearchResultViewUpdateListener? = null

    private var tokofoodSearchFilterTab: TokofoodSearchFilterTab? = null
    private var searchParameter: SearchParameter? = null

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
        collectSearchParameters()
        collectSortFilterUiModels()
        collectVisitables()
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
        TODO("Not yet implemented")
    }

    override fun onOpenQuickFilterBottomSheet(sortList: List<Sort>) {
        TODO("Not yet implemented")
    }

    override fun onOpenQuickFilterBottomSheet(filter: Filter) {
        TODO("Not yet implemented")
    }

    override fun onSelectSortChip(sort: Sort) {
        TODO("Not yet implemented")
    }

    override fun onSelectFilterChip(option: Option) {
        TODO("Not yet implemented")
    }

    override fun onClickRetryError() {
        TODO("Not yet implemented")
    }

    override fun onClickMerchant(merchant: Merchant, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onImpressMerchant(merchant: Merchant, position: Int) {

    }

    override fun onBranchButtonClicked(branchApplink: String) {
        TODO("Not yet implemented")
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

    private fun collectSearchParameters() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.searchParameters.collect {
                searchParameter = it
                if (it != null) {
                    viewModel.loadSortFilter()
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
                        // TODO: Move to separate method
                        Toaster.build(
                            this@SearchResultFragment.requireView(),
                            result.throwable.message.orEmpty(),
                            Toaster.LENGTH_SHORT,
                            Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        }
    }

    private fun collectVisitables() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.visitables.collect { result ->
                when(result) {
                    is Success -> {
                        updateAdapterVisitables(result.data)
                    }
                    else -> {

                    }
                }
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
        if (tokofoodSearchFilterTab == null) {
            binding?.filterTokofoodSearchResult?.let { sortFilter ->
                tokofoodSearchFilterTab = TokofoodSearchFilterTab(
                    sortFilter,
                    filterController,
                    this
                )
            }
        } else {
            // TODO: Create method to change tab values
        }
        tokofoodSearchFilterTab?.setQuickFilter(uiModels)
    }

    private fun updateAdapterVisitables(visitables: List<Visitable<*>>) {
        merchantResultAdapter.submitList(visitables)
    }

}