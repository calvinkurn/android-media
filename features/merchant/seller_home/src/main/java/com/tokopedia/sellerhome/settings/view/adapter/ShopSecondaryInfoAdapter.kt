package com.tokopedia.sellerhome.settings.view.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.seller.menu.common.view.uimodel.base.PowerMerchantProStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.RmTransactionData
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.ShopOperationalData
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.*

class ShopSecondaryInfoAdapter(typeFactory: ShopSecondaryInfoAdapterTypeFactory,
                               val context: Context) :
    BaseListAdapter<Visitable<ShopSecondaryInfoAdapterFactory>, ShopSecondaryInfoAdapterTypeFactory>(typeFactory) {

    fun showInitialInfo() {
        clearAllElements()
        addElement(getDummyVisitableList())
    }

    private fun getDummyVisitableList(): List<Visitable<ShopSecondaryInfoAdapterFactory>> {
        return listOf(
            FreeShippingWidgetUiModel(SettingResponseState.SettingSuccess("https://images.tokopedia.net/img/restriction-engine/bebas-ongkir/BOE_Badge.png")),
            ReputationBadgeWidgetUiModel(SettingResponseState.SettingSuccess("https://ecs7.tokopedia.net/img/repsys/badges-off-hd.jpg")),
            RMTransactionWidgetUiModel(SettingResponseState.SettingSuccess(RmTransactionData(
                90, "10 agustus", true
            ))),
            ShopFollowersWidgetUiModel(SettingResponseState.SettingSuccess("100rb")),
            ShopOperationalWidgetUiModel(SettingResponseState.SettingSuccess(
                ShopOperationalData(false, true, IconUnify.SHOP, MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_RN500), "10.00 WIB")
            )),
            ShopStatusWidgetUiModel(SettingResponseState.SettingSuccess(PowerMerchantProStatus.Advanced))
        )
    }

}