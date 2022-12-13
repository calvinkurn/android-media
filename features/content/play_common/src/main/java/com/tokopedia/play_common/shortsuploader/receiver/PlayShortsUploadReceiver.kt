package com.tokopedia.play_common.shortsuploader.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play_common.shortsuploader.PlayShortsUploader
import com.tokopedia.play_common.shortsuploader.analytic.PlayShortsUploadAnalytic
import com.tokopedia.play_common.shortsuploader.di.DaggerPlayShortsUploaderComponent
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel
import com.tokopedia.url.TokopediaUrl
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 08, 2022
 */
class PlayShortsUploadReceiver : BroadcastReceiver() {

    @Inject
    lateinit var playShortsUploader: PlayShortsUploader

    @Inject
    lateinit var analytic: PlayShortsUploadAnalytic

    override fun onReceive(context: Context, intent: Intent?) {
        inject(context)

        val uploadDataRaw = intent?.getStringExtra(EXTRA_UPLOAD_DATA).orEmpty()
        val uploadData = PlayShortsUploadModel.parse(uploadDataRaw)

        val action = intent?.getIntExtra(EXTRA_ACTION, 0).orZero()

        when(action) {
            Action.OpenPlayRoom.value -> {
                analytic.clickRedirectToChannelRoom(uploadData.authorId, uploadData.authorType, uploadData.shortsId)
                val playRoomIntent = RouteManager.getIntent(context, getPlayRoomWebLink(uploadData)).apply {
                    flags = FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(playRoomIntent)
            }
            Action.Retry.value -> {
                analytic.clickRetryUpload(uploadData.authorId, uploadData.authorType, uploadData.shortsId)
                playShortsUploader.upload(uploadData)
            }
        }
    }

    private fun inject(context: Context) {
        DaggerPlayShortsUploaderComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun getPlayRoomWebLink(uploadData: PlayShortsUploadModel): String {
        return buildString {
            append(TokopediaUrl.getInstance().WEB)
            append(PLAY_ROOM_PATH)
            append(uploadData.shortsId)
            append("?")
            append("$SOURCE_TYPE=${getSourceType(uploadData.authorType)}")
            append("&")
            append("$SOURCE_ID=${uploadData.authorId}")
        }
    }

    private fun getSourceType(authorType: String): String {
        return when(authorType) {
            CONTENT_SHOP -> SOURCE_TYPE_SHOP
            CONTENT_USER -> SOURCE_TYPE_USER
            else -> ""
        }
    }

    companion object {

        private const val EXTRA_UPLOAD_DATA = "EXTRA_UPLOAD_DATA"
        private const val EXTRA_ACTION = "EXTRA_ACTION"

        /** Web Link Const */
        const val PLAY_ROOM_PATH = "play/channel/"
        const val CONTENT_USER = "content-user"
        const val CONTENT_SHOP = "content-shop"

        const val SOURCE_TYPE = "source_type"
        const val SOURCE_ID = "source_id"
        const val SOURCE_TYPE_USER = "SHORT_VIDEO_USER"
        const val SOURCE_TYPE_SHOP = "SHORT_VIDEO_SHOP"

        fun getIntent(
            context: Context,
            uploadData: PlayShortsUploadModel,
            action: Action,
        ): Intent {
            return Intent(context, PlayShortsUploadReceiver::class.java).apply {
                putExtra(EXTRA_UPLOAD_DATA, uploadData.toString())
                putExtra(EXTRA_ACTION, action.value)
            }
        }

        enum class Action(val value: Int) {
            OpenPlayRoom(1), Retry(2)
        }
    }
}
