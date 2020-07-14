package com.tokopedia.product.manage.feature.list.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.manage.common.draft.domain.usecase.GetAllProductsCountDraftUseCase
import com.tokopedia.product.manage.feature.list.domain.ClearAllDraftProductUseCase
import com.tokopedia.product.manage.item.main.draft.domain.UpdateUploadingDraftProductUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductDraftListCountViewModel @Inject constructor(
    private val getAllProductsCountDraftUseCase: GetAllProductsCountDraftUseCase,
    private val clearAllDraftProductUseCase: ClearAllDraftProductUseCase,
    private val updateUploadingDraftProductUseCase: UpdateUploadingDraftProductUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val getAllDraftCountResult
        get() = _getAllDraftCountResult

    private val _getAllDraftCountResult = MutableLiveData<Result<Long>>()

    fun getAllDraftCount() {
        launchCatchError(block = {
            val draftCount = withContext(dispatchers.io) {
                getAllProductsCountDraftUseCase.getData(RequestParams.EMPTY)
            }
            _getAllDraftCountResult.value = Success(draftCount)
        }) {
            _getAllDraftCountResult.value = Fail(it)
        }
    }

    fun fetchAllDraftCountWithUpdateUploading() {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                val params = UpdateUploadingDraftProductUseCase.createRequestParamsUpdateAll(false)
                updateUploadingDraftProductUseCase.getData(params)
            }
            getAllDraftCount()
        }) {
            getAllDraftCount()
        }
    }

    fun clearAllDraft() {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                clearAllDraftProductUseCase.getData(RequestParams.EMPTY)
            }
        }) {
            // do nothing
        }
    }

    fun detachView() {
        getAllProductsCountDraftUseCase.unsubscribe()
        clearAllDraftProductUseCase.unsubscribe()
        updateUploadingDraftProductUseCase.unsubscribe()
    }
}