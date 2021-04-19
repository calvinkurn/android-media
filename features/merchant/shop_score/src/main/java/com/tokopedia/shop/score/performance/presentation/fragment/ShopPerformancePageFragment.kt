package com.tokopedia.shop.score.performance.presentation.fragment

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreCoachMarkPrefs
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.analytics.ShopPerformanceTracking
import com.tokopedia.shop.score.performance.di.component.ShopPerformanceComponent
import com.tokopedia.shop.score.performance.domain.model.ShopScoreWrapperResponse
import com.tokopedia.shop.score.performance.presentation.activity.ShopPerformanceYoutubeActivity
import com.tokopedia.shop.score.performance.presentation.adapter.*
import com.tokopedia.shop.score.performance.presentation.bottomsheet.BottomSheetNextUpdatePMSection
import com.tokopedia.shop.score.performance.presentation.bottomsheet.BottomSheetPerformanceDetail
import com.tokopedia.shop.score.performance.presentation.bottomsheet.BottomSheetShopTooltipLevel
import com.tokopedia.shop.score.performance.presentation.bottomsheet.BottomSheetShopTooltipScore
import com.tokopedia.shop.score.performance.presentation.model.*
import com.tokopedia.shop.score.performance.presentation.viewmodel.ShopPerformanceViewModel
import com.tokopedia.shop.score.performance.presentation.widget.PenaltyDotBadge
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_shop_performance.*
import javax.inject.Inject


