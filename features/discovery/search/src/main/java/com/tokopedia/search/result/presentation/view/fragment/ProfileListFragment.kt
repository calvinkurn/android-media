package com.tokopedia.search.result.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.GCM.GCM_ID
import com.tokopedia.discovery.common.constants.SearchConstant.GCM.GCM_STORAGE
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.search.R
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.presentation.ProfileListSectionContract
import com.tokopedia.search.result.presentation.model.ProfileListViewModel
import com.tokopedia.search.result.presentation.model.ProfileViewModel
import com.tokopedia.search.result.presentation.model.TotalSearchCountViewModel
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.search.result.presentation.view.listener.ProfileListener
import com.tokopedia.search.result.presentation.view.listener.RedirectionListener
import com.tokopedia.search.result.presentation.view.listener.SearchNavigationListener
import com.tokopedia.search.result.presentation.view.typefactory.ProfileListTypeFactory
import com.tokopedia.search.result.presentation.view.typefactory.ProfileListTypeFactoryImpl
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import java.text.DecimalFormat
import javax.inject.Inject

class ProfileListFragment :
    BaseListFragment<ProfileViewModel, ProfileListTypeFactory>(),
    ProfileListSectionContract.View,
    ProfileListener,
    EmptyStateListener {

    companion object {
        private const val EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER"
        private const val SCREEN_SEARCH_PAGE_PROFILE_TAB = "Search result - Profile tab"

        fun newInstance(searchParameter: SearchParameter): ProfileListFragment {
            val args = Bundle()
            args.putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter)

            val profileListFragment = ProfileListFragment()
            profileListFragment.arguments = args

            return profileListFragment
        }
    }

    @Inject
    lateinit var presenter: ProfileListSectionContract.Presenter

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    lateinit var searchNavigationListener: SearchNavigationListener

    lateinit var redirectionListener: RedirectionListener

    lateinit var trackingQueue: TrackingQueue

    var query : String = ""

    var searchParameter: SearchParameter? = null

    var hasLoadData = false

    override fun getRecyclerViewResourceId(): Int {
        return R.id.profile_list_recycler_view
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.profile_list_swipe_refresh_layout
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        context?.let {
            attachNavigationListener(it)
            attachRedirectionListener(it)
        }
    }

    private fun attachNavigationListener(context: Context) {
        if (context is SearchNavigationListener) {
            this.searchNavigationListener = context
        }
    }

    private fun attachRedirectionListener(context: Context) {
        if (context is RedirectionListener) {
            this.redirectionListener = context
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (userVisibleHint && ::searchNavigationListener.isInitialized) {
            searchNavigationListener.hideBottomNavigation()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser && canStartToLoadData()) {
            onSwipeRefresh()
        }
        if (isVisibleToUser && view != null && ::searchNavigationListener.isInitialized) {
            searchNavigationListener.hideBottomNavigation()
        }
    }

    private fun canStartToLoadData(): Boolean {
        return !hasLoadData
                && isAdded
                && ::presenter.isInitialized
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            trackingQueue = TrackingQueue(activity!!)
        }
        if (savedInstanceState != null) {
            loadDataFromSavedState(savedInstanceState)
        } else if (arguments != null) {
            loadDataFromSavedState(arguments!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter.attachView(this)
        presenter.initInjector()

        return inflater.inflate(R.layout.search_fragment_profile_list, container, false)
    }

    override fun onPause() {
        super.onPause()
        if (::trackingQueue.isInitialized) {
            trackingQueue.sendAll()
        }
    }

    override fun onSuccessGetProfileListData(profileListViewModel : ProfileListViewModel) {
        trackImpressionProfileList(profileListViewModel)

        renderList(profileListViewModel.profileModelList, presenter.getHasNextPage())
    }

    private fun trackImpressionProfileList(profileListViewModel: ProfileListViewModel) {
        if (profileListViewModel.getListTrackingObject().isNotEmpty()) {
            if (::trackingQueue.isInitialized) {
                SearchTracking.eventUserImpressionProfileResultInTabProfile(
                    trackingQueue,
                    profileListViewModel.getListTrackingObject(),
                    query
                )
            }
        }
    }

    @Deprecated("These logics need to be put in Presenter and Unit Tested")
    override fun renderList(list: List<ProfileViewModel>, hasNextPage: Boolean) {
        hideLoading()
        clearVisitableList()
        addTotalProfileCountViewModel()
        renderVisitableList(list)
        isLoadingInitialData = false
    }

    override fun clearVisitableList() {
        if (isLoadingInitialData) {
            clearAllData()
        } else {
            adapter.clearAllNonDataElement()
        }
    }

    private fun addTotalProfileCountViewModel() {
        val nextPage = presenter.getNextPage()
        val totalProfileCount = presenter.getTotalProfileCount()
        if (nextPage == 1 && totalProfileCount != 0) {
            adapter.addElement(TotalSearchCountViewModel(createTotalCountText(totalProfileCount)))
        }
    }

    override fun renderVisitableList(visitableList: List<Visitable<*>>) {
        adapter.addElement(visitableList)
        updateScrollListenerState(presenter.getHasNextPage())
    }

    private fun createTotalCountText(totalCount : Int) : String {
        val formatter = DecimalFormat("#,###,###")
        return formatter.format(totalCount)
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return true
    }

    override fun getAdapterTypeFactory(): ProfileListTypeFactory {
        return ProfileListTypeFactoryImpl(this, this)
    }

    override fun onItemClicked(t: ProfileViewModel?) {
    }

    override fun getScreenName(): String {
        return SCREEN_SEARCH_PAGE_PROFILE_TAB
    }

    override fun initInjector() {
        DaggerProfileListViewComponent.builder()
            .baseAppComponent(getBaseAppComponent())
            .build()
            .inject(this)
    }

    override fun loadData(page: Int) {
        hasLoadData = true
        presenter.requestProfileListData(query, page)
    }

    override fun onSuccessToggleFollow(adapterPosition: Int, enable: Boolean) {
        if (adapter.data.get(adapterPosition) is ProfileViewModel) {
            (adapter.data.get(adapterPosition) as ProfileViewModel).followed = enable
            adapter.notifyItemChanged(adapterPosition)
        }
    }

    override fun onErrorToggleFollow() {
        activity?.run {
            NetworkErrorHelper.showSnackbar(activity)
        }
    }

    override fun onErrorToggleFollow(errorMessage: String) {
        activity?.run {
            NetworkErrorHelper.showSnackbar(activity, errorMessage)
        }
    }

    override fun onFollowButtonClicked(adapterPosition: Int, profileModel: ProfileViewModel) {
        SearchTracking.eventClickFollowActionProfileResultProfileTab(
            context,
            query,
            !profileModel.followed,
            profileModel.name,
            profileModel.id,
            adapterPosition
        )

        when (userSessionInterface.isLoggedIn) {
            true -> {
                presenter.handleFollowAction(
                    adapterPosition,
                    profileModel.id.toInt(),
                    profileModel.followed)
            }
            false -> {
                launchLoginPage()
            }
        }
    }

    private fun loadDataFromSavedState(savedInstanceState: Bundle) {
        searchParameter = savedInstanceState.getParcelable(EXTRA_SEARCH_PARAMETER)
        query = searchParameter?.getSearchQuery() ?: ""
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter)
    }

    override fun trackEmptySearchProfile() {
        SearchTracking.eventSearchNoResult(activity, query, screenName, mapOf())
    }

    override fun onErrorGetProfileListData() {
        hideLoading()

        if (!adapter.isContainData) {
            activity?.run {
                NetworkErrorHelper.showEmptyState(activity, view) { loadData(presenter.getNextPage()) }
            }
        } else {
            activity?.run {
                NetworkErrorHelper.createSnackbarWithAction(activity) { loadData(presenter.getNextPage()) }.showRetrySnackbar()
            }
        }
    }

    override fun hideLoading() {
        super.hideLoading()

        removeSearchPageLoading()
    }

    override fun trackImpressionRecommendationProfile(recommendationProfileTrackingObjectList: List<Any>) {
        if (::trackingQueue.isInitialized) {
            SearchTracking.trackEventUserImpressionRecommendationProfile(
                    trackingQueue,
                    recommendationProfileTrackingObjectList,
                    query
            )
        }
    }

    override fun onEmptyButtonClicked() {
        SearchTracking.eventUserClickNewSearchOnEmptySearch(context, screenName)
        redirectionListener.showSearchInputView()
    }

    override fun getSelectedFilterAsOptionList(): MutableList<Option>? {
        return null
    }

    override fun onSelectedFilterRemoved(uniqueId: String) {

    }

    override fun getUserId(): String {
        return userSessionInterface.userId
    }

    private fun launchLoginPage() {
        RouteManager.route(context, ApplinkConst.LOGIN)
    }

    override fun onHandleProfileClick(profileModel: ProfileViewModel) {
        presenter.onViewClickProfile(profileModel)
    }

    override fun trackClickProfile(profileViewModel: ProfileViewModel) {
        SearchTracking.eventUserClickProfileResultInTabProfile(
                profileViewModel.getTrackingObject(),
                query)
    }

    override fun trackClickRecommendationProfile(profileViewModel: ProfileViewModel) {
        SearchTracking.trackEventUserClickRecommendationProfile(
                profileViewModel.getRecommendationTrackingObject(),
                query
        )
    }

    override fun route(applink: String) {
        activity?.let {
            RouteManager.route(it, applink)
        }
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return isFirstActiveTab()
    }

    private fun isFirstActiveTab(): Boolean {
        return searchParameter?.get(SearchApiConst.ACTIVE_TAB) ?: "" == SearchConstant.ActiveTab.PROFILE
    }

    override fun getBaseAppComponent(): BaseAppComponent {
        return (activity!!.application as BaseMainApplication).baseAppComponent
    }

    override fun getRegistrationId(): String {
        if (activity == null || activity!!.applicationContext == null) return ""

        val cache = LocalCacheHandler(activity!!.applicationContext, GCM_STORAGE)
        return cache.getString(GCM_ID, "")
    }

    fun backToTop() {
        view?.let {
            getRecyclerView(view)?.smoothScrollToPosition(0)
        }
    }

    private fun removeSearchPageLoading() {
        if (isFirstActiveTab()) {
            searchNavigationListener.removeSearchPageLoading()
        }
    }
}