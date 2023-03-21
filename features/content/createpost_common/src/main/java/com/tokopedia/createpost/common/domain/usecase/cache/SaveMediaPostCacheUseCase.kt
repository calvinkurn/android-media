package com.tokopedia.createpost.common.domain.usecase.cache

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.createpost.common.FEED_POST_MEDIA_CACHE_ID
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 21, 2023
 */
class SaveMediaPostCacheUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<Set<String>, Unit>(dispatchers.io)  {

    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: Set<String>) {
        val cacheManager = SaveInstanceCacheManager(context, FEED_POST_MEDIA_CACHE_ID)
        cacheManager.put(FEED_POST_MEDIA_CACHE_ID, params)
    }
}
