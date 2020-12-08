package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.OfferDescriptionItem
import com.tokopedia.paylater.domain.model.PayLaterPartnerBenefit
import com.tokopedia.paylater.presentation.viewholder.PayLaterOfferDescriptionViewHolder

class PayLaterOfferDescriptionAdapter(private val offerItemList: List<PayLaterPartnerBenefit>) : RecyclerView.Adapter<PayLaterOfferDescriptionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayLaterOfferDescriptionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PayLaterOfferDescriptionViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PayLaterOfferDescriptionViewHolder, position: Int) {
        val descriptionData = offerItemList[position]
        holder.bindData(descriptionData)
    }

    override fun getItemCount(): Int {
        return offerItemList.size
    }
}