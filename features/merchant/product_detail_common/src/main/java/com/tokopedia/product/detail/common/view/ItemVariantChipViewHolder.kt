package com.tokopedia.product.detail.common.view

import android.view.View
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.R
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by Yehezkiel on 06/05/21
 */
class ItemVariantChipViewHolder(val view: View,
                                val listener: AtcVariantListener) : BaseAtcVariantItemViewHolder<VariantOptionWithAttribute>(view) {

    companion object {
        val LAYOUT = R.layout.atc_variant_chip_viewholder

        private const val ELLIPSIZE_VARIANT_NAME = 15
    }

    private val chipVariant = view.findViewById<ChipsUnify>(R.id.atc_variant_chip)

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
        val shouldShowFlashSale = element.currentState != VariantConstant.STATE_EMPTY
                && element.currentState != VariantConstant.STATE_SELECTED_EMPTY
        renderFlashSale(shouldShowFlashSale, element.flashSale)

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

    private fun renderFlashSale(shouldRender: Boolean, isCampaignActive: Boolean) {
        chipVariant.chip_new_notification.showIfWithBlock(shouldRender && isCampaignActive){
            text = resources.getString(R.string.atc_variant_promo)
        }
    }

    private fun setViewListener(element: VariantOptionWithAttribute, state: Int) {
        view.setOnClickListener {
            listener.onVariantClicked(element, state)
        }
    }

    private fun ellipsize(text: String, maxChar: Int): String{
        return if(text.length > maxChar){
            "${text.substring(0, maxChar)}..."
        } else text
    }
}