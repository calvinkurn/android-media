package com.tokopedia.media.loader

import android.widget.ImageView
import com.tokopedia.config.GlobalConfig
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.tracker.MediaLoaderTracker
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber

inline fun ImageView.loadSecureImage(
    url: String,
    userSession: UserSessionInterface,
    crossinline properties: Properties.() -> Unit = {}
) {
    if (userSession.accessToken.isEmpty() && GlobalConfig.isAllowDebuggingTools()) {
        Timber.e("MediaLoader: Access token not found")
        MediaLoaderTracker.simpleTrack(context, url)
        return
    }

    call(
        url,
        Properties()
            .apply(properties)
            .userId(userSession.userId)
            .userSessionAccessToken(userSession.accessToken),
        isSecure = true
    )
}