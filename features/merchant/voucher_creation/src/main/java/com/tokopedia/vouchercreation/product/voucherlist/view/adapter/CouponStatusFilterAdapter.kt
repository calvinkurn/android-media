package com.tokopedia.vouchercreation.product.voucherlist.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.voucherlist.view.adapter.viewholder.CouponStatusFilterViewHolder

class CouponStatusFilterAdapter(
    private val onCouponStatusClicked: (couponName: String, couponStatus: String) -> Unit
): RecyclerView.Adapter<CouponStatusFilterViewHolder>() {

    private var items: MutableList<Pair<String, String>> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CouponStatusFilterViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mvc_coupon_status_filter, parent, false)

        return CouponStatusFilterViewHolder(rootView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CouponStatusFilterViewHolder, position: Int) {
        items.getOrNull(position)?.let { item ->
            holder.bind(item)
            holder.tvStatus?.setOnClickListener {
                onCouponStatusClicked(item.first, item.second)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newItems: MutableList<Pair<String, String>>) {
        items = newItems
        notifyDataSetChanged()
    }

}