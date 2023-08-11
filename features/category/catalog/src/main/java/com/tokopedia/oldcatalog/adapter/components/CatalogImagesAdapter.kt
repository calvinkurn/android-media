package com.tokopedia.oldcatalog.adapter.components

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.raw.CatalogImage
import com.tokopedia.oldcatalog.viewholder.components.CatalogImagesViewHolder

class CatalogImagesAdapter(val list: ArrayList<CatalogImage>,
                           val listener: CatalogDetailListener?)
    :  RecyclerView.Adapter<CatalogImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogImagesViewHolder {
        return CatalogImagesViewHolder(View.inflate(parent.context, R.layout.item_catalog_image, null))
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: CatalogImagesViewHolder, position: Int) {
        holder.bind(list[position], listener)
    }
}
