package com.tokopedia.creation.common.upload.uploader.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.R
import com.tokopedia.creation.common.upload.uploader.activity.PlayShortsPostUploadActivity
import com.tokopedia.creation.common.upload.model.CreationUploadNotificationText
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 18, 2023
 */
class StoriesUploadNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: CoroutineDispatchers,
) : CreationUploadNotificationManager(context, dispatchers) {

    override val uploadNotificationText: CreationUploadNotificationText = CreationUploadNotificationText(
        progressTitle = context.getString(R.string.content_creation_upload_notification_stories_progress_title),
        progressDescription = context.getString(R.string.content_creation_upload_notification_stories_progress_description),
        successTitle = context.getString(R.string.content_creation_upload_notification_stories_success_title),
        successDescription = context.getString(R.string.content_creation_upload_notification_stories_success_description),
        failTitle = context.getString(R.string.content_creation_upload_notification_stories_fail_title),
        failDescription = context.getString(R.string.content_creation_upload_notification_stories_fail_description),
        failRetryAction = context.getString(R.string.content_creation_upload_notification_stories_fail_retry),
    )

    override fun generateSuccessPendingIntent(): PendingIntent {
        /** TODO JOE: for mocking purpose */
        val intent = PlayShortsPostUploadActivity.getIntent(
            context,
            channelId = uploadData?.creationId.orEmpty(),
            authorId = uploadData?.authorId.orEmpty(),
            authorType = uploadData?.authorType.orEmpty(),
            appLink = "tokopedia://stories/shop/${uploadData?.authorId}"
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
    }
}
