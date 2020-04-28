package com.tokopedia.home_component

import com.tokopedia.home_component.visitable.DynamicLegoBannerViewModel

/**
 * Created by DevAra on 2020-04-28
 */
interface HomeComponentTypeFactory {
    fun type(dynamicLegoBannerViewModel: DynamicLegoBannerViewModel): Int
}