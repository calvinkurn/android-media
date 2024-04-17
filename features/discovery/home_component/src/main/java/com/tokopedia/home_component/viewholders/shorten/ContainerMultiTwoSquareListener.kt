package com.tokopedia.home_component.viewholders.shorten

import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.DealsWidgetListener
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.MissionWidgetListener

abstract class ContainerMultiTwoSquareListener(
    private val deals: DealsWidgetListener,
    private val mission: MissionWidgetListener,
) : DealsWidgetListener by deals,
    MissionWidgetListener by mission
