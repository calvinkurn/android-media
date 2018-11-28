package com.tokopedia.kol.feature.post.view.adapter

import android.os.Build
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.affiliatecommon.R

/**
 * @author by milhamj on 9/21/18.
 */
class PostPagerAdapter(var clickListener: ImageClickListener?) : PagerAdapter() {

    constructor() : this(null)

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
        val imageUrl = imageList[position]

        view.setOnClickListener { clickListener?.onImageClick(position) }
        imageView.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val viewTreeObserver = imageView.viewTreeObserver
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            viewTreeObserver.removeOnGlobalLayoutListener(this)
                        } else {
                            @Suppress("DEPRECATION")
                            viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }

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

    override fun getItemPosition(`object`: Any) = PagerAdapter.POSITION_NONE

    fun setList(imageList: ArrayList<String>) {
        this.imageList.clear()
        this.imageList.addAll(imageList)
        notifyDataSetChanged()
    }

    interface ImageClickListener {
        fun onImageClick(position: Int)
    }
}