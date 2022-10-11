package com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * this model is used to config the product bulk apply bottomsheet
 *
 * @param[bottomSheetTitle] set the title of bottomsheet
 * @param[isShowPeriodSection] to show or hide the period section
 * @param[isShowPeriodChips] to show or hide period chips
 * @param[defaultStartDate] to set the default start date
 * @param[defaultEndDate] to set the default end date
 * @param[isShowTextFieldProductDiscountBottomMessage] to show or hide text helper under the product discount amount textfield
 * @param[minimumDiscountPrice] to set the minimum discount price
 * @param[maximumDiscountPrice] to set the maximum discount price
 * @param[minimumDiscountPercentage] to set the minimum discount percentage
 * @param[maximumDiscountPercentage] to set the maximum discount percentage
 * @param[textStock] to set the value of stock text label
 * @param[textStockAdditionalInfo] to set the value of stock additional info label
 * @param[textStockDescription] to set the value of stock description label
 * @param[minimumStock] to set the minimum product stock
 * @param[maximumStock] to set the maximum product stock
 *
 */
@Parcelize
data class ProductBulkApplyUiModel(
    val bottomSheetTitle: String = "",
    val isShowPeriodSection: Boolean = false,
    val isShowPeriodChips: Boolean = false,
    val defaultStartDate: Date? = null,
    val defaultEndDate: Date? = null,
    val isShowTextFieldProductDiscountBottomMessage: Boolean = false,
    val minimumDiscountPrice: Int = 0,
    val maximumDiscountPrice: Int = 0,
    val minimumDiscountPercentage: Int = 0,
    val maximumDiscountPercentage: Int = 0,
    val textStock: String = "",
    val textStockAdditionalInfo: String = "",
    val textStockDescription: String = "",
    val minimumStock: Int = 0,
    val maximumStock: Int = 0,
) : Parcelable
