package com.tokopedia.play.domain

import com.tokopedia.play.data.Like
import com.tokopedia.play.data.network.PlayApi
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by mzennis on 2019-12-03.
 */

// Please use this usecase to get total likes
class GetTotalLikeUseCase @Inject constructor(private val playApi: PlayApi) : UseCase<Like>() {

    var channelId = ""

    override suspend fun executeOnBackground(): Like {
        return playApi.getTotalLike(channelId).data
    }
}
