package com.tokopedia.media.loader.utils

import android.webkit.MimeTypeMap
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.tokopedia.config.GlobalConfig
import com.tokopedia.media.loader.data.HEADER_FMT
import com.tokopedia.media.loader.data.HEADER_KEY_AUTH
import com.tokopedia.media.loader.data.HEADER_USER_ID
import com.tokopedia.media.loader.data.HEADER_X_DEVICE
import com.tokopedia.media.loader.data.PREFIX_BEARER
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.data.WEBP_SUPPORT

/**
 * A Url builder for WebP support.
 *
 * Media platform allows to return a multiple image format,
 * based on [HEADER_FMT] value. The [HEADER_FMT] will decided
 * which content type need to render in the client side.
 *
 * On this case, we trying to enable the WebP support, which
 * we want to maintain the load time for various image url on
 * our internal CDN in Media Platform.
 *
 * X-Tkp-Fmt: image/webp
 */
private fun String.generateWebpUrl(): GlideUrl {
    return GlideUrl(
        this,
        LazyHeaders.Builder()
            .addHeader(HEADER_FMT, WEBP_SUPPORT)
            .build()
    )
}

/**
 * A Url builder for secure image load.
 *
 * In particular pages need to render a PII image for kind of
 * sensitive content, such as ID-CARD, Secret Receipt, etc. hence
 * to support the secure image load, we have to validate based on
 * user session.
 *
 * During fetch the image, we trying to load the image as well as
 * passing the user session to verify the request on the server side.
 *
 * Accounts-Authorization: Bearer [accessToken]
 * X-Device: android-X-version
 * Tkpd-UserId: [userId]
 *
 * @param userId
 * @param accessToken
 */
private fun String.generateSecureUrl(
    userId: String,
    accessToken: String
): GlideUrl {
    return GlideUrl(
        this,
        LazyHeaders.Builder()
            .apply {
                addHeader(HEADER_KEY_AUTH, "$PREFIX_BEARER %s".format(accessToken))
                addHeader(HEADER_X_DEVICE, "android-${GlobalConfig.VERSION_NAME}")
                addHeader(HEADER_USER_ID, userId)
            }
            .build()
    )
}

internal fun Properties.generateUrl(): Any {
    val data = data.toString()
    val extension = MimeTypeMap.getFileExtensionFromUrl(data)

    // we only need to send the webp converter for *non-webp* url image extension,
    // which reducing the unnecessary process in the BE part. So, if we got already webp url,
    // thus we don't have to send the [X-Tkp-Fmt] into the backend to do converter process.
    val isNotWebpImageUrl = extension != "webp"

    // secure image loader
    if (isSecure) return data.generateSecureUrl(userId, accessToken)

    // indicates that the url from our internal CDN, which contains a custom behavior,
    // such as adaptive delivery, webp support, custom header, etc.
    if (data.isFromInternalCdnImageUrl() && isNotWebpImageUrl) {
        return data.generateWebpUrl()
    }

    // load non-internal cdn image url;
    // including external url, ect7, or local file path.
    return data
}

internal fun String.isFromInternalCdnImageUrl(): Boolean {
    return this.startsWith("https://images.tokopedia.net/")
}

internal fun String.isValidUrl(): Boolean {
    return this.isNotEmpty() && (this.contains("https") || this.contains("http"))
}
