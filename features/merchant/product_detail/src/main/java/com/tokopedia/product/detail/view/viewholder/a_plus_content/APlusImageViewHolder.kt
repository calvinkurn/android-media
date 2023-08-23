package com.tokopedia.product.detail.view.viewholder.a_plus_content

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updatePadding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.updateLayoutParams
import com.tokopedia.product.detail.common.utils.extensions.validDimensionRatio
import com.tokopedia.product.detail.databinding.ItemAPlusImageBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder
import com.tokopedia.unifycomponents.toPx

class APlusImageViewHolder(
    itemView: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<APlusImageUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_a_plus_image

        private const val LAST_ITEM_PADDING_BOTTOM = 16
    }

    private val binding = ItemAPlusImageBinding.bind(itemView)

    override fun bind(element: APlusImageUiModel) {
        setupDivider(element.showTopDivider)
        setupTitle(element)
        setupDescription(element)
        setupImage(element)
        setupToggle(element)
        setupBottomPadding(element.haveBottomPadding)
        setupImpressionListener(element)
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        resetImage()
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
            setImageRatio(element.ratio)
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
                shouldShow = element.showOnCollapsed != element.expanded && element.ctaText.isNotBlank()
            )
        }
        binding.icProductDetailAPlusImageToggle.apply {
            setImage(
                newIconId = if (element.expanded) {
                    IconUnify.CHEVRON_UP
                } else {
                    IconUnify.CHEVRON_DOWN
                }
            )
            showWithCondition(
                shouldShow = element.showOnCollapsed != element.expanded && element.ctaText.isNotBlank()
            )
        }
        binding.tvProductDetailAPlusImageToggle.setOnClickListener {
            listener.onToggleAPlus(
                expanded = !element.expanded,
                componentTrackerData = getComponentTrackData(element)
            )
        }
        binding.icProductDetailAPlusImageToggle.setOnClickListener {
            listener.onToggleAPlus(
                expanded = !element.expanded,
                componentTrackerData = getComponentTrackData(element)
            )
        }
    }

    private fun setupBottomPadding(haveBottomPadding: Boolean) {
        binding.root.updatePadding(
            bottom = if (haveBottomPadding) LAST_ITEM_PADDING_BOTTOM.toPx() else Int.ZERO
        )
    }

    private fun setupImpressionListener(element: APlusImageUiModel) {
        binding.root.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }

    private fun resetImage() {
        binding.ivProductDetailAPlusImage.clearImage()
    }

    private fun setImageRatio(ratio: String) {
        binding.ivProductDetailAPlusImage.updateLayoutParams<ConstraintLayout.LayoutParams> {
            if (this != null) dimensionRatio = ratio
        }
    }
}
