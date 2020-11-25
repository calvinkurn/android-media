package com.tokopedia.talk.feature.reading.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringConstants.TALK_READING_PLT_NETWORK_METRICS
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringConstants.TALK_READING_PLT_PREPARE_METRICS
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringConstants.TALK_READING_PLT_RENDER_METRICS
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringConstants.TALK_READING_TRACE
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.constants.TalkConstants.NO_SHADOW_ELEVATION
import com.tokopedia.talk.common.constants.TalkConstants.PARAM_SHOP_ID
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.common.utils.TalkReadingLoadTimeMonitoringListener
import com.tokopedia.talk.feature.reading.presentation.fragment.TalkReadingFragment

class TalkReadingActivity : BaseSimpleActivity(), HasComponent<TalkComponent>, TalkPerformanceMonitoringListener {

    private var productId: String = ""
    private var shopId: String = ""
    private var isVariantSelected: Boolean = false
    private var availableVariants: String = ""
    var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null
    var talkReadingLoadTimeListener: TalkReadingLoadTimeMonitoringListener? = null

    companion object {
        private const val PRODUCT_ID_EXTRA = "productId"
        private const val SHOP_ID_EXTRA = "shopId"

        @JvmStatic
        fun createIntent(context: Context, productId: String, shopId: String) =
                Intent(context, TalkReadingActivity::class.java).apply {
                    putExtra(PRODUCT_ID_EXTRA, productId)
                    putExtra(SHOP_ID_EXTRA, shopId)
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromAppLink()
        getDataFromIntent()
        if (FirebaseRemoteConfigImpl(applicationContext).getBoolean(TalkConstants.APP_DISABLE_NEW_TALK_REMOTE_CONFIG_KEY, false)) {
            val intent = Intent(applicationContext, TalkProductActivity::class.java)
            intent.putExtra(TalkProductActivity.PRODUCT_ID, productId)
            intent.putExtra(TalkProductActivity.SHOP_ID, shopId)
            startActivity(intent)
            finish()
        }
        super.onCreate(savedInstanceState)
        startPerformanceMonitoring()
        setUpToolBar()
    }

    override fun getNewFragment(): Fragment? {
        return TalkReadingFragment.createNewInstance(productId, shopId, isVariantSelected, availableVariants)
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                TALK_READING_PLT_PREPARE_METRICS,
                TALK_READING_PLT_NETWORK_METRICS,
                TALK_READING_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(TALK_READING_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.let {
            it.stopMonitoring()
        }
        talkReadingLoadTimeListener?.onStopPltListener()
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

    private fun getDataFromIntent() {
        intent?.run {
            shopId = getStringExtra(SHOP_ID_EXTRA).orEmpty()
            productId = getStringExtra(PRODUCT_ID_EXTRA).orEmpty()
        }
    }

    private fun getDataFromAppLink() {
        val uri = intent.data ?: return
        val shopId = uri.getQueryParameter(PARAM_SHOP_ID) ?: ""
        if (shopId.isNotEmpty()) {
            this.shopId = shopId
        }
        val productIdString = uri.pathSegments[uri.pathSegments.size - 1] ?: return
        if (productIdString.isNotEmpty()) {
            this.productId = productIdString
        }
        val isVariantSelectedString = uri.getQueryParameter(TalkConstants.PARAM_APPLINK_IS_VARIANT_SELECTED)
                ?: ""
        if (isVariantSelectedString.isNotEmpty()) {
            this.isVariantSelected = isVariantSelectedString.toBoolean()
        }
        val availableVariantsFromApplink = uri.getQueryParameter(TalkConstants.PARAM_APPLINK_AVAILABLE_VARIANT)
                ?: ""
        if (availableVariantsFromApplink.isNotEmpty()) {
            this.availableVariants = availableVariantsFromApplink
        }
    }

    private fun setUpToolBar() {
        supportActionBar?.elevation = NO_SHADOW_ELEVATION
    }

}