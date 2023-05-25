package com.tokopedia.digital_product_detail.presentation.custom.activityresult

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.tokopedia.digital_product_detail.presentation.webview.RechargeCheckBalanceWebViewActivity

class OpenRechargeCheckBalance : ActivityResultContract<String, Boolean>() {

    override fun createIntent(context: Context, input: String): Intent {
        return RechargeCheckBalanceWebViewActivity.createInstance(context, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}
