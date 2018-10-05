package com.tokopedia.broadcast.message

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object BroadcastMessageInternalRouter {

    fun getBroadcastMessageListIntent(context: Context): Intent {
        Toast.makeText(context, "Broadcast Message List", Toast.LENGTH_SHORT).show()
        return Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("http://www.getBroadcastMessageListIntent.com"))
    }
}