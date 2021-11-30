package com.tokopedia.recharge_component.digital_card.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnificationModel

interface DigitalUnificationCardTypeFactory : AdapterTypeFactory {
    fun type(digitalUnificationModel: DigitalUnificationModel): Int
}