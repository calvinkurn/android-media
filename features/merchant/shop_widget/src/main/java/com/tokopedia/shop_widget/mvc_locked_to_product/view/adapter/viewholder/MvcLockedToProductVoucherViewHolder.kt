package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.ItemMvcLockedToProductVoucherLayoutBinding
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductVoucherUiModel
import com.tokopedia.utils.view.binding.viewBinding

open class MvcLockedToProductVoucherViewHolder(
        itemView: View
) : AbstractViewHolder<MvcLockedToProductVoucherUiModel>(itemView) {
    private val viewBinding: ItemMvcLockedToProductVoucherLayoutBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_locked_to_product_voucher_layout
    }

    override fun bind(uiModel: MvcLockedToProductVoucherUiModel) {
        setVoucherData(uiModel)
    }

    private fun setVoucherData(uiModel: MvcLockedToProductVoucherUiModel) {
        viewBinding?.mvcTextContainer?.setData(
            uiModel.title,
            uiModel.minPurchaseWording,
            uiModel.totalQuotaLeftWording,
            uiModel.shopImage
        )
    }

}