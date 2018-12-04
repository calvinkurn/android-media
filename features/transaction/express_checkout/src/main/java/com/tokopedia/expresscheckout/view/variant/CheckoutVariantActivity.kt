package com.tokopedia.expresscheckout.view.variant

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class CheckoutVariantActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return CheckoutVariantFragment.createInstance()
    }

}
