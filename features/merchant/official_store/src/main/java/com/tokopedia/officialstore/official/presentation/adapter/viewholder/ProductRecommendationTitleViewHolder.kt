package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.support.v7.widget.AppCompatTextView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.ProductRecommendationTitleViewModel

class ProductRecommendationTitleViewHolder(view: View): AbstractViewHolder<ProductRecommendationTitleViewModel>(view) {

    private var textView: AppCompatTextView? = null

    init {
        textView = view?.findViewById(R.id.txt_title_product_recom)
    }

    override fun bind(element: ProductRecommendationTitleViewModel?) {
        textView?.text = element?.title
    }

    companion object {
        val LAYOUT = R.layout.viewmodel_title_product_recom
    }
}