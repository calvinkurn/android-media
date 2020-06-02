package com.tokopedia.topupbills.telco.view.activity

import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.topupbills.R
import timber.log.Timber

open abstract class BaseTelcoActivity : BaseSimpleActivity() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_digital_telco
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar_telco
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.handleExtra()

        //draw background without overdraw GPU
        window.setBackgroundDrawableResource(R.color.digital_cardview_light_background)
    }

    /* This Method is use to tracking Action click when user click TelcoProduct
   */

    private fun Intent.handleExtra() {
        if (intent.data != null) {
            val trackingClick = intent.getStringExtra(RECHARGE_PRODUCT_EXTRA)
            if (trackingClick != null) {
                Timber.w("P2#ACTION_SLICE_CLICK_RECHARGE#$trackingClick")
            }
        }
    }

    companion object {
        const val PARAM_MENU_ID = "menu_id"
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_CLIENT_NUMBER = "client_number"
        const val PARAM_CATEGORY_ID = "category_id"
        const val RECHARGE_PRODUCT_EXTRA = "RECHARGE_PRODUCT_EXTRA"
    }
}