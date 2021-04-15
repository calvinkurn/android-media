package com.tokopedia.media.loader.listener

import android.graphics.Bitmap
import com.tokopedia.media.loader.utils.MediaException
import com.tokopedia.media.loader.wrapper.MediaDataSource

interface MediaListener {
    fun onLoaded(resource: Bitmap?, dataSource: MediaDataSource?)
    fun onFailed(error: MediaException?)
}