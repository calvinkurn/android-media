package com.tokopedia.discovery.categoryrevamp.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.discovery.categoryrevamp.data.bannedCategory.Data
import com.tokopedia.discovery.categoryrevamp.data.catalogModel.SearchCatalog
import com.tokopedia.discovery.categoryrevamp.domain.usecase.CatalogUseCase
import com.tokopedia.discovery.categoryrevamp.domain.usecase.DynamicFilterUseCase
import com.tokopedia.discovery.categoryrevamp.domain.usecase.SubCategoryV3UseCase
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class CategoryNavViewModel
@Inject constructor(var subCategoryV3UseCase: SubCategoryV3UseCase) : ViewModel() {

    var mBannedCheck = MutableLiveData<Result<Data>>()

    fun fetchBannedCheck(params: RequestParams) {
        subCategoryV3UseCase.execute(params,object : Subscriber<Data?>() {
            override fun onNext(data: Data?) {
                data?.let {
                    mBannedCheck.value = Success(data)
                }

            }
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                var data = Data()
                data.isBanned = 1;
                data.bannedMsgHeader = "Server Error"
                data.bannedMessage = "Try Again"
                mBannedCheck.value =  Success(data)
            }
        })
    }

    fun onDetach() {
        subCategoryV3UseCase.unsubscribe()
    }

}

