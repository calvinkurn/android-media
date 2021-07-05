package com.tokopedia.pdpsimulation.creditcard.presentation.simulation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R

class CreditCardBankShimmerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_shimmer_bank_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardBankShimmerViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}