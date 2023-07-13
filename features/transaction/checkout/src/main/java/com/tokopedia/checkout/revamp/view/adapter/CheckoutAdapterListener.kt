package com.tokopedia.checkout.revamp.view.adapter

import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel

interface CheckoutAdapterListener {

    fun onChangeAddress()

    fun onViewNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel)

    fun onClickApplyNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel)

    fun onClickCancelNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel)
}
