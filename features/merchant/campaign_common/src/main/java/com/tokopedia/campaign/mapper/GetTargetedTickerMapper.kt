package com.tokopedia.campaign.mapper

import com.tokopedia.campaign.data.response.GetTargetedTickerResponse
import com.tokopedia.campaign.entity.RemoteTicker


import javax.inject.Inject

class GetTargetedTickerMapper @Inject constructor() {

    fun map(response: GetTargetedTickerResponse): List<RemoteTicker> {
        return response.getTargetedTicker.list.map { remoteTicker ->
            RemoteTicker(
                title = remoteTicker.title,
                description = remoteTicker.content,
                type = remoteTicker.type,
                actionLabel = remoteTicker.action.label,
                actionType = remoteTicker.action.type,
                actionAppUrl = remoteTicker.action.appURL
            )
        }
    }

}
