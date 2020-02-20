package com.tokopedia.reviewseller.feature.reviewlist.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class SellerReviewListAdapter(
        sellerReviewListTypeFactory: SellerReviewListTypeFactory
): BaseListAdapter<Visitable<*>,SellerReviewListTypeFactory>(sellerReviewListTypeFactory)