package com.tokopedia.ordermanagement.snapshot.util

import android.widget.ImageView
import com.tokopedia.media.loader.data.FailureType
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget

/**
 * Created by @ilhamsuaib on 04/07/23.
 */

fun ImageView.loadProductImage(url: String, archivedUrl: String) {
    loadImageWithEmptyTarget(context, url, properties = {
        shouldTrackNetworkResponse(true)
        networkResponse { _, failureType ->
            val isArchivedProduct =
                failureType == FailureType.Gone || failureType == FailureType.NotFound
            if (isArchivedProduct) {
                loadImage(archivedUrl) {
                    setPlaceHolder(-1)
                }
            } else {
                loadImage(url)
            }
        }
    }, mediaTarget = MediaBitmapEmptyTarget())
}