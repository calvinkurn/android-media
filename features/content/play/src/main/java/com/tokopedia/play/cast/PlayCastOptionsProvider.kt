package com.tokopedia.play.cast

import android.content.Context
import com.google.android.gms.cast.framework.*
import com.google.android.gms.cast.framework.media.*

/**
 * Created by jegul on 08/06/21
 */
class PlayCastOptionsProvider : OptionsProvider {

    override fun getCastOptions(context: Context): CastOptions {
        val notificationOptions = NotificationOptions.Builder()
            .setNotificationActionsProvider(PlayCastNotificationAction(context)).build()

        val mediaOptions = CastMediaOptions.Builder()
            .setMediaIntentReceiverClassName(PlayCastMediaIntentReceiver::class.java.name)
            .setNotificationOptions(notificationOptions)
            .build()

        return CastOptions.Builder()
                .setCastMediaOptions(mediaOptions)
                .setReceiverApplicationId("B607CC94")
                .setStopReceiverApplicationWhenEndingSession(true)
                .build()
    }

    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider> {
        return emptyList()
    }
}

