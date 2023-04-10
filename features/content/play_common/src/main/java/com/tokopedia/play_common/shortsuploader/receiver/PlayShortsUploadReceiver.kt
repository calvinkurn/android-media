package com.tokopedia.play_common.shortsuploader.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play_common.shortsuploader.PlayShortsUploader
import com.tokopedia.play_common.shortsuploader.analytic.PlayShortsUploadAnalytic
import com.tokopedia.play_common.shortsuploader.di.uploader.DaggerPlayShortsUploaderComponent
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

        NotificationManagerCompat.from(context).cancel(uploadData.notificationIdAfterUpload)

        val action = intent?.getIntExtra(EXTRA_ACTION, 0).orZero()

        when(action) {
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

    companion object {

        private const val EXTRA_UPLOAD_DATA = "EXTRA_UPLOAD_DATA"
        private const val EXTRA_ACTION = "EXTRA_ACTION"

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
            Retry(1)
        }
    }
}
