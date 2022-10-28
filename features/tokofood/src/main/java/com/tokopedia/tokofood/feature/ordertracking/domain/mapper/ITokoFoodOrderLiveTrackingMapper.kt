package com.tokopedia.tokofood.feature.ordertracking.domain.mapper

import com.tokopedia.tokofood.feature.ordertracking.domain.model.TokoFoodOrderDetailResponse
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory

interface ITokoFoodOrderLiveTrackingMapper {
    fun mapToOrderDetailList(tokoFoodOrderDetail: TokoFoodOrderDetailResponse.TokofoodOrderDetail): List<BaseOrderTrackingTypeFactory>
}