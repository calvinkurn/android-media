@file:JvmName("DevOpsMedia")
package com.tokopedia.developer_options.receiver

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.tokopedia.config.GlobalConfig

fun initReceiver(context: Context){
    val componentName = ComponentName(context, DevOpsMediaButtonReceiver::class.java)

    // construct a PendingIntent for the media button and register it
    val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
    //     the associated intent will be handled by the component being registered
    //     the associated intent will be handled by the component being registered
    mediaButtonIntent.component = componentName

    val pi = PendingIntent.getBroadcast(context,0 , mediaButtonIntent, 0 )
    val mediaSession = MediaSessionCompat(context, GlobalConfig.APPLICATION_ID)
    mediaSession.setPlaybackState(PlaybackStateCompat.Builder()
            .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0f)
            .build())
    mediaSession.setMediaButtonReceiver(pi)
    mediaSession.isActive = true
}