package com.tokopedia.shop.home.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeCampaignNplTncItemViewHolder

class ShopHomeCampaignNplTncAdapter : RecyclerView.Adapter<ShopHomeCampaignNplTncItemViewHolder>() {

    private var listMessage: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHomeCampaignNplTncItemViewHolder {
        return ShopHomeCampaignNplTncItemViewHolder(parent.inflateLayout(R.layout.layout_shop_home_campaign_npl_tnc_item))
    }

    override fun getItemCount(): Int {
        return listMessage.size
    }

    override fun onBindViewHolder(holder: ShopHomeCampaignNplTncItemViewHolder, position: Int) {
        holder.bind(position, listMessage[position])
    }

    fun setListMessageDat(listMessage: List<String>) {
        this.listMessage = listMessage
        notifyDataSetChanged()
    }
}
