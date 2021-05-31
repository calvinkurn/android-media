package com.tokopedia.flight.booking.data

import com.tokopedia.promocheckout.common.view.model.PromoData

/**
 * @author by jessica on 2019-11-01
 */

data class FlightPromoViewEntity (
        var isCouponEnable: Boolean = false,
        var isCouponActive: Int = 0,
        var promoData: PromoData = PromoData()
)

