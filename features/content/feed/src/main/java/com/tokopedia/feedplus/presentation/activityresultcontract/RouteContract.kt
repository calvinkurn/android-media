package com.tokopedia.feedplus.presentation.activityresultcontract

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.tokopedia.applink.RouteManager

/**
 * Created by kenny.hadisaputra on 29/03/23
 */
class RouteContract : ActivityResultContract<String, Unit>() {

    override fun createIntent(context: Context, input: String?): Intent {
        return RouteManager.getIntent(context, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?) {}
}
