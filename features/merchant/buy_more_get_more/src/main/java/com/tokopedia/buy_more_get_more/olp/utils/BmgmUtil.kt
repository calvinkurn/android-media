package com.tokopedia.buy_more_get_more.olp.utils

import android.content.Context
import android.graphics.Bitmap
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget

object BmgmUtil {
    fun loadImageWithEmptyTarget(
        context: Context,
        url: String,
        properties: Properties.() -> Unit,
        mediaTarget: MediaBitmapEmptyTarget<Bitmap>
    ) {
        com.tokopedia.media.loader.loadImageWithEmptyTarget(context, url, properties, mediaTarget)
    }
}
