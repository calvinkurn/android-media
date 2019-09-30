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
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.GCM_ID
import com.tokopedia.discovery.common.constants.SearchConstant.GCM_STORAGE
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.search.R
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.presentation.ProfileListSectionContract
import com.tokopedia.search.result.presentation.model.EmptySearchProfileViewModel
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

    private val PARAM_USER_ID = "{user_id}"

    @Inject
    lateinit var presenter: ProfileListSectionContract.Presenter

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    lateinit var searchNavigationListener: SearchNavigationListener

    lateinit var redirectionListener: RedirectionListener

    lateinit var trackingQueue: TrackingQueue

    var totalProfileCount : Int = Integer.MAX_VALUE

    var isHasNextPage : Boolean = isLoadMoreEnabledByDefault

    var query : String = ""

    var searchParameter: SearchParameter? = null

    var nextPage : Int = 1

    var hasLoadData = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        context?.let {
            attachNavigationListener(it)
            attachRedirectionListener(it)
        }
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.profile_list_recycler_view
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.profile_list_swipe_refresh_layout
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
            hasLoadData = true
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
        if (profileListViewModel.getListTrackingObject().isNotEmpty()) {
            if (::trackingQueue.isInitialized) {
                SearchTracking.eventUserImpressionProfileResultInTabProfile(
                    trackingQueue,
                    profileListViewModel.getListTrackingObject(),
                    query
                )
            }
        }

        totalProfileCount = profileListViewModel.totalSearchCount
        renderList(profileListViewModel.profileModelList, profileListViewModel.isHasNextPage)

        removeSearchPageLoading()
    }

    override fun renderList(list: List<ProfileViewModel>, hasNextPage: Boolean) {
        hideLoading()
        if (isLoadingInitialData) {
            clearAllData()
        } else {
            adapter.clearAllNonDataElement()
        }
        if (nextPage == 1 && totalProfileCount != 0) {
            adapter.addElement(TotalSearchCountViewModel(createTotalCountText(totalProfileCount)))
        }
        adapter.addElement(list)
        updateScrollListenerState(hasNextPage)

        if (isListEmpty) {
            adapter.addElement(emptyDataViewModel)
        } else {
            isLoadingInitialData = false
        }
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
        presenter.requestProfileListData(query, page)
        this.nextPage = page
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

    companion object {
        private val EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER"
        private const val SCREEN_SEARCH_PAGE_PROFILE_TAB = "Search result - Profile tab"

        fun newInstance(searchParameter: SearchParameter): ProfileListFragment {
            val args = Bundle()
            args.putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter)

            val profileListFragment = ProfileListFragment()
            profileListFragment.arguments = args

            return profileListFragment
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

    override fun getEmptyDataViewModel(): Visitable<*> {
        SearchTracking.eventSearchNoResult(activity, query, screenName, mapOf())

        activity?.run {
            return createProfileEmptySearchModel(
                activity!!,
                query,
                getString(R.string.title_profile)
            )
        }

        return EmptySearchProfileViewModel()
    }

    private fun createProfileEmptySearchModel(context: Context, query: String, sectionTitle: String): EmptySearchProfileViewModel {
        val emptySearchModel = EmptySearchProfileViewModel()
        emptySearchModel.imageRes = com.tokopedia.design.R.drawable.ic_empty_search
        emptySearchModel.title = getEmptySearchTitle(context, sectionTitle)
        emptySearchModel.content = String.format(context.getString(R.string.empty_search_content_template), query)
        emptySearchModel.buttonText = context.getString(R.string.empty_search_button_text)
        return emptySearchModel
    }

    private fun getEmptySearchTitle(context: Context, sectionTitle: String): String {
        val templateText = context.getString(R.string.msg_empty_search_with_filter_1)
        return String.format(templateText, sectionTitle).toLowerCase()
    }

    override fun onErrorGetProfileListData() {
        hideLoading()

        if (!adapter.isContainData) {
            activity?.run {
                NetworkErrorHelper.showEmptyState(activity, view) { loadData(nextPage) }
            }
        } else {
            activity?.run {
                NetworkErrorHelper.createSnackbarWithAction(activity) { loadData(nextPage) }.showRetrySnackbar()
            }
        }
    }

    override fun hideLoading() {
        super.hideLoading()

        removeSearchPageLoading()
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
        SearchTracking.eventUserClickProfileResultInTabProfile(
            profileModel.getTrackingObject(),
            query)

        launchProfilePage(profileModel.id)
    }

    private fun launchProfilePage(userId : String) {
        val applink : String = ApplinkConst.PROFILE.replace(PARAM_USER_ID, userId)

        if(isActivityAnApplinkRouter()) {
            handleItemClickedIfActivityAnApplinkRouter(applink, false)
        }
    }

    private fun isActivityAnApplinkRouter(): Boolean {
        return activity != null && activity!!.applicationContext is ApplinkRouter
    }

    private fun handleItemClickedIfActivityAnApplinkRouter(applink: String, shouldFinishActivity: Boolean) {
        activity?.run {
            val router = activity!!.applicationContext as ApplinkRouter
            if (router.isSupportApplink(applink)) {
                handleRouterSupportApplink(router, applink, shouldFinishActivity)
            }
        }
    }

    private fun handleRouterSupportApplink(router: ApplinkRouter, applink: String, shouldFinishActivity: Boolean) {
        finishActivityIfRequired(shouldFinishActivity)
        router.goToApplinkActivity(activity, applink)
    }

    private fun finishActivityIfRequired(shouldFinishActivity: Boolean) {
        if (shouldFinishActivity)
            activity?.finish()
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