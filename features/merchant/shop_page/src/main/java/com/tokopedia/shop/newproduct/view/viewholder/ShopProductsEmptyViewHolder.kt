package com.tokopedia.shop.newproduct.view.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant.URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE
import com.tokopedia.shop.newproduct.view.datamodel.EmptyOwnShopModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ShopProductsEmptyViewHolder(
        val view: View,
        private val shopProductsEmptyViewHolderListener: ShopProductsEmptyViewHolderListener?
) : AbstractViewHolder<EmptyOwnShopModel>(view) {

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


    override fun bind(element: EmptyOwnShopModel) {
        ImageHandler.loadImage(
                view.context,
                imageViewEmptyImage,
                URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE,
                R.drawable.ic_loading_image
        )
        if (element.isMyShop) {
            buttonChooseProduct.apply {
                setOnClickListener { shopProductsEmptyViewHolderListener?.chooseProductClicked() }
            }.show()
            textTitle.text = view.context.getString(R.string.text_shop_no_product_seller)
            textDescription.text = view.context.getString(R.string.text_shop_no_product_description_buyer)
        } else {
            buttonChooseProduct.hide()
            textTitle.text = view.context.getString(R.string.text_shop_no_product)
            textDescription.text = view.context.getString(R.string.text_shop_no_product_follow)
        }
    }

}