package com.tokopedia.play.cast

import android.content.Context
import android.content.Intent
import com.google.android.gms.cast.framework.media.MediaIntentReceiver
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.util.PlayCastHelper
import java.lang.Exception

class PlayCastMediaIntentReceiver: MediaIntentReceiver() {
    override fun onReceiveOtherAction(context: Context?, actionName: String, data: Intent) {
        super.onReceiveOtherAction(context, actionName, data)
        context?.let {
            if(actionName == PlayCastNotificationAction.ACTION_OPEN_PLAY) {
                try {
                    val castContext = PlayCastHelper.getCastContext(it)
                    val channelId = castContext?.sessionManager
                        ?.currentCastSession
                        ?.remoteMediaClient
                        ?.mediaInfo
                        ?.metadata
                        ?.getString(CHANNEL_ID).orEmpty()

                    val intent = RouteManager.getIntent(it, ApplinkConst.PLAY_DETAIL.replace("{$CHANNEL_ID}", channelId))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    it.startActivity(intent)
                }
                catch (e: Exception) {

                }
            }
        }
    }

    companion object {
        private const val CHANNEL_ID = "channel_id"
    }
}
