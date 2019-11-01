package com.tokopedia.flight.bookingV3.data

import com.tokopedia.promocheckout.common.view.model.PromoData

/**
 * @author by jessica on 2019-11-01
 */

data class FlightVoucher (
        var isCouponActive: Boolean = false,
        var isCouponChanged: Boolean = false,
        var promoData: PromoData = PromoData()
)

