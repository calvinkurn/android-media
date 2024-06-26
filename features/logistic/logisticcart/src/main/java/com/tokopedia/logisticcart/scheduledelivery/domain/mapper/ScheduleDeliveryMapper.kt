package com.tokopedia.logisticcart.scheduledelivery.domain.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticcart.scheduledelivery.domain.entity.request.ScheduleDeliveryParam
import com.tokopedia.logisticcart.shipping.model.RatesParam
import javax.inject.Inject

class ScheduleDeliveryMapper @Inject constructor() {

    fun map(
        ratesParam: RatesParam,
        warehouseId: String,
        startDate: String = "",
        isRecommend: Boolean = true
    ): ScheduleDeliveryParam {
        return ScheduleDeliveryParam(
            origin = ratesParam.origin,
            destination = ratesParam.destination,
            spids = ratesParam.spids,
            warehouseId = warehouseId,
            catId = ratesParam.cat_id,
            products = ratesParam.products,
            boMetadata = ratesParam.bo_metadata,
            weight = ratesParam.weight,
            actualWeight = ratesParam.actualWeight ?: "",
            userHistory = ratesParam.user_history,
            uniqueId = ratesParam.unique_id,
            poTime = ratesParam.po_time,
            shopId = ratesParam.shop_id,
            isFulfillment = ratesParam.is_fulfillment,
            shopTier = ratesParam.shop_tier,
            orderValue = ratesParam.order_value.toLongOrZero(),
            cartData = ratesParam.cart_data,
            insurance = ratesParam.insurance.toLongOrZero(),
            productInsurance = ratesParam.product_insurance.toLongOrZero(),
            isRecommend = isRecommend,
            startDate = startDate,
            groupingState = ratesParam.grouping_state
        )
    }
}
