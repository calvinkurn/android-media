package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.paylater.R
import kotlinx.android.synthetic.main.paylater_register_card_bottomsheet_item.view.*

class PayLaterPaymentRegisterViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    fun bindData(partnerStep: String, showDivider: Boolean, position: Int) {
        if(showDivider)  view.dividerVertical.gone()
        view.tvDescription.text =  MethodChecker.fromHtml(partnerStep)
        view.tvNumber.text = position.toString()
    }

    companion object {
        private val LAYOUT_ID = R.layout.paylater_register_card_bottomsheet_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = PayLaterPaymentRegisterViewHolder(
                inflater.inflate(PayLaterPaymentRegisterViewHolder.LAYOUT_ID, parent, false)
        )
    }
}