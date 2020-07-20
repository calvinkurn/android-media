package com.tokopedia.promocheckout.common.domain.model.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class EntityPackage(
        @SerializedName("actual_seat_nos")
        @Expose
        val actualSeatNos: List<Any> = emptyList(),
        @SerializedName("area_code")
        @Expose
        val areaCode: List<Any> = emptyList(),
        @SerializedName("address")
        @Expose
        val address: String = "",
        @SerializedName("base_price")
        @Expose
        val basePrice: Int = 0,
        @SerializedName("city")
        @Expose
        val city: String = "",
        @SerializedName("commision")
        @Expose
        val commision: Int = 0,
        @SerializedName("commision_type")
        @Expose
        val commisionType: String = "",
        @SerializedName("currency_price")
        @Expose
        val currencyPrice: Float = 0f,
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("dimension")
        @Expose
        val dimension: String = "",
        @SerializedName("display_name")
        @Expose
        val displayName: String = "",
        @SerializedName("error_message")
        @Expose
        val errorMessage: String = "",
        @SerializedName("group_id")
        @Expose
        val groupId: Int = 0,
        @SerializedName("group_name")
        @Expose
        val groupName: String = "",
        @SerializedName("invoice_status")
        @Expose
        val invoiceStatus: String = "",
        @SerializedName("package_id")
        @Expose
        val packageId: Int = 0,
        @SerializedName("package_price")
        @Expose
        val packagePrice: Int = 0,
        @SerializedName("payment_type")
        @Expose
        val paymentType: String = "",
        @SerializedName("price_per_seat")
        @Expose
        val pricePerSeat: Int = 0,
        @SerializedName("product_id")
        @Expose
        val productId: Int = 0,
        @SerializedName("provider_invoice_indentifier")
        @Expose
        val providerInvoiceIndentifier: String = "",
        @SerializedName("provider_schedule_id")
        @Expose
        val providerScheduleId: String = "",
        @SerializedName("provider_ticket_id")
        @Expose
        val providerTicketId: String = "",
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0,
        @SerializedName("schedule_date")
        @Expose
        val scheduleDate: Int = 0,
        @SerializedName("schedule_id")
        @Expose
        val scheduleId: Int = 0,
        @SerializedName("show_date")
        @Expose
        val showDate: String = "",
        @SerializedName("str_seatinfo")
        @Expose
        val strSeatinfo: String = "",
        @SerializedName("tkp_invoice_id")
        @Expose
        val tkpInvoiceId: Int = 0,
        @SerializedName("tkp_invoice_item_id")
        @Expose
        val tkpInvoiceItemId: Int = 0,
        @SerializedName("total_ticket_count")
        @Expose
        val totalTicketCount: Int = 0,
        @SerializedName("validity")
        @Expose
        val validity: String = "",
        @SerializedName("venue_detail")
        @Expose
        val venueDetail: String = "",
        @SerializedName("vouchers")
        @Expose
        val vouchers: Any = Any(),
        @SerializedName("vouchers_data")
        @Expose
        val vouchersData: Any = Any(),
        @SerializedName("seat_ids")
        @Expose
        val seatIds: List<Any> = emptyList(),
        @SerializedName("seat_row_ids")
        @Expose
        val seatRowIds: List<Any> = emptyList(),
        @SerializedName("seat_physical_row_ids")
        @Expose
        val seatPhysicalRowIds: List<Any> = emptyList(),
        @SerializedName("session_id")
        @Expose
        val sessionID: String = "",
        @SerializedName("area_id")
        @Expose
        val areaID: String = ""

)