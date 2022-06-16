package com.tokopedia.tokopedianow.common.util

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.View
import com.tokopedia.media.loader.loadImageWithTarget
import com.tokopedia.media.loader.utils.MediaTarget

object ImageUtil {
    fun setBackgroundImage(context: Context, url: String, view: View?) {
        view?.let {
            loadImageWithTarget(context = context, url = url, {
                useBlurHash(true)
            }, MediaTarget(view, onReady = { _, resource ->
                it.background = BitmapDrawable(it.resources, resource)
            }))
        }
    }
}