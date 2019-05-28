package com.tokopedia.iris.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tokopedia.iris.WORKER_SEND_DATA
import com.tokopedia.iris.model.Configuration

/**
 * Created by meta on 28/05/19.
 */
class IrisBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("Iris", "startService")
        val i = Intent(context, IrisService::class.java)
        val config = intent?.getParcelableExtra<Configuration>(WORKER_SEND_DATA)
        i.putExtra(WORKER_SEND_DATA, config)
        IrisService.enqueueWork(context!!, i)
    }

}