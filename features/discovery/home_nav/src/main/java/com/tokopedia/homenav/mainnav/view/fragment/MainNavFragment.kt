package com.tokopedia.homenav.mainnav.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_ALL_TRANSACTION
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_COMPLAIN
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_REVIEW
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_TICKET
import com.tokopedia.homenav.common.util.NpaLayoutManager
import com.tokopedia.homenav.di.DaggerBaseNavComponent
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.di.DaggerMainNavComponent
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.MainNavListAdapter
import com.tokopedia.homenav.mainnav.view.analytics.TrackingBuSection
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.analytics.TrackingUserMenuSection
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.presenter.MainNavViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import com.tokopedia.homenav.view.activity.HomeNavPerformanceInterface
import com.tokopedia.homenav.view.router.NavigationRouter
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.searchbar.navigation_component.NavConstant
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_main_nav.*
import javax.inject.Inject

class MainNavFragment : BaseDaggerFragment(), MainNavListener {

    companion object {
        private const val BUNDLE_MENU_ITEM = "menu_item_bundle"
        private const val REQUEST_LOGIN = 1234
        private const val REQUEST_REGISTER = 2345
    }

    private var sharedPrefs: SharedPreferences? = null

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var viewModel: MainNavViewModel
    lateinit var recyclerView: RecyclerView
    private var scrollView: ScrollView? = null
    lateinit var layoutManager: NpaLayoutManager
    lateinit var adapter: MainNavListAdapter

    private var navToolbar: NavToolbar? = null

    private lateinit var userSession: UserSessionInterface
    val args: MainNavFragmentArgs by navArgs()

    private var pageSource = ""

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
        scrollView = view.findViewById(R.id.scrollView)
        scrollView?.viewTreeObserver?.addOnScrollChangedListener {
            scrollView?.run {
                if (scrollY > 100) {
                    navToolbar?.showShadow(lineShadow = true)
                } else {
                    navToolbar?.hideShadow(lineShadow = true)
                }
            }
        }

        if (recyclerView.itemDecorationCount == 0)
            recyclerView.addItemDecoration(MainNavSpacingDecoration(
                    resources.getDimensionPixelOffset(R.dimen.dp_12)))
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

        viewModel.accountLiveData.observe(viewLifecycleOwner, Observer {

        })

