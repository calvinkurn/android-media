package com.tokopedia.inbox.domain.cache

import com.tokopedia.inbox.common.InboxFragmentType
import com.tokopedia.inboxcommon.RoleType

object InboxCacheState {

    @RoleType
    var role: Int? = null
        private set

    @InboxFragmentType
    var initialPage: Int? = null
        private set

    private val roleCacheKey = "role_${javaClass.simpleName}"
    private val initialPageCacheKey = "initial_page_${javaClass.simpleName}"
    private const val defaultRole = RoleType.BUYER
    private const val defaultInitialPage = InboxFragmentType.NOTIFICATION

    fun init(cacheManager: InboxCacheManager?) {
        if (cacheManager == null) return
        if (role == null) {
            role = getRoleCache(cacheManager)
        }
        if (initialPage == null) {
            initialPage = getInitialPageCache(cacheManager)
        }
    }

    fun updateRole(@RoleType role: Int) {
        this.role = role
    }

    fun updateInitialPage(initialPage: Int) {
        this.initialPage = initialPage
    }

    fun saveAllCache(cacheManager: InboxCacheManager) {
        role?.let {
            saveRoleCache(cacheManager, it)
        }
        initialPage?.let {
            saveInitialPageCache(cacheManager, it)
        }
    }

    private fun saveRoleCache(
            cacheManager: InboxCacheManager,
            cacheValue: Int
    ) {
        cacheManager.saveCacheInt(roleCacheKey, cacheValue)
    }

    private fun saveInitialPageCache(
            cacheManager: InboxCacheManager,
            cacheValue: Int
    ) {
        cacheManager.saveCacheInt(initialPageCacheKey, cacheValue)
    }

    private fun getRoleCache(cacheManager: InboxCacheManager): Int {
        var cacheValue = cacheManager.loadCacheInt(roleCacheKey)
        if (cacheValue == null || cacheValue == -1) {
            cacheValue = defaultRole
            saveRoleCache(cacheManager, cacheValue)
        }
        return cacheValue
    }

    private fun getInitialPageCache(cacheManager: InboxCacheManager): Int? {
        var cacheValue = cacheManager.loadCacheInt(initialPageCacheKey)
        if (cacheValue == null || cacheValue == -1) {
            cacheValue = defaultInitialPage
            saveInitialPageCache(cacheManager, cacheValue)
        }
        return cacheValue
    }

}

