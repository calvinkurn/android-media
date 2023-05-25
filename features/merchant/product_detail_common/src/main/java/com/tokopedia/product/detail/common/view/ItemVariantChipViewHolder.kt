package com.tokopedia.product.detail.common.view

import android.view.View
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.R
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by Yehezkiel on 06/05/21
 */
class ItemVariantChipViewHolder(
    val view: View,
    val listener: AtcVariantListener
) : BaseAtcVariantItemViewHolder<VariantOptionWithAttribute>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_chip_viewholder

        private const val ELLIPSIZE_VARIANT_NAME = 15
    }

    private val chipVariant = view.findViewById<ChipsUnify>(R.id.atc_variant_chip)
    private val variantPromoIcon by lazyThreadSafetyNone {
        view.findViewById<ImageView>(R.id.atc_variant_chip_promo_icon)
    }

    override fun bind(element: VariantOptionWithAttribute, payload: Int) {
        setState(element)
    }

    override fun bind(element: VariantOptionWithAttribute) = with(chipVariant) {
        val image100 = element.image100
        chip_image_icon.showIfWithBlock(image100.isNotEmpty()) {
            loadImage(image100, properties = {
                centerCrop()
            })
        }

        chipText = ellipsize(element.variantName, ELLIPSIZE_VARIANT_NAME)

        setState(element)
    }

    private fun setState(element: VariantOptionWithAttribute) = with(view) {
        setViewListener(element, VariantConstant.IGNORE_STATE)

        renderFlashSale(element = element)

        when (element.currentState) {
            VariantConstant.STATE_EMPTY -> {
                chipVariant.chipType = ChipsUnify.TYPE_DISABLE
            }
            VariantConstant.STATE_SELECTED, VariantConstant.STATE_SELECTED_EMPTY -> {
                chipVariant.chipType = ChipsUnify.TYPE_SELECTED
                setViewListener(element, element.currentState)
            }
            VariantConstant.STATE_UNSELECTED -> {
                chipVariant.chipType = ChipsUnify.TYPE_NORMAL
            }
            else -> {
                chipVariant.chipType = ChipsUnify.TYPE_DISABLE
            }
        }
    }

    private fun renderFlashSale(element: VariantOptionWithAttribute) {
        val isCampaignActive = element.flashSale
        val shouldShowFlashSale = element.currentState != VariantConstant.STATE_EMPTY &&
            element.currentState != VariantConstant.STATE_SELECTED_EMPTY
        variantPromoIcon.showWithCondition(shouldShowFlashSale && isCampaignActive)
    }

    private fun setViewListener(element: VariantOptionWithAttribute, state: Int) {
        view.setOnClickListener {
            listener.onVariantClicked(element, state)
        }
    }

    private fun ellipsize(text: String, maxChar: Int): String {
        return if (text.length > maxChar) {
            "${text.substring(0, maxChar)}..."
        } else {
            text
        }
    }
}
