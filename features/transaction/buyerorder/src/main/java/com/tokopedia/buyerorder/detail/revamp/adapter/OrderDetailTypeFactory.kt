package com.tokopedia.buyerorder.detail.revamp.adapter

import com.tokopedia.buyerorder.detail.data.ItemsDeals
import com.tokopedia.buyerorder.detail.data.ItemsDealsOMP
import com.tokopedia.buyerorder.detail.data.ItemsDealsShort
import com.tokopedia.buyerorder.detail.data.ItemsDefault
import com.tokopedia.buyerorder.detail.data.ItemsEvents
import com.tokopedia.buyerorder.detail.data.ItemsInsurance

/**
 * created by @bayazidnasir on 23/8/2022
 */

interface OrderDetailTypeFactory {
    fun type(item: ItemsDealsShort): Int
    fun type(item: ItemsDealsOMP): Int
    fun type(item: ItemsDeals): Int
    fun type(item: ItemsEvents): Int
    fun type(item: ItemsInsurance): Int
    fun type(item: ItemsDefault): Int
}