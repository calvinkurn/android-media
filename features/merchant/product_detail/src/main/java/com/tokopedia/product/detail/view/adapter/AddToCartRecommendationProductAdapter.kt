package com.tokopedia.product.detail.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationProductDataModel

class AddToCartRecommendationProductAdapter(
        recommendationProductTypeFactory: RecommendationProductTypeFactory
) : BaseListAdapter<AddToCartDoneRecommendationProductDataModel,RecommendationProductTypeFactory>(recommendationProductTypeFactory)