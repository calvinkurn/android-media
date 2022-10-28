package com.tokopedia.digital_product_detail.presentation.adapter.viewholder

import android.text.Html
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital_product_detail.databinding.ViewMoreInfoBinding

class DigitalPDPViewMoreInfoViewHolder(private val binding: ViewMoreInfoBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(element: String){
        with(binding){
            tgViewMoreInfo.setText(Html.fromHtml(element))
        }
    }
}