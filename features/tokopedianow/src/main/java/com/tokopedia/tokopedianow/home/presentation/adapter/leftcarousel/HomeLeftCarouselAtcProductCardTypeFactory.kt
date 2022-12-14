package com.tokopedia.tokopedianow.home.presentation.adapter.leftcarousel

import com.tokopedia.abstraction.base.view.adapter.Visitable

interface HomeLeftCarouselAtcProductCardTypeFactory {
    fun type(visitable: Visitable<*>): Int
}
