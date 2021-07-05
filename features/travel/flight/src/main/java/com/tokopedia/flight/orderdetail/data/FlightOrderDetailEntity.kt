package com.tokopedia.flight.orderdetail.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 14/10/2020
 */
class FlightOrderDetailEntity(
        @SerializedName("error")
        @Expose
        val errors: List<OrderDetailError> = arrayListOf(),
        @SerializedName("data")
        @Expose
        val data: OrderDetailData = OrderDetailData()
) {

    class Response(
            @SerializedName("flightGetOrderDetail")
            @Expose
            val flightGetOrderDetail: FlightOrderDetailEntity = FlightOrderDetailEntity()
    )

    class OrderDetailData(
            @SerializedName("omsID")
            @Expose
            val omsId: Int = 0,
            @SerializedName("createTime")
            @Expose
            val createTime: String = "",
            @SerializedName("status")
            @Expose
            val status: Int = 0,
            @SerializedName("statusString")
            @Expose
            val statusString: String = "",
            @SerializedName("flight")
            @Expose
            val flight: OrderDetailFlight = OrderDetailFlight()
    )

    class OrderDetailError(
            @SerializedName("message")
            @Expose
            val message: String = "",
            @SerializedName("path")
            @Expose
            val path: List<String> = arrayListOf(),
            @SerializedName("extensions")
            @Expose
            val extensions: Extensions = Extensions()
    ) {
        class Extensions(
                @SerializedName("developerMessage")
                @Expose
                val developerMessage: String = "",
                @SerializedName("timestamp")
                @Expose
                val timestamp: String = ""
        )
    }
}

class OrderDetailFlight(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("invoiceID")
        @Expose
        val invoiceId: String = "",
        @SerializedName("contactName")
        @Expose
        val contactName: String = "",
        @SerializedName("email")
        @Expose
        val email: String = "",
        @SerializedName("phone")
        @Expose
        val phone: String = "",
        @SerializedName("countryID")
        @Expose
        val countryId: String = "",
        @SerializedName("totalAdult")
        @Expose
        val totalAdult: String = "",
        @SerializedName("totalAdultNumeric")
        @Expose
        val totalAdultNumeric: Long = 0,
        @SerializedName("totalChild")
        @Expose
        val totalChild: String = "",
        @SerializedName("totalChildNumeric")
        @Expose
        val totalChildNumeric: Long = 0,
        @SerializedName("totalInfant")
        @Expose
        val totalInfant: String = "",
        @SerializedName("totalInfantNumeric")
        @Expose
        val totalInfantNumeric: Long = 0,
        @SerializedName("totalPrice")
        @Expose
        val totalPrice: String = "",
        @SerializedName("totalPriceNumeric")
        @Expose
        val totalPriceNumeric: Long = 0,
        @SerializedName("currency")
        @Expose
        val currency: String = "",
        @SerializedName("pdf")
        @Expose
        val pdf: String = "",
        @SerializedName("isDomestic")
        @Expose
        val isDomestic: Boolean = true,
        @SerializedName("mandatoryDOB")
        @Expose
        val mandatoryDob: Boolean = false,
        @SerializedName("classText")
        @Expose
        val classText: String = "",
        @SerializedName("hasEticket")
        @Expose
        val hasETicket: Boolean = false,
        @SerializedName("contactUsURL")
        @Expose
        val contactUsURL: String = "",
        @SerializedName("payment")
        @Expose
        val payment: OrderDetailPayment = OrderDetailPayment(),
        @SerializedName("journeys")
        @Expose
        val journeys: List<OrderDetailJourney> = arrayListOf(),
        @SerializedName("passengers")
        @Expose
        val passengers: List<OrderDetailPassenger> = arrayListOf(),
        @SerializedName("actionButtons")
        @Expose
        val actionButtons: List<OrderDetailActionButton> = arrayListOf(),
        @SerializedName("conditionalInfo")
        @Expose
        val conditionalInfo: List<OrderDetailConditionalInfo> = arrayListOf(),
        @SerializedName("insurances")
        @Expose
        val insurances: List<OrderDetailInsurance> = arrayListOf(),
        @SerializedName("cancellations")
        @Expose
        val cancellations: List<OrderDetailCancellation> = arrayListOf()
)

