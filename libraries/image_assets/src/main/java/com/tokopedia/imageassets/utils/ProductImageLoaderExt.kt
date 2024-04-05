package com.tokopedia.imageassets.utils

import android.graphics.Bitmap
import android.widget.ImageView
import com.tokopedia.media.loader.data.DEFAULT_ROUNDED
import com.tokopedia.media.loader.data.FailureType
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

/**
 * Created by @ilhamsuaib on 09/07/23.
 */

fun ImageView.loadProductImage(
    url: String,
    archivedUrl: String,
    cornerRadius: Float = DEFAULT_ROUNDED,
    onLoaded: ((isArchived: Boolean) -> Unit)? = null
): MediaBitmapEmptyTarget<Bitmap>? {
    val remoteConfig = FirebaseRemoteConfigImpl(context.applicationContext)
    val isEnabled = remoteConfig.getBoolean(RemoteConfigKey.LOAD_PRODUCT_IMAGE_ARCHIVAL_KEY, false)
    var target: MediaBitmapEmptyTarget<Bitmap>? = null
    if (isEnabled) {
        var isArchived = false
        target = MediaBitmapEmptyTarget(
            onReady = {
                if (isArchived) {
                    loadImageRounded(archivedUrl, cornerRadius)
                } else {
                    loadImageRounded(it, cornerRadius)
                }
                onLoaded?.invoke(isArchived)
            }
        )
        url.getBitmapImageUrl(
            context = context,
            properties = {
                shouldTrackNetworkResponse(true)
                networkResponse { _, failure ->
                    isArchived = failure == FailureType.Gone || failure == FailureType.NotFound
                }
            },
            target = target
        )
    } else {
        loadImageRounded(url, cornerRadius) {
            listener(onSuccess = { _, _ ->
                onLoaded?.invoke(false)
            })
        }
    }
    return target
}
