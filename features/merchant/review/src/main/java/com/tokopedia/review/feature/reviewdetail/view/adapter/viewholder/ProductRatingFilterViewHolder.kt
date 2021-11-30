package com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemProductFilterDetailBinding
import com.tokopedia.review.feature.reviewdetail.view.adapter.ProductRatingFilterAdapter
import com.tokopedia.review.feature.reviewdetail.view.adapter.SellerRatingAndTopicListener
import com.tokopedia.review.feature.reviewdetail.view.model.ProductReviewFilterUiModel

/**
 * Created by Yehezkiel on 28/04/20
 */
class ProductRatingFilterViewHolder(view: View, val listener: SellerRatingAndTopicListener) :
    AbstractViewHolder<ProductReviewFilterUiModel>(view) {

    var adapter: ProductRatingFilterAdapter? = null

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_product_filter_detail
    }

    private val binding = ItemProductFilterDetailBinding.bind(view)

    override fun bind(element: ProductReviewFilterUiModel) {
        adapter = ProductRatingFilterAdapter(binding.root, listener)
        adapter?.setData(element.ratingBarList)

        binding.rvProductFilter.adapter = adapter
    }

    override fun bind(element: ProductReviewFilterUiModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        adapter?.setData(element.ratingBarList)
    }
}