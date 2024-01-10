package com.tokopedia.hotel.evoucher.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemHotelShareAsPdfBinding

/**
 * @author by furqan on 17/05/19
 */
class HotelShareAsPdfAdapter(private var emailList: List<String>, private var listener: ShareAsPdfListener) : RecyclerView.Adapter<HotelShareAsPdfAdapter.HotelShareAsPdfViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelShareAsPdfViewHolder =
            HotelShareAsPdfViewHolder(LayoutInflater.from(parent.context).inflate(
                    HotelShareAsPdfViewHolder.LAYOUT, parent, false), listener)

    override fun getItemCount(): Int = emailList.size

    override fun onBindViewHolder(holder: HotelShareAsPdfViewHolder, position: Int) {
        holder.bind(emailList[position])
    }

    interface ShareAsPdfListener {
        fun onDelete(email: String)
    }

    class HotelShareAsPdfViewHolder(val view: View, val listener: ShareAsPdfListener) : RecyclerView.ViewHolder(view) {

        private val binding = ItemHotelShareAsPdfBinding.bind(view)

        fun bind(element: String) {
            with(binding) {
                tvEmail.text = element
                ivDelete.setOnClickListener {
                    listener.onDelete(element)
                }
            }
        }

        companion object {
            val LAYOUT = R.layout.item_hotel_share_as_pdf
        }

    }
}
