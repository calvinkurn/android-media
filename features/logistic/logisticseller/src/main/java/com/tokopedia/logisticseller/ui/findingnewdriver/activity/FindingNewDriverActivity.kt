package com.tokopedia.logisticseller.ui.findingnewdriver.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.ui.findingnewdriver.fragment.FindingNewDriverFragment

class FindingNewDriverActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val orderId = intent.getStringExtra(LogisticSellerConst.PARAM_ORDER_ID).orEmpty()
        val invoice = intent.getStringExtra(LogisticSellerConst.PARAM_INVOICE).orEmpty()
        return FindingNewDriverFragment.newInstance(orderId, invoice)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }
}
