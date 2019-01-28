package com.tokopedia.product.detail.view.adapter

import android.content.Context
import android.content.res.Configuration
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.product.detail.data.model.Picture

class PicturePagerAdapter(private val context: Context,
                          private val pictures: MutableList<Picture> = mutableListOf(),
                          private var urlTemp: String? = null): PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = pictures.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val urlImage = pictures[position].urlOriginal
        val imageView = ImageView(context)
        imageView.adjustViewBounds = true
        val currentOrientation = context.resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER

            if (!urlTemp.isNullOrEmpty() && position == 0){
                Glide.with(context).load(urlImage)
                        .dontAnimate().dontTransform().fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .thumbnail(Glide.with(context).load(urlTemp)
                                .dontAnimate().dontTransform().fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                        .into(imageView)
            } else if (!urlImage.isEmpty()){
                Glide.with(context)
                        .load(if (urlImage.equals(urlTemp, true)) urlTemp else urlImage)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .fitCenter()
                        .into(imageView)
            }
        } else {
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP

            if (!urlTemp.isNullOrEmpty() && position == 0){
                Glide.with(context).load(urlImage)
                        .dontAnimate().dontTransform().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .thumbnail(Glide.with(context).load(urlTemp)
                                .dontAnimate().dontTransform().centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE))
                        .into(imageView)
            } else if (!urlImage.isEmpty()){
                Glide.with(context)
                        .load(if (urlImage.equals(urlTemp, true)) urlTemp else urlImage)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .into(imageView)
            }
        }
        container.addView(imageView, 0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }

    fun addAllImages(images: List<Picture>){
        pictures.clear()
        pictures.addAll(images.sortedWith(object : Comparator<Picture> {
            override fun compare(o1: Picture, o2: Picture): Int = o1.status.compareTo(o2.status)
        }))
        notifyDataSetChanged()
    }

    fun addFirst(picture: Picture){
        pictures.clear()
        pictures.add(picture)
        urlTemp = picture.urlOriginal
        notifyDataSetChanged()
    }
}