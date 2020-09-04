package com.tokopedia.talk.feature.reply.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringConstants.TALK_REPLY_PLT_NETWORK_METRICS
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringConstants.TALK_REPLY_PLT_PREPARE_METRICS
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringConstants.TALK_REPLY_PLT_RENDER_METRICS
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringConstants.TALK_REPLY_TRACE
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reply.presentation.fragment.TalkReplyFragment
import com.tokopedia.talk_old.talkdetails.view.activity.TalkDetailsActivity

class TalkReplyActivity : BaseSimpleActivity(), HasComponent<TalkComponent>, TalkPerformanceMonitoringListener {

    private var questionId = ""
    private var shopId = ""
    private var source = ""
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null
    private var talkReplyFragment: TalkReplyFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromAppLink()
        if(FirebaseRemoteConfigImpl(applicationContext).getBoolean(TalkConstants.APP_DISABLE_NEW_TALK_REMOTE_CONFIG_KEY, false)) {
            val intent = TalkDetailsActivity.getCallingIntent(questionId, shopId, applicationContext, "")
            startActivity(intent)
            finish()
        }
        super.onCreate(savedInstanceState)
        startPerformanceMonitoring()
        setUpToolBar()
    }

    override fun getNewFragment(): Fragment? {
        talkReplyFragment = TalkReplyFragment.createNewInstance(questionId, shopId, source)
        return talkReplyFragment
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    private fun setUpToolBar() {
        supportActionBar?.elevation = TalkConstants.NO_SHADOW_ELEVATION
    }

    private fun getDataFromAppLink() {
        val uri = intent.data ?: return
        val questionIdString = uri.pathSegments[uri.pathSegments.size - 1] ?: return
        if (questionIdString.isNotEmpty()) {
            this.questionId = questionIdString
        }
        val shopId = uri.getQueryParameter(TalkConstants.PARAM_SHOP_ID) ?: ""
        if (shopId.isNotEmpty()) {
            this.shopId = shopId
        }
        val source = uri.getQueryParameter(TalkConstants.PARAM_SOURCE) ?: ""
        if (source.isNotEmpty()) {
            this.source = source
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                TALK_REPLY_PLT_PREPARE_METRICS,
                TALK_REPLY_PLT_NETWORK_METRICS,
                TALK_REPLY_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(TALK_REPLY_TRACE)
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

    override fun onBackPressed() {
        if(talkReplyFragment?.getDidUserWriteQuestion() == true) {
            talkReplyFragment?.goToReading()
            finish()
        } else {
            super.onBackPressed()
        }
    }
}