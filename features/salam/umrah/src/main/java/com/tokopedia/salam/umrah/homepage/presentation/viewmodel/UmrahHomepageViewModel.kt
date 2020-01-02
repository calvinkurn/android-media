package com.tokopedia.salam.umrah.homepage.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.salam.umrah.homepage.data.*
import com.tokopedia.salam.umrah.homepage.presentation.usecase.UmrahHomepageEmptyDataUseCase
import com.tokopedia.salam.umrah.common.usecase.UmrahSearchParameterUseCase
import com.tokopedia.salam.umrah.common.util.UmrahDispatchersProvider
import com.tokopedia.salam.umrah.homepage.presentation.usecase.UmrahHomepageCategoryFeaturedUseCase
import com.tokopedia.salam.umrah.homepage.presentation.usecase.UmrahHomepageCategoryUseCase
import com.tokopedia.salam.umrah.homepage.presentation.usecase.UmrahHomepageMyUmrahUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * @author by firman on 16/10/19
 */

class UmrahHomepageViewModel @Inject constructor(private val getEmptyData: UmrahHomepageEmptyDataUseCase,
                                                 private val umrahCategoriesFeaturedUseCase: UmrahHomepageCategoryFeaturedUseCase,
                                                 private val umrahCategoriesUseCase: UmrahHomepageCategoryUseCase,
                                                 private val umrahSearchParamUseCase: UmrahSearchParameterUseCase,
                                                 private val umrahHomepageMyUmrahUseCase: UmrahHomepageMyUmrahUseCase,
                                                 dispatcher: UmrahDispatchersProvider) : BaseViewModel(dispatcher.Main) {


    private val _homePageModel = MutableLiveData<List<UmrahHomepageModel>>()
    val homePageModel: LiveData<List<UmrahHomepageModel>>
        get() = _homePageModel

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = _isError


    fun getIntialList(isLoadFromCloud: Boolean) {
        val list: List<UmrahHomepageModel> = getEmptyData.requestEmptyViewModels(isLoadFromCloud)
        _homePageModel.value = list
        _isError.value = false

    }

    fun getSearchParamData(rawQuery: String, isLoadFromCloud: Boolean) {
        launch {
            val result = umrahSearchParamUseCase.executeUseCase(rawQuery, isLoadFromCloud)
            when (result) {
                is Success -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[SEARCH_PARAM_ORDER] = result.data
                        updatedList[SEARCH_PARAM_ORDER].isLoaded = true
                        updatedList[SEARCH_PARAM_ORDER].isSuccess = true
                        _homePageModel.value = updatedList
                    }
                }

                is Fail -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[SEARCH_PARAM_ORDER].isLoaded = false
                        updatedList[SEARCH_PARAM_ORDER].isSuccess = false
                        _homePageModel.value = updatedList
                        _isError.value = true
                    }
                }
            }
        }
    }

    fun getUmrahSayaData(rawQuery: String, isLoadFromCloud: Boolean) {
        launch {
            when (val result = umrahHomepageMyUmrahUseCase.executeMyUmrah(rawQuery, isLoadFromCloud)) {
                is Success -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[DREAM_FUND_ORDER] = result.data
                        updatedList[DREAM_FUND_ORDER].isLoaded = true
                        updatedList[DREAM_FUND_ORDER].isSuccess = true
                        _homePageModel.value = updatedList
                    }
                }

                is Fail -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[DREAM_FUND_ORDER].isLoaded = true
                        updatedList[DREAM_FUND_ORDER].isSuccess = false
                        _homePageModel.value = updatedList
                       // _isError.value = true
                    }
                }
            }
        }
    }


    fun getCategoryData(rawQuery: String, isLoadFromCloud: Boolean) {
        launch {
            val result = umrahCategoriesUseCase.executeCategory(rawQuery, isLoadFromCloud)
            when (result) {
                is Success -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[CATEGORY_ORDER] = result.data
                        updatedList[CATEGORY_ORDER].isLoaded = true
                        updatedList[CATEGORY_ORDER].isSuccess = true
                        _homePageModel.value = updatedList
                    }
                }
                is Fail -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[CATEGORY_ORDER].isLoaded = true
                        updatedList[CATEGORY_ORDER].isSuccess = false
                        _homePageModel.value = updatedList
                        _isError.value = true
                    }
                }
            }
        }
    }

    fun getCategoryFeaturedData(rawQuery: String, isLoadFromCloud: Boolean) {
        launch {
            val result = umrahCategoriesFeaturedUseCase.executeCategoryFeatured(rawQuery, isLoadFromCloud)
            when (result) {
                is Success -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[CATEGORY_FEATURED_ORDER] = result.data
                        updatedList[CATEGORY_FEATURED_ORDER].isLoaded = true
                        updatedList[CATEGORY_FEATURED_ORDER].isSuccess = true
                        _homePageModel.value = updatedList
                    }
                }
                is Fail -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[CATEGORY_FEATURED_ORDER].isLoaded = true
                        updatedList[CATEGORY_FEATURED_ORDER].isSuccess = false
                        _homePageModel.value = updatedList
                        _isError.value = true
                    }
                }
            }
        }
    }

    companion object {
        const val SEARCH_PARAM_ORDER = 0
        const val DREAM_FUND_ORDER = 1
        const val CATEGORY_ORDER = 2
        const val CATEGORY_FEATURED_ORDER = 3
    }
}