package com.tokopedia.sellerorder.waitingpaymentorder.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerorder.common.presenter.activities.BaseSomActivity
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.fragment.WaitingPaymentOrderFragment

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderActivity : BaseSomActivity() {
    override fun getNewFragment(): Fragment? {
        return WaitingPaymentOrderFragment()
    }
}