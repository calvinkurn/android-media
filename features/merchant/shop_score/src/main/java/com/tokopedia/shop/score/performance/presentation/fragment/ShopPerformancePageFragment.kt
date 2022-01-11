package com.tokopedia.shop.score.performance.presentation.fragment

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.gm.common.utils.ShopScoreReputationErrorLogger
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScorePrefManager
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.analytics.ShopScorePenaltyTracking
import com.tokopedia.shop.score.common.plt.ShopPerformanceMonitoringContract
import com.tokopedia.shop.score.common.plt.ShopScorePerformanceMonitoringListener
import com.tokopedia.shop.score.databinding.FragmentShopPerformanceBinding
import com.tokopedia.shop.score.performance.di.component.ShopPerformanceComponent
import com.tokopedia.shop.score.performance.domain.model.ShopScoreWrapperResponse
import com.tokopedia.shop.score.performance.presentation.activity.ShopPerformanceYoutubeActivity
import com.tokopedia.shop.score.performance.presentation.adapter.*
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.*
import com.tokopedia.shop.score.performance.presentation.bottomsheet.*
import com.tokopedia.shop.score.performance.presentation.model.*
import com.tokopedia.shop.score.performance.presentation.model.tablet.BaseParameterDetail
import com.tokopedia.shop.score.performance.presentation.model.tablet.ItemHeaderParameterDetailUiModel
import com.tokopedia.shop.score.performance.presentation.viewmodel.ShopPerformanceViewModel
import com.tokopedia.shop.score.performance.presentation.widget.PenaltyDotBadge
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


