package com.tokopedia.product.detail.common.view

import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.common.R
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.data.views.ProductRoundedImageView
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 06/05/21
 */
class ItemVariantImageViewHolder(val view: View,
                                 val listener: AtcVariantListener) : BaseAtcVariantItemViewHolder<VariantOptionWithAttribute>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_image_viewholder
    }

    private val variantImg = view.findViewById<ProductRoundedImageView>(R.id.img_rounded_variant)
    private val imgContainerVariant = view.findViewById<FrameLayout>(R.id.img_container_variant)
    private val promoVariantImage = view.findViewById<NotificationUnify>(R.id.promo_variant_image)
    private val overlayVariantImgContainer = view.findViewById<Typography>(R.id.img_variant_overlay)

    override fun bind(element: VariantOptionWithAttribute, payload: Int) {
        setState(element)
    }

    override fun bind(element: VariantOptionWithAttribute) = with(view) {
        ImageHandler.LoadImage(variantImg, element.image100)
        setState(element)
    }

    private fun setState(element: VariantOptionWithAttribute) = with(view) {
        if (element.flashSale) {
            promoVariantImage.show()
        } else {
            promoVariantImage.hide()
        }

        when (element.currentState) {
            VariantConstant.STATE_EMPTY -> {
                overlayVariantImgContainer.show()
                imgContainerVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_atc_variant_img_unselected)
                view.isEnabled = false
            }
            VariantConstant.STATE_SELECTED -> {
                overlayVariantImgContainer.hide()
                imgContainerVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_atc_variant_img_selected)
                view.isEnabled = false
            }
            VariantConstant.STATE_UNSELECTED -> {
                overlayVariantImgContainer.hide()
                imgContainerVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_atc_variant_img_unselected)
                view.isEnabled = true
            }
        }

        view.setOnClickListener {
            listener.onVariantClicked(element)
        }
    }
}