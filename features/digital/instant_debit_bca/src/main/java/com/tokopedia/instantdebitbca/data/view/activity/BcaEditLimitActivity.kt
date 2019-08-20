package com.tokopedia.instantdebitbca.data.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment

import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.instantdebitbca.data.view.fragment.EditLimitFragment
import com.tokopedia.instantdebitbca.data.view.utils.ApplinkConstant

class BcaEditLimitActivity : InstantDebitBcaActivity() {

    override fun getNewFragment(): Fragment {
        return EditLimitFragment.newInstance(applicationContext, intent.getStringExtra(InstantDebitBcaActivity.CALLBACK_URL), intent.getStringExtra(XCOID))
    }

    companion object {

        val XCOID = "xcoid"

        fun newInstance(context: Context, callbackUrl: String, xcoid: String): Intent {
            val intent = Intent(context, BcaEditLimitActivity::class.java)
            intent.putExtra(XCOID, xcoid)
            intent.putExtra(CALLBACK_URL, callbackUrl)
            return intent
        }
    }

    object DeepLinkIntent {

        @DeepLink(ApplinkConstant.INSTANT_DEBIT_BCA_EDITLIMIT_APPLINK)
        @JvmStatic
        fun intentForTaskStackEditLimit(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            val xcoid = if (extras.containsKey(XCOID)) extras.getString(XCOID) else ""
            val callbackUrl = if (extras.containsKey(CALLBACK_URL)) extras.getString(CALLBACK_URL) else ""
            return newInstance(context, callbackUrl, xcoid)
        }
    }

}
