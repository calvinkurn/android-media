package com.tokopedia.telemetry.network.di

import com.tokopedia.telemetry.network.TelemetryWorker
import dagger.Component

@TelemetryScope
@Component(modules = [TelemetryModule::class])
interface TelemetryComponent {
    fun inject(telemetryWorker: TelemetryWorker)
}
