package com.tokopedia.sellerpersona.common

import com.tokopedia.remoteconfig.RemoteConfig
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 17/10/23.
 */

class SellerPersonaRemoteConfig @Inject constructor(
    private val remoteConfig: RemoteConfig
) {

    fun isComposeEnabled(): Boolean {
        return remoteConfig.getBoolean(COMPOSE_ENABLED, false)
    }

    companion object {
        private const val COMPOSE_ENABLED = "android_seller_app_persona_compose_enabled"
    }
}