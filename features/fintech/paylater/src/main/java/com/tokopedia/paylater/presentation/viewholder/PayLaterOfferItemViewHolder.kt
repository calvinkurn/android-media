package com.tokopedia.paylater.presentation.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.OfferListResponse
import com.tokopedia.paylater.presentation.adapter.PayLaterOfferDescriptionAdapter
import kotlinx.android.synthetic.main.paylater_cards_info_item.view.*

class PayLaterOfferItemViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val offerTitleName : TextView = view.tvTitlePaylaterPartner
    private val rvOfferDetails: RecyclerView = view.rvPaylaterCards
    private val context: Context = view.context

    fun bindData(offerData: OfferListResponse) {
        offerTitleName.text = offerData.payLaterOfferName
        rvOfferDetails.apply {
            adapter = PayLaterOfferDescriptionAdapter(offerData.offerItemList)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
        }

    }


    companion object {
        val LAYOUT_ID = R.layout.paylater_cards_info_item
        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = PayLaterOfferItemViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}