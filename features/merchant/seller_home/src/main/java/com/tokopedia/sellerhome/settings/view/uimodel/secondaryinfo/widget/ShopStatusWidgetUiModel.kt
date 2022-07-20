package com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.sellerhome.settings.view.adapter.ShopSecondaryInfoAdapterFactory

class ShopStatusWidgetUiModel(override var state: SettingResponseState<ShopType>,
                              val userShopInfoUiModel: UserShopInfoWrapper.UserShopInfoUiModel? = null,
                              val impressHolder: ImpressHolder = ImpressHolder()
) : ShopSecondaryInfoWidget<ShopType> {

    override fun type(typeFactory: ShopSecondaryInfoAdapterFactory): Int =
        typeFactory.type(this)
}