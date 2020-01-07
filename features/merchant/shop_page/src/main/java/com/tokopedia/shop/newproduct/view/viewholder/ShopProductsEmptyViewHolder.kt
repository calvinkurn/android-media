package com.tokopedia.shop.newproduct.view.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant.URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE
import com.tokopedia.shop.newproduct.view.datamodel.EmptyOwnShopModel
import kotlinx.android.synthetic.main.shop_products_empty_state.view.*

class ShopProductsEmptyViewHolder (val view: View): AbstractViewHolder<EmptyOwnShopModel>(view) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.new_shop_products_empty_state
    }

    init {
        initLayout(view)
    }

    private fun initLayout(view: View) {
        imageViewEmptyImage = view.findViewById(R.id.image_view_empty_image)
    }

    lateinit var imageViewEmptyImage: ImageView

    override fun bind(element: EmptyOwnShopModel) {
        ImageHandler.loadImage(
                view.context,
                imageViewEmptyImage,
                URL_IMAGE_BUYER_EMPTY_STATE_TOKOPEDIA_IMAGE,
                R.drawable.ic_loading_image
        )
    }

}