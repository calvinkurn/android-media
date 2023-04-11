package com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel

import java.util.*

/**
 * this model is used to get the result of product bulk apply bottomsheet after clicking apply button
 *
 * @param[startDate] to get the selected start date
 * @param[endDate] to get the selected end date
 * @param[discountType] to get the selected discount type, see [DiscountType]
 * @param[discountAmount] to get the inputted discount amount
 * @param[stock] to get the inputted stock
 *
 */
data class ProductBulkApplyResult(
    val startDate: Date? = null,
    val endDate: Date? = null,
    val discountType: DiscountType = DiscountType.RUPIAH,
    val discountAmount: Long = 0,
    val stock: Int = 0
)
