package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.unifyprinciples.Typography

class ShopHomeFlashSaleTncViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var tncNumberView: Typography? = itemView.findViewById(R.id.tgp_tnc_number)
    private var tncDescriptionView: Typography? = itemView.findViewById(R.id.tgp_tnc_description)

    fun bind(position: Int, description: String) {
        tncNumberView?.text = position.plus(1).toString()
        tncDescriptionView?.text = description
    }
}
