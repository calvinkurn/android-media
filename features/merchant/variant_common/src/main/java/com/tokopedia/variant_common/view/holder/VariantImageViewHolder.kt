package com.tokopedia.variant_common.view.holder

import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.variant_common.R
import com.tokopedia.variant_common.databinding.ItemVariantImageViewHolderBinding

/**
 * Created by Yehezkiel on 08/03/20
 */

class VariantImageViewHolder(val view: View,
                             val listener: AtcVariantListener) : BaseVariantViewHolder<VariantOptionWithAttribute>(view) {

    companion object {
        val LAYOUT = R.layout.item_variant_image_view_holder
    }

    private val binding = ItemVariantImageViewHolderBinding.bind(view)
    private val context = binding.root.context

    override fun bind(element: VariantOptionWithAttribute, payload: Int) {
        setState(element)
    }

    override fun bind(element: VariantOptionWithAttribute) = with(binding) {
        variantImg.loadImage(element.image100, properties = {
            centerCrop()
        })
        setState(element)
    }

    private fun setState(element: VariantOptionWithAttribute) = with(binding) {
        if (element.flashSale) {
            promoVariantImage.show()
        } else {
            promoVariantImage.hide()
        }

        when (element.currentState) {
            VariantConstant.STATE_EMPTY -> {
                overlayVariantImgContainer.show()
                imgContainerVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_img_unselected)
                view.isEnabled = false
            }
            VariantConstant.STATE_SELECTED -> {
                overlayVariantImgContainer.hide()
                imgContainerVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_img_selected)
                view.isEnabled = false
            }
            VariantConstant.STATE_UNSELECTED -> {
                overlayVariantImgContainer.hide()
                imgContainerVariant.background = MethodChecker.getDrawable(context, R.drawable.bg_variant_img_unselected)
                view.isEnabled = true
            }
        }

        view.setOnClickListener {
            listener.onVariantClicked(element)
        }
    }
} 