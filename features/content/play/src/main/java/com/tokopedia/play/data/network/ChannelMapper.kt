package com.tokopedia.play.data.network

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.play.data.Channel
import retrofit2.Response
import rx.functions.Func1


/**
 * Created by mzennis on 2019-12-03.
 */

class ChannelMapper : Func1<Response<DataResponse<Channel.ChannelResponse>>, Channel> {

    override fun call(response: Response<DataResponse<Channel.ChannelResponse>>?): Channel? {
        response?.let {
            return if (it.isSuccessful) {
                it.body()?.data?.channel
            } else {
                null
            }
        }
        return null
    }

}