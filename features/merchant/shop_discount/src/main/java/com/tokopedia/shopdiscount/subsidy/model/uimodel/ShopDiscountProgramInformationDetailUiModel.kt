package com.tokopedia.shopdiscount.subsidy.model.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopDiscountProgramInformationDetailUiModel(
    val isVariant: Boolean = false,
    val formattedOriginalPrice: String = "",
    val formattedFinalDiscountedPrice: String = "",
    val formattedFinalDiscountedPercentage: String = "",
    val mainStock: String = "",
    val maxOrder: Int = 0,
    val productId: String = "",
    val isBottomSheet: Boolean = false,
    val isMultiWarehouse: Boolean = false,
    val subsidyInfo: ShopDiscountSubsidyInfoUiModel = ShopDiscountSubsidyInfoUiModel()
) : Parcelable
