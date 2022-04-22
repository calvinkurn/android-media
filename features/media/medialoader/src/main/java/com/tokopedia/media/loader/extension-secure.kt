package com.tokopedia.media.loader

import android.widget.ImageView
import android.widget.Toast
import com.tokopedia.media.loader.common.Properties
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