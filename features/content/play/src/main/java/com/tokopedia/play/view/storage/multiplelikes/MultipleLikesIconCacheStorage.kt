package com.tokopedia.play.view.storage.multiplelikes

import android.graphics.Bitmap
import com.tokopedia.play.di.PlayScope
import javax.inject.Inject

/**
 * Created by jegul on 15/09/21
 */
@PlayScope
class MultipleLikesIconCacheStorage @Inject constructor() {

    private val cacheMap: MutableMap<String, Bitmap> = mutableMapOf()

    fun addCache(url: String, bitmap: Bitmap) {
        cacheMap[url] = bitmap
    }

    fun getBitmap(url: String): Bitmap? {
        return cacheMap[url]
    }
}