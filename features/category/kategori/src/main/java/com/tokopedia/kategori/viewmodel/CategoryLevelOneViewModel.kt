package com.tokopedia.kategori.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.common_category.data.kategorymodel.CategoryAllList
import com.tokopedia.kategori.view.PerformanceMonitoringListener
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.tokopedia.kategori.repository.KategoriRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import androidx.lifecycle.LiveData


private const val KEY_DEPTH = "depth"
private const val KEY_IS_TRENDING = "isTrending"

class CategoryLevelOneViewModel @Inject constructor(val kategoriRepository: KategoriRepository) : ViewModel() {

    private var categoryDepth = 2
    var categoryListLiveData = MutableLiveData<Result<CategoryAllList>>()

    fun bound(performanceMonitoringListener: PerformanceMonitoringListener?) {
        performanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
        performanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
        viewModelScope.launchCatchError(
                block = {
                    val response = kategoriRepository.getCategoryListItems(createRequestParams(categoryDepth, true).parameters)
                    response?.let {
                        performanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
                        performanceMonitoringListener?.startRenderPerformanceMonitoring()
                        categoryListLiveData.value = Success(it)
                    }
                },
                onError = {
                    categoryListLiveData.value = Fail(it)
                }
        )
    }

    fun getCategoryList(): LiveData<Result<CategoryAllList>> {
        return categoryListLiveData
    }

    fun createRequestParams(depth: Int, isTrending: Boolean): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putInt(KEY_DEPTH, depth)
        requestParams.putBoolean(KEY_IS_TRENDING, isTrending)
        return requestParams
    }

}