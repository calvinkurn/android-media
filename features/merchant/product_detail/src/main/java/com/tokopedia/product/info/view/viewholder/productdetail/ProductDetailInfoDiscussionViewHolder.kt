package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoDiscussionDataModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import kotlinx.android.synthetic.main.bs_item_product_detail_discussion.view.*

/**
 * Created by Yehezkiel on 16/10/20
 */
class ProductDetailInfoDiscussionViewHolder(private val view: View,
                                            private val listener: ProductDetailInfoListener) : AbstractViewHolder<ProductDetailInfoDiscussionDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_discussion
    }

    override fun bind(element: ProductDetailInfoDiscussionDataModel) {
        view.product_detail_discussion_button?.text = if (element.discussionCount > 0) {
            view.context.getString(R.string.product_detail_check_discussion_label)
        } else {
            view.context.getString(R.string.product_detail_ask_discussion_label)
        }

        view.product_detail_discussion_button?.setOnClickListener {
            listener.goToDiscussion(element.discussionCount)
        }

        view.product_detail_discussion_title?.text = element.title
    }
}