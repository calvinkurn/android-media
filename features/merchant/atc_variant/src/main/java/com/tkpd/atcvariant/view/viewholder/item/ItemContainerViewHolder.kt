package com.tkpd.atcvariant.view.viewholder.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.databinding.ItemAtcVariantContainerViewHolderBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.unifycomponents.ChipsUnify
import java.util.*


/**
 * Created by yovi eka putra on 2022-11-10.
 */
class ItemContainerViewHolder(
    private val binding: ItemAtcVariantContainerViewHolderBinding,
    private val listener: AtcVariantListener
) : RecyclerView.ViewHolder(binding.root), AtcVariantListener by listener {

    private val context: Context
        get() = binding.root.context

    companion object {

        private const val ELLIPSIZE_VARIANT_NAME = 15

        fun create(
            parent: ViewGroup,
            listener: AtcVariantListener
        ): ItemContainerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemAtcVariantContainerViewHolderBinding.inflate(
                inflater,
                parent,
                false
            )
            return ItemContainerViewHolder(binding, listener)
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
                it.findViewById<ChipsUnify>(
                    com.tokopedia.product.detail.common.R.id.atc_variant_chip
                ).apply {
                    renderChipUI(element = element)
                    setState(element = element, position = position)
                }

                it.findViewById<ImageView>(
                    com.tokopedia.product.detail.common.R.id.atc_variant_chip_promo_icon
                ).apply {
                    renderFlashSale(element = element)
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

        chipText = ellipsize(element.variantName)
    }

    private fun ChipsUnify.setState(element: VariantOptionWithAttribute, position: Int) {
        setViewListener(
            element = element,
            state = VariantConstant.IGNORE_STATE,
            position = position
        )

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

    private fun ImageView.renderFlashSale(element: VariantOptionWithAttribute) {
        val isCampaignActive = element.flashSale
        val shouldRender = element.currentState != VariantConstant.STATE_EMPTY
            && element.currentState != VariantConstant.STATE_SELECTED_EMPTY

        showWithCondition(shouldRender && isCampaignActive)
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

    private fun ellipsize(text: String): String {
        text.take(ELLIPSIZE_VARIANT_NAME)
        return if (text.length > ELLIPSIZE_VARIANT_NAME) {
            "${text.substring(0, ELLIPSIZE_VARIANT_NAME)}..."
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
                context.getColorChecker(com.tokopedia.unifyprinciples.R.color.Unify_NN950_96)
            )
        }
    }
}
