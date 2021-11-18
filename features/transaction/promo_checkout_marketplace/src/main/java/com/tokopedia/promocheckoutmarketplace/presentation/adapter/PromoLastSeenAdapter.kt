package com.tokopedia.promocheckoutmarketplace.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promocheckoutmarketplace.databinding.PromoCheckoutMarketplaceModuleItemPromoLastSeenBinding
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutLastSeenListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoLastSeenItemUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.viewholder.PromoLastSeenViewHolder

class PromoLastSeenAdapter(val actionListener: PromoCheckoutLastSeenListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = ArrayList<PromoLastSeenItemUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewBinding = PromoCheckoutMarketplaceModuleItemPromoLastSeenBinding.inflate(LayoutInflater.from(parent.context), parent as ViewGroup, false)
        return PromoLastSeenViewHolder(viewBinding, actionListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PromoLastSeenViewHolder).bindData(data[position])
    }

}