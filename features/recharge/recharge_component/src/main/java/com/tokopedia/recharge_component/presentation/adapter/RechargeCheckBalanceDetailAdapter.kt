package com.tokopedia.recharge_component.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recharge_component.databinding.ViewRechargeCheckBalanceDetailBinding
import com.tokopedia.recharge_component.model.check_balance.RechargeCheckBalanceDetailModel
import com.tokopedia.recharge_component.presentation.adapter.viewholder.RechargeCheckBalanceDetailViewHolder

class RechargeCheckBalanceDetailAdapter : RecyclerView.Adapter<RechargeCheckBalanceDetailViewHolder>() {

    private var checkBalanceDetails: List<RechargeCheckBalanceDetailModel> = emptyList()
    private var checkBalanceDetailViewHolderListener: RechargeCheckBalanceDetailViewHolder.RechargeCheckBalanceDetailViewHolderListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RechargeCheckBalanceDetailViewHolder {
        val binding = ViewRechargeCheckBalanceDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RechargeCheckBalanceDetailViewHolder(binding, checkBalanceDetailViewHolderListener)
    }

    override fun onBindViewHolder(holder: RechargeCheckBalanceDetailViewHolder, position: Int) {
        holder.bind(checkBalanceDetails[position])
    }

    override fun getItemCount(): Int = checkBalanceDetails.size

    @SuppressLint("NotifyDataSetChanged")
    fun setCheckBalanceDetails(details: List<RechargeCheckBalanceDetailModel>) {
        checkBalanceDetails = details
        notifyDataSetChanged()
    }

    fun setCheckBalanceDetailViewHolderListener(listener: RechargeCheckBalanceDetailViewHolder.RechargeCheckBalanceDetailViewHolderListener?) {
        checkBalanceDetailViewHolderListener = listener
    }
}
