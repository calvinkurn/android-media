package com.tokopedia.media.loader.common

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.tokopedia.media.loader.utils.MediaException

interface MediaListener {
    fun onLoaded(resource: Bitmap?, dataSource: MediaDataSource?)
    fun onFailed(error: MediaException?)
}