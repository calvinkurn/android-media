package com.tokopedia.catalog.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.catalog.ui.model.CatalogDetailUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.oldcatalog.usecase.detail.CatalogDetailUseCase
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class CatalogDetailPageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val catalogDetailUseCase: CatalogDetailUseCase,
    private val getNotificationUseCase: GetNotificationUseCase
) : BaseViewModel(dispatchers.main) {

    private val _errorsToaster = MutableLiveData<Throwable>()
    val errorsToaster: LiveData<Throwable>
        get() = _errorsToaster

    private val _catalogDetailDataModel = MutableLiveData<Result<CatalogDetailUiModel>>()
    val catalogDetailDataModel: LiveData<Result<CatalogDetailUiModel>>
        get() = _catalogDetailDataModel

    private val _totalCartItem = MutableLiveData<Int>()
    val totalCartItem: LiveData<Int>
        get() = _totalCartItem

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

    fun refreshNotification() {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getNotificationUseCase.executeOnBackground()
                _totalCartItem.postValue(result.totalCart)
            },
            onError = {
                _errorsToaster.postValue(it)
            }
        )
    }
}
