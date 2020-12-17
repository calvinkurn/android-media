@file:JvmName("DevOpsMedia")
package com.tokopedia.developer_options.receiver

import android.content.ComponentName
import android.content.Context
import android.media.AudioManager

fun initReceiver(context: Context){
    val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val componentName = ComponentName(context, DevOpsMediaButtonReceiver::class.java)
    am.registerMediaButtonEventReceiver(componentName)
}