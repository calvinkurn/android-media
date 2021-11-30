package com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.paylater.domain.model.Benefit
import com.tokopedia.pdpsimulation.paylater.presentation.detail.viewholder.PayLaterOfferDescriptionViewHolder

class PayLaterOfferDescriptionAdapter(private val offerItemList: List<Benefit?>?) :
    RecyclerView.Adapter<PayLaterOfferDescriptionViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PayLaterOfferDescriptionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PayLaterOfferDescriptionViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PayLaterOfferDescriptionViewHolder, position: Int) {
        val descriptionData = offerItemList?.get(position)
        holder.bindData(descriptionData)
    }

    override fun getItemCount(): Int {
        return offerItemList?.size ?: 0
    }
}