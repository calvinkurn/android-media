package com.tokopedia.product.detail.view.viewholder.a_plus_content

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.validDimensionRatio
import com.tokopedia.product.detail.databinding.ItemAPlusImageBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class APlusImageViewHolder(
    itemView: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<APlusImageUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_a_plus_image
    }

    private val binding = ItemAPlusImageBinding.bind(itemView)

    override fun bind(element: APlusImageUiModel) {
        setupDivider(element.showTopDivider)
        setupTitle(element)
        setupDescription(element)
        setupImage(element)
        setupToggle(element)
        setupImpressionListener(element)
    }

    private fun setupDivider(showTopDivider: Boolean) {
        binding.dividerProductDetailAPlusImage.showWithCondition(showTopDivider)
    }

    private fun setupTitle(element: APlusImageUiModel) {
        binding.tvProductDetailAPlusImageTitle.apply {
            text = element.title
            showWithCondition(element.title.isNotBlank())
        }
    }

    private fun setupDescription(element: APlusImageUiModel) {
        binding.tvProductDetailAPlusImageDescription.apply {
            text = element.description
            showWithCondition(element.description.isNotBlank())
        }
    }

    private fun setupImage(element: APlusImageUiModel) {
        if (element.url.isNotBlank() && element.ratio.validDimensionRatio()) {
            // Update the image ratio
            // Note: please make sure that all direct descendant views of root have id to prevent crash
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.root)
            constraintSet.setDimensionRatio(binding.ivProductDetailAPlusImage.id, element.ratio)
            constraintSet.applyTo(binding.root)
            binding.ivProductDetailAPlusImage.loadImage(element.url)
            binding.ivProductDetailAPlusImage.show()
        } else {
            binding.ivProductDetailAPlusImage.gone()
        }
    }

    private fun setupToggle(element: APlusImageUiModel) {
        binding.tvProductDetailAPlusImageToggle.apply {
            text = element.ctaText
            showWithCondition(
                shouldShow = element.showOnCollapsed == element.collapsed && element.ctaText.isNotBlank()
            )
        }
        binding.icProductDetailAPlusImageToggle.apply {
            setImage(
                newIconId = if (element.collapsed) {
                    IconUnify.CHEVRON_DOWN
                } else {
                    IconUnify.CHEVRON_UP
                }
            )
            showWithCondition(
                shouldShow = element.showOnCollapsed == element.collapsed && element.ctaText.isNotBlank()
            )
        }
        binding.tvProductDetailAPlusImageToggle.setOnClickListener {
            listener.onToggleAPlus(
                !element.collapsed,
                element.trackerData.copy(adapterPosition = bindingAdapterPosition)
            )
        }
        binding.icProductDetailAPlusImageToggle.setOnClickListener {
            listener.onToggleAPlus(
                !element.collapsed,
                element.trackerData.copy(adapterPosition = bindingAdapterPosition)
            )
        }
    }

    private fun setupImpressionListener(element: APlusImageUiModel) {
        binding.root.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(
                element.trackerData.copy(adapterPosition = bindingAdapterPosition)
            )
        }
    }
}
