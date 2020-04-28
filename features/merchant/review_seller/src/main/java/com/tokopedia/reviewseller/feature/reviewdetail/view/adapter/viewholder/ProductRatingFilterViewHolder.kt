package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.ProductRatingFilterAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SellerRatingAndTopicListener
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.ProductReviewFilterUiModel
import kotlinx.android.synthetic.main.item_product_filter_detail.view.*

/**
 * Created by Yehezkiel on 28/04/20
 */
class ProductRatingFilterViewHolder(val view: View, val listener: SellerRatingAndTopicListener) : AbstractViewHolder<ProductReviewFilterUiModel>(view) {

    var adapter: ProductRatingFilterAdapter? = null
    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_product_filter_detail
    }

    override fun bind(element: ProductReviewFilterUiModel) {
        adapter = ProductRatingFilterAdapter(view,listener)
        adapter?.setData(element.ratingBarList)

        view.rv_product_filter.adapter = adapter
    }

    override fun bind(element: ProductReviewFilterUiModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        adapter?.setData(element.ratingBarList)
    }
}