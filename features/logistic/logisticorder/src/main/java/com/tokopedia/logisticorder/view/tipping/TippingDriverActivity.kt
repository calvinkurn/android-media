package com.tokopedia.logisticorder.view.tipping

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.logisticorder.utils.TippingConstant.PARAM_ORDER_ID

class TippingDriverActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment? {
        val orderId = intent?.data?.getQueryParameter(PARAM_ORDER_ID).orEmpty()
        return TippingDriverFragment.newInstance(orderId)
    }
}
