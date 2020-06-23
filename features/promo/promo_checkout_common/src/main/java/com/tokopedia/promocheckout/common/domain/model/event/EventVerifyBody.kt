package com.tokopedia.promocheckout.common.domain.model.event

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.createTypedArrayList(CartItemVerify),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeTypedList(cartItems)
                parcel.writeString(deviceId)
                parcel.writeString(promocode)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<EventVerifyBody> {
                override fun createFromParcel(parcel: Parcel): EventVerifyBody {
                        return EventVerifyBody(parcel)
                }

                override fun newArray(size: Int): Array<EventVerifyBody?> {
                        return arrayOfNulls(size)
                }
        }
}

data class CartItemVerify(
        @SerializedName("configuration")
        @Expose
        val configuration: ConfigurationVerify = ConfigurationVerify(),
        @SerializedName("meta_data")
        @Expose
        val metaData: MetaDataVerify = MetaDataVerify(),
        @SerializedName("product_id")
        @Expose
        val productId: Int = 0,
        @SerializedName("product_name")
        @Expose
        val productName: String = "",
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readParcelable(ConfigurationVerify::class.java.classLoader),
                parcel.readParcelable(MetaDataVerify::class.java.classLoader),
                parcel.readInt(),
                parcel.readString(),
                parcel.readInt()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeParcelable(configuration, flags)
                parcel.writeParcelable(metaData, flags)
                parcel.writeInt(productId)
                parcel.writeString(productName)
                parcel.writeInt(quantity)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<CartItemVerify> {
                override fun createFromParcel(parcel: Parcel): CartItemVerify {
                        return CartItemVerify(parcel)
                }

                override fun newArray(size: Int): Array<CartItemVerify?> {
                        return arrayOfNulls(size)
                }
        }
}

