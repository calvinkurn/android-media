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

    private val _errorPage = MutableLiveData<Throwable>()
    val errorPage: LiveData<Throwable>
        get() = _errorPage

    private val _usingV4AboveLayout = MutableLiveData<Boolean>()
    val usingV4AboveLayout: LiveData<Boolean>
        get() = _usingV4AboveLayout

    fun getProductCatalogVersion(catalogId: String) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = catalogDetailUseCase.initializeGetCatalogDetail(catalogId)
                _usingV4AboveLayout.postValue(result)
            },
            onError = {
                _errorPage.postValue(it)
            }
        )
    }
}
