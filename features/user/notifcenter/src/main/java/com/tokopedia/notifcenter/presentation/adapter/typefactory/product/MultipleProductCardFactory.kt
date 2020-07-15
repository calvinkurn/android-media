package com.tokopedia.notifcenter.presentation.adapter.typefactory.product

import com.tokopedia.notifcenter.data.viewbean.MultipleProductCardViewBean

interface MultipleProductCardFactory {
    fun type(multipleProduct: MultipleProductCardViewBean): Int
}