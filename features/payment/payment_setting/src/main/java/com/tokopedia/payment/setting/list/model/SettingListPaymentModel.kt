package com.tokopedia.payment.setting.list.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.payment.setting.list.view.adapter.SettingListPaymentAdapterTypeFactory

open class SettingListPaymentModel() : Visitable<SettingListPaymentAdapterTypeFactory>, Parcelable {
    override fun type(typeFactory: SettingListPaymentAdapterTypeFactory?): Int {
        return typeFactory?.type(this)?:0
    }

    @SerializedName("token_id")
    @Expose
    var tokenId: String? = null
    @SerializedName("masked_number")
    @Expose
    var maskedNumber: String? = null
    @SerializedName("expiry_month")
    @Expose
    var expiryMonth: String? = null
    @SerializedName("expiry_year")
    @Expose
    var expiryYear: String? = null
    @SerializedName("card_type")
    @Expose
    var cardType: String? = null
    @SerializedName("name_on_card")
    @Expose
    var nameOnCard: Any? = null
    @SerializedName("alias")
    @Expose
    var alias: String? = null
    @SerializedName("card_number")
    @Expose
    var cardNumber: Any? = null
    @SerializedName("is_debit_online")
    @Expose
    var isDebitOnline: Boolean = false
    @SerializedName("card_type_name")
    @Expose
    var cardTypeName: String? = null
    @SerializedName("type")
    @Expose
    var type: Int = 0
    @SerializedName("ccvault_code")
    @Expose
    var ccvaultCode: String? = null
    @SerializedName("bank")
    @Expose
    var bank: String? = null
    @SerializedName("is_expired")
    @Expose
    var isExpired: Boolean = false
    @SerializedName("card_cvv")
    @Expose
    var cardCvv: Any? = null
    @SerializedName("bank_image")
    @Expose
    var bankImage: String? = null
    @SerializedName("card_type_image")
    @Expose
    var cardTypeImage: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("small_background_image")
    @Expose
    var smallBackgroundImage: String? = null
    @SerializedName("background_image")
    @Expose
    var backgroundImage: String? = null
    @SerializedName("is_registered_fingerprint")
    @Expose
    var isRegisteredFingerprint: Boolean = false

    constructor(parcel: Parcel) : this() {
        tokenId = parcel.readString()
        maskedNumber = parcel.readString()
        expiryMonth = parcel.readString()
        expiryYear = parcel.readString()
        cardType = parcel.readString()
        alias = parcel.readString()
        isDebitOnline = parcel.readByte() != 0.toByte()
        cardTypeName = parcel.readString()
        type = parcel.readInt()
        ccvaultCode = parcel.readString()
        bank = parcel.readString()
        isExpired = parcel.readByte() != 0.toByte()
        bankImage = parcel.readString()
        cardTypeImage = parcel.readString()
        image = parcel.readString()
        smallBackgroundImage = parcel.readString()
        backgroundImage = parcel.readString()
        isRegisteredFingerprint = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tokenId)
        parcel.writeString(maskedNumber)
        parcel.writeString(expiryMonth)
        parcel.writeString(expiryYear)
        parcel.writeString(cardType)
        parcel.writeString(alias)
        parcel.writeByte(if (isDebitOnline) 1 else 0)
        parcel.writeString(cardTypeName)
        parcel.writeInt(type)
        parcel.writeString(ccvaultCode)
        parcel.writeString(bank)
        parcel.writeByte(if (isExpired) 1 else 0)
        parcel.writeString(bankImage)
        parcel.writeString(cardTypeImage)
        parcel.writeString(image)
        parcel.writeString(smallBackgroundImage)
        parcel.writeString(backgroundImage)
        parcel.writeByte(if (isRegisteredFingerprint) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SettingListPaymentModel> {
        override fun createFromParcel(parcel: Parcel): SettingListPaymentModel {
            return SettingListPaymentModel(parcel)
        }

        override fun newArray(size: Int): Array<SettingListPaymentModel?> {
            return arrayOfNulls(size)
        }
    }


}
