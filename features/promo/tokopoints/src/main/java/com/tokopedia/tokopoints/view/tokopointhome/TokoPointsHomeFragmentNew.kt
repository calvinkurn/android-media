package com.tokopedia.tokopoints.view.tokopointhome

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.android.material.tabs.TabLayout.ViewPagerOnTabSelectedListener
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.view.adapter.SectionCategoryAdapter
import com.tokopedia.tokopoints.view.customview.CustomViewPager
import com.tokopedia.tokopoints.view.customview.DynamicItemActionView
import com.tokopedia.tokopoints.view.customview.ServerErrorView
import com.tokopedia.tokopoints.view.customview.TokoPointToolbar
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.TokopointhomePlt.Companion.HOME_TOKOPOINT_PLT
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.TokopointhomePlt.Companion.HOME_TOKOPOINT_PLT_NETWORK_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.TokopointhomePlt.Companion.HOME_TOKOPOINT_PLT_PREPARE_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.TokopointhomePlt.Companion.HOME_TOKOPOINT_PLT_RENDER_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceMonitoringListener
import com.tokopedia.tokopoints.view.interfaces.onAppBarCollapseListener
import com.tokopedia.tokopoints.view.intro.RewardIntroActivity
import com.tokopedia.tokopoints.view.intro.RewardIntroFragment
import com.tokopedia.tokopoints.view.model.rewardintro.TokopediaRewardIntroPage
import com.tokopedia.tokopoints.view.model.rewardtopsection.DynamicActionListItem
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.tp_fragment_homepage_new.*
import kotlinx.android.synthetic.main.tp_item_dynamic_action.view.*
import javax.inject.Inject

/*
 * Dynamic layout params are applied via
 * function setLayoutParams() because configuration in statusBarHeight
 * */
class TokoPointsHomeFragmentNew : BaseDaggerFragment(), TokoPointsHomeContract.View, View.OnClickListener, TokopointPerformanceMonitoringListener {
    private var mContainerMain: ViewFlipper? = null
    private var mTextMembershipValue: TextView? = null
    private var mTargetText: TextView? = null
    private var mTextMembershipValueBottom: TextView? = null
    private var mTextPoints: TextView? = null
    private var mTextPointsBottom: TextView? = null
    private var mTextMembershipLabel: TextView? = null
    private var mImgEgg: ImageView? = null
    private var mImgBackground: ImageView? = null
    private var mTabLayoutPromo: TabLayout? = null
    private var mPagerPromos: CustomViewPager? = null
    private var mRvDynamicLinks: RecyclerView? = null

