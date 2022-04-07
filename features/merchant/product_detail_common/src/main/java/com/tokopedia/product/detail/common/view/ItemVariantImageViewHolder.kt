package com.tokopedia.product.detail.common.view

import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageFitCenter
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
        variantImg?.loadImageFitCenter(element.image100)
        setState(element)
    }

    private fun setState(element: VariantOptionWithAttribute) = with(view) {
        setViewListener(element, VariantConstant.IGNORE_STATE)
        if (listener.shouldHideTextHabis()) {
            overlayVariantImgContainer.text = ""
        } else {
            overlayVariantImgContainer.text = context.getString(R.string.atc_variant_empty)
        }

        val shouldShowFlashSale = element.currentState != VariantConstant.STATE_EMPTY
                && element.currentState != VariantConstant.STATE_SELECTED_EMPTY
        renderFlashSale(shouldShowFlashSale, element.flashSale)

        view.setOnClickListener {
            listener.onVariantClicked(element)
        }

        when (element.currentState) {
            VariantConstant.STATE_EMPTY -> {
                overlayVariantImgContainer?.apply {
                    background = MethodChecker.getDrawable(context, R.drawable.bg_round_corner_atc_variant_overlay)
                    show()
                }
                imgContainerVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_atc_variant_img_unselected)
            }
            VariantConstant.STATE_SELECTED -> {
                overlayVariantImgContainer.hide()
                imgContainerVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_atc_variant_img_selected)
                setViewListener(element, element.currentState)
            }
            VariantConstant.STATE_SELECTED_EMPTY -> {
                overlayVariantImgContainer?.apply {
                    background = MethodChecker.getDrawable(context, R.drawable.bg_round_corner_atc_variant_empty_overlay)
                    show()
                }
                imgContainerVariant.background = null
                setViewListener(element, element.currentState)
            }
            VariantConstant.STATE_UNSELECTED -> {
                overlayVariantImgContainer.hide()
                imgContainerVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_atc_variant_img_unselected)
            }
        }
    }

    private fun renderFlashSale(shouldShow: Boolean, isCampaignActive: Boolean) {
        if (shouldShow && isCampaignActive) {
            promoVariantImage.show()
        } else {
            promoVariantImage.hide()
        }
    }

    private fun setViewListener(element: VariantOptionWithAttribute, state: Int) {
        view.setOnClickListener {
            listener.onVariantClicked(element, state)
        }
    }
}