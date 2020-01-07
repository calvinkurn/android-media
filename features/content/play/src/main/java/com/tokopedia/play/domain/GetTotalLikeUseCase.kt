package com.tokopedia.play.domain

import com.tokopedia.play.data.TotalLike
import com.tokopedia.play.data.network.PlayApi
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by mzennis on 2019-12-03.
 */

class GetTotalLikeUseCase @Inject constructor(private val playApi: PlayApi) : UseCase<TotalLike>() {

    var channelId = ""

    override suspend fun executeOnBackground(): TotalLike {
        return playApi.getTotalLike(channelId).data
    }
}
