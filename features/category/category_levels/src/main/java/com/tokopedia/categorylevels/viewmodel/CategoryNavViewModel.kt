package com.tokopedia.categorylevels.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.common_category.data.catalogModel.CatalogListResponse
import com.tokopedia.common_category.model.bannedCategory.BannedCategoryResponse
import com.tokopedia.common_category.model.bannedCategory.Data
import com.tokopedia.common_category.usecase.repository.CategoryNavRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val IS_ADULT = 1
private const val IS_BANNED = 1

class CategoryNavViewModel @Inject constructor(val pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface?) : ViewModel(), CoroutineScope {
    private val jobs = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + jobs

    @Inject
    lateinit var categoryNavRepository: CategoryNavRepository

    private var categoryDetail = MutableLiveData<Result<Data>>()
    private var redirectionUrl = MutableLiveData<Result<String>>()
    private var adultProduct = MutableLiveData<Result<Data>>()
    private var hasCatalog = MutableLiveData<Boolean>()

    fun fetchCategoryDetail(departmentId: String) {
        launchCatchError(block = {
            pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
            pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
            val graphqlResponse = categoryNavRepository.getCategoryDetailWithCatalogCount(departmentId)
            pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
            pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
            graphqlResponse?.let {
                it.getData<CatalogListResponse>(CatalogListResponse::class.java).searchCatalog?.let { searchCatalog ->
                    hasCatalog.value = searchCatalog.count != 0
                }
                it.getData<BannedCategoryResponse>(BannedCategoryResponse::class.java).categoryDetailQuery?.data?.let { data ->
                    handleCategoryResponse(data)
                }
            }
        }, onError = {
            categoryDetail.value = Fail(it)
        })
    }

    private fun handleCategoryResponse(it: Data) {
        when {
            redirectionEnabled(it.appRedirectionURL) -> {
                handleForRedirectionIfEnabled(it.appRedirectionURL)
            }
            checkIfAdult(it.isAdult, it.isBanned) -> {
                handleForAdultProduct(it)
            }
            else -> {
                handleForCategoryDetail(it)
            }
        }
    }

    private fun handleForCategoryDetail(data: Data) {
        categoryDetail.value = Success(data)
    }

    private fun handleForAdultProduct(data: Data) {
        adultProduct.value = Success(data)
    }

    private fun handleForRedirectionIfEnabled(appRedirection: String?) {
        redirectionUrl.value = Success(appRedirection.toString())
    }

    private fun checkIfAdult(isAdult: Int, isBanned: Int): Boolean {
        return isAdult == IS_ADULT && isBanned != IS_BANNED
    }

    private fun redirectionEnabled(url: String?): Boolean {
        return url != null && url != ""
    }

    override fun onCleared() {
        jobs.cancel()
        super.onCleared()
    }

    fun getCategoryDetailLiveData(): LiveData<Result<Data>> {
        return categoryDetail
    }

    fun getRedirectionUrlLiveData(): LiveData<Result<String>> {
        return redirectionUrl
    }

    fun getAdultProductLiveData(): MutableLiveData<Result<Data>> {
        return adultProduct
    }

    fun getHasCatalogLiveData(): MutableLiveData<Boolean> {
        return hasCatalog
    }
}

