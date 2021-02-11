package com.tokopedia.statistic.common

import android.content.Context
import com.tokopedia.statistic.R
import com.tokopedia.statistic.view.model.StatisticPageUiModel

/**
 * Created By @ilhamsuaib on 16/09/20
 */

object Const {

    object StatisticPage {
        fun getShopStatistic(context: Context): StatisticPageUiModel {
            val title = context.getString(R.string.stc_shop)
            return StatisticPageUiModel(title, PageSource.SHOP_INSIGHT, TickerPageName.SHOP_INSIGHT)
        }

        fun getBuyerStatistic(context: Context): StatisticPageUiModel {
            val title = context.getString(R.string.stc_buyer)
            return StatisticPageUiModel(title, PageSource.BUYER_INSIGHT, TickerPageName.BUYER_INSIGHT)
        }
    }

    object PageSource {
        const val SHOP_INSIGHT = "shop-insight"
        const val BUYER_INSIGHT = "buyer-insight"
    }

    object TickerPageName {
        const val SHOP_INSIGHT = "seller-statistic"
        const val BUYER_INSIGHT = "wawasan-pembeli"
    }

    object BottomSheet {
        const val TAG_MONTH_PICKER = "MonthPickerBottomSheet"
    }
}