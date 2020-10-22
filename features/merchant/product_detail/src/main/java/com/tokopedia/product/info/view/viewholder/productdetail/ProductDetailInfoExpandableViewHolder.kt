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
import com.tokopedia.product.detail.common.data.model.product.Video
import com.tokopedia.product.detail.data.util.ProductCustomMovementMethod
import com.tokopedia.product.detail.data.util.thumbnailUrl
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
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
        setupVideoItem(element.video, element.isShowable)
    }

    private fun setupVideoItem(video: List<Video>, isShowable: Boolean) = with(view) {
        horizontal_scroll_container.showWithCondition(isShowable && video.isNotEmpty())
        video.forEachIndexed { index, it ->
            val layout = context.layoutInflater.inflate(R.layout.bs_item_product_detail_youtube_img, null)
            layout.img_youtube_item.loadImage(it.thumbnailUrl)
            layout.setOnClickListener {
                listener.goToVideoPlayer(video.map { it.url }, index)
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
        product_detail_value?.text = HtmlLinkHelper(context,
                element.textValue.replace("(\r\n|\n)".toRegex(), "<br />")).spannedString
        setSelectClickableTextView()
    }

    private fun setSelectClickableTextView() = with(view) {
        product_detail_value?.autoLinkMask = 0
        Linkify.addLinks(product_detail_value, Linkify.WEB_URLS)

        val selectable = when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.O -> false
            else -> true
        }
        product_detail_value?.setTextIsSelectable(selectable)
        product_detail_value?.movementMethod = ProductCustomMovementMethod(listener::onBranchLinkClicked)
    }

    override fun bind(element: ProductDetailInfoExpandableDataModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (payloads.isNotEmpty()) {
            val bundle = payloads[0] as Bundle
            if (bundle.containsKey("toggle")) {
                val toggle = bundle.getBoolean("toggle")
                if (toggle) {
                    ExpandableAnimation.expand(view.product_detail_value, itemView.width) {
                        view.horizontal_scroll_container.showWithCondition(toggle)
                    }
                } else {
                    view.horizontal_scroll_container.hide()
                    ExpandableAnimation.collapse(view.product_detail_value)
                }
                view.expandable_title_chevron?.isExpand = toggle
            }
        }
    }
}