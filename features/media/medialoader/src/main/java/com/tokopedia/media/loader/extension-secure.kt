package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.Toast
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.user.session.UserSessionInterface

inline fun ImageView.loadSecureImage(
    url: String,
    userSession: UserSessionInterface,
    crossinline properties: Properties.() -> Unit = {}
) {
    if (userSession.accessToken.isEmpty()) {
        Toast.makeText(context, "(Dev) - access token not found", Toast.LENGTH_SHORT).show()
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
    url: String,
    userSession: UserSessionInterface,
    properties: Properties.() -> Unit = {},
    mediaTarget: MediaBitmapEmptyTarget<Bitmap>
) {
    if (userSession.accessToken.isEmpty()) {
        Toast.makeText(context, "(Dev) - access token not found", Toast.LENGTH_SHORT).show()
        return
    }
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