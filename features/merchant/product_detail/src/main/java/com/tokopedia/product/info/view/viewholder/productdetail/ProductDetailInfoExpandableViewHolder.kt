package com.tokopedia.product.info.view.viewholder.productdetail

import android.os.Build
import android.os.Bundle
import android.text.util.Linkify
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo
import com.tokopedia.product.detail.data.util.ProductCustomMovementMethod
import com.tokopedia.product.detail.data.util.thumbnailUrl
import com.tokopedia.product.detail.databinding.BsItemProductDetailExpandableBinding
import com.tokopedia.product.detail.databinding.BsItemProductDetailYoutubeImgBinding
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.adapter.diffutil.ProductDetailInfoDiffUtil.Companion.DIFFUTIL_PAYLOAD_TOGGLE
import com.tokopedia.product.info.widget.ExpandableAnimation
import com.tokopedia.product.share.ekstensions.layoutInflater
import com.tokopedia.unifycomponents.HtmlLinkHelper


/**
 * Created by Yehezkiel on 13/10/20
 */
class ProductDetailInfoExpandableViewHolder(private val view: View, private val listener: ProductDetailInfoListener) : AbstractViewHolder<ProductDetailInfoExpandableDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_expandable
    }

    private val binding = BsItemProductDetailExpandableBinding.bind(view)

    override fun bind(element: ProductDetailInfoExpandableDataModel) {
        binding.expandableTitleChevron.titleText = element.title
        setupExpandableItem(element)
        setupVideoItem(element.youtubeVideo, element.isShowable)
    }

    private fun setupVideoItem(youtubeVideo: List<YoutubeVideo>, isShowable: Boolean) = with(binding) {
        horizontalScrollContainer.showWithCondition(isShowable && youtubeVideo.isNotEmpty())
        horizontalViewContainer.removeAllViews()
        youtubeVideo.forEachIndexed { index, it ->
            val layout = view.context.layoutInflater.inflate(R.layout.bs_item_product_detail_youtube_img, null)
            val youtubeImgBinding = BsItemProductDetailYoutubeImgBinding.bind(layout)
            youtubeImgBinding.imgYoutubeItem.loadImage(it.thumbnailUrl)
            layout.setOnClickListener {
                listener.goToVideoPlayer(youtubeVideo.map { it.url }, index)
            }
            horizontalViewContainer.addView(layout)
        }
    }

    private fun setupExpandableItem(element: ProductDetailInfoExpandableDataModel) = with(binding) {
        productDetailValue.showWithCondition(element.isShowable)
        expandableTitleChevron.isExpand = element.isShowable

        expandableTitleChevron.setOnClickListener {
            expandableTitleChevron.isExpand = expandableTitleChevron.isExpand != true
            listener.closeAllExpand(element.uniqueIdentifier(), expandableTitleChevron.isExpand
                    ?: false)
        }
        binding.productDetailValue.text = HtmlLinkHelper(view.context,
                element.textValue.replace("(\r\n|\n)".toRegex(), "<br />")).spannedString
        setSelectableText()
        setSelectClickableTextView()
    }

    private fun setSelectableText() = with(binding) {
        val selectable = when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.O -> false
            else -> true
        }
        productDetailValue.setTextIsSelectable(selectable)
    }

    private fun setSelectClickableTextView() = with(binding) {

        productDetailValue.autoLinkMask = 0
        Linkify.addLinks(productDetailValue, Linkify.WEB_URLS)
        productDetailValue.movementMethod = ProductCustomMovementMethod(listener::onBranchLinkClicked)
    }

    override fun bind(element: ProductDetailInfoExpandableDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (payloads.isNotEmpty()) {
            val bundle = payloads[0] as Bundle
            if (bundle.containsKey(DIFFUTIL_PAYLOAD_TOGGLE)) {
                val toggle = bundle.getBoolean(DIFFUTIL_PAYLOAD_TOGGLE)
                if (toggle) {
                    binding.productDetailValue.setTextIsSelectable(true)
                    ExpandableAnimation.expand(binding.productDetailValue, customParentWidth = view.width) {
                        binding.horizontalScrollContainer.showWithCondition(toggle)
                    }
                } else {
                    //Need to set selectable to false, to prevent glitch
                    binding.productDetailValue.setTextIsSelectable(false)
                    binding.horizontalScrollContainer.hide()
                    ExpandableAnimation.collapse(binding.productDetailValue)
                }
                //Also we need to re-assign linkify, if not the link will not be clickable
                setSelectClickableTextView()
                binding.expandableTitleChevron.isExpand = toggle
            }
        }
    }
}