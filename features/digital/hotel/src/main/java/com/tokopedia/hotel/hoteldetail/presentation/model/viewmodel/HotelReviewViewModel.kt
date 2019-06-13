package com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.common.getSuccessData
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelReviewParam
import com.tokopedia.hotel.search.data.model.PropertySearch
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by furqan on 22/04/19
 */
class HotelReviewViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                               val graphqlRepository: GraphqlRepository): BaseViewModel(dispatcher) {

    val reviewResult = MutableLiveData<Result<HotelReview.ReviewData>>()

    fun getReview(query: String, hotelReviewParam: HotelReviewParam, dummy: String = "") {
        val dataParams = mapOf(PARAM_REVIEW_KEY to hotelReviewParam)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(query, HotelReview.Response::class.java, dataParams, false)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelReview.Response>()
            reviewResult.value = Success(data = data.propertyReview)
        }) {
            it.printStackTrace()
            val gson = Gson()
            reviewResult.value = Success(gson.fromJson(dummy,
                    HotelReview.Response::class.java).propertyReview)
        }
    }

    fun test() {
        reviewResult.value = Success(data = HotelReview.ReviewData())
    }

    companion object {
        const val PARAM_REVIEW_KEY = "data"
    }
}