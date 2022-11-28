package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.databinding.ItemShopHomeProductListSellerEmptyStateBinding
import com.tokopedia.shop.home.view.model.ShopHomeProductListEmptyUiModel
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeProductListSellerEmptyViewHolder(
    val itemView: View,
    val listener: ShopHomeProductListSellerEmptyListener
) : AbstractViewHolder<ShopHomeProductListEmptyUiModel>(itemView) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.item_shop_home_product_list_seller_empty_state
    }

    private val viewBinding: ItemShopHomeProductListSellerEmptyStateBinding? by viewBinding()
    private var cardAddProduct: CardUnify2? = viewBinding?.cardAddProduct
    private var cardDescription: Typography? = viewBinding?.cardDescriptionLabel
    private var cardDescriptionBackground: ImageUnify? = viewBinding?.cardDescriptionBackground

    override fun bind(element: ShopHomeProductListEmptyUiModel) {
        cardDescriptionBackground?.loadImage(ShopPageConstant.URL_IMAGE_SELLER_PRODUCT_ALL_ETALASE_EMPTY_STATE_BACKGROUND)
        cardDescription?.text = MethodChecker.fromHtml(
            itemView.resources.getString(R.string.shop_product_seller_empty_state_label)
        )
        cardAddProduct?.setOnClickListener {
            listener.chooseProduct()
        }
    }
}

interface ShopHomeProductListSellerEmptyListener {
    fun chooseProduct()
}
