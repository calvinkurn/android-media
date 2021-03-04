package com.tokopedia.recharge_component.visitable

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.recharge_component.RechargeComponentTypeFactory

interface RechargeComponentVisitable: Visitable<RechargeComponentTypeFactory> {
    fun visitableId(): String?
    fun equalsWith(b: Any?): Boolean
    fun getChangePayloadFrom(b: Any?): Bundle?
}