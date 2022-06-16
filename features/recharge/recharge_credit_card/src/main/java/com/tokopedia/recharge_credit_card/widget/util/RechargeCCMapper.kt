package com.tokopedia.recharge_credit_card.widget.util

import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberAutoCompleteModel
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberChipModel

object RechargeCCMapper {
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