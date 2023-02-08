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

    fun updateRecommendation(uniqueId: Int, widget: RecommendationWidget) {
        val items = getItems()
        items.firstOrNull { it.id == uniqueId }?.apply {
            (this as RecommendationUiModel).widget = widget
            notifyDataSetChanged()
        }
    }

    fun removeComponent(uniqueId: Int) {
        getItems().firstOrNull { it.id == uniqueId }?.let {
            removeItem(it)
        }
    }
}
