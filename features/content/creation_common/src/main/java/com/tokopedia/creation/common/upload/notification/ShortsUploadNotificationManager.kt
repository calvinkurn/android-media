package com.tokopedia.creation.common.upload.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.creation.common.upload.activity.PlayShortsPostUploadActivity
import com.tokopedia.creation.common.upload.model.CreationUploadNotificationText
import com.tokopedia.creation.common.upload.model.CreationUploadQueue
import com.tokopedia.creation.common.upload.model.orEmpty
import com.tokopedia.creation.common.upload.receiver.PlayShortsUploadReceiver
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 18, 2023
 */
class ShortsUploadNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: CoroutineDispatchers,
) : CreationUploadNotificationManager(context, dispatchers) {

    override val uploadNotificationText: CreationUploadNotificationText
        get() = CreationUploadNotificationText(
            progressTitle = NOTIFICATION_PROGRESS_TITLE,
            progressDescription = NOTIFICATION_PROGRESS_DESCRIPTION,
            successTitle = NOTIFICATION_SUCCESS_TITLE,
            successDescription = NOTIFICATION_SUCCESS_DESCRIPTION,
            failTitle = NOTIFICATION_FAIL_TITLE,
            failDescription = NOTIFICATION_FAIL_DESCRIPTION,
            failRetryAction = NOTIFICATION_FAIL_RETRY_ACTION,
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
        val intent = PlayShortsUploadReceiver.getIntent(
            context,
            uploadData.orEmpty(),
            PlayShortsUploadReceiver.Companion.Action.Retry
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
        const val NOTIFICATION_PROGRESS_TITLE = "Tunggu ya, videomu lagi di-upload"
        const val NOTIFICATION_PROGRESS_DESCRIPTION = "Selagi menunggu video di-upload, kamu bisa cek produk atau konten menarik lainnya di Tokopedia."

        const val NOTIFICATION_SUCCESS_TITLE = "Yay, videomu berhasil di-upload!"
        const val NOTIFICATION_SUCCESS_DESCRIPTION = "Lihat videomu di sini, yuk!"

        const val NOTIFICATION_FAIL_TITLE = "Oops, gagal upload video"
        const val NOTIFICATION_FAIL_DESCRIPTION = "Tenang, kamu masih bisa coba upload videonya lagi."
        const val NOTIFICATION_FAIL_RETRY_ACTION = "Coba lagi"

        /** Web Link Const */
        const val PLAY_ROOM_PATH = "play/channel/"
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
