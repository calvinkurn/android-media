package com.tokopedia.play.cast

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.media.MediaIntentReceiver
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.view.activity.PlayActivity
import java.lang.Exception

class PlayCastMediaIntentReceiver: MediaIntentReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        super.onReceive(p0, p1)
    }

    override fun onReceiveOtherAction(p0: Context?, p1: String?, p2: Intent?) {
        super.onReceiveOtherAction(p0, p1, p2)
        p0?.let {
            if(p1 == PlayCastNotificationAction.ACTION_OPEN_PLAY) {
                Log.d("<CAST>", "onReceiveOtherAction -> p1 : $p1")
                val castContext = CastContext.getSharedInstance(it)
                val channelId = castContext.sessionManager
                    .currentCastSession
                    .remoteMediaClient
                    ?.mediaInfo
                    ?.metadata
                    ?.getString("channel_id").orEmpty()
                Log.d("<CAST>", "onReceiveOtherAction : channelId : $channelId")

                try {
                    RouteManager.route(p0, "tokopedia://play/${channelId}")
//                it.startActivity(Intent(it, PlayActivity::class.java))
                }
                catch (e: Exception) {
                    Log.d("<CAST>", "Error : ${e.message}")
                }
            }
        }
    }
}