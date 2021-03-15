package com.tokopedia.catalog.adapter.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.catalog.R
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_catalog_gallery_image.view.*

class CatalogGalleryImagePagerAdapter(val list: List<CatalogImage>, val listener: Listener?)
    : PagerAdapter(){

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = list.size


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(
                CatalogImageViewHolder.LAYOUT,
                container,
                false
        )
        val viewHolder = CatalogImageViewHolder(view, listener)
        viewHolder.bind(list[position])

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    class CatalogImageViewHolder(private val itemView: View, val listener: Listener?) {

        fun bind(model: CatalogImage) {
            model.imageURL?.let {
                itemView.image.loadImage(it)
            }
            itemView.setOnClickListener {
                listener?.onImageClick()
            }
        }
        companion object{
            val LAYOUT = R.layout.item_catalog_gallery_image
        }
    }

    interface Listener{
        fun onImageClick()
    }
}