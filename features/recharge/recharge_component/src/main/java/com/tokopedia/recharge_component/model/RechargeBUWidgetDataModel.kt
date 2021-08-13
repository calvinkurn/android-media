package com.tokopedia.recharge_component.model

import android.os.Bundle
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recharge_component.RechargeComponentTypeFactory
import com.tokopedia.recharge_component.visitable.RechargeComponentVisitable

data class RechargeBUWidgetDataModel(
        val data: RechargePerso = RechargePerso(),
        val channel: ChannelModel,
        var isDataCache: Boolean = false
): ImpressHolder(), RechargeComponentVisitable {

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeBUWidgetDataModel) {
            b.data.items.zip(data.items).all { (elt1, elt2) ->
                elt1.id == elt2.id
            }
        } else false
    }

    override fun getChangePayloadFrom(b: Any?): Bundle {
        return Bundle()
    }

    override fun type(typeFactory: RechargeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return data.title
    }
}