package com.tokopedia.salam.umrah.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.salam.umrah.search.data.UmrahSearchProduct
import com.tokopedia.salam.umrah.search.data.UmrahSearchProductDataParam
import com.tokopedia.salam.umrah.search.data.UmrahSearchProductEntity
import com.tokopedia.salam.umrah.search.data.model.ParamFilter
import com.tokopedia.salam.umrah.search.util.SearchOrCategory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by M on 16/12/2019
 */
class UmrahSearchViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                               val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    private val privateSearchResult = MutableLiveData<Result<List<UmrahSearchProduct>>>()
    val searchResult: LiveData<Result<List<UmrahSearchProduct>>>
        get() = privateSearchResult

    private fun getSlugName() = searchParam.categorySlugName

    fun setSearchParam(umrahSearchProductDataParam: UmrahSearchProductDataParam) {
        searchParam = umrahSearchProductDataParam
    }

    fun getSearchParam() = searchParam

    fun resetSearchParam() {
        searchParam = UmrahSearchProductDataParam(sortMethod = searchParam.sortMethod)
    }

    fun setSortValue(sort: String) {
        searchParam.sortMethod = sort
    }

    fun getSortValue() = searchParam.sortMethod

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

    fun getFilter() = ParamFilter().apply {
        departureCity = getSearchParam().departureCityId
        departurePeriod = getSearchParam().departurePeriod
        durationDaysMinimum = getSearchParam().durationDaysMin
        durationDaysMaximum = getSearchParam().durationDaysMax
        priceMinimum = getSearchParam().priceMin
        priceMaximum = getSearchParam().priceMax
    }

    fun searchUmrahProducts(page: Int, searchQuery: String) {
        searchParam.page = page
        launchCatchError(block = {
            val params = mapOf(PARAM_UMRAH_SEARCH to searchParam)
            val graphqlRequest = GraphqlRequest(searchQuery, UmrahSearchProductEntity::class.java, params)

            val response = withContext(dispatcher.io) {
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }
            privateSearchResult.value = Success(response.getSuccessData<UmrahSearchProductEntity>().umrahSearchProducts)
        }) {
            privateSearchResult.value = Fail(it)
        }
    }

    fun getSearchOrCategory(): SearchOrCategory =
            if (getSlugName() != "") SearchOrCategory.CATEGORY
            else SearchOrCategory.SEARCH

    fun initSortValue(sort: String) {
        if (getSortValue() == "") searchParam.sortMethod = sort
    }

    companion object {
        private var searchParam: UmrahSearchProductDataParam = UmrahSearchProductDataParam()
        const val PARAM_UMRAH_SEARCH = "params"
    }
}