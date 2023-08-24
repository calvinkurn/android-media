package com.tokopedia.notifcenter.util.cache

import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class NotifCenterCacheState @Inject constructor(
    private val cacheManager: NotifCenterCacheManager,
    userSession: UserSessionInterface,
) {
    private val defaultRole = RoleType.BUYER

    @RoleType
    var role: Int = defaultRole
        private set

    private val roleCacheKey = "role_${javaClass.simpleName}_${userSession.userId}"

    init {
        role = getRoleCache(cacheManager)
    }

    private fun getRoleCache(cacheManager: NotifCenterCacheManager): Int {
        var cacheValue = cacheManager.loadCacheInt(roleCacheKey)
        if (cacheValue == null || cacheValue == -1) {
            cacheValue = defaultRole
            saveRoleCache(cacheValue)
        }
        return cacheValue
    }

    fun saveRoleCache(
            @RoleType
            role: Int
    ) {
        cacheManager.saveCacheInt(roleCacheKey, role)
    }

}

