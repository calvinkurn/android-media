package com.tokopedia.entertainment.pdp.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventParticipantTitleViewHolder
import com.tokopedia.entertainment.pdp.adapter.viewholder.EventParticipantViewHolder
import com.tokopedia.entertainment.pdp.uimodel.ParticipantTitleUiModel
import com.tokopedia.entertainment.pdp.uimodel.ParticipantUiModel

/**
 * Author firmanda on 17,Nov,2022
 */

class EventRedeemRevampAdapterTypeFactory (
    private val listener: EventParticipantViewHolder.ParticipantListener,
    private val titleListener: EventParticipantTitleViewHolder.ParticipantTitleListener
) : BaseAdapterTypeFactory(),
    EventRedeemRevampTypeFactory {

    override fun type(uiModel: ParticipantUiModel): Int = EventParticipantViewHolder.LAYOUT
    override fun type(uiModel: ParticipantTitleUiModel): Int = EventParticipantTitleViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            EventParticipantViewHolder.LAYOUT -> EventParticipantViewHolder(listener, view)
            EventParticipantTitleViewHolder.LAYOUT -> EventParticipantTitleViewHolder(titleListener, view)
            else -> super.createViewHolder(view, type)
        }
    }
}
