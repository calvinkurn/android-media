package com.tokopedia.media.loader.listener

import android.graphics.Bitmap
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.tokopedia.media.loader.data.MediaException
import com.tokopedia.media.loader.wrapper.MediaDataSource
import java.io.File

interface MediaListener {
    fun onLoaded(resource: Bitmap?, dataSource: MediaDataSource?, isFirstResource: Boolean)
    fun onLoaded(resource: GifDrawable?, dataSource: MediaDataSource?)
    fun onFailed(error: MediaException?)
    fun onDownload(file: File?, dataSource: MediaDataSource?)
}
