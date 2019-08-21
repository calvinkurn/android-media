package com.tokopedia.discovery.categoryrevamp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.discovery.categoryrevamp.data.catalogModel.SearchCatalog
import com.tokopedia.discovery.categoryrevamp.domain.usecase.CatalogUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class CatalogNavViewModel @Inject constructor(var catalogUseCase: CatalogUseCase) : ViewModel() {


    val mCatalog: MutableLiveData<Result<SearchCatalog>> = MutableLiveData()


    fun fetchCatalogDetail(params: RequestParams) {
        catalogUseCase.execute(params, object : Subscriber<SearchCatalog>() {
            override fun onNext(searchCatalog: SearchCatalog) {
                mCatalog.value = Success(searchCatalog)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                mCatalog.value = Fail(e)
            }
        })
    }


}

