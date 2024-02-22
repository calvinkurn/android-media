package com.tokopedia.app.common

import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify

object ImageUnifyInit {
    @JvmStatic
    fun setImageCallback() {
        ImageUnify.setLoadImageCallback { imageView, url ->
            imageView.loadImage(url)
        }
    }
}