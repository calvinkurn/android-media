package com.tokopedia.talk.feature.inbox.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringConstants
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.inbox.analytics.TalkInboxTracking
import com.tokopedia.talk.feature.inbox.analytics.TalkInboxTrackingConstants
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.inbox.presentation.fragment.TalkInboxContainerFragment
import com.tokopedia.talk.feature.inbox.presentation.fragment.TalkInboxFragment
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TalkInboxActivity : BaseSimpleActivity(), TalkPerformanceMonitoringListener, HasComponent<TalkComponent> {

    companion object {
        fun createIntent(context: Context) : Intent {
            return Intent(context, TalkInboxActivity::class.java)
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var talkInboxTracking: TalkInboxTracking

    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        startPerformanceMonitoring()
    }

    override fun getNewFragment(): Fragment? {
        if(GlobalConfig.isSellerApp()) {
            return TalkInboxFragment.createNewInstance(TalkInboxTab.TalkShopInboxTab())
        }
        if(userSession.hasShop()) {
            return TalkInboxContainerFragment.createNewInstance()
        }
        return TalkInboxFragment.createNewInstance(TalkInboxTab.TalkBuyerInboxTab())
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                TalkPerformanceMonitoringConstants.TALK_INBOX_PLT_PREPARE_METRICS,
                TalkPerformanceMonitoringConstants.TALK_INBOX_PLT_NETWORK_METRICS,
                TalkPerformanceMonitoringConstants.TALK_INBOX_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(TalkPerformanceMonitoringConstants.TALK_INBOX_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.stopMonitoring()
        }
        pageLoadTimePerformanceMonitoring = null
    }

    override fun startPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.startPreparePagePerformanceMonitoring()
        }
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.stopPreparePagePerformanceMonitoring()
        }
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.startNetworkRequestPerformanceMonitoring()
        }
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.stopNetworkRequestPerformanceMonitoring()
        }
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.startRenderPerformanceMonitoring()
        }
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.stopRenderPerformanceMonitoring()
        }
    }

    override fun getScreenName(): String {
        return TalkInboxTrackingConstants.SCREEN_NAME
    }

    override fun sendScreenAnalytics() {
        talkInboxTracking.openScreen(screenName)
    }

    override fun onBackPressed() {
        if(isTaskRoot) {
            goToSellerHome()
        }
        super.onBackPressed()
    }

    private fun goToSellerHome() {
        RouteManager.route(this, ApplinkConstInternalSellerapp.SELLER_HOME)
    }
}