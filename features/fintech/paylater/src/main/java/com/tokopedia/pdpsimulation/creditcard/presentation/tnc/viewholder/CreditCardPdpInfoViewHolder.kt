package com.tokopedia.pdpsimulation.creditcard.presentation.tnc.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardPdpInfoContent
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.credit_card_pdp_meta_info_item.view.*

class CreditCardPdpInfoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(creditCardPdpInfoContent: CreditCardPdpInfoContent) {
        view.apply {
            tvTitlePdpInfo.text = creditCardPdpInfoContent.title
            if (creditCardPdpInfoContent.description?.isNotEmpty() == true) {
                tvDescription.visible()
                tvDescription.text = creditCardPdpInfoContent.description
            } else tvDescription.gone()
            if (creditCardPdpInfoContent.notesList?.isNotEmpty() == true) {
                for (note in creditCardPdpInfoContent.notesList) {
                    val noteText = Typography(context)
                    noteText.text = note
                    noteText.setType(Typography.BODY_3)
                    llNotes.addView(noteText)
                }
            }
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_pdp_meta_info_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardPdpInfoViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}