package com.tokopedia.creation.common.upload.domain.usecase.post

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.util.getFileAbsolutePath
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 21, 2023
 */
class DeleteMediaPostCacheUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<Set<String>, Unit>(dispatchers.io) {

    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: Set<String>) {
        params.forEach {
            try {
                val file = File(getFileAbsolutePath(it).orEmpty())
                if (file.exists())
                    file.delete()
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }
}
