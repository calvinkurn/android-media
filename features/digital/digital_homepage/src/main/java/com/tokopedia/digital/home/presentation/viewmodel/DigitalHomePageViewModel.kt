package com.tokopedia.digital.home.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.digital.home.domain.GetSortListHomePageUseCase
import com.tokopedia.digital.home.model.*
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
                val graphqlCacheStrategy = getCacheStrategy(isLoadFromCloud)
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
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
                val graphqlCacheStrategy = getCacheStrategy(isLoadFromCloud)
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
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

    fun getFavoritesList(rawQuery: String, isLoadFromCloud: Boolean) {
        val param = mapOf(SECTION_TYPE to FAVORITES_PARAM)
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(rawQuery, DigitalHomePageFavoritesModel::class.java, param)
                val graphqlCacheStrategy = getCacheStrategy(isLoadFromCloud)
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
            }.getSuccessData<DigitalHomePageFavoritesModel>()
            digitalHomePageList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[FAVORITES_ORDER] = data
                updatedList[FAVORITES_ORDER].isLoaded = true
                updatedList[FAVORITES_ORDER].isSuccess = true
                digitalHomePageList.value = updatedList
            }
        }){
            digitalHomePageList.value?.let {
                val updatedList = it.toMutableList()
                updatedList[FAVORITES_ORDER].isLoaded = true
                updatedList[FAVORITES_ORDER].isSuccess = false
                digitalHomePageList.value = updatedList
                checkIfAllError()
            }
        }
    }

//    fun getTrustMarkList(rawQuery: String, isLoadFromCloud: Boolean) {
//        val param = mapOf(SECTION_TYPE to TRUST_MARK_PARAM)
//        launchCatchError(block = {
//            val data = withContext(Dispatchers.Default){
//                val graphqlRequest = GraphqlRequest(rawQuery, DigitalHomePageTrustMarkModel::class.java, param)
//                val graphqlCacheStrategy = getCacheStrategy(isLoadFromCloud)
//                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
//            }.getSuccessData<DigitalHomePageTrustMarkModel>()
//            digitalHomePageList.value?.let {
//                val updatedList = it.toMutableList()
//                updatedList[TRUST_MARK_ORDER] = data
//                updatedList[TRUST_MARK_ORDER].isLoaded = true
//                updatedList[TRUST_MARK_ORDER].isSuccess = true
//                digitalHomePageList.value = updatedList
//            }
//        }){
//            digitalHomePageList.value?.let {
//                val updatedList = it.toMutableList()
//                updatedList[TRUST_MARK_ORDER].isLoaded = true
//                updatedList[TRUST_MARK_ORDER].isSuccess = false
//                digitalHomePageList.value = updatedList
//                checkIfAllError()
//            }
//        }
//    }

//    fun getNewUserZoneList(rawQuery: String, isLoadFromCloud: Boolean) {
//        val param = mapOf(SECTION_TYPE to NEW_USER_ZONE_PARAM)
//        launchCatchError(block = {
//            val data = withContext(Dispatchers.Default){
//                val graphqlRequest = GraphqlRequest(rawQuery, DigitalHomePageNewUserZoneModel::class.java, param)
//                val graphqlCacheStrategy = getCacheStrategy(isLoadFromCloud)
//                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
//            }.getSuccessData<DigitalHomePageNewUserZoneModel>()
//            digitalHomePageList.value?.let {
//                val updatedList = it.toMutableList()
//                updatedList[NEW_USER_ZONE_ORDER] = data
//                updatedList[NEW_USER_ZONE_ORDER].isLoaded = true
//                updatedList[NEW_USER_ZONE_ORDER].isSuccess = true
//                digitalHomePageList.value = updatedList
//            }
//        }){
//            digitalHomePageList.value?.let {
//                val updatedList = it.toMutableList()
//                updatedList[NEW_USER_ZONE_ORDER].isLoaded = true
//                updatedList[NEW_USER_ZONE_ORDER].isSuccess = false
//                digitalHomePageList.value = updatedList
//                checkIfAllError()
//            }
//        }
//    }

//    fun getSpotlightList(rawQuery: String, isLoadFromCloud: Boolean) {
//        val param = mapOf(SECTION_TYPE to SPOTLIGHT_PARAM)
//        launchCatchError(block = {
//            val data = withContext(Dispatchers.Default){
//                val graphqlRequest = GraphqlRequest(rawQuery, DigitalHomePageSpotlightModel::class.java, param)
//                val graphqlCacheStrategy = getCacheStrategy(isLoadFromCloud)
//                graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)
//            }.getSuccessData<DigitalHomePageSpotlightModel>()
//            digitalHomePageList.value?.let {
//                val updatedList = it.toMutableList()
//                updatedList[SPOTLIGHT_ORDER] = data
//                updatedList[SPOTLIGHT_ORDER].isLoaded = true
//                updatedList[SPOTLIGHT_ORDER].isSuccess = true
//                digitalHomePageList.value = updatedList
//            }
//        }){
//            digitalHomePageList.value?.let {
//                val updatedList = it.toMutableList()
//                updatedList[SPOTLIGHT_ORDER].isLoaded = true
//                updatedList[SPOTLIGHT_ORDER].isSuccess = false
//                digitalHomePageList.value = updatedList
//                checkIfAllError()
//            }
//        }
//    }

    private fun getCacheStrategy(fromCloud: Boolean): GraphqlCacheStrategy {
        return if (fromCloud) {
            GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        } else {
            GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
        }
    }

    private fun checkIfAllError() {
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
        const val BANNER_ORDER = 0
        const val FAVORITES_ORDER = 1
        const val TRUST_MARK_ORDER = 2
        const val NEW_USER_ZONE_ORDER = 3
        const val SPOTLIGHT_ORDER = 4
        const val CATEGORY_ORDER = 2
        const val PROMO_ORDER = 6

        const val SECTION_TYPE = "sectionType"
        const val FAVORITES_PARAM = "BEHAVIOURAL_ICON"
        const val TRUST_MARK_PARAM = "TRUST_MARK"
        const val NEW_USER_ZONE_PARAM = "NEW_USER_ZONE"
        const val SPOTLIGHT_PARAM = "SPOTLIGHT"
    }
}
