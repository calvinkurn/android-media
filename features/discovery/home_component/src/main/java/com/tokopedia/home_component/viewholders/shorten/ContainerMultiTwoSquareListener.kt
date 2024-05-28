package com.tokopedia.home_component.viewholders.shorten

import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.ThumbnailWidgetListener
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.MissionWidgetListener
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.ProductWidgetListener

abstract class ContainerMultiTwoSquareListener(
    private val thumbnail: ThumbnailWidgetListener,
    private val mission: MissionWidgetListener,
    private val product: ProductWidgetListener,
) : ThumbnailWidgetListener by thumbnail,
    MissionWidgetListener by mission,
    ProductWidgetListener by product
