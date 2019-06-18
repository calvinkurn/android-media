package com.tokopedia.topupbills.telco.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoFragment

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class TelcoProductActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return DigitalTelcoFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.elevation = 0f
    }

    object DeeplinkIntents{

        @JvmStatic
        @DeepLink(ApplinkConst.DIGITAL + ApplinkConst.DigitalProduct.TELCO)
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            val intent = TelcoProductActivity.newInstance(context)
            return intent
                    .setData(uri.build())
                    .putExtras(extras)
        }
    }

    companion object {

        fun newInstance(context: Context): Intent {
            val intent = Intent(context, TelcoProductActivity::class.java)
            return intent
        }

    }

    override fun onBackPressed() {
        finish()
    }
}