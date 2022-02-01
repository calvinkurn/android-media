package com.tokopedia.vouchercreation.product.voucherlist.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.voucherlist.view.adapter.viewholder.CouponListViewHolder
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel

class CouponListAdapter(
    private val onCouponOptionClicked: (coupon: VoucherUiModel) -> Unit
) : RecyclerView.Adapter<CouponListViewHolder>() {

    private var items: MutableList<VoucherUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponListViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_mvc_coupon_list, parent, false)
        return CouponListViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CouponListViewHolder, position: Int) {
        items.getOrNull(position)?.let { coupon ->
            holder.bindData(coupon)
            holder.moreButton?.setOnClickListener {
                onCouponOptionClicked.invoke(coupon)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(items: List<VoucherUiModel>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clearData() {
        this.items.clear()
    }
}