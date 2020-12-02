package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.R
import com.tokopedia.paylater.presentation.adapter.PayLaterOfferDescriptionAdapter

class PayLaterPaymentFaqViewHolder(view: View): RecyclerView.ViewHolder(view) {


    companion object {
        private val LAYOUT_ID = R.layout.paylater_card_faq_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = PayLaterPaymentFaqViewHolder(
                inflater.inflate(PayLaterPaymentFaqViewHolder.LAYOUT_ID, parent, false)
        )
    }
}