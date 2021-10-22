package com.tokopedia.play.view.storage.multiplelikes

import android.graphics.drawable.Drawable
import com.tokopedia.play.di.PlayScope
import javax.inject.Inject

/**
 * Created by jegul on 15/09/21
 */
@PlayScope
class MultipleLikesIconCacheStorage @Inject constructor() {

    private val cacheMap: MutableMap<String, Drawable> = mutableMapOf()

    fun addCache(url: String, drawable: Drawable) {
        cacheMap[url] = drawable
    }

    fun getDrawable(url: String): Drawable? {
        return cacheMap[url]
    }
}