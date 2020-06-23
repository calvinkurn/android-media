package com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R
import com.tokopedia.review.feature.reviewdetail.view.model.ProductFeedbackErrorUiModel

/**
 * Created by Yehezkiel on 29/04/20
 */
class FeedbackErrorViewHolder(view: View): AbstractViewHolder<ProductFeedbackErrorUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_not_found
    }

    override fun bind(element: ProductFeedbackErrorUiModel) {}
}