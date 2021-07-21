package com.tokopedia.play.cast

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.media.MediaIntentReceiver
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.play.view.activity.PlayActivity
import java.lang.Exception

class PlayCastMediaIntentReceiver: MediaIntentReceiver() {
    override fun onReceiveOtherAction(p0: Context?, p1: String?, p2: Intent?) {
        super.onReceiveOtherAction(p0, p1, p2)
        p0?.let {
            if(p1 == PlayCastNotificationAction.ACTION_OPEN_PLAY) {
                val castContext = CastContext.getSharedInstance(it)
                val channelId = castContext.sessionManager
                    .currentCastSession
                    .remoteMediaClient
                    ?.mediaInfo
                    ?.metadata
                    ?.getString("channel_id").orEmpty()

                try {
                    val intent = RouteManager.getIntent(it, ApplinkConst.PLAY_DETAIL.replace("{channel_id}", channelId))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    it.startActivity(intent)
                }
                catch (e: Exception) {

                }
            }
        }
    }
}