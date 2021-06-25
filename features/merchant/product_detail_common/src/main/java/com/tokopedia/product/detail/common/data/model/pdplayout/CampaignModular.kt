package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

data class CampaignModular(
        @SerializedName("appLinks")
        val appLinks: String = "",
        @SerializedName("campaignID")
        val campaignID: String = "",
        @SerializedName("campaignType")
        val campaignType: String = "",
        @SerializedName("campaignTypeName")
        val campaignTypeName: String = "",
        @SerializedName("discountedPrice")
        val discountedPrice: Int = 0,
        @SerializedName("endDate")
        val endDate: String = "",
        @SerializedName("endDateUnix")
        val endDateUnix: String = "",
        @SerializedName("hideGimmick")
        val hideGimmick: Boolean = false,
        @SerializedName("isActive")
        val isActive: Boolean = false,
        @SerializedName("isAppsOnly")
        val isAppsOnly: Boolean = false,
        @SerializedName("originalPrice")
        val originalPrice: Int = 0,
        @SerializedName("percentageAmount")
        val percentageAmount: Int = 0,
        @SerializedName("startDate")
        val startDate: String = "",
        @SerializedName("stock")
        val stock: Int = 0,
        @SerializedName("stockSoldPercentage")
        val stockSoldPercentage: Int = 0,
        @SerializedName("isCheckImei")
        val isCheckImei: Boolean = false,
        @SerializedName("isUsingOvo")
        val isUsingOvo: Boolean = false,
        @SerializedName("campaignIdentifier")
        val campaignIdentifier: Int = 0,
        @SerializedName("background")
        val background: String = "",
        @SerializedName("paymentInfoWording")
        val paymentInfoWording:String = ""
) {
    var discountedPriceFmt: String = ""
    var originalPriceFmt:String = ""

    private fun timeIsUnder1Day(): Boolean {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val endDate = dateFormat.parse(endDate)
            val now = System.currentTimeMillis()
            val diff = (endDate?.time ?: 0 - now).toFloat()
            if (diff < 0) {
                //End date is out dated
                false
            } else {
                TimeUnit.MILLISECONDS.toDays(endDate.time - now) < 1
            }
        } catch (e: Throwable) {
            false
        }
    }

    /**
     * Campaign For New User, always show even when timer > 24H but remove count down timer
     * when timer < 24H show count down timer
     */
    val shouldShowRibbonCampaign
        get() = isActive && timeIsUnder1Day()

    val activeAndHasId
        get() = isActive && (campaignID.toIntOrNull() ?: 0) > 0

}