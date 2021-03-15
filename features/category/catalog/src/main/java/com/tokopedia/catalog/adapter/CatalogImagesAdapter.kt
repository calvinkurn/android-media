package com.tokopedia.catalog.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_catalog_image.view.*

class CatalogImagesAdapter(val list: ArrayList<CatalogImage>,
                           val listener: CatalogDetailListener?)
    :  RecyclerView.Adapter<CatalogImagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_catalog_image, null))
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], listener)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(model: CatalogImage, catalogDetailListener: CatalogDetailListener?) {
            model.imageURL?.let {
                itemView.image.loadImage(it)
            }
            itemView.setOnClickListener {
                catalogDetailListener?.onProductImageClick(model,adapterPosition)
            }
        }
    }
}