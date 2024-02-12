package com.tokopedia.logisticseller.ui.confirmshipping.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.logisticseller.common.LogisticSellerConst.PARAM_CURR_IS_CHANGE_SHIPPING
import com.tokopedia.logisticseller.common.LogisticSellerConst.PARAM_ORDER_ID
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by fwidjaja on 2019-11-15.
 *
 * this used for
 * - confirm shipping
 * - change courier
 */
class ConfirmShippingActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.run {
            decorView.setBackgroundColor(
                MethodChecker.getColor(
                    this@ConfirmShippingActivity,
                    unifyprinciplesR.color.Unify_Background
                )
            )
        }
    }
    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras ?: Bundle()
        } else {
            bundle.putString(PARAM_ORDER_ID, "")
            bundle.putBoolean(PARAM_CURR_IS_CHANGE_SHIPPING, false)
        }
        return ConfirmShippingFragment.newInstance(bundle)
    }
}
