package com.tokopedia.play.domain

import com.tokopedia.play.data.StickyComponent
import com.tokopedia.play.data.network.PlayApi
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by mzennis on 2019-12-03.
 */

class GetStickyComponentUseCase @Inject constructor(private val playApi: PlayApi) : UseCase<StickyComponent>() {

    var channelId = ""

    override suspend fun executeOnBackground(): StickyComponent {
        return withContext(Dispatchers.Default) {
            var result = StickyComponent()
            try {
                val response = playApi.getStickyComponents(channelId).await()
                response.data?.stickyComponent?.let {
                    result = it
                }
            } catch (e: Throwable) {
                throw e
            }
            result
        }
    }
}
