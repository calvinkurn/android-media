package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class MetaDataInfo(
    @SerializedName("end_date")
    @Expose
    val endDate: String = "",

    @SerializedName("end_time")
    @Expose
    val endTime: String = "",

    @SerializedName("entity_address")
    @Expose
    val entityAddress: EntityAddress = EntityAddress(),

    @SerializedName("entity_packages")
    @Expose
    val entityPackages: List<EntityPackage> = emptyList(),

    @SerializedName("entity_brand_name")
    @Expose
    val entityBrandName: String = "",

    @SerializedName("entity_image")
    @Expose
    val entityImage: String = "",

    @SerializedName("product_image")
    @Expose
    val productImage: String = "",

    @SerializedName("entity_product_id")
    @Expose
    val entityProductId: Int = 0,

    @SerializedName("entity_product_name")
    @Expose
    val entityProductName: String = "",

    @SerializedName("product_name")
    @Expose
    val productName: String = "",

    @SerializedName("start_date")
    @Expose
    val startDate: String = "",

    @SerializedName("start_time")
    @Expose
    val startTime: String = "",

    @SerializedName("total_ticket_count")
    @Expose
    val totalTicketCount: Int = 0,

    @SerializedName("quantity")
    @Expose
    val quantity: Int = 0,

    @SerializedName("total_ticket_price")
    @Expose
    val totalTicketPrice: Int = 0,

    @SerializedName("total_price")
    @Expose
    val totalPrice: Int = 0,

    @SerializedName("entity_passengers")
    @Expose
    val entityPessengers: List<EntityPessenger> = emptyList(),

    @SerializedName("passenger_forms")
    @Expose
    val passengerForms: List<PassengerForm> = emptyList(),

    @SerializedName("is_hiburan")
    @Expose
    var isHiburan: Int = 0,

    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("location_name")
    @Expose
    val locationName: String = "",

    @SerializedName("location_desc")
    @Expose
    val locationDesc: String = "",

    @SerializedName("seo_url")
    @Expose
    val seoUrl: String = "",

    @SerializedName("insurance_type")
    @Expose
    val insuranceType: String = "",

    @SerializedName("product_quantity_fmt")
    @Expose
    val productQuantity: String = "",

    @SerializedName("insurance_length_fmt")
    @Expose
    val insuranceLength: String = "",

    @SerializedName("premium_price_fmt")
    @Expose
    val premiumPrice: String = "",

    @SerializedName("product_image_url")
    @Expose
    val prouductImage: String = "",

    @SerializedName("product_price_fmt")
    @Expose
    val productPrice: String = "",

    @SerializedName("product_app_url")
    @Expose
    val productAppUrl: String = "",

    @SerializedName("custom_link_app_url")
    @Expose
    val customLinkAppUrl: String = "",

    @SerializedName("custom_link_label")
    @Expose
    val customLinkLabel: String = "",

    @SerializedName("custom_link_type")
    @Expose
    val customLinkType: String = "",
)