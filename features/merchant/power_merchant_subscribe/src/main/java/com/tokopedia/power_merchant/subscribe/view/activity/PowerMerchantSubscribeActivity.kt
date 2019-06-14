package com.tokopedia.power_merchant.subscribe.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.power_merchant.subscribe.view.fragment.PowerMerchantSubscribeFragment

class PowerMerchantSubscribeActivity : BaseSimpleActivity() {

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, PowerMerchantSubscribeActivity::class.java)
        }
    }

    override fun getNewFragment(): Fragment {
        startActivity(PowerMerchantTermsActivity.createIntent(this))
        return PowerMerchantSubscribeFragment.createInstance()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    object DeepLinkIntents {
        @DeepLink(ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE)
        @JvmStatic
        fun getCallingIntentSellerHistory(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            return newInstance(context).setData(uri.build())
                    .putExtras(extras)
        }
    }

}
