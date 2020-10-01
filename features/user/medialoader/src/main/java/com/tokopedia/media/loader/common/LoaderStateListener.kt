package com.tokopedia.media.loader.common

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException

interface LoaderStateListener {
    fun successLoad(resource: Drawable?, dataSource: DataSource?)
    fun failedLoad(error: GlideException?)
}