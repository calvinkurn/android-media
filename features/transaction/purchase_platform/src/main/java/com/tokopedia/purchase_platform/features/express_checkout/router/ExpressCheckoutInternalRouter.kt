package com.tokopedia.purchase_platform.features.express_checkout.router

import android.app.Activity
import android.content.Intent
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.CheckoutVariantActivity
import com.tokopedia.purchase_platform.common.data.model.request.atc.AtcRequestParam

/**
 * Created by Irfan Khoirul on 04/02/19.
 */

class ExpressCheckoutInternalRouter {

    companion object {
        const val EXTRA_ATC_REQUEST = "EXTRA_ATC_REQUEST"
        const val TRACKER_ATTRIBUTION =  "tracker_attribution"
        const val TRACKER_LIST_NAME = "tracker_list_name"

        @JvmStatic @JvmOverloads
        fun createIntent(context: Activity?, atcRequestParam: AtcRequestParam,
                         trackerAttribution: String? = "",
                         trackerListName: String? = ""): Intent {
            val intent = Intent(context, CheckoutVariantActivity::class.java)
            intent.putExtra(EXTRA_ATC_REQUEST, atcRequestParam)
            intent.putExtra(TRACKER_ATTRIBUTION, trackerAttribution)
            intent.putExtra(TRACKER_LIST_NAME, trackerListName)
            return intent
        }
    }

}