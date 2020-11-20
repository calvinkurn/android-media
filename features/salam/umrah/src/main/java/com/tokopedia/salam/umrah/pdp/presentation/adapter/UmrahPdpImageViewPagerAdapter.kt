package com.tokopedia.salam.umrah.pdp.presentation.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.salam.umrah.R
import kotlinx.android.synthetic.main.layout_umrah_image_slider.view.*

/**
 * @author by M on 8/11/19
 */

class UmrahPdpImageViewPagerAdapter : PagerAdapter() {
    var imageUrls = emptyList<String>()
    var onClickListener : SetOnClickListener? = null
    interface SetOnClickListener{
        fun onClick(position: Int)
    }
    override fun isViewFromObject(p0: View, p1: Any): Boolean = p0 == p1

    override fun getCount(): Int = imageUrls.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
                .inflate(R.layout.layout_umrah_image_slider, container, false)
        container.addView(view)
        bind(imageUrls[position], view)
        view.image_banner.setOnClickListener {
            onClickListener?.onClick(position)
        }
        return view
    }

    private fun bind(imageUrl: String, view: View) {
        view.image_banner.loadImage(imageUrl, R.drawable.umrah_loading_image)
    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View?)
    }
}

