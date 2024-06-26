package com.tokopedia.content.product.preview.view.listener

interface MediaImageListener {
    fun onImpressedImage()
    fun onDoubleTapImage() {}

    fun onImageInteraction(isScalingMode: Boolean) {}
}
