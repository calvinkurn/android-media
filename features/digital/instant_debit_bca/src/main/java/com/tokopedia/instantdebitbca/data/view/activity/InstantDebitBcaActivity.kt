package com.tokopedia.instantdebitbca.data.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment

import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.instantdebitbca.data.view.fragment.InstantDebitBcaFragment
import com.tokopedia.instantdebitbca.data.view.utils.ApplinkConstant

/**
 * Created by nabillasabbaha on 25/03/19.
 */
open class InstantDebitBcaActivity : BaseSimpleActivity(), InstantDebitBcaFragment.ActionListener{
    private val finishDelay = 5000

    override fun getNewFragment(): Fragment {
        return InstantDebitBcaFragment.newInstance(applicationContext, intent.getStringExtra(CALLBACK_URL))
    }

    override fun redirectPage(applinkUrl: String?){
        if (RouteManager.isSupportApplink(applicationContext, applinkUrl)) {
            val intentRegisteredApplink = RouteManager.getIntent(applicationContext, applinkUrl)
            startActivity(intentRegisteredApplink)
        }
        finishWithDelay()
    }

    private fun finishWithDelay() {
        val handler = Handler()
        handler.postDelayed({ finish() }, finishDelay.toLong())
    }

    companion object {

        val CALLBACK_URL = "callbackUrl"

        fun newInstance(context: Context, callbackUrl: String): Intent {
            val intent = Intent(context, InstantDebitBcaActivity::class.java)
            intent.putExtra(CALLBACK_URL, callbackUrl)
            return intent
        }
    }

    object DeepLinkIntent{

        @DeepLink(ApplinkConstant.INSTANT_DEBIT_BCA_APPLINK)
        @JvmStatic fun intentForTaskStackBuilderMethods(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            val callbackUrl = if (extras.containsKey(CALLBACK_URL)) extras.getString(CALLBACK_URL) else ""
            return newInstance(context, callbackUrl)
        }
    }
}
