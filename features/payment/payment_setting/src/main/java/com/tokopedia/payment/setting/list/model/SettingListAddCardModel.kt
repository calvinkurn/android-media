package com.tokopedia.payment.setting.list.model

import com.tokopedia.payment.setting.list.SettingListPaymentAdapterTypeFactory

class SettingListAddCardModel : SettingListPaymentModel() {
    override fun type(typeFactory: SettingListPaymentAdapterTypeFactory?): Int {
        return typeFactory?.type(this)?:0
    }
}