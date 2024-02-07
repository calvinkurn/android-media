package com.tokopedia.gamification.pdp.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.gamification.R
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.pdp.data.GamificationAnalytics
import com.tokopedia.gamification.pdp.data.di.components.DaggerPdpComponent
import com.tokopedia.gamification.pdp.data.di.components.PdpComponent
import com.tokopedia.gamification.pdp.data.model.KetupatLandingPageData
import com.tokopedia.gamification.pdp.presentation.LandingPageRefreshCallback
import com.tokopedia.gamification.pdp.presentation.RecommendationCallbackImpl
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingAdapter
import com.tokopedia.gamification.pdp.presentation.adapters.KetupatLandingAdapterTypeFactory
import com.tokopedia.gamification.pdp.presentation.viewmodels.KetupatLandingViewModel
import com.tokopedia.gamification.utils.KetupatSharingComponent
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.infinite.main.InfiniteRecommendationManager
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class KetupatLandingFragment : BaseViewModelFragment<KetupatLandingViewModel>() {

    private val adapter: KetupatLandingAdapter by lazy {
        KetupatLandingAdapter(KetupatLandingAdapterTypeFactory())
    }
    private var ketupatLandingViewModel: KetupatLandingViewModel? = null
    private var scratchCardId: String? = ""

    private var fragmentDataRendered: Boolean = false
    private var fragmentViewCreated: Boolean = false
    private var infiniteRecommendationManager: InfiniteRecommendationManager? = null
    private var recommendationCallbackImpl: RecommendationCallbackImpl? = null
    private var ketupatRV: RecyclerView? = null
    private var campaignSlug = ""

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    @Inject
    @JvmField
    var viewModelProvider: ViewModelProvider.Factory? = null

    var ketupatLPSwipeToRefreshView: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    private fun setObservers() {
        ketupatLandingViewModel?.getAffiliateDataItems()?.observe(this) {
            adapter.clearAllElements()
            adapter.addMoreData(it)
            adapter.notifyDataSetChanged()
        }

        ketupatLandingViewModel?.getRecommVisibility()?.observe(this) {
            if (it) {
                setUpRecommendation()
            }
        }

        ketupatLandingViewModel?.getReferralTimeData()?.observe(this) {
            view?.findViewById<Group>(R.id.shimmer_group)?.hide()
            view?.findViewById<SwipeRefreshLayout>(R.id.ketupat_landing_page_swipe_refresh)?.show()
        }

        ketupatLandingViewModel?.getErrorMessage()?.observe(this) {
            getError()
            view?.findViewById<Group>(R.id.shimmer_group)?.hide()
            view?.findViewById<GlobalError>(R.id.global_error_ketupat_lp)?.show()
        }

        ketupatLandingViewModel?.getLandingPageData()?.observe(this) {
            ketupatLPSwipeToRefreshView?.isRefreshing = false
            if (it.gamiGetScratchCardLandingPage.appBar?.isShownShareIcon == true) {
                view?.let { view -> setSharingHeaderIconAndListener(view, it) }
            }
            scratchCardId = it.gamiGetScratchCardLandingPage.scratchCard?.id.toString()
            GamificationAnalytics.sendDirectRewardLandingPageEvent("{'direct_reward_id':'$scratchCardId'}")
        }
    }

    private fun getError() {
        view?.findViewById<GlobalError>(R.id.global_error_ketupat_lp)?.run {
            val homeDeeplink = "tokopedia://home"
            val homeText = "Balik ke Home"
            this.errorAction.text = homeText
            setActionClickListener {
                RouteManager.route(context, homeDeeplink)
            }
        }
    }

    override fun onFragmentBackPressed(): Boolean {
        GamificationAnalytics.sendClickBackOnNavBarEvent(
            "{'direct_reward_id':'$scratchCardId'}",
            "gamification",
            "tokopediamarketplace"
        )
        return super.onFragmentBackPressed()
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        ketupatLandingViewModel = viewModel as KetupatLandingViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ketupat_landing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPullToRefresh(view)
        fragmentViewCreated = true
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    fun setUpPullToRefresh(view: View?) {
        if (ketupatLPSwipeToRefreshView == null) {
            ketupatLPSwipeToRefreshView =
                view?.findViewById(R.id.ketupat_landing_page_swipe_refresh)
            ketupatLPSwipeToRefreshView?.setOnRefreshListener {
                // refresh lp
                refreshData()
                ketupatLPSwipeToRefreshView?.isRefreshing = true
                ketupatRV?.scrollToPosition(0)
            }
        }
    }

    private fun navigateToLoginPage() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (userSessionInterface?.isLoggedIn == false) {
                activity?.finish()
            } else {
                // refresh data with shimmer
                refreshData()
            }
        }
    }

    private fun getCampaignSlug() {
        if (campaignSlug.isEmpty()) {
            campaignSlug = activity?.intent?.data?.getQueryParameter("slug").toString()
        }
    }
    private fun refreshData() {
        getCampaignSlug()
        if (userSessionInterface?.isLoggedIn == true) {
            ketupatLandingViewModel?.getGamificationLandingPageData(
                context,
                campaignSlug,
                object : LandingPageRefreshCallback {
                    override fun refreshLandingPage() {
                        refreshData()
                    }
                }
            )
            if (!fragmentDataRendered) {
                setUpAdapter()
            }
            fragmentDataRendered = true
        } else {
            navigateToLoginPage()
        }
    }

    private fun setUpAdapter() {
        ketupatRV = view?.findViewById(R.id.ketupat_rv)
        val layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)

        adapter.setVisitables(ArrayList())

        ketupatRV?.layoutManager = layoutManager

        val concatAdapter = ConcatAdapter(adapter)
        ketupatRV?.adapter = concatAdapter
        if (recommendationCallbackImpl == null) {
            recommendationCallbackImpl = RecommendationCallbackImpl(this::getScratchCardId)
        }
        infiniteRecommendationManager =
            context?.let { InfiniteRecommendationManager(it, recommendationCallbackImpl, Typography.HEADING_2) }
        infiniteRecommendationManager?.adapter?.let {
            concatAdapter.addAdapter(it)
        }
    }

    private fun setUpRecommendation() {
        val requestParam = GetRecommendationRequestParam(pageName = "gami_direct_reward")
        infiniteRecommendationManager?.requestParam = requestParam
        infiniteRecommendationManager?.fetchRecommendation()
    }

    fun setSharingHeaderIconAndListener(
        view: View,
        ketupatLandingPageData: KetupatLandingPageData
    ) {
        if (ketupatLandingPageData.gamiGetScratchCardLandingPage.appBar?.shared != null) {
            val userSession = UserSession(context)
            val toolBar = view.findViewById<NavToolbar>(R.id.ketupat_navToolbar)
            toolBar.setIcon(
                IconBuilder(IconBuilderFlag(NavSource.SOS))
                    .addIcon(
                        IconList.ID_SHARE,
                        onClick = {
                            // Open share bottom sheet
                            val sharingComponent = KetupatSharingComponent(view, "landing_page-DirectRewardGame-$campaignSlug", userSession.userId)
                            sharingComponent.show(
                                childFragmentManager,
                                ketupatLandingPageData.gamiGetScratchCardLandingPage.appBar?.shared,
                                userSession.userId
                            )
                            GamificationAnalytics.sendClickShareOnNavBarEvent(
                                "{'landing_page-DirectRewardGame':'$campaignSlug'}",
                                "gamification",
                                "tokopediamarketplace"
                            )
                        },
                        disableDefaultGtmTracker = true
                    )
            )
        }
    }

    fun getScratchCardId(): String? {
        return scratchCardId
    }

    override fun getViewModelType(): Class<KetupatLandingViewModel> {
        return KetupatLandingViewModel::class.java
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectKetupatLandingFragment(this)
    }

    private fun getComponent(): PdpComponent =
        DaggerPdpComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .activityContextModule(context?.let { ActivityContextModule(it) })
            .build()

    companion object {
        @JvmStatic
        fun newInstance() = KetupatLandingFragment()
        const val REQUEST_CODE_LOGIN = 1
    }
}