    @Inject
    lateinit var viewFactory: ViewModelFactory
    private val mPresenter: TokoPointsHomeViewModel by lazy { ViewModelProviders.of(this, viewFactory).get(TokoPointsHomeViewModel::class.java) }
    private var mValueMembershipDescription: String? = null
    lateinit var tickerContainer: View
    lateinit var dynamicLinksContainer: View
    private var appBarCollapseListener: onAppBarCollapseListener? = null
    private var mExploreSectionPagerAdapter: ExploreSectionPagerAdapter? = null
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var coordinatorLayout: CoordinatorLayout? = null
    private var statusBarBgView: View? = null
    private var tokoPointToolbar: TokoPointToolbar? = null
    private var serverErrorView: ServerErrorView? = null
    private var rewardsPointLayout: CardUnify? = null
    private var ivPointStack: AppCompatImageView? = null
    private var dynamicAction: DynamicItemActionView? = null
    lateinit var tvSectionTitleCategory: TextView
    lateinit var tvSectionSubtitleCateory: TextView
    lateinit var appBarHeader: AppBarLayout
    lateinit var categorySeeAll: TextView
    lateinit var cardTierInfo: ConstraintLayout
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null
    private val dynamicItem = "dynamicItem"
    private val toolbarItemList = mutableListOf<NotificationUnify>()

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tp_fragment_homepage_new, container, false)
        initViews(view)
        hideStatusBar()
        (activity as BaseSimpleActivity?)!!.setSupportActionBar(tokoPointToolbar)
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar)
        collapsingToolbarLayout?.setExpandedTitleColor(resources.getColor(android.R.color.transparent))
        collapsingToolbarLayout?.setTitle(" ")
        if (::appBarHeader.isInitialized) {
            appBarHeader.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout: AppBarLayout?, verticalOffset: Int -> handleAppBarOffsetChange(verticalOffset) })
            appBarHeader.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout: AppBarLayout?, verticalOffset: Int -> handleAppBarIconChange(appBarLayout, verticalOffset) })
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stopPreparePagePerformanceMonitoring()
        startNetworkRequestPerformanceMonitoring()
        initListener()
        mPresenter.getTokoPointDetail()
        tokoPointToolbar?.setTitle(R.string.tp_title_tokopoints)
        initObserver()
    }

    private fun initObserver() {
        addRedeemCouponObserver()
        addTokopointDetailObserver()
        addRewardIntroObserver()
    }

    private fun setLayoutParams(cardheight: Int) {
        val statusBarHeight = getStatusBarHeight(activity)
        val layoutParams = tokoPointToolbar!!.layoutParams as FrameLayout.LayoutParams
        layoutParams.topMargin = statusBarHeight
        tokoPointToolbar!!.layoutParams = layoutParams
        val imageEggLp = mImgEgg!!.layoutParams as RelativeLayout.LayoutParams
        imageEggLp.topMargin = (statusBarHeight + activity!!.resources.getDimension(R.dimen.tp_top_margin_big_image)).toInt()
        mImgEgg!!.layoutParams = imageEggLp
        val imageBigLp = mImgBackground!!.layoutParams as RelativeLayout.LayoutParams
        imageBigLp.height = (statusBarHeight + activity!!.resources.getDimension(R.dimen.tp_home_top_bg_height) + cardheight).toInt()
        mImgBackground!!.layoutParams = imageBigLp
    }

    private fun setStatusBarViewHeight() {
        if (activity != null) statusBarBgView?.layoutParams?.height = getStatusBarHeight(activity)
    }

    private fun hideStatusBar() {
        coordinatorLayout!!.fitsSystemWindows = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            coordinatorLayout!!.requestApplyInsets()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = coordinatorLayout!!.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            coordinatorLayout!!.systemUiVisibility = flags
            activity!!.window.statusBarColor = Color.WHITE
        }
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            activity!!.window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun handleAppBarOffsetChange(offset: Int) {
        val positiveOffset = offset * -1
        val toolbarTransitionRange = (resources.getDimensionPixelSize(R.dimen.tp_home_top_bg_height)
                - tokoPointToolbar!!.height - getStatusBarHeight(activity))
        var offsetAlpha = 255f / toolbarTransitionRange * (toolbarTransitionRange - positiveOffset)
        if (offsetAlpha < 0) {
            offsetAlpha = 0f
        }
        if (offsetAlpha >= 255) {
            offsetAlpha = 255f
        }
        var alpha = offsetAlpha / 255 - 1
        if (alpha < 0) alpha = alpha * -1
        statusBarBgView?.alpha = alpha
        if (alpha > 0.5) tokoPointToolbar?.switchToDarkMode() else tokoPointToolbar?.switchToTransparentMode()
        tokoPointToolbar?.applyAlphaToToolbarBackground(alpha)
    }

    private fun handleAppBarIconChange(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val verticalOffset1 = Math.abs(verticalOffset)
        if (verticalOffset1 >= (resources.getDimensionPixelSize(R.dimen.tp_home_top_bg_height))) {
            tokoPointToolbar?.showToolbarIcon()
        } else
            tokoPointToolbar?.hideToolbarIcon()
    }


    private fun addRewardIntroObserver() = mPresenter.rewardIntroData.observe(this, Observer {
        it?.let {
            when (it) {
                is Success -> {
                    showOnBoardingTooltip(it.data.tokopediaRewardIntroPage)
                }
            }
        }
    })

    private fun addTokopointDetailObserver() = mPresenter.tokopointDetailLiveData.observe(this, androidx.lifecycle.Observer {
        it?.let {
            when (it) {
                is Loading -> showLoading()
                is ErrorMessage -> {
                    hideLoading()
                    onError(it.data, NetworkDetector.isConnectedToInternet(context))
                }
                is Success -> {
                    hideLoading()
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    setOnRecyclerViewLayoutReady()
                    onSuccessResponse(it.data.tokoPointEntity, it.data.sectionList)
                }
                else -> {
                }
            }
        }
    })

    private fun addRedeemCouponObserver() = mPresenter.onRedeemCouponLiveData.observe(this, androidx.lifecycle.Observer {
        it?.let { RouteManager.route(context, it) }
    })

    override fun onSuccessResponse(data: TokopediaRewardTopSection?, sections: List<SectionContent>) {
        mContainerMain?.displayedChild = CONTAINER_DATA
        renderToolbarWithHeader(data)
        renderSections(sections)
    }

    override fun onError(error: String, hasInternet: Boolean) {
        if (mContainerMain != null) {
            mContainerMain?.displayedChild = CONTAINER_ERROR
            serverErrorView?.showErrorUi(hasInternet)
        }
    }

    private fun initViews(view: View) {
        coordinatorLayout = view.findViewById(R.id.container)
        mContainerMain = view.findViewById(R.id.container_main)
        mTextMembershipValue = view.findViewById(R.id.text_membership_value)
        mTargetText = view.findViewById(R.id.tv_targetText)
        mTextMembershipLabel = view.findViewById(R.id.text_membership_label)
        mImgEgg = view.findViewById(R.id.img_egg)
        mTabLayoutPromo = view.findViewById(R.id.tab_layout_promos)
        mPagerPromos = view.findViewById(R.id.view_pager_promos)
        mPagerPromos?.disableScroll(true)
        mTextPointsBottom = view.findViewById(R.id.text_my_points_value_bottom)
        mImgBackground = view.findViewById(R.id.img_bg_header)
        appBarHeader = view.findViewById(R.id.app_bar)
        statusBarBgView = view.findViewById(R.id.status_bar_bg)
        tokoPointToolbar = view.findViewById(R.id.toolbar_tokopoint)
        serverErrorView = view.findViewById(R.id.server_error_view)
        rewardsPointLayout = view.findViewById(R.id.card_point)
        ivPointStack = view.findViewById(R.id.img_points_stack)
        dynamicAction = view.findViewById(R.id.dynamic_widget)
        cardTierInfo = view.findViewById(R.id.container_target)

        setStatusBarViewHeight()
    }

    private fun initListener() {
        if (view == null) {
            return
        }
        view?.findViewById<View>(R.id.img_egg)?.setOnClickListener(this)
        view?.findViewById<View>(R.id.text_membership_value)?.setOnClickListener(this)
        serverErrorView?.setErrorButtonClickListener { mPresenter.getTokoPointDetail() }
    }

    override fun openWebView(url: String) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, url)
    }

    private fun showOnBoardingTooltip(data: TokopediaRewardIntroPage?) {
        if (data != null && data.resultStatus?.code == "200") {
            val bundle = Bundle()
            bundle.putParcelable(RewardIntroFragment.INTRO_KEY, data)
            startActivity(RewardIntroActivity.getCallingIntent(context!!, bundle))
            activity?.finish()
        } else
            return
    }

    override fun renderTicker(content: SectionContent) {

        val viewTicker = View.inflate(context, R.layout.tp_layout_section_ticker_new, null)
        tickerContainer = viewTicker.findViewById(R.id.cons_ticker_container)

        if (view == null || content == null || content.layoutTickerAttr == null || content.layoutTickerAttr.tickerList == null || content.layoutTickerAttr.tickerList.isEmpty()) {
            tickerContainer.visibility = View.GONE
            return
        }

        val pager: Ticker? = viewTicker?.findViewById(R.id.ticker_new)
        var link = ""
        var desc = ""
        var linkDesc = ""
        for (tickerItem in content.layoutTickerAttr.tickerList[0].metadata) {
            link = if (tickerItem.link[CommonConstant.TickerMapKeys.APP_LINK]?.length != 0) {
                tickerItem.link[CommonConstant.TickerMapKeys.APP_LINK].toString()
            } else {
                tickerItem.link[CommonConstant.TickerMapKeys.URL].toString()
            }

            if (link.isNotEmpty()) {
                linkDesc = tickerItem.text[CommonConstant.TickerMapKeys.CONTENT].toString()
            } else {
                desc = tickerItem.text[CommonConstant.TickerMapKeys.CONTENT].toString()
            }
        }

        val descriptionText = desc + "<a href=\"${link}\">" + ". " + linkDesc + "</a>"
        pager?.setHtmlDescription(descriptionText)
        pager?.findViewById<TextView>(com.tokopedia.unifycomponents.R.id.ticker_description)?.setMargin(16, 8, 22, 8)
        pager?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                if (link.startsWith(CommonConstant.TickerMapKeys.TOKOPEDIA)) {
                    RouteManager.route(context, link)
                } else {
                    RouteManager.route(context, String.format("%s?url=%", ApplinkConst.WEBVIEW, link))
                }
                AnalyticsTrackerUtil.sendEvent(context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_TICKER,
                        "$desc $linkDesc")
            }

            override fun onDismiss() {
            }
        })

        tickerContainer.visibility = View.VISIBLE
        categoryParent.addView(viewTicker)
    }

    override fun renderCategory(content: SectionContent) {

        val viewCategory = View.inflate(context, R.layout.tp_layout_section_category_parent, null)
        tvSectionTitleCategory = viewCategory.findViewById(R.id.tv_sectionTitle)
        tvSectionSubtitleCateory = viewCategory.findViewById(R.id.tv_ovopointValue)
        dynamicLinksContainer = viewCategory.findViewById(R.id.container_dynamic_links)
        categorySeeAll = viewCategory.findViewById(R.id.tv_seeall_category)
        mRvDynamicLinks = viewCategory.findViewById(R.id.rv_dynamic_link)
        if (content.layoutCategoryAttr == null || content.layoutCategoryAttr.categoryTokopointsList == null || content.layoutCategoryAttr.categoryTokopointsList.isEmpty()) {
            return
        }
        if (content.sectionTitle.isNotEmpty()) {
            tvSectionTitleCategory.show()
            tvSectionTitleCategory.text = content.sectionTitle
        }
        if (content.sectionSubTitle.isNotEmpty()) {
            tvSectionSubtitleCateory.show()
            tvSectionSubtitleCateory.text = content.sectionSubTitle
        }
        dynamicLinksContainer.visibility = View.VISIBLE

        content.cta?.let {
            if (it.text.isNotEmpty()) {
                categorySeeAll.text = it.text
                categorySeeAll.show()
            }
            if (it.appLink.isNotEmpty()) {
                categorySeeAll.setOnClickListener { _ ->
                    RouteManager.route(context, it.appLink)
                }
            } else if (it.appLink.isEmpty() && it.url.isNotEmpty()) {
                categorySeeAll.setOnClickListener { _ ->
                    openWebView(it.url)
                }
            }
        }
        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mRvDynamicLinks?.layoutManager = manager
        mRvDynamicLinks?.adapter = SectionCategoryAdapter(activityContext, content.layoutCategoryAttr.categoryTokopointsList)

        categoryParent.addView(viewCategory)
    }

    override fun renderToolbarWithHeader(data: TokopediaRewardTopSection?) {
        cardTierInfo.doOnLayout {
            setLayoutParams(it.height)
        }
        addDynamicToolbar(data?.dynamicActionList)
        mTextMembershipLabel?.text = data?.introductionText

        data?.target?.let {
            mTargetText?.setTextColor(Color.parseColor("#" + it.textColor))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mTargetText?.text = Html.fromHtml(it.text, Html.FROM_HTML_MODE_LEGACY)
            } else {
                mTargetText?.text = Html.fromHtml(it.text)
            }
            cardTierInfo.background.setColorFilter(Color.parseColor("#" + it.backgroundColor), PorterDuff.Mode.SRC_OVER)
            cardTierInfo.setOnClickListener {
                RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW_TITLE, getString(R.string.tp_label_membership), CommonConstant.WebLink.MEMBERSHIP)
                AnalyticsTrackerUtil.sendEvent(context,
                        AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_MEMBERSHIP, "")
            }
        }

        ImageHandler.loadImageCircle2(activityContext, mImgEgg, data?.profilePicture)
        mTextMembershipValueBottom?.text = mValueMembershipDescription
        data?.backgroundImageURLMobileV2?.let { mImgBackground?.loadImage(it) }
        if (data?.tier != null) {
            mTextMembershipValue?.text = data.tier.nameDesc
        }

        renderDynamicActionList(data?.dynamicActionList)
    }


    private fun addDynamicToolbar(dynamicActionList: List<DynamicActionListItem?>?) {
        dynamicActionList?.forEachIndexed { index, it ->
            it?.let { item ->
                tokoPointToolbar?.addItem(it)?.apply {
                    toolbarItemList.add(this.notif_dynamic)
                    setOnClickListener {
                        RouteManager.route(context, item.cta?.appLink)
                        hideNotification(index)
                        AnalyticsTrackerUtil.sendEvent(context,
                                AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                                item.cta?.text?.let { it1 -> AnalyticsTrackerUtil.ActionKeys.KEY_EVENT_CLICK_DYNAMICITEM.replace(dynamicItem, it1) },
                                "")
                    }
                }
            }
        }
    }

    fun hideNotification(index: Int) {
        toolbarItemList[index].hide()
        when (index) {
            0 -> dynamicAction?.notifFirstLayout?.hide()
            1 -> dynamicAction?.notifCenterLayout?.hide()
            2 -> dynamicAction?.notifRightLayout?.hide()
        }
    }

    fun renderDynamicActionList(dataList: List<DynamicActionListItem?>?) {

        if (dataList != null && dataList.isNotEmpty()) {
            dynamicAction?.setFirstLayoutVisibility(View.VISIBLE)
            dataList[0]?.cta?.text?.let { dynamicAction?.setFirstLayoutText(it) }
            dataList[0]?.iconImageURL?.let { dynamicAction?.setFirstLayoutIcon(it) }
            if (dataList[0]?.counter?.isShowCounter!! && dataList[0]?.counter?.counterStr != "0") {
                dataList[0]?.counter?.counterStr?.let { dynamicAction?.setFirstLayoutNotification(it) }
            }
            dynamicAction?.findViewById<LinearLayout>(R.id.holder_tokopoint)?.setOnClickListener {
                dataList[0]?.cta?.let {
                    hideNotification(0)
                    dynamicAction?.setLayoutClickListener(it.appLink, it.text)
                }
            }
            if (dataList.size > 1) {
                dynamicAction?.setCenterLayoutVisibility(View.VISIBLE)
                dataList[1]?.cta?.text?.let { dynamicAction?.setCenterLayoutText(it) }
                dataList[1]?.iconImageURL?.let { dynamicAction?.setCenterLayoutIcon(it) }
                if (dataList[1]?.counter?.isShowCounter!! && dataList[1]?.counter?.counterStr != "0") {
                    dataList[1]?.counter?.counterStr?.let { dynamicAction?.setCenterLayoutNotification(it) }
                }
                dynamicAction?.findViewById<LinearLayout>(R.id.holder_coupon)?.setOnClickListener {
                    dataList[1]?.cta?.let {
                        hideNotification(1)
                        dynamicAction?.setCenterLayoutClickListener(it.appLink, it.text)
                    }
                }
                dynamicAction?.setVisibilityDividerOne(View.VISIBLE)
            }
            if (dataList.size > 2) {
                dynamicAction?.setRightLayoutVisibility(View.VISIBLE)
                dataList[2]?.cta?.text?.let { dynamicAction?.setRightLayoutText(it) }
                dataList[2]?.iconImageURL?.let { dynamicAction?.setRightLayoutIcon(it) }
                if (dataList[2]?.counter?.isShowCounter!! && dataList[2]?.counter?.counterStr != "0") {
                    dataList[2]?.counter?.counterStr?.let { dynamicAction?.setRightLayoutNotification(it) }
                }
                dynamicAction?.findViewById<LinearLayout>(R.id.holder_tokomember)?.setOnClickListener {
                    dataList[2]?.cta?.let {
                        hideNotification(2)
                        dynamicAction?.setRightLayoutClickListener(it.appLink, it.text)
                    }
                }
                dynamicAction?.setVisibilityDividerTwo(View.VISIBLE)
            }
        }
    }

    override fun renderSections(sections: List<SectionContent>) {
        if (sections == null) { //TODO hide all section container
            return
        }
        val exploreSectionItem: MutableList<SectionContent> = ArrayList()
        for (sectionContent in sections) {
            when (sectionContent.layoutType) {
                CommonConstant.SectionLayoutType.TICKER -> renderTicker(sectionContent)
                CommonConstant.SectionLayoutType.CATEGORY -> renderCategory(sectionContent)
                CommonConstant.SectionLayoutType.COUPON, CommonConstant.SectionLayoutType.CATALOG, CommonConstant.SectionLayoutType.BANNER, CommonConstant.SectionLayoutType.TOPADS -> exploreSectionItem.add(sectionContent)
                else -> {
                }
            }
        }
        //init explore and kupon-saya tab
        renderExploreSectionTab(exploreSectionItem)
    }

    override fun renderExploreSectionTab(sections: List<SectionContent>) {
        if (sections.isEmpty()) { //TODO hide tab or show empty box
        }
        mExploreSectionPagerAdapter = ExploreSectionPagerAdapter(activityContext, mPresenter, sections)
        mExploreSectionPagerAdapter?.setRefreshing(false)
        mPagerPromos?.adapter = mExploreSectionPagerAdapter
    }

    override fun onResume() {
        super.onResume()
        AnalyticsTrackerUtil.sendScreenEvent(activity, screenName)
    }

    override fun showLoading() {
        mContainerMain?.displayedChild = CONTAINER_LOADER
    }

    override fun hideLoading() {
        mContainerMain?.displayedChild = CONTAINER_DATA
    }

    override fun getAppContext(): Context {
        return activity!!
    }

    override fun getActivityContext(): Context {
        return activity!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TokoPointsHomeNewActivity) appBarCollapseListener = context
    }

    override fun getScreenName(): String {
        return AnalyticsTrackerUtil.ScreenKeys.HOME_PAGE_SCREEN_NAME
    }

    override fun initInjector() {
        getComponent(TokopointBundleComponent::class.java).inject(this)
    }

    override fun onClick(source: View) {
        if (source.id == R.id.text_membership_label || source.id == R.id.img_egg || source.id == R.id.text_membership_value) {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW_TITLE, getString(R.string.tp_label_membership), CommonConstant.WebLink.MEMBERSHIP)
            AnalyticsTrackerUtil.sendEvent(context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_STATUS_MEMBERSHIP,
                    mValueMembershipDescription)
        } else if (source.id == R.id.text_failed_action) {
            mPresenter.getTokoPointDetail()
        }
    }

    override fun onDestroyView() {
        if (mExploreSectionPagerAdapter != null) {
            mExploreSectionPagerAdapter!!.onDestroyView()
        }
        super.onDestroyView()
    }

    inline fun View.doOnLayout(crossinline action: (view: View) -> Unit) {
        if (ViewCompat.isLaidOut(this) && !isLayoutRequested) {
            action(this)
        } else {
            doOnNextLayout {
                action(it)
            }
        }
    }

    inline fun View.doOnNextLayout(crossinline action: (view: View) -> Unit) {
        addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                    view: View,
                    left: Int,
                    top: Int,
                    right: Int,
                    bottom: Int,
                    oldLeft: Int,
                    oldTop: Int,
                    oldRight: Int,
                    oldBottom: Int
            ) {
                view.removeOnLayoutChangeListener(this)
                action(view)
            }
        })
    }

    companion object {
        private const val CONTAINER_LOADER = 0
        private const val CONTAINER_DATA = 1
        private const val CONTAINER_ERROR = 2

        fun newInstance(): TokoPointsHomeFragmentNew {
            return TokoPointsHomeFragmentNew()
        }

        fun getStatusBarHeight(context: Context?): Int {
            var height = 0
            val resId = context!!.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resId > 0) {
                height = context.resources.getDimensionPixelSize(resId)
            }
            return height
        }

        fun setWindowFlag(activity: Activity?, bits: Int, on: Boolean) {
            val win = activity!!.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                HOME_TOKOPOINT_PLT_PREPARE_METRICS,
                HOME_TOKOPOINT_PLT_NETWORK_METRICS,
                HOME_TOKOPOINT_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(HOME_TOKOPOINT_PLT)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        pageLoadTimePerformanceMonitoring = null
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()

    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()

    }

    private fun setOnRecyclerViewLayoutReady() {
        mRvDynamicLinks?.viewTreeObserver
                ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (pageLoadTimePerformanceMonitoring != null) {
                            stopRenderPerformanceMonitoring()
                            stopPerformanceMonitoring()
                        }
                        pageLoadTimePerformanceMonitoring = null
                        mRvDynamicLinks?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    }
                })
    }
}