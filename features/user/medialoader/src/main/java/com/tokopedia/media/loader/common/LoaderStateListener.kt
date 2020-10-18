package com.tokopedia.media.loader.common

import android.graphics.drawable.Drawable
import com.tokopedia.media.loader.utils.MediaException

interface LoaderStateListener {
    fun successLoad(resource: Drawable?, dataSource: MediaDataSource?)
    fun failedLoad(error: MediaException?)
}