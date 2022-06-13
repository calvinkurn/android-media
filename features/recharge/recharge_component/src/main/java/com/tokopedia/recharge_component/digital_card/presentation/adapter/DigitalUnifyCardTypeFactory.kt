package com.tokopedia.recharge_component.digital_card.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyModel

interface DigitalUnifyCardTypeFactory : AdapterTypeFactory {
    fun type(digitalUnifyModel: DigitalUnifyModel): Int
}