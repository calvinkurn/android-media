package com.tokopedia.talk.feature.write.presentation.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringConstants
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.write.presentation.fragment.TalkWriteFragment

class TalkWriteActivity : BaseSimpleActivity(), HasComponent<TalkComponent>, TalkPerformanceMonitoringListener {

    companion object {
        const val PARAM_PRODUCT_ID = "product_id"
        fun createIntent(context: Context, productId: String, isVariantSelected: Boolean, availableVariants: String): Intent {
            val intent = Intent(context, TalkWriteActivity::class.java)
            intent.putExtra(TalkConstants.PARAM_PRODUCT_ID, productId)
            intent.putExtra(TalkConstants.PARAM_APPLINK_IS_VARIANT_SELECTED, isVariantSelected)
            intent.putExtra(TalkConstants.PARAM_APPLINK_AVAILABLE_VARIANT, availableVariants)
            return intent
        }
    }

    private var productId: String = ""
    private var isVariantSelected: Boolean = false
    private var availableVariants: String = ""
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        productId = intent.getStringExtra(TalkConstants.PARAM_PRODUCT_ID) ?: ""
        isVariantSelected = intent.getBooleanExtra(TalkConstants.PARAM_APPLINK_IS_VARIANT_SELECTED, isVariantSelected)
        availableVariants = intent.getStringExtra(TalkConstants.PARAM_APPLINK_AVAILABLE_VARIANT) ?: ""
        if(productId.isBlank()) {
            getDataFromApplink()
        }
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment? {
        return TalkWriteFragment.createNewInstance(productId, isVariantSelected, availableVariants)
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    override fun startPerformanceMonitoring() {
        with(TalkPerformanceMonitoringConstants) {
            pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                    TALK_WRITE_PLT_PREPARE_METRICS,
                    TALK_WRITE_PLT_NETWORK_METRICS,
                    TALK_WRITE_PLT_RENDER_METRICS,
                    0,
                    0,
                    0,
                    0,
                    null
            )
            pageLoadTimePerformanceMonitoring?.startMonitoring(TALK_WRITE_TRACE)
            pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
        }
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

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun getDataFromApplink() {
        val uri = intent.data ?: return
        val productIdString = uri.getQueryParameter(PARAM_PRODUCT_ID) ?: ""
        if (productIdString.isNotEmpty()) {
            this.productId = productIdString
        }
        val isVariantSelectedString = uri.getQueryParameter(TalkConstants.PARAM_APPLINK_IS_VARIANT_SELECTED) ?: ""
        if (isVariantSelectedString.isNotEmpty()) {
            this.isVariantSelected = isVariantSelectedString.toBoolean()
        }
        val availableVariantFromApplink = uri.getQueryParameter(TalkConstants.PARAM_APPLINK_AVAILABLE_VARIANT) ?: ""
        if (availableVariantFromApplink.isNotEmpty()) {
            this.availableVariants = availableVariantFromApplink
        }
    }
}