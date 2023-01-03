package com.tokopedia.tokofood.feature.search.initialstate.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivity
import com.tokopedia.tokofood.common.util.TokofoodErrorLogger
import com.tokopedia.tokofood.common.util.TokofoodExt.getGlobalErrorType
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.databinding.FragmentInitialStateFoodBinding
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment
import com.tokopedia.tokofood.feature.search.common.presentation.viewholder.TokofoodSearchErrorStateViewHolder
import com.tokopedia.tokofood.feature.search.common.util.OnScrollListenerSearch
import com.tokopedia.tokofood.feature.search.common.util.hideKeyboardOnTouchListener
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

class InitialSearchStateFragment : BaseDaggerFragment(), InitialStateListener,
    TokofoodSearchErrorStateViewHolder.Listener, TokofoodScrollChangedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytics: TokoFoodInitSearchStateAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private val initialSearchAdapterTypeFactory by lazy(LazyThreadSafetyMode.NONE) {
        InitialStateTypeFactoryImpl(this, this, this)
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
    private var onScrollChangedListenerList: MutableList<ViewTreeObserver.OnScrollChangedListener> =
        mutableListOf()
    private var cuisineScrollChangedListenerList: MutableList<ViewTreeObserver.OnScrollChangedListener> =
        mutableListOf()
    private var recentSearchScrollChangedListenerList: MutableList<ViewTreeObserver.OnScrollChangedListener> =
        mutableListOf()

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
        removeScrollChangedListener()
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

    override fun onRetry() {
        localCacheModel?.let { viewModel.fetchInitialState(it) }
    }

    override fun onGoToHome() {
        navigateToNewFragment(TokoFoodHomeFragment.createInstance())
        initialStateViewUpdateListener?.hideKeyboard()
    }

    override fun onChipsClicked(data: ChipsPopularSearch) {
        initialStateViewUpdateListener?.setKeywordSearchBarView(data.title)
        analytics.clickTopKeyword(keyword)
        initialStateViewUpdateListener?.hideKeyboard()
    }

    override fun onImpressionPopularSearch(item: ChipsPopularSearch, position: Int) {
        analytics.impressViewTopKeyword(
            item.title,
            localCacheModel?.district_id.orEmpty(),
            position,
        )
    }

    override fun setCuisineItemClicked(item: CuisineItemUiModel) {
        redirectToCategoryPage(item.appLink)
        analytics.clickCuisineList(keyword, item.title)
    }

    override fun onImpressCuisineItem(item: CuisineItemUiModel, position: Int) {
        analytics.impressViewCuisineItem(
            keyword,
            localCacheModel?.district_id.orEmpty(),
            position,
            item.title
        )
    }

    override fun onSetCuisineImpressionListener(listener: ViewTreeObserver.OnScrollChangedListener) {
        cuisineScrollChangedListenerList.add(listener)
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
        initialStateViewUpdateListener?.hideKeyboard()
    }

    override fun onImpressionRecentSearch(item: RecentSearchItemUiModel, position: Int) {
        analytics.impressViewSearchHistory(
            keyword,
            localCacheModel?.district_id.orEmpty(),
            position
        )
    }

    override fun onScrollChangedListenerAdded(onScrollChangedListener: ViewTreeObserver.OnScrollChangedListener) {
        onScrollChangedListenerList.add(onScrollChangedListener)
    }

    override fun onSeeMoreCuisineBtnClicked(element: SeeMoreCuisineUiModel) {
        initialSearchAdapter.removeSeeMoreButtonSection(element)
        initialSearchAdapter.expandInitialStateItem(element.cuisineMoreList)
    }

    override fun onSeeMoreRecentSearchBtnClicked(element: SeeMoreRecentSearchUiModel) {
        initialSearchAdapter.removeSeeMoreButtonSection(element)
        initialSearchAdapter.expandInitialStateItem(element.recentSearchMoreList)
    }

    fun setInitialStateViewUpdateListener(initialStateViewUpdateListener: InitialStateViewUpdateListener) {
        this.initialStateViewUpdateListener = initialStateViewUpdateListener
    }

    fun showInitialSearchState(keyword: String) {
        this.keyword = keyword
        this.initialStateViewUpdateListener?.showInitialStateView()
        localCacheModel?.let { viewModel.fetchInitialState(it) }
    }

    private fun initRecyclerView() {
        binding?.rvSearchInitialState?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = initialSearchAdapter
            addOnScrollListener(OnScrollListenerSearch(this))
            hideKeyboardOnTouchListener()
        }
    }

    private fun observeGetInitialState() {
        observe(viewModel.initialStateWrapper) {
            when (it) {
                is Success -> {
                    setInitialStateData(it.data.initialStateList)
                }
                is Fail -> {
                    initialSearchAdapter.removeAllInitialState()
                    initialSearchAdapter.showErrorState(it.throwable.getGlobalErrorType())
                    logExceptionToServerLogger(
                        it.throwable,
                        TokofoodErrorLogger.ErrorType.ERROR_INITIAL_SEARCH_STATE,
                        TokofoodErrorLogger.ErrorDescription.ERROR_INITIAL_SEARCH_STATE,
                    )
                }
            }
        }
    }

    private fun observeRemoveRecentSearch() {
        observe(viewModel.removeSearchHistory) {
            when (it) {
                is Fail -> {
                    logExceptionToServerLogger(
                        it.throwable,
                        TokofoodErrorLogger.ErrorType.ERROR_REMOVE_RECENT_SEARCH,
                        TokofoodErrorLogger.ErrorDescription.ERROR_REMOVE_RECENT_SEARCH,
                    )
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
        initialSearchAdapter.removeErrorState()
        initialSearchAdapter.setInitialStateList(initialStateList)
    }

    private fun navigateToNewFragment(fragment: Fragment) {
        navigateToNewFragment(fragment)
    }

    private fun logExceptionToServerLogger(
        throwable: Throwable,
        errorType: String,
        errorDesc: String
    ) {
        TokofoodErrorLogger.logExceptionToServerLogger(
            TokofoodErrorLogger.PAGE.SEARCH,
            throwable,
            errorType,
            userSession.deviceId.orEmpty(),
            errorDesc
        )
    }

    private fun removeScrollChangedListener() {
        onScrollChangedListenerList.forEach {
            view?.viewTreeObserver?.removeOnScrollChangedListener(it)
        }
        onScrollChangedListenerList.clear()
    }

    companion object {
        const val ALL_PARAM = "all"
    }
}
