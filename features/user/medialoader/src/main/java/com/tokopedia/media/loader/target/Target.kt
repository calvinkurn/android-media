package com.tokopedia.media.loader.target

import android.graphics.drawable.Drawable
import com.tokopedia.media.loader.common.MediaDataSource
import com.tokopedia.media.loader.utils.MediaException

typealias MediaTarget = Target

interface Target {
    fun successLoad(resource: Drawable?, dataSource: MediaDataSource?)
    fun failedLoad(error: MediaException?)
}