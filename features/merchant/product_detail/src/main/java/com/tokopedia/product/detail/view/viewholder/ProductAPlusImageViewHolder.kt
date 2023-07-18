package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
        setupTitle(element)
        setupImage(element)
        setupToggle(element)
    }

    private fun setupTitle(element: ProductAPlusImageDataModel) {
        binding.tvProductDetailAPlusImageTitle.apply {
            text = element.title
            showWithCondition(element.title.isNotBlank())
        }
    }

    private fun setupImage(element: ProductAPlusImageDataModel) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.wrapperProductDetailAPlusImage)
        constraintSet.setDimensionRatio(binding.ivProductDetailAPlusImage.id, element.ratio)
        constraintSet.applyTo(binding.wrapperProductDetailAPlusImage)
        binding.ivProductDetailAPlusImage.loadImage(element.url)
    }

    private fun setupToggle(element: ProductAPlusImageDataModel) {
        binding.tvProductDetailAPlusImageToggle.apply {
            text = element.ctaText
            showWithCondition(
                shouldShow = element.showOnCollapsed == element.collapsed && element.ctaText.isNotBlank()
            )
        }
        binding.tvProductDetailAPlusImageToggle.setOnClickListener {
            listener.onToggleAPlus(!element.collapsed)
        }
    }
}
