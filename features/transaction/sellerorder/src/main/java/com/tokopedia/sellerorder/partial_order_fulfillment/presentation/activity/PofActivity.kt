package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.sellerorder.partial_order_fulfillment.di.DaggerPofComponent
import com.tokopedia.sellerorder.partial_order_fulfillment.di.PofComponent
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.bottomsheet.PofBottomSheet
import timber.log.Timber
import com.tokopedia.abstraction.R as abstractionR

class PofActivity : BaseSimpleActivity(), HasComponent<PofComponent> {

    companion object {
        const val PARAMS_ORDER_ID = "ORDER_ID"
        const val PARAMS_POF_STATUS = "POF_STATUS"
    }

    private val orderId by lazyThreadSafetyNone {
        intent.extras?.getString(PARAMS_ORDER_ID).toLongOrZero()
    }

    private val pofStatus by lazyThreadSafetyNone {
        intent.extras?.getString(PARAMS_POF_STATUS).toIntOrZero()
    }

    private val daggerComponent by lazyThreadSafetyNone {
        DaggerPofComponent
            .builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleDimming()
        hideToolbar()
        showWriteFormBottomSheet()
    }

    override fun getNewFragment(): Fragment? {
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

    private fun showWriteFormBottomSheet() {
        supportFragmentManager.findFragmentByTag(PofBottomSheet.TAG).let {
            if (it == null) {
                PofBottomSheet.createInstance(orderId, pofStatus).run {
                    show(supportFragmentManager, PofBottomSheet.TAG)
                }
            }
        }
    }
}
