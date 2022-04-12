package com.tokopedia.digital_product_detail.presentation.utils

import com.tokopedia.digital_product_detail.domain.model.AutoCompleteModel
import com.tokopedia.digital_product_detail.domain.model.FavoriteChipModel
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
}