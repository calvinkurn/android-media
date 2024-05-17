package com.tokopedia.media.loaderfresco.data

data class Properties(
    internal var data: Any? = null,
    internal var roundedRadius: Float = 0f,
    internal var error: Int = ERROR_RES_UNIFY,
    internal var placeHolder: Int = 0
) {

    fun setSource(data: Any?) = apply {
        this.data = data
    }

    fun setRoundedRadius(radius: Float) = apply {
        this.roundedRadius = radius
    }

    fun setErrorDrawable(resourceId: Int) = apply {
        this.error = resourceId
    }

    fun setPlaceHolder(resourceId: Int) = apply {
        this.placeHolder = resourceId
    }

}
