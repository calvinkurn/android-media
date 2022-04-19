package com.tokopedia.devicefingerprint.di

import com.tokopedia.devicefingerprint.appauth.AppAuthWorker
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorWorker
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Component

@DeviceFingerprintScope
@Component(modules = [DeviceFingerprintModule::class])
interface DeviceFingerprintComponent {
    fun inject(appAuthWorker: AppAuthWorker)
    fun inject(submitDeviceWorker: SubmitDeviceWorker)
    fun inject(worker: DataVisorWorker)
    fun getGraphQlRepo(): GraphqlRepository
}
