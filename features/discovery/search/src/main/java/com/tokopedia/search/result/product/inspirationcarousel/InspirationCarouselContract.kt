package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView

interface InspirationCarouselContract {
    interface View : CustomerView {
        val className: String
        fun redirectionStartActivity(applink: String?, url: String?)
        fun trackEventImpressionInspirationCarouselGridItem(product: InspirationCarouselDataView.Option.Product)
        fun trackEventImpressionInspirationCarouselListItem(product: InspirationCarouselDataView.Option.Product)
        fun trackEventImpressionInspirationCarouselChipsItem(product: InspirationCarouselDataView.Option.Product)
        fun trackEventClickInspirationCarouselGridItem(product: InspirationCarouselDataView.Option.Product)
        fun trackEventClickInspirationCarouselListItem(product: InspirationCarouselDataView.Option.Product)
        fun trackEventClickInspirationCarouselChipsItem(product: InspirationCarouselDataView.Option.Product)
    }
    interface Presenter: CustomerPresenter<View> {
        fun onInspirationCarouselProductImpressed(product: InspirationCarouselDataView.Option.Product)
        fun onInspirationCarouselProductClick(product: InspirationCarouselDataView.Option.Product)
    }
}