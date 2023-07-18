package com.tokopedia.media.loader.listener

import com.tokopedia.media.loader.data.FailureType
import com.tokopedia.media.loader.data.Header

interface NetworkResponseListener {
    fun header(data: List<Header>, type: FailureType?)
}
