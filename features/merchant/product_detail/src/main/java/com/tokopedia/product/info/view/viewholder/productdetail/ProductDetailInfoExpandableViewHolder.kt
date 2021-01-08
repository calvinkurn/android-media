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
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.adapter.diffutil.ProductDetailInfoDiffUtil.Companion.DIFFUTIL_PAYLOAD_TOGGLE
import com.tokopedia.product.info.widget.ExpandableAnimation
import com.tokopedia.product.share.ekstensions.layoutInflater
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.bs_item_product_detail_expandable.view.*
import kotlinx.android.synthetic.main.bs_item_product_detail_youtube_img.view.*


/**
 * Created by Yehezkiel on 13/10/20
 */
class ProductDetailInfoExpandableViewHolder(private val view: View, private val listener: ProductDetailInfoListener) : AbstractViewHolder<ProductDetailInfoExpandableDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_expandable
    }

    override fun bind(element: ProductDetailInfoExpandableDataModel) {
        view.expandable_title_chevron?.titleText = element.title
        setupExpandableItem(element)
        setupVideoItem(element.youtubeVideo, element.isShowable)
    }

    private fun setupVideoItem(youtubeVideo: List<YoutubeVideo>, isShowable: Boolean) = with(view) {
        horizontal_scroll_container.showWithCondition(isShowable && youtubeVideo.isNotEmpty())
        horizontal_view_container.removeAllViews()
        youtubeVideo.forEachIndexed { index, it ->
            val layout = context.layoutInflater.inflate(R.layout.bs_item_product_detail_youtube_img, null)
            layout.img_youtube_item.loadImage(it.thumbnailUrl)
            layout.setOnClickListener {
                listener.goToVideoPlayer(youtubeVideo.map { it.url }, index)
            }
            horizontal_view_container.addView(layout)
        }
    }

    private fun setupExpandableItem(element: ProductDetailInfoExpandableDataModel) = with(view) {
        product_detail_value?.showWithCondition(element.isShowable)
        expandable_title_chevron?.isExpand = element.isShowable

        expandable_title_chevron?.setOnClickListener {
            expandable_title_chevron?.isExpand = expandable_title_chevron?.isExpand != true
            listener.closeAllExpand(element.uniqueIdentifier(), expandable_title_chevron?.isExpand
                    ?: false)
        }
        view.product_detail_value?.text = HtmlLinkHelper(view.context,
                element.textValue.replace("(\r\n|\n)".toRegex(), "<br />")).spannedString
        setSelectableText()
        setSelectClickableTextView()
    }

    private fun setSelectableText() = with(view) {
        val selectable = when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.O -> false
            else -> true
        }
        product_detail_value?.setTextIsSelectable(selectable)
    }

    private fun setSelectClickableTextView() = with(view) {

        product_detail_value?.autoLinkMask = 0
        Linkify.addLinks(product_detail_value, Linkify.WEB_URLS)
        product_detail_value?.movementMethod = ProductCustomMovementMethod(listener::onBranchLinkClicked)
    }

    override fun bind(element: ProductDetailInfoExpandableDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (payloads.isNotEmpty()) {
            val bundle = payloads[0] as Bundle
            if (bundle.containsKey(DIFFUTIL_PAYLOAD_TOGGLE)) {
                val toggle = bundle.getBoolean(DIFFUTIL_PAYLOAD_TOGGLE)
                if (toggle) {
                    view.product_detail_value?.setTextIsSelectable(true)
                    ExpandableAnimation.expand(view.product_detail_value, customParentWidth = view.width) {
                        view.horizontal_scroll_container.showWithCondition(toggle)
                    }
                } else {
                    //Need to set selectable to false, to prevent glitch
                    view.product_detail_value?.setTextIsSelectable(false)
                    view.horizontal_scroll_container.hide()
                    ExpandableAnimation.collapse(view.product_detail_value)
                }
                //Also we need to re-assign linkify, if not the link will not be clickable
                setSelectClickableTextView()
                view.expandable_title_chevron?.isExpand = toggle
            }
        }
    }
}