class OrderDetailPayment(
        @SerializedName("id")
        @Expose
        val id: Long = 0,
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("statusStr")
        @Expose
        val statusStr: String = "",
        @SerializedName("gatewayName")
        @Expose
        val gatewayName: String = "",
        @SerializedName("gatewayIcon")
        @Expose
        val gatewayIcon: String = "",
        @SerializedName("paymentDate")
        @Expose
        val paymentDate: String = "",
        @SerializedName("expireOn")
        @Expose
        val expireOn: String = "",
        @SerializedName("transactionCode")
        @Expose
        val transactionCode: String = "",
        @SerializedName("promoCode")
        @Expose
        val promoCode: String = "",
        @SerializedName("adminFeeAmt")
        @Expose
        val adminFeeAmount: Long = 0,
        @SerializedName("adminFeeAmtStr")
        @Expose
        val adminFeeAmountStr: String = "",
        @SerializedName("voucherAmt")
        @Expose
        val voucherAmount: Long = 0,
        @SerializedName("voucherAmtStr")
        @Expose
        val voucherAmountStr: String = "",
        @SerializedName("saldoAmt")
        @Expose
        val saldoAmount: Long = 0,
        @SerializedName("saldoAmtStr")
        @Expose
        val saldoAmountStr: String = "",
        @SerializedName("totalAmt")
        @Expose
        val totalAmount: Long = 0,
        @SerializedName("totalAmtString")
        @Expose
        val totalAmountString: String = "",
        @SerializedName("needToPayAmt")
        @Expose
        val needToPayAmount: Long = 0,
        @SerializedName("needToPayAmtStr")
        @Expose
        val needToPayAmountStr: String = "",
        @SerializedName("manualTransfer")
        @Expose
        val manualTransfer: OrderDetailManualTransfer = OrderDetailManualTransfer()
) {
    class OrderDetailManualTransfer(
            @SerializedName("uniqueCode")
            @Expose
            val uniqueCode: Long = 0,
            @SerializedName("accountBankName")
            @Expose
            val accountBankName: String = "",
            @SerializedName("accountBranch")
            @Expose
            val accountBranch: String = "",
            @SerializedName("accountNo")
            @Expose
            val accountNo: String = "",
            @SerializedName("accountName")
            @Expose
            val accountName: String = "",
            @SerializedName("total")
            @Expose
            val total: String = ""
    )
}

class OrderDetailJourney(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("departureID")
        @Expose
        val departureId: String = "",
        @SerializedName("departureTime")
        @Expose
        val departureTime: String = "",
        @SerializedName("departureAirportName")
        @Expose
        val departureAirportName: String = "",
        @SerializedName("departureCityName")
        @Expose
        val departureCityName: String = "",
        @SerializedName("arrivalID")
        @Expose
        val arrivalId: String = "",
        @SerializedName("arrivalTime")
        @Expose
        val arrivalTime: String = "",
        @SerializedName("arrivalAirportName")
        @Expose
        val arrivalAirportName: String = "",
        @SerializedName("arrivalCityName")
        @Expose
        val arrivalCityName: String = "",
        @SerializedName("totalTransit")
        @Expose
        val totalTransit: Int = 0,
        @SerializedName("totalStop")
        @Expose
        val totalStop: Int = 0,
        @SerializedName("addDayArrival")
        @Expose
        val addDayArrival: Int = 0,
        @SerializedName("duration")
        @Expose
        val duration: String = "",
        @SerializedName("durationMinute")
        @Expose
        val durationMinute: Int = 0,
        @SerializedName("fare")
        @Expose
        val fare: OrderDetailFare = OrderDetailFare(),
        @SerializedName("routes")
        @Expose
        val routes: List<OrderDetailRoute> = arrayListOf(),
        @SerializedName("webCheckIn")
        @Expose
        val webCheckIn: OrderDetailWebCheckIn = OrderDetailWebCheckIn()
) {
    class OrderDetailFare(
            @SerializedName("adultNumeric")
            @Expose
            val adultNumeric: Long = 0,
            @SerializedName("childNumeric")
            @Expose
            val childNumeric: Long = 0,
            @SerializedName("infantNumeric")
            @Expose
            val infantNumeric: Long = 0
    )

    class OrderDetailWebCheckIn(
            @SerializedName("title")
            @Expose
            val title: String = "",
            @SerializedName("subTitle")
            @Expose
            val subtitle: String = "",
            @SerializedName("startTime")
            @Expose
            val startTime: String = "",
            @SerializedName("endTime")
            @Expose
            val endTime: String = "",
            @SerializedName("iconURL")
            @Expose
            val iconUrl: String = "",
            @SerializedName("appURL")
            @Expose
            val appUrl: String = "",
            @SerializedName("webURL")
            @Expose
            val webUrl: String = ""
    )
}

