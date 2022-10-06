package com.tokopedia.sellerhome.settings.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.settings.view.adapter.viewholder.*
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.*
import com.tokopedia.sellerhome.settings.view.viewholder.OtherMenuViewHolder

class ShopSecondaryInfoAdapterTypeFactory(private val listener: OtherMenuViewHolder.Listener) :
    BaseAdapterTypeFactory(),
    ShopSecondaryInfoAdapterFactory {

    override fun type(uiModel: ShopOperationalWidgetUiModel): Int =
        ShopOperationalViewHolder.LAYOUT_RES

    override fun type(uiModel: ShopStatusWidgetUiModel): Int = ShopStatusViewHolder.LAYOUT_RES

    override fun type(uiModel: RMTransactionWidgetUiModel): Int = RMTransactionViewHolder.LAYOUT_RES

    override fun type(uiModel: ReputationBadgeWidgetUiModel): Int =
        ReputationBadgeViewHolder.LAYOUT_RES

    override fun type(uiModel: TokoMemberWidgetUiModel): Int = ShopTokoMemberViewHolder.LAYOUT_RES

    override fun type(uiModel: ShopFollowersWidgetUiModel): Int = ShopFollowersViewHolder.LAYOUT_RES

    override fun type(uiModel: FreeShippingWidgetUiModel): Int = FreeShippingViewHolder.LAYOUT_RES

    override fun type(uiModel: TokoPlusWidgetUiModel): Int = TokoPlusViewHolder.LAYOUT_RES

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopOperationalViewHolder.LAYOUT_RES -> ShopOperationalViewHolder(
                parent,
                listener::onShopOperationalClicked,
                listener::onOperationalHourRefresh
            )
            ShopStatusViewHolder.LAYOUT_RES -> ShopStatusViewHolder(
                parent,
                listener::onGoToPowerMerchantSubscribe,
                listener::onRefreshShopInfo,
                listener::onShopStatusImpression
            )
            RMTransactionViewHolder.LAYOUT_RES -> RMTransactionViewHolder(
                parent,
                listener::onRmTransactionClicked,
                listener::onRefreshShopInfo
            )
            ReputationBadgeViewHolder.LAYOUT_RES -> ReputationBadgeViewHolder(
                parent,
                listener::onShopBadgeClicked,
                listener::onShopBadgeRefresh
            )
            ShopTokoMemberViewHolder.LAYOUT_RES -> ShopTokoMemberViewHolder(
                parent,
                listener::onTokoMemberCountClicked,
                listener::onTotalTokoMemberRefresh,
                listener::onImpressionTokoMember
            )
            ShopFollowersViewHolder.LAYOUT_RES -> ShopFollowersViewHolder(
                parent,
                listener::onFollowersCountClicked,
                listener::onShopTotalFollowersRefresh
            )
            FreeShippingViewHolder.LAYOUT_RES -> FreeShippingViewHolder(
                parent,
                listener::onFreeShippingClicked,
                listener::onFreeShippingRefresh,
                listener::onFreeShippingImpression
            )
            TokoPlusViewHolder.LAYOUT_RES -> TokoPlusViewHolder(
                parent,
                listener::onTokoPlusClicked,
                listener::onTokoPlusImpressed,
                listener::onFreeShippingRefresh
            )
            else -> super.createViewHolder(parent, type)
        }
    }
}
