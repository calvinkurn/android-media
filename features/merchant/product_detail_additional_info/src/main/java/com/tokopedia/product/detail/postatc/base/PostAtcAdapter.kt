package com.tokopedia.product.detail.postatc.base

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.product.detail.postatc.component.error.ErrorDelegate
import com.tokopedia.product.detail.postatc.component.loading.LoadingDelegate
import com.tokopedia.product.detail.postatc.component.productinfo.ProductInfoDelegate
import com.tokopedia.product.detail.postatc.component.recommendation.RecommendationDelegate
import com.tokopedia.product.detail.postatc.component.recommendation.RecommendationUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

class PostAtcAdapter(
    listener: PostAtcListener
) : BaseAdapter<PostAtcUiModel>() {
    init {
        delegatesManager
            .addDelegate(ProductInfoDelegate(listener))
            .addDelegate(RecommendationDelegate(listener))
            .addDelegate(ErrorDelegate(listener))
            .addDelegate(LoadingDelegate())
    }


    fun updateRecommendation(widgets: List<RecommendationWidget>) {
        val items = getItems()
        widgets.forEach { widget ->
            items.filter { widget.pageName == it.name }.forEach { item ->
                (item as RecommendationUiModel).widget = widget
            }
        }
        notifyDataSetChanged()
    }
}
