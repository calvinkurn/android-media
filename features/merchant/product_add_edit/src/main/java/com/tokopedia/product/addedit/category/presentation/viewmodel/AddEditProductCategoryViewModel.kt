package com.tokopedia.product.addedit.category.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.core.common.category.domain.interactor.GetCategoryLiteTreeUseCase
import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditProductCategoryViewModel @Inject constructor(
        private val coroutineDispatcher: CoroutineDispatchers,
        private val getCategoryLiteTreeUseCase: GetCategoryLiteTreeUseCase
): BaseViewModel(coroutineDispatcher.main) {

    private val _categoryLiteTree = MutableLiveData<Result<CategoriesResponse>>()
    val categoryLiteTree get() = _categoryLiteTree

    fun getCategoryLiteTree() {
        launchCatchError(coroutineDispatcher.io, block = {
            val category = Success(getCategoryLiteTreeUseCase.createObservable(GetCategoryLiteTreeUseCase.createRequestParams()).toBlocking().first())
            _categoryLiteTree.postValue(category)
        }) {
            _categoryLiteTree.postValue(Fail(it))
        }
    }

}