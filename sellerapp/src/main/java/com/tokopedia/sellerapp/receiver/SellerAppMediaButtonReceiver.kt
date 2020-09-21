package com.tokopedia.sellerapp.receiver

import android.content.Context
import android.content.Intent
import androidx.media.session.MediaButtonReceiver
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 21/09/20
 */

class SellerAppMediaButtonReceiver : MediaButtonReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            super.onReceive(context, intent)
        } catch (e: IllegalStateException) {
            Timber.i(e.toString())
        }
    }
}