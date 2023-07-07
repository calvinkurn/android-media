package com.tokopedia.tokofood.stub.postpurchase.domain.mapper

import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.ITokoFoodOrderCompletedMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.ITokoFoodOrderLiveTrackingMapper
import com.tokopedia.tokofood.feature.ordertracking.domain.mapper.TokoFoodOrderDetailMapper

class TokoFoodOrderDetailMapperStub(
    orderLiveTrackingMapper: ITokoFoodOrderLiveTrackingMapper,
    orderCompletedMapper: ITokoFoodOrderCompletedMapper
) : TokoFoodOrderDetailMapper(orderLiveTrackingMapper, orderCompletedMapper)
