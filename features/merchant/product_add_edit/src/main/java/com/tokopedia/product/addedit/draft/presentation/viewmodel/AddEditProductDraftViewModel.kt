package com.tokopedia.product.addedit.draft.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.draft.domain.usecase.DeleteAllProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.DeleteProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.GetAllProductDraftUseCase
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AddEditProductDraftViewModel @Inject constructor(
        private val coroutineDispatcher: CoroutineDispatchers,
        private val deleteProductDraftUseCase: DeleteProductDraftUseCase,
        private val deleteAllProductDraftUseCase: DeleteAllProductDraftUseCase,
        private val getAllProductDraftUseCase: GetAllProductDraftUseCase
) : BaseViewModel(coroutineDispatcher.main) {

    private val _drafts = MutableLiveData<Result<List<ProductDraft>>>()
    private val _deleteDraft = MutableLiveData<Result<Boolean>>()
    private val _deleteAllDraft = MutableLiveData<Result<Boolean>>()

    val drafts: LiveData<Result<List<ProductDraft>>>
        get() = _drafts
    val deleteDraft: LiveData<Result<Boolean>>
        get() = _deleteDraft
    val deleteAllDraft: LiveData<Result<Boolean>>
        get() = _deleteAllDraft

    fun getAllProductDraft() {
        launchCatchError(coroutineDispatcher.io, {
            _drafts.postValue(Success(getAllProductDraftUseCase.executeOnBackground()))
        }, onError = {
            _drafts.postValue(Fail(it))
        })
    }

    fun deleteProductDraft(draftId: Long) {
        launchCatchError(coroutineDispatcher.io, {
            deleteProductDraftUseCase.params = DeleteProductDraftUseCase.createRequestParams(draftId)
            _deleteDraft.postValue(Success(deleteProductDraftUseCase.executeOnBackground()))
        }, onError = {
            _deleteDraft.postValue(Fail(it))
        })
    }

    fun deleteAllProductDraft() {
        launchCatchError(coroutineDispatcher.io, {
            _deleteAllDraft.postValue(Success(deleteAllProductDraftUseCase.executeOnBackground()))
        }, onError = {
            _deleteAllDraft.postValue(Fail(it))
        })
    }
}