package com.tokopedia.product.info.view.viewholder.productdetail

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.BsItemProductDetailExpandableImageBinding
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.adapter.diffutil.ProductDetailInfoDiffUtil.Companion.DIFFUTIL_PAYLOAD_TOGGLE
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableImageDataModel
import com.tokopedia.product.info.widget.ExpandableAnimation

/**
 * Created by Yehezkiel on 14/10/20
 */
class ProductDetailInfoExpandableImageViewHolder(
    private val view: View,
    private val listener: ProductDetailInfoListener
) : AbstractViewHolder<ProductDetailInfoExpandableImageDataModel>(view) {

    companion object {

        val LAYOUT = R.layout.bs_item_product_detail_expandable_image
    }

    private val binding = BsItemProductDetailExpandableImageBinding.bind(view)

    override fun bind(element: ProductDetailInfoExpandableImageDataModel) {
        binding.expandableTitleChevron.titleText = element.title
        setupExpandableItem(element)
    }

    private fun setupExpandableItem(element: ProductDetailInfoExpandableImageDataModel) =
        with(binding) {
            expandableImage.loadImage(element.imageUrl)

            expandableImage.showWithCondition(element.isShowable)
            expandableImage.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.goToImagePreview(element.imageUrl)
                }

            })
            expandableTitleChevron.isExpand = element.isShowable

            expandableTitleChevron.setOnClickListener {
                expandableTitleChevron.isExpand = expandableTitleChevron.isExpand != true
                listener.closeAllExpand(
                    element.uniqueIdentifier(), expandableTitleChevron.isExpand
                        ?: false
                )
            }
        }

    override fun bind(
        element: ProductDetailInfoExpandableImageDataModel,
        payloads: MutableList<Any>
    ) {
        super.bind(element, payloads)
        if (payloads.isNotEmpty()) {
            val bundle = payloads[0] as Bundle
            if (bundle.containsKey(DIFFUTIL_PAYLOAD_TOGGLE)) {
                val toggle = bundle.getBoolean(DIFFUTIL_PAYLOAD_TOGGLE)
                if (toggle) {
                    ExpandableAnimation.expand(
                        binding.expandableImage,
                        customHeight = view.resources.displayMetrics.widthPixels
                    ) {
                        binding.expandableImage.loadImage(element.imageUrl)
                    }
                } else {
                    ExpandableAnimation.collapse(binding.expandableImage)
                }
                binding.expandableTitleChevron.isExpand = toggle
            }
        }
    }
}
