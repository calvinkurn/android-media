package com.tokopedia.digital_product_detail.presentation.custom.activityresult

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.presentation.webview.RechargeCheckBalanceWebViewActivity

class OpenRechargeCheckBalance : ActivityResultContract<String, OpenRechargeCheckBalance.CheckBalanceOTPResult>() {

    override fun createIntent(context: Context, input: String): Intent {
        return RechargeCheckBalanceWebViewActivity.createInstance(context, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): CheckBalanceOTPResult {
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                val accessToken = intent.getStringExtra(DigitalPDPConstant.EXTRA_CHECK_BALANCE_ACCESS_TOKEN) ?: ""
                if (accessToken.isNotEmpty()) {
                    return CheckBalanceOTPResult.Success(accessToken)
                }
            }
        }
        return CheckBalanceOTPResult.EmptyToken
    }

    sealed class CheckBalanceOTPResult {
        object EmptyToken : CheckBalanceOTPResult()
        data class Success(val accessToken: String) : CheckBalanceOTPResult()
    }
}
