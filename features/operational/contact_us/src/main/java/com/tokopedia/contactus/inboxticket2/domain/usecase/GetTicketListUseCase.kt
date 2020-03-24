package com.tokopedia.contactus.inboxticket2.domain.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.contactus.inboxticket2.data.InboxEndpoint
import com.tokopedia.contactus.inboxticket2.domain.TicketListResponse
import com.tokopedia.core.network.constants.TkpdBaseURL
import com.tokopedia.network.data.model.response.DataResponse
import java.util.*
import javax.inject.Inject

const val STATUS = "status"
const val READ = "read"
const val RATING = "rating"

class GetTicketListUseCase @Inject constructor() {
    private lateinit var mUrl: String
    suspend fun getTicketListResponse(queryMap: MutableMap<String, Any>): TicketListResponse {
        return (BaseRepository().getRestData(
                url,
                object : TypeToken<DataResponse<TicketListResponse>>() {}.type,
                RequestType.GET,
                queryMap) as DataResponse<TicketListResponse>).data

    }

    fun setQueryMap(status: Int, read: Int, rating: Int): MutableMap<String, Any> {
        val queryMap = HashMap<String, Any>()
        if (status > 0) queryMap[STATUS] = status
        if (read > 0) queryMap[READ] = read
        if (rating > 0) queryMap[RATING] = rating
        return queryMap
    }

    var url: String
        get() = if (::mUrl.isInitialized && mUrl.isNotEmpty()) mUrl else TkpdBaseURL.BASE_CONTACT_US + InboxEndpoint.LIST_TICKET
        set(Url) {
            mUrl = Url
        }
}