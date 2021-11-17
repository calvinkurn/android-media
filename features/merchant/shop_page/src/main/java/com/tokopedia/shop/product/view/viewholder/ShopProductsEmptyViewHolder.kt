package com.tokopedia.shop.product.view.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant.URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE
import com.tokopedia.shop.databinding.NewShopProductsEmptyStateBinding
import com.tokopedia.shop.product.view.datamodel.ShopEmptyProductUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopProductsEmptyViewHolder(
        val view: View,
        private val shopProductsEmptyViewHolderListener: ShopProductsEmptyViewHolderListener?
) : AbstractViewHolder<ShopEmptyProductUiModel>(view) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.new_shop_products_empty_state
    }

    init {
        initLayout()
    }

    interface ShopProductsEmptyViewHolderListener {
        fun chooseProductClicked()
    }

    private val viewBinding : NewShopProductsEmptyStateBinding? by viewBinding()
    private var imageViewEmptyImage: ImageView? = null
    private var textTitle: Typography? = null
    private var textDescription: Typography? = null
    private var buttonChooseProduct: UnifyButton? = null


    private fun initLayout() {
        imageViewEmptyImage = viewBinding?.imageViewEmptyImage
        textTitle = viewBinding?.textTitle
        textDescription = viewBinding?.textDescription
        buttonChooseProduct = viewBinding?.buttonChooseProduct
    }


    override fun bind(element: ShopEmptyProductUiModel) {
        imageViewEmptyImage?.loadImage(URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE) {
            setPlaceHolder(R.drawable.ic_shop_page_loading_image)
        }
        textTitle?.text = element.title
        textDescription?.text = element.description
    }

}