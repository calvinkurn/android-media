package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.home.view.model.ShopHomeProductChangeGridSectionUiModel
import kotlinx.android.synthetic.main.item_shop_product_change_grid_section_view.view.*

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopHomeProductChangeGridSectionViewHolder(
        itemView: View,
        private val listener: ShopProductChangeGridSectionListener
) : AbstractViewHolder<ShopHomeProductChangeGridSectionUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_change_grid_section_view
    }

    override fun bind(model: ShopHomeProductChangeGridSectionUiModel) {
        val productListDataFormatted = model.totalProduct.thousandFormatted()
        itemView.label_total_product?.apply {
            text = String.format(
                    context.getString(R.string.shop_change_grid_section_total_product),
                    productListDataFormatted
            )
        }
        setGridIcon(model.gridType)
        itemView.iv_grid_icon?.setOnClickListener {
            listener.onChangeProductGridClicked(switchGridLayout(model.gridType))
        }
    }

    private fun switchGridLayout(gridType: ShopProductViewGridType): ShopProductViewGridType {
        return when (gridType) {
            ShopProductViewGridType.LIST -> ShopProductViewGridType.BIG_GRID
            ShopProductViewGridType.SMALL_GRID -> ShopProductViewGridType.LIST
            ShopProductViewGridType.BIG_GRID -> ShopProductViewGridType.SMALL_GRID
        }
    }

    private fun setGridIcon(gridType: ShopProductViewGridType) {
        val gridIcon = when (gridType) {
            ShopProductViewGridType.LIST -> R.drawable.ic_shop_page_product_grid_list
            ShopProductViewGridType.SMALL_GRID -> R.drawable.ic_shop_page_product_grid_small
            ShopProductViewGridType.BIG_GRID -> R.drawable.ic_shop_page_product_grid_big
        }
        itemView.iv_grid_icon?.setImageDrawable(MethodChecker.getDrawable(
                itemView.context,
                gridIcon
        ))
    }

}
