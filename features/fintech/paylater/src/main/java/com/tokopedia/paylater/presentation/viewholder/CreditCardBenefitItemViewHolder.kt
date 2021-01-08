package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.R
import kotlinx.android.synthetic.main.paylater_cards_content_info_item.view.*

class CreditCardBenefitItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData() {
        view.apply {
            tvBenefitsDesc.text = "Dapatkan Cashback & Tokopedia Gift Card total Rp1 Juta dengan ajukan sekarang"
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.paylater_cards_content_info_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardBenefitItemViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}