package com.tokopedia.digital.home.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.digital.home.domain.GetSortListHomePageUseCase
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePagePromoModel
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageItemModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
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

    fun getInitialList() {
        val list: List<DigitalHomePageItemModel> = getSortListHomePageUseCase.getSortEmptyList()
        digitalHomePageList.value = list
    }

    fun getBannerList(rawQuery: String){
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(rawQuery, DigitalHomePageBannerModel::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
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
            }
        }
    }

    fun getCategoryList(rawQuery: String){
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default){
                val graphqlRequest = GraphqlRequest(rawQuery, DigitalHomePageCategoryModel::class.java)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
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
            }
        }
    }

//    /**
//     * get promo list
//     * api not ready yet
//     */
//    fun getPromoList(rawQuery: String){
//        launchCatchError(block = {
//            val data = withContext(Dispatchers.Default){
//                val graphqlRequest = GraphqlRequest(rawQuery, DigitalHomePagePromoModel::class.java)
//                graphqlRepository.getReseponse(listOf(graphqlRequest))
//            }.getSuccessData<DigitalHomePagePromoModel>()
//            digitalHomePageList.value?.let {
//                val updatedList = it.toMutableList()
//                updatedList[CATEGORY_ORDER] = data.response
//                updatedList[CATEGORY_ORDER].isLoaded = true
//                updatedList[CATEGORY_ORDER].isSuccess = true
//                digitalHomePageList.value = updatedList
//            }
//        }){
//            digitalHomePageList.value?.let {
//                val updatedList = it.toMutableList()
//                updatedList[CATEGORY_ORDER].isLoaded = true
//                updatedList[CATEGORY_ORDER].isSuccess = false
//                digitalHomePageList.value = updatedList
//            }
//        }
//    }

    companion object {
        val BANNER_ORDER = 0
        val CATEGORY_ORDER = 1
        val PROMO_ORDER = 2
    }
}
