package com.tokopedia.notifications.receiver

import android.app.Activity
import android.os.Bundle
import com.tokopedia.notifications.R


class CMReceiverActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cmreceiver)
        CMNotificationHandler.instance.handleIntent(this, intent)
        finish()
    }
}