package com.tokopedia.content.product.preview.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager

/**
 * @author by astidhiyaa on 15/12/23
 */
class LoginReviewContract : ActivityResultContract<Unit, Boolean>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return RouteManager.getIntent(context, ApplinkConst.LOGIN)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}
