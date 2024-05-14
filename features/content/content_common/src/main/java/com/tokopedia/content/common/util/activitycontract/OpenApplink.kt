package com.tokopedia.content.common.util.activitycontract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager

class ContentActivityResultContracts {

    class OpenAppLink : ActivityResultContract<String, OpenAppLink.Result>() {

        data class Result(val resultCode: Int, val intent: Intent?)

        override fun createIntent(context: Context, input: String): Intent {
            return RouteManager.getIntent(context, input)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Result {
            return Result(resultCode, intent)
        }
    }

    class OpenLogin : ActivityResultContract<Unit, Boolean>() {

        private val openAppLink = OpenAppLink()

        override fun createIntent(context: Context, input: Unit): Intent {
            return openAppLink.createIntent(context, ApplinkConst.LOGIN)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return openAppLink.parseResult(resultCode, intent).resultCode == Activity.RESULT_OK
        }
    }
}
