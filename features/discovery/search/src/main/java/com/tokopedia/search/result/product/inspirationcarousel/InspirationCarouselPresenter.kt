package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.domain.model.SearchCouponModel
import com.tokopedia.search.result.presentation.model.CouponDataView
import kotlinx.coroutines.flow.Flow

interface InspirationCarouselPresenter {
    fun onInspirationCarouselProductImpressed(product: InspirationCarouselDataView.Option.Product)

    fun onInspirationCarouselProductClick(
        product: InspirationCarouselDataView.Option.Product,
    )

    fun onInspirationCarouselChipsClick(
        adapterPosition: Int,
        inspirationCarouselViewModel: InspirationCarouselDataView,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
        searchParameter: Map<String, Any>
    )

    fun getInspirationCouponData(
        visitableList: MutableList<Visitable<*>>,
        dataView: CouponDataView,
        isDarkMode: Boolean,
    ): Flow<Unit>

    fun ctaCoupon(dataView: CouponDataView, item: SearchCouponModel.CouponListWidget)

    fun onCouponImpressed(couponDataView: CouponDataView)
}
