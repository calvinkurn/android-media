package com.tokopedia.pdpsimulation.creditcard.presentation.tnc.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.credit_card_pdp_meta_info_trx_table_item.view.*

class CreditCardMinTransactionTableViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(data: ArrayList<String>, position: Int) {
        view.apply {
            if (position == 0) {
                setTypography(tvColumnData1, Typography.HEADING_5)
                setTypography(tvColumnData2, Typography.HEADING_5)
            } else {
                setTypography(tvColumnData1, Typography.BODY_3)
                setTypography(tvColumnData2, Typography.BODY_3)
            }
            if (position % 2 == 0)
                clTransaction.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N50))
            else clTransaction.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))

            tvColumnData1.text = data.getOrNull(0) ?: "-"
            tvColumnData2.text = data.getOrNull(1) ?: "-"
        }
    }

    private fun setTypography(tv: Typography, type: Int) {
        if (type == Typography.HEADING_5) {
            tv.setType(Typography.HEADING_5)
            tv.setWeight(Typography.BOLD)
        } else {
            tv.setType(Typography.BODY_3)
            tv.setWeight(Typography.REGULAR)
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_pdp_meta_info_trx_table_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardMinTransactionTableViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}