class ShopPerformancePageFragment : BaseDaggerFragment(),
        ShopPerformanceListener, ItemShopPerformanceListener,
        ItemPotentialRegularMerchantListener, ItemRecommendationFeatureListener,
        ItemStatusPowerMerchantListener, ItemTimerNewSellerListener, ItemHeaderShopPerformanceListener, SectionFaqListener,
        GlobalErrorListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject lateinit var shopPerformanceTracking: ShopPerformanceTracking

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopPerformanceViewModel::class.java)
    }

    private val shopPerformanceAdapterTypeFactory by lazy {
        ShopPerformanceAdapterTypeFactory(this, this,
                this, this,
                this, this, this,
                this, this)
    }

    private val shopPerformanceAdapter by lazy { ShopPerformanceAdapter(shopPerformanceAdapterTypeFactory) }
    private var shopScoreWrapperResponse: ShopScoreWrapperResponse? = null

    private val coachMarkItemList = ArrayList<CoachMark2Item>()
    private val coachMark by lazy { context?.let { CoachMark2(it) } }
    private val shopScoreCoachMarkPrefs by lazy { ShopScoreCoachMarkPrefs(requireContext()) }

    private val penaltyDotBadge: PenaltyDotBadge? by lazy {
        context?.let { PenaltyDotBadge(it) }
    }

    private var counterPenalty = 0
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_performance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        inflater.inflate(R.menu.menu_shop_score, menu)
        this.menu = menu
        showPenaltyBadge()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            INFO_MENU_ID -> {
                RouteManager.route(context, ShopScoreConstant.SHOP_INFO_URL)
                shopPerformanceTracking.clickMenuCompleteInfo()
            }
            PENALTY_WARNING_MENU_ID -> {
                goToPenaltyPage()
                shopPerformanceTracking.clickMenuPenalty()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ShopPerformanceComponent::class.java).inject(this)
    }

    override fun onTooltipLevelClicked(level: Int) {
        val shopLevelData = shopScoreWrapperResponse?.shopScoreTooltipResponse?.result
        val bottomSheetShopTooltipLevel = BottomSheetShopTooltipLevel.createInstance(
                shopLevel = level,
                shopIncome = shopLevelData?.niv?.toString().orEmpty(),
                productSold = shopLevelData?.itemSold.toString(),
                period = shopLevelData?.period.toString(),
                nextUpdate = shopLevelData?.nextUpdate.toString())
        bottomSheetShopTooltipLevel.show(childFragmentManager)
    }

    override fun onTooltipScoreClicked() {
        val bottomSheetShopTooltipScore = BottomSheetShopTooltipScore()
        bottomSheetShopTooltipScore.show(childFragmentManager)
    }

    override fun onTickerClickedToPenaltyPage() {
        goToPenaltyPage()
        shopPerformanceTracking.clickHereTickerPenalty()
    }

    override fun onTickerImpressionToPenaltyPage() {
        shopPerformanceTracking.impressTickerPenaltyShopScore()
    }

    /**
     * ItemShopPerformanceListener
     */
    override fun onItemClickedToDetailBottomSheet(titlePerformanceDetail: String, identifierPerformanceDetail: String) {
        val bottomSheetDetail = BottomSheetPerformanceDetail.createInstance(titlePerformanceDetail, identifierPerformanceDetail)
        bottomSheetDetail.show(childFragmentManager)
    }

    /**
     * ItemShopPerformanceListener
     **/
    override fun onViewItemDetailPerformanceListener(view: View) {
        if (!shopScoreCoachMarkPrefs.getHasShownItemPerformanceDetail()) {
            coachMarkItemList.add(CoachMark2Item(
                    view.findViewById(R.id.cardItemDetailShopPerformance),
                    getString(R.string.title_coachmark_shop_score_2),
                    getString(R.string.desc_coachmark_shop_score_2),
                    position = CoachMark2.POSITION_TOP
            ))
            shopScoreCoachMarkPrefs.setHasShownItemPerformanceDetail(true)
        }
        val isShowCoachMarkTwoItem = shopPerformanceAdapter.list.find { it is ItemStatusPMUiModel }
        val itemStatusRMUiModel = shopPerformanceAdapter.list.find { it is ItemStatusRMUiModel }
        if (isShowCoachMarkTwoItem == null && itemStatusRMUiModel == null) {
            showCoachMark()
        }
    }

    /**
     * ItemStatusPowerMerchantListener
     */
    override fun onItemClickedNextUpdatePM() {
        val bottomSheetNextUpdate = BottomSheetNextUpdatePMSection.newInstance()
        bottomSheetNextUpdate.show(childFragmentManager)
    }

    /**
     * ItemStatusPowerMerchantListener
     */
    override fun onItemClickedGoToPMActivation() {
        goToPowerMerchantSubscribe()
        shopPerformanceTracking.clickPowerMerchantSection()
    }

    /**
     * ItemStatusPowerMerchantListener
     **/
    override fun onViewItemPowerMerchantListener(view: View) {
        if (!shopScoreCoachMarkPrefs.getHasShownItemPM()) {
            coachMarkItemList.add(CoachMark2Item(
                    view.findViewById(R.id.containerPowerMerchant),
                    getString(R.string.title_coachmark_shop_score_3),
                    getString(R.string.desc_coachmark_shop_score_3),
                    position = CoachMark2.POSITION_TOP
            ))
            shopScoreCoachMarkPrefs.setHasShownItemPM(true)
        }
    }

    override fun onImpressHeaderPowerMerchantSection() {
        shopPerformanceTracking.impressPotentialPowerMerchant()
    }

    /**
     * ItemPotentialRegularMerchantListener
     */
    override fun onItemClickedBenefitPotentialRM() {
        goToPowerMerchantSubscribe()
    }

    /**
     * ItemPotentialRegularMerchantListener
     **/
    override fun onViewRegularMerchantListener(view: View) {
        if (!shopScoreCoachMarkPrefs.getHasShownItemRM()) {
            coachMarkItemList.add(CoachMark2Item(
                    view.findViewById(R.id.containerRegularMerchantSection),
                    getString(R.string.title_coachmark_shop_score_3),
                    getString(R.string.desc_coachmark_shop_score_3),
                    position = CoachMark2.POSITION_TOP
            ))
            shopScoreCoachMarkPrefs.setHasShownItemRM(true)
        }
        showCoachMark()
    }

    override fun onImpressBenefitSeeAll() {
        shopPerformanceTracking.impressSeeAllBenefitPowerMerchant()
    }

    /**
     * ItemHeaderShopPerformanceListener
     **/
    override fun onViewHeaderListener(view: View) {
        if (!shopScoreCoachMarkPrefs.getHasShownHeaderPerformanceDetail()) {
            coachMarkItemList.add(CoachMark2Item(
                    view.findViewById(R.id.containerCornerShopPerformance),
                    getString(R.string.title_coachmark_shop_score_1),
                    getString(R.string.desc_coachmark_shop_score_1),
                    position = CoachMark2.POSITION_BOTTOM
            ))
            shopScoreCoachMarkPrefs.setHasShownHeaderPerformanceDetail(true)
        }
    }

    /**
     * ItemTimerNewSellerListener
     */
    override fun onItemClickedRecommendationFeature(appLink: String) {
        RouteManager.route(requireContext(), appLink)
    }

    /**
     * ItemTimerNewSellerListener
     */
    override fun onBtnShopPerformanceToFaqClicked() {
        val faqData = shopPerformanceAdapter.list.firstOrNull { it is SectionFaqUiModel }
        if (faqData != null) {
            val positionFaqSection = shopPerformanceAdapter.list.indexOf(faqData)
            val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_END
                }
            }
            smoothScroller.targetPosition = positionFaqSection
            rvShopPerformance?.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    override fun onBtnShopPerformanceToInterruptClicked(infoPageUrl: String) {
        RouteManager.route(context, infoPageUrl)
    }

    override fun onWatchVideoClicked(videoId: String) {
        context?.startActivity(ShopPerformanceYoutubeActivity.createInstance(context, videoId))
    }

    override fun onHelpCenterClicked() {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, ShopScoreConstant.HELP_URL)
    }

    private fun showPenaltyBadge() {
        Handler().postDelayed({
            context?.let {
                val menuItem = menu?.findItem(PENALTY_WARNING_MENU_ID)
                if (counterPenalty.isLessThanZero()) {
                    penaltyDotBadge?.showBadge(menuItem ?: return@let)
                } else {
                    penaltyDotBadge?.removeBadge(menuItem ?: return@let)
                }
            }
        }, PENALTY_BADGE_DELAY)
    }

    private fun recyclerViewScrollListenerCoachMark() {
        rvShopPerformance?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val performanceLayoutManager = rvShopPerformance.layoutManager as? LinearLayoutManager
                performanceLayoutManager?.let { layoutManager ->
                    val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

                    val itemHeaderIndex = shopPerformanceAdapter.list.indexOfFirst { it is HeaderShopPerformanceUiModel }
                    val itemDetailPerformanceIndex = shopPerformanceAdapter.list.indexOfFirst { it is ItemDetailPerformanceUiModel }
                    val itemPMIndex = shopPerformanceAdapter.list.indexOfFirst { it is ItemStatusPMUiModel }
                    val itemRMIndex = shopPerformanceAdapter.list.indexOfFirst { it is ItemStatusRMUiModel }

                    if (coachMark?.isShowing == true) {
                        when (coachMark?.currentIndex) {
                            COACHMARK_HEADER_POSITION -> {
                                if (itemHeaderIndex in firstVisiblePosition..lastVisiblePosition) {
                                    coachMark?.animateShow()
                                } else {
                                    coachMark?.animateHide()
                                }
                            }
                            COACHMARK_ITEM_DETAIL_POSITION -> {
                                if (itemDetailPerformanceIndex in firstVisiblePosition..lastVisiblePosition) {
                                    coachMark?.animateShow()
                                } else {
                                    coachMark?.animateHide()
                                }
                            }
                            COACHMARK_LAST_POSITION_PM_RM -> {
                                if (itemPMIndex in firstVisiblePosition..lastVisiblePosition || itemRMIndex in firstVisiblePosition..lastVisiblePosition) {
                                    coachMark?.animateShow()
                                } else {
                                    coachMark?.animateHide()
                                }
                            }
                            else -> { }
                        }
                    }
                }
            }
        })
    }

    private fun showCoachMark() {
        coachMark?.setStepListener(object : CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                if (currentIndex == 1) {
                    coachMark?.stepPagination?.hide()
                    scrollToItemHeaderCoachMark()
                } else {
                    coachMark?.stepPagination?.show()
                }

                when (currentIndex) {
                    COACHMARK_ITEM_DETAIL_POSITION -> scrollToItemDetailCoachMark()
                    COACHMARK_LAST_POSITION_PM_RM -> scrollToLastItemCoachMark()
                    else -> {}
                }
            }
        })

        if (coachMarkItemList.isNotEmpty() && !shopScoreCoachMarkPrefs.getFinishCoachMark()) {
            coachMark?.showCoachMark(coachMarkItemList)
        }

        coachMark?.onFinishListener = {
            shopScoreCoachMarkPrefs.setFinishCoachMark(true)
        }
    }

    private fun scrollToLastItemCoachMark() {
        val positionPM = shopPerformanceAdapter.list.firstOrNull { it is ItemStatusPMUiModel }
        var position = -1
        if (positionPM != null) {
            position = shopPerformanceAdapter.list.indexOfFirst { it is ItemStatusPMUiModel }
        } else {
            val rmData = shopPerformanceAdapter.list.firstOrNull { it is ItemStatusRMUiModel }
            if (rmData != null) {
                position = shopPerformanceAdapter.list.indexOf(rmData)
            }
        }
        if (position != -1) {
            val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_END
                }
            }
            smoothScroller.targetPosition = position
            rvShopPerformance?.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    private fun scrollToItemHeaderCoachMark() {
        val positionItemHeader = shopPerformanceAdapter.list.indexOfFirst { it is HeaderShopPerformanceUiModel }
        if (positionItemHeader != -1) {
            val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
            smoothScroller.targetPosition = positionItemHeader
            rvShopPerformance?.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    private fun scrollToItemDetailCoachMark() {
        val positionItemDetail = shopPerformanceAdapter.list.indexOfFirst { it is ItemDetailPerformanceUiModel }
        if (positionItemDetail != -1) {
            val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
            smoothScroller.targetPosition = positionItemDetail
            rvShopPerformance?.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    private fun goToPenaltyPage() {
        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.SHOP_PENALTY)
    }

    private fun goToPowerMerchantSubscribe() {
        val appLink = ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
        RouteManager.route(requireContext(), appLink)
    }

    private fun setupAdapter() {
        rvShopPerformance?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shopPerformanceAdapter
        }
    }

    private fun observeShopPeriod() {
        observe(viewModel.shopInfoPeriod) {
            when (it) {
                is Success -> {
                    viewModel.getShopScoreLevel(it.data)
                }
                is Fail -> {
                    shopPerformanceAdapter.hideLoading()
                    shopPerformanceAdapter.setShopPerformanceError(ItemShopPerformanceErrorUiModel())
                }
            }
        }
        loadData()
    }

    private fun observeShopPerformancePage() {
        observe(viewModel.shopPerformancePage) {
            hideLoading()
            when (it) {
                is Success -> {
                    shopPerformanceAdapter.setShopPerformanceData(it.data.first)
                    this.shopScoreWrapperResponse = it.data.second
                    counterPenalty = it.data.first.filterIsInstance<HeaderShopPerformanceUiModel>().firstOrNull()?.scorePenalty.orZero()
                    showPenaltyBadge()
                    scrollToItemHeaderCoachMark()
                }
                is Fail -> {
                    shopPerformanceAdapter.hideLoading()
                    shopPerformanceAdapter.setShopPerformanceError(ItemShopPerformanceErrorUiModel())
                }
            }
        }
    }

    private fun onSwipeRefreshShopPerformance() {
        shopPerformanceSwipeRefresh?.setOnRefreshListener {
            loadData()
            showPenaltyBadge()
        }
    }

    private fun loadData() {
        shopPerformanceAdapter.clearAllElements()
        showLoading()
        viewModel.getShopInfoPeriod()
    }

    private fun showLoading() {
        shopPerformanceAdapter.showLoading()
        shopPerformanceSwipeRefresh?.isRefreshing = false
    }

    private fun hideLoading() {
        shopPerformanceAdapter.hideLoading()
    }

    companion object {

        val PENALTY_WARNING_MENU_ID = R.id.menu_penalty_shop_performance
        val INFO_MENU_ID = R.id.menu_info_shop_performance

        private const val PENALTY_BADGE_DELAY = 500L

        private const val COACHMARK_LAST_POSITION_PM_RM = 2
        private const val COACHMARK_HEADER_POSITION = 0
        private const val COACHMARK_ITEM_DETAIL_POSITION = 1

        @JvmStatic
        fun newInstance(): ShopPerformancePageFragment {
            return ShopPerformancePageFragment()
        }
    }

    override fun onBtnErrorStateClicked() {
        loadData()
        showPenaltyBadge()
    }
}