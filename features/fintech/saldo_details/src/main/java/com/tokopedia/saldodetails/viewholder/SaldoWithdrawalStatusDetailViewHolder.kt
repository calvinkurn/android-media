package com.tokopedia.saldodetails.viewholder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.response.model.saldo_detail_info.WithdrawalInfoHistory
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
            tvWithdrawalDetail.text = model.description
            tvWithdrawalDate.text = model.createdTime
        }
    }

    private fun setColorState(isFirstItem: Boolean) {
        view.apply {
            if (isFirstItem) {
                tvStatusTitle.setTextColor(Color.parseColor("#03AC0E"))
                ivCircleDot.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_circle_light_green))
                statusDivider.setBackgroundColor(Color.parseColor("#03AC0E"))
            }
            else {
                tvStatusTitle.setTextColor(Color.parseColor("#F531353B"))
                ivCircleDot.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_circle_light_grey))
                statusDivider.setBackgroundColor(Color.parseColor("#5231353B"))
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