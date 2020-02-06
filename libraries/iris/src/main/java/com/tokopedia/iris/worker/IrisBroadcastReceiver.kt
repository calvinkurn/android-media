package com.tokopedia.iris.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tokopedia.iris.util.DEFAULT_MAX_ROW
import com.tokopedia.iris.util.MAX_ROW
import rx.Observable
import rx.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by meta on 28/05/19.
 */
class IrisBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Observable.just(true)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .map {
                if (context != null) {
                    val i = Intent(context, IrisService::class.java)
                    val maxRow = intent?.getIntExtra(MAX_ROW, DEFAULT_MAX_ROW)
                    i.putExtra(MAX_ROW, maxRow)
                    IrisService.enqueueWork(context, i)
                }
            }
            .subscribe({}, {
                Timber.e("P1#IRIS#onReceiver %s", it.toString())
            })
    }
}
