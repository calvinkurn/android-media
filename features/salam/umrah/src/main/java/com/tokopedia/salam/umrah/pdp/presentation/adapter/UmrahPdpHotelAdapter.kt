package com.tokopedia.salam.umrah.pdp.presentation.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.list.adapter.SpaceItemDecoration
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahHotel
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getDate
import kotlinx.android.synthetic.main.item_umrah_pdp_hotel.view.*

/**
 * @author by M on 30/10/19
 */
class UmrahPdpHotelAdapter : RecyclerView.Adapter<UmrahPdpHotelAdapter.UmrahPdpHotelViewHolder>() {
    var hotels = emptyList<UmrahHotel>()
    var onImageListener: UmrahPdpHotelImagesAdapter.UmrahPdpHotelImagesListener? = null
    lateinit var onScrollListener: OnScrollListener
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahPdpHotelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_pdp_hotel,parent,false)
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
            with(itemView){
                tg_umrah_pdp_rv_hotel_name.text = umrahHotel.name
                tg_umrah_pdp_hotel_check_in.text = resources.getString(R.string.umrah_pdp_hotel_check_in,getDate("dd",umrahHotel.checkIn), getDate("dd MMM yyyy",umrahHotel.checkOut))
                tg_umrah_pdp_hotel_distance.text = umrahHotel.poiDistances
                rb_umrah_pdp_hotel_rating.apply {
                    numStars = umrahHotel.rating
                    rating = umrahHotel.rating.toFloat()
                }
                rv_umrah_pdp_hotel_images.apply {
                    layoutManager = LinearLayoutManager(context,LinearLayout.HORIZONTAL,false)
                    adapter = UmrahPdpHotelImagesAdapter(umrahHotel.imageUrls).apply {
                        clickListener = onImageListener
                        itemPosition = adapterPosition
                    }
                    addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_12), LinearLayoutManager.HORIZONTAL))
                    addOnScrollListener(object : RecyclerView.OnScrollListener(){
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            onScrollListener.onScrolled()
                        }
                    })
                }
            }
        }
    }
    interface OnScrollListener{
        fun onScrolled()
    }

}