package com.tokopedia.content.common.util.activitycontract

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
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
}
