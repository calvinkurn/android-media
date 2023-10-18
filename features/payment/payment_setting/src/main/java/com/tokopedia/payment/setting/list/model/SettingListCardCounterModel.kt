package com.tokopedia.payment.setting.list.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.payment.setting.list.view.adapter.SettingListPaymentAdapterTypeFactory

data class SettingListCardCounterModel(
    val size: Int
) : SettingListPaymentModel() {

    override fun type(typeFactory: SettingListPaymentAdapterTypeFactory?): Int {
        return typeFactory?.type(this)?:0
    }
}
