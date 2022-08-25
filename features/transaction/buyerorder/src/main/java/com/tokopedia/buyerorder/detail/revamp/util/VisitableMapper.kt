package com.tokopedia.buyerorder.detail.revamp.util

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.ItemsDeals
import com.tokopedia.buyerorder.detail.data.ItemsDealsOMP
import com.tokopedia.buyerorder.detail.data.ItemsDealsShort
import com.tokopedia.buyerorder.detail.data.ItemsDefault
import com.tokopedia.buyerorder.detail.data.ItemsEvents
import com.tokopedia.buyerorder.detail.data.ItemsInsurance
import com.tokopedia.buyerorder.detail.data.OrderDetails

/**
 * created by @bayazidnasir on 23/8/2022
 */

object VisitableMapper {

    const val CATEGORY_DEALS = "deals"
    const val DEALS_CATEGORY_ID = 35
    private const val CATEGORY_DEALS_OMP = "Food & Voucher"
    private const val UPSTREAM_KEY = "ORDERINTERNAL"
    private const val EVENTS_CATEGORY_ID_1 = 32
    private const val EVENTS_CATEGORY_ID_2 = 23
    private const val EVENTS_CATEGORY_INSURANCE = 1301

    fun mappingVisitable(list: List<Items>, isShortLayout: Boolean, upstream: String, orderDetails: OrderDetails): List<Visitable<*>>{
        return list.map {
           return@map when{
                it.category.equals(CATEGORY_DEALS, true)
                        || it.category.equals(CATEGORY_DEALS_OMP, true)
                        || it.categoryID == DEALS_CATEGORY_ID -> {
                    getDealsCategory(orderDetails, upstream, isShortLayout, it)
                }
                it.categoryID == EVENTS_CATEGORY_ID_1
                        || it.categoryID == EVENTS_CATEGORY_ID_2
                        || it.category.equals(OrderCategory.EVENT.category, true) -> {
                    ItemsEvents(orderDetails, it)
                }
                it.categoryID == EVENTS_CATEGORY_INSURANCE -> {
                    ItemsInsurance(orderDetails, it)
                }
                else -> ItemsDefault(orderDetails, it)
            }
        }
    }

    private fun getDealsCategory(
        orderDetails: OrderDetails,
        upstream: String,
        isShortLayout: Boolean,
        items: Items,
    ): Visitable<*>{
        return when {
            isShortLayout -> {
                ItemsDealsShort(items)
            }
            upstream.equals(UPSTREAM_KEY, true) -> {
                ItemsDealsOMP(orderDetails, items)
            }
            else -> {
                ItemsDeals(orderDetails, items)
            }
        }
    }
}