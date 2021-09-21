package com.tokopedia.home_component.visitable

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by yoasfs on 31/05/21
 */

data class FeaturedBrandItem (
        val impressHolder: ImpressHolder = ImpressHolder(),
        val grid: ChannelGrid = ChannelGrid()
)