package com.tokopedia.salam.umrah.pdp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.salam.umrah.R
import kotlinx.android.synthetic.main.item_umrah_pdp_hotel_images.view.*

/**
 * @author by M on 30/10/19
 */
class UmrahPdpHotelImagesAdapter(private val imageUrls: List<String>) : RecyclerView.Adapter<UmrahPdpHotelImagesAdapter.UmrahPdpHotelImagesViewHolder>() {

    var clickListener: UmrahPdpHotelImagesListener? = null
    var itemPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahPdpHotelImagesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_pdp_hotel_images, parent, false)
        return UmrahPdpHotelImagesViewHolder(view)
    }

    override fun getItemCount(): Int = imageUrls.size

    override fun onBindViewHolder(holder: UmrahPdpHotelImagesViewHolder, position: Int) {
        holder.bind(imageUrls[position])
        holder.itemView.setOnClickListener {
            clickListener?.onImageClicked(itemPosition, position)
        }
    }

    inner class UmrahPdpHotelImagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(imageUrl: String) {
            with(itemView) {
                iv_umrah_pdp_hotel_image.loadImage(imageUrl)
            }
        }
    }

    interface UmrahPdpHotelImagesListener {
        fun onImageClicked(itemPosition: Int, position: Int)
    }
}
