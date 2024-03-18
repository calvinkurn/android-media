package com.tokopedia.content.common.ui.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager

class OpenLogin : ActivityResultContract<Unit, Boolean>() {

    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(RouteManager.getIntent(context, ApplinkConst.LOGIN))
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}
