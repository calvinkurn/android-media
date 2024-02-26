package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.views.activities.TransParentActivity

object ProductDetailNavigator {

    fun Context?.goToMvc(
        shopId: String,
        productId: String,
        mvcAdditionalData: String,
        launcher: ActivityResultLauncher<Intent>
    ) {
        if (this == null) return
        val intent = TransParentActivity.getIntent(
            context = this,
            shopId = shopId,
            source = MvcSource.PDP,
            productId = productId,
            additionalParamJson = mvcAdditionalData
        )
        launcher.launch(intent)
    }
}