        viewModel.profileResultListener.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Fail -> {

                }
            }
        })
        viewModel.membershipResultListener.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Fail -> {

                }
            }
        })
        viewModel.ovoResultListener.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Fail -> {

                }
            }
        })
        viewModel.shopResultListener.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Fail -> {

                }
            }
        })

        viewModel.onboardingListLiveData.observe(viewLifecycleOwner, Observer {
            viewModel.setOnboardingSuccess(showNavigationPageOnboarding(it))
        })

        viewModel.networkProcessLiveData.observe(viewLifecycleOwner, Observer { isFinished->
            if (!isFinished) {
                getNavPerformanceCallback()?.startNetworkRequestPerformanceMonitoring()
            } else {
                getNavPerformanceCallback()?.stopNetworkRequestPerformanceMonitoring()
            }
        })
    }

    override fun onRefresh() {
    }

    override fun onProfileSectionClicked() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.OLD_HOME_ACCOUNT)
        startActivity(intent)
    }

    override fun onProfileLoginClicked() {
        startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_LOGIN)
    }

    override fun onProfileRegisterClicked() {
        startActivityForResult(RouteManager.getIntent(context, ApplinkConst.REGISTER), REQUEST_REGISTER)
    }

    override fun onErrorProfileNameClicked(element: AccountHeaderViewModel) {
        viewModel.reloadUserData(element)
    }

    override fun onErrorProfileOVOClicked(element: AccountHeaderViewModel) {
        viewModel.reloadOvoData(element)
        viewModel.reloadSaldoData(element)
    }

    override fun onErrorProfileShopClicked(element: AccountHeaderViewModel) {
        viewModel.reloadShopData(getUserSession().shopId.toInt(), element)
    }

    override fun onErrorBuListClicked(position: Int) {
        viewModel.refreshBuListdata()
    }

    override fun onMenuClick(homeNavMenuViewModel: HomeNavMenuViewModel) {
        view?.let {
            if (homeNavMenuViewModel.sectionId == MainNavConst.Section.BU_ICON) {
                if(homeNavMenuViewModel.applink.isNotEmpty()){
                    RouteManager.route(context, homeNavMenuViewModel.applink)
                } else {
                    NavigationRouter.MainNavRouter.navigateTo(it, NavigationRouter.PAGE_CATEGORY,
                            bundleOf("title" to homeNavMenuViewModel.itemTitle, BUNDLE_MENU_ITEM to homeNavMenuViewModel))
                }
                TrackingBuSection.onClickBusinessUnitItem(homeNavMenuViewModel.itemTitle, userSession.userId)
            } else {
                RouteManager.route(requireContext(), homeNavMenuViewModel.applink)
                hitClickTrackingBasedOnId(homeNavMenuViewModel)
            }
        }
    }

    private fun hitClickTrackingBasedOnId(homeNavMenuViewModel: HomeNavMenuViewModel) {
        when(homeNavMenuViewModel.id) {
            ID_ALL_TRANSACTION -> TrackingTransactionSection.clickOnAllTransaction(userSession.userId)
            ID_TICKET -> TrackingTransactionSection.clickOnTicket(userSession.userId)
            ID_REVIEW -> TrackingTransactionSection.clickOnReview(userSession.userId)
            else -> TrackingUserMenuSection.clickOnUserMenu(homeNavMenuViewModel.trackerName, userSession.userId)
        }
    }

    override fun onMenuImpression(homeNavMenuViewModel: HomeNavMenuViewModel) {

    }

    override fun getUserId(): String {
        return userSession.userId
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
        val mainNavFactory = MainNavTypeFactoryImpl(this, getUserSession(), remoteConfig)
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

    private fun populateAccountHeader(data: AccountHeaderViewModel) {
        val dataList: List<Visitable<*>> = mutableListOf(data)
        adapter.submitList(dataList)
    }

    private fun populateAdapterData(data: MainNavigationDataModel) {
        setupViewPerformanceMonitoring(data)
        adapter.submitList(data.dataList)
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

    //return is the function is success or not
    private fun showNavigationPageOnboarding(list: List<Visitable<*>>): Boolean {
        val profileSectionView = list.indexOf( list.find { it is AccountHeaderViewModel } ).getViewForThisPosition()
        val allTransactionView = list.indexOf( list.find { it is HomeNavMenuViewModel && it.id == ID_ALL_TRANSACTION } ).getViewForThisPosition()
        val complainSectionView = list.indexOf( list.find { it is HomeNavMenuViewModel && it.id == ID_COMPLAIN } ).getViewForThisPosition()

        if (allTransactionView == null || complainSectionView == null) return false

        if (isFirstViewNavigationNavPageP1()) {
            //do the p1 onboarding
            val coachMarkItem = ArrayList<CoachMark2Item>()
            val coachMark = CoachMark2(requireContext())

            if (getUserSession().isLoggedIn) {
                profileSectionView?.let {
                    coachMarkItem.add(
                            CoachMark2Item(
                                    profileSectionView,
                                    getString(R.string.onboarding_login_p1_s1_title),
                                    getString(R.string.onboarding_login_p1_s1_description)
                            )
                    )
                }

                allTransactionView?.let {
                    coachMarkItem.add(
                            CoachMark2Item(
                                    allTransactionView,
                                    getString(R.string.onboarding_login_p1_s2_title),
                                    getString(R.string.onboarding_login_p1_s2_description)
                            )
                    )
                }

                complainSectionView?.let {
                    coachMarkItem.add(
                            CoachMark2Item(
                                    complainSectionView,
                                    getString(R.string.onboarding_login_p1_s3_title),
                                    getString(R.string.onboarding_login_p1_s3_description)
                            )
                    )
                }
                coachMark.setStepListener(object: CoachMark2.OnStepListener {
                    override fun onStep(currentIndex: Int, item: CoachMark2Item) {
                        if (currentIndex == (coachMarkItem.size-1)) {
                            recyclerView.smoothScrollToPosition((adapter.itemCount-1))
                        }
                    }

                })
            } else {
                allTransactionView?.let {
                    coachMarkItem.add(
                            CoachMark2Item(
                                    allTransactionView,
                                    getString(R.string.onboarding_nonlogin_p1_s1_title),
                                    getString(R.string.onboarding_nonlogin_p1_s1_description)
                            )
                    )
                }

                complainSectionView?.let {
                    coachMarkItem.add(
                            CoachMark2Item(
                                    complainSectionView,
                                    getString(R.string.onboarding_nonlogin_p1_s2_title),
                                    getString(R.string.onboarding_nonlogin_p1_s2_description)
                            )
                    )
                }
            }

            coachMark.onFinishListener = {
                saveFirstViewNavigationNavPagP1(false)
            }
            if (coachMarkItem.isNotEmpty()) coachMark.showCoachMark(step = coachMarkItem, scrollView = scrollView)
        } else if (isFirstViewNavigationNavPageP2()) {
            //do the p2 onboarding
            val coachMarkItem = ArrayList<CoachMark2Item>()
            val coachMark = CoachMark2(requireContext())
            if (getUserSession().isLoggedIn && isP1OnboardingDoneAsNonLogin()){
                profileSectionView?.let {
                    coachMarkItem.add(
                            CoachMark2Item(
                                    profileSectionView,
                                    getString(R.string.onboarding_login_p2_s1_title),
                                    getString(R.string.onboarding_login_p2_s1_description)
                            )
                    )
                }
            }
            if (coachMarkItem.isNotEmpty()) {
                coachMark.showCoachMark(step = coachMarkItem, scrollView = scrollView)
                saveFirstViewNavigationNavPagP2(false)
            }
        }
        return true
    }

    private fun Int.getViewForThisPosition(): View? {
        if (this != -1) return recyclerView.findViewHolderForAdapterPosition(this)?.itemView
        return null
    }
}