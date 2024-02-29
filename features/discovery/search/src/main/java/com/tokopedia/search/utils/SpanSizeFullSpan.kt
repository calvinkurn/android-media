package com.tokopedia.search.utils

import com.tokopedia.search.result.presentation.model.CouponDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.RecommendationItemViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.SmallGridProductItemViewHolder
import com.tokopedia.search.result.product.coupon.CouponGridViewHolder
import com.tokopedia.search.result.product.inspirationwidget.card.SmallGridInspirationCardViewHolder
import com.tokopedia.search.result.product.productitem.GridProductItemViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.reimagine.InspirationKeywordReimagineViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.viewholder.GridInspirationProductItemViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.viewholder.InspirationProductItemReimagineViewHolder

private val notFullSpanLayout = listOf(
    SmallGridProductItemViewHolder.LAYOUT,
    SmallGridProductItemViewHolder.LAYOUT_WITH_VIEW_STUB,
    RecommendationItemViewHolder.LAYOUT,
    SmallGridInspirationCardViewHolder.LAYOUT,
    InspirationKeywordViewHolder.LAYOUT,
    GridInspirationProductItemViewHolder.LAYOUT,
    GridInspirationProductItemViewHolder.LAYOUT_WITH_VIEW_STUB,
    GridProductItemViewHolder.LAYOUT,
    InspirationProductItemReimagineViewHolder.LAYOUT,
    InspirationKeywordReimagineViewHolder.LAYOUT,
    CouponGridViewHolder.LAYOUT,
)

fun isFullSpan(viewType: Int): Boolean = viewType !in notFullSpanLayout
