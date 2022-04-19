package com.tokopedia.hotel.common.util

import com.tokopedia.hotel.booking.data.model.HotelCart
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView

object HotelMapper {
    fun mapToPromoData(hotelCart: HotelCart.Response): PromoData? {
        var promoData: PromoData? = null
        hotelCart.response.appliedVoucher.let {
            promoData = PromoData(title = it.titleDescription,
                    description = it.message,
                    promoCode = it.code,
                    typePromo = it.isCoupon,
                    amount = it.discountAmount,
                    state = TickerCheckoutView.State.ACTIVE)
        }
        return promoData
    }
}