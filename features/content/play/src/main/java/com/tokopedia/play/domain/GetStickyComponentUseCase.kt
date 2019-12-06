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

// Warn! Don't use this, if you want to get sticky component, please use getChannelInfo > pinned_message
class GetStickyComponentUseCase @Inject constructor(private val playApi: PlayApi) : UseCase<StickyComponent>() {

    var channelId = ""

    override suspend fun executeOnBackground(): StickyComponent {
        return playApi
                .getStickyComponents(channelId)
                .data
                .stickyComponent
    }
}
