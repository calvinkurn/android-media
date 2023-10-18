package com.tokopedia.discovery.common.microinteraction

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

abstract class SearchBarMicroInteractionProvider<T>(
    private var contextProvider: (() -> Context?)?
): Lazy<T?> {

    private var microInteraction: Any? = UNINITIALIZED

    override fun isInitialized() = microInteraction != UNINITIALIZED

    override val value: T?
        get() =
            if (microInteraction === UNINITIALIZED)
                initializeMicroInteraction()
            else
                @Suppress("UNCHECKED_CAST")
                microInteraction as? T?

    private fun initializeMicroInteraction(): T? {
        microInteraction = createMicroInteraction(contextProvider)
        contextProvider = null

        @Suppress("UNCHECKED_CAST")
        return microInteraction as? T?
    }

    private fun createMicroInteraction(contextProvider: (() -> Context?)?): T? {
        val context = contextProvider?.invoke() ?: return null

        return if (isEnabled(context)) create(context) else null
    }

    abstract fun create(context: Context): T

    private fun isEnabled(context: Context): Boolean = isEnabledFromRemoteConfig(context)

    private fun isEnabledFromRemoteConfig(context: Context): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)

        return remoteConfig.getBoolean(MICRO_INTERACTION_REMOTE_CONFIG_KEY, false)
    }

    private object UNINITIALIZED
}
