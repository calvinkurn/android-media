package com.tokopedia.homenav.mainnav.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation.SOURCE_ACCOUNT
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_ALL_TRANSACTION
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_COMPLAIN
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_FAVORITE_SHOP
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_HOME
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_REVIEW
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_TICKET
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_WISHLIST_MENU
import com.tokopedia.homenav.common.util.NpaLayoutManager
import com.tokopedia.homenav.di.DaggerBaseNavComponent
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.MainNavConst.RecentViewAb.CONTROL
import com.tokopedia.homenav.mainnav.MainNavConst.RecentViewAb.EXP_NAME
import com.tokopedia.homenav.mainnav.MainNavConst.RecentViewAb.VARIANT
import com.tokopedia.homenav.mainnav.di.DaggerMainNavComponent
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.MainNavListAdapter
import com.tokopedia.homenav.mainnav.view.analytics.TrackingBuSection
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.analytics.TrackingUserMenuSection
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.mainnav.view.datamodel.AccountHeaderDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavigationDataModel
import com.tokopedia.homenav.view.activity.HomeNavPerformanceInterface
import com.tokopedia.homenav.view.router.NavigationRouter
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.searchbar.navigation_component.NavConstant
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import java.util.HashMap
import javax.inject.Inject

class MainNavFragment : BaseDaggerFragment(), MainNavListener {

    companion object {
        private const val BUNDLE_MENU_ITEM = "menu_item_bundle"
        private const val REQUEST_LOGIN = 1234
        private const val REQUEST_REGISTER = 2345
    }

    private var mainNavDataFetched: Boolean = false
    private var sharedPrefs: SharedPreferences? = null

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var viewModel: MainNavViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: NpaLayoutManager
    lateinit var adapter: MainNavListAdapter

    private var navToolbar: NavToolbar? = null

    protected var trackingQueue: TrackingQueue? = null

    private lateinit var userSession: UserSessionInterface
    private val args: MainNavFragmentArgs by navArgs()

    private var pageSource = ""

    //for coachmark purpose
    private var isOngoingShowOnboarding = false

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        val baseNavComponent
                = DaggerBaseNavComponent.builder()
                .baseAppComponent((requireActivity().applicationContext as BaseMainApplication).baseAppComponent)
                .build() as DaggerBaseNavComponent

