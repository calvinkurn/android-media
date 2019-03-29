package com.tokopedia.topads.dashboard.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar

import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.showcase.ShowCaseContentPosition
import com.tokopedia.showcase.ShowCaseDialog
import com.tokopedia.showcase.ShowCaseObject
import com.tokopedia.topads.common.data.util.ApplinkUtil
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.TopAdsDashboardRouter
import com.tokopedia.topads.dashboard.TopAdsDashboardTracking
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.utils.ShowCaseDialogFactory
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashboardFragment
import com.tokopedia.topads.common.view.listener.OneUseGlobalLayoutListener

import java.util.ArrayList

/**
 * Created by hadi.putra on 23/04/2018.
 */
class TopAdsDashboardActivity : BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent>, TopAdsDashboardFragment.Callback {

    internal lateinit var showCaseDialog: ShowCaseDialog
    internal var tracker: TopAdsDashboardTracking? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (application is TopAdsDashboardRouter) {
            tracker = TopAdsDashboardTracking(application as TopAdsDashboardRouter)
        }
        actionSendAnalyticsIfFromPushNotif()
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
        if (isTaskRoot) {
            if (intent.extras?.getBoolean(TopAdsDashboardConstant.EXTRA_APPLINK_FROM_PUSH, false) == true) {
                val homeIntent = (application as TopAdsDashboardRouter).getHomeIntent(this)
                startActivity(homeIntent)
                finish()
            } else
            //coming from deeplink
                if (application is TopAdsDashboardRouter) {
                    val router = application as TopAdsDashboardRouter
                    try {
                        val intent = Intent(this, router.getHomeClass(this))
                        this.startActivity(intent)
                        this.finish()
                        return
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    }

                }
        }
        super.onBackPressed()
    }

    override fun getTagFragment(): String {
        return TAG
    }

    override fun startShowCase() {
        val showCaseTag = TopAdsDashboardActivity::class.java.name

        val fragment = supportFragmentManager.findFragmentByTag(TAG) as TopAdsDashboardFragment
                ?: return

        showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase()
        showCaseDialog.setShowCaseStepListener { previousStep, nextStep, showCaseObject -> false }

        val showCaseList = ArrayList<ShowCaseObject>()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        if (toolbar.height > 0) {
            val height = toolbar.height
            val width = toolbar.width

            if (fragment.shopInfoLayout != null) {
                showCaseList.add(ShowCaseObject(fragment.shopInfoLayout,
                        getString(R.string.topads_showcase_home_title_3),
                        getString(R.string.topads_showcase_home_desc_3),
                        ShowCaseContentPosition.UNDEFINED,
                        R.color.white, fragment.scrollView))
            }
            if (fragment.isContentVisible) {
                if (fragment.contentStatisticsView != null) {
                    showCaseList.add(ShowCaseObject(fragment.contentStatisticsView,
                            getString(R.string.topads_showcase_home_title_7),
                            getString(R.string.topads_showcase_home_desc_5),
                            ShowCaseContentPosition.UNDEFINED,
                            R.color.white, fragment.scrollView))
                }

                if (fragment.groupSummaryLabelView != null) {
                    showCaseList.add(ShowCaseObject(fragment.groupSummaryLabelView,
                            getString(R.string.topads_showcase_home_title_8),
                            getString(R.string.topads_showcase_home_desc_8),
                            ShowCaseContentPosition.UNDEFINED,
                            R.color.white, fragment.scrollView))
                }
                if (fragment.viewGroupPromo != null) {
                    showCaseList.add(ShowCaseObject(fragment.viewGroupPromo,
                            getString(R.string.topads_showcase_home_title_1),
                            getString(R.string.topads_showcase_home_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            R.color.white, fragment.scrollView))
                }
            }

            if (fragment.buttonAddPromo != null) {
                showCaseList.add(ShowCaseObject(fragment.buttonAddPromo,
                        getString(R.string.topads_showcase_home_title_6),
                        getString(R.string.topads_showcase_home_desc_6),
                        ShowCaseContentPosition.UNDEFINED,
                        R.color.white))
            }

            showCaseList.add(
                    ShowCaseObject(
                            findViewById(android.R.id.content),
                            getString(R.string.topads_showcase_help),
                            getString(R.string.topads_showcase_detail_help),
                            ShowCaseContentPosition.UNDEFINED,
                            Color.WHITE)
                            .withCustomTarget(intArrayOf(width - (height * 0.8).toInt(), 0, width, height)))

            showCaseDialog.show(this, showCaseTag, showCaseList)
        } else {
            toolbar.viewTreeObserver.addOnGlobalLayoutListener(OneUseGlobalLayoutListener(
                    toolbar,
                    OneUseGlobalLayoutListener.OnGlobalLayoutListener { startShowCase() }
            ))
        }


    }

    override fun getNewFragment(): Fragment {
        return TopAdsDashboardFragment.createInstance()
    }

    companion object {
        val TAG = TopAdsDashboardActivity::class.java.simpleName

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, TopAdsDashboardActivity::class.java)
        }
    }

    object DeepLinkIntents {
        @DeepLink(ApplinkConst.SellerApp.TOPADS_DASHBOARD)
        @JvmStatic
        fun getCallingApplinkIntent(context: Context, extras: Bundle): Intent {
            if (GlobalConfig.isSellerApp()) {
                val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
                return getCallingIntent(context)
                        .setData(uri.build())
                        .putExtras(extras)
            } else {
                return ApplinkUtil.getSellerAppApplinkIntent(context, extras)
            }
        }
    }
}
