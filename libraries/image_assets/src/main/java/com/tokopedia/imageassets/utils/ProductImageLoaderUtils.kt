package com.tokopedia.imageassets.utils

import android.widget.ImageView
import com.tokopedia.media.loader.data.DEFAULT_ROUNDED
import com.tokopedia.media.loader.data.FailureType
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget

/**
 * Created by @ilhamsuaib on 09/07/23.
 */

fun ImageView.loadProductImage(
    url: String, archivedUrl: String, cornerRadius: Float = DEFAULT_ROUNDED
) {
    url.getBitmapImageUrl(
        context = context, properties = {
            shouldTrackNetworkResponse(true)
            networkResponse { _, failure ->
                val isArchivedProduct =
                    failure == FailureType.Gone || failure == FailureType.NotFound
                if (isArchivedProduct) {
                    loadImage(archivedUrl) {
                        setPlaceHolder(-1)
                    }
                } else {
                    loadImage(url)
                }
            }
            setRoundedRadius(cornerRadius)
        }, target = MediaBitmapEmptyTarget()
    )
}