package com.tokopedia.content.product.preview.view.listener

import com.tokopedia.content.product.preview.view.components.ProductPreviewExoPlayer

interface ProductPreviewListener {

    fun getVideoPlayer(id: String): ProductPreviewExoPlayer

}
