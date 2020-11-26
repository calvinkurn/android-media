package com.tokopedia.paylater.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.domain.model.OfferListResponse
import com.tokopedia.paylater.presentation.viewholder.PayLaterOfferItemViewHolder

class PayLaterOffersAdapter(val offerList: ArrayList<OfferListResponse>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PayLaterOfferItemViewHolder.getViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val offerData = offerList[position]
        (holder as PayLaterOfferItemViewHolder).bindData(
                offerData
        )
    }

    override fun getItemCount(): Int {
        return offerList.size
    }


}