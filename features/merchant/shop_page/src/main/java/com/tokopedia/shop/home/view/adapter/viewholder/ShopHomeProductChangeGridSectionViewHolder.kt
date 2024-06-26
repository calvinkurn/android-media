package com.tokopedia.shop.home.view.adapter.viewholder

import android.graphics.PorterDuff
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeProductChangeGridSectionViewBinding
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.ShopHomeProductChangeGridSectionUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopHomeProductChangeGridSectionViewHolder(
    itemView: View,
    private val listener: ShopProductChangeGridSectionListener,
    private val shopHomeListener: ShopHomeListener
) : AbstractViewHolder<ShopHomeProductChangeGridSectionUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_change_grid_section_view
    }
    private val viewBinding: ItemShopHomeProductChangeGridSectionViewBinding? by viewBinding()
    private val labelTotalProduct: Typography? = viewBinding?.labelTotalProduct
    private val ivGridIcon: IconUnify? = viewBinding?.ivGridIcon
    private val labelTampilan: Typography? = viewBinding?.labelTampilan

    private val drawableProductGridList: Int
        get() = R.drawable.ic_shop_page_product_grid_list

    private val drawableProductGridSmall: Int
        get() = R.drawable.ic_shop_page_product_grid_small

    private val drawableProductGridBig: Int
        get() = R.drawable.ic_shop_page_product_grid_big

    override fun bind(model: ShopHomeProductChangeGridSectionUiModel) {
        val productListDataFormatted = model.totalProduct.thousandFormatted()
        labelTotalProduct?.apply {
            text = String.format(
                context.getString(R.string.shop_change_grid_section_total_product),
                productListDataFormatted
            )
        }
        setGridIcon(model.gridType)
        ivGridIcon?.setOnClickListener {
            listener.onChangeProductGridClicked(model.gridType, switchGridLayout(model.gridType))
        }
        labelTampilan?.show()
        configColorTheme()
    }

    private fun configColorTheme() {
        if (shopHomeListener.isOverrideTheme()) {
            configReimaginedColor()
        } else {
            configDefaultColor()
        }
    }

    private fun configReimaginedColor() {
        val totalProductColor = shopHomeListener.getShopPageColorSchema().getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS)
        val labelPreviewColor = shopHomeListener.getShopPageColorSchema().getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        val iconColor = shopHomeListener.getShopPageColorSchema().getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        labelTotalProduct?.setTextColor(totalProductColor)
        labelTampilan?.setTextColor(labelPreviewColor)
        ivGridIcon?.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP)
    }

    private fun configDefaultColor() {
        val totalProductColor = MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_NN950_32)
        val labelPreviewColor = MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_NN950_96)
        val iconColor = MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_NN950)
        labelTotalProduct?.setTextColor(totalProductColor)
        labelTampilan?.setTextColor(labelPreviewColor)
        ivGridIcon?.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP)
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
        ivGridIcon?.setImageDrawable(
            MethodChecker.getDrawable(
                itemView.context,
                gridIcon
            )
        )
    }
}
