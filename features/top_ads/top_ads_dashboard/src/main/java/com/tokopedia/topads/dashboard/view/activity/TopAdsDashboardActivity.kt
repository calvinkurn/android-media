package com.tokopedia.topads.dashboard.view.activity

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.AppUtil
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.seller_migration_common.presentation.fragment.bottomsheet.SellerMigrationCommunicationBottomSheet
import com.tokopedia.seller_migration_common.presentation.model.CommunicationInfo
import com.tokopedia.seller_migration_common.presentation.util.initializeSellerMigrationCommunicationTicker
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.getPdpAppLink
import com.tokopedia.topads.common.isFromPdpSellerMigration
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.TopAdsDashboardTracking
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.AUTO_ADS_DISABLED
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EXPIRE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.FIRST_LAUNCH
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.SELLER_CREATE_FORM_PATH
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.SELLER_PACKAGENAME
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.BerandaTabFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsRecommendationFragment
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_dash_activity_base_layout.*
import javax.inject.Inject

/**
 * Created by hadi.putra on 23/04/2018.
 */

private const val CLICK_BUAT_IKLAN = "click - tambah iklan"

class TopAdsDashboardActivity : BaseActivity(), HasComponent<TopAdsDashboardComponent>, TopAdsProductIklanFragment.AppBarAction, BerandaTabFragment.GoToInsight {

    private var tracker: TopAdsDashboardTracking? = null
    private val INSIGHT_PAGE = 2

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    private val sellerMigrationStaticCommunicationBottomSheet by lazy {
        SellerMigrationCommunicationBottomSheet.createInstance(
                this,
                CommunicationInfo.TopAds,
                screenName.orEmpty(),
                userSession.userId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
        topAdsDashboardPresenter.getShopListHiddenTrial(resources)
        setContentView(R.layout.topads_dash_activity_base_layout)
        renderTabAndViewPager()
        header_toolbar?.actionTextView?.setOnClickListener {
            if (GlobalConfig.isSellerApp()) {
                val intent = RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_CREATE_CHOOSER)
                startActivityForResult(intent, AUTO_ADS_DISABLED)
            } else {
                openCreateForm()
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
        createAd.setOnClickListener {
            if (GlobalConfig.isSellerApp()) {
                val intent = RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_CREATE_CHOOSER)
                startActivityForResult(intent, AUTO_ADS_DISABLED)
            } else {
                openCreateForm()
            }
        }
        createAd?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val height = createAd?.measuredHeight
        view_pager?.setPadding(0, 0, 0, height ?: 0)

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    bottom.visibility = View.VISIBLE
                    createAd?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    val height = createAd?.measuredHeight
                    view_pager?.setPadding(0, 0, 0, height ?: 0)
                } else {
                    bottom.visibility = View.GONE
                    view_pager?.setPadding(0, 0, 0, 0)

                }
            }
        })
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsOpenScreenEvent()

        if (!GlobalConfig.isSellerApp()) {
            setupSellerMigrationTicker()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTO_ADS_DISABLED) {
            if (resultCode == Activity.RESULT_OK)
                renderTabAndViewPager()
        }
    }

    private fun renderTabAndViewPager() {
        view_pager.adapter = getViewPagerAdapter()
        view_pager.offscreenPageLimit = 3
        view_pager.currentItem = 0
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
        tab_layout.setupWithViewPager(view_pager)
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
        val list: MutableList<FragmentTabItem> = mutableListOf()
        list.add(FragmentTabItem(resources.getString(R.string.topads_dash_beranda), BerandaTabFragment.createInstance()))
        list.add(FragmentTabItem(resources.getString(R.string.topads_dash_iklan_produck), TopAdsProductIklanFragment.createInstance()))
        list.add(FragmentTabItem(resources.getString(R.string.topads_dash_recommend), TopAdsRecommendationFragment.createInstance()))
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

    private fun openCreateForm() {
        if (AppUtil.isSellerInstalled(this)) {
            val intent = RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_CREATE_CHOOSER)
            intent.component = ComponentName(SELLER_PACKAGENAME, SELLER_CREATE_FORM_PATH)
            startActivity(intent)
        } else {
            RouteManager.route(this, ApplinkConstInternalMechant.MERCHANT_REDIRECT_CREATE_SHOP)
        }
    }

    private fun setupSellerMigrationTicker() {
        initializeSellerMigrationCommunicationTicker(sellerMigrationStaticCommunicationBottomSheet, ticker_seller_migration_topads, CommunicationInfo.TopAds)
    }

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
}
