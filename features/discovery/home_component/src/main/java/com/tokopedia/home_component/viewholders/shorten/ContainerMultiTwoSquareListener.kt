package com.tokopedia.home_component.viewholders.shorten

import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.ThumbnailWidgetListener
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.MissionWidgetListener

abstract class ContainerMultiTwoSquareListener(
    private val deals: ThumbnailWidgetListener,
    private val mission: MissionWidgetListener,
) : ThumbnailWidgetListener by deals,
    MissionWidgetListener by mission
