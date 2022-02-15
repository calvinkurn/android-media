package com.tokopedia.media.picker.utils

import android.content.Context
import android.widget.ImageView
import com.google.android.material.tabs.TabLayout
import com.tokopedia.config.GlobalConfig
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.R
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File

fun exceptionHandler(invoke: () -> Unit) {
    try {
        invoke()
    } catch (e: Exception) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace()
        }
    }
}

fun TabLayout?.addOnTabSelected(
    selected: (Int) -> Unit
) = this?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.position?.let { selected(it) }
    }
    override fun onTabReselected(tab: TabLayout.Tab?) {}
    override fun onTabUnselected(tab: TabLayout.Tab?) {}
})

fun Context.dimensionPixelOffsetOf(dimen: Int)
    = resources.getDimensionPixelOffset(dimen)

fun Context.dimensionOf(dimen: Int)
    = resources.getDimension(dimen)

// TODO create shared-component for this
fun ImageView.pickerLoadImage(path: String) {
    val thumbnailSize = context.dimensionPixelOffsetOf(R.dimen.picker_thumbnail_size)
    val roundedSize = context.dimensionOf(R.dimen.picker_thumbnail_rounded)

    val isFitCenter = File(path).let {
        if (it.exists()) {
            ImageProcessingUtil.shouldLoadFitCenter(it)
        } else {
            false
        }
    }

    loadImage(path) {
        overrideSize(Resize(thumbnailSize, thumbnailSize))
        setRoundedRadius(roundedSize)
        isAnimate(true)
        setPlaceHolder(-1)

        if (isFitCenter) {
            fitCenter()
        } else {
            centerCrop()
        }
    }

}