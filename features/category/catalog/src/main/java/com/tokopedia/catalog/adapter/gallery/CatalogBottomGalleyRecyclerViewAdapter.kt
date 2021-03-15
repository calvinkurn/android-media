package com.tokopedia.catalog.adapter.gallery

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_catalog_gallery_bottom_image_view.view.*

class CatalogBottomGalleyRecyclerViewAdapter(val list: ArrayList<CatalogImage>, private val onImageClick : (Int) -> Unit,
                                             private var selectedPosition:Int) :  RecyclerView.Adapter<CatalogBottomGalleyRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_catalog_gallery_bottom_image_view, null))
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], onImageClick, selectedPosition)
    }

    fun changeSelectedPosition(selectedPosition:Int){
        this.selectedPosition = selectedPosition
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(model: CatalogImage, onImageClick : (Int) -> Unit , selectedPosition: Int) {
            if(adapterPosition == selectedPosition){
                itemView.image.alpha = 1.0F
            } else {
                itemView.image.alpha = 0.5F
            }
            model.imageURL?.let {
                itemView.image.loadImage(it)
            }
            itemView.setOnClickListener {
                onImageClick(adapterPosition)
            }
        }
    }
}