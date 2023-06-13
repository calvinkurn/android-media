package com.tokopedia.entertainment.pdp.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.entertainment.pdp.adapter.diffutil.BaseEventRedeemRevampDiffer
import com.tokopedia.entertainment.pdp.adapter.factory.EventRedeemRevampAdapterTypeFactory

/**
 * Author firmanda on 17,Nov,2022
 */

class EventRedeemRevampAdapter (
    typeFactory: EventRedeemRevampAdapterTypeFactory,
    differ: BaseEventRedeemRevampDiffer,
    eventRedeemBaseAdapterListener: EventRedeemBaseAdapterListener,
): BaseEventRedeemRevampAdapter<Visitable<*>, EventRedeemRevampAdapterTypeFactory>(typeFactory, differ, eventRedeemBaseAdapterListener)

