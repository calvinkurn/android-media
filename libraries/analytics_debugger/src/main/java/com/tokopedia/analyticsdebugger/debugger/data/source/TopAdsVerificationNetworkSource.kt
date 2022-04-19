package com.tokopedia.analyticsdebugger.debugger.data.source

import android.content.Context
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.database.*
import com.tokopedia.analyticsdebugger.debugger.domain.model.Item
import com.tokopedia.analyticsdebugger.debugger.domain.model.TopAdsVerificationData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Observable
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class TopAdsVerificationNetworkSource @Inject
constructor(val context: Context, val graphqlUseCase: GraphqlUseCase) {

    companion object {
        private const val TOPADS_VERIFICATOR_CHUNK_SIZE = 50
    }

    var PENDING_DURATION_MS = 10000 // 10 seconds

    private val topAdsLogDao: TopAdsLogDao

    init {
        topAdsLogDao = TkpdAnalyticsDatabase.getInstance(context).topAdsLogDao()
    }

    fun appendVerificationStatus(logDBList: List<TopAdsLogDB>, isForTopadsVerificator: Boolean): Observable<List<TopAdsLogDB>> {
        return Observable.create { subscriber ->
            val urlCheckList = ArrayList<String>()
            val urlCheckMap = HashMap<String, TopAdsLogDB>()
            val chuckedSize = if (isForTopadsVerificator) TOPADS_VERIFICATOR_CHUNK_SIZE else logDBList.size
            logDBList.chunked(chuckedSize).forEach {
                for (item in it) {
                    if (item.eventStatus == STATUS_PENDING && System.currentTimeMillis() - item.timestamp > PENDING_DURATION_MS) {
                        urlCheckList.add(item.url.trim())
                        urlCheckMap[item.url.trim()] = item
                    }
                }
                if (isForTopadsVerificator) {
                    verifyAndUpdateItems(urlCheckList, urlCheckMap)
                }
                subscriber.onNext(it)

                urlCheckList.clear()
                urlCheckMap.clear()
            }
            subscriber.onCompleted()
        }
    }

    fun verifyAndUpdateItems(urlCheckList: List<String>, urlCheckMap: HashMap<String, TopAdsLogDB>) {
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_topads_verificator),
                TopAdsVerificationData::class.java
        )
        val variables = createParametersForQuery(urlCheckList)
        graphqlRequest.variables = variables
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        val response: TopAdsVerificationData? = graphqlUseCase.getData(RequestParams.EMPTY)
                .getData<TopAdsVerificationData>(TopAdsVerificationData::class.java)
        val resultList = response?.topadsVerifyClicksViews?.data
        resultList?.let {
            for (result in resultList) {
                val item = urlCheckMap[result.url.trim()]
                item?.let {
                    if (result.status) {
                        if (result.type == it.eventType) {
                            it.eventStatus = STATUS_MATCH
                        } else {
                            it.eventStatus = STATUS_NOT_MATCH
                        }
                    } else {
                        it.eventStatus = STATUS_DATA_NOT_FOUND
                    }

                    it.fullResponse = convertToFormattedString(result)
                    updateItem(it)
                }
            }
        }
    }

    private fun convertToFormattedString(item: Item) : String {
        val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
        try {
            return URLDecoder.decode(gson.toJson(item), "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            return gson.toJson(item)
        }
    }

    private fun createParametersForQuery(urlList: List<String>): Map<String, Any> {
        val variables: MutableMap<String, Any> = HashMap()
        variables["url"] = urlList.joinToString()
        return variables
    }

    private fun updateItem(item: TopAdsLogDB) {
        topAdsLogDao.updateItem(item)
    }
}
