package com.tokopedia.digital_product_detail.data.mapper

import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.digital_product_detail.data.model.data.perso.PersoFavNumberGroup
import com.tokopedia.digital_product_detail.di.DigitalPDPScope
import com.tokopedia.digital_product_detail.domain.model.AutoCompleteModel
import com.tokopedia.digital_product_detail.domain.model.FavoriteChipModel
import com.tokopedia.digital_product_detail.domain.model.FavoriteGroupModel
import com.tokopedia.digital_product_detail.domain.model.PrefillModel
import javax.inject.Inject

@DigitalPDPScope
class DigitalPersoMapper @Inject constructor() {

    fun mapDigiPersoFavoriteToModel(data: PersoFavNumberGroup): FavoriteGroupModel {
         return FavoriteGroupModel(
             prefill = mapDigiPersoPrefillToModel(data.favoriteNumberPrefill),
             autoCompletes = mapDigiPersoListToModel(data.favoriteNumberList),
             favoriteChips = mapDigiPersoChipsToModel(data.favoriteNumberChips)
         )
    }

    private fun mapDigiPersoPrefillToModel(data: TopupBillsPersoFavNumberData): PrefillModel {
        if (data.persoFavoriteNumber.items.isEmpty()) return PrefillModel()
        val persoPrefillData = data.persoFavoriteNumber.items.first()

        return PrefillModel(
            clientName = persoPrefillData.subtitle,
            clientNumber = persoPrefillData.title
        )
    }

    private fun mapDigiPersoListToModel(data: TopupBillsPersoFavNumberData): List<AutoCompleteModel> {
        return data.persoFavoriteNumber.items.map {

            val (clientName, clientNumber) = if (it.subtitle.isNotEmpty())
                it.title to it.subtitle
            else "" to it.title

            AutoCompleteModel(
                clientName = clientName,
                clientNumber = clientNumber
            )
        }
    }

    private fun mapDigiPersoChipsToModel(data: TopupBillsPersoFavNumberData): List<FavoriteChipModel> {
        return data.persoFavoriteNumber.items.map {

            val (clientName, clientNumber) = if (it.subtitle.isNotEmpty())
                it.title to it.subtitle
            else "" to it.title

            FavoriteChipModel(
                clientName = clientName,
                clientNumber = clientNumber,
                operatorId = it.trackingData.operatorId
            )
        }
    }
}