open class ShopPerformancePageFragment : BaseDaggerFragment(),
    ShopPerformanceListener, ShopPerformanceMonitoringContract {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var shopScorePenaltyTracking: ShopScorePenaltyTracking

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopPerformanceViewModel::class.java)
    }

    private val shopPerformanceAdapterTypeFactory by lazy {
        ShopPerformanceAdapterTypeFactory(
            this
        )
    }

    open val shopPerformanceAdapter by lazy {
        ShopPerformanceAdapter(
            shopPerformanceAdapterTypeFactory
        )
    }

    open var binding by autoClearedNullable<FragmentShopPerformanceBinding>()

    private var shopScoreWrapperResponse: ShopScoreWrapperResponse? = null
    private var isNewSeller = false

    private var shopScorePerformanceMonitoringListener: ShopScorePerformanceMonitoringListener? =
        null

    private val shopScorePrefManager by lazy { context?.let { ShopScorePrefManager(it) } }

    private val coachMark by getInstanceCoachMark()

    private val penaltyDotBadge: PenaltyDotBadge? by lazy {
        context?.let { PenaltyDotBadge(it) }
    }

    private var counterPenalty = 0L
    private var menu: Menu? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        shopScorePerformanceMonitoringListener =
            castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        splitCompatInstall()
        binding = FragmentShopPerformanceBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startNetworkRequestPerformanceMonitoring()
        stopPreparePerformancePageMonitoring()
        super.onViewCreated(view, savedInstanceState)
        setPageBackground()
        setupActionBar()
        setupAdapter()
        onSwipeRefreshShopPerformance()
        observeShopPeriod()
        observeShopPerformancePage()
        recyclerViewScrollListenerCoachMark()
    }

    override fun onDestroy() {
        viewModel.shopInfoLevel.removeObservers(this)
        viewModel.shopPerformanceDetail.removeObservers(this)
        viewModel.shopPerformancePage.removeObservers(this)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_shop_score, menu)
        this.menu = menu
        showPenaltyBadge()
    }

    override fun onBtnErrorStateClicked() {
        loadData(false)
        showPenaltyBadge()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            INFO_MENU_ID -> {
                RouteManager.route(
                    context,
                    ApplinkConstInternalGlobal.WEBVIEW,
                    ShopScoreConstant.SHOP_INFO_URL
                )
                shopScorePenaltyTracking.clickMenuCompleteInfo()
            }
            PENALTY_WARNING_MENU_ID -> {
                goToPenaltyPage()
                shopScorePenaltyTracking.clickMenuPenalty()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ShopPerformanceComponent::class.java).inject(this)
    }

    override fun onTooltipLevelClicked(level: Long) {
        val shopLevelData = shopScoreWrapperResponse?.shopScoreTooltipResponse?.result
        val bottomSheetShopTooltipLevel = BottomSheetShopTooltipLevel.createInstance(
            shopLevel = level,
            shopIncome = shopLevelData?.niv?.toString().orEmpty(),
            productSold = shopLevelData?.itemSold.toString(),
            period = shopLevelData?.period.orEmpty(),
            nextUpdate = if (shopLevelData?.nextUpdate?.isBlank() == true) "-"
            else shopLevelData?.nextUpdate ?: "-"
        )
        bottomSheetShopTooltipLevel.show(childFragmentManager)
    }

    override fun onTooltipScoreClicked() {
        val bottomSheetShopTooltipScore = BottomSheetShopTooltipScore()
        bottomSheetShopTooltipScore.show(childFragmentManager)
    }

    override fun onTickerClickedToPenaltyPage() {
        goToPenaltyPage()
        shopScorePenaltyTracking.clickHereTickerPenalty()
    }

    override fun onTickerImpressionToPenaltyPage() {
        shopScorePenaltyTracking.impressTickerPenaltyShopScore()
    }

    /**
     * ItemShopPerformanceListener
     */
    override fun onItemClickedToDetailBottomSheet(
        titlePerformanceDetail: String,
        identifierPerformanceDetail: String
    ) {

        val bottomSheetDetail = BottomSheetPerformanceDetail.createInstance(
            titlePerformanceDetail,
            identifierPerformanceDetail
        )
        bottomSheetDetail.show(childFragmentManager)
    }

    override fun onItemClickedToFaqClicked() {
        goToFaqSection()
    }

    override fun onItemClickedGotoPMPro() {
        goToPowerMerchantSubscribe(PARAM_PM_PRO)
    }

    /**
     * ItemStatusPowerMerchantListener
     */
    override fun onItemClickedGoToPMActivation() {
        goToPowerMerchantSubscribe(PARAM_PM)
        shopScorePenaltyTracking.clickPowerMerchantSection(isNewSeller)
    }

    override fun onImpressHeaderPowerMerchantSection() {
        shopScorePenaltyTracking.impressPotentialPowerMerchant(isNewSeller)
    }

    /**
     * ItemPotentialRegularMerchantListener
     */
    override fun onItemClickedBenefitPotentialRM() {
        goToPowerMerchantSubscribe(PARAM_PM)
        shopScorePenaltyTracking.clickSeeAllBenefitInRM(isNewSeller)
    }

    override fun onImpressBenefitSeeAll() {
        shopScorePenaltyTracking.impressSeeAllBenefitPowerMerchant(isNewSeller)
    }

    override fun onGotoPMProPage() {
        goToPowerMerchantSubscribe(PARAM_PM_PRO)
    }

    override fun onItemClickedGoToPMProActivation() {
        goToPowerMerchantSubscribe(PARAM_PM_PRO)
    }

    /**
     * ItemRecommendationFeatureListener
     */
    override fun onItemClickedRecommendationFeature(appLink: String, identifier: String) {
        shopScorePenaltyTracking.clickMerchantToolsRecommendation(identifier)
        context?.let { context ->
            if (GlobalConfig.isSellerApp()) {
                RouteManager.route(context, appLink)
            } else {
                if (appLink.startsWith(ApplinkConst.SellerApp.CREATE_VOUCHER)) {
                    val appLinks = ArrayList<String>().apply {
                        add(ApplinkConstInternalSellerapp.SELLER_HOME)
                        add(ApplinkConstInternalSellerapp.CREATE_VOUCHER)
                    }
                    goToSellerMigrationPage(context = context, appLinks)
                } else {
                    RouteManager.route(context, appLink)
                }
            }
        }
    }

    /**
     * ItemRegularMerchantListener
     */
    override fun onRMSectionToPMPage() {
        goToPowerMerchantSubscribe(PARAM_PM)
    }

    private fun goToSellerMigrationPage(context: Context, appLinks: ArrayList<String>) {
        val intent = SellerMigrationActivity.createIntent(
            context,
            SellerMigrationFeatureName.FEATURE_SHOP_CASHBACK_VOUCHER,
            SCREEN_NAME,
            appLinks
        )
        startActivity(intent)
    }

    override fun onItemImpressRecommendationFeature(identifier: String) {
        shopScorePenaltyTracking.impressMerchantToolsRecommendation(identifier)
    }

    /**
     * ItemTimerNewSellerListener
     */
    override fun onBtnLearnNowToSellerEduClicked(sellerEduUrl: String) {
        goToSellerEdu(sellerEduUrl)
    }

    override fun onBtnLearnNowToFaqClicked() {
        goToFaqSection()
    }

    override fun onBtnShopPerformanceToInterruptClicked(infoPageUrl: String) {
        context?.let {
            RouteManager.route(it, ApplinkConstInternalGlobal.WEBVIEW, infoPageUrl)
        }
        shopScorePenaltyTracking.clickLearnShopPerformanceNewSeller()
    }

    override fun onWatchVideoClicked(videoId: String) {
        goToYoutubePage(videoId)
        shopScorePenaltyTracking.clickWatchVideoNewSeller()
    }

    override fun onImpressBtnLearnPerformance() {
        shopScorePenaltyTracking.impressLearnShopPerformanceNewSeller(isNewSeller)
    }

    override fun onImpressWatchVideo() {
        shopScorePenaltyTracking.impressWatchVideoNewSeller(isNewSeller)
    }

    override fun onWatchVideoReactivatedClicked(videoId: String) {
        goToYoutubePage(videoId)
    }

    override fun onBtnLearnNowReactivatedClicked(sellerEduUrl: String) {
        goToSellerEdu(sellerEduUrl)
    }

    override fun onCloseTickerClicked() {
        shopPerformanceAdapter.removeTickerReactivated()
        shopScorePrefManager?.setIsNeedShowTickerReactivated(false)
    }

    /**
     * SectionFaqListener
     */
    override fun onHelpCenterClicked() {
        context?.let {
            RouteManager.route(
                it,
                ApplinkConstInternalGlobal.WEBVIEW,
                ShopScoreConstant.HELP_URL
            )
        }
        shopScorePenaltyTracking.clickHelpCenterFaqNewSeller()
    }

    /**
     * SectionFaqListener
     */
    override fun onImpressHelpCenter() {
        shopScorePenaltyTracking.impressHelpCenterFaqNewSeller(isNewSeller)
    }

    /**
     * ItemPMPotentialPMProListener
     */
    override fun onGotoBenefitPMPro() {
        context?.let {
            RouteManager.route(
                it,
                ApplinkConstInternalGlobal.WEBVIEW,
                ShopScoreConstant.SELLER_EDU_PM_PRO_URL
            )
        }
    }

    override fun onPMToPMProPage() {
        goToPowerMerchantSubscribe(PARAM_PM_PRO)
    }

    override fun onProtectedParameterChevronClicked(descParameterRelief: String) {
        val bottomSheetProtectedParameter = BottomSheetProtectedParameter.createInstance(
            descParameterRelief
        )
        bottomSheetProtectedParameter.show(childFragmentManager)
    }

    override fun stopPreparePerformancePageMonitoring() {
        shopScorePerformanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        shopScorePerformanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        shopScorePerformanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        shopScorePerformanceMonitoringListener?.startRenderPerformanceMonitoring()
        binding?.rvShopPerformance?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                shopScorePerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                shopScorePerformanceMonitoringListener?.stopPerformanceMonitoring()
                binding?.rvShopPerformance?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): ShopScorePerformanceMonitoringListener? {
        return if (context is ShopScorePerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    private fun goToSellerEdu(sellerEduUrl: String) {
        context?.let {
            RouteManager.route(
                it,
                ApplinkConstInternalGlobal.WEBVIEW,
                sellerEduUrl
            )
        }
    }

    private fun goToYoutubePage(videoId: String) {
        context?.let {
            it.startActivity(ShopPerformanceYoutubeActivity.createIntent(it, videoId))
        }
    }

    private fun setPageBackground() {
        try {
            context?.let {
                activity?.window?.decorView?.setBackgroundColor(it.getResColor(R.color.shop_score_page_dms_background))
            }
        } catch (e: Resources.NotFoundException) {
            Timber.e(e)
        }
    }

    private fun goToFaqSection() {
        val faqData = shopPerformanceAdapter.list.find { it is SectionFaqUiModel }
        if (faqData != null) {
            context?.let {
                val positionFaqSection = shopPerformanceAdapter.list.indexOf(faqData)
                val smoothScroller: RecyclerView.SmoothScroller =
                    object : LinearSmoothScroller(it) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_END
                        }
                    }
                smoothScroller.targetPosition = positionFaqSection
                binding?.rvShopPerformance?.layoutManager?.startSmoothScroll(smoothScroller)
            }
        }
        shopScorePenaltyTracking.clickLearnShopPerformanceNewSeller()
    }

    private fun toggleMenuForNewSeller() {
        if (isNewSeller) {
            menu?.findItem(PENALTY_WARNING_MENU_ID)?.isVisible = false
            menu?.findItem(INFO_MENU_ID)?.isVisible = false
        } else {
            menu?.findItem(PENALTY_WARNING_MENU_ID)?.isVisible = true
            if (!viewModel.userSession.isShopOfficialStore)
                menu?.findItem(INFO_MENU_ID)?.isVisible = true
        }
    }

    private fun impressMenuShopPerformance() {
        if (menu?.findItem(PENALTY_WARNING_MENU_ID)?.isVisible == true)
            if (!isNewSeller) shopScorePenaltyTracking.impressMenuPenalty()

        if (menu?.findItem(INFO_MENU_ID)?.isVisible == true)
            if (!viewModel.userSession.isShopOfficialStore)
                shopScorePenaltyTracking.impressMenuInfoPage()
    }

    private fun showPenaltyBadge() {
        Handler(Looper.getMainLooper()).postDelayed({
            context?.let {
                val menuItem = menu?.findItem(PENALTY_WARNING_MENU_ID)
                if (counterPenalty < 0L) {
                    penaltyDotBadge?.showBadge(menuItem ?: return@let)
                } else {
                    penaltyDotBadge?.removeBadge(menuItem ?: return@let)
                }
            }
        }, PENALTY_BADGE_DELAY)
    }

    private fun recyclerViewScrollListenerCoachMark() {
        binding?.rvShopPerformance?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val performanceLayoutManager =
                    binding?.rvShopPerformance?.layoutManager as? LinearLayoutManager
                performanceLayoutManager?.let { layoutManager ->
                    val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

                    val itemHeaderIndex =
                        shopPerformanceAdapter.list.indexOfFirst { it is HeaderShopPerformanceUiModel }
                    val itemPeriodDetailPerformanceIndex =
                        shopPerformanceAdapter.list.indexOfFirst { it is PeriodDetailPerformanceUiModel }
                    val itemPMIndex =
                        shopPerformanceAdapter.list.indexOfFirst { it is ItemStatusPMUiModel }
                    val itemRMIndex =
                        shopPerformanceAdapter.list.indexOfFirst { it is ItemStatusRMUiModel }
                    val itemRMNonEligibleIndex =
                        shopPerformanceAdapter.list.indexOfFirst { it is SectionRMPotentialPMBenefitUiModel }
                    val itemPotentialPMProIndex =
                        shopPerformanceAdapter.list.indexOfFirst { it is SectionRMPotentialPMProUiModel }
                    val itemPMProIndex =
                        shopPerformanceAdapter.list.indexOfFirst { it is ItemStatusPMProUiModel }
                    val itemHeaderParameterDetailIndex = shopPerformanceAdapter.list.indexOfFirst {
                        it is ItemHeaderParameterDetailUiModel
                    }


                    if (coachMark?.isShowing == true) {
                        when (coachMark?.currentIndex) {
                            COACHMARK_HEADER_POSITION -> {
                                if (DeviceScreenInfo.isTablet(recyclerView.context)) {
                                    if (itemHeaderParameterDetailIndex in firstVisiblePosition..lastVisiblePosition) {
                                        coachMark?.animateShow()
                                    } else {
                                        coachMark?.animateHide()
                                    }
                                } else {
                                    if (itemHeaderIndex in firstVisiblePosition..lastVisiblePosition) {
                                        coachMark?.animateShow()
                                    } else {
                                        coachMark?.animateHide()
                                    }
                                }
                            }
                            COACHMARK_ITEM_DETAIL_POSITION -> {
                                if (DeviceScreenInfo.isTablet(recyclerView.context)) {
                                    if (itemHeaderParameterDetailIndex in firstVisiblePosition..lastVisiblePosition) {
                                        coachMark?.animateShow()
                                    } else {
                                        coachMark?.animateHide()
                                    }
                                } else {
                                    if (itemPeriodDetailPerformanceIndex in firstVisiblePosition..lastVisiblePosition) {
                                        coachMark?.animateShow()
                                    } else {
                                        coachMark?.animateHide()
                                    }
                                }
                            }
                            COACHMARK_LAST_POSITION_PM_RM -> {
                                if (itemPMIndex in firstVisiblePosition..lastVisiblePosition
                                    || itemRMIndex in firstVisiblePosition..lastVisiblePosition
                                    || itemRMNonEligibleIndex in firstVisiblePosition..lastVisiblePosition
                                    || itemPotentialPMProIndex in firstVisiblePosition..lastVisiblePosition
                                    || itemPMProIndex in firstVisiblePosition..lastVisiblePosition
                                ) {
                                    coachMark?.animateShow()
                                } else {
                                    coachMark?.animateHide()
                                }
                            }
                            else -> {
                            }
                        }
                    }
                }
            }
        })
    }

    private fun getInstanceCoachMark(): Lazy<CoachMark2?> {
        return lazy {
            val coachMark = context?.let { CoachMark2(it) }
            coachMark?.setStepListener(object : CoachMark2.OnStepListener {
                override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                    when (currentIndex) {
                        COACHMARK_HEADER_POSITION -> {
                            scrollToItemHeaderCoachMark()
                        }
                        COACHMARK_ITEM_DETAIL_POSITION -> {
                            scrollToItemDetailCoachMark()
                        }
                        COACHMARK_LAST_POSITION_PM_RM -> {
                            scrollToLastItemCoachMark()
                        }
                        else -> {
                        }
                    }
                }
            })
            coachMark?.onFinishListener = {
                shopScorePrefManager?.setFinishCoachMark(true)
            }
            coachMark
        }
    }

    private fun showCoachMark() {
        try {
            binding?.run {
                rvShopPerformance.post {
                    coachMark?.isDismissed = false
                    if (getCoachMarkItems(root.context).value.isNotEmpty()) {
                        coachMark?.showCoachMark(getCoachMarkItems(root.context).value)
                    }
                }
            }
        } catch (ignored: Exception) {
        }
    }

    private fun getPositionLastItemCoachMark(): Int? {
        val positionPotentialPMPro =
            shopPerformanceAdapter.list.indexOfFirst { it is SectionRMPotentialPMProUiModel }
        val positionPMPro =
            shopPerformanceAdapter.list.indexOfFirst { it is ItemStatusPMProUiModel }
        val positionPM = shopPerformanceAdapter.list.indexOfFirst { it is ItemStatusPMUiModel }
        val positionRMNonEligible =
            shopPerformanceAdapter.list.indexOfFirst { it is SectionRMPotentialPMBenefitUiModel }
        val positionRMEligible =
            shopPerformanceAdapter.list.indexOfFirst { it is ItemStatusRMUiModel }

        var position = RecyclerView.NO_POSITION

        when {
            positionPMPro != RecyclerView.NO_POSITION -> {
                position = positionPMPro
            }
            positionPotentialPMPro != RecyclerView.NO_POSITION -> {
                position = positionPotentialPMPro
            }
            positionPM != RecyclerView.NO_POSITION -> {
                position = positionPM
            }
            positionRMNonEligible != RecyclerView.NO_POSITION -> {
                position = positionRMNonEligible
            }
            positionRMEligible != RecyclerView.NO_POSITION -> {
                position = positionRMEligible
            }
        }
        return position.takeIf { it != RecyclerView.NO_POSITION }
    }

    private fun scrollToLastItemCoachMark() {
        getPositionLastItemCoachMark()?.let {
            context?.let { context ->
                val smoothScroller: RecyclerView.SmoothScroller =
                    object : LinearSmoothScroller(context) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_END
                        }
                    }
                smoothScroller.targetPosition = it
                binding?.rvShopPerformance?.layoutManager?.startSmoothScroll(smoothScroller)
            }
        }
    }

    private fun scrollToItemHeaderCoachMark() {
        scrollTo<HeaderShopPerformanceUiModel>()
    }

    private fun scrollToItemDetailCoachMark() {
        scrollTo<PeriodDetailPerformanceUiModel>()
    }

    private fun scrollToItemParameterDetailCoachMark() {
        scrollTo<ItemDetailPerformanceUiModel>(true)
    }

    private inline fun <reified T : Visitable<*>> scrollTo(isLastIndex: Boolean = false) {
        val positionItem = if (isLastIndex) {
            shopPerformanceAdapter.list.indexOfLast { it is T }
        } else {
            shopPerformanceAdapter.list.indexOfFirst { it is T }
        }

        context?.let {
            if (positionItem != RecyclerView.NO_POSITION) {
                val smoothScroller: RecyclerView.SmoothScroller =
                    object : LinearSmoothScroller(it) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_END
                        }
                    }
                smoothScroller.targetPosition = positionItem
                binding?.rvShopPerformance?.layoutManager?.startSmoothScroll(smoothScroller)
            }
        }
    }

    private fun getCoachMarkItems(context: Context): Lazy<ArrayList<CoachMark2Item>> {
        return lazy {
            arrayListOf<CoachMark2Item>().apply {

                val headerPerformanceView = if (DeviceScreenInfo.isTablet(context)) {
                    getHeaderPerformanceViewTablet()
                } else {
                    getHeaderPerformanceView()
                }

                headerPerformanceView?.let { headerView ->
                    add(
                        CoachMark2Item(
                            headerView,
                            getString(R.string.title_coachmark_shop_score_1),
                            getString(R.string.desc_coachmark_shop_score_1),
                            position = CoachMark2.POSITION_BOTTOM
                        )
                    )
                }

                val periodDetailPerformanceView = if (DeviceScreenInfo.isTablet(context)) {
                    getPeriodDetailPerformanceViewTablet()
                } else {
                    getPeriodDetailPerformanceView()
                }

                periodDetailPerformanceView?.let { periodDetailView ->
                    add(
                        CoachMark2Item(
                            periodDetailView,
                            getString(R.string.title_coachmark_shop_score_2),
                            getString(R.string.desc_coachmark_shop_score_2),
                            position = CoachMark2.POSITION_TOP
                        )
                    )
                }

                getSectionLastItemViewCoachMark()?.let { lastItemView ->
                    add(
                        CoachMark2Item(
                            lastItemView,
                            getString(R.string.title_coachmark_shop_score_3),
                            getString(R.string.desc_coachmark_shop_score_3),
                            position = CoachMark2.POSITION_TOP
                        )
                    )
                }
            }
        }
    }

    private fun getSectionLastItemViewCoachMark(): View? {
        return getPositionLastItemCoachMark()?.let { lastItemPosition ->
            val layoutManager = binding?.rvShopPerformance?.layoutManager as? LinearLayoutManager
            val lastItemView = layoutManager?.findLastCompletelyVisibleItemPosition()
            val view =
                lastItemView?.let {
                    binding?.rvShopPerformance?.layoutManager?.getChildAt(
                        lastItemPosition
                    )
                }
            val viewHolder = view?.let {
                binding?.rvShopPerformance?.findContainingViewHolder(it)
            }
            viewHolder?.itemView
        }
    }

    private inline fun <reified T : Visitable<*>> getViewHolder(): View? {
        val position = shopPerformanceAdapter.list.indexOfFirst { it is T }.takeIf {
            it != RecyclerView.NO_POSITION
        }
        val view = position?.let { binding?.rvShopPerformance?.layoutManager?.getChildAt(it) }
        val viewHolder = view?.let { binding?.rvShopPerformance?.findContainingViewHolder(it) }
        return viewHolder?.itemView
    }

    private fun getHeaderPerformanceView(): View? {
        return getViewHolder<HeaderShopPerformanceUiModel>()
    }

    private fun getPeriodDetailPerformanceView(): View? {
        return getViewHolder<PeriodDetailPerformanceUiModel>()
    }

    private fun getHeaderPerformanceViewTablet(): View? {
        return getViewHolder<ItemHeaderParameterDetailUiModel>()?.findViewById(R.id.headerPerformanceWidget)
    }

    private fun getPeriodDetailPerformanceViewTablet(): View? {
        return getViewHolder<ItemHeaderParameterDetailUiModel>()?.findViewById(R.id.rvParameterDetailTablet)
    }

    private fun goToPenaltyPage() {
        context?.let { RouteManager.route(it, ApplinkConstInternalMarketplace.SHOP_PENALTY) }
    }

    private fun goToPowerMerchantSubscribe(tab: String) {
        val appLink = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
        val appLinkPMTab =
            Uri.parse(appLink).buildUpon().appendQueryParameter(TAB_PM_PARAM, tab).build()
                .toString()
        context?.let { RouteManager.route(context, appLinkPMTab) }
    }

    private fun setupAdapter() {
        binding?.rvShopPerformance?.run {
            layoutManager = context?.let { LinearLayoutManager(it) }
            adapter = shopPerformanceAdapter
        }
    }

    private fun observeShopPeriod() {
        observe(viewModel.shopInfoPeriod) {
            when (it) {
                is Success -> {
                    viewModel.getShopScoreLevel(it.data)
                    this.isNewSeller = it.data.isNewSeller
                    toggleMenuForNewSeller()
                    impressMenuShopPerformance()
                }
                is Fail -> {
                    hideLoading()
                    shopPerformanceAdapter.setShopPerformanceError(
                        ItemShopPerformanceErrorUiModel(
                            it.throwable
                        )
                    )
                    coachMark?.dismissCoachMark()
                    ShopScoreReputationErrorLogger.logToCrashlytic(
                        String.format(
                            ShopScoreReputationErrorLogger.SHOP_INFO_PM_SETTING_INFO_ERROR,
                            ShopScoreReputationErrorLogger.SHOP_PERFORMANCE
                        ), it.throwable
                    )
                }
            }
        }
        loadData(true)
    }

    private fun observeShopPerformancePage() {
        observe(viewModel.shopPerformancePage) {
            hideLoading()
            when (it) {
                is Success -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    shopPerformanceAdapter.setShopPerformanceData(it.data.first)
                    this.shopScoreWrapperResponse = it.data.second
                    val headerShopPerformanceUiModel =
                        it.data.first.filterIsInstance<HeaderShopPerformanceUiModel>().firstOrNull()
                    counterPenalty = headerShopPerformanceUiModel?.scorePenalty.orZero()
                    showPenaltyBadge()
                    processShowCoachMark()
                    context?.let { context ->
                        processShowPopupEndTenure(it.data.first, context)
                    }
                }
                is Fail -> {
                    shopPerformanceAdapter.setShopPerformanceError(
                        ItemShopPerformanceErrorUiModel(
                            it.throwable
                        )
                    )
                    coachMark?.dismissCoachMark()
                    ShopScoreReputationErrorLogger.logToCrashlytic(
                        ShopScoreReputationErrorLogger.SHOP_PERFORMANCE_ERROR, it.throwable
                    )
                }
            }
        }
    }

    private fun processShowPopupEndTenure(data: List<BaseShopPerformance>, context: Context) {
        val headerShopPerformanceUiModel = if (DeviceScreenInfo.isTablet(context)) {
            data.filterIsInstance<ItemHeaderParameterDetailUiModel>()
                .firstOrNull()?.headerShopPerformanceUiModel
        } else {
            data.filterIsInstance<HeaderShopPerformanceUiModel>().firstOrNull()
        }
        headerShopPerformanceUiModel?.let { headerPerformanceUiModel ->
            showPopupEndTenureNewSeller(
                headerPerformanceUiModel
            )
        }
    }

    private fun processShowCoachMark() {
        if (shopScorePrefManager?.getFinishCoachMark() == false && !isNewSeller) {
            context?.let {
                if (DeviceScreenInfo.isTablet(it)) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        showCoachMark()
                    }, COACH_MARK_RENDER_SHOW)
                } else {
                    postDelayShowCoachMarkMobile()
                }
            } ?: postDelayShowCoachMarkMobile()
        }
    }

    private fun postDelayShowCoachMarkMobile() {
        Handler(Looper.getMainLooper()).postDelayed({
            scrollToItemParameterDetailCoachMark()
            showCoachMark()
        }, COACH_MARK_RENDER_SHOW)
    }

    private fun onSwipeRefreshShopPerformance() {
        binding?.shopPerformanceSwipeRefresh?.setOnRefreshListener {
            loadData(false)
            showPenaltyBadge()
            coachMark?.dismissCoachMark()
        }
    }

    private fun loadData(isFirstLoad: Boolean) {
        showLoading()
        viewModel.getShopInfoPeriod(isFirstLoad)
    }

    private fun showLoading() {
        shopPerformanceAdapter.run {
            removeShopPerformanceData()
            removeShopPerformanceError()
            setShopPerformanceLoading(LoadingModel())
        }
        binding?.shopPerformanceSwipeRefresh?.isRefreshing = false
    }

    private fun hideLoading() {
        shopPerformanceAdapter.removeShopPerformanceLoading()
    }

    private fun setupActionBar() {
        (activity as? AppCompatActivity)?.run {
            supportActionBar?.hide()
            setSupportActionBar(binding?.shopPerformanceToolbar)
            supportActionBar?.run {
                title = getString(R.string.title_shop_performance)
            }
        }
    }

    private fun showPopupEndTenureNewSeller(
        headerShopPerformanceUiModel: HeaderShopPerformanceUiModel
    ) {
        val isShowPopupEndTenure = headerShopPerformanceUiModel.isShowPopupEndTenure
        if (!isShowPopupEndTenure) return

        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        val popupEndTenureUiModel = PopupEndTenureUiModel(
            powerMerchantData = headerShopPerformanceUiModel.powerMerchantData,
            shopScore = headerShopPerformanceUiModel.shopScore,
            shopLevel = headerShopPerformanceUiModel.shopLevel
        )
        val bottomSheetPopupEndTenure =
            BottomSheetPopupEndTenure.newInstance(cacheManager?.id.orEmpty())

        bottomSheetPopupEndTenure.setOnDismissListener {
            if (isShowPopupEndTenure) {
                shopScorePrefManager?.setIsShowPopupEndTenure(false)
            }
        }
        if (isShowPopupEndTenure) {
            if (isMoreThanSixHours()) {
                cacheManager?.put(
                    BottomSheetPopupEndTenure.KEY_ITEM_END_TENURE_POP_UP,
                    popupEndTenureUiModel
                )
                bottomSheetPopupEndTenure.show(childFragmentManager)
            }
        }
    }

    private fun isMoreThanSixHours(): Boolean {
        val cal = Calendar.getInstance()
        cal.timeInMillis = System.currentTimeMillis()
        return cal.get(Calendar.HOUR_OF_DAY) >= SIX_HOURS_OF_DAY
    }

    private fun splitCompatInstall() {
        activity?.let{
            SplitCompat.installActivity(it)
        }
    }

    companion object {

        val PENALTY_WARNING_MENU_ID = R.id.menu_penalty_shop_performance
        val INFO_MENU_ID = R.id.menu_info_shop_performance

        private const val PENALTY_BADGE_DELAY = 1000L
        private const val COACH_MARK_RENDER_SHOW = 1000L
        private const val SIX_HOURS_OF_DAY = 6

        private const val COACHMARK_LAST_POSITION_PM_RM = 2
        private const val COACHMARK_HEADER_POSITION = 0
        private const val COACHMARK_ITEM_DETAIL_POSITION = 1
        private const val SCREEN_NAME = "MA - Shop Performance"
        private const val TAB_PM_PARAM = "tab"
        private const val PARAM_PM = "pm"
        private const val PARAM_PM_PRO = "pm_pro"

        @JvmStatic
        fun newInstance(): ShopPerformancePageFragment {
            return ShopPerformancePageFragment()
        }
    }
}