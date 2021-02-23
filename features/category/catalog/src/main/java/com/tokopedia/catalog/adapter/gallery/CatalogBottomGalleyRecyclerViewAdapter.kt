package com.tokopedia.catalog.adapter.gallery

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.catalog.R
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_catalog_gallery_bottom_image_view.view.*

class CatalogBottomGalleyRecyclerViewAdapter(val list: ArrayList<CatalogImage>, val listener: Listener?,
                                             private var selectedPosition:Int) :  RecyclerView.Adapter<CatalogBottomGalleyRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_catalog_gallery_bottom_image_view, null))
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], listener, selectedPosition)
    }

    fun changeSelectedPosition(selectedPosition:Int){
        this.selectedPosition = selectedPosition
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(model: CatalogImage, listener: Listener?, selectedPosition: Int) {
            if(adapterPosition == selectedPosition){
                itemView.image.alpha = 1.0F
            } else {
                itemView.image.alpha = 0.5F
            }
            itemView.image.loadImage(model.imageURL)
            itemView.setOnClickListener {
                listener?.onImageClick(adapterPosition)
            }
        }
    }

    interface Listener{
        fun onImageClick(adapterPosition: Int)
    }
}