data class MetaDataVerify(
        @SerializedName("entity_category_id")
        @Expose
        val entityCategoryId: Int = 0,
        @SerializedName("entity_category_name")
        @Expose
        val entityCategoryName: String = "",
        @SerializedName("entity_group_id")
        @Expose
        val entityGroupId: Int = 0,
        @SerializedName("entity_packages")
        @Expose
        val entityPackages: List<EntityPackageVerify> = arrayListOf(),
        @SerializedName("total_ticket_count")
        @Expose
        val totalTicketCount: Int = 0,
        @SerializedName("entity_product_id")
        @Expose
        val entityProductId: Int = 0,
        @SerializedName("entity_product_name")
        @Expose
        val entityProductName: String = "",
        @SerializedName("entity_provider_id")
        @Expose
        val entityProviderId: Int = 0,
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
        val totalTaxAmount: Int = 0,
        @SerializedName("total_ticket_price")
        @Expose
        val totalTicketPrice: Int = 0,
        @SerializedName("entity_image")
        @Expose
        val entityImage: String = ""
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString(),
                parcel.readInt(),
                parcel.createTypedArrayList(EntityPackageVerify),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.createTypedArrayList(EntityPassengerVerify),
                parcel.readParcelable(EntityAddressVerify::class.java.classLoader),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.createTypedArrayList(TaxPerQuantity),
                parcel.createTypedArrayList(OtherCharge),
                parcel.readByte() != 0.toByte(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(entityCategoryId)
                parcel.writeString(entityCategoryName)
                parcel.writeInt(entityGroupId)
                parcel.writeTypedList(entityPackages)
                parcel.writeInt(totalTicketCount)
                parcel.writeInt(entityProductId)
                parcel.writeString(entityProductName)
                parcel.writeInt(entityProviderId)
                parcel.writeInt(entityScheduleId)
                parcel.writeTypedList(entityPassengers)
                parcel.writeParcelable(entityAddress, flags)
                parcel.writeString(citySearched)
                parcel.writeString(entityEndTime)
                parcel.writeString(entityStartTime)
                parcel.writeTypedList(taxPerQuantity)
                parcel.writeTypedList(otherCharges)
                parcel.writeByte(if (tncApproved) 1 else 0)
                parcel.writeInt(totalOtherCharges)
                parcel.writeInt(totalTaxAmount)
                parcel.writeInt(totalTicketPrice)
                parcel.writeString(entityImage)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<MetaDataVerify> {
                override fun createFromParcel(parcel: Parcel): MetaDataVerify {
                        return MetaDataVerify(parcel)
                }

                override fun newArray(size: Int): Array<MetaDataVerify?> {
                        return arrayOfNulls(size)
                }
        }
}

data class EntityPackageVerify(
        @SerializedName("package_id")
        @Expose
        val packageId: Int = 0,
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0,
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("price_per_seat")
        @Expose
        val pricePerSeat: Int = 0,
        @SerializedName("session_id")
        @Expose
        val sessionID: String = "",
        @SerializedName("product_id")
        @Expose
        val productId: Int = 0,
        @SerializedName("group_id")
        @Expose
        val groupId: Int = 0,
        @SerializedName("schedule_id")
        @Expose
        val scheduleId: Int = 0,
        @SerializedName("area_id")
        @Expose
        val areaID: String = ""
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(packageId)
                parcel.writeInt(quantity)
                parcel.writeString(description)
                parcel.writeInt(pricePerSeat)
                parcel.writeString(sessionID)
                parcel.writeInt(productId)
                parcel.writeInt(groupId)
                parcel.writeInt(scheduleId)
                parcel.writeString(areaID)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<EntityPackageVerify> {
                override fun createFromParcel(parcel: Parcel): EntityPackageVerify {
                        return EntityPackageVerify(parcel)
                }

                override fun newArray(size: Int): Array<EntityPackageVerify?> {
                        return arrayOfNulls(size)
                }
        }
}

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
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = "",
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
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(elementType)
                parcel.writeString(errorMessage)
                parcel.writeString(helpText)
                parcel.writeInt(id)
                parcel.writeString(name)
                parcel.writeInt(productId)
                parcel.writeString(required)
                parcel.writeString(title)
                parcel.writeString(validatorRegex)
                parcel.writeString(value)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<EntityPassengerVerify> {
                override fun createFromParcel(parcel: Parcel): EntityPassengerVerify {
                        return EntityPassengerVerify(parcel)
                }

                override fun newArray(size: Int): Array<EntityPassengerVerify?> {
                        return arrayOfNulls(size)
                }
        }
}

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
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(address)
                parcel.writeString(city)
                parcel.writeString(email)
                parcel.writeString(latitude)
                parcel.writeString(longitude)
                parcel.writeString(mobile)
                parcel.writeString(name)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<EntityAddressVerify> {
                override fun createFromParcel(parcel: Parcel): EntityAddressVerify {
                        return EntityAddressVerify(parcel)
                }

                override fun newArray(size: Int): Array<EntityAddressVerify?> {
                        return arrayOfNulls(size)
                }
        }
}

data class ConfigurationVerify(
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("sub_config")
        @Expose
        val subConfig: SubConfigVerify = SubConfigVerify()
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readParcelable(SubConfigVerify::class.java.classLoader)) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(price)
                parcel.writeParcelable(subConfig, flags)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<ConfigurationVerify> {
                override fun createFromParcel(parcel: Parcel): ConfigurationVerify {
                        return ConfigurationVerify(parcel)
                }

                override fun newArray(size: Int): Array<ConfigurationVerify?> {
                        return arrayOfNulls(size)
                }
        }
}

data class SubConfigVerify(
        @SerializedName("name")
        @Expose
        val name: String = ""
) : Parcelable {
        constructor(parcel: Parcel) : this(parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(name)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<SubConfigVerify> {
                override fun createFromParcel(parcel: Parcel): SubConfigVerify {
                        return SubConfigVerify(parcel)
                }

                override fun newArray(size: Int): Array<SubConfigVerify?> {
                        return arrayOfNulls(size)
                }
        }
}