package com.tokopedia.profile.view.adapter

import android.app.Fragment
import android.graphics.Bitmap
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.profile.R

/**
 * @author by milhamj on 9/21/18.
 */
class PostImageAdapter: PagerAdapter() {
    val imageList: ArrayList<String> = ArrayList()

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = imageList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(
                R.layout.item_post_image,
                container,
                false
        )
        val imageView = view.findViewById<ImageView>(R.id.image)
//        val imageCardView = view.findViewById<CardView>(R.id.imageCardView)
        val imageUrl = imageList[position]

        imageView.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val viewTreeObserver = imageView.getViewTreeObserver()
                        viewTreeObserver.removeOnGlobalLayoutListener(this)

                        imageView.maxHeight = imageView.width
                        imageView.requestLayout()
                    }
                }
        )
        ImageHandler.loadImageRounded2(imageView.context, imageView, imageUrl)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun setList(imageList: ArrayList<String>) {
        this.imageList.addAll(imageList)
        notifyDataSetChanged()
    }
}