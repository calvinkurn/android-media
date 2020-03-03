package com.tokopedia.notifcenter.data.mapper

import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.viewbean.MultipleProductCardViewBean

object MultipleProductCardMapper {

    fun map(products: List<ProductData>): List<MultipleProductCardViewBean> {
        val multiProductCards = arrayListOf<MultipleProductCardViewBean>()
        products.forEach {
            val multiProductCardItem = MultipleProductCardViewBean()
            multiProductCardItem.product = it
            multiProductCards.add(multiProductCardItem)
        }
        return multiProductCards
    }

}