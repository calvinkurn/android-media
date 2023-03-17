package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.abstraction.base.view.adapter.Visitable

interface TokoNowProductCardCarouselTypeFactory {
    fun type(visitable: Visitable<*>): Int
}
