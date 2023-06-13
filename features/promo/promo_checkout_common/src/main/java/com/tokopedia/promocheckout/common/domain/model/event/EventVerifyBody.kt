package com.tokopedia.promocheckout.common.domain.model.event

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventVerifyBody(
        @SerializedName("cart_items")
        @Expose
        val cartItems: List<CartItemVerify> = arrayListOf(),
        @SerializedName("device_id")
        @Expose
        val deviceId: String = "",
        @SerializedName("promocode")
        @Expose
        var promocode: String = ""
) : Parcelable

@Parcelize
data class CartItemVerify(
        @SerializedName("configuration")
        @Expose
        val configuration: ConfigurationVerify = ConfigurationVerify(),
        @SerializedName("meta_data")
        @Expose
        val metaData: MetaDataVerify = MetaDataVerify(),
        @SuppressLint("Invalid Data Type")
        @SerializedName("product_id")
        @Expose
        val productId: Int = 0,
        @SerializedName("product_name")
        @Expose
        val productName: String = "",
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0
) : Parcelable

@Parcelize
data class MetaDataVerify(
        @SuppressLint("Invalid Data Type")
        @SerializedName("entity_category_id")
        @Expose
        val entityCategoryId: Int = 0,
        @SerializedName("entity_category_name")
        @Expose
        val entityCategoryName: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("entity_group_id")
        @Expose
        val entityGroupId: Int = 0,
        @SerializedName("entity_packages")
        @Expose
        val entityPackages: List<EntityPackageVerify> = arrayListOf(),
        @SerializedName("total_ticket_count")
        @Expose
        val totalTicketCount: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("entity_product_id")
        @Expose
        val entityProductId: Int = 0,
        @SerializedName("entity_product_name")
        @Expose
        val entityProductName: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("entity_provider_id")
        @Expose
        val entityProviderId: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("entity_schedule_id")
        @Expose
        val entityScheduleId: Int = 0,
        @SerializedName("entity_passengers")
        @Expose
        val entityPassengers: List<EntityPassengerVerify> = arrayListOf(),
        @SerializedName("entity_address")
        @Expose
        val entityAddress: EntityAddressVerify = EntityAddressVerify(),
        @SerializedName("city_searched")
        @Expose
        val citySearched: String = "",
        @SerializedName("entity_end_time")
        @Expose
        val entityEndTime: String = "",
        @SerializedName("entity_start_time")
        @Expose
        val entityStartTime: String = "",
        @SerializedName("tax_per_quantity")
        @Expose
        val taxPerQuantity: List<TaxPerQuantity> = arrayListOf(),
        @SerializedName("other_charges")
        @Expose
        val otherCharges: List<OtherCharge> = arrayListOf(),
        @SerializedName("tnc_approved")
        @Expose
        val tncApproved: Boolean = false,
        @SerializedName("total_other_charges")
        @Expose
        val totalOtherCharges: Int = 0,
        @SerializedName("total_tax_amount")
        @Expose
        val totalTaxAmount: Long = 0L,
        @SuppressLint("Invalid Data Type")
        @SerializedName("total_ticket_price")
        @Expose
        val totalTicketPrice: Int = 0,
        @SerializedName("entity_image")
        @Expose
        val entityImage: String = ""
) : Parcelable

@Parcelize
data class EntityPackageVerify(
        @SuppressLint("Invalid Data Type")
        @SerializedName("package_id")
        @Expose
        val packageId: Int = 0,
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0,
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("price_per_seat")
        @Expose
        val pricePerSeat: Int = 0,
        @SerializedName("session_id")
        @Expose
        val sessionID: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("product_id")
        @Expose
        val productId: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("group_id")
        @Expose
        val groupId: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("schedule_id")
        @Expose
        val scheduleId: Int = 0,
        @SerializedName("area_id")
        @Expose
        val areaID: String = ""
) : Parcelable

@Parcelize
data class EntityPassengerVerify(
        @SerializedName("element_type")
        @Expose
        val elementType: String = "",
        @SerializedName("error_message")
        @Expose
        val errorMessage: String = "",
        @SerializedName("help_text")
        @Expose
        val helpText: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("product_id")
        @Expose
        val productId: Int = 0,
        @SerializedName("required")
        @Expose
        val required: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("validator_regex")
        @Expose
        val validatorRegex: String = "",
        @SerializedName("value")
        @Expose
        val value: String = ""
) : Parcelable

@Parcelize
data class EntityAddressVerify(
        @SerializedName("address")
        @Expose
        val address: String = "",
        @SerializedName("city")
        @Expose
        val city: String = "",
        @SerializedName("email")
        @Expose
        val email: String = "",
        @SerializedName("latitude")
        @Expose
        val latitude: String = "",
        @SerializedName("longitude")
        @Expose
        val longitude: String = "",
        @SerializedName("mobile")
        @Expose
        val mobile: String = "",
        @SerializedName("Name")
        @Expose
        val name: String = ""
) : Parcelable

@Parcelize
data class ConfigurationVerify(
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("sub_config")
        @Expose
        val subConfig: SubConfigVerify = SubConfigVerify()
) : Parcelable

@Parcelize
data class SubConfigVerify(
        @SerializedName("name")
        @Expose
        val name: String = ""
) : Parcelable