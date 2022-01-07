package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.ItemMvcLockedToProductTotalProductAndSortLayoutBinding
import com.tokopedia.shop_widget.databinding.ItemMvcLockedToProductVoucherLayoutBinding
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductProductGridUiModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductTotalProductAndSortUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

open class MvcLockedToProductTotalProductAndSortViewHolder(
        itemView: View
) : AbstractViewHolder<MvcLockedToProductTotalProductAndSortUiModel>(itemView) {
    private val viewBinding: ItemMvcLockedToProductTotalProductAndSortLayoutBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_locked_to_product_total_product_and_sort_layout
    }

    override fun bind(uiModel: MvcLockedToProductTotalProductAndSortUiModel) {
        setTotalProduct(uiModel)
        setSortChipData(uiModel)
//        this.shopHomeProductViewModel = shopHomeProductViewModel
//        productCard?.setProductModel(ShopPageHomeMapper.mapToProductCardModel(
//                isHasAddToCartButton = false,
//                hasThreeDots = isShowTripleDot,
//                shopHomeProductViewModel = shopHomeProductViewModel,
//                isWideContent = false
//        ))
    }

    private fun setSortChipData(uiModel: MvcLockedToProductTotalProductAndSortUiModel) {
        viewBinding?.chipSort?.apply {
            chipText = "Urutkan"
            setChevronClickListener {

            }
        }
    }

    private fun setTotalProduct(uiModel: MvcLockedToProductTotalProductAndSortUiModel) {
        viewBinding?.textTotalProduct?.text = "11 Barang"
    }

}