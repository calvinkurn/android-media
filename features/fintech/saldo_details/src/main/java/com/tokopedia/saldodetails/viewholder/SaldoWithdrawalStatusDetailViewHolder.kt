package com.tokopedia.saldodetails.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.response.model.saldo_detail_info.WithdrawalInfoHistory
import kotlinx.android.synthetic.main.saldo_withdrawal_status_item_view.view.*

class SaldoWithdrawalStatusDetailViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    fun bindData(model: WithdrawalInfoHistory, position: Int) {
        setSeparator(position)
        setDetails(model)
    }

    private fun setDetails(model: WithdrawalInfoHistory) {
        view.apply {
            tvStatusTitle.text = model.historyTitle
            tvWithdrawalDetail.text = model.description
            tvWithdrawalDate.text = model.createdTime
        }
    }

    private fun setSeparator(position: Int) {
        if (position == 0) {
            view.ivCircleDot.setImageDrawable(AppCompatResources.getDrawable(view.context, R.drawable.ic_circle_light_green))
            view.statusDivider.setBackgroundColor(MethodChecker.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        }
        else {
            view.ivCircleDot.setImageDrawable(AppCompatResources.getDrawable(view.context, R.drawable.ic_circle_light_grey))
            view.statusDivider.setBackgroundColor(MethodChecker.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
        }

        val showDivider = position != 4
        if (showDivider) view.statusDivider.visible()
        view.statusDivider.gone()

    }

    companion object {
        private val LAYOUT_ID = R.layout.saldo_withdrawal_status_item_view

        fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup) = SaldoWithdrawalStatusDetailViewHolder(
            inflater.inflate(LAYOUT_ID, parent, false)
        )    }
}