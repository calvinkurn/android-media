package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.unifyprinciples.Typography


class ShopHomeCampaignNplTncItemViewHolder(
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val tvMessageCounter: Typography? = itemView.findViewById(R.id.tv_message_counter)
    private val tvMessage: Typography? = itemView.findViewById(R.id.tv_message)

    fun bind(position: Int, message: String) {
        tvMessageCounter?.text = position.plus(1).toString()
        tvMessage?.text = message
    }
}