package com.tokopedia.payment.setting.list.model

import com.tokopedia.payment.setting.list.view.adapter.SettingListPaymentAdapterTypeFactory

class SettingListAddCardModel : SettingListPaymentModel() {
    override fun type(typeFactory: SettingListPaymentAdapterTypeFactory?): Int {
        return typeFactory?.type(this)?:0
    }
}