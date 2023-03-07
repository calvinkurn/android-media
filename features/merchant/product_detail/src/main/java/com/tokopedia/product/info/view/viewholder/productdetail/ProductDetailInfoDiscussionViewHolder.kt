package com.tokopedia.product.info.view.viewholder.productdetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.BsItemProductDetailDiscussionBinding
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.models.ProductDetailInfoDiscussionDataModel

/**
 * Created by Yehezkiel on 16/10/20
 */
class ProductDetailInfoDiscussionViewHolder(
    private val view: View,
    private val listener: ProductDetailInfoListener
) : AbstractViewHolder<ProductDetailInfoDiscussionDataModel>(view) {

    companion object {

        val LAYOUT = R.layout.bs_item_product_detail_discussion
    }

    private val binding = BsItemProductDetailDiscussionBinding.bind(view)

    override fun bind(element: ProductDetailInfoDiscussionDataModel) {
        binding.productDetailDiscussionButton.text = if (element.discussionCount > 0) {
            view.context.getString(R.string.product_detail_check_discussion_label)
        } else {
            view.context.getString(R.string.product_detail_ask_discussion_label)
        }

        binding.productDetailDiscussionButton.setOnClickListener {
            listener.goToDiscussion(element.discussionCount)
        }

        binding.productDetailDiscussionTitle.text = element.title
    }
}
