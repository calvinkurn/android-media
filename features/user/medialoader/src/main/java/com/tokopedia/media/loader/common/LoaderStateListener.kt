package com.tokopedia.media.loader.common

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.tokopedia.media.loader.utils.MediaException

interface LoaderStateListener {
    fun successLoad(resource: Bitmap?, dataSource: MediaDataSource?)
    fun failedLoad(error: MediaException?)
}