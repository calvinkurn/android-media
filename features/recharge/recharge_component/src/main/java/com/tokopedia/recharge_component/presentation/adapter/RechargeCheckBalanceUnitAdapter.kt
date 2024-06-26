package com.tokopedia.recharge_component.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recharge_component.databinding.ViewRechargeCheckBalanceUnitBinding
import com.tokopedia.recharge_component.model.check_balance.RechargeCheckBalanceUnitModel
import com.tokopedia.recharge_component.presentation.adapter.viewholder.RechargeCheckBalanceUnitViewHolder

class RechargeCheckBalanceUnitAdapter : RecyclerView.Adapter<RechargeCheckBalanceUnitViewHolder>() {

    private var balanceInfo = listOf<RechargeCheckBalanceUnitModel>()
    private var unitListener: RechargeCheckBalanceUnitViewHolder.RechargeCheckBalanceUnitListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RechargeCheckBalanceUnitViewHolder {
        val binding = ViewRechargeCheckBalanceUnitBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RechargeCheckBalanceUnitViewHolder(binding, unitListener)
    }

    override fun onBindViewHolder(holder: RechargeCheckBalanceUnitViewHolder, position: Int) {
        holder.bind(balanceInfo[position])
    }

    override fun getItemCount(): Int = balanceInfo.size

    @SuppressLint("NotifyDataSetChanged")
    fun setBalanceInfo(balanceInfo: List<RechargeCheckBalanceUnitModel>) {
        this.balanceInfo = balanceInfo
        notifyDataSetChanged()
    }

    fun setCheckBalanceUnitListener(listener: RechargeCheckBalanceUnitViewHolder.RechargeCheckBalanceUnitListener) {
        unitListener = listener
    }
}
