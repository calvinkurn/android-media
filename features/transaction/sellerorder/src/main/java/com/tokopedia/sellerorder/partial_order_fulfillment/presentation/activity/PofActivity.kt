package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.activity

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.sellerorder.partial_order_fulfillment.di.DaggerPofComponent
import com.tokopedia.sellerorder.partial_order_fulfillment.di.PofComponent
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestInfoResponse.Data.InfoRequestPartialOrderFulfillment.Companion.STATUS_INITIAL
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.bottomsheet.PofBottomSheet
import timber.log.Timber
import com.tokopedia.abstraction.R as abstractionR

class PofActivity : BaseSimpleActivity(), HasComponent<PofComponent> {

    private var orderId: Long = Long.ZERO
    private var pofStatus: Int = STATUS_INITIAL

    private val daggerComponent by lazyThreadSafetyNone {
        DaggerPofComponent
            .builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustOrientation()
        handleDimming()
        hideToolbar()
        showPofBottomSheet()
    }

    override fun getNewFragment(): Fragment? {
        orderId = intent.extras?.getString(DeeplinkMapperOrder.Pof.INTENT_PARAM_ORDER_ID)?.toLongOrNull()
            ?: intent.data?.getQueryParameter(DeeplinkMapperOrder.Pof.INTENT_PARAM_ORDER_ID).toLongOrZero()
        pofStatus = intent.extras?.getString(DeeplinkMapperOrder.Pof.INTENT_PARAM_POF_STATUS)?.toIntOrNull()
            ?: intent.data?.getQueryParameter(DeeplinkMapperOrder.Pof.INTENT_PARAM_POF_STATUS).toIntOrZero()
        return null
    }

    override fun getComponent(): PofComponent {
        return daggerComponent
    }

    private fun handleDimming() {
        try {
            window.setDimAmount(0f)
        } catch (th: Throwable) {
            Timber.e(th)
        }
    }

    private fun hideToolbar() {
        findViewById<Toolbar>(abstractionR.id.toolbar)?.hide()
    }

    private fun showPofBottomSheet() {
        supportFragmentManager.findFragmentByTag(PofBottomSheet.TAG).let {
            if (it == null) {
                PofBottomSheet.createInstance(orderId, pofStatus).run {
                    show(supportFragmentManager, PofBottomSheet.TAG)
                }
            }
        }
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
