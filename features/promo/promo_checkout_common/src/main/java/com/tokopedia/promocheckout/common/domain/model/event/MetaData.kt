package com.tokopedia.promocheckout.common.domain.model.event

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MetaData(
        @SerializedName("city_searched")
        @Expose
        val citySearched: String = "",
        @SerializedName("end_date")
        @Expose
        val endDate: String = "",
        @SerializedName("entity_address")
        @Expose
        val entityAddress: EntityAddress = EntityAddress(),
        @SerializedName("entity_category_id")
        @Expose
        val entityCategoryId: String = "0",
        @SerializedName("entity_category_name")
        @Expose
        val entityCategoryName: String = "",
        @SerializedName("entity_end_time")
        @Expose
        val entityEndTime: String = "",
        @SerializedName("entity_group_id")
        @Expose
        val entityGroupId: String = "0",
        @SerializedName("entity_image")
        @Expose
        val entityImage: String = "",
        @SerializedName("entity_packages")
        @Expose
        val entityPackages: List<EntityPackage> = emptyList(),
        @SerializedName("entity_passengers")
        @Expose
        val entityPassengers: List<EntityPassenger> = emptyList(),
        @SerializedName("entity_product_id")
        @Expose
        val entityProductId: String = "0",
        @SerializedName("entity_product_name")
        @Expose
        val entityProductName: String = "",
        @SerializedName("entity_provider_id")
        @Expose
        val entityProviderId: String = "0",
        @SerializedName("entity_schedule_id")
        @Expose
        val entityScheduleId: String = "0",
        @SerializedName("entity_start_time")
        @Expose
        val entityStartTime: String = "",
        @SerializedName("integrator_sp")
        @Expose
        val integratorSp: Int = 0,
        @SerializedName("integrator_txn_id")
        @Expose
        val integratorTxnId: String = "0",
        @SerializedName("min_start_date")
        @Expose
        val minStartDate: String = "",
        @SerializedName("order_trace_id")
        @Expose
        val orderTraceId: String = "",
        @SerializedName("other_charges")
        @Expose
        val otherCharges: List<OtherCharge> = emptyList(),
        @SerializedName("seo_url")
        @Expose
        val seoUrl: String = "",
        @SerializedName("start_date")
        @Expose
        val startDate: String = "",
        @SerializedName("tax_per_quantity")
        @Expose
        val taxPerQuantity: List<TaxPerQuantity> = emptyList(),
        @SerializedName("tnc_approved")
        @Expose
        val tncApproved: Boolean = false,
        @SerializedName("total_other_charges")
        @Expose
        val totalOtherCharges: Int = 0,
        @SerializedName("total_tax_amount")
        @Expose
        val totalTaxAmount: Long = 0L,
        @SerializedName("total_ticket_count")
        @Expose
        val totalTicketCount: Int = 0,
        @SerializedName("total_ticket_price")
        @Expose
        val totalTicketPrice: Double = 0.0,
        @SerializedName("vertical_id")
        @Expose
        val verticalId: String = "0"
)