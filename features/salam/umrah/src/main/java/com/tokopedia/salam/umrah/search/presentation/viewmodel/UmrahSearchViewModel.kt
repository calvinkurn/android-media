package com.tokopedia.salam.umrah.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.salam.umrah.common.util.UmrahDispatchersProvider
import com.tokopedia.salam.umrah.search.data.UmrahSearchProduct
import com.tokopedia.salam.umrah.search.data.UmrahSearchProductDataParam
import com.tokopedia.salam.umrah.search.data.UmrahSearchProductEntity
import com.tokopedia.salam.umrah.search.data.model.ParamFilter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by furqan on 20/10/2019
 */
class UmrahSearchViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                               val dispatcher: UmrahDispatchersProvider)
    : BaseViewModel(dispatcher.Main) {

    private val privateSearchResult = MutableLiveData<Result<List<UmrahSearchProduct>>>()
    val searchResult: LiveData<Result<List<UmrahSearchProduct>>>
        get() = privateSearchResult

    fun setSearchParam(_searchParam: UmrahSearchProductDataParam) {
        searchParam = _searchParam
    }

    fun getSortValue() = searchParam.sortMethod

    fun getSlugName() = searchParam.categorySlugName

    fun setFilter(paramFilter: ParamFilter) {
        paramFilter.let {
            searchParam.apply {
                departureCityId = it.departureCity
                departurePeriod = it.departurePeriod
                durationDaysMin = it.durationDaysMinimum
                durationDaysMax = it.durationDaysMaximum
                priceMin = it.priceMinimum
                priceMax = it.priceMaximum
            }
        }
    }

    fun setSortValue(sort: String) {
        searchParam.sortMethod = sort
    }

    fun searchUmrahProducts(page: Int, searchQuery: String) {
        searchParam.page = page
        launchCatchError(block = {
            val params = mapOf(PARAM_UMRAH_SEARCH to searchParam)
            val graphqlRequest = GraphqlRequest(searchQuery, UmrahSearchProductEntity::class.java, params)

            val response = withContext(dispatcher.IO) {
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }
            privateSearchResult.value = Success(response.getSuccessData<UmrahSearchProductEntity>().umrahSearchProducts)
        }) {
            privateSearchResult.value = Fail(it)
        }
    }

    companion object {
        var searchParam: UmrahSearchProductDataParam = UmrahSearchProductDataParam()
        const val PARAM_UMRAH_SEARCH = "params"
    }
}