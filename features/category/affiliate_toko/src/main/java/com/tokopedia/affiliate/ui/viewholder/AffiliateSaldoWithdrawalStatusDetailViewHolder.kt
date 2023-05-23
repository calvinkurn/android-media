package com.tokopedia.affiliate.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.affiliate.model.response.WithdrawalInfoHistory
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifyprinciples.Typography

class AffiliateSaldoWithdrawalStatusDetailViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    fun bindData(model: WithdrawalInfoHistory, isFirstItem: Boolean, showDescendingLine: Boolean) {
        setDetails(model)
        setColorState(isFirstItem)
        setSeparator(showDescendingLine)
    }

    private fun setDetails(model: WithdrawalInfoHistory) {
        view.apply {
            findViewById<Typography>(R.id.tvStatusTitle).text = model.historyTitle
            findViewById<Typography>(R.id.tvWithdrawalDetail).text = model.description.parseAsHtml()
            findViewById<Typography>(R.id.tvWithdrawalDate).text = model.createdTime
        }
    }

    private fun setColorState(isFirstItem: Boolean) {
        view.apply {
            if (isFirstItem) {
                findViewById<Typography>(R.id.tvStatusTitle).setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                findViewById<ImageView>(R.id.ivCircleDot).setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.affiliate_ic_circle_light_green))
                findViewById<DividerUnify>(R.id.statusDivider).setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            }
            else {
                findViewById<Typography>(R.id.tvStatusTitle).setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
                findViewById<ImageView>(R.id.ivCircleDot).setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.affiliate_ic_circle_light_grey))
                findViewById<DividerUnify>(R.id.statusDivider).setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_32))
            }
        }
    }

    private fun setSeparator(showDescendingLine: Boolean) {
        if (showDescendingLine) view.findViewById<DividerUnify>(R.id.statusDivider).visible()
        else view.findViewById<DividerUnify>(R.id.statusDivider).gone()
    }

    companion object {
        private val LAYOUT_ID = R.layout.affiliate_saldo_withdrawal_status_item_view

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = AffiliateSaldoWithdrawalStatusDetailViewHolder(
            inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}