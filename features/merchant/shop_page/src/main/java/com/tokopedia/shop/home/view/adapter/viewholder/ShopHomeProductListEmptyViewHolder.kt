package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant.URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.NewShopProductsEmptyStateBinding
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.ShopHomeProductListEmptyUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeProductListEmptyViewHolder(
    val itemView: View,
    private val shopHomeListener: ShopHomeListener
) : AbstractViewHolder<ShopHomeProductListEmptyUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.new_shop_products_empty_state
    }

    private val viewBinding: NewShopProductsEmptyStateBinding? by viewBinding()
    private var imageViewEmptyImage: ImageView? = viewBinding?.imageViewEmptyImage
    private var textTitle: Typography? = viewBinding?.textTitle
    private var textDescription: Typography? = viewBinding?.textDescription

    override fun bind(element: ShopHomeProductListEmptyUiModel) {
        imageViewEmptyImage?.loadImage(URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE) {
            setPlaceHolder(R.drawable.ic_shop_page_loading_image)
        }
        configColorTheme()
    }

    private fun configColorTheme() {
        if (shopHomeListener.isShopHomeTabHasFestivity()) {
            setDefaultColorConfig()
        } else {
            if (shopHomeListener.isOverrideTheme()) {
                setReimaginedColorConfig(shopHomeListener.getShopPageColorSchema())
            } else {
                setDefaultColorConfig()
            }
        }
    }

    private fun setReimaginedColorConfig(colorSchema: ShopPageColorSchema) {
        val titleColor = colorSchema.getColorIntValue(
            ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS
        )
        val descriptionColor = colorSchema.getColorIntValue(
            ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS
        )
        setTextColor(titleColor, descriptionColor)
    }

    private fun setDefaultColorConfig() {
        val titleColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
        )
        val descriptionColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
        )
        setTextColor(titleColor, descriptionColor)
    }

    private fun setTextColor(titleColor: Int, descriptionColor: Int) {
        textTitle?.setTextColor(titleColor)
        textDescription?.setTextColor(descriptionColor)
    }
}
