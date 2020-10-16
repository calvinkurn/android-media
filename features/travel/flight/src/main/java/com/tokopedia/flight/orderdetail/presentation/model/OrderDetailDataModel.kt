package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class OrderDetailDataModel(
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
        val payment: OrderDetailPaymentModel,
        val journeys: List<OrderDetailJourneyModel>,
        val passengers: List<OrderDetailPassengerModel>,
        val actionButtons: List<OrderDetailActionButtonModel>,
        val conditionalInfos: List<OrderDetailConditionalInfoModel>,
        val insurances: List<OrderDetailInsuranceModel>,
        val cancellations: List<OrderDetailCancellationModel>
) : Parcelable