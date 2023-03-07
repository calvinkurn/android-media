package com.tkpd.atcvariant.view.viewholder.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.databinding.ItemAtcVariantContainerChipGroupViewHolderBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.unifycomponents.ChipsUnify
import java.util.Locale


/**
 * Created by mzennis on 2020-03-11.
 */
class ItemContainerChipGroupViewHolder(
    private val binding: ItemAtcVariantContainerChipGroupViewHolderBinding,
    private val listener: AtcVariantListener
) : RecyclerView.ViewHolder(binding.root), AtcVariantListener by listener {

    private val context: Context
        get() = binding.root.context

    companion object {

        private const val ELLIPSIZE_VARIANT_NAME = 15

        fun create(
            parent: ViewGroup,
            listener: AtcVariantListener
        ): ItemContainerChipGroupViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemAtcVariantContainerChipGroupViewHolderBinding.inflate(
                inflater,
                parent,
                false
            )
            return ItemContainerChipGroupViewHolder(binding, listener)
        }
    }

    fun bind(data: VariantCategory) = with(binding) {
        processBind(variants = data.variantOptions)
        setSelectedOptionText(data)

        if (data.variantGuideline.isNotEmpty() && !listener.onVariantGuideLineHide()) {
            txtVariantGuideline.show()
            txtVariantGuideline.setOnClickListener {
                listener.onVariantGuideLineClicked(data.variantGuideline)
            }
        } else {
            txtVariantGuideline.hide()
        }
    }

    private fun processBind(variants: List<VariantOptionWithAttribute>) = with(binding) {
        chipGroupAtcVariant.removeAllViews()

        variants.forEachIndexed { index, data ->
            val child = createChip(element = data, position = index)
            chipGroupAtcVariant.addView(child)
        }
    }

    private fun createChip(element: VariantOptionWithAttribute, position: Int): View {
        return LayoutInflater.from(context)
            .inflate(
                com.tokopedia.product.detail.common.R.layout.atc_variant_chip_viewholder,
                binding.root,
                false
            ).also {
                val chip = it.findViewById<ChipsUnify>(
                    com.tokopedia.product.detail.common.R.id.atc_variant_chip
                ).apply {
                    renderChipUI(element = element)
                    setState(element = element, position = position)
                }
            }
    }

    private fun ChipsUnify.renderChipUI(element: VariantOptionWithAttribute) {
        val image100 = element.image100
        chip_image_icon.showIfWithBlock(image100.isNotEmpty()) {
            loadImage(image100, properties = {
                centerCrop()
            })
        }

        chipText = ellipsize(
            element.variantName,
            ELLIPSIZE_VARIANT_NAME
        )
    }

    private fun ChipsUnify.setState(element: VariantOptionWithAttribute, position: Int) {
        setViewListener(
            element = element,
            state = VariantConstant.IGNORE_STATE,
            position = position
        )
        renderFlashSale(element = element)

        when (element.currentState) {
            VariantConstant.STATE_EMPTY -> {
                chipType = ChipsUnify.TYPE_DISABLE
            }
            VariantConstant.STATE_SELECTED, VariantConstant.STATE_SELECTED_EMPTY -> {
                chipType = ChipsUnify.TYPE_SELECTED
                setViewListener(
                    element = element,
                    state = element.currentState,
                    position = position
                )
            }
            VariantConstant.STATE_UNSELECTED -> {
                chipType = ChipsUnify.TYPE_NORMAL
            }
            else -> {
                chipType = ChipsUnify.TYPE_DISABLE
            }
        }
    }

    private fun ChipsUnify.renderFlashSale(element: VariantOptionWithAttribute) {
        val isCampaignActive = element.flashSale
        val shouldRender = element.currentState != VariantConstant.STATE_EMPTY
            && element.currentState != VariantConstant.STATE_SELECTED_EMPTY

        chip_new_notification.showIfWithBlock(shouldRender && isCampaignActive) {
            text = resources.getString(
                com.tokopedia.product.detail.common.R.string.atc_variant_promo
            )
        }
    }

    private fun ChipsUnify.setViewListener(
        element: VariantOptionWithAttribute,
        state: Int,
        position: Int
    ) {
        setOnClickListener {
            listener.onVariantClicked(element, state)
        }

        if (element.currentState == VariantConstant.STATE_SELECTED)
            listener.onSelectionChanged(this, position)
    }

    private fun ellipsize(text: String, maxChar: Int): String {
        text.take(maxChar)
        return if (text.length > maxChar) {
            "${text.substring(0, maxChar)}..."
        } else text
    }

    private fun setSelectedOptionText(data: VariantCategory) = with(binding) {
        txtVariantCategoryName.text = context.getString(
            R.string.atc_variant_option_builder_3,
            data.name
        ).replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }

        if (data.getSelectedOption() == null || hideVariantName()) {
            txtVariantSelectedOption.text = ""
        } else {
            txtVariantSelectedOption.text = data.getSelectedOption()?.variantName
            txtVariantSelectedOption.setTextColor(
                context.getColorChecker(com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
            )
        }
    }
}
