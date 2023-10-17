package com.tokopedia.stories.internal.storage

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Created by kenny.hadisaputra on 22/08/23
 */
object AppSessionStoriesSeenStorage : StoriesSeenStorage {

    private val statusMap = mutableMapOf<StoriesSeenStorage.Author, Long>()

    private val mutex = Mutex()

    override suspend fun hasSeenAllAuthorStories(
        key: StoriesSeenStorage.Author,
        laterThanMillis: Long,
    ): Boolean = mutex.withLock {
        val latestTimeChanged = statusMap[key] ?: return false
        return latestTimeChanged > laterThanMillis
    }

    override suspend fun setSeenAllAuthorStories(key: StoriesSeenStorage.Author) = mutex.withLock {
        val latestTimeChanged = statusMap[key]
        val currentTimeMillis = System.currentTimeMillis()
        if (latestTimeChanged != null && latestTimeChanged > currentTimeMillis) return
        statusMap[key] = currentTimeMillis
    }
}
