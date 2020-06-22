package com.tokopedia.kategori.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.kategori.model.CategoryAllList
import com.tokopedia.kategori.subscriber.CategoryLevelOneSubscriber
import com.tokopedia.kategori.usecase.AllCategoryQueryUseCase
import com.tokopedia.kategori.view.PerformanceMonitoringListener
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class CategoryLevelOneViewModel @Inject constructor(private var getCategoryListUseCase: AllCategoryQueryUseCase) : ViewModel() {

    private var categoryDepth = 2
    var categoryListLiveData: LiveData<Result<CategoryAllList>>? = null

    fun bound(performanceMonitoringListener: PerformanceMonitoringListener?) {
        performanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
        performanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
        val subscriber = getSubscriber(performanceMonitoringListener)
        categoryListLiveData = subscriber.getCategoryList()
        getCategoryListUseCase.execute(getCategoryListUseCase.createRequestParams(categoryDepth, true), subscriber)
    }

    internal fun getSubscriber(performanceMonitoringListener: PerformanceMonitoringListener?): CategoryLevelOneSubscriber {
        return CategoryLevelOneSubscriber(performanceMonitoringListener)
    }

    override fun onCleared() {
        super.onCleared()
        getCategoryListUseCase.unsubscribe()
    }
}