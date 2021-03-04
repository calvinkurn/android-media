package com.tokopedia.entertainment.pdp.network_api

import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl

interface EventCheckoutApi {

    companion object{
        val BASE_URL = if(TokopediaUrl.getInstance().TYPE == Env.LIVE) "https://booking.tokopedia.com/"
        else "https://booking-staging.tokopedia.com/"
    }
}