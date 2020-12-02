package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.R
import com.tokopedia.paylater.presentation.adapter.PayLaterOfferDescriptionAdapter.Companion.VIEW_SEE_MORE_BUTTON

class PayLaterSeeMoreViewHolder(view: View, clickListener: (Int) -> Unit): RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener { clickListener(VIEW_SEE_MORE_BUTTON) }
    }
    companion object {
        private val LAYOUT_ID = R.layout.paylater_payment_option_ghost_button_item
        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup, clickListener: (Int) -> Unit) = PayLaterSeeMoreViewHolder(
                inflater.inflate(PayLaterSeeMoreViewHolder.LAYOUT_ID, parent, false), clickListener
        )
    }
}