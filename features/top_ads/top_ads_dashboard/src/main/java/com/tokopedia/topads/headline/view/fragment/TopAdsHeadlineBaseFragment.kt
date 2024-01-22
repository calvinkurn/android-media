package com.tokopedia.topads.headline.view.fragment

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.constant.TopAdsFeature
import com.tokopedia.topads.common.data.internal.AutoAdsStatus.STATUS_INACTIVE
import com.tokopedia.topads.common.data.internal.AutoAdsStatus.STATUS_IN_PROGRESS_ACTIVE
import com.tokopedia.topads.common.data.internal.AutoAdsStatus.STATUS_IN_PROGRESS_AUTOMANAGE
import com.tokopedia.topads.common.data.internal.AutoAdsStatus.STATUS_IN_PROGRESS_INACTIVE
import com.tokopedia.topads.common.data.internal.AutoAdsStatus.STATUS_NOT_DELIVERED
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.AD_TYPE_SHOP_ADS
import com.tokopedia.topads.common.data.model.WhiteListUserResponse
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.common.data.response.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.common.recommendation.RecommendationWidget
import com.tokopedia.topads.common.view.widget.AutoAdsWidgetCommon
import com.tokopedia.topads.dashboard.R as topadsdashboardR
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTracker
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsBaseTabFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashDeletedGroupFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashStatisticFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.topads.dashboard.view.listener.RecommendationWidgetCTAListener
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import kotlin.math.abs
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by Pika on 16/10/20.
 */

private const val CLICK_MULAI_BERIKLAN = "click - mulai beriklan"
private const val VIEW_MULAI_BERIKLAN = "view - mulai iklan toko"

open class TopAdsHeadlineBaseFragment : TopAdsBaseTabFragment() {

    private var loader: LoaderUnify? = null
    private var swipeRefreshLayout: SwipeToRefresh? = null
    private var appBarLayout: AppBarLayout? = null
    private var hariIni: ConstraintLayout? = null
    private var pager: ViewPager? = null
    private var headlineAdsViePager: ViewPager? = null
    private var headlineTabLayout: TabsUnify? = null
    private var noTabSpace: View? = null
    private var recommendationWidget: RecommendationWidget? = null
    private var autoadsOnboarding: CardUnify? = null
    private var progressView: LinearLayout? = null
    private var autoadsEditWidget: AutoAdsWidgetCommon? = null
    private var recommendationWidgetCTAListener: RecommendationWidgetCTAListener? = null
    private var autoPsStatisticTable: CardUnify? = null
    private var confirmationDailog: DialogUnify? = null

    private val autoAdsWidget: AutoAdsWidgetCommon?
        get() = autoadsEditWidget

    @Inject
    lateinit var presenter: TopAdsDashboardPresenter

    @Inject
    lateinit var userSession: UserSessionInterface
    private var dataStatistic: DataStatistic? = null
    private var mCurrentState = TopAdsProductIklanFragment.State.IDLE
    private var collapseStateCallBack: AppBarActionHeadline? = null
    private var currentDateText: String = ""
    private var groupPagerAdapter: TopAdsDashboardBasePagerAdapter? = null
    private var isDeletedTabEnabled: Boolean = false
    private var tampil: Typography? = null
    private var click: Typography? = null
    private var percentClick: Typography? = null
    private var pengeluaran: Typography? = null
    private var pendapatan: Typography? = null
    private var efektivitas: Typography? = null
    private var produkTenjual: Typography? = null
    private var totalTenjual: Typography? = null
    private var onBoardingCta: UnifyButton? = null
    private var statisticGraph: CardUnify? = null
    private var currentAdsStatus: Int? = null

    companion object {
        fun createInstance(): TopAdsHeadlineBaseFragment {
            return TopAdsHeadlineBaseFragment()
        }
    }

