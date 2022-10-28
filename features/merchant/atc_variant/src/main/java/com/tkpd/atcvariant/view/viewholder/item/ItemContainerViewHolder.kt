package com.tkpd.atcvariant.view.viewholder.item

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.databinding.ItemAtcVariantContainerViewHolderBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.common.view.AtcVariantOptionAdapter
import java.util.Locale


/**
 * Created by mzennis on 2020-03-11.
 */

/**
 * please remove this class if [ItemContainerChipGroupViewHolder] has stable
 */
class ItemContainerViewHolder(
    private val binding: ItemAtcVariantContainerViewHolderBinding,
    private val listener: AtcVariantListener
) : RecyclerView.ViewHolder(binding.root), AtcVariantListener by listener {

    private val context: Context
        get() = binding.root.context

    private val variantOptionAdapter = AtcVariantOptionAdapter(this)

    companion object {
        private const val NUMBER_OF_VARIANT_THRESHOLD = 25

        fun create(
            parent: ViewGroup,
            listener: AtcVariantListener
        ): ItemContainerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemAtcVariantContainerViewHolderBinding.inflate(inflater, parent, false)
            return ItemContainerViewHolder(binding, listener)
        }
    }

    init {
        with(binding.rvAtcVariant) {
            adapter = variantOptionAdapter
            setHasFixedSize(true)
            itemAnimator = null
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
        setupFlexBox(dataSize = variants.size)
        variantOptionAdapter.setData(variants)
    }

    /**
     * Data size is for one level options, we want to use this to treshold flexbox to be column or row
     * This approach is to fix flexbox issue child disappear when it wrap with scrollview
     * https://github.com/google/flexbox-layout/issues/420
     */
    private fun setupFlexBox(dataSize: Int) = with(binding) {
        val flexboxManager = FlexboxLayoutManager(context).apply {
            alignItems = AlignItems.FLEX_START
        }

        if (dataSize > NUMBER_OF_VARIANT_THRESHOLD) {
            flexboxManager.flexDirection = FlexDirection.COLUMN
        } else {
            flexboxManager.flexDirection = FlexDirection.ROW
        }

        rvAtcVariant.layoutManager = flexboxManager

        if (rvAtcVariant.itemDecorationCount == 0) {
            val itemDecoration = FlexboxItemDecoration(context).apply {
                setDrawable(ContextCompat.getDrawable(context, R.drawable.bg_atc_chip_divider))
                setOrientation(FlexboxItemDecoration.HORIZONTAL)
            }

            rvAtcVariant.addItemDecoration(itemDecoration)
        }
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
