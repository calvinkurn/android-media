package com.tokopedia.flight.orderlist.domain.model.mapper

import com.tokopedia.flight.orderlist.data.cloud.entity.FlightOrderInsuranceEntity
import com.tokopedia.flight.orderlist.domain.model.FlightInsurance
import javax.inject.Inject

class FlightInsuranceMapper @Inject
constructor() {

    fun transform(entities: List<FlightOrderInsuranceEntity>): List<FlightInsurance> {
        return entities.map {
            return@map FlightInsurance(
                    it.id,
                    it.paidAmount,
                    it.paidAmountNumeric,
                    it.title,
                    it.tagline)
        }
    }
}
