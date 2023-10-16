package com.tokopedia.creation.common.upload.uploader.notification

import android.app.PendingIntent
import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.model.CreationUploadNotificationText
import javax.inject.Inject
import com.tokopedia.creation.common.R

/**
 * Created By : Jonathan Darwin on October 16, 2023
 */
class PostUploadNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: CoroutineDispatchers,
    private val gson: Gson,
) : CreationUploadNotificationManager(context, dispatchers, gson){

    override val uploadNotificationText: CreationUploadNotificationText = CreationUploadNotificationText(
    progressTitle = context.getString(R.string.content_creation_upload_notification_post_progress_title),
    progressDescription = context.getString(R.string.content_creation_upload_notification_post_progress_description),
    successTitle = context.getString(R.string.content_creation_upload_notification_post_success_title),
    successDescription = context.getString(R.string.content_creation_upload_notification_post_success_description),
    failTitle = context.getString(R.string.content_creation_upload_notification_post_fail_title),
    failDescription = context.getString(R.string.content_creation_upload_notification_post_fail_description),
    failRetryAction = context.getString(R.string.content_creation_upload_notification_post_fail_retry),
    )

    override fun generateSuccessPendingIntent(): PendingIntent {
        TODO("Not yet implemented")
    }
}
