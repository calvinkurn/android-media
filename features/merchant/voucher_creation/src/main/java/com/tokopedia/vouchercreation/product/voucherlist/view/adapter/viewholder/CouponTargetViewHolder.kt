package com.tokopedia.vouchercreation.product.voucherlist.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.databinding.ItemMvcCouponListBinding
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel

class CouponListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var binding: ItemMvcCouponListBinding? by viewBinding()
    val moreButton by lazy { binding?.iuMoreButton }

    fun bindData(coupon: VoucherUiModel) {
        binding?.tvMvcVoucherName?.text = coupon.name
    }
}
