package com.tokopedia.review.feature.inbox.presentation

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.config.GlobalConfig
import com.tokopedia.header.HeaderUnify
import com.tokopedia.review.R
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.common.analytics.ReviewSellerPerformanceMonitoringListener
import com.tokopedia.review.common.presentation.listener.OnTabChangeListener
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.common.util.ReviewUtil.DptoPx
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTracking
import com.tokopedia.review.feature.inbox.buyerreview.analytics.ReputationTrackingConstant
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.SectionsPagerAdapter
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationFragment
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.GlobalMainTabSelectedListener
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationListener
import com.tokopedia.review.feature.inbox.di.DaggerInboxReputationComponent
import com.tokopedia.review.feature.inbox.di.InboxReputationComponent
import com.tokopedia.review.feature.inbox.presentation.ReviewInboxActivity.Companion.createNewInstance
import com.tokopedia.review.feature.inboxreview.presentation.fragment.InboxReviewFragment
import com.tokopedia.review.feature.reputationhistory.view.fragment.SellerReputationPenaltyFragment.Companion.newInstance
import com.tokopedia.review.feature.reviewlist.view.fragment.RatingProductFragment
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by nisie on 8/10/17.
 */
class InboxReputationActivity : BaseActivity(), HasComponent<InboxReputationComponent?>,
    InboxReputationListener, ReviewSellerPerformanceMonitoringListener {

    companion object {
        const val GO_TO_REPUTATION_HISTORY = "GO_TO_REPUTATION_HISTORY"
        const val GO_TO_BUYER_REVIEW = "GO_TO_BUYER_REVIEW"
        const val IS_DIRECTLY_GO_TO_RATING = "is_directly_go_to_rating"
        const val TAB_BUYER_REVIEW = 3
        const val TAB_SELLER_REPUTATION_HISTORY = 2
        const val TAB_SELLER_INBOX_REVIEW = 1
        const val NOTIFICATION = 100
        private const val MARGIN_TAB = 8
        private const val MARGIN_START_END_TAB = 16
        private const val SELLER_INBOX_REVIEW_TAB = "inbox-ulasan"

        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, InboxReputationActivity::class.java)
        }
    }

    private var reviewSellerFragment: Fragment? = null
    private var inboxReviewFragment: Fragment? = null
    private var sellerReputationPenaltyFragment: Fragment? = null
    private var viewPager: ViewPager? = null
    private var sectionAdapter: PagerAdapter? = null
    private var toolbar: HeaderUnify? = null
    var indicator: TabsUnify? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var reputationTracking: ReputationTracking

    private var goToReputationHistory = false
    private var goToBuyerReview = false
    private var canFireTracking = false
    private var isAppLinkProccessed = false
    private var pageLoadTimePerformance: PageLoadTimePerformanceInterface? = null
    private var tickerTitle: String? = null
    private var fragmentList: List<Fragment> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goToReputationHistory = intent.getBooleanExtra(GO_TO_REPUTATION_HISTORY, false)
        goToBuyerReview = intent.getBooleanExtra(GO_TO_BUYER_REVIEW, false)
        val tab = intent.data?.getQueryParameter(ReviewInboxConstants.PARAM_TAB)
        val source = intent.data?.getQueryParameter(ReviewInboxConstants.PARAM_SOURCE)
        canFireTracking = !goToReputationHistory
        component.inject(this)
        if (!GlobalConfig.isSellerApp()) {
            startActivity(createNewInstance(this, tab, source))
            finish()
        }
        startPerformanceMonitoring()
        setContentView(R.layout.activity_inbox_reputation)
        setupStatusBar()
        clearCacheIfFromNotification()
        initView(tab)
        setupTabViewpager(tab)
        openBuyerReview()
    }

    override fun updateTickerTitle(title: String) {
        tickerTitle = title
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformance = PageLoadTimePerformanceCallback(
            ReviewConstants.RATING_PRODUCT_PLT_PREPARE_METRICS,
            ReviewConstants.RATING_PRODUCT_PLT_NETWORK_METRICS,
            ReviewConstants.RATING_PRODUCT_PLT_RENDER_METRICS,
            0,
            0,
            0,
            0,
            null
        )
        pageLoadTimePerformance?.startMonitoring(ReviewConstants.RATING_PRODUCT_TRACE)
        pageLoadTimePerformance?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformance?.stopMonitoring()
        pageLoadTimePerformance = null
    }

    override fun startPreparePagePerformanceMonitoring() {
        pageLoadTimePerformance?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformance?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformance?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformance?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformance?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformance?.stopRenderPerformanceMonitoring()
    }

    override fun getComponent(): InboxReputationComponent {
        return DaggerInboxReputationComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            RouteManager.route(this, ApplinkConst.SellerApp.SELLER_APP_HOME)
            finish()
        }
        tickerTitle = null
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun getFragmentList(): List<Fragment> {
        return fragmentList
    }

    private fun populateFragmentList() {
        val fragmentList = mutableListOf<Fragment>()
        reviewSellerFragment?.let { fragmentList.add(it) }
        inboxReviewFragment?.let { fragmentList.add(it) }
        fragmentList.add(InboxReputationFragment.createInstance(TAB_BUYER_REVIEW))
        sellerReputationPenaltyFragment?.let { fragmentList.add(it) }
        this.fragmentList = fragmentList
    }

    private fun initView(tab: String?) {
        viewPager = findViewById(R.id.pager_reputation)
        indicator = findViewById(R.id.indicator_unify)
        toolbar = findViewById(R.id.headerInboxReputation)
        indicator?.getUnifyTabLayout()?.clearOnTabSelectedListeners()
        window.decorView.setBackgroundColor(
            ContextCompat.getColor(
                this,
                com.tokopedia.unifyprinciples.R.color.Unify_N0
            )
        )
        setupToolbar()
        reviewSellerFragment = RatingProductFragment.createInstance()
        val reviewSellerBundle = Bundle()
        if (isExistParamTab(tab)) {
            reviewSellerBundle.putBoolean(IS_DIRECTLY_GO_TO_RATING, goToReputationHistory)
        } else {
            reviewSellerBundle.putBoolean(IS_DIRECTLY_GO_TO_RATING, !goToReputationHistory)
        }
        reviewSellerFragment?.arguments = reviewSellerBundle
        inboxReviewFragment = InboxReviewFragment.createInstance()
        sellerReputationPenaltyFragment = newInstance()
    }

    private fun setupTabViewpager(tab: String?) {
        indicator?.customTabMode = TabLayout.MODE_SCROLLABLE
        indicator?.customTabGravity = TabLayout.GRAVITY_FILL
        viewPager?.addOnPageChangeListener(TabLayoutOnPageChangeListener(indicator?.getUnifyTabLayout()))
        var selectedTabPosition = indicator?.tabLayout?.selectedTabPosition
        indicator?.getUnifyTabLayout()
            ?.addOnTabSelectedListener(object : GlobalMainTabSelectedListener(viewPager, this) {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    super.onTabSelected(tab)
                    val position = tab.position
                    if (position != selectedTabPosition) {
                        populateFragmentList()
                        for (i in fragmentList.indices) {
                            val fragment = fragmentList[i]
                            if (fragment is InboxReviewFragment) {
                                val onTabChangeListener = fragmentList[i] as OnTabChangeListener?
                                onTabChangeListener?.onTabChange(position)
                            }
                        }
                        selectedTabPosition = position
                    }
                    if (!canFireTracking) {
                        canFireTracking = true
                        return
                    }
                    tickerTitle?.let {
                        reputationTracking.onSuccessGetIncentiveOvoTracker(
                            it,
                            ReputationTrackingConstant.WAITING_REVIEWED
                        )
                    }
                }
            })
        setupTabName()
        sectionAdapter = SectionsPagerAdapter(
            supportFragmentManager,
            fragmentList,
            indicator?.getUnifyTabLayout()
        )
        viewPager?.offscreenPageLimit = fragmentList.size
        viewPager?.adapter = sectionAdapter
        if (GlobalConfig.isSellerApp()) {
            if (isExistParamTab(tab)) {
                if (tab == SELLER_INBOX_REVIEW_TAB) {
                    viewPager?.currentItem = TAB_SELLER_INBOX_REVIEW
                }
            }
        }
        if (goToReputationHistory) {
            viewPager?.currentItem = TAB_SELLER_REPUTATION_HISTORY
        }
        if (goToBuyerReview) {
            viewPager?.currentItem = TAB_BUYER_REVIEW
        }
        wrapTabIndicatorToTitle(
            indicator?.getUnifyTabLayout(), DptoPx(this, MARGIN_START_END_TAB)
                .toInt(), DptoPx(this, MARGIN_TAB).toInt()
        )
    }

    private fun isExistParamTab(tab: String?): Boolean {
        return tab != null && !tab.isEmpty()
    }

    private fun setupTabName() {
        if (reviewSellerFragment != null) {
            indicator?.addNewTab(getString(R.string.title_rating_product))
        }
        if (inboxReviewFragment != null) {
            indicator?.addNewTab(getString(R.string.title_review_inbox))
        }
        if (userSession.hasShop()) {
            indicator?.addNewTab(getString(R.string.title_tab_buyer_review))
        }
        if (sellerReputationPenaltyFragment != null) {
            indicator?.addNewTab(getString(R.string.title_reputation_history))
        }
    }

    private fun openBuyerReview() {
        if (!isAppLinkProccessed && intent != null && intent.data != null) {
            val featureName =
                intent.getStringExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME)
            if (featureName != null && featureName.isNotEmpty()) {
                isAppLinkProccessed = true
                val buyerReviewFragmentPosition = findBuyerReviewFragmentPosition()
                if (buyerReviewFragmentPosition != -1) {
                    viewPager?.currentItem = buyerReviewFragmentPosition
                }
            }
        }
    }

    private fun findBuyerReviewFragmentPosition(): Int {
        sectionAdapter?.let {
            for (i in 0 until it.count) {
                val fragment = (sectionAdapter as SectionsPagerAdapter?)?.getItem(i)
                if (fragment is InboxReputationFragment && fragment.tab == TAB_BUYER_REVIEW) return i
            }
        }
        return -1
    }

    private fun wrapTabIndicatorToTitle(
        tabLayout: TabLayout?,
        externalMargin: Int,
        internalMargin: Int
    ) {
        val tabStrip = tabLayout?.getChildAt(0)
        if (tabStrip is ViewGroup) {
            val childCount = tabStrip.childCount
            for (i in 0 until childCount) {
                val tabView = tabStrip.getChildAt(i)
                tabView.minimumWidth = 0
                tabView.setPadding(0, tabView.paddingTop, 0, tabView.paddingBottom)
                if (tabView.layoutParams is ViewGroup.MarginLayoutParams) {
                    val layoutParams = tabView.layoutParams as ViewGroup.MarginLayoutParams
                    when (i) {
                        0 -> {
                            settingMargin(layoutParams, externalMargin, internalMargin)
                        }
                        childCount - 1 -> {
                            settingMargin(layoutParams, internalMargin, externalMargin)
                        }
                        else -> {
                            settingMargin(layoutParams, internalMargin, internalMargin)
                        }
                    }
                }
            }
            tabLayout.requestLayout()
        }
    }

    private fun settingMargin(layoutParams: ViewGroup.MarginLayoutParams, start: Int, end: Int) {
        layoutParams.marginStart = start
        layoutParams.marginEnd = end
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.title_activity_reputation_review)
    }

    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor =
                ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }
    }

    private fun clearCacheIfFromNotification() {
        val intent = intent
        if (intent != null && intent.hasExtra(ReviewInboxConstants.EXTRA_FROM_PUSH)) {
            if (intent.getBooleanExtra(ReviewInboxConstants.EXTRA_FROM_PUSH, false)) {
                val notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(NOTIFICATION)
                LocalCacheHandler.clearCache(this, ReviewInboxConstants.GCM_NOTIFICATION)
            }
        }
    }
}