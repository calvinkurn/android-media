package com.tokopedia.brandlist.brandlist_category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.brandlist.brandlist_category.data.model.BrandlistCategories
import com.tokopedia.brandlist.brandlist_category.domain.GetBrandlistCategoriesUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject

class BrandlistCategoryViewModel @Inject constructor(
        private val getBrandlistCategoriesUseCase: GetBrandlistCategoriesUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val _brandlistCategoriesResponse = MutableLiveData<Result<BrandlistCategories>>()
    val brandlistCategoriesResponse: LiveData<Result<BrandlistCategories>>
            get() = _brandlistCategoriesResponse

    fun getBrandlistCategories() {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                val brandlistCategoriesResult = getBrandlistCategoriesUseCase.executeOnBackground()
                brandlistCategoriesResult.let {
                    _brandlistCategoriesResponse.postValue(Success(it))
                }
            }
        }) {
            _brandlistCategoriesResponse.value = Fail(it)
        }
    }
}