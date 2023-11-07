package com.tokopedia.imageassets.utils

import android.widget.ImageView
import com.tokopedia.media.loader.data.DEFAULT_ROUNDED
import com.tokopedia.media.loader.data.FailureType
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.media.loader.loadImage
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
) {
    val remoteConfig = FirebaseRemoteConfigImpl(context.applicationContext)
    val isEnabled = remoteConfig.getBoolean(RemoteConfigKey.LOAD_PRODUCT_IMAGE_ARCHIVAL_KEY, false)
    if (isEnabled) {
        var type: FailureType? = null
        url.getBitmapImageUrl(
            context = context, properties = {
                shouldTrackNetworkResponse(true)
                networkResponse { _, failure ->
                    type = failure
                }
                setRoundedRadius(cornerRadius)
            }, target = MediaBitmapEmptyTarget(
                onReady = {
                    val isArchived = type == FailureType.Gone || type == FailureType.NotFound
                    onLoaded?.invoke(isArchived)

                    if (isArchived) {
                        loadImage(archivedUrl)
                    } else {
                        this.setImageBitmap(it)
                    }
                }
            )
        )
    } else {
        loadImage(url)
    }
}