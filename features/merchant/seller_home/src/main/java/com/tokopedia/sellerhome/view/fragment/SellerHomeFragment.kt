package com.tokopedia.sellerhome.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
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
import com.tokopedia.gm.common.utils.CoachMarkPrefHelper
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.seller.active.common.plt.LoadTimeMonitoringActivity
import com.tokopedia.seller.active.common.worker.UpdateShopActiveWorker
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
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_CALENDAR_TRACE
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
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_UNIFICATION_TRACE
import com.tokopedia.sellerhome.common.SellerHomeConst
import com.tokopedia.sellerhome.common.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.common.errorhandler.SellerHomeErrorHandler
import com.tokopedia.sellerhome.common.newrelic.SellerHomeNewRelic
import com.tokopedia.sellerhome.databinding.FragmentSahBinding
import com.tokopedia.sellerhome.di.component.HomeDashboardComponent
import com.tokopedia.sellerhome.domain.model.PROVINCE_ID_EMPTY
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.view.SellerHomeDiffUtilCallback
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity
import com.tokopedia.sellerhome.view.customview.NotificationDotBadge
import com.tokopedia.sellerhome.view.dialog.NewSellerDialog
import com.tokopedia.sellerhome.view.helper.NewSellerJourneyHelper
import com.tokopedia.sellerhome.view.model.SellerHomeDataUiModel
import com.tokopedia.sellerhome.view.model.ShopShareDataUiModel
import com.tokopedia.sellerhome.view.model.ShopStateInfoUiModel
import com.tokopedia.sellerhome.view.viewhelper.SellerHomeLayoutManager
import com.tokopedia.sellerhome.view.viewhelper.ShopShareHelper
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeViewModel
import com.tokopedia.sellerhomecommon.common.DateFilterUtil
import com.tokopedia.sellerhomecommon.common.EmptyLayoutException
import com.tokopedia.sellerhomecommon.common.WidgetListener
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.common.const.SellerHomeUrl
import com.tokopedia.sellerhomecommon.domain.mapper.PostMapper
import com.tokopedia.sellerhomecommon.domain.model.TableAndPostDataKey
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseDismissibleWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseMilestoneMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarEventUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarFilterDataKeyUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem
import com.tokopedia.sellerhomecommon.presentation.model.DescriptionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.FeedbackLoopOptionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LastUpdatedDataInterface
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneFinishMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineMetricUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListPagerUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.SectionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ShopStateUiModel
import com.tokopedia.sellerhomecommon.presentation.model.SubmitWidgetDismissUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TickerWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationTabUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetDismissalResultUiModel
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.CalendarWidgetDateFilterBottomSheet
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.FeedbackLoopOptionsBottomSheet
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.PostMoreOptionBottomSheet
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.TooltipBottomSheet
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.UnificationTabBottomSheet
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.WidgetFilterBottomSheet
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

