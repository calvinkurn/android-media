package com.tokopedia.recharge_pdp_emoney.utils

import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumberData
import com.tokopedia.common_digital.common.presentation.model.DigitalDppoConsent
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.recharge_pdp_emoney.presentation.model.EmoneyBCAGenCheckModel
import com.tokopedia.recharge_pdp_emoney.presentation.model.EmoneyDppoConsentModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData

/**
 * @author by jessica on 01/04/21
 */
object EmoneyPdpMapper {
    private const val GEN_ONE = "1"
    fun mapTopUpBillsTickersToTickersData(tickers: List<TopupBillsTicker>): List<TickerData> {
        return tickers.map { item ->
            var description: String = item.content
            if (item.actionText.isNotEmpty() && item.actionLink.isNotEmpty()) {
                description += " [${item.actionText}]{${item.actionLink}}"
            }
            TickerData(item.name, description,
                    when (item.type) {
                        TopupBillsTicker.TYPE_WARNING -> Ticker.TYPE_WARNING
                        TopupBillsTicker.TYPE_INFO -> Ticker.TYPE_INFORMATION
                        TopupBillsTicker.TYPE_SUCCESS -> Ticker.TYPE_ANNOUNCEMENT
                        TopupBillsTicker.TYPE_ERROR -> Ticker.TYPE_ERROR
                        else -> Ticker.TYPE_INFORMATION
                    }
            )
        }
    }

    fun mapDppoConsentToEmoneyModel(data: DigitalDppoConsent): EmoneyDppoConsentModel {
        return EmoneyDppoConsentModel(
            description = data.persoData.items.getOrNull(Int.ZERO)?.title ?: ""
        )
    }

    fun mapDigiPersoToBCAGenCheck(data: TopupBillsPersoFavNumberData): EmoneyBCAGenCheckModel {
        val isGenOne = (data.persoFavoriteNumber.items?.first()?.label1 ?: "").equals(GEN_ONE)
        return EmoneyBCAGenCheckModel(
            isGenOne,
            data.persoFavoriteNumber.items?.first()?.label2 ?: "",
        )
    }
}
