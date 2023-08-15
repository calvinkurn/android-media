package com.tokopedia.home_component

import com.tokopedia.home_component_header.util.HomeChannelHeaderRollenceController
import com.tokopedia.remoteconfig.RollenceKey

/**
 * Created by yfsx on 4/28/21.
 */
object HomeComponentRollenceController {

    fun fetchHomeComponentRollenceValue() {
        HomeChannelHeaderRollenceController.fetchHomeHeaderRollence()
    }

}
