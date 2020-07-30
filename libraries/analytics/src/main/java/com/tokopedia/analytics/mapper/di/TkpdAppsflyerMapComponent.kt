package com.tokopedia.analytics.mapper.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.analytics.mapper.TkpdAppsFlyerMapper

import dagger.Component

@TkpdAppsflyerMapScope
@Component(dependencies = [BaseAppComponent::class])
interface TkpdAppsflyerMapComponent {
    fun inject(tkpdAppsFlyerMapper: TkpdAppsFlyerMapper)
}
