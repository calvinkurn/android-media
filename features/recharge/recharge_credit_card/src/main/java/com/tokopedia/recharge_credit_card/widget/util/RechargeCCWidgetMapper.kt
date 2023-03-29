package com.tokopedia.recharge_credit_card.widget.util

import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberAutoCompleteModel
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberChipModel
import com.tokopedia.recharge_credit_card.toEditable
import com.tokopedia.recharge_credit_card.util.RechargeCCConst.DIVIDER
import com.tokopedia.recharge_credit_card.util.RechargeCCConst.TOTAL_DIGITS
import com.tokopedia.recharge_credit_card.util.RechargeCCUtil
import com.tokopedia.recharge_credit_card.widget.RechargeCCClientNumberWidget

object RechargeCCWidgetMapper {
    fun mapFavoriteChipsToWidgetModels(favoriteChipModels: List<FavoriteChipModel>): List<RechargeClientNumberChipModel> {
        return favoriteChipModels.map { chip ->
            val formattedClientNumber = RechargeCCUtil.concatStringWith16D(
                RechargeCCUtil.getDigitArray(chip.clientNumber.toEditable(),
                    TOTAL_DIGITS
                ), DIVIDER
            )
            RechargeClientNumberChipModel(
                clientName = chip.clientName,
                clientNumber = formattedClientNumber,
                operatorId = chip.operatorId,
                token = chip.token,
            )
        }
    }

    fun mapAutoCompletesToWidgetModels(autoCompleteModels: List<AutoCompleteModel>): List<RechargeClientNumberAutoCompleteModel> {
        return autoCompleteModels.map { autoComplete ->
            val formattedClientNumber = RechargeCCUtil.concatStringWith16D(
                RechargeCCUtil.getDigitArray(autoComplete.clientNumber.toEditable(),
                    TOTAL_DIGITS
                ), DIVIDER
            )
            RechargeClientNumberAutoCompleteModel(
                clientName = autoComplete.clientName,
                clientNumber = formattedClientNumber,
                token = autoComplete.token,
            )
        }
    }
}
