package com.tokopedia.home.beranda.presentation.view.listener.shorten

import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.listener.shorten.item.ThumbnailTwoSquareWidgetCallback
import com.tokopedia.home.beranda.presentation.view.listener.shorten.item.MissionTwoSquareWidgetCallback
import com.tokopedia.home.beranda.presentation.view.listener.shorten.item.ProductTwoSquareWidgetCallback
import com.tokopedia.home_component.viewholders.shorten.ContainerMultiTwoSquareListener

class TwoSquareWidgetListenerCallback(
    val homeCategoryListener: HomeCategoryListener
) : ContainerMultiTwoSquareListener(
    ThumbnailTwoSquareWidgetCallback(homeCategoryListener),
    MissionTwoSquareWidgetCallback(homeCategoryListener),
    ProductTwoSquareWidgetCallback(homeCategoryListener)
)
