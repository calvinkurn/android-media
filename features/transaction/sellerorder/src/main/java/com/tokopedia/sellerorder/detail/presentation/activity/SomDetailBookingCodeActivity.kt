package com.tokopedia.sellerorder.detail.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailBookingCodeFragment

/**
 * Created by fwidjaja on 2019-11-27.
 */
class SomDetailBookingCodeActivity: BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras
        } else {
            // bundle.putString(PARAM_ORDER_ID, "")
        }
        return SomDetailBookingCodeFragment.newInstance(bundle)
    }
}