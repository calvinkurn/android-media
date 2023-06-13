package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.databinding.LayoutShopHomeCampaignNplTncItemBinding
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeCampaignNplTncItemViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    private val viewBinding: LayoutShopHomeCampaignNplTncItemBinding? by viewBinding()
    private val tvMessageCounter: Typography? = viewBinding?.tvMessageCounter
    private val tvMessage: Typography? = viewBinding?.tvMessage

    fun bind(position: Int, message: String) {
        tvMessageCounter?.text = position.plus(1).toString()
        tvMessage?.text = message
    }
}
