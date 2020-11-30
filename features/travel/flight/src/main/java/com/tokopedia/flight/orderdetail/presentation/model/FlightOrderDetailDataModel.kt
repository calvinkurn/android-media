package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class FlightOrderDetailDataModel(
        val omsId: Int,
        val createTime: String,
        val status: Int,
        val statusString: String,
        val id: Int,
        val invoiceId: String,
        val contactName: String,
        val email: String,
        val phone: String,
        val countryId: String,
        val totalAdult: String,
        val totalAdultNumeric: Long,
        val totalChild: String,
        val totalChildNumeric: Long,
        val totalInfant: String,
        val totalInfantNumeric: Long,
        val totalPrice: String,
        val totalPriceNumeric: Long,
        val currency: String,
        val pdf: String,
        val isDomestic: Boolean,
        val mandatoryDob: Boolean,
        val classText: String,
        val contactUsURL: String,
        val hasETicket: Boolean,
        val payment: FlightOrderDetailPaymentModel,
        val journeys: List<FlightOrderDetailJourneyModel>,
        val passengers: List<FlightOrderDetailPassengerModel>,
        val actionButtons: List<FlightOrderDetailActionButtonModel>,
        val conditionalInfos: List<FlightOrderDetailConditionalInfoModel>,
        val insurances: List<FlightOrderDetailInsuranceModel>,
        val cancellations: List<FlightOrderDetailCancellationModel>
) : Parcelable