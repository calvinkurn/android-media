package com.tokopedia.createpost.common.domain.usecase.cache

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.createpost.common.FEED_POST_MEDIA_CACHE_ID
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 21, 2023
 */
class DeleteMediaPostCacheUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<Unit, Unit>(dispatchers.io) {

    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: Unit) {
        try {
            val cacheManager = SaveInstanceCacheManager(context, FEED_POST_MEDIA_CACHE_ID)

            val cacheList: Set<String> = cacheManager.get(
                FEED_POST_MEDIA_CACHE_ID,
                Set::class.java
            ) ?: setOf()

            cacheList.forEach {
                val file = File(it)
                if(file.exists())
                    file.delete()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
