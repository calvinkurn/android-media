package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.R

class CreditCardPdpInfoButtonViewHolder(val view: View) : RecyclerView.ViewHolder(view) {


    companion object {
        private val LAYOUT_ID = R.layout.credit_card_pdp_meta_info_button

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardPdpInfoButtonViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}