package com.tokopedia.vouchercreation.product.voucherlist.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.voucherlist.view.adapter.viewholder.CouponListViewHolder
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import java.lang.Exception

class CouponListAdapter(
    private val onCouponOptionClicked: (coupon: VoucherUiModel) -> Unit,
    private val onCouponIconCopyClicked: (couponCode: String) -> Unit
) : RecyclerView.Adapter<CouponListViewHolder>() {

    private var items: MutableList<VoucherUiModel> = mutableListOf()
    private var isLoading = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponListViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_mvc_coupon_list, parent, false)
        return CouponListViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CouponListViewHolder, position: Int) {
        items.getOrNull(position)?.let { coupon ->
            val isItemLoading = isLoading && (position == items.lastIndex)
            holder.bindData(coupon, isItemLoading)
            holder.moreButton?.setOnClickListener {
                onCouponOptionClicked.invoke(coupon)
            }
            holder.iconCopyCode?.setOnClickListener {
                onCouponIconCopyClicked.invoke(coupon.code)
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

    fun showLoading() {
        if (itemCount.isMoreThanZero()) {
            isLoading = true
            notifyItemChanged(items.lastIndex)
        }
    }

    fun hideLoading() {
        isLoading = false
    }
}