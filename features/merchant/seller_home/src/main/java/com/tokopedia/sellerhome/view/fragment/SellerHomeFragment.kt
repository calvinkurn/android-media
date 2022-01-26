package com.tokopedia.sellerhome.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.gm.common.utils.PMShopScoreInterruptHelper
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.seller.active.common.plt.LoadTimeMonitoringActivity
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.seller_migration_common.listener.SellerHomeFragmentListener
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.analytic.NavigationSearchTracking
import com.tokopedia.sellerhome.analytic.NavigationTracking
import com.tokopedia.sellerhome.analytic.SellerHomeTracking
import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.analytic.performance.HomeLayoutLoadTimeMonitoring
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_ANNOUNCEMENT_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_BAR_CHART_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_CARD_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_CAROUSEL_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_LINE_GRAPH_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_MILESTONE_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_MULTI_LINE_GRAPH_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_PIE_CHART_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_POST_LIST_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_PROGRESS_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_RECOMMENDATION_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_TABLE_TRACE
import com.tokopedia.sellerhome.common.FragmentType
import com.tokopedia.sellerhome.common.SellerHomeConst
import com.tokopedia.sellerhome.common.errorhandler.SellerHomeErrorHandler
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.databinding.FragmentSahBinding
import com.tokopedia.sellerhome.di.component.HomeDashboardComponent
import com.tokopedia.sellerhome.domain.model.PROVINCE_ID_EMPTY
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.newrelic.SellerHomeNewRelic
import com.tokopedia.sellerhome.view.FragmentChangeCallback
import com.tokopedia.sellerhome.view.SellerHomeDiffUtilCallback
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity
import com.tokopedia.sellerhome.view.model.ShopShareDataUiModel
import com.tokopedia.sellerhome.view.viewhelper.SellerHomeLayoutManager
import com.tokopedia.sellerhome.view.viewhelper.ShopShareHelper
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeViewModel
import com.tokopedia.sellerhome.view.widget.toolbar.NotificationDotBadge
import com.tokopedia.sellerhomecommon.common.EmptyLayoutException
import com.tokopedia.sellerhomecommon.common.WidgetListener
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.common.const.SellerHomeUrl
import com.tokopedia.sellerhomecommon.domain.model.TableAndPostDataKey
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.TooltipBottomSheet
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.WidgetFilterBottomSheet
import com.tokopedia.sellerhomecommon.utils.Utils
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageProcessingUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeFragment : BaseListFragment<BaseWidgetUiModel<*>, WidgetAdapterFactoryImpl>(),
    WidgetListener, CoroutineScope, SellerHomeFragmentListener, FragmentChangeCallback {

    companion object {
        @JvmStatic
        fun newInstance() = SellerHomeFragment()

        val NOTIFICATION_MENU_ID = R.id.menu_sah_notification
        val SEARCH_MENU_ID = R.id.menu_sah_search

        private const val REQ_CODE_MILESTONE_WIDGET = 8043
        private const val NOTIFICATION_BADGE_DELAY = 2000L
        private const val TAG_TOOLTIP = "seller_home_tooltip"
        private const val ERROR_LAYOUT = "Error get layout data."
        private const val ERROR_WIDGET = "Error get widget data."
        private const val ERROR_TICKER = "Error get ticker data."
        private const val TOAST_DURATION = 5000L
        private const val DEFAULT_HEIGHT_DP = 720f
        private const val RV_TOP_POSITION = 0
        private const val TICKER_FIRST_INDEX = 0
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var remoteConfig: SellerHomeRemoteConfig

    @Inject
    lateinit var newRelic: SellerHomeNewRelic

    @Inject
    lateinit var pmShopScoreInterruptHelper: PMShopScoreInterruptHelper

    @Inject
    lateinit var shopShareHelper: ShopShareHelper

    private val sellerHomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SellerHomeViewModel::class.java)
    }

    private val deviceDisplayHeight: Float
        get() = try {
            val dm = resources.displayMetrics
            dm.heightPixels / dm.density
        } catch (ex: Exception) {
            DEFAULT_HEIGHT_DP
        }

    private var sellerHomeListener: Listener? = null
    private var menu: Menu? = null
    private val notificationDotBadge: NotificationDotBadge? by lazy {
        NotificationDotBadge(context ?: return@lazy null)
    }

    private val isNewLazyLoad by lazy {
        Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 && remoteConfig.isSellerHomeDashboardNewLazyLoad()
    }

    private var notifCenterCount = 0
    private var isFirstLoad = true
    private var isErrorToastShown = false

    private var performanceMonitoringSellerHomePltCompleted = false
    private var performanceMonitoringSellerHomePlt: HomeLayoutLoadTimeMonitoring? = null

    private var emptyState: EmptyStateUnify? = null

    private var recommendationWidgetView: View? = null
    private var navigationOtherMenuView: View? = null
    private val coachMark: CoachMark2? by lazy {
        context?.let {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                CoachMark2(it)
            } else null
        }
    }
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var shopShareData: ShopShareDataUiModel? = null
    private var shopImageFilePath: String = ""
    private var binding by autoClearedNullable<FragmentSahBinding>()

    private val recyclerView: RecyclerView?
        get() = try {
            super.getRecyclerView(view)
        } catch (ex: Exception) {
            null
        }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun getScreenName(): String = TrackingConstant.SCREEN_NAME_SELLER_HOME

    override fun initInjector() {
        getComponent(HomeDashboardComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPltPerformanceMonitoring()
        startHomeLayoutNetworkMonitoring()
        startHomeLayoutCustomMetric()
        val deviceHeight = if (isNewLazyLoad) {
            deviceDisplayHeight
        } else {
            null
        }
        sellerHomeViewModel.getWidgetLayout(deviceHeight)
        (activity as? SellerHomeActivity)?.attachSellerHomeFragmentChangeCallback(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentSahBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideTooltipIfExist()
        setupView()

        observeWidgetLayoutLiveData()
        observeShopLocationLiveData()
        observeWidgetData(sellerHomeViewModel.cardWidgetData, WidgetType.CARD)
        observeWidgetData(sellerHomeViewModel.lineGraphWidgetData, WidgetType.LINE_GRAPH)
        observeWidgetData(sellerHomeViewModel.progressWidgetData, WidgetType.PROGRESS)
        observeWidgetData(sellerHomeViewModel.postListWidgetData, WidgetType.POST_LIST)
        observeWidgetData(sellerHomeViewModel.carouselWidgetData, WidgetType.CAROUSEL)
        observeWidgetData(sellerHomeViewModel.tableWidgetData, WidgetType.TABLE)
        observeWidgetData(sellerHomeViewModel.pieChartWidgetData, WidgetType.PIE_CHART)
        observeWidgetData(sellerHomeViewModel.barChartWidgetData, WidgetType.BAR_CHART)
        observeWidgetData(sellerHomeViewModel.multiLineGraphWidgetData, WidgetType.MULTI_LINE_GRAPH)
        observeWidgetData(sellerHomeViewModel.announcementWidgetData, WidgetType.ANNOUNCEMENT)
        observeWidgetData(sellerHomeViewModel.recommendationWidgetData, WidgetType.RECOMMENDATION)
        observeWidgetData(sellerHomeViewModel.milestoneWidgetData, WidgetType.MILESTONE)
        observeTickerLiveData()
        observeCustomTracePerformanceMonitoring()
        observeShopShareData()
        observeShopShareTracker()

        context?.let { UpdateShopActiveService.startService(it) }
        setupPMShopScoreInterrupt()
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstLoad)
            reloadPage()
    }

    override fun onPause() {
        super.onPause()
        hideTooltipIfExist()
    }

    override fun onDestroy() {
        super.onDestroy()
        pmShopScoreInterruptHelper.destroy()
        shopShareHelper.removeTemporaryShopImage(shopImageFilePath)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleMilestoneWidgetFinishedMission(requestCode)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            SellerHomeTracking.sendScreen(screenName)
            view?.post {
                requestVisibleWidgetsData()
            }
            recyclerView?.post {
                resetWidgetImpressionHolder()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.sah_menu_home_toolbar, menu)

        for (i in 0 until menu.size()) {
            menu.getItem(i)?.let { menuItem ->
                menuItem.actionView?.setOnClickListener {
                    onOptionsItemSelected(menuItem)
                }
            }
        }

        this.menu = menu
        showNotificationBadge()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == NOTIFICATION_MENU_ID) {
            RouteManager.route(requireContext(), ApplinkConst.SELLER_INFO)
            NavigationTracking.sendClickNotificationEvent()
        } else if (item.itemId == SEARCH_MENU_ID) {
            RouteManager.route(requireContext(), ApplinkConstInternalSellerapp.SELLER_SEARCH)
            NavigationSearchTracking.sendClickSearchMenuEvent(userSession.userId.orEmpty())
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getAdapterTypeFactory(): WidgetAdapterFactoryImpl {
        return WidgetAdapterFactoryImpl(this)
    }

    override fun onItemClicked(t: BaseWidgetUiModel<*>?) {

    }

    override fun loadData(page: Int) {

    }

    override fun setCurrentFragmentType(fragmentType: Int) {
        if (fragmentType != FragmentType.HOME) {
            if (coachMark?.isDismissed == false) {
                coachMark?.dismissCoachMark()
            }
        }
    }

    override fun onTooltipClicked(tooltip: TooltipUiModel) {
        if (!isAdded || context == null) return
        val tooltipBottomSheet =
            (childFragmentManager.findFragmentByTag(TAG_TOOLTIP) as? TooltipBottomSheet)
                ?: TooltipBottomSheet.createInstance()
        tooltipBottomSheet.init(requireContext(), tooltip)
        tooltipBottomSheet.show(childFragmentManager, TAG_TOOLTIP)
    }

    override fun removeWidget(position: Int, widget: BaseWidgetUiModel<*>) {
        recyclerView?.post {
            adapter.data.remove(widget)
            adapter.notifyItemRemoved(position)
        }
    }

    override fun getIsShouldRemoveWidget(): Boolean = true

    override fun onRemoveWidget(position: Int) {}

    override fun sendCardImpressionEvent(model: CardWidgetUiModel) {
        val cardValue = model.data?.value ?: "0"
        val state = model.data?.state?.name.orEmpty()
        SellerHomeTracking.sendImpressionCardEvent(model.dataKey, state, cardValue)
    }

    override fun sendCardClickTracking(model: CardWidgetUiModel) {
        SellerHomeTracking.sendClickCardEvent(
            model.dataKey,
            model.data?.state?.name.orEmpty(), model.data?.value ?: "0"
        )
    }

    override fun sendCarouselImpressionEvent(
        dataKey: String,
        carouselItems: List<CarouselItemUiModel>,
        position: Int
    ) {
        SellerHomeTracking.sendImpressionCarouselItemBannerEvent(dataKey, carouselItems, position)
    }

    override fun sendCarouselClickTracking(
        dataKey: String,
        carouselItems: List<CarouselItemUiModel>,
        position: Int
    ) {
        SellerHomeTracking.sendClickCarouselItemBannerEvent(dataKey, carouselItems, position)
    }

    override fun sendCarouselCtaClickEvent(dataKey: String) {
        SellerHomeTracking.sendClickCarouselCtaEvent(dataKey)
    }

    override fun sendCarouselEmptyStateCtaClickEvent(element: CarouselWidgetUiModel) {}

    override fun sendDescriptionImpressionEvent(model: DescriptionWidgetUiModel) {
        SellerHomeTracking.sendImpressionDescriptionEvent(model.dataKey)
    }

    override fun sendDescriptionCtaClickEvent(model: DescriptionWidgetUiModel) {
        SellerHomeTracking.sendClickDescriptionEvent(model.dataKey)
    }

    override fun sendLineGraphImpressionEvent(model: LineGraphWidgetUiModel) {
        SellerHomeTracking.sendImpressionLineGraphEvent(model)
    }

    override fun sendLineGraphCtaClickEvent(model: LineGraphWidgetUiModel) {
        SellerHomeTracking.sendClickLineGraphEvent(model)
    }

    override fun sendLineChartEmptyStateCtaClickEvent(model: LineGraphWidgetUiModel) {
        SellerHomeTracking.sendClickEmptyCtaLineGraphEvent(model)
    }

    override fun sendPosListItemClickEvent(element: PostListWidgetUiModel, post: PostItemUiModel) {
        SellerHomeTracking.sendClickPostItemEvent(element, post)
    }

    override fun sendPostListFilterClick(element: PostListWidgetUiModel) {
        SellerHomeTracking.sendPostListFilterClick(element)
    }

    override fun sendRecommendationCtaClickEvent(element: RecommendationWidgetUiModel) {
        SellerHomeTracking.sendRecommendationCtaClickEvent(element)
    }

    override fun sendRecommendationImpressionEvent(element: RecommendationWidgetUiModel) {
        SellerHomeTracking.sendRecommendationImpressionEvent(element)
    }

    override fun sendRecommendationItemClickEvent(
        element: RecommendationWidgetUiModel,
        item: RecommendationItemUiModel
    ) {
        SellerHomeTracking.sendRecommendationItemClickEvent(element.dataKey, item)
    }

    override fun showRecommendationWidgetCoachMark(view: View) {
        recommendationWidgetView = view
        showCoachMarkShopScore()
    }

    private fun showCoachMarkShopScore() {
        val coachMarkItems by getCoachMarkItems()
        val isEligibleShowRecommendationCoachMark =
            !pmShopScoreInterruptHelper.getRecommendationCoachMarkStatus()
        if (isEligibleShowRecommendationCoachMark) {
            if (coachMarkItems.isNotEmpty()) {
                coachMark?.onFinishListener = {
                    pmShopScoreInterruptHelper.saveRecommendationCoachMarkFlag()
                }
                coachMark?.isDismissed = false
                coachMark?.showCoachMark(coachMarkItems)
            }
        }
    }

    override fun onScrollToTop() {
        recyclerView?.post {
            recyclerView?.smoothScrollToPosition(RV_TOP_POSITION)
        }
    }

    override fun setOnErrorWidget(position: Int, widget: BaseWidgetUiModel<*>, error: String) {
        showErrorToaster(error)
    }

    override fun showPostFilter(element: PostListWidgetUiModel, adapterPosition: Int) {
        if (!isAdded || context == null) return

        val postFilterBottomSheet =
            (childFragmentManager.findFragmentByTag(WidgetFilterBottomSheet.POST_FILTER_TAG) as? WidgetFilterBottomSheet)
                ?: WidgetFilterBottomSheet.newInstance()
        postFilterBottomSheet.init(
            requireContext(),
            com.tokopedia.sellerhomecommon.R.string.shc_select_category,
            element.postFilter
        ) {
            recyclerView?.post {
                val copiedWidget = element.copy().apply { data = null }
                notifyWidgetChanged(copiedWidget)
                getPostData(listOf(element))
            }
        }.show(childFragmentManager, WidgetFilterBottomSheet.POST_FILTER_TAG)
    }

    override fun showTableFilter(element: TableWidgetUiModel, adapterPosition: Int) {
        if (!isAdded || context == null) return

        val tableFilterBottomSheet =
            (childFragmentManager.findFragmentByTag(WidgetFilterBottomSheet.TABLE_FILTER_TAG) as? WidgetFilterBottomSheet)
                ?: WidgetFilterBottomSheet.newInstance()
        tableFilterBottomSheet.init(
            requireContext(),
            com.tokopedia.sellerhomecommon.R.string.shc_select_statistic_data,
            element.tableFilters
        ) {
            recyclerView?.post {
                val copiedWidget = element.copy().apply { data = null }
                notifyWidgetChanged(copiedWidget)
                getTableData(listOf(element))
            }
            SellerHomeTracking.sendTableFilterClickEvent(element)
        }.show(childFragmentManager, WidgetFilterBottomSheet.TABLE_FILTER_TAG)
    }

    override fun onMilestoneMissionActionClickedListener(
        element: MilestoneWidgetUiModel,
        mission: BaseMilestoneMissionUiModel,
        missionPosition: Int
    ) {
        when (mission) {
            is MilestoneMissionUiModel -> {
                when (mission.missionButton.urlType) {
                    BaseMilestoneMissionUiModel.UrlType.REDIRECT -> {
                        activity?.let {
                            val mIntent = RouteManager.getIntent(it, mission.missionButton.appLink)
                            it.startActivityForResult(mIntent, REQ_CODE_MILESTONE_WIDGET)
                        }
                    }
                    BaseMilestoneMissionUiModel.UrlType.SHARE -> {
                        shopShareHelper.removeTemporaryShopImage(shopImageFilePath)
                        setupShopSharing()
                    }
                }
            }
            is MilestoneFinishMissionUiModel -> {
                activity?.let {
                    val mIntent = RouteManager.getIntent(it, mission.getWebViewAppLink())
                    it.startActivityForResult(mIntent, REQ_CODE_MILESTONE_WIDGET)
                    SellerHomeTracking.sendMilestoneFinishedMissionCtaClickEvent()
                }
            }
        }

        SellerHomeTracking.sendMilestoneMissionCtaClickEvent(mission, missionPosition)
    }

    override fun sendPostListImpressionEvent(element: PostListWidgetUiModel) {
        SellerHomeTracking.sendImpressionPostEvent(element)
    }

    override fun sendPostListCtaClickEvent(element: PostListWidgetUiModel) {
        SellerHomeTracking.sendClickPostSeeMoreEvent(element)
    }

    override fun sendPostListEmptyStateCtaClickEvent(element: PostListWidgetUiModel) {
        SellerHomeTracking.sendPostEmptyStateCtaClick(element)
    }

    override fun sendProgressImpressionEvent(dataKey: String, stateColor: String, valueScore: Int) {
        SellerHomeTracking.sendImpressionProgressBarEvent(dataKey, stateColor, valueScore)
    }

    override fun sendProgressCtaClickEvent(dataKey: String, stateColor: String, valueScore: Int) {
        SellerHomeTracking.sendClickProgressBarEvent(dataKey, stateColor, valueScore)
    }

    override fun sendTableImpressionEvent(
        model: TableWidgetUiModel,
        slidePosition: Int,
        maxSlidePosition: Int,
        isSlideEmpty: Boolean
    ) {
        val isFirstSlide = slidePosition == 0
        if (isFirstSlide) {
            SellerHomeTracking.sendTableImpressionEvent(model, isSlideEmpty)
        }
    }

    override fun sendTableOnSwipeEvent(
        element: TableWidgetUiModel,
        slidePosition: Int,
        maxSlidePosition: Int,
        isSlideEmpty: Boolean
    ) {
        SellerHomeTracking.sendTableOnSwipeEvent(
            element,
            slidePosition,
            maxSlidePosition,
            isSlideEmpty
        )
    }

    override fun sendTableHyperlinkClickEvent(dataKey: String, url: String, isEmpty: Boolean) {
        SellerHomeTracking.sendTableClickHyperlinkEvent(dataKey, url, isEmpty, userSession.userId)
    }

    override fun sendTableEmptyStateCtaClickEvent(element: TableWidgetUiModel) {
        SellerHomeTracking.sendTableEmptyStateCtaClick(element)
    }

    override fun sendTableSeeMoreClickEvent(element: TableWidgetUiModel, isEmpty: Boolean) {
        SellerHomeTracking.sendTableSeeMoreClickEvent(element, isEmpty)
    }

    override fun sendPieChartImpressionEvent(model: PieChartWidgetUiModel) {
        SellerHomeTracking.sendPieChartImpressionEvent(model)
    }

    override fun sendPieChartEmptyStateCtaClickEvent(element: PieChartWidgetUiModel) {
        SellerHomeTracking.sendPieChartEmptyStateCtaClickEvent(element)
    }

    override fun sendPieChartSeeMoreClickEvent(model: PieChartWidgetUiModel) {
        SellerHomeTracking.sendPieChartSeeMoreCtaClickEvent(model)
    }

    override fun sendBarChartImpressionEvent(model: BarChartWidgetUiModel) {
        SellerHomeTracking.sendBarChartImpressionEvent(model)
    }

    override fun sendBarChartEmptyStateCtaClick(model: BarChartWidgetUiModel) {
        SellerHomeTracking.sendBarChartEmptyStateCtaClickEvent(model)
    }

    override fun sendBarChartSeeMoreClickEvent(model: BarChartWidgetUiModel) {
        SellerHomeTracking.sendBarChartSeeMoreClickEvent(model)
    }

    override fun sendMultiLineGraphImpressionEvent(element: MultiLineGraphWidgetUiModel) {
        SellerHomeTracking.sendMultiLineGraphImpressionEvent(element, userSession.userId)
    }

    override fun sendMultiLineGraphMetricClick(
        element: MultiLineGraphWidgetUiModel,
        metric: MultiLineMetricUiModel
    ) {
        SellerHomeTracking.sendMultiLineGraphMetricClick(element, metric)
    }

    override fun sendMultiLineGraphCtaClick(element: MultiLineGraphWidgetUiModel) {
        SellerHomeTracking.sendMultiLineGraphCtaClick(element)
    }

    override fun sendMultiLineGraphEmptyStateCtaClick(element: MultiLineGraphWidgetUiModel) {
        SellerHomeTracking.sendMultiLineGraphEmptyStateCtaClick(element, userSession.userId)
    }

    override fun sendAnnouncementImpressionEvent(element: AnnouncementWidgetUiModel) {
        SellerHomeTracking.sendAnnouncementImpressionEvent(element)
    }

    override fun sendAnnouncementClickEvent(element: AnnouncementWidgetUiModel) {
        SellerHomeTracking.sendAnnouncementClickEvent(element)
    }

    override fun sendMilestoneMissionImpressionEvent(
        mission: BaseMilestoneMissionUiModel,
        position: Int
    ) {
        SellerHomeTracking.sendMilestoneMissionImpressionEvent(mission, position)
    }

    override fun sendMilestoneWidgetImpressionEvent(element: MilestoneWidgetUiModel) {
        SellerHomeTracking.sendMilestoneWidgetImpressionEvent(element)
    }

    override fun sendMilestoneWidgetCtaClickEvent() {
        SellerHomeTracking.sendMilestoneWidgetCtaClickEvent()
    }

    override fun sendMilestoneWidgetMinimizeClickEvent() {
        SellerHomeTracking.sendMilestoneWidgetMinimizeClickEvent()
    }

    override fun reloadMultiLineGraphWidget(element: MultiLineGraphWidgetUiModel) {
        getMultiLineGraphData(listOf(element))
    }

    override fun reloadMilestoneWidget(model: MilestoneWidgetUiModel) {
        getMilestoneData(listOf(model))
    }

    override fun reloadLineGraphWidget(element: LineGraphWidgetUiModel) {
        getLineGraphData(listOf(element))
    }

    override fun reloadRecommendationWidget(element: RecommendationWidgetUiModel) {
        getRecommendationData(listOf(element))
    }

    override fun reloadPostListWidget(element: PostListWidgetUiModel) {
        getPostData(listOf(element))
    }

    override fun reloadCardWidget(element: CardWidgetUiModel) {
        getCardData(listOf(element))
    }

    override fun reloadPieChartWidget(element: PieChartWidgetUiModel) {
        getPieChartData(listOf(element))
    }

    override fun reloadTableWidget(element: TableWidgetUiModel) {
        getTableData(listOf(element))
    }

    fun setNavigationOtherMenuView(view: View?) {
        if (navigationOtherMenuView == null) {
            navigationOtherMenuView = view
        }
    }

    fun showNotificationBadge() {
        Handler(Looper.getMainLooper()).postDelayed({
            context?.let {
                val menuItem = menu?.findItem(NOTIFICATION_MENU_ID)
                if (notifCenterCount > 0) {
                    notificationDotBadge?.showBadge(menuItem ?: return@let)
                } else {
                    notificationDotBadge?.removeBadge(menuItem ?: return@let)
                }
            }
        }, NOTIFICATION_BADGE_DELAY)
    }

    private fun initPltPerformanceMonitoring() {
        performanceMonitoringSellerHomePlt =
            (activity as? SellerHomeActivity)?.performanceMonitoringSellerHomeLayoutPlt
    }

    private fun hideTooltipIfExist() {
        val bottomSheet = childFragmentManager.findFragmentByTag(TAG_TOOLTIP)
        if (bottomSheet is BottomSheetUnify)
            bottomSheet.dismiss()
    }

    private fun setupView() = binding?.root?.run {
        emptyState = findViewById(R.id.empty_state_seller_home)

        val sellerHomeLayoutManager = SellerHomeLayoutManager(context, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return try {
                        val isCardWidget = adapter.data[position].widgetType != WidgetType.CARD
                        if (isCardWidget) spanCount else 1
                    } catch (e: IndexOutOfBoundsException) {
                        spanCount
                    }
                }
            }

            setOnVerticalScrollListener {
                requestVisibleWidgetsData()
                handleCoachMarkVisibility()
            }
        }
        recyclerView?.run {
            layoutManager = sellerHomeLayoutManager
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        binding?.swipeRefreshLayout?.setOnRefreshListener {
            reloadPage()
            showNotificationBadge()
            sellerHomeListener?.getShopInfo()
        }

        binding?.sahGlobalError?.setActionClickListener {
            reloadPage()
        }
        emptyState?.run {
            setTitle(context?.getString(R.string.sah_failed_to_get_information).orEmpty())
            setImageDrawable(
                resources.getDrawable(
                    com.tokopedia.globalerror.R.drawable.unify_globalerrors_500,
                    null
                )
            )
            setPrimaryCTAText(
                context?.getString(com.tokopedia.globalerror.R.string.error500Action).orEmpty()
            )
            setPrimaryCTAClickListener {
                reloadPage()
            }
        }

        setRecyclerViewLayoutAnimation()

        setViewBackground()
    }

    /**
     * load only visible widget on screen, except card widget should load all directly
     * */
    private fun requestVisibleWidgetsData() {
        val layoutManager = recyclerView?.layoutManager as? GridLayoutManager ?: return
        val firstVisible = layoutManager.findFirstVisibleItemPosition()
        val lastVisible = layoutManager.findLastVisibleItemPosition()

        val visibleWidgets = mutableListOf<BaseWidgetUiModel<*>>()
        adapter.data.forEachIndexed { index, widget ->
            if (!widget.isLoaded) {
                if (widget.widgetType == WidgetType.CARD) {
                    visibleWidgets.add(widget)
                } else {
                    if (index in firstVisible..lastVisible) {
                        visibleWidgets.add(widget)
                    }
                }
            }
        }

        if (visibleWidgets.isNotEmpty()) getWidgetsData(visibleWidgets)
    }

    private fun reloadPage() = binding?.run {
        val isAdapterNotEmpty = adapter.data.isNotEmpty()
        setProgressBarVisibility(!isAdapterNotEmpty)
        swipeRefreshLayout.isRefreshing = isAdapterNotEmpty

        sahGlobalError.gone()
        emptyState?.gone()
        val deviceHeight =
            if (isNewLazyLoad) {
                deviceDisplayHeight
            } else {
                null
            }
        sellerHomeViewModel.getWidgetLayout(deviceHeight)
        sellerHomeViewModel.getTicker()
    }

    private fun List<BaseWidgetUiModel<*>>.setLoading() {
        forEach {
            it.isLoading = true
            it.isLoaded = true
        }
    }

    private fun getCardData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<CardWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_CARD_TRACE)
        sellerHomeViewModel.getCardWidgetData(dataKeys)
    }

    private fun getLineGraphData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<LineGraphWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_LINE_GRAPH_TRACE)
        sellerHomeViewModel.getLineGraphWidgetData(dataKeys)
    }

    private fun getProgressData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<ProgressWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_PROGRESS_TRACE)
        sellerHomeViewModel.getProgressWidgetData(dataKeys)
    }

    private fun getPostData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys: List<TableAndPostDataKey> =
            widgets.filterIsInstance<PostListWidgetUiModel>().map {
                val postFilter = it.postFilter.find { filter -> filter.isSelected }?.value.orEmpty()
                return@map TableAndPostDataKey(it.dataKey, postFilter, it.maxData, it.maxDisplay)
            }
        startCustomMetric(SELLER_HOME_POST_LIST_TRACE)
        sellerHomeViewModel.getPostWidgetData(dataKeys)
    }

    private fun getCarouselData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<CarouselWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_CAROUSEL_TRACE)
        sellerHomeViewModel.getCarouselWidgetData(dataKeys)
    }

    private fun getTableData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys: List<TableAndPostDataKey> =
            widgets.filterIsInstance<TableWidgetUiModel>().map {
                val postFilter =
                    it.tableFilters.find { filter -> filter.isSelected }
                        ?.value.orEmpty()
                return@map TableAndPostDataKey(it.dataKey, postFilter, it.maxData, it.maxDisplay)
            }
        startCustomMetric(SELLER_HOME_TABLE_TRACE)
        sellerHomeViewModel.getTableWidgetData(dataKeys)
    }

    private fun getPieChartData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<PieChartWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_PIE_CHART_TRACE)
        sellerHomeViewModel.getPieChartWidgetData(dataKeys)
    }

    private fun getBarChartData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<BarChartWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_BAR_CHART_TRACE)
        sellerHomeViewModel.getBarChartWidgetData(dataKeys)
    }

    private fun getMultiLineGraphData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<MultiLineGraphWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_MULTI_LINE_GRAPH_TRACE)
        sellerHomeViewModel.getMultiLineGraphWidgetData(dataKeys)
    }

    private fun getRecommendationData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<RecommendationWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_RECOMMENDATION_TRACE)
        sellerHomeViewModel.getRecommendationWidgetData(dataKeys)
    }

    private fun getAnnouncementData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<AnnouncementWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_ANNOUNCEMENT_TRACE)
        sellerHomeViewModel.getAnnouncementWidgetData(dataKeys)
    }

    private fun getMilestoneData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<MilestoneWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_MILESTONE_TRACE)
        sellerHomeViewModel.getMilestoneWidgetData(dataKeys)
    }

    private fun setupShopSharing() {
        ImageHandler.loadImageWithTarget(
            context,
            shopShareData?.shopSnippetURL.orEmpty(),
            object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    val savedFile = ImageProcessingUtil.writeImageToTkpdPath(
                        resource,
                        Bitmap.CompressFormat.PNG
                    )
                    if (savedFile != null) {
                        shopImageFilePath = savedFile.absolutePath
                        initShopShareBottomSheet()
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // no op
                }
            }
        )
        if (shopShareData == null) {
            val milestoneWidget = adapter.data.firstOrNull { it is MilestoneWidgetUiModel }
            milestoneWidget?.let {
                handleShopShareMilestoneWidget(it)
                notifyWidgetChanged(it)
            }
        }
    }

    private fun initShopShareBottomSheet() {
        val shareListener = object : ShareBottomsheetListener {

            override fun onShareOptionClicked(shareModel: ShareModel) {
                val shareDataModel = ShopShareHelper.DataModel(
                    shareModel = shareModel,
                    shopImageFilePath = shopImageFilePath,
                    shopCoreUrl = shopShareData?.shopUrl.orEmpty()
                )
                activity?.let {
                    shopShareHelper.onShareOptionClicked(
                        it,
                        view,
                        shareDataModel,
                        callback = { shareModel, _ ->
                            setOnShopShareOptionClicked(shareModel)
                        }
                    )
                }
            }

            override fun onCloseOptionClicked() {
                //no op
            }
        }

        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(shareListener)
            setMetaData(
                userSession.shopName,
                userSession.shopAvatar,
                ""
            )
            setOgImageUrl(shopShareData?.shopSnippetURL.orEmpty())
            imageSaved(shopImageFilePath)
        }
        universalShareBottomSheet?.show(childFragmentManager, this)
    }

    private fun setOnShopShareOptionClicked(shareModel: ShareModel) {
        val socialMediaName = when (shareModel) {
            is ShareModel.CopyLink -> {
                SellerHomeConst.SHOP_SHARE_DEFAULT_CHANNEL
            }
            is ShareModel.Others -> {
                SellerHomeConst.SHOP_SHARE_OTHERS_CHANNEL
            }
            else -> shareModel.socialMediaName.orEmpty()
        }
        SellerHomeTracking.sendMilestoneMissionShareClickEvent(socialMediaName)
        sellerHomeViewModel.sendShopShareQuestTracker(socialMediaName)
        universalShareBottomSheet?.dismiss()
    }

    private fun setProgressBarVisibility(isShown: Boolean) {
        binding?.progressBarSah?.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    private fun observeShopLocationLiveData() {
        sellerHomeViewModel.shopLocation.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> setOnSuccessGetShopLocation(result.data)
                is Fail -> {
                    SellerHomeErrorHandler.logException(
                        result.throwable, SellerHomeErrorHandler.SHOP_LOCATION
                    )
                    SellerHomeErrorHandler.logExceptionToServer(
                        SellerHomeErrorHandler.SELLER_HOME_TAG,
                        result.throwable,
                        SellerHomeErrorHandler.SHOP_LOCATION,
                        userSession.deviceId.orEmpty()
                    )
                }
            }
            setProgressBarVisibility(false)
        })

        setProgressBarVisibility(true)
        sellerHomeViewModel.getShopLocation()
    }

    private fun setOnSuccessGetShopLocation(data: ShippingLoc) {
        if (data.provinceID == PROVINCE_ID_EMPTY) {
            activity?.let {
                RouteManager.route(it, ApplinkConst.CREATE_SHOP)
                it.finish()
            }
        }
    }

    private fun observeWidgetLayoutLiveData() {
        sellerHomeViewModel.widgetLayout.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    stopLayoutCustomMetric(result.data)
                    setOnSuccessGetLayout(result.data)
                    setRecommendationCoachMarkEligibility()
                }
                is Fail -> {
                    stopCustomMetric(
                        SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_TRACE,
                        true
                    )
                    setOnErrorGetLayout(result.throwable)
                }
            }
        })

        setProgressBarVisibility(true)
    }

    private fun stopLayoutCustomMetric(widgets: List<BaseWidgetUiModel<*>>) {
        val isFromCache = widgets.firstOrNull()?.isFromCache == true
        stopCustomMetric(
            SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_TRACE,
            isFromCache
        )
    }

    private fun startHomeLayoutCustomMetric() {
        startCustomMetric(SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_TRACE)
    }

    private fun startHomeLayoutNetworkMonitoring() {
        performanceMonitoringSellerHomePlt?.startNetworkPerformanceMonitoring()
    }

    private fun startHomeLayoutRenderMonitoring() {
        performanceMonitoringSellerHomePlt?.startRenderPerformanceMonitoring()
    }

    @Suppress("UNCHECKED_CAST")
    private fun stopHomeLayoutRenderMonitoring(fromCache: Boolean) {
        performanceMonitoringSellerHomePlt?.addDataSourceAttribution(fromCache)
        performanceMonitoringSellerHomePlt?.stopRenderPerformanceMonitoring()
        activity?.let {
            (it as LoadTimeMonitoringActivity).loadTimeMonitoringListener?.onStopPltMonitoring()
            newRelic.sendSellerHomeNewRelicData(
                it.application,
                screenName,
                userSession.userId,
                performanceMonitoringSellerHomePlt
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setOnSuccessGetLayout(widgets: List<BaseWidgetUiModel<*>>) {
        binding?.sahGlobalError?.gone()
        emptyState?.gone()
        recyclerView?.visible()

        adapter.hideLoading()
        hideSnackBarRetry()
        updateScrollListenerState(false)

        var isWidgetHasError = false
        val newWidgetFromCache = widgets.firstOrNull()?.isFromCache ?: false
        if (isNewLazyLoad) {
            startHomeLayoutRenderMonitoring()
            stopPltMonitoringIfNotCompleted(fromCache = newWidgetFromCache)
        }
        val newWidgets = if (adapter.data.isEmpty()) {
            widgets as List<BaseWidgetUiModel<BaseDataUiModel>>
        } else {
            if (newWidgetFromCache) {
                return
            } else {
                val oldWidgets = adapter.data as List<BaseWidgetUiModel<BaseDataUiModel>>
                val newWidgets = arrayListOf<BaseWidgetUiModel<*>>()
                widgets.forEach { newWidget ->
                    oldWidgets.find { isTheSameWidget(it, newWidget) }.let { oldWidget ->
                        if (isNewLazyLoad) {
                            // If there are card widgets exist in adapter data, set the previous value in the latest widget
                            // to enable animation after pull to refresh
                            if (newWidget is CardWidgetUiModel) {
                                newWidget.data?.previousValue =
                                    (oldWidget as? CardWidgetUiModel)?.data?.previousValue
                            }
                            // Set flag if initial widget layout list has error data to determine showing toaster or not
                            if (!newWidget.data?.error.isNullOrBlank()) {
                                isWidgetHasError = true
                            }
                        }
                        if (oldWidget == null) {
                            newWidgets.add(newWidget)
                        } else {
                            if (oldWidget.isFromCache && !oldWidget.needToRefreshData(newWidget as BaseWidgetUiModel<BaseDataUiModel>)) {
                                newWidget.apply {
                                    data = oldWidget.data
                                    isLoaded = oldWidget.isLoaded
                                    isLoading = oldWidget.isLoading
                                }
                                val widgetData = newWidget.data
                                if (widgetData == null || !shouldRemoveWidget(
                                        newWidget,
                                        widgetData
                                    )
                                ) {
                                    newWidgets.add(newWidget)
                                }
                            } else {
                                newWidgets.add(newWidget)
                                Unit
                            }
                        }
                        recyclerView?.post {
                            handleShopShareMilestoneWidget(newWidget)
                        }
                    }
                }
                newWidgets
            }
        }

        notifyWidgetWithSdkChecking {
            updateWidgets(newWidgets as List<BaseWidgetUiModel<BaseDataUiModel>>)
        }

        val isAnyWidgetFromCache = adapter.data.any { it.isFromCache }
        if (!isAnyWidgetFromCache) {
            binding?.swipeRefreshLayout?.isEnabled = true
            binding?.swipeRefreshLayout?.isRefreshing = false
        }

        recyclerView?.post {
            requestVisibleWidgetsData()
        }

        setProgressBarVisibility(false)
        showEmptyState()

        if (isWidgetHasError) {
            showErrorToaster()
        }

        loadNextUnloadedWidget()
    }

    private fun isTheSameWidget(
        oldWidget: BaseWidgetUiModel<*>,
        newWidget: BaseWidgetUiModel<*>
    ): Boolean {
        return oldWidget.widgetType == newWidget.widgetType && oldWidget.title == newWidget.title &&
                oldWidget.subtitle == newWidget.subtitle && oldWidget.appLink == newWidget.appLink &&
                oldWidget.tooltip == newWidget.tooltip && oldWidget.ctaText == newWidget.ctaText &&
                oldWidget.dataKey == newWidget.dataKey && oldWidget.isShowEmpty == newWidget.isShowEmpty &&
                oldWidget.emptyState == newWidget.emptyState
    }

    @Suppress("UNCHECKED_CAST")
    private fun getWidgetsData(widgets: List<BaseWidgetUiModel<*>>) {
        val groupedWidgets = widgets.groupBy { it.widgetType }
        groupedWidgets[WidgetType.ANNOUNCEMENT]?.run { getAnnouncementData(this) }
        groupedWidgets[WidgetType.CARD]?.run { getCardData(this) }
        groupedWidgets[WidgetType.LINE_GRAPH]?.run { getLineGraphData(this) }
        groupedWidgets[WidgetType.PROGRESS]?.run { getProgressData(this) }
        groupedWidgets[WidgetType.CAROUSEL]?.run { getCarouselData(this) }
        groupedWidgets[WidgetType.POST_LIST]?.run { getPostData(this) }
        groupedWidgets[WidgetType.TABLE]?.run { getTableData(this) }
        groupedWidgets[WidgetType.PIE_CHART]?.run { getPieChartData(this) }
        groupedWidgets[WidgetType.BAR_CHART]?.run { getBarChartData(this) }
        groupedWidgets[WidgetType.MULTI_LINE_GRAPH]?.run { getMultiLineGraphData(this) }
        groupedWidgets[WidgetType.RECOMMENDATION]?.run { getRecommendationData(this) }
        groupedWidgets[WidgetType.MILESTONE]?.run { getMilestoneData(this) }
        groupedWidgets[WidgetType.SECTION]?.run {
            recyclerView?.post {
                val newWidgetList = adapter.data.toMutableList()
                forEach { section ->
                    newWidgetList.indexOf(section).takeIf { it > -1 }?.let { index ->
                        newWidgetList[index] = section.copyWidget().apply { isLoaded = true }
                    }
                }
                updateWidgets(newWidgetList as List<BaseWidgetUiModel<BaseDataUiModel>>)
                checkLoadingWidgets()
            }
        }
    }

    private fun setOnErrorGetLayout(throwable: Throwable) = binding?.run {
        if (adapter.data.isEmpty()) {
            showErrorViewByException(throwable)
        } else {
            throwable.showErrorToaster()
            sahGlobalError.gone()
            emptyState?.gone()
        }
        swipeRefreshLayout.isRefreshing = false
        setProgressBarVisibility(false)

        SellerHomeErrorHandler.logException(
            throwable = throwable,
            message = ERROR_LAYOUT
        )
        SellerHomeErrorHandler.logExceptionToServer(
            errorTag = SellerHomeErrorHandler.SELLER_HOME_TAG,
            throwable = throwable,
            errorType = SellerHomeErrorHandler.ErrorType.ERROR_LAYOUT,
            deviceId = userSession.deviceId.orEmpty()
        )
    }

    private fun showErrorViewByException(throwable: Throwable) = binding?.run {
        val errorType: Int? = when (throwable) {
            is MessageErrorException -> null
            is UnknownHostException, is SocketTimeoutException -> GlobalError.NO_CONNECTION
            is EmptyLayoutException -> GlobalError.PAGE_NOT_FOUND
            else -> GlobalError.SERVER_ERROR
        }

        when (errorType) {
            null -> {
                sahGlobalError.gone()
                emptyState?.showMessageExceptionError(throwable)
            }
            GlobalError.PAGE_NOT_FOUND -> showEmptyState()
            else -> {
                sahGlobalError.run {
                    setType(errorType)
                    visible()
                }
                emptyState?.gone()
            }
        }
    }

    private fun showEmptyState() {
        recyclerView?.post {
            val isLayoutEmpty = adapter.data.isEmpty()
            if (isLayoutEmpty) {
                emptyState?.run {
                    if (isVisible) return@post
                    emptyState?.setImageUrl(SellerHomeUrl.IMG_LAYOUT_NO_PERMISSION)
                    setTitle(getString(R.string.sah_empty_layout_message))
                    setDescription(SellerHomeConst.EMPTY_STRING)
                    setPrimaryCTAText(SellerHomeConst.EMPTY_STRING)
                    visible()
                }
                binding?.sahGlobalError?.gone()
            }
        }
    }

    private fun showErrorToaster(errorMessage: String? = null) = binding?.run {
        if (isErrorToastShown) return@run
        isErrorToastShown = true

        val message = errorMessage ?: getString(R.string.sah_failed_to_get_information)

        Toaster.build(
            this.root, message,
            TOAST_DURATION.toInt(), Toaster.TYPE_ERROR, getString(R.string.sah_reload)
        ) {
            reloadPageOrLoadDataOfErrorWidget()
        }.show()

        Handler(Looper.getMainLooper()).postDelayed({
            isErrorToastShown = false
        }, TOAST_DURATION)
    }

    private fun Throwable.showErrorToaster() {
        context?.let {
            showErrorToaster(ErrorHandler.getErrorMessage(it, this))
        }
    }

    /**
     * if any widget that failed when load their data, the action should be load the widget data
     * else, reload the page like pull refresh
     * */
    private fun reloadPageOrLoadDataOfErrorWidget() {
        val isAnyErrorWidget = adapter.data.any { !it.data?.error.isNullOrBlank() }
        if (!isAnyErrorWidget) {
            reloadPage()
            return
        }

        isErrorToastShown = false

        val errorWidgets: List<BaseWidgetUiModel<*>> = adapter.data.filterIndexed { index, widget ->
            val isWidgetError = !widget.data?.error.isNullOrBlank()
            if (isWidgetError) {
                //set data to null then notify adapter to show the widget shimmer
                widget.data = null
                adapter.notifyItemChanged(index)
            }
            return@filterIndexed isWidgetError
        }

        if (errorWidgets.isNotEmpty()) {
            getWidgetsData(errorWidgets)
        }
    }

    private fun observeTickerLiveData() {
        sellerHomeViewModel.homeTicker.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    stopCustomMetric(
                        SellerHomePerformanceMonitoringConstant.SELLER_HOME_TICKER_TRACE,
                        it.data.firstOrNull()?.isFromCache
                            ?: false
                    )
                    onSuccessGetTickers(it.data)
                }
                is Fail -> {
                    stopCustomMetric(
                        SellerHomePerformanceMonitoringConstant.SELLER_HOME_TICKER_TRACE,
                        false
                    )
                    SellerHomeErrorHandler.logException(
                        throwable = it.throwable,
                        message = ERROR_TICKER,
                    )
                    SellerHomeErrorHandler.logExceptionToServer(
                        errorTag = SellerHomeErrorHandler.SELLER_HOME_TAG,
                        throwable = it.throwable,
                        errorType = SellerHomeErrorHandler.ErrorType.ERROR_TICKER,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                    binding?.relTicker?.gone()
                }
            }
        })
        startCustomMetric(SellerHomePerformanceMonitoringConstant.SELLER_HOME_TICKER_TRACE)
        sellerHomeViewModel.getTicker()
    }

    private fun observeCustomTracePerformanceMonitoring() {
        sellerHomeViewModel.startWidgetCustomMetricTag.observe(viewLifecycleOwner) { tag ->
            startCustomMetric(tag)
        }
        sellerHomeViewModel.stopWidgetType.observe(viewLifecycleOwner) { widgetType ->
            stopSellerHomeFragmentWidgetPerformanceMonitoring(widgetType, false)
        }
    }

    private fun observeShopShareData() {
        observe(sellerHomeViewModel.shopShareData) {
            when (it) {
                is Success -> {
                    shopShareData = it.data
                }
                is Fail -> {
                    SellerHomeErrorHandler.logException(
                        it.throwable, SellerHomeErrorHandler.SHOP_SHARE_DATA
                    )
                    SellerHomeErrorHandler.logExceptionToServer(
                        errorTag = SellerHomeErrorHandler.SELLER_HOME_TAG,
                        throwable = it.throwable,
                        errorType = SellerHomeErrorHandler.SHOP_SHARE_DATA,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
        }
    }

    private fun observeShopShareTracker() {
        observe(sellerHomeViewModel.shopShareTracker) {
            if (it is Fail) {
                SellerHomeErrorHandler.logException(
                    it.throwable, SellerHomeErrorHandler.SHOP_SHARE_TRACKING
                )
                SellerHomeErrorHandler.logExceptionToServer(
                    SellerHomeErrorHandler.SELLER_HOME_TAG,
                    it.throwable,
                    SellerHomeErrorHandler.SHOP_SHARE_TRACKING,
                    userSession.deviceId.orEmpty()
                )
            }
        }
    }

    private fun setViewBackground() = binding?.run {
        val isOfficialStore = userSession.isShopOfficialStore
        val isPowerMerchant = userSession.isPowerMerchantIdle || userSession.isGoldMerchant
        when {
            isOfficialStore -> viewBgShopStatus.setBackgroundResource(R.drawable.sah_shop_state_bg_official_store)
            isPowerMerchant -> viewBgShopStatus.setBackgroundResource(R.drawable.sah_shop_state_bg_power_merchant)
            else -> viewBgShopStatus.setBackgroundColor(root.context.getResColor(android.R.color.transparent))
        }
    }

    private inline fun <reified D : BaseDataUiModel> observeWidgetData(
        liveData: LiveData<Result<List<D>>>,
        type: String
    ) {
        liveData.observe(viewLifecycleOwner, { result ->
            startHomeLayoutRenderMonitoring()
            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> {
                    stopSellerHomeFragmentWidgetPerformanceMonitoring(type, isFromCache = false)
                    stopPltMonitoringIfNotCompleted(fromCache = false)
                    result.throwable.setOnErrorWidgetState<D, BaseWidgetUiModel<D>>(type)
                }
            }
            loadNextUnloadedWidget()
        })
    }

    /**
     * Load next unloaded indexed widget in adapter after the previous one is complete
     */
    private fun loadNextUnloadedWidget() {
        if (isNewLazyLoad) {
            adapter.data?.find { it.isNeedToLoad() }?.let { newWidgets ->
                getWidgetsData(listOf(newWidgets))
            }
        }
    }

    private fun stopSellerHomeFragmentWidgetPerformanceMonitoring(
        type: String,
        isFromCache: Boolean
    ) {
        when (type) {
            WidgetType.CARD -> stopCustomMetric(SELLER_HOME_CARD_TRACE, isFromCache)
            WidgetType.LINE_GRAPH -> stopCustomMetric(SELLER_HOME_LINE_GRAPH_TRACE, isFromCache)
            WidgetType.PROGRESS -> stopCustomMetric(SELLER_HOME_PROGRESS_TRACE, isFromCache)
            WidgetType.POST_LIST -> stopCustomMetric(SELLER_HOME_POST_LIST_TRACE, isFromCache)
            WidgetType.CAROUSEL -> stopCustomMetric(SELLER_HOME_CAROUSEL_TRACE, isFromCache)
            WidgetType.TABLE -> stopCustomMetric(SELLER_HOME_TABLE_TRACE, isFromCache)
            WidgetType.PIE_CHART -> stopCustomMetric(SELLER_HOME_PIE_CHART_TRACE, isFromCache)
            WidgetType.BAR_CHART -> stopCustomMetric(SELLER_HOME_BAR_CHART_TRACE, isFromCache)
            WidgetType.MULTI_LINE_GRAPH -> stopCustomMetric(
                SELLER_HOME_MULTI_LINE_GRAPH_TRACE,
                isFromCache
            )
            WidgetType.ANNOUNCEMENT -> stopCustomMetric(SELLER_HOME_ANNOUNCEMENT_TRACE, isFromCache)
            WidgetType.RECOMMENDATION -> stopCustomMetric(
                SELLER_HOME_RECOMMENDATION_TRACE,
                isFromCache
            )
            WidgetType.MILESTONE -> stopCustomMetric(
                SELLER_HOME_MILESTONE_TRACE,
                isFromCache
            )
        }
    }

    private fun startCustomMetric(tag: String) {
        performanceMonitoringSellerHomePlt?.startCustomMetric(tag)
    }

    private fun stopCustomMetric(tag: String, isFromCache: Boolean) {
        performanceMonitoringSellerHomePlt?.addDataSourceAttribution(isFromCache)
        performanceMonitoringSellerHomePlt?.stopCustomMetric(tag)
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> List<D>.setOnSuccessWidgetState(
        widgetType: String
    ) {
        val isFromCache = firstOrNull()?.isFromCache == true
        stopSellerHomeFragmentWidgetPerformanceMonitoring(widgetType, isFromCache)
        stopPltMonitoringIfNotCompleted(isFromCache)
        val newWidgetList = adapter.data.toMutableList()
        forEach { widgetData ->
            newWidgetList.indexOfFirst {
                it.dataKey == widgetData.dataKey && it.widgetType == widgetType
            }.takeIf { it > RecyclerView.NO_POSITION }?.let { index ->
                val widget = newWidgetList.getOrNull(index)
                if (widget is W) {
                    if (shouldRemoveWidget(widget, widgetData)) {
                        newWidgetList.removeAt(index)
                        removeEmptySections(newWidgetList, index)
                    } else {
                        val copiedWidget = widget.copyWidget()
                        copiedWidget.data = widgetData
                        copiedWidget.isLoading = widget.data?.isFromCache ?: false

                        handleShopShareMilestoneWidget(copiedWidget)

                        newWidgetList[index] = copiedWidget
                    }
                }
            }
        }
        notifyWidgetWithSdkChecking {
            updateWidgets(newWidgetList as List<BaseWidgetUiModel<BaseDataUiModel>>)
        }
        binding?.root?.addOneTimeGlobalLayoutListener {
            recyclerView?.post {
                checkLoadingWidgets()
                requestVisibleWidgetsData()
            }
        }
    }

    private fun <D : BaseDataUiModel> handleShopShareMilestoneWidget(widget: BaseWidgetUiModel<D>) {
        if (widget is MilestoneWidgetUiModel) {
            val shareMission = widget.data?.milestoneMissions?.firstOrNull {
                return@firstOrNull it.missionButton
                    .urlType == BaseMilestoneMissionUiModel.UrlType.SHARE
            }
            val isShareMissionAvailable = !shareMission?.missionCompletionStatus.orFalse()
            if (isShareMissionAvailable) {
                sellerHomeViewModel.getShopInfoById()
            }
        }
    }

    private fun shouldRemoveWidget(
        widget: BaseWidgetUiModel<*>,
        widgetData: BaseDataUiModel
    ): Boolean {
        return !widget.isFromCache && !widgetData.isFromCache && (!widgetData.showWidget || (!widget.isShowEmpty && widgetData.isWidgetEmpty()))
    }

    private fun removeEmptySections(
        newWidgetList: MutableList<BaseWidgetUiModel<*>>,
        removedWidgetIndex: Int
    ) {
        val previousWidget = newWidgetList.getOrNull(removedWidgetIndex - 1)
        val widgetReplacement = newWidgetList.getOrNull(removedWidgetIndex)
        if ((widgetReplacement == null || widgetReplacement is SectionWidgetUiModel) && previousWidget is SectionWidgetUiModel) {
            newWidgetList.removeAt(removedWidgetIndex - 1)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> Throwable.setOnErrorWidgetState(
        widgetType: String
    ) {
        val message = this.message.orEmpty()
        val newWidgetList = adapter.data.map { widget ->
            val isSameWidgetType = widget.widgetType == widgetType
            if (widget is W && widget.data == null && widget.isLoaded && isSameWidgetType) {
                widget.copyWidget().apply {
                    data = D::class.java.newInstance().apply {
                        error = message
                    }
                    isLoading = widget.data?.isFromCache ?: false
                }
            } else {
                widget
            }
        }

        logWidgetException(widgetType, this)

        notifyWidgetWithSdkChecking {
            updateWidgets(newWidgetList as List<BaseWidgetUiModel<BaseDataUiModel>>)
        }
        showErrorToaster()
        view?.addOneTimeGlobalLayoutListener {
            requestVisibleWidgetsData()
            checkLoadingWidgets()
        }
    }

    private fun logWidgetException(widgetType: String, throwable: Throwable) {
        // Log error to crashlytics and scalyr.
        // We define layoutId value as joined list of error layout id. Ex: (100, 150)
        // The extras will be defined as widget type + layout id as JSON object.
        // Ex: ({"widget_type": "lineGraph", "layout_id": "100, 150"})
        val layoutId = adapter.data.mapNotNull {
            if (it.widgetType == widgetType) it.id else null
        }.joinToString(", ")
        val widgetErrorExtraMap = mapOf(
            SellerHomeErrorHandler.WIDGET_TYPE_KEY to widgetType,
            SellerHomeErrorHandler.LAYOUT_ID_KEY to layoutId
        )
        SellerHomeErrorHandler.logException(
            throwable = throwable,
            message = "$ERROR_WIDGET $widgetType"
        )

        SellerHomeErrorHandler.logExceptionToServer(
            errorTag = SellerHomeErrorHandler.SELLER_HOME_TAG,
            throwable = throwable,
            errorType = SellerHomeErrorHandler.ErrorType.ERROR_WIDGET,
            deviceId = userSession.deviceId.orEmpty(),
            extras = widgetErrorExtraMap
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun notifyWidgetChanged(widget: BaseWidgetUiModel<*>) {
        val newWidgetList = adapter.data.map {
            return@map if (it.dataKey == widget.dataKey && it.widgetType == widget.widgetType) {
                widget
            } else {
                it
            }
        }
        notifyWidgetWithSdkChecking {
            updateWidgets(newWidgetList as List<BaseWidgetUiModel<BaseDataUiModel>>)
        }
        checkLoadingWidgets()
    }

    private fun onSuccessGetTickers(tickers: List<TickerItemUiModel>) {
        binding?.relTicker?.visibility = if (tickers.isEmpty()) View.GONE else View.VISIBLE
        binding?.tickerView?.run {
            val tickerImpressHolders = mutableListOf<ImpressHolder>()
            val tickersData = tickers.map {
                TickerData(it.title, it.message, it.type, true, it).also {
                    tickerImpressHolders.add(ImpressHolder())
                }
            }

            val adapter = TickerPagerAdapter(context, tickersData)
            addPagerView(adapter, tickersData)
            adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    if (!RouteManager.route(context, linkUrl.toString())) {
                        if (itemData is TickerItemUiModel) {
                            RouteManager.route(context, itemData.redirectUrl)
                        }
                    }
                    (itemData as? TickerItemUiModel)?.let {
                        SellerHomeTracking.sendHomeTickerCtaClickEvent(
                            it.id,
                            it.type
                        )
                    }
                }
            })

            // Add impression listener on first page of ticker
            addSellerHomeImpressionListener(
                tickerImpressHolders.firstOrNull(),
                tickers.firstOrNull()
            )

            // Add impression listener if ticker view pager swiped to another page
            onTickerPageChangeListener = { pageIndex ->
                if (pageIndex > TICKER_FIRST_INDEX) {
                    addSellerHomeImpressionListener(
                        tickerImpressHolders.getOrNull(pageIndex),
                        tickers.getOrNull(pageIndex)
                    )
                }
            }
        }
    }

    private fun Ticker.addSellerHomeImpressionListener(
        impressHolder: ImpressHolder?,
        ticker: TickerItemUiModel?
    ) {
        impressHolder?.let { holder ->
            ticker?.let { ticker ->
                addOnImpressionListener(holder) {
                    SellerHomeTracking.sendHomeTickerImpressionEvent(
                        ticker.id,
                        ticker.type
                    )
                }
            }
        }
    }

    private fun stopPltMonitoringIfNotCompleted(fromCache: Boolean) {
        if (!performanceMonitoringSellerHomePltCompleted) {
            performanceMonitoringSellerHomePltCompleted = true
            recyclerView?.addOneTimeGlobalLayoutListener {
                stopHomeLayoutRenderMonitoring(fromCache)
            }
        }
    }

    fun setNotifCenterCounter(count: Int) {
        this.notifCenterCount = count
        showNotificationBadge()
    }

    @Suppress("UNCHECKED_CAST")
    private fun resetWidgetImpressionHolder() {
        val newWidgetList = adapter.data.map { widget ->
            val isInvoked = widget.impressHolder.isInvoke
            when (widget) {
                !is SectionWidgetUiModel,
                !is TickerWidgetUiModel,
                !is WhiteSpaceUiModel -> {
                    if (isInvoked) {
                        widget.copyWidget().apply { impressHolder = ImpressHolder() }
                    } else {
                        widget
                    }
                }
                else -> widget
            }
        }
        notifyWidgetWithSdkChecking {
            updateWidgets(newWidgetList as List<BaseWidgetUiModel<BaseDataUiModel>>)
        }
        checkLoadingWidgets()
    }

    @Suppress("UNCHECKED_CAST")
    private fun updateWidgets(newWidgets: List<BaseWidgetUiModel<BaseDataUiModel>>) {
        try {
            val diffUtilCallback = SellerHomeDiffUtilCallback(
                adapter.data as List<BaseWidgetUiModel<BaseDataUiModel>>,
                newWidgets
            )
            val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallback)
            adapter.data.clear()
            adapter.data.addAll(newWidgets)
            diffUtilResult.dispatchUpdatesTo(adapter)
        } catch (e: Exception) {
            SellerHomeErrorHandler.logException(e, SellerHomeErrorHandler.UPDATE_WIDGET_ERROR)
        }
    }

    @SuppressLint("AnnotateVersionCheck")
    private fun notifyWidgetWithSdkChecking(callback: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            callback()
        } else {
            recyclerView?.post {
                callback()
            }
        }
    }

    private fun checkLoadingWidgets() {
        val isAnyLoadingWidget =
            adapter.data.any { it.isLoading || (it.isLoaded && it.isFromCache) }
        if (!isAnyLoadingWidget) {
            binding?.swipeRefreshLayout?.isRefreshing = false
            hideLoading()
        }
    }

    private fun setRecyclerViewLayoutAnimation() {
        if (isNewLazyLoad) {
            context?.let {
                val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(
                    it, R.anim.seller_home_rv_layout_animation
                )
                recyclerView?.layoutAnimation = animation
            }
        }
    }

    private fun EmptyStateUnify.showMessageExceptionError(throwable: Throwable) {
        val errorMessage = context?.let {
            ErrorHandler.getErrorMessage(it, throwable)
        } ?: this@SellerHomeFragment.context?.getString(R.string.sah_failed_to_get_information)
            .orEmpty()
        setDescription(errorMessage)
        visible()
    }

    private fun BaseWidgetUiModel<*>.isNeedToLoad(): Boolean {
        return !isLoaded && this !is SectionWidgetUiModel && this !is TickerWidgetUiModel &&
                this !is DescriptionWidgetUiModel && this !is WhiteSpaceUiModel
    }

    private fun setupPMShopScoreInterrupt() {
        activity?.let {
            pmShopScoreInterruptHelper.showInterrupt(it, viewLifecycleOwner, childFragmentManager)
        }
    }

    private fun setRecommendationCoachMarkEligibility() {
        val isEligibleShowRecommendationCoachMark =
            !pmShopScoreInterruptHelper.getRecommendationCoachMarkStatus()
        if (isEligibleShowRecommendationCoachMark) {
            scrollToRecommendationWidget()
        }
    }

    private fun scrollToRecommendationWidget() {
        val widgetPosition = adapter.data.indexOfFirst { it is RecommendationWidgetUiModel }
        if (widgetPosition != RecyclerView.NO_POSITION) {
            val layoutManager = recyclerView?.layoutManager as? SellerHomeLayoutManager
            layoutManager?.scrollToPositionWithOffset(widgetPosition, 0)
            recyclerView?.post {
                requestVisibleWidgetsData()
            }
        }
    }

    private fun handleCoachMarkVisibility() {
        val recommendationWidgetCoachMarkIndex = 0
        if (coachMark?.currentIndex != recommendationWidgetCoachMarkIndex) return

        val layoutManager = recyclerView?.layoutManager as? SellerHomeLayoutManager
        layoutManager?.let {
            if (coachMark?.isDismissed == true) {
                val firstRecommendationWidget =
                    adapter.data.indexOfFirst { it is RecommendationWidgetUiModel }
                val firstVisibleIndex = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleIndex = layoutManager.findLastCompletelyVisibleItemPosition()
                if ((lastVisibleIndex != RecyclerView.NO_POSITION && lastVisibleIndex == firstRecommendationWidget)
                    || firstVisibleIndex >= firstRecommendationWidget
                ) {
                    showCoachMarkShopScore()
                }
            } else {
                val firstRecommendationWidget =
                    adapter.data.indexOfFirst { it is RecommendationWidgetUiModel }
                if (firstRecommendationWidget != RecyclerView.NO_POSITION) {
                    val firstVisibleIndex = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleIndex = layoutManager.findLastVisibleItemPosition()
                    if (firstRecommendationWidget !in firstVisibleIndex..lastVisibleIndex.minus(1)) {
                        coachMark?.dismissCoachMark()
                    }
                }
            }
        }
    }

    private fun getCoachMarkItems(): Lazy<ArrayList<CoachMark2Item>> {
        return lazy {
            val coachMarkItems = arrayListOf<CoachMark2Item>()
            recommendationWidgetView?.let {
                coachMarkItems.add(
                    CoachMark2Item(
                        anchorView = it,
                        title = getString(R.string.sah_recommendation_coach_mark_title),
                        description = getString(R.string.sah_recommendation_coach_mark_description),
                        position = CoachMark2.POSITION_BOTTOM
                    )
                )
            }
            navigationOtherMenuView?.let {
                coachMarkItems.add(
                    CoachMark2Item(
                        anchorView = it,
                        title = getString(R.string.sah_other_menu_coach_mark_title),
                        description = getString(R.string.sah_other_menu_coach_mark_description),
                        position = CoachMark2.POSITION_TOP
                    )
                )
            }
            return@lazy coachMarkItems
        }
    }

    private fun handleMilestoneWidgetFinishedMission(requestCode: Int) {
        view?.post {
            if (requestCode == REQ_CODE_MILESTONE_WIDGET) {
                val milestoneWidgets = adapter.data.filterIsInstance<MilestoneWidgetUiModel>()
                getMilestoneData(milestoneWidgets)
            }
        }
    }

    interface Listener {
        fun getShopInfo()
    }
}
