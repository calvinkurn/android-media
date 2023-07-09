package com.tokopedia.ordermanagement.snapshot.util

import android.widget.ImageView
import com.tokopedia.media.loader.data.FailureType
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget

/**
 * Created by @ilhamsuaib on 04/07/23.
 */

fun ImageView.loadProductImage(url: String, archivedUrl: String, onArchived: (() -> Unit)? = null) {
    url.getBitmapImageUrl(
        context = context, properties = {
            shouldTrackNetworkResponse(true)
            networkResponse { _, failure ->
                val isArchivedProduct =
                    failure == FailureType.Gone || failure == FailureType.NotFound
                if (isArchivedProduct) {
                    onArchived?.invoke()
                    loadImage(archivedUrl) {
                        setPlaceHolder(-1)
                    }
                } else {
                    loadImage(url)
                }
            }
        }, target = MediaBitmapEmptyTarget()
    )
}