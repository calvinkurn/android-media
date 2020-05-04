package com.tokopedia.officialstore.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.officialstore.OfficialStoreDispatcherProvider
import com.tokopedia.officialstore.category.data.model.OfficialStoreCategories
import com.tokopedia.officialstore.category.domain.GetOfficialStoreCategoriesUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfficialStoreCategoryViewModel @Inject constructor(
        private val getOfficialStoreCategoriesUseCase: GetOfficialStoreCategoriesUseCase,
        private val dispatchers: OfficialStoreDispatcherProvider
) : BaseViewModel(dispatchers.ui()) {

    val officialStoreCategoriesResult: LiveData<Result<OfficialStoreCategories>>
        get() = _officialStoreCategoriesResult

    private val _officialStoreCategoriesResult by lazy {
        MutableLiveData<Result<OfficialStoreCategories>>()
    }

    fun getOfficialStoreCategories() {
        launchCatchError(block = {
            val response = withContext(dispatchers.io()) {
                getOfficialStoreCategoriesUseCase.executeOnBackground()
            }
            _officialStoreCategoriesResult.value = Success(response)
        }) {
            _officialStoreCategoriesResult.value = Fail(it)
        }
    }
}