package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.CreditCardBank
import kotlinx.android.synthetic.main.base_payment_register_item.view.*
import kotlinx.android.synthetic.main.credit_card_available_bank_item.view.*
import kotlinx.android.synthetic.main.credit_card_item.view.*

class CreditCardItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData() {
        view.apply {
            ivRecommendationBadge.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_credit_card_bg_recommendation))
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardItemViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}