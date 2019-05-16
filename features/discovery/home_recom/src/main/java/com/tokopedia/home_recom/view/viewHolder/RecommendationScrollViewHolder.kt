package com.tokopedia.home_recom.view.viewHolder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.dataModel.RecommendationScrollDataModel

class RecommendationScrollViewHolder(
        view: View
) : AbstractViewHolder<RecommendationScrollDataModel>(view){

    private val productName: TextView by lazy { view.findViewById<TextView>(R.id.product_name) }
    private val productDescription: TextView by lazy { view.findViewById<TextView>(R.id.product_description) }

    override fun bind(element: RecommendationScrollDataModel) {
        productName.text = element.products[0].name
        productDescription.text = element.products[0].name
    }
}