package com.tokopedia.feedplus.presentation.activityresultcontract

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager

/**
 * Created by kenny.hadisaputra on 17/03/23
 */
class OpenLoginContract : ActivityResultContract<Unit, Boolean>() {

    override fun createIntent(context: Context, input: Unit): Intent {
        return RouteManager.getIntent(context, ApplinkConst.LOGIN)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}