    override fun getLayoutId(): Int {
        return topadsdashboardR.layout.topads_dash_headline_layout
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun setUpView(view: View) {
        headlineAdsViePager = view.findViewById(topadsdashboardR.id.headlineAdsViePager)
        headlineTabLayout = view.findViewById(topadsdashboardR.id.headlineTabLayout)
        noTabSpace = view.findViewById(topadsdashboardR.id.noTabSpace)
        loader = view.findViewById(topadsdashboardR.id.loader)
        swipeRefreshLayout = view.findViewById(topadsdashboardR.id.swipe_refresh_layout)
        appBarLayout = view.findViewById(topadsdashboardR.id.app_bar_layout_2)
        hariIni = view.findViewById(topadsdashboardR.id.hari_ini)
        pager = view.findViewById(topadsdashboardR.id.pager)
        autoadsOnboarding = view.findViewById(topadsdashboardR.id.autoadsOnboarding)
        recommendationWidget = view.findViewById(topadsdashboardR.id.insightCentreEntryPointHeadline)
        autoadsEditWidget = view.findViewById(topadsdashboardR.id.autoads_edit_widget)
        progressView = view.findViewById(topadsdashboardR.id.progressView)
        autoPsStatisticTable = view.findViewById(topadsdashboardR.id.auto_ps_statistic_table)
        tampil = view.findViewById(topadsdashboardR.id.tampil_count)
        click = view.findViewById(topadsdashboardR.id.klik_count)
        percentClick = view.findViewById(topadsdashboardR.id.persentase_klik_count)
        pendapatan = view.findViewById(topadsdashboardR.id.pendapatan_count)
        efektivitas = view.findViewById(topadsdashboardR.id.efektivitas_iklan_count)
        produkTenjual = view.findViewById(topadsdashboardR.id.produk_terjual_count)
        pengeluaran = view.findViewById(topadsdashboardR.id.pengeluaran_count)
        totalTenjual = view.findViewById(topadsdashboardR.id.total_terjual_count)
        onBoardingCta = view.findViewById(topadsdashboardR.id.onBoarding)
        statisticGraph = view.findViewById(topadsdashboardR.id.statistic_graph)
    }

    override fun getChildScreenName(): String {
        return TopAdsHeadlineBaseFragment::class.java.name
    }

    override fun loadChildStatisticsData() {
        swipeRefreshLayout?.isRefreshing = false
        val list = (headlineAdsViePager?.adapter as? TopAdsDashboardBasePagerAdapter)?.getList()
        list?.forEach { fragmentTabItem ->
            when (val f = fragmentTabItem.fragment) {
                is TopAdsDashDeletedGroupFragment -> {
                    f.fetchFirstPage(AD_TYPE_SHOP_ADS)
                }
                is TopAdsHeadlineShopFragment -> {
                    f.fetchFirstPage()
                }
            }
        }
        loadStatisticsData()
    }

    private fun setUpObserver() {
        presenter.groupAdsInsight.observe(viewLifecycleOwner) {
            if (it is TopAdsListAllInsightState.Success) {
                recommendationWidget?.renderWidget(it.data.remainingAdsGroup, it.data.totalAdsGroup)
                recommendationWidget?.binding?.widgetCTAButton?.setOnClickListener {
                    RecommendationTracker.clickLihatSelengkapnyaSaranTopadsHeadline()
                    recommendationWidgetCTAListener?.onWidgetCTAClick()
                }
            }
        }
        presenter.shopVariant.observe(viewLifecycleOwner){
            getAutoAdsStatus()
        }
    }

    private fun isAutoPsWhitelisted(): Boolean {
        var isWhitelisted = false
        presenter.shopVariant.value?.let { shopVariants ->
            isWhitelisted = shopVariants.isNotEmpty() && shopVariants.filter {
                it.experiment == TopAdsCommonConstant.AUTOPS_EXPERIMENT &&
                    it.variant == TopAdsCommonConstant.AUTOPS_VARIANT }
                .isNotEmpty()
        }
        return isWhitelisted
    }

    private fun getAutoAdsStatus() {
        try {
            if(isAutoPsWhitelisted())
                presenter.getAutoAdsStatus(requireContext().resources, ::setAutoAds)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun setAutoAds(data: AutoAdsResponse.TopAdsGetAutoAds.Data) {
        currentAdsStatus = data.status
        when (data.status) {
            STATUS_INACTIVE -> setAutoPsInactiveLayout()

            STATUS_IN_PROGRESS_ACTIVE,
            STATUS_IN_PROGRESS_AUTOMANAGE,
            STATUS_IN_PROGRESS_INACTIVE -> setProgressiveLayout(data.status)

            else -> setAutoPsActiveLayout()
        }
    }

    private fun setProgressiveLayout(status: Int) {
        autoAdsWidget?.show()
        autoAdsWidget?.loadData(0)
        val title = view?.findViewById<Typography>(topadsdashboardR.id.progressiveView_title)
        val desc = view?.findViewById<Typography>(topadsdashboardR.id.progressiveView_description)
        if(status == STATUS_IN_PROGRESS_ACTIVE){
            title?.text = getString(topadsdashboardR.string.topads_dash_reload_title)
            desc?.text = getString(topadsdashboardR.string.topads_dash_auto_ads_enable_msg)
        } else if(status == STATUS_IN_PROGRESS_INACTIVE){
            title?.text = getString(topadsdashboardR.string.topads_autops_deactivation_inprogress_title)
            desc?.text = getString(topadsdashboardR.string.topads_autops_deactivation_inprogress_description)
        }
        progressView?.show()
        autoadsOnboarding?.gone()
        headlineAdsViePager?.gone()
        headlineTabLayout?.gone()
        autoPsStatisticTable?.gone()
        recommendationWidget?.gone()
        statisticGraph?.gone()
        view?.findViewById<CardUnify>(topadsdashboardR.id.empty_view)?.gone()
        view?.findViewById<CardUnify>(topadsdashboardR.id.empty_view_autops)?.gone()
    }

    private fun setAutoPsInactiveLayout(){
        autoAdsWidget?.gone()
        autoadsOnboarding?.show()
        headlineAdsViePager?.show()
        headlineTabLayout?.show()
        autoPsStatisticTable?.gone()
        recommendationWidget?.show()
        statisticGraph?.show()
        progressView?.gone()
        setupOnboarding()
    }

    private fun setAutoPsActiveLayout(){
        autoAdsWidget?.show()
        autoadsOnboarding?.gone()
        autoAdsWidget?.loadData(0)
        headlineAdsViePager?.gone()
        headlineTabLayout?.gone()
        autoPsStatisticTable?.show()
        statisticGraph?.show()
        recommendationWidget?.gone()
        progressView?.gone()
        val resources = context?.resources
        if(resources != null) {
            presenter.getProductStats(
                resources,
                Utils.format.format(startDate),
                Utils.format.format(endDate),
                mutableListOf(),
                String.EMPTY,
                Int.ZERO,
                ::setAutoPsStatistics
            )
        }
    }

    private fun setupOnboarding(){
        if(isAutoPsWhitelisted()){
            onBoardingCta?.text = getString(topadsdashboardR.string.topads_enable_auto_ps)
            onBoardingCta?.setOnClickListener {
                if (confirmationDailog?.isShowing != true) {
                    confirmationDailog =
                        DialogUnify(
                            requireContext(),
                            DialogUnify.HORIZONTAL_ACTION,
                            DialogUnify.WITH_ILLUSTRATION
                        )
                    confirmationDailog?.show()
                }

                val description = getString(topadsdashboardR.string.topads_auto_ps_activation_confirmation_desc)
                val title = getString(topadsdashboardR.string.topads_auto_ps_confirmation_title_to_activate_auto_ps)

                confirmationDailog?.let {
                    it.setTitle(title)
                    it.setImageUrl(TopAdsDashboardConstant.ACTIVATE_AUTO_PS_CONFIRMATION_IMG_URL)
                    it.setDescription(description)

                    it.setPrimaryCTAText(getString(topadsdashboardR.string.topads_dash_aktifan))
                    it.setSecondaryCTAText(getString(topadsdashboardR.string.top_ads_batal))

                    it.setPrimaryCTAClickListener {
                        RouteManager.route(context,ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE)
                    }

                    it.setSecondaryCTAClickListener {
                        it.dismiss()
                    }
                }
            }
        }
    }

    private fun setAutoPsStatistics(data: GetDashboardProductStatistics){
        if(data.data.isNotEmpty()) {
            tampil?.text = data.data.firstOrNull()?.adPriceDailyFmt
            click?.text = data.data.firstOrNull()?.adPriceBidFmt
            percentClick?.text = data.data.firstOrNull()?.adPriceBid.toString()
            pendapatan?.text = data.data.firstOrNull()?.statTotalImpression
            efektivitas?.text = data.data.firstOrNull()?.statTotalSpent
            produkTenjual?.text = data.data.firstOrNull()?.statTotalCtr
            pengeluaran?.text = data.data.firstOrNull()?.statTotalConversion
            totalTenjual?.text = data.data.firstOrNull()?.statTotalSold
        }
    }

    override fun renderGraph() {
        currentStatisticsFragment?.showLineGraph(dataStatistic)
    }

    override fun getCustomDateText(customDateText: String) {
        currentDateText = customDateText
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loader?.visibility = View.VISIBLE
        loadStatisticsData()
        swipeRefreshLayout?.setOnRefreshListener {
            loadChildStatisticsData()
            getAutoAdsStatus()
        }
        appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
            when {
                offset == 0 -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.EXPANDED) {
                        onStateChanged(TopAdsProductIklanFragment.State.EXPANDED)
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.EXPANDED
                }
                abs(offset) >= appBarLayout.totalScrollRange -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.COLLAPSED) {
                        onStateChanged(TopAdsProductIklanFragment.State.COLLAPSED)
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.COLLAPSED
                }
                else -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.IDLE) {
                        onStateChanged(TopAdsProductIklanFragment.State.IDLE)
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.IDLE
                }
            }
        })
        presenter.getAdGroupWithInsight(RecommendationConstants.HEADLINE_KEY)
    }

    private fun onSuccessWhiteListing(response: WhiteListUserResponse.TopAdsGetShopWhitelistedFeature) {
        response.data.forEach {
            when (it.featureId) {
                TopAdsFeature.DELETED_TAB_PRODUCT_HEADLINE -> isDeletedTabEnabled = true
            }
        }
    }

    private fun renderHeadlineViewPager() {
        headlineAdsViePager?.adapter = getHeadlineViewPagerAdapter()
        headlineAdsViePager?.let { headlineTabLayout?.setupWithViewPager(it) }
    }

    private fun getHeadlineViewPagerAdapter(): TopAdsDashboardBasePagerAdapter {
        val list: ArrayList<FragmentTabItem> = arrayListOf()
        headlineTabLayout?.let {
            it.getUnifyTabLayout().removeAllTabs()
            it.addNewTab(TopAdsDashboardConstant.IKLAN_TOKO)
            it.customTabMode = TabLayout.MODE_SCROLLABLE
        }
        list.add(
            FragmentTabItem(
                TopAdsDashboardConstant.IKLAN_TOKO,
                TopAdsHeadlineShopFragment.createInstance()
            )
        )
        addDeletedTab(list)
        val adapter = TopAdsDashboardBasePagerAdapter(childFragmentManager, 0)
        adapter.setList(list)
        groupPagerAdapter = adapter
        return adapter
    }

    private fun addDeletedTab(list: ArrayList<FragmentTabItem>) {
        if (isDeletedTabEnabled) {
            headlineTabLayout?.show()
            noTabSpace?.hide()
            headlineTabLayout?.customTabMode = TabLayout.MODE_FIXED
            headlineTabLayout?.addNewTab(TopAdsDashboardConstant.DIHAPUS)
            list.add(
                FragmentTabItem(
                    TopAdsDashboardConstant.DIHAPUS,
                    TopAdsDashDeletedGroupFragment.createInstance(prepareBundle())
                )
            )
        }
    }

    private fun prepareBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString(ParamObject.KEY_AD_TYPE, AD_TYPE_SHOP_ADS)
        return bundle
    }

    private fun onStateChanged(state: TopAdsProductIklanFragment.State?) {
        collapseStateCallBack?.setAppBarStateHeadline(state)
        swipeRefreshLayout?.isEnabled = state == TopAdsProductIklanFragment.State.EXPANDED
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.getVariantById()
        presenter.getWhiteListedUser(::onSuccessWhiteListing) {
            presenter.getShopAdsInfo {
                val info = it.topadsGetShopInfoV2_1.data.ads.getOrNull(1)
                if (info?.type == TopAdsDashboardConstant.HEADLINE) {
                    if (!info.isUsed) {
                        if(isAutoPsWhitelisted()){
                            showAutoPsEmptyView()
                        } else {
                            showEmptyView()
                        }
                    } else {
                        renderHeadlineViewPager()
                    }
                }
            }
        }
        setUpObserver()
    }

    private fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        presenter.getStatistic(
            startDate!!,
            endDate!!,
            TopAdsStatisticsType.HEADLINE_ADS,
            "-1",
            ::onSuccessGetStatisticsInfo
        )
    }

    private fun onSuccessGetStatisticsInfo(dataStatistic: DataStatistic) {
        loader?.visibility = View.GONE
        this.dataStatistic = dataStatistic
        if (this.dataStatistic != null && dataStatistic.cells.isNotEmpty()) {
            topAdsTabAdapter?.setSummary(
                dataStatistic.summary,
                resources.getStringArray(topadsdashboardR.array.top_ads_tab_statistics_labels)
            )
            topAdsTabAdapter?.hideTabforHeadline()
        }
        val fragment = pager?.let { it.adapter?.instantiateItem(it, it.currentItem) } as? Fragment
        if (fragment != null && fragment is TopAdsDashStatisticFragment) {
            fragment.showLineGraph(this.dataStatistic)
        }
    }

    private fun showEmptyView() {
        progressView?.gone()
        appBarLayout?.visibility = View.GONE
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(VIEW_MULAI_BERIKLAN,
            "{${userSession.shopId}}",
            userSession.userId)
        view?.findViewById<ConstraintLayout>(topadsdashboardR.id.empty_view)?.visibility = View.VISIBLE
        view?.findViewById<UnifyButton>(topadsdashboardR.id.mulai_beriklan)?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(CLICK_MULAI_BERIKLAN,
                "{${userSession.shopId}}",
                userSession.userId)
            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION)
        }
        view?.findViewById<ImageUnify>(topadsdashboardR.id.image_empty)
            ?.setImageDrawable(context?.getResDrawable(topadsdashboardR.drawable.topads_dashboard_empty_product))
        view?.findViewById<Typography>(topadsdashboardR.id.text_title)?.text =
            getString(topadsdashboardR.string.topads_headline_empty_state_title)
        view?.findViewById<Typography>(topadsdashboardR.id.text_desc)?.text =
            getString(topadsdashboardR.string.topads_headline_empty_state_desc)
        hariIni?.visibility = View.GONE
    }

    private fun showAutoPsEmptyView() {
        if(currentAdsStatus == STATUS_NOT_DELIVERED){
            setAutoPsActiveLayout()
        } else {
            appBarLayout?.gone()
            hariIni?.gone()
            view?.findViewById<CardUnify>(topadsdashboardR.id.empty_view_autops)?.let {
                it.show()
                it.findViewById<Typography>(topadsdashboardR.id.article_link)?.let {
                    it.movementMethod = LinkMovementMethod.getInstance()
                    it.text = getClickableString()
                }
                it.findViewById<ImageUnify>(topadsdashboardR.id.empty_image)?.urlSrc = TopAdsDashboardConstant.IKLAN_TOKO_AUTO_PS_EMPTY_VIEW_IMG_URL
                it.findViewById<UnifyButton>(topadsdashboardR.id.create_shopads_cta)?.setOnClickListener {
                    RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_HEADLINE_ADS_CREATION)
                }
                it.findViewById<UnifyButton>(topadsdashboardR.id.autops_activate_cta)?.setOnClickListener {
                    RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_AUTOADS_CREATE)
                }
            }
        }
    }

    private fun getClickableString(): SpannableString {
        val text = getString(topadsdashboardR.string.topads_auto_ps_iklan_toko_empty_view_desc)
        val ss = SpannableString(text)
        val cs = object : ClickableSpan() {
            override fun onClick(p0: View) {
                RouteManager.route(
                    context,
                    ApplinkConstInternalGlobal.WEBVIEW,
                    TopAdsDashboardConstant.IKLAN_TOKO_AUTO_PS_ARTICLE_LINK
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                context?.let {
                    ds.color = ContextCompat.getColor(
                        it,
                        unifyprinciplesR.color.Unify_GN500
                    )
                }
                ds.isFakeBoldText = true
            }
        }

        ss.setSpan(cs, text.length - 12, text.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    override fun setDeletedGroupCount(size: Int) {
        headlineTabLayout?.getUnifyTabLayout()?.getTabAt(1)?.setCounter(size)
    }

    override fun setGroupCount(size: Int) {
        headlineTabLayout?.getUnifyTabLayout()?.getTabAt(0)?.setCounter(size)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppBarActionHeadline)
            collapseStateCallBack = context

        if (context is RecommendationWidgetCTAListener){
            recommendationWidgetCTAListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        collapseStateCallBack = null
    }

    interface AppBarActionHeadline {
        fun setAppBarStateHeadline(state: TopAdsProductIklanFragment.State?)
    }

}
