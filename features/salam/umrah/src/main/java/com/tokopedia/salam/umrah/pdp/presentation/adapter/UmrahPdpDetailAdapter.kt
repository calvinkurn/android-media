package com.tokopedia.salam.umrah.pdp.presentation.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahVariant
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter.getRupiahFormat
import kotlinx.android.synthetic.main.item_umrah_pdp_detail_room.view.*

/**
 * @author by M on 31/10/2019
 */
class UmrahPdpDetailAdapter : RecyclerView.Adapter<UmrahPdpDetailAdapter.UmrahPdpDetailViewHolder>() {
    var variants = emptyList<UmrahVariant>()
    var selectedItem: String = ""
    lateinit var pdpDetailListener: UmrahPdpDetailListener

    fun updateSelectedItem(itemSelected: String) {
        selectedItem = itemSelected
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahPdpDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_pdp_detail_room, parent, false)
        return UmrahPdpDetailViewHolder(view)
    }

    override fun getItemCount(): Int = variants.size

    override fun onBindViewHolder(holder: UmrahPdpDetailViewHolder, position: Int) {
        holder.bind(variants[position])
    }

    inner class UmrahPdpDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(variant: UmrahVariant) {
            with(itemView) {
                item_rbu_umrah_pdp_detail_room.apply {
                    isChecked = variant.name == selectedItem
                    setOnClickListener {
                        selectedItem = variant.name
                        notifyDataSetChanged()
                        pdpDetailListener.onRoomClicked(variant)
                    }
                }
                setOnClickListener {
                    selectedItem = variant.name
                    notifyDataSetChanged()
                    pdpDetailListener.onRoomClicked(variant)
                }
                item_tg_umrah_pdp_detail_room_title.text = variant.name
                val rpPrice = getRupiahFormat(variant.price)
                val slashedRpPrice = getRupiahFormat(variant.slashPrice)
                item_tg_umrah_pdp_detail_room_desc.text = variant.desc
                item_tg_umrah_pdp_detail_room_price.text = getStylingPriceString(context, resources, slashedRpPrice, rpPrice)
            }
        }
    }

    fun getStylingPriceString(context: Context, resources: Resources, slashedPrice: String, price: String): SpannableString {
        val spannablePrice = SpannableString(resources.getString(R.string.umrah_pdp_detail_choose_room_desc, slashedPrice, price))
        val slashedPriceLength = slashedPrice.length
        val priceLength = price.length
        val colorPrice = ForegroundColorSpan(ContextCompat.getColor(context, R.color.Yellow_Y500))
        val stylePrice = StyleSpan(Typeface.BOLD)
        spannablePrice.setSpan(StrikethroughSpan(), 0, slashedPriceLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannablePrice.setSpan(colorPrice, slashedPriceLength + 1, slashedPriceLength + 1 + priceLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannablePrice.setSpan(stylePrice, slashedPriceLength + 1, slashedPriceLength + 1 + priceLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannablePrice
    }

    interface UmrahPdpDetailListener {
        fun onRoomClicked(umrahVariant: UmrahVariant)
    }
}
