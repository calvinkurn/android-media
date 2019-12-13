package com.tokopedia.tokopoints.view.respository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.TokoPointDetailEntity
import com.tokopedia.tokopoints.view.model.TokoPointEntity
import com.tokopedia.tokopoints.view.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Subscriber
import java.util.HashMap
import javax.inject.Inject

class PointHistoryRepository @Inject constructor(val mGetPointHistory: GraphqlUseCase, val queryMap : Map<String, String>) {

    suspend fun getPointsDetail(liveData: MutableLiveData<Resources<TokoPointEntity>>) =  withContext(Dispatchers.IO){
        liveData.postValue(Loading())
        val variables = HashMap<String, Any>()
        val request = GraphqlRequest(queryMap[CommonConstant.GQLQuery.TP_GQL_CURRENT_POINTS],
                TokoPointDetailEntity::class.java,
                variables, false)
        mGetPointHistory.clearRequest()
        mGetPointHistory.addRequest(request)
        mGetPointHistory.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                liveData.postValue(ErrorMessage("unable to load data"))
            }

            override fun onNext(response: GraphqlResponse) {
                val data = response.getData<TokoPointDetailEntity>(TokoPointDetailEntity::class.java)
                liveData.postValue(Success(data.tokoPoints))
            }
        })
    }

    fun onCleared() {
        mGetPointHistory.unsubscribe()
    }
}