class OrderDetailRoute(
        @SerializedName("departureID")
        @Expose
        val departureId: String = "",
        @SerializedName("departureTime")
        @Expose
        val departureTime: String = "",
        @SerializedName("departureAirportName")
        @Expose
        val departureAirportName: String = "",
        @SerializedName("departureCityName")
        @Expose
        val departureCityName: String = "",
        @SerializedName("arrivalID")
        @Expose
        val arrivalId: String = "",
        @SerializedName("arrivalTime")
        @Expose
        val arrivalTime: String = "",
        @SerializedName("arrivalAirportName")
        @Expose
        val arrivalAirportName: String = "",
        @SerializedName("arrivalCityName")
        @Expose
        val arrivalCityName: String = "",
        @SerializedName("pnr")
        @Expose
        val pnr: String = "",
        @SerializedName("airlineID")
        @Expose
        val airlineId: String = "",
        @SerializedName("airlineName")
        @Expose
        val airlineName: String = "",
        @SerializedName("airlineLogo")
        @Expose
        val airlineLogo: String = "",
        @SerializedName("operatorAirlineID")
        @Expose
        val operatorAirlineId: String = "",
        @SerializedName("flightNumber")
        @Expose
        val flightNumber: String = "",
        @SerializedName("duration")
        @Expose
        val duration: String = "",
        @SerializedName("durationMinute")
        @Expose
        val durationMinute: Int = 0,
        @SerializedName("layover")
        @Expose
        val layover: String = "",
        @SerializedName("layoverMinute")
        @Expose
        val layoverMinute: Int = 0,
        @SerializedName("refundable")
        @Expose
        val refundable: Boolean = false,
        @SerializedName("departureTerminal")
        @Expose
        val departureTerminal: String = "",
        @SerializedName("arrivalTerminal")
        @Expose
        val arrivalTerminal: String = "",
        @SerializedName("stop")
        @Expose
        val stop: Int = 0,
        @SerializedName("carrier")
        @Expose
        val carrier: String = "",
        @SerializedName("stopDetails")
        @Expose
        val stopDetails: List<String> = arrayListOf(),
        @SerializedName("ticketNumbers")
        @Expose
        val ticketNumbers: List<OrderTickerNumbers> = arrayListOf(),
        @SerializedName("freeAmenities")
        @Expose
        val freeAmenities: OrderDetailFreeAmenity = OrderDetailFreeAmenity()
) {
    class OrderTickerNumbers(
            @SerializedName("passengerID")
            @Expose
            val passengerId: Int = 0,
            @SerializedName("ticketNumber")
            @Expose
            val ticketNumber: String = ""
    )
}

class OrderDetailFreeAmenity(
        @SerializedName("cabinBaggage")
        @Expose
        val cabinBaggage: OrderDetailBaggage = OrderDetailBaggage(),
        @SerializedName("freeBaggage")
        @Expose
        val freeBaggage: OrderDetailBaggage = OrderDetailBaggage(),
        @SerializedName("meal")
        @Expose
        val meal: Boolean = false,
        @SerializedName("usbPort")
        @Expose
        val usbPort: Boolean = false,
        @SerializedName("wifi")
        @Expose
        val wifi: Boolean = false,
        @SerializedName("others")
        @Expose
        val others: List<String> = arrayListOf()
) {
    class OrderDetailBaggage(
            @SerializedName("isUpTo")
            @Expose
            val isUpTo: Boolean = false,
            @SerializedName("unit")
            @Expose
            val unit: String = "",
            @SerializedName("value")
            @Expose
            val value: Int = 0
    )
}

class OrderDetailPassenger(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("type")
        @Expose
        val type: Int = 0,
        @SerializedName("title")
        @Expose
        val title: Int = 1,
        @SerializedName("firstName")
        @Expose
        val firstName: String = "",
        @SerializedName("lastName")
        @Expose
        val lastName: String = "",
        @SerializedName("dob")
        @Expose
        val dob: String = "",
        @SerializedName("nationality")
        @Expose
        val nationality: String = "",
        @SerializedName("passportNo")
        @Expose
        val passportNo: String = "",
        @SerializedName("passportCountry")
        @Expose
        val passportCountry: String = "",
        @SerializedName("passportExpiry")
        @Expose
        val passportExpiry: String = "",
        @SerializedName("amenities")
        @Expose
        val amenities: List<OrderDetailAmenity> = arrayListOf(),
        @SerializedName("cancelStatus")
        @Expose
        val cancelStatus: List<OrderDetailPassengerCancelStatus> = arrayListOf()

) {
    class OrderDetailAmenity(
            @SerializedName("departureID")
            @Expose
            val departureId: String = "",
            @SerializedName("arrivalID")
            @Expose
            val arrivalId: String = "",
            @SerializedName("type")
            @Expose
            val type: Int = 0,
            @SerializedName("price")
            @Expose
            val price: String = "",
            @SerializedName("priceNumeric")
            @Expose
            val priceNumeric: Long = 0,
            @SerializedName("detail")
            @Expose
            val detail: String = ""
    )

    class OrderDetailPassengerCancelStatus(
            @SerializedName("status")
            @Expose
            val status: Int = 0,
            @SerializedName("statusStr")
            @Expose
            val statusStr: String = "",
            @SerializedName("statusType")
            @Expose
            val statusType: String = ""
    )
}

