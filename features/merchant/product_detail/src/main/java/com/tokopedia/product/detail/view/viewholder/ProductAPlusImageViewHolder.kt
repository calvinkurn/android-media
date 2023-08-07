package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductAPlusImageDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicAPlusImageBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ProductAPlusImageViewHolder(
    itemView: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductAPlusImageDataModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_a_plus_image
    }

    private val binding = ItemDynamicAPlusImageBinding.bind(itemView)

    override fun bind(element: ProductAPlusImageDataModel) {
        setupDivider(element.showTopDivider)
        setupTitle(element)
        setupDescription(element)
        setupImage(element)
        setupToggle(element)
    }

    private fun setupDivider(showTopDivider: Boolean) {
        binding.dividerProductDetailAPlusImage.showWithCondition(showTopDivider)
    }

    private fun setupTitle(element: ProductAPlusImageDataModel) {
        binding.tvProductDetailAPlusImageTitle.apply {
            text = element.title
            showWithCondition(element.title.isNotBlank())
        }
    }

    private fun setupDescription(element: ProductAPlusImageDataModel) {
        binding.tvProductDetailAPlusImageDescription.apply {
            text = element.description
            showWithCondition(element.description.isNotBlank())
        }
    }

    private fun setupImage(element: ProductAPlusImageDataModel) {
        // Update the image ratio
        // Note: please make sure that all direct descendant views of root have id to prevent crash
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        constraintSet.setDimensionRatio(binding.ivProductDetailAPlusImage.id, element.ratio)
        constraintSet.applyTo(binding.root)
        binding.ivProductDetailAPlusImage.loadImage(element.url)
    }

    private fun setupToggle(element: ProductAPlusImageDataModel) {
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
            listener.onToggleAPlus(!element.collapsed)
        }
        binding.icProductDetailAPlusImageToggle.setOnClickListener {
            listener.onToggleAPlus(!element.collapsed)
        }
    }
}
