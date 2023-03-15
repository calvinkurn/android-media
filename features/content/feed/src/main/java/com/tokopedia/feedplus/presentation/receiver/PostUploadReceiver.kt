package com.tokopedia.feedplus.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.affiliatecommon.UPLOAD_FIRST_IMAGE
import com.tokopedia.affiliatecommon.UPLOAD_POST_NEW
import com.tokopedia.affiliatecommon.UPLOAD_POST_PROGRESS
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 14/03/23
 */
class PostUploadReceiver @AssistedInject constructor(
    @Assisted context: Context
) : UploadReceiver {

    @AssistedFactory
    interface Factory {
        fun create(context: Context): PostUploadReceiver
    }

    private val mContext = WeakReference(context)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observe(): Flow<UploadInfo> {
        return callbackFlow {
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    trySendBlocking(
                        UploadInfo(
                            intent.getIntExtra(UPLOAD_POST_PROGRESS, 0),
                            intent.getStringExtra(UPLOAD_FIRST_IMAGE).orEmpty(),
                        )
                    )
                }
            }

            val context = mContext.get() ?: return@callbackFlow
            LocalBroadcastManager.getInstance(context).registerReceiver(
                receiver,
                IntentFilter(UPLOAD_POST_NEW)
            )

            awaitClose {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
            }
        }
    }
}
