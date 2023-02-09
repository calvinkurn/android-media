package com.tokopedia.productcard_compact.productcardcarousel.presentation.adapter.typefactory

import com.tokopedia.abstraction.base.view.adapter.Visitable

interface TokoNowProductCardCarouselTypeFactory {
    fun type(visitable: Visitable<*>): Int
}
