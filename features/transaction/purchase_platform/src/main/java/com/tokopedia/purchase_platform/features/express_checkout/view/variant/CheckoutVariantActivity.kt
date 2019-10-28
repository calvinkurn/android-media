package com.tokopedia.purchase_platform.features.express_checkout.view.variant

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.data.model.request.atc.AtcRequestParam
import com.tokopedia.purchase_platform.common.constant.Constant.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 * For navigate: use ApplinkConstInternalMarketplace.EXPRESS_CHECKOUT
 */
open class CheckoutVariantActivity : BaseSimpleActivity(), CheckoutVariantFragmentListener {

    lateinit var atcRequestParam: AtcRequestParam
    var trackerAttribution:String? = null
    var trackerListName:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val extra = intent.extras
        if (extra == null) {
            finish()
            return
        }
        atcRequestParam = extra.getParcelable(EXTRA_ATC_REQUEST) ?: AtcRequestParam()
        trackerAttribution = extra.getString(TRACKER_ATTRIBUTION)
        trackerListName = extra.getString(TRACKER_LIST_NAME)
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
    }

    override fun getNewFragment(): Fragment {
        return CheckoutVariantFragment.createInstance(atcRequestParam, trackerAttribution,trackerListName)
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
