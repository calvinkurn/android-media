package com.tokopedia.rechargegeneral.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.rechargegeneral.presentation.adapter.RechargeGeneralAdapterFactory

/**
 * Created by resakemal on 26/11/19.
 */
class RechargeGeneralProductItemData(
        var selectedProductId: String = "")

    : RechargeGeneralProductInput(), Visitable<RechargeGeneralAdapterFactory> {
    override fun type(typeFactory: RechargeGeneralAdapterFactory) = typeFactory.type(this)
}