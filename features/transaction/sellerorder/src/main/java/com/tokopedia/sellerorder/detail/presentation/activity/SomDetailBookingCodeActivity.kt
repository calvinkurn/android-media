package com.tokopedia.sellerorder.detail.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerorder.common.presenter.activities.BaseSomActivity
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailBookingCodeFragment

/**
 * Created by fwidjaja on 2019-11-27.
 */
class SomDetailBookingCodeActivity: BaseSomActivity() {
    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras ?: Bundle()
        } else {
            bundle.putString(SomConsts.PARAM_BOOKING_CODE, "")
            bundle.putString(SomConsts.PARAM_BARCODE_TYPE, "")
        }
        return SomDetailBookingCodeFragment.newInstance(bundle)
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }
}