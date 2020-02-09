package com.tokopedia.brandlist.brandlist_category.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.brandlist.brandlist_category.data.model.BrandlistCategories
import com.tokopedia.brandlist.brandlist_category.domain.GetBrandlistCategoriesUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject

class BrandlistCategoryViewModel @Inject constructor(
        private val getBrandlistCategoriesUseCase: GetBrandlistCategoriesUseCase,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    val brandlistCategoriesResponse = MutableLiveData<Result<BrandlistCategories>>()

    fun getBrandlistCategories() {
        launchCatchError(block = {
            coroutineScope {
                launch(Dispatchers.IO) {
                    val brandlistCategoriesResult = getBrandlistCategoriesData()
                    brandlistCategoriesResult.let {
                        brandlistCategoriesResponse.postValue(Success(it))
                    }
                }
            }
        }) {
            brandlistCategoriesResponse.value = Fail(it)
        }
    }

    private suspend fun getBrandlistCategoriesData(): BrandlistCategories {
        var brandlistCategoriesResult = BrandlistCategories()
        try {
            brandlistCategoriesResult = getBrandlistCategoriesUseCase.executeOnBackground()
        } catch (t: Throwable) {
        }
        return brandlistCategoriesResult
    }

}