package com.tokopedia.talk.feature.reply.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringConstants.TALK_REPLY_PLT_NETWORK_METRICS
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringConstants.TALK_REPLY_PLT_PREPARE_METRICS
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringConstants.TALK_REPLY_PLT_RENDER_METRICS
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringConstants.TALK_REPLY_TRACE
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.constants.TalkConstants.PARAM_SHOP_ID
import com.tokopedia.talk.common.constants.TalkConstants.QUESTION_ID
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reply.presentation.fragment.TalkReplyFragment

class TalkReplyActivity : BaseSimpleActivity(), HasComponent<TalkComponent>, TalkPerformanceMonitoringListener {

    private var questionId = ""
    private var shopId = ""
    private var source = ""
    private var inboxType = ""
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null
    private var talkReplyFragment: TalkReplyFragment? = null

    companion object {
        @JvmStatic
        fun createIntent(context: Context, questionId: String, shopId: String) =
                Intent(context, TalkReplyActivity::class.java).apply {
                    putExtra(QUESTION_ID, questionId)
                    putExtra(PARAM_SHOP_ID, shopId)
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromIntent()
        getDataFromAppLink()
        super.onCreate(savedInstanceState)
        startPerformanceMonitoring()
    }

    override fun getNewFragment(): Fragment? {
        talkReplyFragment = TalkReplyFragment.createNewInstance(questionId, shopId, source, inboxType)
        return talkReplyFragment
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    private fun getDataFromIntent() {
        intent?.run {
            shopId = getStringExtra(PARAM_SHOP_ID).orEmpty()
            questionId = getStringExtra(QUESTION_ID).orEmpty()
        }
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
        val inboxType = uri.getQueryParameter(TalkConstants.PARAM_TYPE) ?: ""
        if (inboxType.isNotEmpty()) {
            this.inboxType = inboxType
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
        KeyboardHandler.hideSoftKeyboard(this)
        if(talkReplyFragment?.getDidUserWriteQuestion() == true) {
            talkReplyFragment?.goToReading()
            finish()
        } else {
            super.onBackPressed()
        }
    }
}