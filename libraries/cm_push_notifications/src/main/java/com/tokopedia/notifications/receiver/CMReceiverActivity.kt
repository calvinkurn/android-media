package com.tokopedia.notifications.receiver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tokopedia.notifications.R


class CMReceiverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cmreceiver)
        CMNotificationHandler.instance.handleIntent(this, intent)
    }
}