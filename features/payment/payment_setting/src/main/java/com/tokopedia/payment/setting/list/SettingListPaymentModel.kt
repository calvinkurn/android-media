package com.tokopedia.payment.setting.list

import com.tokopedia.abstraction.base.view.adapter.Visitable

class SettingListPaymentModel : Visitable<SettingListPaymentAdapterTypeFactory> {
    override fun type(typeFactory: SettingListPaymentAdapterTypeFactory?): Int {
        return typeFactory?.type(this)?:0
    }

}
