package com.tokopedia.product.detail.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.RecommendationProductViewModel
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.trackingoptimizer.TrackingQueue

class AddToCartRecommendationProductAdapter(
        recommendationProductTypeFactory: RecommendationProductTypeFactory
) : BaseListAdapter<RecommendationProductViewModel,RecommendationProductTypeFactory>(recommendationProductTypeFactory)