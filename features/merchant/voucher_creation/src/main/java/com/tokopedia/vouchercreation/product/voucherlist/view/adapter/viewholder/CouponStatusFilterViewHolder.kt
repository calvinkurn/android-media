package com.tokopedia.vouchercreation.product.voucherlist.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.databinding.ItemMvcCouponStatusFilterBinding

class CouponStatusFilterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private var binding: ItemMvcCouponStatusFilterBinding? by viewBinding()
    val tvStatus by lazy { binding?.tvStatus }

    fun bind(item: Pair<String, String>) {
        tvStatus?.text = item.first
    }
}