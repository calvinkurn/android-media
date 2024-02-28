package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.domain.model.SearchCouponModel
import com.tokopedia.search.result.presentation.model.CouponDataView

interface InspirationCarouselPresenter {
    fun onInspirationCarouselProductImpressed(product: InspirationCarouselDataView.Option.Product)

    fun onInspirationCarouselProductClick(product: InspirationCarouselDataView.Option.Product)

    fun onInspirationCarouselChipsClick(
        adapterPosition: Int,
        inspirationCarouselViewModel: InspirationCarouselDataView,
        clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
        searchParameter: Map<String, Any>
    )

    suspend fun getInspirationCouponData(
        visitableList: MutableList<Visitable<*>>,
        dataView: CouponDataView
    ): List<Visitable<*>>

    fun ctaCoupon(dataView: CouponDataView, item: SearchCouponModel.CouponListWidget)

    fun onCouponImpressed(couponDataView: CouponDataView)
}
