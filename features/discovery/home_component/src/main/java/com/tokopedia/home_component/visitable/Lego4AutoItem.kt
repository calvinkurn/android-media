package com.tokopedia.home_component.visitable

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by yoasfs on 28/07/20
 */

data class Lego4AutoItem (
        val impressHolder: ImpressHolder = ImpressHolder(),
        val grid: ChannelGrid = ChannelGrid()
)