package com.tokopedia.pdpsimulation.creditcard.presentation.tnc.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.credit_card_pdp_meta_info_table_content_item.view.*

class CreditCardServiceFeeTableContentViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bindData(data: ArrayList<String>, position: Int) {
        view.apply {
            if (position % 2 == 0)
                clTransaction.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N50))
            else clTransaction.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            if (position == 0) {
                setTypography(tvColumnData1, Typography.HEADING_6)
                setTypography(tvColumnSubHeader1, Typography.HEADING_6)
                setTypography(tvColumnSubHeader2, Typography.HEADING_6)
            } else {
                setTypography(tvColumnData1, Typography.BODY_3)
                setTypography(tvColumnSubHeader1, Typography.BODY_3)
                setTypography(tvColumnSubHeader2, Typography.BODY_3)

            }
            tvColumnData1.text = data.getOrNull(0) ?: "-"
            tvColumnSubHeader1.text = data.getOrNull(1) ?: "-"
            tvColumnSubHeader2.text = data.getOrNull(2) ?: "-"
        }
    }

    private fun setTypography(tv: Typography, type: Int) {
        if (type == Typography.HEADING_6) {
            tv.setType(Typography.HEADING_6)
            tv.setWeight(Typography.BOLD)
        } else {
            tv.setType(Typography.BODY_3)
            tv.setWeight(Typography.REGULAR)
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_pdp_meta_info_table_content_item

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = CreditCardServiceFeeTableContentViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}