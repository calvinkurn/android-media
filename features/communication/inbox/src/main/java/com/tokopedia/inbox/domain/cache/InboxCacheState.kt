package com.tokopedia.inbox.domain.cache

import com.tokopedia.inbox.common.InboxFragmentType
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class InboxCacheState @Inject constructor(
        private val cacheManager: InboxCacheManager,
        private val userSession: UserSessionInterface,
) {
    private val defaultRole = RoleType.BUYER
    private val defaultInitialPage = InboxFragmentType.NOTIFICATION

    @RoleType
    var role: Int = defaultRole
        private set

    @InboxFragmentType
    var initialPage: Int = defaultInitialPage
        private set

    private val roleCacheKey = "role_${javaClass.simpleName}_${userSession.userId}"
    private val initialPageCacheKey = "initial_page_${javaClass.simpleName}_${userSession.userId}"

    init {
        role = getRoleCache(cacheManager)
        initialPage = getInitialPageCache(cacheManager)
    }

    private fun getRoleCache(cacheManager: InboxCacheManager): Int {
        var cacheValue = cacheManager.loadCacheInt(roleCacheKey)
        if (cacheValue == null || cacheValue == -1) {
            cacheValue = defaultRole
            saveRoleCache(cacheValue)
        }
        return cacheValue
    }

    private fun getInitialPageCache(cacheManager: InboxCacheManager): Int {
        var cacheValue = cacheManager.loadCacheInt(initialPageCacheKey)
        if (cacheValue == null || cacheValue == -1) {
            cacheValue = defaultInitialPage
            saveInitialPageCache(cacheValue)
        }
        return cacheValue
    }

    fun saveRoleCache(
            @RoleType
            role: Int
    ) {
        cacheManager.saveCacheInt(roleCacheKey, role)
    }

    fun saveInitialPageCache(
            @InboxFragmentType
            page: Int
    ) {
        cacheManager.saveCacheInt(initialPageCacheKey, page)
    }

}

