package com.tokopedia.salam.umrah.pdp.presentation.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahHotel
import com.tokopedia.salam.umrah.common.util.UmrahSpaceItemDecoration
import kotlinx.android.synthetic.main.item_umrah_pdp_hotel.view.*

/**
 * @author by M on 30/10/19
 */
class UmrahPdpHotelAdapter : RecyclerView.Adapter<UmrahPdpHotelAdapter.UmrahPdpHotelViewHolder>() {
    var hotels = emptyList<UmrahHotel>()
    var onImageListener: UmrahPdpHotelImagesAdapter.UmrahPdpHotelImagesListener? = null
    lateinit var onScrollListener: OnScrollListener

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahPdpHotelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_pdp_hotel, parent, false)
        return UmrahPdpHotelViewHolder(view)
    }

    override fun getItemCount(): Int = hotels.size

    override fun onBindViewHolder(holder: UmrahPdpHotelViewHolder, position: Int) {
        holder.apply {
            bind(hotels[position])
        }
    }

    inner class UmrahPdpHotelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(umrahHotel: UmrahHotel) {
            with(itemView) {
                tg_umrah_pdp_rv_hotel_name.text = umrahHotel.name
                tg_umrah_pdp_hotel_check_in.text = resources.getString(R.string.umrah_pdp_hotel_stay_dates, umrahHotel.ui.stayDates)
                tg_umrah_pdp_hotel_distance.text = umrahHotel.ui.distance
                rb_umrah_pdp_hotel_rating.apply {
                    numStars = umrahHotel.rating
                    rating = umrahHotel.rating.toFloat()
                }
                rv_umrah_pdp_hotel_images.apply {
                    isNestedScrollingEnabled = false
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    adapter = UmrahPdpHotelImagesAdapter(umrahHotel.imageUrls).apply {
                        clickListener = onImageListener
                        itemPosition = adapterPosition
                    }
                    while (itemDecorationCount > 0) removeItemDecorationAt(0)
                    addItemDecoration(UmrahSpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2), RecyclerView.HORIZONTAL))
                    addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                            onScrollListener.onScrollStateChanged()
                        }
                    })
                }
            }
        }
    }

    interface OnScrollListener {
        fun onScrollStateChanged()
    }

}