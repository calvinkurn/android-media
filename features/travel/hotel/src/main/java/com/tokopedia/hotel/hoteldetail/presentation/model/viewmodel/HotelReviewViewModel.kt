package com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelReviewParam
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by furqan on 22/04/19
 */
class HotelReviewViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                               val graphqlRepository: GraphqlRepository) : BaseViewModel(dispatcher.io) {

    val reviewResult = MutableLiveData<Result<HotelReview.ReviewData>>()

    fun getReview(query: String, hotelReviewParam: HotelReviewParam) {
        val dataParams = mapOf(PARAM_REVIEW_KEY to hotelReviewParam)
        launchCatchError(block = {
            val data = withContext(dispatcher.main) {
                val graphqlRequest = GraphqlRequest(query, HotelReview.Response::class.java, dataParams, false)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelReview.Response>()
            reviewResult.postValue(Success(data = data.propertyReview))
        }) {
            reviewResult.postValue(Fail(it))
        }
    }

    companion object {
        const val PARAM_REVIEW_KEY = "data"
    }
}