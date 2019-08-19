package com.tokopedia.discovery.categoryrevamp.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.tokopedia.discovery.categoryrevamp.data.catalogModel.SearchCatalog
import com.tokopedia.discovery.categoryrevamp.domain.usecase.CatalogUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class CatalogNavViewModel @Inject constructor(var catalogUseCase: CatalogUseCase) : ViewModel() {


    val mCatalog: MutableLiveData<Result<SearchCatalog>> = MutableLiveData()


    fun fetchCatalogDetail(params: RequestParams) {
        catalogUseCase.execute(params, object : Subscriber<SearchCatalog>() {
            override fun onNext(searchCatalog: SearchCatalog) {
                mCatalog.value = Success(searchCatalog as SearchCatalog)
                Log.d("CatalogNavViewModel", "onNext")
            }

            override fun onCompleted() {
                Log.d("CatalogNavViewModel", "onCompleted")

            }

            override fun onError(e: Throwable?) {
                Log.d("CatalogNavViewModel", "onError")
            }
        })
    }


}

