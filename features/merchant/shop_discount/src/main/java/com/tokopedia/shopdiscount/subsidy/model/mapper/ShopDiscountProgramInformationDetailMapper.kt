package com.tokopedia.shopdiscount.subsidy.model.mapper

import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountProgramInformationDetailUiModel
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountSubsidyInfoUiModel
import javax.inject.Inject

object ShopDiscountProgramInformationDetailMapper {

    fun map(
        isVariant: Boolean,
        formattedOriginalPrice: String,
        formattedFinalDiscountedPercentage: String,
        formattedFinalDiscountedPrice: String,
        mainStock: String,
        maxOrder: Int,
        subsidyInfo: ShopDiscountSubsidyInfoUiModel
    ): ShopDiscountProgramInformationDetailUiModel {
        return ShopDiscountProgramInformationDetailUiModel(
            isVariant = isVariant,
            formattedOriginalPrice = formattedOriginalPrice,
            formattedFinalDiscountedPercentage = formattedFinalDiscountedPercentage,
            formattedFinalDiscountedPrice = formattedFinalDiscountedPrice,
            mainStock = mainStock,
            maxOrder = maxOrder,
            subsidyInfo = subsidyInfo
        )
    }
}