@Suppress("DEPRECATION")
class SellerHomeFragment : BaseListFragment<BaseWidgetUiModel<*>, WidgetAdapterFactoryImpl>(),
    WidgetListener, CoroutineScope, SellerHomeFragmentListener {

    companion object {
        @JvmStatic
        fun newInstance(data: SellerHomeDataUiModel? = null) = SellerHomeFragment().apply {
            data?.let {
                arguments = Bundle().apply {
                    putParcelable(KEY_SELLER_HOME_DATA, it)
                }
            }
        }

        val NOTIFICATION_MENU_ID = R.id.menu_sah_notification
        val SEARCH_MENU_ID = R.id.menu_sah_search

        private const val KEY_SELLER_HOME_DATA = "seller_home_data"
        private const val REQ_CODE_MILESTONE_WIDGET = 8043
        private const val NOTIFICATION_BADGE_DELAY = 2000L
        private const val TAG_TOOLTIP = "seller_home_tooltip"
        private const val ERROR_LAYOUT = "Error get layout data."
        private const val ERROR_WIDGET = "Error get widget data."
        private const val ERROR_TICKER = "Error get ticker data."
        private const val TOAST_DURATION = 5000L
        private const val SHORT_TOAST_DURATION = 2000L
        private const val DEFAULT_HEIGHT_DP = 720f
        private const val RV_TOP_POSITION = 0
        private const val TICKER_FIRST_INDEX = 0
        private const val FEEDBACK_OPTION_1 = 0
        private const val FEEDBACK_OPTION_2 = 1
        private const val FEEDBACK_OPTION_3 = 2
        private const val FEEDBACK_OPTION_4 = 3
        private const val ANNOUNCEMENT_DISMISSAL_KEY = "widget.announcement.%s"
        private const val POST_LIST_DISMISSAL_KEY = "widget.post.%s"
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
    lateinit var coachMarkPrefHelper: CoachMarkPrefHelper

    @Inject
    lateinit var shopShareHelper: ShopShareHelper

    @Inject
    lateinit var newSellerJourneyHelper: NewSellerJourneyHelper

    private val sellerHomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SellerHomeViewModel::class.java)
    }

    private val deviceDisplayHeight: Float
        get() = try {
            val dm = activity?.resources?.displayMetrics
            if (dm != null) {
                dm.heightPixels / dm.density
            } else {
                DEFAULT_HEIGHT_DP
            }
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
    private var isReloading = false
    private var shouldShowSuccessToaster: Boolean = false
    private var performanceMonitoringSellerHomePltCompleted = false
    private var performanceMonitoringSellerHomePlt: HomeLayoutLoadTimeMonitoring? = null
    private var emptyState: EmptyStateUnify? = null
    private var rebateWidgetView: View? = null
    private var navigationView: View? = null
    private var otherMenuView: View? = null
    private var unificationWidgetTitleView: View? = null
    private var rebateCoachMark: CoachMark2? = null
    private var unificationWidgetCoachMark: CoachMark2? = null
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var shopShareData: ShopShareDataUiModel? = null
    private var shopImageFilePath: String = ""
    private var binding by autoClearedNullable<FragmentSahBinding>()
    private var isNewSellerState: Boolean = false

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
        getWidgetLayout()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        observeWidgetData(sellerHomeViewModel.calendarWidgetData, WidgetType.CALENDAR)
        observeWidgetData(sellerHomeViewModel.unificationWidgetData, WidgetType.UNIFICATION)
        observeTickerLiveData()
        observeCustomTracePerformanceMonitoring()
        observeShopShareData()
        observeShopShareTracker()
        observeWidgetDismissalStatus()
        observeShopStateInfo()
        showSellerHomeToaster()

        context?.let { UpdateShopActiveWorker.execute(it) }
    }

    override fun onResume() {
        super.onResume()
        startWidgetSse()
    }

    override fun onPause() {
        super.onPause()
        stopWidgetSse()
        hideTooltipIfExist()
    }

    override fun onDestroy() {
        super.onDestroy()
        shopShareHelper.removeTemporaryShopImage(shopImageFilePath)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleMilestoneWidgetFinishedMission(requestCode)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (hidden) {
            rebateCoachMark?.dismissCoachMark()
            unificationWidgetCoachMark?.dismissCoachMark()
        } else {
            SellerHomeTracking.sendScreen(screenName)
            view?.post {
                requestVisibleWidgetsData()
            }

            recyclerView?.post {
                resetWidgetImpressionHolder()
                showRebateCoachMark()
                showUnificationCoachMarkWhenVisible()
            }

            if (!isFirstLoad) {
                getShopStateInfoIfEligible()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.sah_menu_home_toolbar, menu)

        for (i in Int.ZERO until menu.size()) {
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

    override fun onItemClicked(t: BaseWidgetUiModel<*>?) {}

    override fun loadData(page: Int) {}

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
            val widgetList = mutableListOf<BaseWidgetUiModel<*>>()
            adapter.data.forEach {
                val isRemovedWidget = it != widget
                if (isRemovedWidget) {
                    widgetList.add(it)
                }
            }
            notifyWidgetWithSdkChecking {
                updateWidgets(widgetList)
            }
        }
    }

    override fun getIsShouldRemoveWidget(): Boolean = true

    override fun onRemoveWidget(position: Int) {}

    override fun sendCardImpressionEvent(model: CardWidgetUiModel) {
        val cardValue = model.data?.value ?: Int.ZERO.toString()
        val state = model.data?.state?.name.orEmpty()
        val isSingle = model.data?.secondaryDescription.isNullOrBlank()
        SellerHomeTracking.sendImpressionCardEvent(
            dataKey = model.dataKey, state = state, cardValue = cardValue, isSingle = isSingle
        )
    }

    override fun sendCardClickTracking(model: CardWidgetUiModel) {
        val isSingle = model.data?.secondaryDescription.isNullOrBlank()
        SellerHomeTracking.sendClickCardEvent(
            dataKey = model.dataKey,
            state = model.data?.state?.name.orEmpty(),
            cardValue = model.data?.value ?: Int.ZERO.toString(),
            isSingle = isSingle
        )
    }

    override fun sendCarouselImpressionEvent(
        dataKey: String, carouselItems: List<CarouselItemUiModel>, position: Int
    ) {
        SellerHomeTracking.sendImpressionCarouselItemBannerEvent(dataKey, carouselItems, position)
    }

    override fun sendCarouselClickTracking(
        dataKey: String, carouselItems: List<CarouselItemUiModel>, position: Int
    ) {
        SellerHomeTracking.sendClickCarouselItemBannerEvent(dataKey, carouselItems, position)
    }

    override fun sendCarouselCtaClickEvent(dataKey: String) {
        SellerHomeTracking.sendClickCarouselCtaEvent(dataKey)
    }

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
        element: RecommendationWidgetUiModel, item: RecommendationItemUiModel
    ) {
        SellerHomeTracking.sendRecommendationItemClickEvent(element.dataKey, item)
    }

    override fun sendRecommendationTickerCtaClickEvent(element: RecommendationWidgetUiModel) {
        SellerHomeTracking.sendRecommendationTickerCtaClickEvent(element)
    }

    override fun onScrollToTop() {
        recyclerView?.post {
            recyclerView?.smoothScrollToPosition(RV_TOP_POSITION)
        }
    }

    override fun setOnErrorWidget(position: Int, widget: BaseWidgetUiModel<*>, error: String) {
        showErrorToaster(error)
    }

    override fun showPostFilter(element: PostListWidgetUiModel) {
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
            SellerHomeTracking.sendTableFilterClickEvent(element)
            recyclerView?.post {
                val copiedWidget = element.copy().apply { data = null }
                notifyWidgetChanged(copiedWidget)
                getTableData(listOf(element))
            }
        }.show(childFragmentManager, WidgetFilterBottomSheet.TABLE_FILTER_TAG)
    }

    override fun onMilestoneMissionActionClickedListener(
        element: MilestoneWidgetUiModel, mission: BaseMilestoneMissionUiModel, missionPosition: Int
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

    override fun sendProgressImpressionEvent(
        dataKey: String, stateColor: String, valueScore: Long
    ) {
        SellerHomeTracking.sendImpressionProgressBarEvent(dataKey, stateColor, valueScore)
    }

    override fun sendProgressCtaClickEvent(dataKey: String, stateColor: String, valueScore: Long) {
        SellerHomeTracking.sendClickProgressBarEvent(dataKey, stateColor, valueScore)
    }

    override fun sendTableImpressionEvent(
        model: TableWidgetUiModel,
        position: Int,
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
            element, slidePosition, maxSlidePosition, isSlideEmpty
        )
    }

    override fun sendTableHyperlinkClickEvent(
        dataKey: String, url: String, isEmpty: Boolean
    ) {
        SellerHomeTracking.sendTableClickHyperlinkEvent(dataKey, url, isEmpty)
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
        SellerHomeTracking.sendMultiLineGraphImpressionEvent(element)
    }

    override fun sendMultiLineGraphMetricClick(
        element: MultiLineGraphWidgetUiModel, metric: MultiLineMetricUiModel
    ) {
        SellerHomeTracking.sendMultiLineGraphMetricClick(element, metric)
    }

    override fun sendMultiLineGraphCtaClick(element: MultiLineGraphWidgetUiModel) {
        SellerHomeTracking.sendMultiLineGraphCtaClick(element)
    }

    override fun sendMultiLineGraphEmptyStateCtaClick(element: MultiLineGraphWidgetUiModel) {
        SellerHomeTracking.sendMultiLineGraphEmptyStateCtaClick(element)
    }

    override fun sendAnnouncementImpressionEvent(element: AnnouncementWidgetUiModel) {
        SellerHomeTracking.sendAnnouncementImpressionEvent(element)
    }

    override fun sendAnnouncementClickEvent(element: AnnouncementWidgetUiModel) {
        SellerHomeTracking.sendAnnouncementClickEvent(element)
    }

    override fun sendUnificationImpressionEvent(element: UnificationWidgetUiModel) {
        SellerHomeTracking.sendUnificationImpressionEvent(element.dataKey)
    }

    override fun sendUnificationEmptyStateCtaClickEvent(element: UnificationWidgetUiModel) {
        val selectedTab = element.data?.tabs?.firstOrNull { it.isSelected } ?: return
        SellerHomeTracking.sendUnificationEmptyStateCtaClickEvent(element.dataKey, selectedTab)
    }

    override fun sendUnificationTableItemClickEvent(
        element: UnificationWidgetUiModel,
        text: String,
        meta: TableRowsUiModel.Meta,
        isEmpty: Boolean
    ) {
        val selectedTab = element.data?.tabs?.firstOrNull { it.isSelected } ?: return
        SellerHomeTracking.sendUnificationTableItemClickEvent(
            element.dataKey, selectedTab, text, meta, isEmpty
        )
    }

    override fun showUnificationWidgetCoachMark(anchor: View) {
        val isEligibleCoachMark = !coachMarkPrefHelper.getUnificationCoachMarkStatus()
        if (isEligibleCoachMark) {
            if (this.unificationWidgetTitleView == null) {
                this.unificationWidgetTitleView = anchor

                context?.let {
                    unificationWidgetCoachMark = CoachMark2(it).apply {
                        simpleCloseIcon?.setOnClickListener {
                            coachMarkPrefHelper.saveUnificationMarkFlag()
                            unificationWidgetCoachMark = null
                            dismissCoachMark()
                        }
                    }
                    showUnificationCoachMarkWhenVisible()
                }
            }
        }
    }

    override fun sendUnificationSeeMoreClickEvent(dataKey: String, tab: UnificationTabUiModel) {
        SellerHomeTracking.sendUnificationSeeMoreClickEvent(dataKey, tab)
    }

    override fun sendUnificationTabImpressionEvent(element: UnificationWidgetUiModel) {
        val selectedTab = element.data?.tabs?.firstOrNull { it.isSelected } ?: return
        SellerHomeTracking.sendUnificationTabImpressionEvent(element.dataKey, selectedTab)
    }

    override fun showAnnouncementWidgetCoachMark(dataKey: String, view: View) {
        val isEligibleCoachMark = !coachMarkPrefHelper.getRebateCoachMarkMvpStatus()
        if (isEligibleCoachMark && dataKey == CoachMarkPrefHelper.REBATE_MVP_DATA_KEY) {
            if (this.rebateWidgetView == null) {
                this.rebateWidgetView = view

                rebateCoachMark = CoachMark2(requireContext()).apply {
                    simpleCloseIcon?.setOnClickListener {
                        coachMarkPrefHelper.saveRebateCoachMarkMvpFlag()
                        rebateWidgetView = null
                        dismissCoachMark()
                    }
                }
                showRebateCoachMark()
            }
        }
    }

    override fun setOnAnnouncementWidgetYesClicked(element: AnnouncementWidgetUiModel) {
        val param = SubmitWidgetDismissUiModel(
            action = SubmitWidgetDismissUiModel.Action.KEEP,
            dismissKey = String.format(ANNOUNCEMENT_DISMISSAL_KEY, element.dataKey),
            feedbackWidgetIDParent = element.id,
            dismissObjectIDs = listOf(element.id),
            shopId = userSession.shopId,
            isFeedbackPositive = true
        )
        sellerHomeViewModel.submitWidgetDismissal(param)
        SellerHomeTracking.sendClickWidgetAnnouncementDismissalPromptEvent(element.dataKey, true)
    }

    override fun setOnAnnouncementWidgetNoClicked(element: AnnouncementWidgetUiModel) {
        showFeedbackLoopOption(element)
        SellerHomeTracking.sendClickWidgetAnnouncementDismissalPromptEvent(element.dataKey, false)
    }

    override fun setOnWidgetCancelDismissal(element: BaseDismissibleWidgetUiModel<*>) {
        val param = SubmitWidgetDismissUiModel(
            action = SubmitWidgetDismissUiModel.Action.CANCEL,
            dismissToken = element.dismissToken,
            shopId = userSession.shopId
        )
        sellerHomeViewModel.submitWidgetDismissal(param)
        sendCancelDismissalTracker(element)
    }

    override fun showProgressBarCoachMark(dataKey: String, view: View) {
        val isEligibleCoachMark = !coachMarkPrefHelper.getRebateCoachMarkUltimateStatus()
        if (isEligibleCoachMark && dataKey == CoachMarkPrefHelper.REBATE_ULTIMATE_DATA_KEY) {
            if (this.rebateWidgetView == null) {
                this.rebateWidgetView = view

                rebateCoachMark = CoachMark2(requireContext()).apply {
                    simpleCloseIcon?.setOnClickListener {
                        coachMarkPrefHelper.saveRebateCoachMarkUltimateFlag()
                        rebateWidgetView = null
                        dismissCoachMark()
                    }
                }
                showRebateCoachMark()
            }
        }
    }

    override fun sendMilestoneMissionImpressionEvent(
        mission: BaseMilestoneMissionUiModel, position: Int
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

    override fun onReloadWidget(widget: BaseWidgetUiModel<*>) {
        isReloading = true
        shouldShowSuccessToaster = true
        refreshSimilarWidgets(widget)
    }

    override fun showCalendarWidgetDateFilter(element: CalendarWidgetUiModel) {
        if (!isAdded) {
            return
        }

        val perWeekSelectedDate = Date(
            DateTimeUtil.getTimeInMillis(
                element.filter.perWeek.startDate, DateTimeUtil.FORMAT_DD_MM_YYYY
            )
        )
        val perMontSelectedDate = Date(
            DateTimeUtil.getTimeInMillis(
                element.filter.perMonth.startDate, DateTimeUtil.FORMAT_DD_MM_YYYY
            )
        )

        val prevSelectedFilterType = element.filter.filterType
        val dateFilters = DateFilterUtil.FilterList.getCalendarPickerFilterList(
            requireContext(), perWeekSelectedDate, perMontSelectedDate, prevSelectedFilterType
        )

        CalendarWidgetDateFilterBottomSheet.newInstance(dateFilters)
            .setOnApplyChanges { dateFilter ->
                applyCalendarFilter(element, dateFilter)
            }.show(childFragmentManager)
    }

    override fun sendCalendarImpressionEvent(element: CalendarWidgetUiModel) {
        SellerHomeTracking.sendCalendarImpressionEvent(element)
    }

    override fun sendCalendarItemClickEvent(
        element: CalendarWidgetUiModel, event: CalendarEventUiModel
    ) {
        SellerHomeTracking.sendCalendarItemClickEvent(element, event)
    }

    override fun showUnificationTabBottomSheets(element: UnificationWidgetUiModel) {
        val tabs = element.data?.tabs.orEmpty()

        if (tabs.isEmpty()) return

        val bottomSheet = UnificationTabBottomSheet.createInstance()
        bottomSheet.setItems(tabs).setOnTabItemSelected { selectedTab ->
            val prevSelected = tabs.firstOrNull { it.isSelected }
            if (prevSelected?.dataKey != selectedTab.dataKey) {
                applyUnificationTabSelected(element, selectedTab)
            }
            bottomSheet.dismiss()
        }.show(childFragmentManager)

        val selectedTab = tabs.firstOrNull { it.isSelected } ?: return
        SellerHomeTracking.sendUnificationTabClickEvent(element.dataKey, selectedTab)
    }

    override fun showPostWidgetMoreOption(element: PostListWidgetUiModel) {
        val bottomSheet = PostMoreOptionBottomSheet.createInstance()
        bottomSheet.setOnRemoveArticleOptionClicked {
            bottomSheet.dismiss()
            switchPostWidgetCheckingMode(element)
        }
        bottomSheet.show(childFragmentManager)
    }

    override fun setOnPostWidgetRemoveItemClicked(element: PostListWidgetUiModel) {
        showFeedbackLoopOption(element)
        SellerHomeTracking.sendClickWidgetPostDeleteEvent(element.dataKey)
    }

    fun setNavigationNavigationView(navigationView: View?, otherMenuView: View?) {
        lifecycleScope.launchWhenResumed {
            this@SellerHomeFragment.navigationView = navigationView
            this@SellerHomeFragment.otherMenuView = otherMenuView
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

    fun setNotifCenterCounter(count: Int) {
        this.notifCenterCount = count
        showNotificationBadge()
    }

    private fun getWidgetLayout() {
        val deviceHeight = if (isNewLazyLoad) {
            deviceDisplayHeight
        } else {
            null
        }
        sellerHomeViewModel.getWidgetLayout(deviceHeight)
    }

    private fun showRebateCoachMark() {
        rebateCoachMark?.let { coachMark ->
            val coachMarkItems = getCoachMarkRebateProgram()
            if (coachMarkItems.isNotEmpty()) {
                coachMark.isDismissed = false
                coachMark.showCoachMark(coachMarkItems)
            }
        }
    }

    private fun showUnificationWidgetCoachMark() {
        unificationWidgetCoachMark?.let { coachMark ->
            val coachMarkItems = getCoachMarkUnification()
            if (coachMarkItems.isNotEmpty()) {
                coachMark.isDismissed = false
                coachMark.showCoachMark(coachMarkItems)
            }
        }
    }

    private fun applyUnificationTabSelected(
        element: UnificationWidgetUiModel, selectedTab: UnificationTabUiModel
    ) {
        val unificationWidgets = mutableListOf<BaseWidgetUiModel<*>>()
        val isTabAuthorized = !selectedTab.isUnauthorized
        val widgets = adapter.data.map { widget ->
            return@map if (widget.dataKey == element.dataKey && widget is UnificationWidgetUiModel) {
                val unificationWidget = widget.copyWidget().apply unificationWidget@{
                    val widgetData = widget.data
                    data = widgetData?.copy(tabs = widgetData.tabs.map tab@{
                        it.isSelected = it.dataKey == selectedTab.dataKey
                        return@tab it
                    })
                    impressHolder = ImpressHolder()
                    if (isTabAuthorized) {
                        showLoadingState = true
                    }
                }
                if (isTabAuthorized) {
                    unificationWidgets.add(unificationWidget)
                }
                unificationWidget
            } else {
                widget
            }
        }

        getWidgetsData(unificationWidgets)

        notifyWidgetWithSdkChecking {
            updateWidgets(widgets)
        }
    }

    private fun applyCalendarFilter(element: CalendarWidgetUiModel, dateFilter: DateFilterItem) {
        SellerHomeTracking.sendCalendarFilterClickEvent(element, dateFilter)
        val calendarWidgets = mutableListOf<BaseWidgetUiModel<*>>()
        val widgets = adapter.data.map {
            if (it.dataKey == element.dataKey && it is CalendarWidgetUiModel) {
                val startDate = dateFilter.startDate
                val endData = dateFilter.endDate
                if (startDate != null && endData != null) {
                    val calendarWidget = it.apply calendarWidget@{
                        data = null
                        filter = getAppliedDateFilter(filter, startDate, endData, dateFilter.type)
                        impressHolder = ImpressHolder()
                    }.copyWidget()
                    calendarWidgets.add(calendarWidget)
                    return@map calendarWidget
                } else {
                    return@map it
                }
            } else {
                return@map it
            }
        }

        getWidgetsData(calendarWidgets)

        notifyWidgetWithSdkChecking {
            updateWidgets(widgets)
        }
    }

    private fun getAppliedDateFilter(
        filter: CalendarFilterDataKeyUiModel, startDate: Date, endData: Date, filterType: Int
    ): CalendarFilterDataKeyUiModel {
        val startDateStr = DateTimeUtil.format(
            startDate.time, DateTimeUtil.FORMAT_DD_MM_YYYY
        )
        val endDateStr = DateTimeUtil.format(
            endData.time, DateTimeUtil.FORMAT_DD_MM_YYYY
        )
        return when (filterType) {
            DateFilterItem.TYPE_PER_MONTH -> {
                filter.copy(
                    perMonth = CalendarFilterDataKeyUiModel.DateRange(
                        startDate = startDateStr, endDate = endDateStr
                    ), filterType = filterType
                )
            }
            else -> {
                filter.copy(
                    perWeek = CalendarFilterDataKeyUiModel.DateRange(
                        startDate = startDateStr, endDate = endDateStr
                    ), filterType = filterType
                )
            }
        }
    }

    private fun initPltPerformanceMonitoring() {
        performanceMonitoringSellerHomePlt =
            (activity as? SellerHomeActivity)?.performanceMonitoringSellerHomeLayoutPlt
    }

    private fun hideTooltipIfExist() {
        val bottomSheet = childFragmentManager.findFragmentByTag(TAG_TOOLTIP)
        if (bottomSheet is BottomSheetUnify) bottomSheet.dismiss()
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
                handleRebateCoachMark()
                showUnificationCoachMarkWhenVisible()
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

        setupEmptyState()
        setRecyclerViewLayoutAnimation()
        setViewBackground()
    }

    private fun setupEmptyState() {
        emptyState?.run {
            setTitle(context?.getString(R.string.sah_failed_to_get_information).orEmpty())
            try {
                val imageDrawable = requireContext().getResDrawable(
                    com.tokopedia.globalerror.R.drawable.unify_globalerrors_500
                )
                if (imageDrawable != null) {
                    setImageDrawable(imageDrawable)
                } else {
                    setImageUrl(SellerHomeConst.Images.IMG_ERROR_500)
                }
            } catch (e: Exception) {
                setImageUrl(SellerHomeConst.Images.IMG_ERROR_500)
            }
            setPrimaryCTAText(
                context?.getString(com.tokopedia.globalerror.R.string.error500Action).orEmpty()
            )
            setPrimaryCTAClickListener {
                reloadPage()
            }
        }
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
        isReloading = true
        val isAdapterNotEmpty = adapter.data.isNotEmpty()
        setProgressBarVisibility(!isAdapterNotEmpty)
        swipeRefreshLayout.isRefreshing = isAdapterNotEmpty

        sahGlobalError.gone()
        emptyState?.gone()
        val deviceHeight = if (isNewLazyLoad) {
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
                    it.tableFilters.find { filter -> filter.isSelected }?.value.orEmpty()
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

    private fun getCalendarData(widgets: List<BaseWidgetUiModel<*>>) {
        startCustomMetric(SELLER_HOME_CALENDAR_TRACE)
        widgets.setLoading()
        val dataKeys = widgets.filterIsInstance<CalendarWidgetUiModel>().map {
            it.filter
        }
        sellerHomeViewModel.getCalendarWidgetData(dataKeys)
    }

    private fun getUnificationData(widgets: List<BaseWidgetUiModel<*>>) {
        startCustomMetric(SELLER_HOME_UNIFICATION_TRACE)
        widgets.setLoading()
        val mWidgets = widgets.filterIsInstance<UnificationWidgetUiModel>()
        sellerHomeViewModel.getUnificationWidgetData(mWidgets)
    }

    private fun setupShopSharing() {
        ImageHandler.loadImageWithTarget(
            context,
            shopShareData?.shopSnippetURL.orEmpty(),
            object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap, transition: Transition<in Bitmap>?
                ) {
                    val savedFile = ImageProcessingUtil.writeImageToTkpdPath(
                        resource, Bitmap.CompressFormat.PNG
                    )
                    if (savedFile != null) {
                        shopImageFilePath = savedFile.absolutePath
                        initShopShareBottomSheet()
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // no op
                }
            })
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
                        })
                }
            }

            override fun onCloseOptionClicked() {
                //no op
            }
        }

        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(shareListener)
            setMetaData(
                userSession.shopName, userSession.shopAvatar, ""
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
        sellerHomeViewModel.shopLocation.observe(viewLifecycleOwner) { result ->
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
        }

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
        sellerHomeViewModel.widgetLayout.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    isFirstLoad = false
                    stopLayoutCustomMetric(result.data.widgetList)
                    setOnSuccessGetLayout(result.data.widgetList)
                    startWidgetSse()
                    setupShopState(result.data.shopState)
                }
                is Fail -> {
                    stopCustomMetric(
                        SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_TRACE, true
                    )
                    setOnErrorGetLayout(result.throwable)
                }
            }
        }

        setProgressBarVisibility(true)
    }

    private fun stopLayoutCustomMetric(widgets: List<BaseWidgetUiModel<*>>) {
        val isFromCache = widgets.firstOrNull()?.isFromCache == true
        stopCustomMetric(
            SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_TRACE, isFromCache
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
                it.application, screenName, userSession.userId, performanceMonitoringSellerHomePlt
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setOnSuccessGetLayout(widgets: List<BaseWidgetUiModel<*>>) {
        isReloading = false
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
                                val newData = newWidget.data as? LastUpdatedDataInterface
                                newWidget.apply {
                                    data = oldWidget.data.also {
                                        if (it is LastUpdatedDataInterface && newData != null) {
                                            it.lastUpdated.lastUpdatedInMillis =
                                                newData.lastUpdated.lastUpdatedInMillis
                                            it.lastUpdated.needToUpdated =
                                                newData.lastUpdated.needToUpdated
                                        }
                                    }
                                    isLoaded = oldWidget.isLoaded
                                    isLoading = oldWidget.isLoading
                                }
                                val widgetData = newWidget.data
                                if (widgetData == null || !shouldRemoveWidget(
                                        newWidget, widgetData
                                    )
                                ) {
                                    newWidgets.add(newWidget)
                                }
                            } else {
                                newWidgets.add(newWidget)
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
        oldWidget: BaseWidgetUiModel<*>, newWidget: BaseWidgetUiModel<*>
    ): Boolean {
        return oldWidget.widgetType == newWidget.widgetType && oldWidget.title == newWidget.title && oldWidget.subtitle == newWidget.subtitle && oldWidget.appLink == newWidget.appLink && oldWidget.tooltip == newWidget.tooltip && oldWidget.ctaText == newWidget.ctaText && oldWidget.dataKey == newWidget.dataKey && oldWidget.isShowEmpty == newWidget.isShowEmpty && oldWidget.emptyState == newWidget.emptyState
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
        groupedWidgets[WidgetType.CALENDAR]?.run { getCalendarData(this) }
        groupedWidgets[WidgetType.UNIFICATION]?.run { getUnificationData(this) }
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
            if (isReloading) {
                throwable.showErrorToaster()
            }
            sahGlobalError.gone()
            emptyState?.gone()
            showWidgetRefreshButton()
        }
        swipeRefreshLayout.isRefreshing = false
        setProgressBarVisibility(false)

        SellerHomeErrorHandler.logException(
            throwable = throwable, message = ERROR_LAYOUT
        )
        SellerHomeErrorHandler.logExceptionToServer(
            errorTag = SellerHomeErrorHandler.SELLER_HOME_TAG,
            throwable = throwable,
            errorType = SellerHomeErrorHandler.ErrorType.ERROR_LAYOUT,
            deviceId = userSession.deviceId.orEmpty()
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun showWidgetRefreshButton() {
        val widgets = adapter.data.map { widget ->
            val copiedWidget = widget.copyWidget() as BaseWidgetUiModel<BaseDataUiModel>
            (copiedWidget.data as? LastUpdatedDataInterface)?.lastUpdated?.needToUpdated = true
            copiedWidget
        }
        notifyWidgetWithSdkChecking {
            updateWidgets(widgets)
        }
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
            this.root,
            message,
            TOAST_DURATION.toInt(),
            Toaster.TYPE_ERROR,
            getString(R.string.sah_reload)
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
        sellerHomeViewModel.homeTicker.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    stopCustomMetric(
                        SellerHomePerformanceMonitoringConstant.SELLER_HOME_TICKER_TRACE,
                        it.data.firstOrNull()?.isFromCache.orFalse()
                    )
                    onSuccessGetTickers(it.data)
                }
                is Fail -> {
                    stopCustomMetric(
                        SellerHomePerformanceMonitoringConstant.SELLER_HOME_TICKER_TRACE, false
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
        }
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

    private fun observeWidgetDismissalStatus() {
        viewLifecycleOwner.observe(sellerHomeViewModel.submitWidgetDismissal) {
            when (it) {
                is Success -> setSubmitDismissalSuccess(it.data)
                is Fail -> it.throwable.showErrorToaster()
            }
        }
    }

    private fun observeShopStateInfo() {
        viewLifecycleOwner.observe(sellerHomeViewModel.shopStateInfo) {
            if (it is Success) {
                val info = it.data
                this.isNewSellerState = info.isNewSellerState
                setViewBackgroundNewSeller()
                if (info.subtitle.isBlank()) return@observe

                if (info.subType == ShopStateInfoUiModel.SubType.TOAST) {
                    showShopStateToaster(info)
                } else if (info.subType == ShopStateInfoUiModel.SubType.POPUP) {
                    showShopStatePopup(info)
                }
            } else {
                this.isNewSellerState = false
            }
        }
    }

    private fun setViewBackgroundNewSeller() {
        binding?.run {
            if (isNewSellerState) {
                viewBgShopStatus.visible()
                viewBgShopStatus.layoutParams.height = LayoutParams.MATCH_PARENT
                viewBgShopStatus.setImageResource(R.drawable.sah_shop_state_bg_new_seller)
                viewBgShopStatus.requestLayout()
                imgSahNewSellerLeft.loadImage(SellerHomeConst.Images.IMG_NEW_SELLER_LEFT) {
                    useCache(true)
                    listener(onSuccess = { _, _ ->
                        imgSahNewSellerLeft.visible()
                    })
                }

                imgSahNewSellerRight.loadImage(SellerHomeConst.Images.IMG_NEW_SELLER_RIGHT) {
                    useCache(true)
                    listener(onSuccess = { _, _ ->
                        imgSahNewSellerRight.visible()
                    })
                }
            } else {
                imgSahNewSellerLeft.gone()
                imgSahNewSellerRight.gone()
                setViewBackground()
            }
            setSectionWidgetTextColor()
        }
    }

    private fun setSectionWidgetTextColor() {
        recyclerView?.post {
            val widgets = adapter.data.map {
                if (it is SectionWidgetUiModel) {
                    val titleTextColor: Int
                    val subTitleTextColor: Int
                    if (isNewSellerState) {
                        titleTextColor = com.tokopedia.unifyprinciples.R.color.Unify_NN0
                        subTitleTextColor = com.tokopedia.unifyprinciples.R.color.Unify_NN0
                    } else {
                        titleTextColor = com.tokopedia.unifyprinciples.R.color.Unify_N700_96
                        subTitleTextColor = com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                    }
                    return@map it.copy(
                        titleTextColorId = titleTextColor, subTitleTextColorId = subTitleTextColor
                    )
                }
                return@map it
            }

            notifyWidgetWithSdkChecking {
                updateWidgets(widgets)
            }
        }
    }

    private fun setViewBackground() = binding?.run {
        val isOfficialStore = userSession.isShopOfficialStore
        val isPowerMerchant = userSession.isPowerMerchantIdle || userSession.isGoldMerchant
        when {
            isOfficialStore -> {
                showRegularHomeBackground(R.drawable.sah_shop_state_bg_official_store)
            }
            isPowerMerchant -> {
                showRegularHomeBackground(R.drawable.sah_shop_state_bg_power_merchant)
            }
            else -> {
                viewBgShopStatus.gone()
            }
        }
    }

    private fun showRegularHomeBackground(backgroundResource: Int) {
        binding?.run {
            val height = requireActivity().resources.getDimensionPixelSize(R.dimen.sah_dimen_280dp)
            viewBgShopStatus.layoutParams.height = height
            viewBgShopStatus.visible()
            viewBgShopStatus.setImageResource(backgroundResource)
            viewBgShopStatus.requestLayout()
        }
    }

    private inline fun <reified D : BaseDataUiModel> observeWidgetData(
        liveData: LiveData<Result<List<D>>>, type: String
    ) {
        liveData.observe(viewLifecycleOwner) { result ->
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
        }
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
        type: String, isFromCache: Boolean
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
                SELLER_HOME_MULTI_LINE_GRAPH_TRACE, isFromCache
            )
            WidgetType.ANNOUNCEMENT -> stopCustomMetric(SELLER_HOME_ANNOUNCEMENT_TRACE, isFromCache)
            WidgetType.RECOMMENDATION -> stopCustomMetric(
                SELLER_HOME_RECOMMENDATION_TRACE, isFromCache
            )
            WidgetType.MILESTONE -> stopCustomMetric(
                SELLER_HOME_MILESTONE_TRACE, isFromCache
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
    private fun <D : BaseDataUiModel> List<D>.setOnSuccessWidgetState(
        widgetType: String
    ) {
        isReloading = false
        val isFromCache = firstOrNull()?.isFromCache == true
        stopSellerHomeFragmentWidgetPerformanceMonitoring(widgetType, isFromCache)
        stopPltMonitoringIfNotCompleted(isFromCache)

        mergedWidgetAndData(this, widgetType)

        binding?.root?.addOneTimeGlobalLayoutListener {
            recyclerView?.post {
                checkLoadingWidgets()
                requestVisibleWidgetsData()
            }
        }
        if (!isFromCache) {
            showWidgetSuccessToaster()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> mergedWidgetAndData(
        widgetDataList: List<D>, widgetType: String
    ) {
        val widgetList: MutableList<BaseWidgetUiModel<*>> = if (widgetType == WidgetType.CARD) {
            getWidgetListForSse(widgetDataList).toMutableList()
        } else {
            adapter.data.toMutableList()
        }

        widgetDataList.forEach { widgetData ->
            widgetList.indexOfFirst {
                it.dataKey == widgetData.dataKey && it.widgetType == widgetType
            }.takeIf { it > RecyclerView.NO_POSITION }?.let { index ->
                val widget = widgetList.getOrNull(index)
                if (widget is W) {
                    if (shouldRemoveWidget(widget, widgetData)) {
                        widgetList.removeAt(index)
                        removeEmptySections(widgetList, index)
                    } else {
                        val copiedWidget = widget.copyWidget()
                        copiedWidget.data = widgetData
                        copiedWidget.isLoading = widget.data?.isFromCache.orFalse()
                        copiedWidget.showLoadingState = false

                        handleShopShareMilestoneWidget(copiedWidget)

                        widgetList[index] = copiedWidget
                    }
                }
            }
        }

        notifyWidgetWithSdkChecking {
            updateWidgets(widgetList as List<BaseWidgetUiModel<BaseDataUiModel>>)
        }
    }

    /**
     * SSE special case for Card Widgets
     *
     * We need to handle case for `zero to non zero` case, means we need to show up again
     * the card widget if card widget data from SSE is more then 0.
     * So this method to handle that and will return list of widgets
     * */
    private fun <D : BaseDataUiModel> getWidgetListForSse(widgetDataList: List<D>): List<BaseWidgetUiModel<*>> {
        val widgetMap: Map<String, BaseWidgetUiModel<*>> = adapter.data.associateBy { it.dataKey }
        val widgetList = mutableListOf<BaseWidgetUiModel<*>>()
        val widgetDataMap = widgetDataList.associateBy { it.dataKey }

        sellerHomeViewModel.rawWidgetList.forEach { w ->
            val widget = widgetMap[w.dataKey]
            if (w is CardWidgetUiModel) {
                val data = widgetDataMap[w.dataKey]
                if (data is CardDataUiModel) {
                    widgetList.add(w)
                } else {
                    if (widget != null) {
                        widgetList.add(widget)
                    }
                }
            } else {
                if (widget != null) {
                    widgetList.add(widget)
                }
            }
        }

        return widgetList.toList()
    }

    private fun showWidgetSuccessToaster() {
        if (shouldShowSuccessToaster) {
            binding?.let {
                val message = getString(R.string.sah_widget_success_toaster)
                Toaster.build(it.root, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
            }
        }
        shouldShowSuccessToaster = false
    }

    private fun <D : BaseDataUiModel> handleShopShareMilestoneWidget(widget: BaseWidgetUiModel<D>) {
        if (widget is MilestoneWidgetUiModel) {
            val shareMission = widget.data?.milestoneMissions?.firstOrNull {
                return@firstOrNull it.missionButton.urlType == BaseMilestoneMissionUiModel.UrlType.SHARE
            }
            val isShareMissionAvailable = !shareMission?.missionCompletionStatus.orFalse()
            if (isShareMissionAvailable) {
                sellerHomeViewModel.getShopInfoById()
            }
        }
    }

    private fun shouldRemoveWidget(
        widget: BaseWidgetUiModel<*>, widgetData: BaseDataUiModel
    ): Boolean {
        return !widget.isFromCache && !widgetData.isFromCache && (!widgetData.showWidget || (!widget.isShowEmpty && widgetData.isWidgetEmpty()))
    }

    private fun removeEmptySections(
        newWidgetList: MutableList<BaseWidgetUiModel<*>>, removedWidgetIndex: Int
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
        val errorMessage = this.message.orEmpty()
        var isAnyWidgetNeedToUpdated = false
        val newWidgetList = adapter.data.map { widget ->
            val isSameWidgetType = widget.widgetType == widgetType
            val shouldShowErrorState = widget.data == null && widget.isLoaded && isSameWidgetType
            val shouldShowExistingData = widget is W && widget.data != null && isSameWidgetType
            val newWidget = if (widget is W && shouldShowErrorState) {
                widget.copyWidget().apply {
                    data = D::class.java.newInstance().apply {
                        error = errorMessage
                    }
                    isLoading = widget.data?.isFromCache ?: false
                }
            } else if (shouldShowExistingData) {
                isAnyWidgetNeedToUpdated = true
                copyExistingWidget(widget)
            } else {
                widget
            }

            return@map newWidget.apply {
                showLoadingState = false
            }
        }

        logWidgetException(widgetType, this)

        notifyWidgetWithSdkChecking {
            updateWidgets(newWidgetList as List<BaseWidgetUiModel<BaseDataUiModel>>)
        }

        if (isAnyWidgetNeedToUpdated && !isReloading) {
            showWidgetLastUpdatedWarningToaster()
        } else {
            showErrorToaster()
        }

        view?.addOneTimeGlobalLayoutListener {
            requestVisibleWidgetsData()
            checkLoadingWidgets()
        }
        isReloading = false
    }

    private fun showWidgetLastUpdatedWarningToaster() {
        binding?.run {
            if (isErrorToastShown) return
            isErrorToastShown = true

            val message = getString(R.string.sah_some_widgets_need_to_be_refreshed)
            Toaster.build(
                this.root,
                message,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_NORMAL,
                getString(R.string.sah_reload)
            ) {
                reloadNotUpdatedWidgets()
            }.show()

            Handler(Looper.getMainLooper()).postDelayed({
                isErrorToastShown = false
            }, SHORT_TOAST_DURATION)
        }
    }

    private fun reloadNotUpdatedWidgets() {
        val widgets = adapter.data.filter {
            //filter all widgets that the data still from cache
            it.data?.isFromCache.orFalse()
        }
        getWidgetsData(widgets)
    }

    private fun copyExistingWidget(widget: BaseWidgetUiModel<*>): BaseWidgetUiModel<*> {
        val tempWidget = widget.copyWidget()
        widget.data = null
        val widgetData = tempWidget.data
        if (widgetData is LastUpdatedDataInterface) {
            widgetData.lastUpdated.needToUpdated = true
        }
        return tempWidget
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
            throwable = throwable, message = "$ERROR_WIDGET $widgetType"
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
                            it.id, it.type
                        )
                    }
                }
            })

            // Add impression listener on first page of ticker
            addSellerHomeImpressionListener(
                tickerImpressHolders.firstOrNull(), tickers.firstOrNull()
            )

            // Add impression listener if ticker view pager swiped to another page
            onTickerPageChangeListener = { pageIndex ->
                if (pageIndex > TICKER_FIRST_INDEX) {
                    addSellerHomeImpressionListener(
                        tickerImpressHolders.getOrNull(pageIndex), tickers.getOrNull(pageIndex)
                    )
                }
            }
        }
    }

    private fun Ticker.addSellerHomeImpressionListener(
        impressHolder: ImpressHolder?, ticker: TickerItemUiModel?
    ) {
        impressHolder?.let { holder ->
            ticker?.let { ticker ->
                addOnImpressionListener(holder) {
                    SellerHomeTracking.sendHomeTickerImpressionEvent(
                        ticker.id, ticker.type
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

    @Suppress("UNCHECKED_CAST")
    private fun resetWidgetImpressionHolder() {
        val newWidgetList = adapter.data.map { widget ->
            val isInvoked = widget.impressHolder.isInvoke
            when (widget) {
                !is SectionWidgetUiModel, !is TickerWidgetUiModel -> {
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

    @SuppressLint("NotifyDataSetChanged")
    @Suppress("UNCHECKED_CAST")
    private fun updateWidgets(newWidgets: List<BaseWidgetUiModel<*>>) {
        try {
            val diffUtilCallback = SellerHomeDiffUtilCallback(
                adapter.data as List<BaseWidgetUiModel<BaseDataUiModel>>,
                newWidgets as List<BaseWidgetUiModel<BaseDataUiModel>>
            )
            val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallback)
            adapter.data.clear()
            adapter.data.addAll(newWidgets)
            diffUtilResult.dispatchUpdatesTo(adapter)
        } catch (e: Exception) {
            try {
                recyclerView?.post {
                    adapter.notifyDataSetChanged()
                }
            } catch (notifyException: Exception) {
                SellerHomeErrorHandler.logException(
                    notifyException, SellerHomeErrorHandler.UPDATE_WIDGET_ERROR
                )
            }
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
        return !isLoaded && this !is SectionWidgetUiModel && this !is TickerWidgetUiModel && this !is DescriptionWidgetUiModel
    }

    private fun handleRebateCoachMark() {
        if (rebateWidgetView == null) return

        getSellerHomeLayoutManager()?.let { layoutManager ->
            val rebateWidget = adapter.data.indexOfFirst {
                val isRebateMvp =
                    it.dataKey == CoachMarkPrefHelper.REBATE_MVP_DATA_KEY && !coachMarkPrefHelper.getRebateCoachMarkMvpStatus()
                val isRebateUltimate =
                    !coachMarkPrefHelper.getRebateCoachMarkUltimateStatus() && it.dataKey == CoachMarkPrefHelper.REBATE_ULTIMATE_DATA_KEY
                return@indexOfFirst isRebateMvp || isRebateUltimate
            }
            val firstVisibleIndex = layoutManager.findFirstVisibleItemPosition()
            val lastVisibleIndex = layoutManager.findLastCompletelyVisibleItemPosition()
            if (rebateWidget != RecyclerView.NO_POSITION && rebateWidget in firstVisibleIndex..lastVisibleIndex) {
                showRebateCoachMark()
            } else {
                rebateCoachMark?.dismissCoachMark()
            }
        }
    }

    private fun showUnificationCoachMarkWhenVisible() {
        if (unificationWidgetTitleView == null) return

        getSellerHomeLayoutManager()?.let { layoutManager ->
            val unificationWidget = adapter.data.indexOfFirst { it is UnificationWidgetUiModel }
            val firstVisibleIndex = layoutManager.findFirstCompletelyVisibleItemPosition()
            val lastVisibleIndex = layoutManager.findLastVisibleItemPosition()
            if (unificationWidget != RecyclerView.NO_POSITION && unificationWidget in firstVisibleIndex..lastVisibleIndex) {
                showUnificationWidgetCoachMark()
            } else {
                unificationWidgetCoachMark?.dismissCoachMark()
            }
        }
    }

    private fun getSellerHomeLayoutManager(): SellerHomeLayoutManager? {
        return recyclerView?.layoutManager as? SellerHomeLayoutManager
    }

    private fun getCoachMarkRebateProgram(): ArrayList<CoachMark2Item> {
        val coachMarkItems = arrayListOf<CoachMark2Item>()
        rebateWidgetView?.let { view ->
            coachMarkItems.add(
                CoachMark2Item(
                    anchorView = view,
                    title = view.context.getString(R.string.sah_rebate_coach_mark_title),
                    description = view.context.getString(R.string.sah_rebate_coach_mark_description),
                    position = CoachMark2.POSITION_BOTTOM
                )
            )
        }
        return coachMarkItems
    }

    private fun getCoachMarkUnification(): ArrayList<CoachMark2Item> {
        val coachMarkItems = arrayListOf<CoachMark2Item>()
        unificationWidgetTitleView?.let { view ->
            coachMarkItems.add(
                CoachMark2Item(
                    anchorView = view,
                    title = view.context.getString(R.string.sah_unification_coach_mark_title),
                    description = view.context.getString(R.string.sah_unification_coach_mark_description),
                    position = CoachMark2.POSITION_BOTTOM
                )
            )
        }
        return coachMarkItems
    }

    private fun handleMilestoneWidgetFinishedMission(requestCode: Int) {
        view?.post {
            if (requestCode == REQ_CODE_MILESTONE_WIDGET) {
                val milestoneWidgets = adapter.data.filterIsInstance<MilestoneWidgetUiModel>()
                getMilestoneData(milestoneWidgets)
            }
        }
    }

    private fun refreshSimilarWidgets(widget: BaseWidgetUiModel<*>) {
        val similarWidget = mutableListOf<BaseWidgetUiModel<*>>()
        val widgets = adapter.data.map {
            val isTheSameWidget = it.widgetType == widget.widgetType
            val tempWidget = if (isTheSameWidget) {
                similarWidget.add(it)
                it.copyWidget().apply {
                    showLoadingState = true
                }
            } else {
                it
            }
            return@map tempWidget
        }
        updateWidgets(widgets)
        getWidgetsData(similarWidget)
    }

    private fun switchPostWidgetCheckingMode(element: PostListWidgetUiModel) {
        val widgets = adapter.data.map {
            val isTheSameWidget = it.dataKey == element.dataKey && it is PostListWidgetUiModel
            if (isTheSameWidget) {
                val widget = it.copyWidget() as? PostListWidgetUiModel
                widget?.let {
                    widget.isCheckingMode = !widget.isCheckingMode
                    return@map widget
                }
                return@map it
            } else {
                return@map it
            }
        }

        notifyWidgetWithSdkChecking {
            updateWidgets(widgets)
        }
    }

    private fun showFeedbackLoopOption(element: BaseWidgetUiModel<*>) {
        val bottomSheet = FeedbackLoopOptionsBottomSheet.createInstance()
        bottomSheet.setOnSubmitClickedListener {
            submitFeedback(element, it)
            bottomSheet.dismiss()
        }
        bottomSheet.show(childFragmentManager)
    }

    private fun submitFeedback(
        element: BaseWidgetUiModel<*>, reasons: List<FeedbackLoopOptionUiModel>
    ) {
        val dismissObjectIDs: List<String>
        val dismissSign: String
        val dismissKey = when (element) {
            is AnnouncementWidgetUiModel -> {
                dismissObjectIDs = listOf(element.id)
                dismissSign = element.data?.widgetDataSign.orEmpty()
                SellerHomeTracking.sendClickWidgetAnnouncementSubmitDismissalEvent(element.dataKey)

                String.format(ANNOUNCEMENT_DISMISSAL_KEY, element.dataKey)
            }
            is PostListWidgetUiModel -> {
                dismissObjectIDs =
                    element.data?.postPagers?.flatMap { it.postList }?.filter { it.isChecked }
                        ?.map { it.postItemId }.orEmpty()
                dismissSign = element.data?.widgetDataSign.orEmpty()
                val numberPosts = dismissObjectIDs.size
                SellerHomeTracking.sendClickWidgetPostSubmitDismissalEvent(
                    element.dataKey, numberPosts
                )

                String.format(POST_LIST_DISMISSAL_KEY, element.dataKey)
            }
            else -> {
                dismissObjectIDs = emptyList()
                dismissSign = String.EMPTY
                String.EMPTY
            }
        }

        val param = SubmitWidgetDismissUiModel(
            action = SubmitWidgetDismissUiModel.Action.DISMISS,
            dismissKey = dismissKey,
            feedbackReason1 = reasons.getOrNull(FEEDBACK_OPTION_1)?.isSelected.orFalse(),
            feedbackReason2 = reasons.getOrNull(FEEDBACK_OPTION_2)?.isSelected.orFalse(),
            feedbackReason3 = reasons.getOrNull(FEEDBACK_OPTION_3)?.isSelected.orFalse(),
            feedbackReasonOther = (reasons.getOrNull(FEEDBACK_OPTION_4) as? FeedbackLoopOptionUiModel.Other)?.value.orEmpty(),
            feedbackWidgetIDParent = element.id,
            dismissObjectIDs = dismissObjectIDs,
            dismissSign = dismissSign,
            shopId = userSession.shopId
        )
        sellerHomeViewModel.submitWidgetDismissal(param)
    }

    private fun setSubmitDismissalSuccess(result: WidgetDismissalResultUiModel) {
        var shouldUpdateWidget = false
        val widgets: List<BaseWidgetUiModel<*>> = adapter.data.map {
            return@map when {
                it.id == result.widgetId && it is AnnouncementWidgetUiModel -> {
                    shouldUpdateWidget = true
                    getDismissalAnnouncementWidget(it, result)
                }
                it.id == result.widgetId && it is PostListWidgetUiModel -> {
                    shouldUpdateWidget = true
                    getDismissalPostListWidget(it, result)
                }
                else -> it
            }
        }

        if (shouldUpdateWidget) {
            notifyWidgetWithSdkChecking {
                updateWidgets(widgets)
            }
        }
    }

    private fun getDismissalPostListWidget(
        widget: PostListWidgetUiModel, result: WidgetDismissalResultUiModel
    ): BaseWidgetUiModel<*> {
        val isDismissAction = result.action == SubmitWidgetDismissUiModel.Action.DISMISS

        val prevPostList = widget.data?.postPagers?.flatMap { it.postList }.orEmpty()
        val postDismissalItem = PostItemUiModel.PostTimerDismissalUiModel(
            totalDeletedItems = prevPostList.count { it.isChecked },
            runningTimeInMillis = Int.ZERO.toLong()
        )
        val tempPostList = listOf(postDismissalItem).plus(prevPostList)
        val maxItemPerPage = if (widget.maxDisplay == Int.ZERO) {
            PostMapper.MAX_ITEM_PER_PAGE
        } else {
            widget.maxDisplay
        }

        val postPagers = tempPostList.chunked(maxItemPerPage).map {
            return@map PostListPagerUiModel(it)
        }
        return widget.copy(
            dismissToken = result.dismissToken,
            shouldShowDismissalTimer = isDismissAction,
            data = widget.data?.copy(
                postPagers = postPagers
            ),
            isCheckingMode = false
        )
    }

    private fun getDismissalAnnouncementWidget(
        widget: AnnouncementWidgetUiModel, result: WidgetDismissalResultUiModel
    ): BaseWidgetUiModel<*> {
        val isDismissAction = result.action == SubmitWidgetDismissUiModel.Action.DISMISS
        val isKeepAction = result.action == SubmitWidgetDismissUiModel.Action.KEEP
        return widget.copy(
            dismissToken = result.dismissToken,
            shouldShowDismissalTimer = isDismissAction,
            isDismissible = isKeepAction
        ).copyWidget()
    }

    private fun sendCancelDismissalTracker(element: BaseDismissibleWidgetUiModel<*>) {
        when (element) {
            is AnnouncementWidgetUiModel -> {
                SellerHomeTracking.sendClickWidgetAnnouncementCancelDismissalEvent(element.dataKey)
            }
            is PostListWidgetUiModel -> {
                val numberOfPosts =
                    element.data?.postPagers?.flatMap { it.postList }?.count { it.isChecked }
                        .orZero()
                SellerHomeTracking.sendClickWidgetPostCancelDismissalEvent(
                    element.dataKey, numberOfPosts
                )
            }
        }
    }

    private fun startWidgetSse() {
        lifecycleScope.launchWhenResumed {
            withContext(Dispatchers.IO) {
                val widgets = sellerHomeViewModel.rawWidgetList
                val isAnyRealtimeWidget = widgets.any { it.useRealtime }
                if (isAnyRealtimeWidget) {
                    val realTimeDataKeys = widgets.filter { it.useRealtime }.map { it.dataKey }
                    sellerHomeViewModel.startSse(realTimeDataKeys)
                }
            }
        }
    }

    private fun stopWidgetSse() {
        sellerHomeViewModel.stopSSE()
    }

    private fun showShopStatePopup(info: ShopStateInfoUiModel) {
        context?.let {
            newSellerJourneyHelper.showFirstOrderDialog(it, info, onDismiss = {
                fetchNewLayoutAdjustmentToaster()
            })
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                delay(TOAST_DURATION)
                sendShopStateDismissal(info.dataSign)
            }
        }
    }

    private fun fetchNewLayoutAdjustmentToaster() {
        sellerHomeViewModel.getShopStateInfo()
    }

    private fun showShopStateToaster(info: ShopStateInfoUiModel) {
        binding?.run {
            Toaster.build(
                root, info.subtitle, TOAST_DURATION.toInt(), Toaster.TYPE_NORMAL, info.button.name
            ) {
                if (info.button.appLink.isNotBlank()) {
                    RouteManager.route(root.context, info.button.appLink)
                }
            }.show()

            sendShopStateDismissal(info.dataSign)
        }
    }

    private fun sendShopStateDismissal(dataSign: String) {
        val param = SubmitWidgetDismissUiModel(
            action = SubmitWidgetDismissUiModel.Action.DISMISS,
            dismissKey = NewSellerDialog.DISMISSAL_KEY,
            dismissSign = dataSign,
            dismissObjectIDs = listOf(NewSellerJourneyHelper.WIDGET_DISMISSAL_ID),
            shopId = userSession.shopId,
            isFeedbackPositive = true,
            feedbackWidgetIDParent = NewSellerJourneyHelper.WIDGET_DISMISSAL_ID
        )
        sellerHomeViewModel.submitWidgetDismissal(param)
    }

    private fun setupShopState(shopState: ShopStateUiModel) {
        val shouldGetShopStateInfo =
            shopState == ShopStateUiModel.AddedProduct || shopState == ShopStateUiModel.ViewedProduct || shopState == ShopStateUiModel.HasOrder
        if (shopState == ShopStateUiModel.NewRegisteredShop) {
            showNewSellerDialog()
            isNewSellerState = true
            setViewBackgroundNewSeller()
        } else if (shouldGetShopStateInfo) {
            getShopStateInfoIfEligible()
        }
    }

    private fun getShopStateInfoIfEligible() {
        if (newSellerJourneyHelper.shouldFetchShopInfo()) {
            sellerHomeViewModel.getShopStateInfo()
        } else {
            isNewSellerState = false
            setViewBackgroundNewSeller()
        }
    }

    private fun showNewSellerDialog() {
        recyclerView?.post {
            newSellerJourneyHelper.showNewSellerDialog(
                requireActivity(),
                sectionWidgetAnchor = getSectionView(),
                notificationAnchor = getNotificationView(),
                navigationAnchor = navigationView,
                otherMenuAnchor = otherMenuView
            )
        }
    }

    private fun getSectionView(): View? {
        val firstSectionWidgetIndex = adapter.data.indexOfFirst { it is SectionWidgetUiModel }
        if (firstSectionWidgetIndex != RecyclerView.NO_POSITION) {
            return recyclerView?.layoutManager?.findViewByPosition(firstSectionWidgetIndex)
        }
        return null
    }

    private fun getNotificationView(): View? {
        return menu?.findItem(NOTIFICATION_MENU_ID)?.actionView
    }

    @SuppressLint("DeprecatedMethod")
    private fun showSellerHomeToaster() {
        binding?.run {
            root.post {
                val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arguments?.getParcelable(
                        KEY_SELLER_HOME_DATA, SellerHomeDataUiModel::class.java
                    )
                } else {
                    arguments?.getParcelable(KEY_SELLER_HOME_DATA)
                }
                val message = data?.toasterMessage
                if (!message.isNullOrBlank()) {
                    Toaster.build(
                        this.root,
                        message,
                        Toaster.LENGTH_LONG,
                        Toaster.TYPE_NORMAL,
                        data.toasterCta
                    ).show()
                }
            }
        }
    }

    interface Listener {
        fun getShopInfo()
    }
}
