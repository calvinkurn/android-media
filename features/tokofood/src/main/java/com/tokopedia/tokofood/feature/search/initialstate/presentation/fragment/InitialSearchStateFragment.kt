package com.tokopedia.tokofood.feature.search.initialstate.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.databinding.FragmentInitialStateFoodBinding
import com.tokopedia.tokofood.feature.search.container.presentation.listener.InitialStateViewUpdateListener
import com.tokopedia.tokofood.feature.search.di.component.DaggerTokoFoodSearchComponent
import com.tokopedia.tokofood.feature.search.initialstate.analytics.TokoFoodInitSearchStateAnalytics
import com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.InitialStateTypeFactoryImpl
import com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.TokoFoodInitStateAdapter
import com.tokopedia.tokofood.feature.search.initialstate.presentation.listener.InitialStateListener
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.BaseInitialStateVisitable
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.ChipsPopularSearch
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.CuisineItemUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.RecentSearchItemUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.SeeMoreCuisineUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.SeeMoreRecentSearchUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.viewmodel.InitialStateSearchViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class InitialSearchStateFragment : BaseDaggerFragment(), InitialStateListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytics: TokoFoodInitSearchStateAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private val initialSearchAdapterTypeFactory by lazy(LazyThreadSafetyMode.NONE) {
        InitialStateTypeFactoryImpl(this)
    }

    private val initialSearchAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TokoFoodInitStateAdapter(
            initialSearchAdapterTypeFactory
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InitialStateSearchViewModel::class.java)
    }

    private var binding by autoClearedNullable<FragmentInitialStateFoodBinding>()

    private var initialStateViewUpdateListener: InitialStateViewUpdateListener? = null
    private var localCacheModel: LocalCacheModel? = null
    private var keyword: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInitialStateFoodBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        updateLocalCacheModelData()
        observeGetInitialState()
        observeRemoveRecentSearch()
    }

    override fun onDestroyView() {
        initialStateViewUpdateListener = null
        localCacheModel = null
        super.onDestroyView()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodSearchComponent
                .builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun onChipsClicked(data: ChipsPopularSearch) {
        initialStateViewUpdateListener?.setKeywordSearchBarView(data.title)
        analytics.clickTopKeyword(keyword)
    }

    override fun onImpressionPopularSearch(item: ChipsPopularSearch, position: Int) {
        analytics.impressViewTopKeyword(
            keyword,
            localCacheModel?.district_id.orEmpty(),
            position,
        )
    }

    override fun setCuisineItemClicked(item: CuisineItemUiModel) {
        redirectToCategoryPage(item.appLink)
        analytics.clickCuisineList(keyword)
    }

    override fun onImpressCuisineItem(item: CuisineItemUiModel, position: Int) {
        analytics.impressViewCuisineItem(
            keyword,
            localCacheModel?.district_id.orEmpty(),
            position,
            item.title
        )
    }

    override fun onHeaderAllRemovedClicked(labelAction: String) {
        initialSearchAdapter.removeAllRecentSearchSection()
        viewModel.removeRemoveRecentSearch(ALL_PARAM)
    }

    override fun onDeleteRecentSearchClicked(itemId: String, position: Int) {
        initialSearchAdapter.removeRecentSearchItem(position)
        viewModel.removeRemoveRecentSearch(itemId)
    }

    override fun onRecentSearchItemClicked(title: String) {
        initialStateViewUpdateListener?.setKeywordSearchBarView(title)
        analytics.clickSearchHistory(keyword)
    }

    override fun onImpressionRecentSearch(item: RecentSearchItemUiModel, position: Int) {
        analytics.impressViewSearchHistory(
            keyword,
            localCacheModel?.district_id.orEmpty(),
            position
        )
    }

    override fun onSeeMoreCuisineBtnClicked(element: SeeMoreCuisineUiModel) {
        initialSearchAdapter.removeSeeMoreButtonSection(element)
        initialSearchAdapter.expandInitialStateItem(element.cuisineMoreList)
    }

    override fun onSeeMoreRecentSearchBtnClicked(element: SeeMoreRecentSearchUiModel) {
        initialSearchAdapter.removeSeeMoreButtonSection(element)
        initialSearchAdapter.expandInitialStateItem(element.recentSearchMoreList)
    }

    private fun initRecyclerView() {
        binding?.rvSearchInitialState?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = initialSearchAdapter
        }
    }

    private fun observeGetInitialState() {
        observe(viewModel.initialStateWrapper) {
            when (it) {
                is Success -> {
                    setInitialStateData(it.data.initialStateList)
                }
                is Fail -> {
                    //TODO send server logger
                }
            }
        }
    }

    private fun observeRemoveRecentSearch() {
        observe(viewModel.removeSearchHistory) {
            when (it) {
                is Fail -> {
                    //TODO send server logger
                }
            }
        }
    }

    private fun redirectToCategoryPage(appLink: String) {
        context?.let {
            TokofoodRouteManager.routePrioritizeInternal(it, appLink)
        }
    }

    private fun updateLocalCacheModelData() {
        context?.let {
            localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private fun setInitialStateData(initialStateList: List<BaseInitialStateVisitable>) {
        initialSearchAdapter.setInitialStateList(initialStateList)
    }

    fun setInitialStateViewUpdateListener(initialStateViewUpdateListener: InitialStateViewUpdateListener) {
        this.initialStateViewUpdateListener = initialStateViewUpdateListener
    }

    fun showInitialSearchState(keyword: String) {
        this.keyword = keyword
        this.initialStateViewUpdateListener?.showInitialStateView()
        localCacheModel?.let { viewModel.fetchInitialState(it) }
    }

    companion object {
        const val ALL_PARAM = "all"
    }
}