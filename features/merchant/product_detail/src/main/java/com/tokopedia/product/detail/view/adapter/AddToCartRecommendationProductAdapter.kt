package com.tokopedia.product.detail.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.product.detail.data.model.addtocartrecommendation.RecommendationProductDataModel

class AddToCartRecommendationProductAdapter(
        recommendationProductTypeFactory: RecommendationProductTypeFactory
) : BaseListAdapter<RecommendationProductDataModel,RecommendationProductTypeFactory>(recommendationProductTypeFactory)