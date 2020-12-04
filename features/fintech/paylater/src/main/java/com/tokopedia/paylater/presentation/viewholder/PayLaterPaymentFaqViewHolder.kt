package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.PayLaterPartnerFaq
import com.tokopedia.paylater.presentation.adapter.PayLaterOfferDescriptionAdapter
import kotlinx.android.synthetic.main.paylater_card_faq_item.view.*

class PayLaterPaymentFaqViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    fun bindData(faqData: PayLaterPartnerFaq, notifyItem: () -> Unit) {
        setLayout(faqData)
        view.setOnClickListener {
            faqData.expandLayout = !faqData.expandLayout
            setLayout(faqData)
            notifyItem()
        }
    }

    private fun setLayout(faqData: PayLaterPartnerFaq) {
        if(faqData.expandLayout) {
            view.ivArrowBottom.animate().rotation(180f).duration = 300
            view.tvFaqDescription.text = faqData.faqAnswer
            view.tvFaqDescription.visible()
        } else {
            view.ivArrowBottom.animate().rotation(0f).duration = 300
            view.tvFaqDescription.gone()
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.paylater_card_faq_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = PayLaterPaymentFaqViewHolder(
                inflater.inflate(PayLaterPaymentFaqViewHolder.LAYOUT_ID, parent, false)
        )
    }
}