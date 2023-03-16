package com.tokopedia.feedplus.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.affiliatecommon.BROADCAST_SUBMIT_POST_NEW
import com.tokopedia.affiliatecommon.SUBMIT_POST_SUCCESS_NEW
import com.tokopedia.affiliatecommon.UPLOAD_FIRST_IMAGE
import com.tokopedia.affiliatecommon.UPLOAD_POST_NEW
import com.tokopedia.affiliatecommon.UPLOAD_POST_PROGRESS
import com.tokopedia.createpost.common.DRAFT_ID
import com.tokopedia.createpost.common.view.service.SubmitPostService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.lang.ref.WeakReference

/**
 * Created by kenny.hadisaputra on 14/03/23
 */
class PostUploadReceiver @AssistedInject constructor(
    @Assisted context: Context,
) : UploadReceiver {

    @AssistedFactory
    interface Factory {
        fun create(context: Context): PostUploadReceiver
    }

    private val mContext = WeakReference(context)

    private val intentFilter = IntentFilter().apply {
        addAction(UPLOAD_POST_NEW)
        addAction(BROADCAST_SUBMIT_POST_NEW)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observe(): Flow<UploadInfo> {
        return callbackFlow {
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    when (intent.action) {
                        UPLOAD_POST_NEW -> {
                            trySendBlocking(onProgress(intent))
                        }
                        BROADCAST_SUBMIT_POST_NEW -> {
                            trySendBlocking(onFinished(intent))
                        }
                    }
                }
            }

            val context = mContext.get() ?: return@callbackFlow
            LocalBroadcastManager.getInstance(context).registerReceiver(
                receiver,
                intentFilter,
            )

            awaitClose {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
            }
        }

//        return flow {
//            emit(UploadInfo.Progress(10, ""))
//            delay(5000)
//            emit(UploadInfo.Progress(30, ""))
//            delay(5000)
//            emit(UploadInfo.Progress(40, ""))
//            delay(5000)
//            emit(UploadInfo.Progress(80, ""))
//            delay(5000)
//            emit(UploadInfo.Progress(100, ""))
//            delay(5000)
//            emit(UploadInfo.Failed("") {
//                Log.d("UPLOAD FAILED", "Retry")
//            })
//        }
    }

    private fun onProgress(intent: Intent): UploadInfo.Progress {
        val progress = intent.getIntExtra(UPLOAD_POST_PROGRESS, -1)
        val thumbnailUrl = intent.getStringExtra(UPLOAD_FIRST_IMAGE).orEmpty()

        return UploadInfo.Progress(progress, thumbnailUrl)
    }

    private fun onFinished(intent: Intent): UploadInfo {
        val isSuccess = intent.getBooleanExtra(SUBMIT_POST_SUCCESS_NEW, false)
        val thumbnailUrl = intent.getStringExtra(UPLOAD_FIRST_IMAGE).orEmpty()

        return if (isSuccess) {
            UploadInfo.Finished
        } else {
            UploadInfo.Failed(thumbnailUrl) {
                val draftId = intent.getStringExtra(DRAFT_ID) ?: return@Failed
                val context = mContext.get() ?: return@Failed
                SubmitPostService.startService(context = context, draftId)
            }
        }
    }
}
