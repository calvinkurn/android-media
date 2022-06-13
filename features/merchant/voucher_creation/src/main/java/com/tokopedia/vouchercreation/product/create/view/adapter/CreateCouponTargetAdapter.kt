package com.tokopedia.vouchercreation.product.create.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.create.view.adapter.viewholder.CouponTargetViewHolder
import com.tokopedia.vouchercreation.product.create.view.uimodel.CouponTargetEnum
import com.tokopedia.vouchercreation.product.create.view.uimodel.CouponTargetUiModel

class CreateCouponTargetAdapter(
    private val onCouponTargetChanged: (target: CouponTargetEnum) -> Unit
) : RecyclerView.Adapter<CouponTargetViewHolder>() {

    private var items: MutableList<CouponTargetUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponTargetViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.mvc_voucher_target_item, parent, false)
        return CouponTargetViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CouponTargetViewHolder, position: Int) {
        items.getOrNull(position)?.let { couponTargetModel ->
            holder.bindData(couponTargetModel)
            holder.voucherTargetItemRadioButton?.setOnClickListener {
                setSelectedAtPosition(position)
                onCouponTargetChanged.invoke(couponTargetModel.value)
            }
        }
    }

    private fun setSelectedAtPosition(position: Int) {
        items.forEachIndexed { index, couponTargetUiModel ->
            items[index] = couponTargetUiModel.apply {
                selected = index == position
            }
            notifyItemChanged(index)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: List<CouponTargetUiModel>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }
}