package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.tokopedia.media.loader.data.ERROR_RES_UNIFY
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber

/**
 * An ImageLoader for loading a secure image into an ImageView on Android. This extension require a [UserSessionInterface]
 * for authorize the permission to render the image efficiently.
 *
 * <b>Sample Usage</b>
 *
 * ```
 * imageView.loadSecureImage("https://tokopedia.net/confidential-pii.png", userSession)
 * ```
 *
 * <b>Sample Usage with Custom Properties</b>
 *
 * ```
 * imageView.loadSecureImage("https://tokopedia.net/confidential-pii.png") {
 *    setErrorDrawable(R.drawable.ic_error)
 *    setCacheStrategy(MediaCacheStrategy.RESOURCE)
 *    overrideSize(Resize(540, 540))
 * }
 * ```
 *
 * @receiver [ImageView]
 * @param url Url to load image
 * @param userSession A user session for authorized
 * @param properties A custom properties
 * @since v1.0.0
 */
inline fun ImageView.loadSecureImage(
    url: String?,
    userSession: UserSessionInterface,
    crossinline properties: Properties.() -> Unit = {}
) {
    if (userSession.accessToken.isEmpty() || url.isNullOrEmpty()) {
        Timber.e("MediaLoader: Access token not found")
        loadImage(ERROR_RES_UNIFY)
        return
    }

    call(
        url,
        Properties()
            .apply(properties)
            .isSecure(true)
            .userId(userSession.userId)
            .userSessionAccessToken(userSession.accessToken)
    )
}

/**
 * The [loadSecureImageWithEmptyTarget] is pretty similar like [loadSecureImage] one. If you need to fetch the confidential image
 * into non-[ImageView] target, then you could use this extension instead. The extension will produces a bitmap and receive
 * the [mediaTarget] to set the view target to render the image.
 *
 * <b>Sample Usage</b>
 *
 * ```
 * imageView.loadSecureImageWithEmptyTarget(
 *    context = context,
 *    url = "https://tokopedia.net/confidential-pii.png",
 *    userSession = userSession,
 *    mediaTarget = mediaTarget
 * )
 * ```
 *
 * <b>Sample Usage with Custom Properties</b>
 *
 * ```
 * imageView.loadSecureImageWithEmptyTarget(
 *    context = context,
 *    url = "https://tokopedia.net/confidential-pii.png",
 *    userSession = userSession,
 *    mediaTarget = mediaTarget
 * ) {
 *    setErrorDrawable(R.drawable.ic_error)
 *    setCacheStrategy(MediaCacheStrategy.RESOURCE)
 *    overrideSize(Resize(540, 540))
 * }
 * ```
 *
 * @receiver [ImageView]
 * @param url Url to load image
 * @param userSession A user session for authorized
 * @param properties A custom properties
 * @param mediaTarget Custom target to render the bitmap result
 * @since v1.0.0
 */
fun loadSecureImageWithEmptyTarget(
    context: Context,
    url: String?,
    userSession: UserSessionInterface,
    properties: Properties.() -> Unit = {},
    mediaTarget: MediaBitmapEmptyTarget<Bitmap>
) {
    if (userSession.accessToken.isEmpty() || url.isNullOrEmpty()) {
        Timber.e("MediaLoader: Access token not found")
        return
    }

    MediaLoaderTarget.loadImage(
        context,
        Properties()
            .apply(properties)
            .setSource(url)
            .isSecure(true)
            .userId(userSession.userId)
            .userSessionAccessToken(userSession.accessToken),
        mediaTarget
    )
}
