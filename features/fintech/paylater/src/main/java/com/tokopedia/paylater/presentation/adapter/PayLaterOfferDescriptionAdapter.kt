package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.OfferDescriptionItem
import com.tokopedia.paylater.presentation.viewholder.PayLaterOfferDescriptionViewHolder

class PayLaterOfferDescriptionAdapter(val offerItemList: List<OfferDescriptionItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PayLaterOfferDescriptionViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val descriptionData = offerItemList[position]
        (holder as PayLaterOfferDescriptionViewHolder).bindData(descriptionData)
    }

    override fun getItemCount(): Int {
        return offerItemList.size
    }
}