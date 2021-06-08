package com.tokopedia.salam.umrah.travel.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.salam.umrah.search.data.UmrahSearchProductDataParam
import com.tokopedia.salam.umrah.travel.data.UmrahTravelProduct
import com.tokopedia.salam.umrah.travel.data.UmrahTravelAgentProductEntity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UmrahTravelProductViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                      val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    private val privateSearchResult = MutableLiveData<Result<List<UmrahTravelProduct>>>()
    val searchResult: LiveData<Result<List<UmrahTravelProduct>>>
        get() = privateSearchResult


    fun getDataProductTravel(page: Int,travelSlugName: String, searchQuery: String) {
        searchParam.travelAgentSlugName = travelSlugName
        searchParam.page = page
        searchParam.limit = PARAM_LIMIT
        searchParam.sortMethod = PARAM_SORT_METHOD
        launchCatchError(block = {
            val params = mapOf(PARAM_UMRAH_SEARCH to searchParam)
            val graphqlRequest = GraphqlRequest(searchQuery, UmrahTravelAgentProductEntity::class.java, params)

            val response = withContext(dispatcher.io) {
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }
            privateSearchResult.value = Success(response.getSuccessData<UmrahTravelAgentProductEntity>().umrahSearchProducts)
        }) {
            privateSearchResult.value = Fail(it)
        }
    }

    companion object {
        private var searchParam: UmrahSearchProductDataParam = UmrahSearchProductDataParam()
        const val PARAM_UMRAH_SEARCH = "params"
        const val PARAM_LIMIT = 5
        const val PARAM_SORT_METHOD = "3"
    }
}