package com.tokopedia.shop.product.view.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant.URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE
import com.tokopedia.shop.product.view.datamodel.ShopEmptyProductUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ShopProductsEmptyViewHolder(
        val view: View,
        private val shopProductsEmptyViewHolderListener: ShopProductsEmptyViewHolderListener?
) : AbstractViewHolder<ShopEmptyProductUiModel>(view) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.new_shop_products_empty_state
    }

    init {
        initLayout(view)
    }

    interface ShopProductsEmptyViewHolderListener {
        fun chooseProductClicked()
    }

    lateinit var imageViewEmptyImage: ImageView
    lateinit var textTitle: Typography
    lateinit var textDescription: Typography
    lateinit var buttonChooseProduct: UnifyButton


    private fun initLayout(view: View) {
        imageViewEmptyImage = view.findViewById(R.id.image_view_empty_image)
        textTitle = view.findViewById(R.id.text_title)
        textDescription = view.findViewById(R.id.text_description)
        buttonChooseProduct = view.findViewById(R.id.button_choose_product)
    }


    override fun bind(element: ShopEmptyProductUiModel) {
        imageViewEmptyImage.loadImage(URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE) {
            setPlaceHolder(R.drawable.ic_shop_page_loading_image)
        }
        textTitle.text = element.title
        textDescription.text = element.description
    }

}