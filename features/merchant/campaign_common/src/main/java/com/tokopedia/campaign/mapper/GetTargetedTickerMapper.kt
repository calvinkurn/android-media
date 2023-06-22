package com.tokopedia.campaign.mapper

import com.tokopedia.campaign.data.response.GetTargetedTickerResponse
import com.tokopedia.campaign.entity.RemoteTargetedTicker

import javax.inject.Inject

class GetTargetedTickerMapper @Inject constructor() {

    fun map(response: GetTargetedTickerResponse): List<RemoteTargetedTicker> {
        return response.getTargetedTicker.list.map { remoteTicker ->
            RemoteTargetedTicker(
                title = remoteTicker.title,
                description = remoteTicker.content,
                type = remoteTicker.type
            )
        }
    }

}
