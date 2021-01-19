package com.tokopedia.paylater.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.CreditCardPdpInfoContent
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.credit_card_pdp_meta_info_bullet_item.view.*
import kotlinx.android.synthetic.main.credit_card_pdp_meta_info_item.view.*

class CreditCardPdpInfoBulletViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(creditCardPdpInfoContent: String?) {
        view.apply {
            tvBulletPoint.text = creditCardPdpInfoContent
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_pdp_meta_info_bullet_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardPdpInfoBulletViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}