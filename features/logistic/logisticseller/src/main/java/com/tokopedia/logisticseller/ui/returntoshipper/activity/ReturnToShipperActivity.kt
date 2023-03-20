package com.tokopedia.logisticseller.ui.returntoshipper.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.logisticseller.ui.returntoshipper.fragment.ReturnToShipperFragment

class ReturnToShipperActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val orderId = intent.getStringExtra(ApplinkConstInternalOrder.PARAM_ORDER_ID).orEmpty()
        return ReturnToShipperFragment.newInstance(orderId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideActionBar()
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }
}