        DaggerMainNavComponent.builder()
                .baseNavComponent(baseNavComponent)
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageSource = args.StringMainNavArgsSourceKey
        viewModel.setPageSource(pageSource)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.findViewById<NavToolbar>(R.id.toolbar)?.let {
            it.setToolbarTitle(getString(R.string.title_main_nav))
            it.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_CLOSE)
            navToolbar = it
        }
        return inflater.inflate(R.layout.fragment_main_nav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)
        if (recyclerView.itemDecorationCount == 0)
            recyclerView.addItemDecoration(MainNavSpacingDecoration(
                    resources.getDimensionPixelOffset(R.dimen.dp_12)))
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offset = recyclerView.computeVerticalScrollOffset()
                if (offset > 100) {
                    navToolbar?.showShadow(lineShadow = true)
                } else {
                    navToolbar?.hideShadow(lineShadow = true)
                }
            }
        })
        initAdapter()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_LOGIN, REQUEST_REGISTER -> viewModel.reloadMainNavAfterLogin()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeCategoryListData()
        viewModel.mainNavLiveData.observe(viewLifecycleOwner, Observer {
            populateAdapterData(it)
        })

        viewModel.allProcessFinished.observe(viewLifecycleOwner, Observer {
            if (it.getContentIfNotHandled() == true) {
                validateOnboarding()
            }
        })

        viewModel.networkProcessLiveData.observe(viewLifecycleOwner, Observer { isFinished->
            if (!isFinished) {
                getNavPerformanceCallback()?.startNetworkRequestPerformanceMonitoring()
            } else {
                getNavPerformanceCallback()?.stopNetworkRequestPerformanceMonitoring()
            }
        })
    }

    override fun onPause() {
        super.onPause()

        getTrackingQueueObj()?.sendAll()
    }

    override fun onRefresh() {
    }

    override fun getTrackingQueueObj(): TrackingQueue? {
        if (trackingQueue == null) {
            activity?.let {
                trackingQueue = TrackingQueue(it)
            }
        }
        return trackingQueue
    }

    override fun putEEToTrackingQueue(data: HashMap<String, Any>) {
        if (getTrackingQueueObj() != null) {
            getTrackingQueueObj()?.putEETracking(data)
        }
    }

    override fun onProfileSectionClicked() {
        if (pageSource == SOURCE_ACCOUNT) {
            activity?.onBackPressed()
        } else {
            val intent = RouteManager.getIntent(context, ApplinkConst.ACCOUNT)
            startActivity(intent)
        }
    }

    override fun onProfileLoginClicked() {
        startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_LOGIN)
    }

    override fun onProfileRegisterClicked() {
        startActivityForResult(RouteManager.getIntent(context, ApplinkConst.REGISTER), REQUEST_REGISTER)
    }

    override fun onErrorProfileRefreshClicked(position: Int) {
        viewModel.refreshProfileData()
    }

    override fun onErrorShopInfoRefreshClicked(position: Int) {
        viewModel.refreshUserShopData()
    }

    override fun onErrorBuListClicked(position: Int) {
        viewModel.refreshBuListdata()
    }

    override fun onErrorTransactionListClicked(position: Int) {
        viewModel.refreshTransactionListData()
    }

    override fun onMenuClick(homeNavMenuDataModel: HomeNavMenuDataModel) {
        view?.let {
            if (homeNavMenuDataModel.sectionId == MainNavConst.Section.BU_ICON) {
                if(homeNavMenuDataModel.applink.isNotEmpty()){
                    RouteManager.route(context, homeNavMenuDataModel.applink)
                } else {
                    NavigationRouter.MainNavRouter.navigateTo(it, NavigationRouter.PAGE_CATEGORY,
                            bundleOf("title" to homeNavMenuDataModel.itemTitle, BUNDLE_MENU_ITEM to homeNavMenuDataModel))
                }
                TrackingBuSection.onClickBusinessUnitItem(homeNavMenuDataModel.itemTitle, userSession.userId)
            } else {
                RouteManager.route(requireContext(), homeNavMenuDataModel.applink)
                hitClickTrackingBasedOnId(homeNavMenuDataModel)
            }
        }
    }

    private fun hitClickTrackingBasedOnId(homeNavMenuDataModel: HomeNavMenuDataModel) {
        when(homeNavMenuDataModel.id) {
            ID_ALL_TRANSACTION -> TrackingTransactionSection.clickOnAllTransaction(userSession.userId)
            ID_TICKET -> TrackingTransactionSection.clickOnTicket(userSession.userId)
            ID_REVIEW -> TrackingTransactionSection.clickOnReview(userSession.userId)
            ID_WISHLIST_MENU -> TrackingUserMenuSection.clickOnUserMenu(homeNavMenuDataModel.trackerName, userSession.userId)
            ID_FAVORITE_SHOP -> TrackingTransactionSection.clickOnTokoFavorit(userSession.userId)
            ID_HOME -> TrackingBuSection.onClickBackToHome(userSession.userId)
            else -> TrackingUserMenuSection.clickOnUserMenu(homeNavMenuDataModel.trackerName, userSession.userId)
        }
    }

    override fun onMenuImpression(homeNavMenuDataModel: HomeNavMenuDataModel) {

    }

    override fun getUserId(): String {
        return userSession.userId
    }

    override fun getReviewCounterAbIsUnify(): Boolean {
        return remoteConfig.getString(EXP_NAME, CONTROL) == VARIANT
    }

    private fun getNavPerformanceCallback(): PageLoadTimePerformanceInterface? {
        context?.let {
            return (it as? HomeNavPerformanceInterface)?.getNavPerformanceInterface()
        }
        return null
    }

    private fun observeCategoryListData(){
        onRefresh()
        viewModel.businessListLiveData.observe(viewLifecycleOwner, Observer {
            if(it is Fail){

            }
        })
    }

    private fun initAdapter() {
        val mainNavFactory = MainNavTypeFactoryImpl(this, getUserSession())
        adapter = MainNavListAdapter(mainNavFactory)

        var windowHeight = 0
        activity?.let {
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            windowHeight = displayMetrics.heightPixels
        }

        layoutManager = NpaLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun populateAccountHeader(data: AccountHeaderDataModel) {
        val dataList: List<Visitable<*>> = mutableListOf(data)
        adapter.submitList(dataList)
    }

    private fun populateAdapterData(data: MainNavigationDataModel) {
        setupViewPerformanceMonitoring(data)
        adapter.submitList(data.dataList)
        if (data.dataList.size > 1 && !mainNavDataFetched) {
            viewModel.getMainNavData(true)
            mainNavDataFetched = true
        }
    }

    private fun setupViewPerformanceMonitoring(data: MainNavigationDataModel) {
        if (data.dataList.size > 1) {
            getNavPerformanceCallback()?.startRenderPerformanceMonitoring()
            recyclerView.addOneTimeGlobalLayoutListener {
                getNavPerformanceCallback()?.stopRenderPerformanceMonitoring()
                getNavPerformanceCallback()?.stopMonitoring()
            }
        }
    }

    private fun getUserSession() : UserSessionInterface{
        if(!::userSession.isInitialized){
            activity?.let {
                userSession = UserSession(it)
            }
        }
        return userSession
    }

    //coach mark logic
    //true if user has done navigation onboarding on P1
    private fun saveFirstViewNavigationNavPagP1(boolean: Boolean) {
        context?.let {
            sharedPrefs = it.getSharedPreferences(
                    NavConstant.KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
            sharedPrefs?.run {
                edit()
                        .putBoolean(NavConstant.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1, boolean)
                        .putBoolean(NavConstant.KEY_P1_DONE_AS_NON_LOGIN,
                                !getUserSession().isLoggedIn)
                        .apply()
            }
        }
    }

    //true if user has done navigation onboarding on P1
    private fun saveFirstViewNavigationNavPagP2(boolean: Boolean) {
        context?.let {
            sharedPrefs = it.getSharedPreferences(
                    NavConstant.KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
            sharedPrefs?.run {
                edit()
                        .putBoolean(NavConstant.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2, boolean)
                        .apply()
            }
        }
    }

    private fun isFirstViewNavigationNavPageP1(): Boolean {
        context?.let {
            sharedPrefs = it.getSharedPreferences(
                    NavConstant.KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
            val firstViewNavigation = sharedPrefs?.getBoolean(
                    NavConstant.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1, false)?:false
            return firstViewNavigation
        }
        return true
    }

    private fun isFirstViewNavigationNavPageP2(): Boolean {
        context?.let {
            sharedPrefs = it.getSharedPreferences(
                    NavConstant.KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
            val firstViewNavigation = sharedPrefs?.getBoolean(
                    NavConstant.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2, true)?:false
            return firstViewNavigation
        }
        return true
    }

    private fun isP1OnboardingDoneAsNonLogin(): Boolean {
        context?.let {
            sharedPrefs = it.getSharedPreferences(
                    NavConstant.KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
            val fromNonLogin = sharedPrefs?.getBoolean(
                    NavConstant.KEY_P1_DONE_AS_NON_LOGIN, true)?:false
            return fromNonLogin
        }
        return true
    }

    private fun needToShowOnboarding(): Boolean {
        return isFirstViewNavigationNavPageP1() || isFirstViewNavigationNavPageP2()
    }

    private fun validateOnboarding() {
        if (needToShowOnboarding() &&
                !isOngoingShowOnboarding) {
            showNavigationPageOnboarding()
            isOngoingShowOnboarding = true
        }
    }

    //return is the function is success or not
    private fun showNavigationPageOnboarding(): Boolean {
        if (isFirstViewNavigationNavPageP1()) {
            //do the p1 onboarding

            if (getUserSession().isLoggedIn) {
                val coachMarkConfig = buildP1LoggedInCoachmarkConfig()
                val coachMark = CoachMark2(requireContext()).buildCoachmarkFromConfig(coachMarkConfig)
                coachMark.showCoachMark(step = coachMarkConfig.items)
                coachMarkConfig.onFinish.invoke()
            }
            else {
                val coachMarkConfig = buildP1NonLoggedInCoachmarkConfig()
                val coachMark = CoachMark2(requireContext()).buildCoachmarkFromConfig(coachMarkConfig)
                coachMark.showCoachMark(step = coachMarkConfig.items)
                coachMarkConfig.onFinish.invoke()
            }
        }
        else if (isFirstViewNavigationNavPageP2()) {
            if (getUserSession().isLoggedIn && isP1OnboardingDoneAsNonLogin()){
                val coachMarkConfig = buildP2LoggedInCoachmarkConfig()
                val coachMark = CoachMark2(requireContext()).buildCoachmarkFromConfig(coachMarkConfig)
                coachMark.showCoachMark(step = coachMarkConfig.items)
                coachMarkConfig.onFinish.invoke()
            }
        }
        return true
    }

    private fun haveUserLogoutData(): Boolean {
        val name = getSharedPreference().getString(AccountHeaderDataModel.KEY_USER_NAME, "") ?: ""
        return name.isNotEmpty()
    }

    private fun getSharedPreference(): SharedPreferences {
        return requireContext().getSharedPreferences(AccountHeaderDataModel.STICKY_LOGIN_REMINDER_PREF, Context.MODE_PRIVATE)
    }

    private fun buildP1LoggedInCoachmarkConfig(): CoachmarkRecyclerViewConfig {
        val itemsArray = arrayListOf<CoachMark2Item>()
        itemsArray.buildP1LoggedInCoachmark()
        val itemsConfigArray = arrayListOf<CoachmarkItemReyclerViewConfig>()
        itemsConfigArray.buildP1LoggedInCoachmarkItemReyclerViewConfig()
        return CoachmarkRecyclerViewConfig(itemsArray, itemsConfigArray) {
            saveFirstViewNavigationNavPagP1(false)
        }
    }

    private fun buildP1NonLoggedInCoachmarkConfig(): CoachmarkRecyclerViewConfig {
        val itemsArray = arrayListOf<CoachMark2Item>()
        itemsArray.buildP1NonLoggedInCoachmark()
        val itemsConfigArray = arrayListOf<CoachmarkItemReyclerViewConfig>()
        itemsConfigArray.buildP1NonLoggedInCoachmarkItemReyclerViewConfig()
        return CoachmarkRecyclerViewConfig(itemsArray, itemsConfigArray) {
            saveFirstViewNavigationNavPagP1(false)
        }
    }

    private fun buildP2LoggedInCoachmarkConfig(): CoachmarkRecyclerViewConfig {
        val itemsArray = arrayListOf<CoachMark2Item>()
        itemsArray.buildP2LoggedInCoachmark()
        val itemsConfigArray = arrayListOf<CoachmarkItemReyclerViewConfig>()
        itemsConfigArray.buildP2LoggedInCoachmarkItemReyclerViewConfig()
        return CoachmarkRecyclerViewConfig(itemsArray, itemsConfigArray) {
            saveFirstViewNavigationNavPagP2(false)
        }
    }

    private fun CoachMark2.buildCoachmarkFromConfig(coachmarkRecyclerViewConfig: CoachmarkRecyclerViewConfig): CoachMark2 {
        val coachMark = this
        val coachMarkItems = coachmarkRecyclerViewConfig.items
        val coachMarkConfig = coachmarkRecyclerViewConfig.configs

        if (coachMarkConfig.isNotEmpty()) {
            val firstPositionConfig = coachMarkConfig[0]
            firstPositionConfig.targetPosition?.let {
                val holder = recyclerView.findViewHolderForAdapterPosition(it)
                holder?.let {
                    if (coachMarkItems.isNotEmpty()) {
                        coachMarkItems[0].anchorView = holder.itemView
                    }
                }
            }

            coachMark.setStepListener(object: CoachMark2.OnStepListener {
                override fun onStep(currentIndex: Int, item: CoachMark2Item) {
                    val coachMarkItem = coachMarkItem[currentIndex]
                    val config = coachMarkConfig[currentIndex]

                    recyclerView.smoothScrollToPosition(config.scrollToPosition)
                    coachMark.isDismissed = true
                    Handler().postDelayed({
                        config.targetPosition?.let {
                            coachMark.isDismissed = false
                            val holder = recyclerView.findViewHolderForAdapterPosition(it)
                            holder?.let {
                                coachMarkItem.anchorView = holder.itemView
                                coachMark.showCoachMark(coachMarkItems, null, currentIndex)
                            }
                        }
                    },200)
                }
            })
        }
        return coachMark
    }

    private fun ArrayList<CoachMark2Item>.buildP1LoggedInCoachmark() {
        this.add(
                CoachMark2Item(
                        recyclerView.rootView,
                        getString(R.string.onboarding_login_p1_s1_title),
                        getString(R.string.onboarding_login_p1_s1_description)
                )
        )
        this.add(
                CoachMark2Item(
                        recyclerView.rootView,
                        getString(R.string.onboarding_login_p1_s2_title),
                        getString(R.string.onboarding_login_p1_s2_description)
                )
        )
        this.add(
                CoachMark2Item(
                        recyclerView.rootView,
                        getString(R.string.onboarding_login_p1_s3_title),
                        getString(R.string.onboarding_login_p1_s3_description)
                )
        )
    }

    private fun ArrayList<CoachMark2Item>.buildP1NonLoggedInCoachmark() {
        this.add(
                CoachMark2Item(
                        recyclerView.rootView,
                        getString(R.string.onboarding_login_p1_s2_title),
                        getString(R.string.onboarding_login_p1_s2_description)
                )
        )
        this.add(
                CoachMark2Item(
                        recyclerView.rootView,
                        getString(R.string.onboarding_login_p1_s3_title),
                        getString(R.string.onboarding_login_p1_s3_description)
                )
        )
    }

    private fun ArrayList<CoachMark2Item>.buildP2LoggedInCoachmark() {
        this.add(
                CoachMark2Item(
                        recyclerView.rootView,
                        getString(R.string.onboarding_login_p2_s1_title),
                        getString(R.string.onboarding_login_p2_s1_description)
                )
        )
    }

    private fun ArrayList<CoachmarkItemReyclerViewConfig>.buildP1LoggedInCoachmarkItemReyclerViewConfig() {
        this.add(
                CoachmarkItemReyclerViewConfig(
                        0, viewModel.findHeaderModelPosition())
        )
        this.add(
                CoachmarkItemReyclerViewConfig(
                        (viewModel.mainNavLiveData.value?.dataList?.size?:0)-1, viewModel.findAllTransactionModelPosition())
        )
        this.add(
                CoachmarkItemReyclerViewConfig(
                        (viewModel.mainNavLiveData.value?.dataList?.size?:0)-1, viewModel.findComplainModelPosition())
        )
    }

    private fun ArrayList<CoachmarkItemReyclerViewConfig>.buildP1NonLoggedInCoachmarkItemReyclerViewConfig() {
        this.add(
                CoachmarkItemReyclerViewConfig(
                        (viewModel.mainNavLiveData.value?.dataList?.size?:0)-1, viewModel.findAllTransactionModelPosition())
        )
        this.add(
                CoachmarkItemReyclerViewConfig(
                        (viewModel.mainNavLiveData.value?.dataList?.size?:0)-1, viewModel.findComplainModelPosition())
        )
    }

    private fun ArrayList<CoachmarkItemReyclerViewConfig>.buildP2LoggedInCoachmarkItemReyclerViewConfig() {
        this.add(
                CoachmarkItemReyclerViewConfig(
                        0, viewModel.findHeaderModelPosition())
        )
    }
}

data class CoachmarkRecyclerViewConfig(
        val items: ArrayList<CoachMark2Item>,
        val configs: ArrayList<CoachmarkItemReyclerViewConfig>,
        val onFinish: () -> Unit
)

data class CoachmarkItemReyclerViewConfig(
        val scrollToPosition: Int,
        val targetPosition: Int?
)