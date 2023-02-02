package com.tokopedia.entertainment.pdp.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.entertainment.pdp.adapter.factory.EventRedeemRevampTypeFactory

/**
 * Author firmanda on 17,Nov,2022
 */

class ParticipantTitleUiModel (
    val id: String = "",
    val day: Int = -1,
    val title: String = "",
    var isDisabled: Boolean = false,
    var isChecked: Boolean = false
): Visitable<EventRedeemRevampTypeFactory> {
    override fun type(typeFactory: EventRedeemRevampTypeFactory): Int {
        return typeFactory.type(this)
    }
}
