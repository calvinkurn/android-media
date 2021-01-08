package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.CreditCardBank
import kotlinx.android.synthetic.main.credit_card_available_bank_item.view.*

class CreditCardBankShimmerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_shimmer_bank_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardBankShimmerViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}