package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductViewModel
import kotlinx.android.synthetic.main.add_to_cart_done_added_product_layout.view.*

class AddToCartDoneAddedProductViewHolder(
        itemView: View,
        private val addToCartDoneAddedProductListener: AddToCartDoneAddedProductListener
) : AbstractViewHolder<AddToCartDoneAddedProductViewModel>(itemView) {
    override fun bind(element: AddToCartDoneAddedProductViewModel) {
        with(itemView) {
            text_view_product_name.text = element.productName
            ImageHandler.loadImage(
                    itemView.context,
                    image_view_added_product,
                    element.productImageUr,
                    R.drawable.loading_page
            )
            button_go_to_cart.setOnClickListener {
                addToCartDoneAddedProductListener.onButtonGoToCartClicked()
            }
        }
    }
//
//    private fun gotoCart() {
//        if (productInfoViewModel.isUserSessionActive()) {
//            startActivity(RouteManager.getIntent(it, ApplinkConst.CART))
//        } else {
//            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
//                    REQUEST_CODE_LOGIN)
//        }
//        productDetailTracking.eventCartMenuClicked(generateVariantString())
//    }

    companion object {
        val LAYOUT_RES = R.layout.add_to_cart_done_added_product_layout
    }

    interface AddToCartDoneAddedProductListener {
        fun onButtonGoToCartClicked()
    }
}