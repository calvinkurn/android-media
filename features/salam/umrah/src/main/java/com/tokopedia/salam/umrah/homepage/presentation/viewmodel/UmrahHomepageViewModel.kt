package com.tokopedia.salam.umrah.homepage.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import com.tokopedia.salam.umrah.homepage.data.*
import com.tokopedia.salam.umrah.common.usecase.UmrahSearchParameterUseCase
import com.tokopedia.salam.umrah.common.usecase.UmrahTravelAgentsUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.salam.umrah.homepage.presentation.usecase.*
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
                                                 private val umrahHomepageBannerUseCase : UmrahHomepageBannerUseCase,
                                                 private val umrahTravelAgentsUseCase: UmrahTravelAgentsUseCase,
                                                 dispatcher: CoroutineDispatchers) : BaseViewModel(dispatcher.main) {


    private val homePageModelMutable = MutableLiveData<List<UmrahHomepageModel>>()
    val homePageModel: LiveData<List<UmrahHomepageModel>>
        get() = homePageModelMutable

    private val isErrorMutable = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = isErrorMutable


    fun getIntialList(isLoadFromCloud: Boolean) {
        val list: List<UmrahHomepageModel> = requestEmptyViewModels(isLoadFromCloud)
        homePageModelMutable.value = list
        isErrorMutable.value = false

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
                        homePageModelMutable.value = updatedList
                    }
                }

                is Fail -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[SEARCH_PARAM_ORDER].isLoaded = true
                        updatedList[SEARCH_PARAM_ORDER].isSuccess = false
                        homePageModelMutable.value = updatedList
                        isErrorMutable.value = true
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
                        homePageModelMutable.value = updatedList
                    }
                }

                is Fail -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[DREAM_FUND_ORDER].isLoaded = true
                        updatedList[DREAM_FUND_ORDER].isSuccess = false
                        homePageModelMutable.value = updatedList
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
                        homePageModelMutable.value = updatedList
                    }
                }
                is Fail -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[CATEGORY_ORDER].isLoaded = true
                        updatedList[CATEGORY_ORDER].isSuccess = false
                        homePageModelMutable.value = updatedList
                        isErrorMutable.value = true
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
                        homePageModelMutable.value = updatedList
                    }
                }
                is Fail -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[CATEGORY_FEATURED_ORDER].isLoaded = true
                        updatedList[CATEGORY_FEATURED_ORDER].isSuccess = false
                        homePageModelMutable.value = updatedList
                        isErrorMutable.value = true
                    }
                }
            }
        }
    }


    fun getBannerData(rawQuery: String, isLoadFromCloud: Boolean) {
        launch {
            val result = umrahHomepageBannerUseCase.executeBanner(rawQuery, isLoadFromCloud)
            when (result) {
                is Success -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[BANNER_ORDER] = result.data
                        updatedList[BANNER_ORDER].isLoaded = true
                        updatedList[BANNER_ORDER].isSuccess = true
                        homePageModelMutable.value = updatedList
                    }
                }
                is Fail -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[BANNER_ORDER].isLoaded = true
                        updatedList[BANNER_ORDER].isSuccess = false
                        homePageModelMutable.value = updatedList
                        isErrorMutable.value = true
                    }
                }
            }
        }
    }

    fun getPartnerTravelData(rawQuery: String, isLoadFromCloud: Boolean) {
        val flags = listOf(FLAGS_PARTNER)
        launch {
            val result = umrahTravelAgentsUseCase.executeUseCase(rawQuery,
                    isLoadFromCloud, 1,6, flags)
            when (result) {
                is Success -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[PARTNER_TRAVEL_ORDER] = result.data
                        updatedList[PARTNER_TRAVEL_ORDER].isLoaded = true
                        updatedList[PARTNER_TRAVEL_ORDER].isSuccess = true
                        homePageModelMutable.value = updatedList
                    }
                }
                is Fail -> {
                    homePageModel.value?.let {
                        val updatedList = it.toMutableList()
                        updatedList[PARTNER_TRAVEL_ORDER].isLoaded = true
                        updatedList[PARTNER_TRAVEL_ORDER].isSuccess = false
                        homePageModelMutable.value = updatedList
                        isErrorMutable.value = true
                    }
                }
            }
        }
    }

    fun requestEmptyViewModels(isLoadedFromCloud: Boolean): List<UmrahHomepageModel> {
        val umrahSearchParam = UmrahSearchParameterEntity()
        umrahSearchParam.isLoadFromCloud = isLoadedFromCloud

        val umrahDreamFund = UmrahHomepageMyUmrahEntity()
        umrahDreamFund.isLoadFromCloud = isLoadedFromCloud

        val umrahCategory = UmrahHomepageCategoryEntity()
        umrahCategory.isLoadFromCloud = isLoadedFromCloud

        val umrahCategoryFeatured = UmrahHomepageCategoryFeaturedEntity()
        umrahCategoryFeatured.isLoadFromCloud = isLoadedFromCloud

        val umrahBanner = UmrahHomepageBannerEntity()
        umrahBanner.isLoadFromCloud = isLoadedFromCloud

        val umrahPartnerTravel = UmrahTravelAgentsEntity()
        umrahPartnerTravel.isLoadFromCloud = isLoadedFromCloud

        return listOf(umrahSearchParam,
                umrahBanner,
                umrahDreamFund,
                umrahCategory,
                umrahCategoryFeatured,
                umrahPartnerTravel
        )
    }

    companion object {
        const val SEARCH_PARAM_ORDER = 0
        const val BANNER_ORDER = 1
        const val DREAM_FUND_ORDER = 2
        const val CATEGORY_ORDER = 3
        const val CATEGORY_FEATURED_ORDER = 4
        const val PARTNER_TRAVEL_ORDER = 5

        const val FLAGS_PARTNER = "TRAVEL_AGENT_FEATURED_ON_HOMEPAGE"
    }
}