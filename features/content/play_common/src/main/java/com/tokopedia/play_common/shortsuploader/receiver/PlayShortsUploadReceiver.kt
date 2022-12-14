package com.tokopedia.play_common.shortsuploader.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.play_common.shortsuploader.PlayShortsUploader
import com.tokopedia.play_common.shortsuploader.di.DaggerPlayShortsUploaderComponent
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 08, 2022
 */
class PlayShortsUploadReceiver : BroadcastReceiver() {

    @Inject
    lateinit var playShortsUploader: PlayShortsUploader

    override fun onReceive(context: Context, intent: Intent?) {
        inject(context)

        val uploadDataRaw = intent?.getStringExtra(EXTRA_UPLOAD_DATA).orEmpty()
        val uploadData = PlayShortsUploadModel.parse(uploadDataRaw)

        NotificationManagerCompat.from(context).cancel(uploadData.notificationIdAfterUpload)

        playShortsUploader.upload(uploadData)
    }

    private fun inject(context: Context) {
        DaggerPlayShortsUploaderComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    companion object {

        private const val EXTRA_UPLOAD_DATA = "EXTRA_UPLOAD_DATA"

        fun getIntent(context: Context, uploadData: PlayShortsUploadModel): Intent {
            return Intent(context, PlayShortsUploadReceiver::class.java).apply {
                putExtra(EXTRA_UPLOAD_DATA, uploadData.toString())
            }
        }
    }
}
