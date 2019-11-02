package com.tokopedia.digital.home.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.digital.home.domain.GetSortListHomePageUseCase
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePageItemModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DigitalHomePageViewModel  @Inject constructor(
        val graphqlRepository: GraphqlRepository,
        val dispatcher: CoroutineDispatcher,
        val getSortListHomePageUseCase: GetSortListHomePageUseCase)
    : BaseViewModel(dispatcher) {

    val digitalHomePageList = MutableLiveData<List<DigitalHomePageItemModel>>()
    val isAllError = MutableLiveData<Boolean>()

    fun getInitialList(isLoadFromCloud: Boolean) {
        val list: List<DigitalHomePageItemModel> = getSortListHomePageUseCase.getSortEmptyList(isLoadFromCloud)
        digitalHomePageList.value = list
        isAllError.value = false
    }

    fun getBannerList(rawQuery: String, isLoadFromCloud: Boolean){
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(rawQuery, DigitalHomePageBannerModel::class.java)
                var graphQlCacheStrategy : GraphqlCacheStrategy
                if(isLoadFromCloud) {
                    graphQlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
                }else{
                    graphQlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                }
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphQlCacheStrategy)
            }.getSuccessData<DigitalHomePageBannerModel>()
            digitalHomePageList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[BANNER_ORDER] = data
                updatedList[BANNER_ORDER].isLoaded = true
                updatedList[BANNER_ORDER].isSuccess = true
                digitalHomePageList.value = updatedList
            }
        }){
            digitalHomePageList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[BANNER_ORDER].isLoaded = true
                updatedList[BANNER_ORDER].isSuccess = false
                digitalHomePageList.value = updatedList
                checkIfAllError()
            }
        }
    }

    fun getCategoryList(rawQuery: String, isLoadFromCloud: Boolean){
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(rawQuery, DigitalHomePageCategoryModel::class.java)
                var graphQlCacheStrategy : GraphqlCacheStrategy
                if(isLoadFromCloud) {
                    graphQlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
                }else{
                    graphQlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                }
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphQlCacheStrategy)
            }.getSuccessData<DigitalHomePageCategoryModel>()
            digitalHomePageList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[CATEGORY_ORDER] = data
                updatedList[CATEGORY_ORDER].isLoaded = true
                updatedList[CATEGORY_ORDER].isSuccess = true
                digitalHomePageList.value = updatedList
            }
        }){
            digitalHomePageList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[CATEGORY_ORDER].isLoaded = true
                updatedList[CATEGORY_ORDER].isSuccess = false
                digitalHomePageList.value = updatedList
                checkIfAllError()
            }
        }
    }

    fun checkIfAllError() {
        digitalHomePageList.value?.let {
            var isSuccess = false
            for (item in it) {
                if (item.isSuccess || !item.isLoaded) {
                    isSuccess = true
                    break
                }
            }
            if (!isSuccess) isAllError.value = true
        }
    }

    companion object {
        val BANNER_ORDER = 0
        val TRANSACTION_ORDER = 1
        val CATEGORY_ORDER = 2
        val PROMO_ORDER = 3
    }
}
