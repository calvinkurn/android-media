package com.tokopedia.shop.product.view.viewholder

import android.graphics.PorterDuff
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopProductChangeGridSectionViewBinding
import com.tokopedia.shop.product.view.datamodel.ShopProductChangeGridSectionUiModel
import com.tokopedia.shop.product.view.fragment.ShopProductTabInterface
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by normansyahputa on 2/22/18.
 */

class ShopProductChangeGridSectionViewHolder(
    itemView: View,
    private val listener: ShopProductChangeGridSectionListener,
    private val shopProductTabInterface: ShopProductTabInterface?
) : AbstractViewHolder<ShopProductChangeGridSectionUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_change_grid_section_view
    }

    private val viewBinding: ItemShopProductChangeGridSectionViewBinding? by viewBinding()
    private val drawableProductGridList: Int
        get() = R.drawable.ic_shop_page_product_grid_list

    private val drawableProductGridSmall: Int
        get() = R.drawable.ic_shop_page_product_grid_small

    private val drawableProductGridBig: Int
        get() = R.drawable.ic_shop_page_product_grid_big
    private val labelTotalProduct: Typography? = viewBinding?.labelTotalProduct
    private val ivGridIcon: IconUnify? = viewBinding?.ivGridIcon
    private val labelTampilan: Typography? = viewBinding?.labelTampilan

    override fun bind(model: ShopProductChangeGridSectionUiModel) {
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
        if (shopProductTabInterface?.isOverrideTheme() == true) {
            configReimaginedColor()
        } else {
            configDefaultColor()
        }
    }

    private fun configReimaginedColor() {
        val totalProductColor = shopProductTabInterface?.getShopPageColorSchema()?.getColorIntValue(
            ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS
        ).orZero()
        val labelPreviewColor = shopProductTabInterface?.getShopPageColorSchema()?.getColorIntValue(
            ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
        ).orZero()
        val iconColor = shopProductTabInterface?.getShopPageColorSchema()?.getColorIntValue(
            ShopPageColorSchema.ColorSchemaName.ICON_CTA_LINK_COLOR
        ).orZero()
        labelTotalProduct?.setTextColor(totalProductColor)
        labelTampilan?.setTextColor(labelPreviewColor)
        ivGridIcon?.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP)
    }

    private fun configDefaultColor() {
        val totalProductColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_32)
        val labelPreviewColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96)
        val iconColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
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
