package com.tokopedia.discovery.categoryrevamp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.discovery.categoryrevamp.data.catalogModel.SearchCatalog
import com.tokopedia.discovery.categoryrevamp.domain.usecase.CatalogUseCase
import com.tokopedia.discovery.categoryrevamp.domain.usecase.DynamicFilterUseCase
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class CatalogNavViewModel
@Inject constructor(var catalogUseCase: CatalogUseCase,
                    var dynamicFilterUseCase: DynamicFilterUseCase) : ViewModel() {


    val mCatalog: MutableLiveData<Result<SearchCatalog>> = MutableLiveData()
    var mDynamicFilterModel = MutableLiveData<Result<DynamicFilterModel>>()
    val mCatalogCount = MutableLiveData<String>()


    fun fetchCatalogDetail(params: RequestParams) {
        catalogUseCase.execute(params, object : Subscriber<SearchCatalog>() {
            override fun onNext(searchCatalog: SearchCatalog) {
                mCatalog.value = Success(searchCatalog)
                mCatalogCount.value = (searchCatalog.count).toString()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                mCatalog.value = Fail(e)
            }
        })
    }

    fun fetchDynamicAttribute(params: RequestParams) {
        dynamicFilterUseCase.execute(params, object : Subscriber<DynamicFilterModel>() {
            override fun onNext(t: DynamicFilterModel?) {
                mDynamicFilterModel.value = Success(t as DynamicFilterModel)

            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                mDynamicFilterModel.value = (Fail(e))
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        catalogUseCase.unsubscribe()
        dynamicFilterUseCase.unsubscribe()
    }

}

