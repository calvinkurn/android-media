package com.tokopedia.imagepreview.imagesecure

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.design.image.TouchImageView
import com.tokopedia.imagepreview.utils.loadPreviewImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageProcessingUtil.getBitmapFromPath

/**
 * Kotlin version of [com.tokopedia.design.list.adapter.TouchImageAdapter]
 * The main purpose of this change is to support load image secure
 */
class TouchImageAdapterKt(
    private val fileLoc: ArrayList<String>?,
    private val userSessionInterface: UserSessionInterface,
    private val isSecure: Boolean
) : PagerAdapter() {

    interface OnImageStateChange {
        fun onStateDefault()
        fun onStateZoom()
    }

    private var imageStateChangeListener: OnImageStateChange? = null

    fun setOnImageStateChangeListener(Listener: OnImageStateChange?) {
        imageStateChangeListener = Listener
    }

    override fun getCount(): Int {
        return fileLoc?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = TouchImageView(container.context) { stateSize: Float ->
            if (stateSize <= 1) {
                imageStateChangeListener?.onStateDefault()
            } else {
                imageStateChangeListener?.onStateZoom()
            }
        }
        handleLoadImage(container, position, imageView)
        return imageView
    }

    private fun handleLoadImage(container: ViewGroup, position: Int, imageView: ImageView) {
        try {
            val source = fileLoc?.get(position)?: ""
            if (URLUtil.isNetworkUrl(source)) {
                handleUrlImage(imageView, source)
            } else {
                loadImageFromFile(imageView, source)
            }
            container.addView(imageView, 0)
        } catch (ignored: Exception) {}
    }

    private fun handleUrlImage(imageView: ImageView, source: String) {
        imageView.loadPreviewImage(source, userSessionInterface, isSecure)
    }

    private fun loadImageFromFile(imageView: ImageView, thumbnail: String) {
        if (!TextUtils.isEmpty(thumbnail)) {
            val bitmap = getBitmapFromPath(thumbnail, DEF_WIDTH, DEF_HEIGHT, false)
            imageView.loadImage(bitmap)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as TouchImageView)
    }

    companion object {
        private const val DEF_WIDTH = 2560
        private const val DEF_HEIGHT = 2560
    }
}