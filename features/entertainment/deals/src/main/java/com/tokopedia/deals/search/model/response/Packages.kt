package com.tokopedia.deals.search.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Packages (
        @SerializedName("id")
        @Expose
        var id: String = "",

        @SerializedName("product_id")
        @Expose
        var productId: String = "",

        @SerializedName("product_schedule_id")
        @Expose
        var productScheduleId: String = "",

        @SerializedName("product_group_id")
        @Expose
        var productGroupId: String = "",

        @SerializedName("provider_schedule_id")
        @Expose
        var providerScheduleId: String = "",

        @SerializedName("provider_ticket_id")
        @Expose
        var providerTicketId: String = "",

        @SerializedName("name")
        @Expose
        var name: String = "",

        @SerializedName("display_name")
        @Expose
        var displayName: String = "",

        @SerializedName("description")
        @Expose
        var description: String = "",

        @SerializedName("tnc")
        @Expose
        var tnc: String = "",

        @SerializedName("convenience_fee")
        @Expose
        var convenienceFee: String = "",

        @SerializedName("mrp")
        @Expose
        var mrp: String = "",

        @SerializedName("commission")
        @Expose
        var commission: String = "",

        @SerializedName("commission_type")
        @Expose
        var commissionType: String = "",

        @SerializedName("sales_price")
        @Expose
        var salesPrice: String = "",

        @SerializedName("price_code")
        @Expose
        var priceCode: String = "",

        @SerializedName("sold")
        @Expose
        var sold: String = "",

        @SerializedName("booked")
        @Expose
        var booked: String = "",

        @SerializedName("available")
        @Expose
        var available: String = "",

        @SerializedName("min_qty")
        @Expose
        var minQty: String = "",

        @SerializedName("max_qty")
        @Expose
        var maxQty: String = "",

        @SerializedName("status")
        @Expose
        var status: String = "",

        @SerializedName("color")
        @Expose
        var color: String = "",

        @SerializedName("icon")
        @Expose
        var icon: String = "",

        @SerializedName("provider_meta_data")
        @Expose
        var providerMetaData: String = "",

        @SerializedName("provider_status")
        @Expose
        var providerStatus: String = "",

        @SerializedName("venue_detail")
        @Expose
        var venueDetail: String = "",

        @SerializedName("venue_id")
        @Expose
        var venueId: String = "",

        @SerializedName("start_date")
        @Expose
        var startDate: String = "",

        @SerializedName("end_date")
        @Expose
        var endDate: String = "",

        @SerializedName("show_date")
        @Expose
        var showDate: String = "",

        @SerializedName("fetch_section_url")
        @Expose
        var fetchSectionUrl: String = "",

        @SerializedName("created_at")
        @Expose
        var createdAt: String = "",

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String = "",

        @SerializedName("schedule_status_english")
        @Expose
        var scheduleStatusEnglish: String = "",

        @SerializedName("schedule_status_bahasa")
        @Expose
        var scheduleStatusBahasa: String = "",

        @SerializedName("currency_id")
        @Expose
        var currencyId: String = "",

        @SerializedName("currency_price")
        @Expose
        var currencyPrice: String = ""
): Parcelable