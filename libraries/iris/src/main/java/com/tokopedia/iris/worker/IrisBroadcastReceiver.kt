package com.tokopedia.iris.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tokopedia.iris.DEFAULT_MAX_ROW
import com.tokopedia.iris.MAX_ROW

/**
 * Created by meta on 28/05/19.
 */
class IrisBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("Iris", "startBroadcastReceiver")
        val i = Intent(context, IrisService::class.java)
        val maxRow = intent?.getIntExtra(MAX_ROW, DEFAULT_MAX_ROW)
        i.putExtra(MAX_ROW, maxRow)
        IrisService.enqueueWork(context!!, i)
    }
}
