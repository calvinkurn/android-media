package com.tokopedia.profile.view.adapter

import android.graphics.Bitmap
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.common.utils.image.ImageHandler

/**
 * @author by milhamj on 9/21/18.
 */
class PostImageAdapter: PagerAdapter() {
    val imageList: ArrayList<String> = ArrayList()

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = imageList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(container.context)
        val imageUrl = imageList[position]

        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.adjustViewBounds = true
        imageView.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val viewTreeObserver = imageView.getViewTreeObserver()
                        viewTreeObserver.removeOnGlobalLayoutListener(this)

                        imageView.setMaxHeight(imageView.getWidth())
                        imageView.requestLayout()
                    }
                }
        )
        ImageHandler.loadImageWithTarget(
                container.context, imageUrl,
                object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap,
                                                 glideAnimation: GlideAnimation<in Bitmap>) {
                        imageView.setImageBitmap(resource)
                    }
                }
        )

        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }

    fun setList(imageList: ArrayList<String>) {
        this.imageList.addAll(imageList)
        notifyDataSetChanged()
    }
}