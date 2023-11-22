package com.tokopedia.creation.common.upload.uploader.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.R
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.uploader.activity.ContentCreationPostUploadActivity
import com.tokopedia.creation.common.upload.model.CreationUploadNotificationText
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 18, 2023
 */
class StoriesUploadNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: CoroutineDispatchers,
    private val gson: Gson,
) : CreationUploadNotificationManager(context, dispatchers, gson) {

    override val uploadNotificationText: CreationUploadNotificationText = CreationUploadNotificationText(
        progressTitle = context.getString(R.string.content_creation_upload_notification_stories_progress_title),
        progressDescription = context.getString(R.string.content_creation_upload_notification_stories_progress_description),
        successTitle = context.getString(R.string.content_creation_upload_notification_stories_success_title),
        successDescription = context.getString(R.string.content_creation_upload_notification_stories_success_description),
        failTitle = context.getString(R.string.content_creation_upload_notification_stories_fail_title),
        failDescription = context.getString(R.string.content_creation_upload_notification_stories_fail_description),
        failRetryAction = context.getString(R.string.content_creation_upload_notification_stories_fail_retry),
    )

    override fun generateSuccessPendingIntent(): PendingIntent? {
        val storiesUploadData = uploadData
        if (storiesUploadData !is CreationUploadData.Stories) return null

        val intent = ContentCreationPostUploadActivity.getIntent(
            context,
            channelId = storiesUploadData.creationId,
            authorId = storiesUploadData.authorId,
            authorType = storiesUploadData.authorType,
            uploadType = storiesUploadData.uploadType.type,
            appLink = storiesUploadData.applink,
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
