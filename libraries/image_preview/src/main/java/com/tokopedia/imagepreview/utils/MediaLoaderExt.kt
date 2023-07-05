package com.tokopedia.imagepreview.utils

import android.widget.ImageView
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.media.loader.data.ERROR_RES_UNIFY
import com.tokopedia.media.loader.data.FailureType
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.loadSecureImage
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by @ilhamsuaib on 05/07/23.
 */

private val productImageRegex = Regex(".*/(hDjmkQ|VqbcmM|product-1|attachment|XwPCqN)/.*")

fun ImageView.loadPreviewImage(url: String, userSession: UserSessionInterface, isSecure: Boolean) {

    if (userSession.accessToken.isEmpty() || url.isEmpty()) {
        loadImage(ERROR_RES_UNIFY)
        return
    }

    val isProductImage = productImageRegex.matches(url)
    if (isProductImage) {
        loadImageWithEmptyTarget(context, url, properties = {
            if (isSecure) {
                isSecure(true)
                userId(userSession.userId)
                userSessionAccessToken(userSession.accessToken)
            }
            shouldTrackNetworkResponse(true)
            networkResponse { _, failureType ->
                val isArchivedProduct =
                    failureType == FailureType.Gone || failureType == FailureType.NotFound
                if (isArchivedProduct) {
                    loadImage(TokopediaImageUrl.IMG_ARCHIVED_PRODUCT_LARGE) {
                        setPlaceHolder(-1)
                        fitCenter()
                        setCacheStrategy(MediaCacheStrategy.DATA)
                    }
                } else {
                    loadImage(url, userSession, isSecure)
                }
            }
        }, mediaTarget = MediaBitmapEmptyTarget())
    } else {
        loadImage(url, userSession, isSecure)
    }
}

private fun ImageView.loadImage(url: String, userSession: UserSessionInterface, isSecure: Boolean) {
    if (isSecure) {
        loadSecureImage(url, userSession) {
            fitCenter()
            setCacheStrategy(MediaCacheStrategy.DATA)
        }
    } else {
        loadImage(url) {
            fitCenter()
            setCacheStrategy(MediaCacheStrategy.DATA)
        }
    }
}
