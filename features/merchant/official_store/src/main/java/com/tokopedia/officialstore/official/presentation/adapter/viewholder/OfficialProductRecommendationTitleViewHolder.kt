package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.ProductRecommendationTitleDataModel

class OfficialProductRecommendationTitleViewHolder(view: View): AbstractViewHolder<ProductRecommendationTitleDataModel>(view) {

    private var textView: AppCompatTextView? = null

    init {
        textView = view?.findViewById(R.id.txt_title_product_recom)
    }

    override fun bind(element: ProductRecommendationTitleDataModel) {
        textView?.text = element.title
    }

    companion object {
        val LAYOUT = R.layout.viewmodel_title_product_recom
    }
}