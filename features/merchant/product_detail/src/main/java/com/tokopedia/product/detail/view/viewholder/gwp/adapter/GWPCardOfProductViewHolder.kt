package com.tokopedia.product.detail.view.viewholder.gwp.adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.setLayoutWidth
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.extensions.getColorChecker
import com.tokopedia.product.detail.common.utils.extensions.addOnPdpImpressionListener
import com.tokopedia.product.detail.common.utils.extensions.updateLayoutParams
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.databinding.GwpCardItemBinding
import com.tokopedia.product.detail.databinding.GwpProductImageBinding
import com.tokopedia.product.detail.databinding.GwpProductImageCountBinding
import com.tokopedia.product.detail.view.viewholder.gwp.callback.GWPCallback
import com.tokopedia.product.detail.view.viewholder.gwp.event.GWPEvent
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.product.detail.R as productdetailR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class GWPCardOfProductViewHolder(
    private val binding: GwpCardItemBinding,
    private val callback: GWPCallback,
    private val getParentTrackData: () -> ComponentTrackDataModel
) : GWPCardViewHolder<GWPWidgetUiModel.Card.Product>(binding.root) {

    init {
        val context = binding.root.context
        val color = context.getColorChecker(unifyprinciplesR.color.Unify_NN0)
        binding.gwpCardItem.setCardUnifyBackgroundColor(color)
    }

    override fun bind(data: GWPWidgetUiModel.Card.Product) {
        setTitle(title = data.title)
        setProductName(name = data.productName)
        setSubTitle(subTitle = data.subTitle)
        setProductImages(images = data.images)
        setCardWidth(width = data.width)
        setEvent(data = data)
        setImpression(data = data)
    }

    private fun setTitle(title: String) {
        binding.gwpTitle.showIfWithBlock(title.isNotBlank()) {
            text = title
        }
    }

    private fun setProductName(name: String) {
        binding.gwpProductName.showIfWithBlock(name.isNotBlank()) {
            text = name
        }
    }

    private fun setSubTitle(subTitle: String) {
        binding.gwpSubtitle.showIfWithBlock(subTitle.isNotBlank()) {
            text = subTitle
        }
    }

    private fun setCardWidth(width: Int) {
        binding.gwpCardItemContainer.setLayoutWidth(width = width)
    }

    private fun setEvent(data: GWPWidgetUiModel.Card.Product) {
        binding.root.setOnClickListener {
            val updateData = data.copy(trackData = getTrackData(data))
            callback.event(GWPEvent.OnClickProduct(data = updateData))
        }
    }

    private fun setImpression(data: GWPWidgetUiModel.Card.Product) {
        binding.root.addOnPdpImpressionListener(
            holders = callback.impressionHolders,
            name = data.id.toString()
        ) {
            val updateData = data.copy(trackData = getTrackData(data))
            callback.event(GWPEvent.OnCardImpress(card = updateData))
        }
    }

    private fun setProductImages(images: List<GWPWidgetUiModel.Card.Product.Images>) {
        binding.gwpImages.removeAllViews()

        images.forEachIndexed { index, image ->
            val isLastIndex = index == images.size.dec()
            val productImageView = createProductImage(isLastIndex = isLastIndex, image = image)
            binding.gwpImages.addView(productImageView)
        }
    }

    private fun createProductImage(
        isLastIndex: Boolean,
        image: GWPWidgetUiModel.Card.Product.Images
    ) = createProductImageView().apply {
        setMarginEnd(isLastIndex = isLastIndex)
        loadImage(url = image.imageUrl)
        setImageOverlay(counter = image.counter)
        setTextCounter(counter = image.counter)
    }.root

    private fun createProductImageView(): GwpProductImageBinding {
        val inflater = LayoutInflater.from(binding.root.context)
        return GwpProductImageBinding.inflate(inflater, binding.gwpImages, false)
    }

    private fun GwpProductImageBinding.setMarginEnd(isLastIndex: Boolean) {
        if (!isLastIndex) {
            root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                this?.marginEnd = PRODUCT_IMAGE_MARGIN_END
            }
        }
    }

    private fun GwpProductImageBinding.loadImage(url: String) {
        gwpProductImage.loadImage(url) {
            centerCrop()
        }
    }

    private fun GwpProductImageBinding.setImageOverlay(counter: String) {
        if (counter.isNotBlank()) {
            val color = root.context.getColorChecker(unifyprinciplesR.color.Unify_Overlay_Lvl2)
            gwpProductImage.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun GwpProductImageBinding.setTextCounter(counter: String) {
        if (counter.isNotBlank()) {
            val showMoreView = createProductImageShowMoreView()
            showMoreView.root.text = counter
        }
    }

    private fun GwpProductImageBinding.createProductImageShowMoreView(): GwpProductImageCountBinding {
        val view = gwpProductImageShowMoreStub.inflate()
        return GwpProductImageCountBinding.bind(view)
    }

    companion object {
        val ID = productdetailR.layout.gwp_card_item
        private val PRODUCT_IMAGE_MARGIN_END = 4.toPx()

        fun create(
            parent: ViewGroup,
            callback: GWPCallback,
            getParentTrackData: () -> ComponentTrackDataModel
        ): GWPCardOfProductViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = GwpCardItemBinding.inflate(inflater)
            return GWPCardOfProductViewHolder(
                binding = view,
                callback = callback,
                getParentTrackData = getParentTrackData
            )
        }
    }
}
