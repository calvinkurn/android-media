package com.tokopedia.saldodetails.transactionDetailPages.withdrawal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.saldodetails.R
import kotlinx.android.synthetic.main.saldo_withdrawal_status_item_view.view.*

class SaldoWithdrawalStatusDetailViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    fun bindData(model: WithdrawalInfoHistory, isFirstItem: Boolean, showDescendingLine: Boolean) {
        setDetails(model)
        setColorState(isFirstItem)
        setSeparator(showDescendingLine)
    }

    private fun setDetails(model: WithdrawalInfoHistory) {
        view.apply {
            tvStatusTitle.text = model.historyTitle
            tvWithdrawalDetail.text = model.description.parseAsHtml()
            tvWithdrawalDate.text = model.createdTime
        }
    }

    private fun setColorState(isFirstItem: Boolean) {
        view.apply {
            if (isFirstItem) {
                tvStatusTitle.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                ivCircleDot.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_circle_light_green))
                statusDivider.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            }
            else {
                tvStatusTitle.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
                ivCircleDot.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_circle_light_grey))
                statusDivider.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_32))
            }
        }
    }

    private fun setSeparator(showDescendingLine: Boolean) {
        if (showDescendingLine) view.statusDivider.visible()
        else view.statusDivider.gone()
    }

    companion object {
        private val LAYOUT_ID = R.layout.saldo_withdrawal_status_item_view

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = SaldoWithdrawalStatusDetailViewHolder(
            inflater.inflate(LAYOUT_ID, parent, false)
        )
    }
}