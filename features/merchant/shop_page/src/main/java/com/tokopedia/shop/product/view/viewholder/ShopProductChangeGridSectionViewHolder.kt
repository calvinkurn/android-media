package com.tokopedia.shop.product.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.product.view.datamodel.ShopProductChangeGridSectionUiModel
import kotlinx.android.synthetic.main.item_shop_product_change_grid_section_view.view.*

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopProductChangeGridSectionViewHolder(
        itemView: View,
        private val listener: ShopProductChangeGridSectionListener
) : AbstractViewHolder<ShopProductChangeGridSectionUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_change_grid_section_view
    }

    private val drawableProductGridList : Int
        get() = R.drawable.ic_shop_page_product_grid_list.takeIf {
            ShopUtil.isUsingNewNavigation()
        } ?: R.drawable.ic_shop_page_product_grid_list_old

    private val drawableProductGridSmall : Int
        get() = R.drawable.ic_shop_page_product_grid_small.takeIf {
            ShopUtil.isUsingNewNavigation()
        } ?: R.drawable.ic_shop_page_product_grid_small_old

    private val drawableProductGridBig : Int
        get() = R.drawable.ic_shop_page_product_grid_big.takeIf {
            ShopUtil.isUsingNewNavigation()
        } ?: R.drawable.ic_shop_page_product_grid_big_old

    override fun bind(model: ShopProductChangeGridSectionUiModel) {
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
        if(ShopUtil.isUsingNewNavigation()){
            itemView.label_tampilan?.hide()
        }else{
            itemView.label_tampilan?.show()
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
            ShopProductViewGridType.LIST -> drawableProductGridList
            ShopProductViewGridType.SMALL_GRID -> drawableProductGridSmall
            ShopProductViewGridType.BIG_GRID -> drawableProductGridBig
        }
        itemView.iv_grid_icon?.setImageDrawable(MethodChecker.getDrawable(
                itemView.context,
                gridIcon
        ))
    }

}