class OrderDetailActionButton(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("buttonType")
        @Expose
        val buttonType: String = "",
        @SerializedName("uri")
        @Expose
        val uri: String = "",
        @SerializedName("uriWeb")
        @Expose
        val uriWeb: String = "",
        @SerializedName("mappingURL")
        @Expose
        val mappingUrl: String = "",
        @SerializedName("method")
        @Expose
        val method: String = "",
        @SerializedName("weight")
        @Expose
        val weight: Int = 1
)

class OrderDetailConditionalInfo(
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("color")
        @Expose
        val color: OrderDetailConditionalInfoColor = OrderDetailConditionalInfoColor()
) {
    class OrderDetailConditionalInfoColor(
            @SerializedName("border")
            @Expose
            val border: String = "",
            @SerializedName("background")
            @Expose
            val background: String = ""
    )
}

class OrderDetailInsurance(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("tagline")
        @Expose
        val tagline: String = "",
        @SerializedName("paidAmount")
        @Expose
        val paidAmount: String = "",
        @SerializedName("paidAmountNumeric")
        @Expose
        val paidAmountNumeric: Long = 0
)

class OrderDetailCancellation(
        @SerializedName("cancelID")
        @Expose
        val cancelId: Int = 0,
        @SerializedName("cancelDetail")
        @Expose
        val cancelDetail: List<OrderDetailCancelDetail> = arrayListOf(),
        @SerializedName("createTime")
        @Expose
        val createTime: String = "",
        @SerializedName("updateTime")
        @Expose
        val updateTime: String = "",
        @SerializedName("estimatedRefund")
        @Expose
        val estimatedRefund: String = "",
        @SerializedName("estimatedRefundNumeric")
        @Expose
        val estimatedRefundNumeric: Long = 0,
        @SerializedName("realRefund")
        @Expose
        val realRefund: String = "",
        @SerializedName("realRefundNumeric")
        @Expose
        val realRefundNumeric: Long = 0,
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("statusStr")
        @Expose
        val statusStr: String = "",
        @SerializedName("statusType")
        @Expose
        val statusType: String = "",
        @SerializedName("refundInfo")
        @Expose
        val refundInfo: String = "",
        @SerializedName("refundDetail")
        @Expose
        val refundDetail: OrderDetailRefundDetail = OrderDetailRefundDetail()
) {
    class OrderDetailCancelDetail(
            @SerializedName("journeyID")
            @Expose
            val journeyId: Int = 0,
            @SerializedName("passengerID")
            @Expose
            val passengerId: Int = 0,
            @SerializedName("refundedGateway")
            @Expose
            val refundedGateway: String = "",
            @SerializedName("refundedTime")
            @Expose
            val refundedTime: String = ""
    )

    @Parcelize
    class OrderDetailRefundDetail(
            @SerializedName("topInfo")
            @Expose
            val topInfo: List<OrderDetailRefundKeyValue> = arrayListOf(),
            @SerializedName("middleInfo")
            @Expose
            val middleInfo: List<OrderDetailRefundTitleContent> = arrayListOf(),
            @SerializedName("bottomInfo")
            @Expose
            val bottomInfo: List<OrderDetailRefundKeyValue> = arrayListOf(),
            @SerializedName("notes")
            @Expose
            val notes: List<OrderDetailRefundKeyValue> = arrayListOf()
    ) : Parcelable

    @Parcelize
    class OrderDetailRefundKeyValue(
            @SerializedName("key")
            @Expose
            val key: String = "",
            @SerializedName("value")
            @Expose
            val value: String = ""
    ) : Parcelable

    @Parcelize
    class OrderDetailRefundTitleContent(
            @SerializedName("title")
            @Expose
            val title: String = "",
            @SerializedName("content")
            @Expose
            val content: List<OrderDetailRefundKeyValue> = arrayListOf()
    ) : Parcelable
}