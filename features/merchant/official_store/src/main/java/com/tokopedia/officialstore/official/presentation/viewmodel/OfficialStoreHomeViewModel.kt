package com.tokopedia.officialstore.official.presentation.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.officialstore.official.data.OfficialStoreBanners
import com.tokopedia.officialstore.official.domain.GetOfficialStoreHomeUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfficialStoreHomeViewModel @Inject constructor(
        private val getOfficialStoreBannersUseCase: GetOfficialStoreHomeUseCase,
        dispatchers: CoroutineDispatcher
) : BaseViewModel(dispatchers) {

    val officialStoreBannersResult: LiveData<Result<OfficialStoreBanners>> by lazy {
        _officialStoreBannersResult
    }

    private val _officialStoreBannersResult by lazy {
        MutableLiveData<Result<OfficialStoreBanners>>()
    }

    fun getOfficialStoreBanners(categoryId: String) {
        launchCatchError(block = {
            val response = withContext(Dispatchers.IO) {
                getOfficialStoreBannersUseCase.params = GetOfficialStoreHomeUseCase.createParams(categoryId.toIntOrNull() ?: 0)
                getOfficialStoreBannersUseCase.executeOnBackground()
            }
            _officialStoreBannersResult.value = Success(response)
        }) {
            _officialStoreBannersResult.value = Fail(it)
        }
    }
}