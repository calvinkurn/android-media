package com.tokopedia.instantdebitbca.data.view.activity

import android.os.Handler
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.instantdebitbca.data.view.fragment.InstantDebitBcaFragment

/**
 * Created by nabillasabbaha on 25/03/19.
 */
open class InstantDebitBcaActivity : BaseSimpleActivity(), InstantDebitBcaFragment.ActionListener{

    companion object {
        val CALLBACK_URL = "callbackUrl"
    }

    private val finishDelay = 5000

    override fun getNewFragment(): Fragment {
        var callback = ""
        intent?.extras?.getString(CALLBACK_URL)?.let {
            callback = it
        }
        return InstantDebitBcaFragment.newInstance(callback)
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
}