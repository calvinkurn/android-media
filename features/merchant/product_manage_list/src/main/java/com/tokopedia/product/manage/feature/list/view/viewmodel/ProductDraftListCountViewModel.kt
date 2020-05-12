package com.tokopedia.product.manage.feature.list.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product.manage.common.draft.domain.usecase.GetAllProductsCountDraftUseCase
import com.tokopedia.product.manage.feature.list.domain.ClearAllDraftProductUseCase
import com.tokopedia.product.manage.feature.list.domain.FetchAllDraftProductCountUseCase
import com.tokopedia.product.manage.item.main.draft.domain.UpdateUploadingDraftProductUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class ProductDraftListCountViewModel @Inject constructor(
    private val fetchAllDraftProductCountUseCase: FetchAllDraftProductCountUseCase,
    private val getAllProductsCountDraftUseCase: GetAllProductsCountDraftUseCase,
    private val clearAllDraftProductUseCase: ClearAllDraftProductUseCase,
    private val updateUploadingDraftProductUseCase: UpdateUploadingDraftProductUseCase
): ViewModel() {

    val getAllDraftCountResult
        get() = _getAllDraftCountResult

    private val _getAllDraftCountResult = MutableLiveData<Result<Long>>()

    fun getAllDraftCount() {
        getAllProductsCountDraftUseCase.execute(getSubscriber())
    }

    fun fetchAllDraftCountWithUpdateUploading() {
        val params = UpdateUploadingDraftProductUseCase.createRequestParamsUpdateAll(false)
        updateUploadingDraftProductUseCase.execute(params, getUploadingSubscriber())
    }

    fun clearAllDraft() {
        clearAllDraftProductUseCase.execute(RequestParams.EMPTY, object : Subscriber<Boolean>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {}

            override fun onNext(aBoolean: Boolean) {}
        })
    }

    private fun getSubscriber(): Subscriber<Long> {
        return object : Subscriber<Long>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                _getAllDraftCountResult.value = Fail(e)
            }

            override fun onNext(rowCount: Long) {
                _getAllDraftCountResult.value = Success(rowCount)
            }
        }
    }

    fun detachView() {
        fetchAllDraftProductCountUseCase.unsubscribe()
        clearAllDraftProductUseCase.unsubscribe()
        updateUploadingDraftProductUseCase.unsubscribe()
    }

    private fun getUploadingSubscriber(): Subscriber<Boolean> {
        return object : Subscriber<Boolean>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                getAllDraftCount()
            }

            override fun onNext(aBoolean: Boolean) {
                getAllDraftCount()
            }
        }
    }
}