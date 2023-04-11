package com.tokopedia.sellerhome.settings.view.adapter

import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.*

interface ShopSecondaryInfoAdapterFactory {
    fun type(uiModel: ShopOperationalWidgetUiModel): Int
    fun type(uiModel: ShopStatusWidgetUiModel): Int
    fun type(uiModel: RMTransactionWidgetUiModel): Int
    fun type(uiModel: ReputationBadgeWidgetUiModel): Int
    fun type(uiModel: TokoMemberWidgetUiModel): Int
    fun type(uiModel: ShopFollowersWidgetUiModel): Int
    fun type(uiModel: FreeShippingWidgetUiModel): Int
    fun type(uiModel: TokoPlusWidgetUiModel): Int
}
