package com.tokopedia.remoteconfig

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.lang.Exception

class RemoteConfigRealTimeManager(
    private val remoteConfig: RemoteConfig
) : DefaultLifecycleObserver {

    private val _updatedRemoteConfigFlow =
        MutableSharedFlow<Result<Set<String>>>(replay = 1)
    val updatedRemoteConfigFlow: SharedFlow<Result<Set<String>>>
        get() = _updatedRemoteConfigFlow.asSharedFlow()

    private var realTimeUpdateListener: RemoteConfig.RealTimeUpdateListener? = null

    private var lifecycleOwner: LifecycleOwner? = null

    fun startRealtimeUpdates(owner: LifecycleOwner) {
        if (lifecycleOwner == null) {
            owner.lifecycle.addObserver(this)

            lifecycleOwner = owner

            if (realTimeUpdateListener == null) {
                // Add a listener to Firebase Remote Config for updates
                realTimeUpdateListener = getRealTimeUpdateListener()
                remoteConfig.setRealtimeUpdate(realTimeUpdateListener)
            }
        }
    }

    fun stopRealTimeUpdates() {
        lifecycleOwner?.lifecycle?.removeObserver(this)
        lifecycleOwner = null
        realTimeUpdateListener = null
    }

    private fun getRealTimeUpdateListener(): RemoteConfig.RealTimeUpdateListener {
        return object : RemoteConfig.RealTimeUpdateListener {
            override fun onUpdate(updatedKeys: MutableSet<String>) {
                // override onUpdate when the updatedKeys value is ready to consume
                _updatedRemoteConfigFlow.tryEmit(Success(updatedKeys.toSet()))
            }

            override fun onError(e: Exception) {
                _updatedRemoteConfigFlow.tryEmit(Fail(e))
            }
        }
    }
}
