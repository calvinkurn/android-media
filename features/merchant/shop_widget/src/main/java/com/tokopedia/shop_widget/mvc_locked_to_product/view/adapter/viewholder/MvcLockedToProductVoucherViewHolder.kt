package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.ItemMvcLockedToProductTotalProductAndSortLayoutBinding
import com.tokopedia.shop_widget.databinding.ItemMvcLockedToProductVoucherLayoutBinding
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductProductGridUiModel
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
            "Cashback 20% hingga Rp100.000",
            "Berakhir 10 hari lagi",
            "Sisa 7 kupon",
            "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2021/7/19/61ceb233-b52a-417c-9039-d5c8001db1c2.jpg"
        )
    }

}