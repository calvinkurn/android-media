package com.tokopedia.kategori.subscriber

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kategori.model.CategoryAllList
import com.tokopedia.kategori.view.PerformanceMonitoringListener
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import com.tokopedia.usecase.coroutines.Result

class CategoryLevelOneSubscriber(private val performanceMonitoringListener: PerformanceMonitoringListener?) : Subscriber<CategoryAllList>() {

    var categoryListLiveData = MutableLiveData<Result<CategoryAllList>>()

    override fun onNext(categoryAllList: CategoryAllList?) {
        performanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
        performanceMonitoringListener?.startRenderPerformanceMonitoring()
        categoryListLiveData.value = Success(categoryAllList as CategoryAllList)
    }


    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        categoryListLiveData.value = Fail(e)
    }


    fun getCategoryList(): LiveData<Result<CategoryAllList>> {
        return categoryListLiveData
    }

}