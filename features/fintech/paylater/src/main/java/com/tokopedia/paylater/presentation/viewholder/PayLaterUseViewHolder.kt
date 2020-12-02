package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.R
import com.tokopedia.paylater.presentation.adapter.PayLaterOfferDescriptionAdapter

class PayLaterUseViewHolder(view: View, clickListener: (Int) -> Unit): RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener { clickListener(PayLaterOfferDescriptionAdapter.VIEW_HOW_TO_REGISTER_BUTTON) }
    }

    companion object {
        private val LAYOUT_ID = R.layout.paylater_payment_option_filled_button_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup, clickListener: (Int) -> Unit) = PayLaterUseViewHolder(
                inflater.inflate(PayLaterUseViewHolder.LAYOUT_ID, parent, false), clickListener
        )
    }
}