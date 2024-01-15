package com.tokopedia.shopdiscount.subsidy.model.uimodel

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopDiscountSubsidyInfoUiModel(
    val ctaProgramLink: String = "",
    val subsidyType: SubsidyType = SubsidyType.CHIP_IN,
    val discountedPrice: Double = 0.0,
    val discountedPercentage : Int = 0,
    val remainingQuota : Int = 0,
    val quotaSubsidy: Int = 0,
    val maxOrder: Int = 0,
    val subsidyDateStart: String = "",
    val subsidyDateEnd: String = "",
    val sellerDiscountPrice: Double = 0.0,
    val sellerDiscountPercentage: Int = 0,
) : Parcelable
{
    companion object{
        fun getSubsidyType(value: Int) : SubsidyType {
            return when(value) {
                Int.ZERO -> SubsidyType.CHIP_IN
                Int.ONE -> SubsidyType.FULL
                else -> SubsidyType.CHIP_IN
            }
        }
    }
    enum class SubsidyType(val value: Int) {
        CHIP_IN(0),
        FULL(1)
    }
}
