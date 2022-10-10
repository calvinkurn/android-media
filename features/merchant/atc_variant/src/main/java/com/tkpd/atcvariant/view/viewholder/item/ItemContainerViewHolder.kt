package com.tkpd.atcvariant.view.viewholder.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.tkpd.atcvariant.R
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.unifycomponents.ChipsUnify
import java.util.*


/**
 * Created by mzennis on 2020-03-11.
 */
class ItemContainerViewHolder(val view: View, val listener: AtcVariantListener) :
    RecyclerView.ViewHolder(view), AtcVariantListener by listener {

    // private val variantOptionAdapter = AtcVariantOptionAdapter(this)
    // private val layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)

    private val txtVariantSelectedOption =
        view.findViewById<TextView>(R.id.txt_variant_selected_option)
    private val txtVariantCategoryName = view.findViewById<TextView>(R.id.txt_variant_category_name)
    private val txtVariantGuideline = view.findViewById<TextView>(R.id.txt_variant_guideline)
    private val rvVariant = view.findViewById<ChipGroup>(R.id.rv_atc_variant)

    private val context: Context
        get() = view.context

    companion object {

        private const val ELLIPSIZE_VARIANT_NAME = 15
    }

    fun bind(data: VariantCategory, isOptionChanged: Boolean) {
        if (isOptionChanged) {
            setSelectedOptionText(data)
            processBind(variants = data.variantOptions)
        }
    }

    fun bind(data: VariantCategory) {
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

    private fun processBind(variants: List<VariantOptionWithAttribute>) {
        rvVariant.removeAllViews()

        variants.forEachIndexed { index, data ->
            val child = createChip(element = data, position = index)
            rvVariant.addView(child)
        }
    }

    private fun createChip(element: VariantOptionWithAttribute, position: Int): View {
        return LayoutInflater.from(context)
            .inflate(
                com.tokopedia.product.detail.common.R.layout.atc_variant_chip_viewholder,
                null,
                false
            ).apply {
                val chip =
                    findViewById<ChipsUnify>(com.tokopedia.product.detail.common.R.id.atc_variant_chip)
                renderChipUI(chip = chip, element = element)
                setState(chip = chip, element = element, position = position)
            }
    }

    private fun renderChipUI(chip: ChipsUnify, element: VariantOptionWithAttribute) = with(chip) {
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

    private fun setState(chip: ChipsUnify, element: VariantOptionWithAttribute, position: Int) =
        with(chip) {
            setViewListener(
                chip = chip,
                element = element,
                state = VariantConstant.IGNORE_STATE,
                position = position
            )
            val shouldShowFlashSale = element.currentState != VariantConstant.STATE_EMPTY
                && element.currentState != VariantConstant.STATE_SELECTED_EMPTY
            renderFlashSale(chip = chip, shouldShowFlashSale, element.flashSale)

            when (element.currentState) {
                VariantConstant.STATE_EMPTY -> {
                    chipType = ChipsUnify.TYPE_DISABLE
                }
                VariantConstant.STATE_SELECTED, VariantConstant.STATE_SELECTED_EMPTY -> {
                    chipType = ChipsUnify.TYPE_SELECTED
                    setViewListener(
                        chip = chip,
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

    private fun renderFlashSale(
        chip: ChipsUnify,
        shouldRender: Boolean,
        isCampaignActive: Boolean
    ) = with(chip) {
        chip_new_notification.showIfWithBlock(shouldRender && isCampaignActive) {
            text = resources.getString(
                com.tokopedia.product.detail.common.R.string.atc_variant_promo
            )
        }
    }

    private fun setViewListener(
        chip: ChipsUnify,
        element: VariantOptionWithAttribute,
        state: Int,
        position: Int
    ) {
        chip.setOnClickListener {
            listener.onVariantClicked(element, state)
        }

        if (element.currentState == VariantConstant.STATE_SELECTED)
            listener.onSelectionChanged(chip, position)
    }

    private fun ellipsize(text: String, maxChar: Int): String {
        return if (text.length > maxChar) {
            "${text.substring(0, maxChar)}..."
        } else text
    }

    override fun onSelectionChanged(view: View, position: Int) {
        /* FIXME if (!layoutManager.isViewPartiallyVisible(view, true, true))
            view.post { rvVariant.smoothScrollToPosition(position) }*/
    }

    private fun setSelectedOptionText(data: VariantCategory) {
        txtVariantCategoryName.text =
            context.getString(R.string.atc_variant_option_builder_3, data.name)
                .capitalize(Locale.getDefault())

        if (data.getSelectedOption() == null || hideVariantName()) {
            txtVariantSelectedOption.text = ""
        } else {
            txtVariantSelectedOption.text = data.getSelectedOption()?.variantName
            txtVariantSelectedOption.setTextColor(
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_N700_96
                )
            )
        }
    }
}
