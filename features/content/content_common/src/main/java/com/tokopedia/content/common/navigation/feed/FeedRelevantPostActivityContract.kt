package com.tokopedia.content.common.navigation.feed

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent

/**
 * Created By : Jonathan Darwin on January 12, 2024
 */
class FeedRelevantPostActivityContract :  ActivityResultContract<FeedRelevantPostActivityContract.Data, Unit>() {

    override fun createIntent(context: Context, input: Data): Intent {
        return RouteManager.getIntent(
            context,
            ApplinkConst.FEED_RELEVANT_POST,
            input.postId,
        ).apply {
            putExtra(ApplinkConstInternalContent.UF_EXTRA_REFRESH_FOR_RELEVANT_POST, true)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?) {

    }

    data class Data(
        val postId: String
    )
}
