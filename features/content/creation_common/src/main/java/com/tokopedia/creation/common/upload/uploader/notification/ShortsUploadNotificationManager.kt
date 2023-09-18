package com.tokopedia.creation.common.upload.uploader.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.creation.common.upload.uploader.activity.PlayShortsPostUploadActivity
import com.tokopedia.creation.common.upload.model.CreationUploadNotificationText
import com.tokopedia.creation.common.upload.model.CreationUploadQueue
import com.tokopedia.creation.common.upload.model.orEmpty
import javax.inject.Inject
import com.tokopedia.creation.common.R
import com.tokopedia.creation.common.upload.uploader.receiver.CreationUploadReceiver

/**
 * Created By : Jonathan Darwin on September 18, 2023
 */
class ShortsUploadNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: CoroutineDispatchers,
) : CreationUploadNotificationManager(context, dispatchers) {

    override val uploadNotificationText: CreationUploadNotificationText = CreationUploadNotificationText(
        progressTitle = context.getString(R.string.content_creation_upload_notification_shorts_progress_title),
        progressDescription = context.getString(R.string.content_creation_upload_notification_shorts_progress_description),
        successTitle = context.getString(R.string.content_creation_upload_notification_shorts_success_title),
        successDescription = context.getString(R.string.content_creation_upload_notification_shorts_success_description),
        failTitle = context.getString(R.string.content_creation_upload_notification_shorts_fail_title),
        failDescription = context.getString(R.string.content_creation_upload_notification_shorts_fail_description),
        failRetryAction = context.getString(R.string.content_creation_upload_notification_shorts_fail_retry),
    )

    override fun generateSuccessPendingIntent(): PendingIntent {
        val intent = PlayShortsPostUploadActivity.getIntent(
            context,
            channelId = uploadData?.creationId.orEmpty(),
            authorId = uploadData?.authorId.orEmpty(),
            authorType = uploadData?.authorType.orEmpty(),
            appLink = getPlayRoomAppLink(uploadData.orEmpty())
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

    private fun getPlayRoomAppLink(uploadData: CreationUploadQueue): String {
        return buildString {
            append(UriUtil.buildUri(ApplinkConst.PLAY_DETAIL, uploadData.creationId))
            append("?")
            append("$SOURCE_TYPE=${getSourceType(uploadData.authorType)}")
            append("&")
            append("$AUTHOR_TYPE=${uploadData.authorType}")
            append("&")
            append("$SOURCE_ID=${uploadData.authorId}")
            append("&")
            append("$IS_FROM_NOTIF_SUCCESS_UPLOAD=true")
        }
    }

    private fun getSourceType(authorType: String): String {
        return when(authorType) {
            CONTENT_SHOP -> SOURCE_TYPE_SHOP
            CONTENT_USER -> SOURCE_TYPE_USER
            else -> ""
        }
    }

    override fun generateErrorPendingIntent(): PendingIntent {
        val intent = CreationUploadReceiver.getIntent(
            context,
            uploadData.orEmpty(),
            CreationUploadReceiver.Companion.Action.Retry
        )

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
    }

    companion object {
        /** Web Link Const */
        const val CONTENT_USER = "content-user"
        const val CONTENT_SHOP = "content-shop"

        const val SOURCE_TYPE = "source_type"
        const val SOURCE_ID = "source_id"
        const val AUTHOR_TYPE = "author_type"
        const val IS_FROM_NOTIF_SUCCESS_UPLOAD = "is_from_notif_success_upload"
        const val SOURCE_TYPE_USER = "SHORT_VIDEO_USER"
        const val SOURCE_TYPE_SHOP = "SHORT_VIDEO_SHOP"
    }
}
