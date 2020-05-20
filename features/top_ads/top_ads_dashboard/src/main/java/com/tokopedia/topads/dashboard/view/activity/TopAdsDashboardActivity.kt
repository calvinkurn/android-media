package com.tokopedia.topads.dashboard.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.showcase.ShowCaseContentPosition
import com.tokopedia.showcase.ShowCaseDialog
import com.tokopedia.showcase.ShowCaseObject
import com.tokopedia.topads.auto.view.widget.AutoAdsWidgetView
import com.tokopedia.topads.auto.view.widget.ToasterAutoAds
import com.tokopedia.topads.common.view.listener.OneUseGlobalLayoutListener
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.TopAdsDashboardTracking
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.utils.ShowCaseDialogFactory
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.fragment.HiddenTrialFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashboardFragment
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import java.util.*
import javax.inject.Inject

/**
 * Created by hadi.putra on 23/04/2018.
 */
class TopAdsDashboardActivity : BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent>, TopAdsDashboardFragment.Callback {

    internal lateinit var showCaseDialog: ShowCaseDialog
    internal var tracker: TopAdsDashboardTracking? = null
    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
        tracker = TopAdsDashboardTracking()
        actionSendAnalyticsIfFromPushNotif()
        topAdsDashboardPresenter.getShopListHiddenTrial(resources)
        topAdsDashboardPresenter.getExpiryDate(resources)
        setFragment()
    }

    private fun setFragment() {
        topAdsDashboardPresenter.isShopWhiteListed.observe(this, androidx.lifecycle.Observer {

            if (it) {
                val fragment = HiddenTrialFragment.newInstance()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.parent_view, fragment, TAG)
                transaction.commit()

            } else {
                val fragment = TopAdsDashboardFragment.createInstance()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.parent_view, fragment, TAG)
                transaction.commit()

            }

        })
        topAdsDashboardPresenter.expiryDateHiddenTrial.observe(this, androidx.lifecycle.Observer {
            if (supportFragmentManager.findFragmentById(R.id.parent_view) is HiddenTrialFragment)
                (supportFragmentManager.findFragmentById(R.id.parent_view) as HiddenTrialFragment)
                        .getData(it.substring(0, it.length - 8))
        })
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
        if (isTaskRoot) {
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

    override fun getTagFragment(): String {
        return TAG
    }

    override fun startShowCase(isAutoAds : Boolean) {
        val showCaseTag = TopAdsDashboardActivity::class.java.name

        val fragment = supportFragmentManager.findFragmentByTag(TAG) as TopAdsDashboardFragment
                ?: return

        showCaseDialog = ShowCaseDialogFactory.createTkpdShowCase(this)
        showCaseDialog.setShowCaseStepListener { previousStep, nextStep, showCaseObject -> false }

        val showCaseList = ArrayList<ShowCaseObject>()
        val toolbar = findViewById<Toolbar>(com.tokopedia.product.manage.item.R.id.toolbar)
        if (toolbar.height > 0) {
            val height = toolbar.height
            val width = toolbar.width

            if (fragment.shopInfoLayout != null) {
                showCaseList.add(ShowCaseObject(fragment.shopInfoLayout,
                        getString(R.string.topads_showcase_home_title_3),
                        getString(R.string.topads_showcase_home_desc_3),
                        ShowCaseContentPosition.UNDEFINED,
                        com.tokopedia.topads.auto.R.color.white, fragment.scrollView))
            }
            if (fragment.isContentVisible) {
                if (fragment.contentStatisticsView != null) {
                    showCaseList.add(ShowCaseObject(fragment.contentStatisticsView,
                            getString(R.string.topads_showcase_home_title_7),
                            getString(R.string.topads_showcase_home_desc_5),
                            ShowCaseContentPosition.UNDEFINED,
                            com.tokopedia.topads.auto.R.color.white, fragment.scrollView))
                }

                if (fragment.groupSummaryLabelView != null && !isAutoAds) {
                    showCaseList.add(ShowCaseObject(fragment.groupSummaryLabelView,
                            getString(R.string.topads_showcase_home_title_8),
                            getString(R.string.topads_showcase_home_desc_8),
                            ShowCaseContentPosition.UNDEFINED,
                            com.tokopedia.topads.auto.R.color.white, fragment.scrollView))
                }
                if (fragment.viewGroupPromo != null && !isAutoAds) {
                    showCaseList.add(ShowCaseObject(fragment.viewGroupPromo,
                            getString(R.string.topads_showcase_home_title_1),
                            getString(R.string.topads_showcase_home_desc_1),
                            ShowCaseContentPosition.UNDEFINED,
                            com.tokopedia.topads.auto.R.color.white, fragment.scrollView))
                }
            }

            if (fragment.buttonAddPromo != null && !isAutoAds) {
                showCaseList.add(ShowCaseObject(fragment.buttonAddPromo,
                        getString(R.string.topads_showcase_home_title_6),
                        getString(R.string.topads_showcase_home_desc_6),
                        ShowCaseContentPosition.UNDEFINED,
                        com.tokopedia.topads.auto.R.color.white))
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
                    OneUseGlobalLayoutListener.OnGlobalLayoutListener { startShowCase(isAutoAds) }
            ))
        }


    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    companion object {
        val TAG = TopAdsDashboardActivity::class.java.simpleName
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AutoAdsWidgetView.REQUEST_KEY_AUTOADS_WIDGET && resultCode == Activity.RESULT_OK) {
            ToasterAutoAds.showClose(this, getString(com.tokopedia.topads.auto.R.string.toaster_inactive_success), onClick = {
                val fragment = (supportFragmentManager.findFragmentByTag(TAG) as TopAdsDashboardFragment)
                fragment.loadData()
                fragment.loadAutoAds()
            })
        }
    }
}
