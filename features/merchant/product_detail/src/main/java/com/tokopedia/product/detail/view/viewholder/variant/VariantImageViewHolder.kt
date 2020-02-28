package com.tokopedia.product.detail.view.viewholder.variant

import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.variant.VariantOptionWithAttribute
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_variant_image_view_holder.view.*

/**
 * Created by Yehezkiel on 2020-02-27
 */
class VariantImageViewHolder(val view: View,
                             val listener: DynamicProductDetailListener) : BaseVariantViewHolder<VariantOptionWithAttribute>(view) {

    companion object {
        val LAYOUT = R.layout.item_variant_image_view_holder
    }

    override fun bind(element: VariantOptionWithAttribute) = with(view) {
        ImageHandler.loadImage(view.context, variantImg, element.image, R.drawable.ic_loading_placeholder)
        when (element.currentState) {
            ProductDetailConstant.STATE_EMPTY -> {
                overlayVariantImgContainer.show()
                variantImgContainer.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_img_unselected)
            }
            ProductDetailConstant.STATE_SELECTED -> {
                overlayVariantImgContainer.hide()
                variantImgContainer.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_img_selected)
            }
            ProductDetailConstant.STATE_UNSELECTED -> {
                overlayVariantImgContainer.hide()
                variantImgContainer.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_img_unselected)
            }
        }

        view.setOnClickListener {
            //TO DO
        }
    }
}