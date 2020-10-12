package com.tokopedia.devicefingerprint.di

import com.tokopedia.devicefingerprint.service.SubmitDeviceInfoService
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Component

@DeviceFingerprintScope
@Component(modules = [DeviceFingerprintModule::class])
interface DeviceFingerprintComponent {
    fun inject(service: SubmitDeviceInfoService)
    fun getGraphQlRepo(): GraphqlRepository
}
