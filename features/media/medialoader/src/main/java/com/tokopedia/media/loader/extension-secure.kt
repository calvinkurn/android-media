package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.tokopedia.config.GlobalConfig
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.tracker.MediaLoaderTracker
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber

inline fun ImageView.loadSecureImage(
    url: String?,
    userSession: UserSessionInterface,
    crossinline properties: Properties.() -> Unit = {}
) {
    if (userSession.accessToken.isEmpty() && GlobalConfig.isAllowDebuggingTools()) {
        Timber.e("MediaLoader: Access token not found")
        MediaLoaderTracker.simpleTrack(context, url.toString())
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


fun loadSecureImageWithEmptyTarget(
    context: Context,
    url: String?,
    userSession: UserSessionInterface,
    properties: Properties.() -> Unit = {},
    mediaTarget: MediaBitmapEmptyTarget<Bitmap>
) {
    if (userSession.accessToken.isEmpty() || url.isNullOrEmpty()) return
    MediaLoaderTarget.loadImage(
        context,
        Properties()
            .apply(properties)
            .setSource(url)
            .userId(userSession.userId)
            .userSessionAccessToken(userSession.accessToken),
        mediaTarget,
        isSecure = true
    )
}