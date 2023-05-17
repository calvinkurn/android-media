package com.tokopedia.mvc.data.mapper

import com.tokopedia.mvc.data.response.GetTargetedTickerResponse
import com.tokopedia.mvc.domain.entity.RemoteTicker
import javax.inject.Inject

class GetTargetedTickerMapper @Inject constructor() {

    fun map(response: GetTargetedTickerResponse): List<RemoteTicker> {
        return response.getTargetedTicker.list.map { remoteTicker ->
            RemoteTicker(remoteTicker.title, remoteTicker.content)
        }
    }

}
