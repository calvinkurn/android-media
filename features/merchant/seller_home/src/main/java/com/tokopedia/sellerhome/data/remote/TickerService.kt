package com.tokopedia.sellerhome.data.remote

import com.tokopedia.sellerhome.data.remote.model.TickerResponse
import com.tokopedia.url.TokopediaUrl
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created By @ilhamsuaib on 2020-01-17
 */

interface TickerService {

    companion object {

        val BASE_URL = TokopediaUrl.getInstance().MOJITO
        const val PATH_API_V1_ANNOUNCEMENT_TICKER = "/api/v1/tickers"

        const val SIZE = "50"
        const val HEADER_USER_ID = "Tkpd-UserId"
        const val PAGE_SIZE = "page[size]"
        const val FILTER_DEVICE = "filter[device]"
        const val FILTER_SELLERAPP_ANDROID_DEVICE = "sellerapp-android"
    }

    @GET(PATH_API_V1_ANNOUNCEMENT_TICKER)
    suspend fun getTicker(
            @Header(HEADER_USER_ID) userId: String,
            @Query(PAGE_SIZE) size: String,
            @Query(FILTER_DEVICE) device: String
    ): TickerResponse
}