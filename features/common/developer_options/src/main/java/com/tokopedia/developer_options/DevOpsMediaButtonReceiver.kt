package com.tokopedia.developer_options

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import androidx.media.session.MediaButtonReceiver
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity


class DevOpsMediaButtonReceiver : MediaButtonReceiver() {
    private val TRIGGER_SEQUENCE = intArrayOf(KeyEvent.ACTION_UP, KeyEvent.ACTION_UP,
            KeyEvent.ACTION_DOWN, KeyEvent.ACTION_DOWN)
    var triggerSequenceStep = 0
    var lastButtonClickTimeStamp = 0L
    val MAX_INTERVAL = 2000L // 2 seconds

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val event: KeyEvent = intent!!.getParcelableExtra(Intent.EXTRA_KEY_EVENT) as KeyEvent?
                    ?: return

            val now = System.currentTimeMillis()
            if (now - lastButtonClickTimeStamp > MAX_INTERVAL) {
                triggerSequenceStep = 0
            }
            lastButtonClickTimeStamp = now
            if (TRIGGER_SEQUENCE[triggerSequenceStep] == event.action) {
                triggerSequenceStep++
            } else {
                triggerSequenceStep = 0
            }

            if (triggerSequenceStep == TRIGGER_SEQUENCE.size) {
                triggerSequenceStep = 0
                val devOpsintent = Intent(context, DeveloperOptionActivity::class.java)
                devOpsintent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                context?.startActivity(devOpsintent)
            }
        } catch (e: Exception) {

        }
    }
}