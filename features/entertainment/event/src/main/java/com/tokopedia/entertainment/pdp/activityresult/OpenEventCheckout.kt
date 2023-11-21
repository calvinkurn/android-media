package com.tokopedia.entertainment.pdp.activityresult

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.tokopedia.entertainment.pdp.activity.EventCheckoutActivity
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutBundle

class OpenEventCheckout : ActivityResultContract<EventCheckoutBundle, Boolean>() {

    override fun createIntent(context: Context, input: EventCheckoutBundle): Intent {
        return EventCheckoutActivity.createIntent(
            context,
            input.urlPDP,
            input.metadataResponse,
            input.idPackageActive,
            input.gatewayCode,
            input.softbookExpireTime
        )
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}
