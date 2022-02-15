package com.tokopedia.tradein.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TradeInDetailModel(
    @SerializedName("getTradeInDetailData")
    var getTradeInDetailData: GetTradeInDetailData
) {
    data class GetTradeInDetailData(
        @SerializedName("activePromo")
        var activePromo: ActivePromo,
        @SerializedName("bannerUrl")
        var bannerUrl: String,
        @SerializedName("deviceAttribute")
        var deviceAttribute: DeviceAttribute,
        @SerializedName("logisticMessage")
        var logisticMessage: String,
        @SerializedName("logisticOptions")
        var logisticOptions: ArrayList<LogisticOption>,
        @SerializedName("productPriceFmt")
        var productPriceFmt: String
    ) {
        data class ActivePromo(
            @SerializedName("code")
            var code: String,
            @SerializedName("subTitle")
            var subTitle: String,
            @SerializedName("title")
            var title: String
        )

        data class DeviceAttribute(
            @SerializedName("brand")
            var brand: String,
            @SerializedName("grade")
            var grade: String,
            @SerializedName("imei")
            var imei: List<String>,
            @SerializedName("model")
            var model: String,
            @SerializedName("model_id")
            var modelId: String,
            @SerializedName("ram")
            var ram: String,
            @SerializedName("storage")
            var storage: String
        )

        data class LogisticOption(
            @SerializedName("diagnosticPriceFmt")
            var diagnosticPriceFmt: String,
            @SerializedName("diagnosticReview")
            var diagnosticReview: String,
            @SerializedName("discountPercentageFmt")
            var discountPercentageFmt: String,
            @SerializedName("estimationPriceFmt")
            var estimationPriceFmt: String,
            @SerializedName("expiryTime")
            var expiryTime: String,
            @SerializedName("finalPriceFmt")
            var finalPriceFmt: String,
            @SerializedName("is3PL")
            var is3PL: Boolean,
            @SerializedName("isAvailable")
            var isAvailable: Boolean,
            @SerializedName("isDiagnosed")
            var isDiagnosed: Boolean,
            @SerializedName("isPrefered")
            var isPrefered: Boolean,
            @SerializedName("subTitle")
            var subTitle: String,
            @SerializedName("title")
            var title: String
        ) : Parcelable {
            constructor(parcel: Parcel) : this(
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readString() ?: "",
                parcel.readString() ?: ""
            ) {
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(diagnosticPriceFmt)
                parcel.writeString(diagnosticReview)
                parcel.writeString(discountPercentageFmt)
                parcel.writeString(estimationPriceFmt)
                parcel.writeString(expiryTime)
                parcel.writeString(finalPriceFmt)
                parcel.writeByte(if (is3PL) 1 else 0)
                parcel.writeByte(if (isAvailable) 1 else 0)
                parcel.writeByte(if (isDiagnosed) 1 else 0)
                parcel.writeByte(if (isPrefered) 1 else 0)
                parcel.writeString(subTitle)
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