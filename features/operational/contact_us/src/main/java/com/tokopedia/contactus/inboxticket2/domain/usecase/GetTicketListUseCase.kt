package com.tokopedia.contactus.inboxticket2.domain.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.contactus.inboxticket2.data.ContactUsRepository
import com.tokopedia.contactus.inboxticket2.domain.TicketListResponse
import com.tokopedia.network.data.model.response.DataResponse
import java.util.*
import javax.inject.Inject

const val STATUS = "status"
const val READ = "read"
const val RATING = "rating"

class GetTicketListUseCase @Inject constructor(private val repository: ContactUsRepository) {
    suspend fun getTicketListResponse(queryMap: MutableMap<String, Any>, url: String): TicketListResponse {
        return (repository.getRestData(
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
}