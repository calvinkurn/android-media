package com.tokopedia.topads.dashboard.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.PARAM_AUTOADS_BUDGET
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.TOPADS_MOVE_TO_DASHBOARD
import com.tokopedia.topads.common.getPdpAppLink
import com.tokopedia.topads.common.isFromPdpSellerMigration
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.TopAdsDashboardTracking
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.AUTO_ADS_DISABLED
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_0
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_1
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_2
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_3
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EXPIRE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.FIRST_LAUNCH
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.IS_CHANGED
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PARAM_DAILY_BUDGET
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PARAM_INSIGHT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PARAM_KEYWORD
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PARAM_PRODUCT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PARAM_PRODUCT_AD
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PARAM_SHOP_AD
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PARAM_INSIGHT_TAB
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PARAM_TAB
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.BerandaTabFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsRecommendationFragment
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.dashboard.view.sheet.NoProductBottomSheet
import com.tokopedia.topads.headline.view.fragment.TopAdsHeadlineBaseFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_dash_activity_base_layout.*
import javax.inject.Inject

/**
 * Created by hadi.putra on 23/04/2018.
 */

private const val CLICK_BUAT_IKLAN = "click - tambah iklan"
private const val VIEW_BUAT_IKLAN = "view - tambah iklan"
private const val CLICK_IKLAN_TOKO = " click - iklan toko"
private const val VIEW_HEADLINE_EVENT = "view - iklan toko"
class TopAdsDashboardActivity : BaseActivity(), HasComponent<TopAdsDashboardComponent>,
        TopAdsProductIklanFragment.AppBarAction, BerandaTabFragment.GoToInsight,
        TopAdsProductIklanFragment.AdInfo, TopAdsHeadlineBaseFragment.AppBarActionHeadline {

    private var tracker: TopAdsDashboardTracking? = null
    private val INSIGHT_PAGE = 3
    private val HEADLINE_ADS_TAB = 2
    private var adType = "-1"
    private var isNoProduct = false
    var redirectToTab = 0
    var redirectToTabInsight = 0

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        topAdsDashboardPresenter.getShopListHiddenTrial(resources)
        setContentView(R.layout.topads_dash_activity_base_layout)
        renderTabAndViewPager()
        header_toolbar?.actionTextView?.setOnClickListener {
            if (GlobalConfig.isSellerApp()) {
                navigateToAdTypeSelection()
            }
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BUAT_IKLAN, "")
        }
        header_toolbar?.setNavigationOnClickListener {
            onBackPressed()
        }
        tracker = TopAdsDashboardTracking()
        actionSendAnalyticsIfFromPushNotif()

        topAdsDashboardPresenter.isShopWhiteListed.observe(this, Observer {
            if (it) {
                topAdsDashboardPresenter.getExpiryDate(resources)
            }
        })
        topAdsDashboardPresenter.expiryDateHiddenTrial.observe(this, Observer {
            val intent = Intent(this, HiddenTrialActivity::class.java)
            intent.putExtra(EXPIRE, it)
            startActivity(intent)
            finish()
        })

        multiActionBtn?.setOnClickListener {
            if (tab_layout?.getUnifyTabLayout()?.selectedTabPosition == 0)
                navigateToAdTypeSelection()
            if (tab_layout?.getUnifyTabLayout()?.selectedTabPosition == 3) {
                val fragments = (view_pager?.adapter as TopAdsDashboardBasePagerAdapter).getList()
                for (frag in fragments) {
                    when (frag.fragment) {
                        is TopAdsRecommendationFragment -> {
                            (frag.fragment as TopAdsRecommendationFragment).setClick()
                        }
                    }
                }
            }

        }
        setPadding()
        tab_layout?.getUnifyTabLayout()?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    CONST_0 -> {
                        bottom.visibility = View.VISIBLE
                        multiActionBtn.buttonSize = UnifyButton.Size.LARGE
                        multiActionBtn?.text = getString(R.string.topads_dash_button_submit_beranda)
                        setPadding()
                    }
                    INSIGHT_PAGE -> {
                        bottom.visibility = View.GONE
                        multiActionBtn.buttonSize = UnifyButton.Size.MEDIUM
                        multiActionBtn?.text = getString(com.tokopedia.topads.common.R.string.topads_iklankan_button)
                        checkVisibility()
                    }
                    HEADLINE_ADS_TAB -> {
                        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(CLICK_IKLAN_TOKO, "{${userSession.shopId}", userSession.userId)
                        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsViewEvent(VIEW_HEADLINE_EVENT, "{${userSession.shopId}}", userSession.userId)
                    }
                    else -> {
                        bottom.visibility = View.GONE
                        view_pager?.setPadding(0, 0, 0, 0)
                    }
                }
            }
        })
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsOpenScreenEvent()
        setToast()
    }

    private fun setToast() {
        val bundle = intent.extras
        if (bundle?.getInt(TopAdsCommonConstant.TOPADS_AUTOADS_BUDGET_UPDATED, 0) == PARAM_AUTOADS_BUDGET) {
            Toaster.build(this.findViewById(android.R.id.content), getString(R.string.topads_dashboard_updated_daily_budget), TopAdsDashboardConstant.TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL).show()
        }
    }

    private fun checkVisibility() {
        val fragments = (view_pager?.adapter as TopAdsDashboardBasePagerAdapter).getList()
        for (frag in fragments) {
            when (frag.fragment) {
                is TopAdsRecommendationFragment -> {
                    (frag.fragment as TopAdsRecommendationFragment).checkButtonVisibility()
                }
            }
        }
    }

    fun hideButton(toHide: Boolean) {
        if (multiActionBtn?.text?.equals(getString(com.tokopedia.topads.common.R.string.topads_iklankan_button)) == true) {
            bottom.visibility = if (toHide) View.GONE else View.VISIBLE
            if (toHide) {
                view_pager?.setPadding(0, 0, 0, 0)
            } else {
                setPadding()
            }
        }
    }

    private fun setPadding() {
        multiActionBtn?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val heightButton = multiActionBtn?.measuredHeight
        view_pager?.setPadding(0, 0, 0, heightButton ?: 0)
    }

    fun enableRecommButton(isEnable: Boolean) {
        multiActionBtn.isEnabled = isEnable
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTO_ADS_DISABLED || requestCode == TopAdsDashboardConstant.EDIT_HEADLINE_REQUEST_CODE || intent.getBooleanExtra(IS_CHANGED, false)) {
            if (resultCode == Activity.RESULT_OK)
                renderTabAndViewPager()
        }
    }

    private fun renderTabAndViewPager() {
        val bundle = intent.extras
        redirectToTab = bundle?.getInt(TOPADS_MOVE_TO_DASHBOARD, 0) ?: 0
        if (intent.extras?.get(PARAM_TAB) != null) {
            redirectToTab = when (intent.extras?.get(PARAM_TAB)) {
                PARAM_INSIGHT -> CONST_3
                PARAM_PRODUCT_AD -> CONST_1
                PARAM_SHOP_AD -> CONST_2
                else -> CONST_0
            }
        }
        if (redirectToTab == CONST_3) {
            redirectToTabInsight = when (intent.extras?.get(PARAM_INSIGHT_TAB)) {
                PARAM_DAILY_BUDGET -> CONST_1
                PARAM_KEYWORD -> CONST_2
                PARAM_PRODUCT -> CONST_0
                else -> CONST_0
            }
        }
        view_pager.adapter = getViewPagerAdapter()
        view_pager.offscreenPageLimit = 3
        tab_layout?.getUnifyTabLayout()?.getTabAt(redirectToTab)?.select()
        view_pager.currentItem = redirectToTab
        if (view_pager.currentItem != 0) {
            bottom.gone()
        }
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
                if (position == INSIGHT_PAGE) {
                    if (sharedPref.getBoolean(FIRST_LAUNCH, true)) {
                        showFirstTimeDialog(this@TopAdsDashboardActivity)
                        with(sharedPref.edit()) {
                            putBoolean(FIRST_LAUNCH, false)
                            commit()
                        }
                    }
                }
            }
        })
        tab_layout?.setupWithViewPager(view_pager)
    }

    private fun showFirstTimeDialog(context: Context) {
        val dialog = DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ILLUSTRATION)
        dialog.setImageDrawable(R.drawable.topads_insight_dialog)
        dialog.setDescription(context.getString(R.string.topads_dash_insight_dialog_desc))
        dialog.setTitle(context.getString(R.string.topads_dash_insight_dialog_title))
        dialog.setPrimaryCTAText(context.getString(R.string.topads_dash_insight_dialog_btn))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun getViewPagerAdapter(): PagerAdapter {
        multiActionBtn?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val btnHeight = multiActionBtn?.measuredHeight
        val list: MutableList<FragmentTabItem> = mutableListOf()
        tab_layout?.getUnifyTabLayout()?.removeAllTabs()
        tab_layout?.addNewTab(getString(R.string.topads_dash_beranda))
        tab_layout?.addNewTab(getString(R.string.topads_dash_iklan_produck))
        tab_layout?.addNewTab(getString(R.string.topads_dash_headline_title))
        tab_layout?.addNewTab(getString(R.string.topads_dash_recommend))
        tab_layout?.customTabMode = TabLayout.MODE_SCROLLABLE
        list.add(FragmentTabItem(resources.getString(R.string.topads_dash_beranda), BerandaTabFragment.createInstance()))
        list.add(FragmentTabItem(resources.getString(R.string.topads_dash_iklan_produck), TopAdsProductIklanFragment.createInstance()))
        list.add(FragmentTabItem(resources.getString(R.string.topads_dash_headline_title), TopAdsHeadlineBaseFragment.createInstance()))
        list.add(FragmentTabItem(resources.getString(R.string.topads_dash_recommend), TopAdsRecommendationFragment.createInstance(btnHeight, redirectToTabInsight)))
        val pagerAdapter = TopAdsDashboardBasePagerAdapter(supportFragmentManager, 0)
        pagerAdapter.setList(list)
        return pagerAdapter
    }

    private fun initInjector() {
        DaggerTopAdsDashboardComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent).build().inject(this)
    }


    private fun actionSendAnalyticsIfFromPushNotif() {
        val intent = intent
        if (intent != null && intent.hasExtra(TopAdsDashboardConstant.EXTRA_FROM_PUSH)) {
            tracker?.run {
                if (intent.getBooleanExtra(TopAdsDashboardConstant.EXTRA_FROM_PUSH, false)) {
                    eventOpenTopadsPushNotification(TopAdsDashboardConstant.EXTRA_LABEL)
                }
            }
        }
    }

    override fun getComponent(): TopAdsDashboardComponent = DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

    override fun onBackPressed() {
        if (!moveToPdpIfFromPdpSellerMigration() && isTaskRoot) {
            val applinkConst = ApplinkConst.HOME
            if (intent.extras?.getBoolean(TopAdsDashboardConstant.EXTRA_APPLINK_FROM_PUSH, false) == true) {
                val homeIntent = RouteManager.getIntent(this, applinkConst)
                startActivity(homeIntent)
                finish()
            } else
            //coming from deeplink
                try {
                    this.startActivity(RouteManager.getIntent(this, applinkConst))
                    this.finish()
                    return
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
        }
        super.onBackPressed()
    }

    private fun moveToPdpIfFromPdpSellerMigration(): Boolean {
        if (isFromPdpSellerMigration(intent?.extras)) {
            val pdpAppLink = getPdpAppLink(intent?.extras)
            if (pdpAppLink.isNotEmpty()) {
                return RouteManager.route(this, pdpAppLink)
            }
        }

        return false
    }

    fun getAdInfo(): String? = adType

    override fun setAppBarState(state: TopAdsProductIklanFragment.State?) {
        if (state == TopAdsProductIklanFragment.State.COLLAPSED) {
            app_bar_layout.setExpanded(false)
        } else if (state == TopAdsProductIklanFragment.State.EXPANDED) {
            app_bar_layout.setExpanded(true)
        }
    }

    override fun gotToInsights() {
        view_pager?.currentItem = INSIGHT_PAGE
    }

    override fun adInfo(adInfo: String) {
        adType = adInfo
        val fragments = (view_pager?.adapter as TopAdsDashboardBasePagerAdapter).getList()
        for (frag in fragments) {
            when (frag.fragment) {
                is BerandaTabFragment -> {
                    (frag.fragment as BerandaTabFragment).loadStatisticsData()
                }
            }
        }
    }

    override fun onNoProduct(isNoProduct: Boolean) {
        this.isNoProduct = isNoProduct
    }

    override fun setAppBarStateHeadline(state: TopAdsProductIklanFragment.State?) {
        if (state == TopAdsProductIklanFragment.State.COLLAPSED) {
            app_bar_layout.setExpanded(false)
        } else if (state == TopAdsProductIklanFragment.State.EXPANDED) {
            app_bar_layout.setExpanded(true)
        }
    }

    private fun navigateToAdTypeSelection() {
        if (isNoProduct) {
            val noProductBottomSheet = NoProductBottomSheet.newInstance()
            noProductBottomSheet.show(supportFragmentManager, "")
        } else {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineCreatFormEvent(VIEW_BUAT_IKLAN, "{${userSession.shopId}}", userSession.userId)
            val intent = RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_ADS_SELECTION)
            startActivityForResult(intent, AUTO_ADS_DISABLED)
        }
    }
}
