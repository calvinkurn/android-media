package com.tokopedia.play.domain

import com.tokopedia.play.data.Channel
import com.tokopedia.play.data.network.PlayApi
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by mzennis on 2019-12-03.
 */

class GetChannelInfoUseCase @Inject constructor(private val playApi: PlayApi) : UseCase<Channel>() {

    var channelId = ""

    override suspend fun executeOnBackground(): Channel {
        return playApi.getChannelInfoV3(channelId).data.channel
    }
}
