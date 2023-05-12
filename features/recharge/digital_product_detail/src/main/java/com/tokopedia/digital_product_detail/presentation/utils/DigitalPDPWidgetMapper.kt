package com.tokopedia.digital_product_detail.presentation.utils

import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.digital_product_detail.domain.model.DigitalCheckBalanceOTPModel
import com.tokopedia.recharge_component.model.check_balance.RechargeCheckBalanceOTPBottomSheetModel
import com.tokopedia.recharge_component.model.check_balance.RechargeCheckBalanceOTPModel
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberAutoCompleteModel
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberChipModel

object DigitalPDPWidgetMapper {
    fun mapFavoriteChipsToWidgetModels(favoriteChipModels: List<FavoriteChipModel>): List<RechargeClientNumberChipModel> {
        return favoriteChipModels.map { chip ->
            RechargeClientNumberChipModel(
                clientName = chip.clientName,
                clientNumber = chip.clientNumber,
                operatorId = chip.operatorId
            )
        }
    }

    fun mapAutoCompletesToWidgetModels(autoCompleteModels: List<AutoCompleteModel>): List<RechargeClientNumberAutoCompleteModel> {
        return autoCompleteModels.map { autoComplete ->
            RechargeClientNumberAutoCompleteModel(
                clientName = autoComplete.clientName,
                clientNumber = autoComplete.clientNumber
            )
        }
    }

    fun mapCheckBalanceOTPToWidgetModels(checkBalanceOTPModel: DigitalCheckBalanceOTPModel): RechargeCheckBalanceOTPModel {
        return RechargeCheckBalanceOTPModel(
            subtitle = checkBalanceOTPModel.subtitle,
            label = checkBalanceOTPModel.label,
            bottomSheetModel = RechargeCheckBalanceOTPBottomSheetModel(
                title = checkBalanceOTPModel.bottomSheetModel.title,
                mediaUrl = checkBalanceOTPModel.bottomSheetModel.mediaUrl,
                description = checkBalanceOTPModel.bottomSheetModel.description,
                buttonText = checkBalanceOTPModel.bottomSheetModel.buttonText,
                buttonAppLink = checkBalanceOTPModel.bottomSheetModel.buttonAppLink
            )
        )
    }
}
