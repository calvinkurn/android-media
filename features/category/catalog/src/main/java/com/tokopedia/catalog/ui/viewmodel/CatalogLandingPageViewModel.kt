package com.tokopedia.catalog.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.oldcatalog.usecase.detail.CatalogDetailUseCase
import javax.inject.Inject

class CatalogLandingPageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val catalogDetailUseCase: CatalogDetailUseCase
) : BaseViewModel(dispatchers.main) {

    private val _errorsToaster = MutableLiveData<Throwable>()
    val errorsToaster: LiveData<Throwable>
        get() = _errorsToaster

    private val _catalogVersion = MutableLiveData<Int>()
    val catalogVersion: LiveData<Int>
        get() = _catalogVersion

    fun getProductCatalogVersion(catalogId: String) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = catalogDetailUseCase.initializeGetCatalogDetail(catalogId)
                _catalogVersion.postValue(result)
            },
            onError = {
                _errorsToaster.postValue(it)
            }
        )
    }
}
