package com.tokopedia.flight.orderlist.domain.model

import com.tokopedia.flight.orderlist.data.cloud.entity.CancellationEntity
import com.tokopedia.flight.orderlist.data.cloud.entity.PaymentInfoEntity

class FlightOrder(
        val id: String,
        val status: Int,
        val statusString: String,
        val createTime: String,
        val email: String,
        val telp: String,
        val totalAdult: String,
        val totalAdultNumeric: Int,
        val totalChild: String,
        val totalChildNumeric: Int,
        val totalInfant: String,
        val totalInfantNumeric: Int,
        val invoiceUri: String,
        val eticketUri: String,
        val currency: String,
        val pdf: String,
        val journeys: List<FlightOrderJourney>,
        val passengerViewModels: List<FlightOrderPassengerViewModel>,
        val payment: PaymentInfoEntity,
        val cancellations: List<CancellationEntity>,
        val insurances: List<FlightInsurance>,
        val cancelledPassengerCount: Int,
        val contactUsUrl: String
)
