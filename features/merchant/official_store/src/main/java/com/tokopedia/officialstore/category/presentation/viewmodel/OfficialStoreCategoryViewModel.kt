package com.tokopedia.officialstore.category.presentation.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.officialstore.category.data.model.OfficialStoreCategories
import com.tokopedia.officialstore.category.domain.GetOfficialStoreCategoriesUseCase
import com.tokopedia.officialstore.category.presentation.data.OSChooseAddressData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfficialStoreCategoryViewModel @Inject constructor(
        private val getOfficialStoreCategoriesUseCase: GetOfficialStoreCategoriesUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    val officialStoreCategoriesResult: LiveData<Result<OfficialStoreCategories>>
        get() = _officialStoreCategoriesResult

    private val _officialStoreCategoriesResult by lazy {
        MutableLiveData<Result<OfficialStoreCategories>>()
    }

    fun removeObservers(lifecyclerOwner: LifecycleOwner) {
        officialStoreCategoriesResult.removeObservers(lifecyclerOwner)
    }

    fun getOfficialStoreCategories(doQueryHashing: Boolean,
                                   onCacheStartLoad: () -> Unit = {},
                                   onCacheStopLoad: () -> Unit = {},
                                   onCloudStartLoad: () -> Unit = {},
                                   onCloudStopLoad: () -> Unit = {}) {
        launchCatchError(block = {
            onCacheStartLoad.invoke()
            onCloudStartLoad.invoke()
            val cacheResponse = async(dispatchers.io) {
                getOfficialStoreCategoriesUseCase.executeOnBackground(true, doQueryHashing)
            }
            val cloudResponse = async(dispatchers.io) {
                getOfficialStoreCategoriesUseCase.executeOnBackground(false, doQueryHashing)
            }
            val cacheData = cacheResponse.await()
            cacheData.isCache = true
            _officialStoreCategoriesResult.value = Success(cacheData)
            onCacheStopLoad.invoke()
            _officialStoreCategoriesResult.value = Success(cloudResponse.await())
            onCloudStopLoad.invoke()
        }) {
            _officialStoreCategoriesResult.value = Fail(it)
        }
    }
}