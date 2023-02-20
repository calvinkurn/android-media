package com.tokopedia.media.loader.options

import android.graphics.Bitmap
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.tokopedia.media.loader.data.Properties

class TransformationOptions(private val properties: Properties) : RequestOptions() {

    private val transforms = arrayListOf<Transformation<Bitmap>>()

    init {
        transforms.clear()

        singleTransform()
        multiTransform()
        setCenterCrop()
        setFitCenter()
        setCenterInside()
        setRoundedCorners()

        if (transforms.isNotEmpty()) {
            transform(MultiTransformation(transforms))
        }
    }

    private fun setCenterCrop() {
        if (properties.centerCrop) {
            transforms.add(CenterCrop())
        }
    }

    private fun setFitCenter() {
        if (properties.fitCenter) {
            transforms.add(FitCenter())
        }
    }

    private fun setCenterInside() {
        if (properties.centerInside) {
            transforms.add(CenterInside())
        }
    }

    private fun setRoundedCorners() {
        if (properties.roundedRadius > 0f) {
            transforms.add(RoundedCorners(properties.roundedRadius.toInt()))
        }
    }

    private fun singleTransform() {
        properties.transform?.let {
            transforms.add(it)
        }
    }

    private fun multiTransform() {
        properties.transforms?.let {
            transforms.addAll(it)
        }
    }

    companion object {
        @Volatile
        private var factory: TransformationOptions? = null

        fun build(properties: Properties): RequestOptions {
            return factory ?: TransformationOptions(properties).also {
                factory = it
            }
        }
    }
}
