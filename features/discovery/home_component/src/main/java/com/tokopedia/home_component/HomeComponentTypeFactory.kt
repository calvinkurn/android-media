package com.tokopedia.home_component

import com.tokopedia.home_component.visitable.DynamicLegoBannerViewModel

/**
 * Created by Devara on 2020-04-28
 */
interface HomeComponentTypeFactory {
    fun type(dynamicLegoBannerViewModel: DynamicLegoBannerViewModel): Int
}