package com.tokopedia.product.info.view.viewholder.productdetail

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Video
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.widget.ExpandableAnimation
import com.tokopedia.product.share.ekstensions.layoutInflater
import com.tokopedia.utils.image.ImageUtils
import kotlinx.android.synthetic.main.bs_item_product_detail_expandable.view.*
import kotlinx.android.synthetic.main.item_youtube_thumbnail.view.*


/**
 * Created by Yehezkiel on 13/10/20
 */
class ProductDetailInfoExpandableViewHolder(private val view: View, private val listener: ProductDetailInfoListener) : AbstractViewHolder<ProductDetailInfoExpandableDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_expandable
    }

    override fun bind(element: ProductDetailInfoExpandableDataModel) {
        view.expandable_title_chevron?.titleText = "Deskripsi Produk"
        setupExpandableItem(element)
        setupVideoItem(element.video, element.isShowable)
    }

    private fun setupVideoItem(video: List<Video>, isShowable: Boolean) = with(view) {
        horizontal_scroll_container.showWithCondition(isShowable && video.isNotEmpty())
        repeat(5) {
            val layout = context.layoutInflater.inflate(R.layout.item_youtube_thumbnail, null)
            ImageUtils.loadImageRounded2(context, layout.video_thumbnail , "https://ecs7-p.tokopedia.net/img/cache/700/product-1/2020/7/28/17211033/17211033_a30b5d37-0bf9-41da-8273-b32731d2553b_1180_1180", 10F)
            horizontal_view_container.addView(layout)
        }
    }

    private fun setupExpandableItem(element: ProductDetailInfoExpandableDataModel) = with(view) {
        view.product_detail_value?.text = view.context.getString(R.string.lorem)
        product_detail_value?.showWithCondition(element.isShowable)
        expandable_title_chevron?.isExpand = element.isShowable

        setOnClickListener {
            expandable_title_chevron?.isExpand = expandable_title_chevron?.isExpand != true
            listener.closeAllExpand(element.uniqueIdentifier(), expandable_title_chevron?.isExpand
                    ?: false)
        }
        product_detail_value?.text = view.context.getString(R.string.lorem)
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