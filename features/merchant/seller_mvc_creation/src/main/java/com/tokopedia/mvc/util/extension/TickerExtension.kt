package com.tokopedia.mvc.util.extension

import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.mvc.data.response.GetTargetedTickerResponse

fun List<GetTargetedTickerResponse.GetTargetedTicker.TickerList>.firstTickerMessage(): String {
    val length = size
    return if (length.isMoreThanZero()) {
        this[0].content
    } else {
        ""
    }
}
