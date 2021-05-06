package com.tokopedia.pdpsimulation.creditcard.presentation.tnc.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R
import kotlinx.android.synthetic.main.credit_card_pdp_meta_info_button.view.*

class CreditCardPdpInfoButtonViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(labelName: String?) {
        view.btnMoreInfo.text = labelName
                ?: view.context.getString(R.string.credit_card_tnc_more_info_text)
    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_pdp_meta_info_button

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardPdpInfoButtonViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}