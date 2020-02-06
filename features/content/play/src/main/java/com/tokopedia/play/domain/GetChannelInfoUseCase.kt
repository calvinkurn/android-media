package com.tokopedia.play.domain

import com.google.gson.Gson
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.ERR_SERVER_ERROR
import com.tokopedia.play.data.Channel
import com.tokopedia.play.data.network.PlayApi
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by mzennis on 2019-12-03.
 */

class GetChannelInfoUseCase @Inject constructor(private val playApi: PlayApi) : UseCase<Channel>() {

    var channelId = ""

    override suspend fun executeOnBackground(): Channel {
        val response = playApi.getChannelInfoV5(channelId)
        if (response.isSuccessful) {
            response.body()?.let {
                return it.data.channel
            }
        }
        response.errorBody()?.let {
            val error = Gson().fromJson<DataResponse<Channel.Response>>(it.charStream(),
                    DataResponse::class.java)
            if (error != null && error.header != null) {
                throw MessageErrorException(error.header.errorCode)
            }
        }
        throw MessageErrorException(ERR_SERVER_ERROR)
    }
}
