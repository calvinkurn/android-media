package com.tokopedia.expresscheckout.view.variant

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.router.ExpressCheckoutInternalRouter.Companion.EXTRA_ATC_REQUEST
import com.tokopedia.expresscheckout.router.ExpressCheckoutInternalRouter.Companion.TRACKER_ATTRIBUTION
import com.tokopedia.expresscheckout.router.ExpressCheckoutInternalRouter.Companion.TRACKER_LIST_NAME
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam
import com.tokopedia.transactiondata.entity.shared.expresscheckout.Constant.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 * For navigate: use ApplinkConstInternalMarketplace.EXPRESS_CHECKOUT
 */
open class CheckoutVariantActivity : BaseSimpleActivity(), CheckoutVariantFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
    }

    override fun getNewFragment(): Fragment {
        val extra = intent.extras
        if (extra!= null) {
            return CheckoutVariantFragment.createInstance(extra[EXTRA_ATC_REQUEST] as AtcRequestParam,
                extra.getString(TRACKER_ATTRIBUTION),extra.getString(TRACKER_LIST_NAME))
        } else {
            return Fragment()
        }
    }

    override fun finishWithResult(messages: String) {
        val intentResult = Intent()
        intentResult.putExtra(EXTRA_MESSAGES_ERROR, messages)
        setResult(RESULT_CODE_ERROR, intentResult)
        finish()
        overridePendingTransition(0, R.anim.push_down)
    }

    override fun navigateAtcToOcs() {
        setResult(RESULT_CODE_NAVIGATE_TO_OCS)
        finish()
        overridePendingTransition(0, 0)
    }

    override fun navigateAtcToNcf() {
        setResult(RESULT_CODE_NAVIGATE_TO_NCF)
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, R.anim.push_down)
    }
}
