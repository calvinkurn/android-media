package com.tokopedia.statistic.common

import android.content.Context
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.statistic.R
import com.tokopedia.statistic.view.model.ActionMenuUiModel
import com.tokopedia.statistic.view.model.StatisticPageUiModel

/**
 * Created By @ilhamsuaib on 16/09/20
 */

object Const {

    object StatisticPage {
        fun getShopStatistic(context: Context): StatisticPageUiModel {
            val title = context.getString(R.string.stc_shop)
            return StatisticPageUiModel(
                    pageTitle = title,
                    pageSource = PageSource.SHOP_INSIGHT,
                    tickerPageName = TickerPageName.SHOP_INSIGHT,
                    actionMenu = listOf(
                            ActionMenuUiModel(
                                    title = context.getString(R.string.stc_give_suggestions),
                                    appLink = "https://docs.google.com/forms/d/1t-KeapZJwOeYOBnbXDEmzRJiUqMBicE9cQIauc40qMU",
                                    iconUnify = IconUnify.INFORMATION
                            ),
                            ActionMenuUiModel(
                                    title = context.getString(R.string.stc_learn_more),
                                    appLink = "https://www.tokopedia.com/help/article/apa-itu-statistik-toko?source=sapp-wawasan-toko",
                                    iconUnify = IconUnify.INFORMATION
                            )
                    )
            )
        }

        fun getBuyerStatistic(context: Context): StatisticPageUiModel {
            val title = context.getString(R.string.stc_buyer)
            return StatisticPageUiModel(
                    pageTitle = title,
                    pageSource = PageSource.BUYER_INSIGHT,
                    tickerPageName = TickerPageName.BUYER_INSIGHT,
                    actionMenu = listOf(
                            ActionMenuUiModel(
                                    title = context.getString(R.string.stc_give_suggestions),
                                    appLink = "https://docs.google.com/forms/d/1g16aH6t8n6k-jMqOZpDK4QVgaxIXNodclNpwhS9KdkU/edit",
                                    iconUnify = IconUnify.INFORMATION
                            ),
                            ActionMenuUiModel(
                                    title = context.getString(R.string.stc_learn_more),
                                    appLink = "https://www.tokopedia.com/help/article/apa-itu-wawasan-pembeli?source=sapp-wawasan-pembeli",
                                    iconUnify = IconUnify.INFORMATION
                            )
                    )
            )
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