package com.tokopedia.devicefingerprint.di

import com.tokopedia.devicefingerprint.crysp.workmanager.CryspWorker
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorWorker
import com.tokopedia.devicefingerprint.service.SubmitDeviceInfoService
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Component

@DeviceFingerprintScope
@Component(modules = [DeviceFingerprintModule::class])
interface DeviceFingerprintComponent {
    fun inject(service: SubmitDeviceInfoService)
    fun inject(worker: CryspWorker)
    fun inject(worker: DataVisorWorker)
    fun getGraphQlRepo(): GraphqlRepository
}
