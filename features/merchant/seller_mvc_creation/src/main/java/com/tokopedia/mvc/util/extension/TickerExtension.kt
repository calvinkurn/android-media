package com.tokopedia.mvc.util.extension

import com.tokopedia.campaign.data.response.GetTargetedTickerResponse
import com.tokopedia.kotlin.extensions.view.isMoreThanZero


fun List<GetTargetedTickerResponse.GetTargetedTicker.TickerList>.firstTickerMessage(): String {
    val length = size
    return if (length.isMoreThanZero()) {
        this[0].content
    } else {
        ""
    }
}
