package com.tokopedia.centralizedpromoold.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.centralizedpromoold.view.model.OnGoingPromoUiModelOld
import com.tokopedia.centralizedpromoold.view.model.PromoCreationUiModelOld
import com.tokopedia.centralizedpromoold.view.viewholder.OnGoingPromoViewHolderOld
import com.tokopedia.centralizedpromoold.view.viewholder.PromoCreationViewHolderOld

class CentralizedPromoAdapterTypeFactoryOld(
    private val onFreeShippingImpression: () -> Unit,
    private val onFreeShippingClicked: () -> Unit,
    private val onProductCouponImpression: () -> Unit,
    private val onProductCouponClicked: () -> Unit,
    private val onProductCouponOngoingClicked: (String) -> Unit,
    private val onTokoMemberImpression: () -> Unit,
    private val onTokoMemberClicked: () -> Unit,
    private val onFlashSaleTokoClicked: (String) -> Unit
) : BaseAdapterTypeFactory() {
    fun type(onGoingPromoUiModel: OnGoingPromoUiModelOld): Int {
        return OnGoingPromoViewHolderOld.RES_LAYOUT
    }

    fun type(promoCreationUiModel: PromoCreationUiModelOld): Int {
        return PromoCreationViewHolderOld.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            PromoCreationViewHolderOld.RES_LAYOUT -> PromoCreationViewHolderOld(parent).apply {
                onFreeShippingImpression = this@CentralizedPromoAdapterTypeFactoryOld.onFreeShippingImpression
                onFreeShippingClicked = this@CentralizedPromoAdapterTypeFactoryOld.onFreeShippingClicked
                onProductCouponImpression = this@CentralizedPromoAdapterTypeFactoryOld.onProductCouponImpression
                onProductCouponClicked = this@CentralizedPromoAdapterTypeFactoryOld.onProductCouponClicked
                onTokoMemberImpression = this@CentralizedPromoAdapterTypeFactoryOld.onTokoMemberImpression
                onTokoMemberClicked = this@CentralizedPromoAdapterTypeFactoryOld.onTokoMemberClicked
                onFlashSaleTokoCLicked = this@CentralizedPromoAdapterTypeFactoryOld.onFlashSaleTokoClicked
            }
            OnGoingPromoViewHolderOld.RES_LAYOUT -> OnGoingPromoViewHolderOld(onProductCouponOngoingClicked, parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}