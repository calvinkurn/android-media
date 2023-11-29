package com.tokopedia.product.detail.view.viewholder.gwp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.databinding.GwpCardItemBinding
import com.tokopedia.product.detail.databinding.GwpProductImageBinding
import com.tokopedia.product.detail.databinding.GwpProductImageCountBinding
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.product.detail.R as productdetailR

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

class GWPCardOfProductViewHolder(
    private val binding: GwpCardItemBinding
) : GWPCardViewHolder<GWPWidgetUiModel.Card.Product>(binding.root) {

    override fun bind(data: GWPWidgetUiModel.Card.Product) {
        setTitle(title = data.title)
        setProductName(name = data.productName)
        setSubTitle(subTitle = data.subTitle)
        setProductImages(images = data.images)
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
        loadImage(url = image.imageUrl)
        setTextCounter(counter = image.counter)
    }.root

    private fun createProductImageView(): GwpProductImageBinding {
        val inflater = LayoutInflater.from(binding.root.context)
        return GwpProductImageBinding.inflate(inflater, binding.gwpImages, false)
    }

    private fun GwpProductImageBinding.loadImage(url: String) {
        gwpProductImage.loadImage(url) {
            centerCrop()
        }
    }

    private fun GwpProductImageBinding.setTextCounter(counter: String) {
        if (counter.isNotBlank()) {
            val showMoreView = createProductImageShowMoreView()
            showMoreView.gwpProductImageShowMoreText.text = counter
        }
    }

    private fun GwpProductImageBinding.createProductImageShowMoreView(): GwpProductImageCountBinding {
        val view = gwpProductImageShowMoreStub.inflate()
        return GwpProductImageCountBinding.bind(view)
    }

    companion object {
        val ID = productdetailR.layout.gwp_card_item
        private val PRODUCT_IMAGE_MARGIN_END = 4.toPx()

        fun create(parent: ViewGroup): GWPCardOfProductViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = GwpCardItemBinding.inflate(inflater)
            return GWPCardOfProductViewHolder(view)
        }
    }
}
