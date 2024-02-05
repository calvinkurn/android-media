package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.kotlin.extensions.view.percentFormatted
import java.util.Date
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
    @SerializedName("campaignLogo")
    @Expose
    val campaignLogo: String = "",
    @SerializedName("discountedPrice")
    val discountedPrice: Double = 0.0,
    @SerializedName("endDate")
    val endDate: String = "",
    @SerializedName("endDateUnix")
    val endDateUnix: String = "",
    @SerializedName("hideGimmick")
    val hideGimmick: Boolean = false,
    /**
     * isActive is false when identifier is thematic and no campaign(0)
     * otherwise when identifier 1,2,3,4,7
     */
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("isAppsOnly")
    val isAppsOnly: Boolean = false,
    @SerializedName("originalPrice")
    val originalPrice: Double = 0.0,
    @SerializedName("percentageAmount")
    val percentageAmount: Float = 0f,
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
    val paymentInfoWording: String = ""
) {

    companion object {
        private const val ONE_THOUSAND = 1000L
    }

    @Transient
    var priceFmt: String = ""

    @Transient
    var slashPriceFmt: String = ""

    @Transient
    var discPercentageFmt: String = ""

    private fun timeIsUnder1Day(): Boolean {
        return try {
            val endDateLong = endDateUnix.toLongOrNull() ?: 0
            val endDateMillis = endDateLong * ONE_THOUSAND
            val endDate = Date(endDateMillis)
            val now = System.currentTimeMillis()
            val diff = (endDate.time - now).toFloat()
            if (diff < 0) {
                // End date is out dated
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

    fun processMaskingPrice(price: Price) {
        discPercentageFmt = price.discPercentage.ifNullOrBlank {
            percentageAmount.percentFormatted()
        }
        slashPriceFmt = price.slashPriceFmt.ifNullOrBlank {
            discountedPrice.getCurrencyFormatted()
        }
        priceFmt = price.priceFmt.ifNullOrBlank {
            originalPrice.getCurrencyFormatted()
        }

        if (hideGimmick) {
            priceFmt = price.slashPriceFmt.ifNullOrBlank {
                discountedPrice.getCurrencyFormatted()
            }
        }
    }
}
