package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.paylater.R
import com.tokopedia.paylater.presentation.adapter.PayLaterOfferDescriptionAdapter
import com.tokopedia.unifycomponents.DividerUnify
import kotlinx.android.synthetic.main.fragment_paylater_cards_info.*
import kotlinx.android.synthetic.main.paylater_payment_method_item.view.*
import kotlinx.android.synthetic.main.paylater_register_card_bottomsheet_item.view.*

class PayLaterPaymentRegisterViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    fun bindData(position: Int) {
        if(position == 4)  view.dividerVertical.gone()
    }

    companion object {
        private val LAYOUT_ID = R.layout.paylater_register_card_bottomsheet_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = PayLaterPaymentRegisterViewHolder(
                inflater.inflate(PayLaterPaymentRegisterViewHolder.LAYOUT_ID, parent, false)
        )
    }
}