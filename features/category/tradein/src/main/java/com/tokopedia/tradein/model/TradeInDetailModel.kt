package com.tokopedia.tradein.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TradeInDetailModel(
    @SerializedName("getTradeInDetail")
    var getTradeInDetail: GetTradeInDetail
) {
    data class GetTradeInDetail(
        @SerializedName("ActivePromo")
        var activePromo: ActivePromo,
        @SerializedName("BannerURL")
        var bannerURL: String,
        @SerializedName("DeviceAttribute")
        var deviceAttribute: DeviceAttribute,
        @SerializedName("ErrCode")
        var errCode: Int,
        @SerializedName("ErrMessage")
        var errMessage: String,
        @SerializedName("ErrTitle")
        var errTitle: String,
        @SerializedName("IsFraud")
        var isFraud: Boolean,
        @SerializedName("LogisticMessage")
        var logisticMessage: String,
        @SerializedName("LogisticOptions")
        var logisticOptions: ArrayList<LogisticOption>,
        @SerializedName("OriginalPriceFmt")
        var originalPriceFmt: String
    ) {
        data class ActivePromo(
            @SerializedName("Code")
            var code: String,
            @SerializedName("Subtitle")
            var subtitle: String,
            @SerializedName("Title")
            var title: String
        )

        data class DeviceAttribute(
            @SerializedName("Brand")
            var brand: String,
            @SerializedName("Grade")
            var grade: String,
            @SerializedName("Imei")
            var imei: List<String>,
            @SerializedName("Model")
            var model: String,
            @SerializedName("ModelId")
            var modelId: Int,
            @SerializedName("Ram")
            var ram: String,
            @SerializedName("Storage")
            var storage: String
        )

        data class LogisticOption(
            @SerializedName("DiagnosticPriceFmt")
            var diagnosticPriceFmt: String,
            @SerializedName("DiagnosticPrice")
            var diagnosticPrice: Int,
            @SerializedName("DiagnosticReview")
            var diagnosticReview: ArrayList<DiagnosticReview>,
            @SerializedName("DiscountPercentageFmt")
            var discountPercentageFmt: String,
            @SerializedName("EstimatedPriceFmt")
            var estimatedPriceFmt: String,
            @SerializedName("ExpiryTime")
            var expiryTime: String,
            @SerializedName("FinalPriceFmt")
            var finalPriceFmt: String,
            @SerializedName("FinalPrice")
            var finalPrice: Int,
            @SerializedName("Is3PL")
            var is3PL: Boolean,
            @SerializedName("IsAvailable")
            var isAvailable: Boolean,
            @SerializedName("IsDiagnosed")
            var isDiagnosed: Boolean,
            @SerializedName("IsPreferred")
            var isPreferred: Boolean,
            @SerializedName("Subtitle")
            var subtitle: String,
            @SerializedName("Title")
            var title: String
        ) : Parcelable {
            constructor(parcel: Parcel) : this(
                parcel.readString() ?: "",
                diagnosticPrice = parcel.readInt(),
                parcel.createTypedArrayList(DiagnosticReview) ?: arrayListOf(),
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                finalPrice = parcel.readInt(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readString() ?: "",
                parcel.readString() ?: ""
            ) {
            }

            data class DiagnosticReview(
                @SerializedName("Field")
                var `field`: String,
                @SerializedName("Value")
                var value: String
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                    parcel.readString() ?: "",
                    parcel.readString() ?: ""
                ) {
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeString(field)
                    parcel.writeString(value)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<DiagnosticReview> {
                    override fun createFromParcel(parcel: Parcel): DiagnosticReview {
                        return DiagnosticReview(parcel)
                    }

                    override fun newArray(size: Int): Array<DiagnosticReview?> {
                        return arrayOfNulls(size)
                    }
                }
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(diagnosticPriceFmt)
                parcel.writeInt(diagnosticPrice)
                parcel.writeTypedList(diagnosticReview)
                parcel.writeString(discountPercentageFmt)
                parcel.writeString(estimatedPriceFmt)
                parcel.writeString(expiryTime)
                parcel.writeString(finalPriceFmt)
                parcel.writeInt(finalPrice)
                parcel.writeByte(if (is3PL) 1 else 0)
                parcel.writeByte(if (isAvailable) 1 else 0)
                parcel.writeByte(if (isDiagnosed) 1 else 0)
                parcel.writeByte(if (isPreferred) 1 else 0)
                parcel.writeString(subtitle)
                parcel.writeString(title)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<LogisticOption> {
                override fun createFromParcel(parcel: Parcel): LogisticOption {
                    return LogisticOption(parcel)
                }

                override fun newArray(size: Int): Array<LogisticOption?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}