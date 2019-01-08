package com.tokopedia.expresscheckout.view.variant

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequest
import com.tokopedia.transaction.common.data.expresscheckout.Constant.*

class CheckoutVariantActivity : BaseSimpleActivity(), CheckoutVariantFragmentListener {

    companion object {
        val EXTRA_ATC_REQUEST = "EXTRA_ATC_REQUEST"

        @JvmStatic
        fun createIntent(context: Activity?, atcRequest: AtcRequest): Intent {
            val intent = Intent(context, CheckoutVariantActivity::class.java)
            intent.putExtra(EXTRA_ATC_REQUEST, atcRequest)

            return intent
        }

    }

    override fun getNewFragment(): Fragment {
        return CheckoutVariantFragment.createInstance(intent.extras[EXTRA_ATC_REQUEST] as AtcRequest)
    }

    override fun finishWithResult(messages: String) {
        val intentResult = Intent()
        intentResult.putExtra(EXTRA_MESSAGES_ERROR, messages)
        setResult(RESULT_CODE_ERROR, intentResult)
        finish()
    }

    override fun navigateToOcs() {
        setResult(RESULT_CODE_NAVIGATE_TO_OCS)
        finish()
    }

    override fun navigateToNcf() {
        setResult(RESULT_CODE_NAVIGATE_TO_NCF)
        finish()
    }

}
