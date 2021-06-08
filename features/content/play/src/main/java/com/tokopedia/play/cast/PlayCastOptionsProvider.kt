package com.tokopedia.play.cast

import android.content.Context
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider

/**
 * Created by jegul on 08/06/21
 */
class PlayCastOptionsProvider : OptionsProvider {

    override fun getCastOptions(context: Context): CastOptions {
        return CastOptions.Builder()
                .setReceiverApplicationId("B607CC94")
                .build()
    }

    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider> {
        return emptyList()
    }
}