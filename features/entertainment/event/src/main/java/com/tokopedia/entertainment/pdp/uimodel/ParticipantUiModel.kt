package com.tokopedia.entertainment.pdp.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.entertainment.pdp.adapter.factory.EventRedeemRevampTypeFactory

/**
 * Author firmanda on 17,Nov,2022
 */

class ParticipantUiModel(
    val id: String = "",
    val title: String = "",
    val subTitle: String = "",
    var isChecked: Boolean = false
): Visitable<EventRedeemRevampTypeFactory> {
    override fun type(typeFactory: EventRedeemRevampTypeFactory): Int {
        return typeFactory.type(this)
    }
}
