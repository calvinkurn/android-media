package com.tokopedia.iris.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tokopedia.iris.DEFAULT_MAX_ROW
import com.tokopedia.iris.MAX_ROW
import com.tokopedia.iris.launchCatchError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Created by meta on 28/05/19.
 */
class IrisBroadcastReceiver : BroadcastReceiver(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun onReceive(context: Context?, intent: Intent?) {
        launchCatchError {
            if (context != null) {
                val i = Intent(context, IrisService::class.java)
                val maxRow = intent?.getIntExtra(MAX_ROW, DEFAULT_MAX_ROW)
                i.putExtra(MAX_ROW, maxRow)
                IrisService.enqueueWork(context, i)
            }
        }
    }
}
