package com.tokopedia.topupbills.postpaid

import com.tokopedia.topupbills.di.DigitalTelcoInstanceStub
import com.tokopedia.topupbills.telco.common.di.DigitalTelcoComponent
import com.tokopedia.topupbills.telco.postpaid.activity.TelcoPostpaidActivity

class TelcoPostpaidActivityStub: TelcoPostpaidActivity() {

    override fun getComponent(): DigitalTelcoComponent {
        return DigitalTelcoInstanceStub.getComponent(application)
    }
}
