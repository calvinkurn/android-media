package com.tokopedia.mediauploader.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.mediauploader.di.DaggerMediaUploaderTestComponent
import com.tokopedia.mediauploader.di.MediaUploaderTestModule
import dagger.Lazy
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UploaderReceiver : BroadcastReceiver(), CoroutineScope {

    @Inject lateinit var lazyUploaderUseCase: Lazy<UploaderUseCase>
    private val uploaderUseCase: UploaderUseCase get() = lazyUploaderUseCase.get()

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    override fun onReceive(context: Context, intent: Intent?) {
        initInjector(context)

        intent?.let {
            if (it.hasExtra(BROADCAST_CANCEL_UPLOAD)) {
                val notificationId = it.getIntExtra(BROADCAST_NOTIFICATION_ID, -1)
                val sourceId = it.getStringExtra(BROADCAST_SOURCE_ID)?: ""
                val filePath = it.getStringExtra(BROADCAST_FILE_PATH)?: ""

                if (notificationId != -1) {
                    launch {
                        uploaderUseCase.abortUpload(
                            sourceId = sourceId,
                            filePath = filePath
                        ) {
                            withContext(Dispatchers.Main) {
                                UploaderWorker.cancelWork(context)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initInjector(context: Context) {
        DaggerMediaUploaderTestComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .mediaUploaderTestModule(MediaUploaderTestModule(context))
            .mediaUploaderModule(MediaUploaderModule())
            .build()
            .inject(this)
    }

    companion object {
        const val BROADCAST_CANCEL_UPLOAD = "cancel_upload"
        const val BROADCAST_NOTIFICATION_ID = "notification_id"
        const val BROADCAST_SOURCE_ID = "source_id"
        const val BROADCAST_FILE_PATH = "file_path"
    }

}