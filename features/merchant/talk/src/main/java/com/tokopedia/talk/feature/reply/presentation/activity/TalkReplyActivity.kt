package com.tokopedia.talk.feature.reply.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
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
    private var isFromInbox = false
    private var productId = ""
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        getDataFromAppLink()
        super.onCreate(savedInstanceState)
        setUpToolBar()
    }

    override fun getNewFragment(): Fragment? {
        return TalkReplyFragment.createNewInstance(questionId, shopId, productId, isFromInbox)
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
        val productIdString = uri.getQueryParameter(TalkConstants.PARAM_PRODUCT_ID) ?: ""
        if (productIdString.isNotEmpty()) {
            this.productId = productIdString
        }
        val shopId = uri.getQueryParameter(TalkConstants.PARAM_SHOP_ID) ?: ""
        if (shopId.isNotEmpty()) {
            this.shopId = shopId
        }
        with(TalkDetailsActivity) {
            val source = uri.getQueryParameter(SOURCE) ?: ""
            if (source.isNotEmpty()) {
                isFromInbox = source == SOURCE_INBOX
            }
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
        pageLoadTimePerformanceMonitoring = null;
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
            it.stopPreparePagePerformanceMonitoring()
        }
    }
}