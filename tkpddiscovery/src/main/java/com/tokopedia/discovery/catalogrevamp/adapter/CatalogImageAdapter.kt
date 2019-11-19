package com.tokopedia.discovery.catalogrevamp.adapter

import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.discovery.R
import com.tokopedia.discovery.catalogrevamp.model.ProductCatalogResponse.ProductCatalogQuery.Data.Catalog.CatalogImage
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_catalog_image.view.*

class CatalogImageAdapter(val list: List<CatalogImage>, val listener: Listener?) : PagerAdapter(){

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = list.size


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(
                R.layout.item_catalog_image,
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

    class CatalogImageViewHolder(val itemView: View, val listener: Listener?) {
        fun bind(model: CatalogImage) {
            itemView.image.loadImage(model.imageUrl)
            itemView.setOnClickListener {
                listener?.onImageClick()
            }
        }
    }

    interface Listener{
        fun onImageClick()
    }
}