package com.tokopedia.devicefingerprint.di

import com.tokopedia.devicefingerprint.appauth.AppAuthWorker
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceInfoService
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Component

@DeviceFingerprintScope
@Component(modules = [DeviceFingerprintModule::class])
interface DeviceFingerprintComponent {
    fun inject(appAuthWorker: AppAuthWorker)
    fun inject(service: SubmitDeviceInfoService)
    fun getGraphQlRepo(): GraphqlRepository
}
