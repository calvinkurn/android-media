package com.tokopedia.creation.common.upload.uploader.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent.UF_EXTRA_REFRESH_FOR_RELEVANT_POST
import com.tokopedia.config.GlobalConfig
import com.tokopedia.creation.common.R
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadNotificationText
import com.tokopedia.creation.common.upload.model.CreationUploadSuccessData
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on January 03, 2024
 */
class PostUploadNotificationManager  @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: CoroutineDispatchers,
    private val gson: Gson,
) : CreationUploadNotificationManager(context, dispatchers, gson) {

    override val uploadNotificationText: CreationUploadNotificationText = CreationUploadNotificationText(
        progressTitle = context.getString(R.string.content_creation_upload_notification_post_progress_title),
        progressDescription = context.getString(R.string.content_creation_upload_notification_post_progress_description),
        successTitle = context.getString(R.string.content_creation_upload_notification_post_success_title),
        successDescription = context.getString(R.string.content_creation_upload_notification_post_success_description),
        failTitle = context.getString(R.string.content_creation_upload_notification_post_fail_title),
        failDescription = context.getString(R.string.content_creation_upload_notification_post_fail_description),
        failRetryAction = context.getString(R.string.content_creation_upload_notification_post_fail_retry),
    )

    override fun generateSuccessPendingIntent(successData: CreationUploadSuccessData): PendingIntent? {
        val postUploadData = uploadData
        if (postUploadData !is CreationUploadData.Post || successData !is CreationUploadSuccessData.Post) return null

        val intent = if (GlobalConfig.isSellerApp()) {
            RouteManager.getIntent(context, ApplinkConst.SHOP_FEED, postUploadData.authorId).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        } else {
            RouteManager.getIntent(context, ApplinkConst.FEED_RELEVANT_POST, successData.activityId)
                .apply {
                    putExtra(UF_EXTRA_REFRESH_FOR_RELEVANT_POST, true)
                }
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
