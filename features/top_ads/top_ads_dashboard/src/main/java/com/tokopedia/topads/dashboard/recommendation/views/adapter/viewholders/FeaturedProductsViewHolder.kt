package com.tokopedia.topads.dashboard.recommendation.views.adapter.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.FeaturedProductsUiModel
import com.tokopedia.unifycomponents.ImageUnify

class FeaturedProductsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val image: ImageUnify = view.findViewById(R.id.productImg)

    fun bind(item: FeaturedProductsUiModel) {
        image.urlSrc = item.imgUrl
    }
}
