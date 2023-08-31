package com.tokopedia.catalog.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.catalog.ui.model.CatalogDetailUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.oldcatalog.usecase.detail.CatalogDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class CatalogDetailPageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private var catalogDetailUseCase: CatalogDetailUseCase
) : BaseViewModel(dispatchers.main) {

    private val _errorsToaster = MutableLiveData<Throwable>()
    val errorsToaster: LiveData<Throwable>
        get() = _errorsToaster

    private val _catalogDetailDataModel = MutableLiveData<Result<CatalogDetailUiModel>>()
    val catalogDetailDataModel: LiveData<Result<CatalogDetailUiModel>>
        get() = _catalogDetailDataModel

    fun getProductCatalog(catalogId: String,comparedCatalogId : String,userId : String, device : String) {
        launchCatchError(
            dispatchers.io,
            block = {
                catalogDetailUseCase.getCatalogReimagineDetail(
                    catalogId,
                    comparedCatalogId,
                    userId,
                    device,
                    _catalogDetailDataModel
                )
            },
            onError = {
                _catalogDetailDataModel.postValue(Fail(it))
            }
        )
    }
}
