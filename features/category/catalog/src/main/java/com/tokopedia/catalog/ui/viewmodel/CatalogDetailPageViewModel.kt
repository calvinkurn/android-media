package com.tokopedia.catalog.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.catalog.ui.model.CatalogDetailUiModel
import com.tokopedia.catalog.ui.model.CatalogProductAtcUiModel
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.oldcatalog.usecase.detail.CatalogDetailUseCase
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class CatalogDetailPageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val catalogDetailUseCase: CatalogDetailUseCase,
    private val getNotificationUseCase: GetNotificationUseCase,
    private val userSession: UserSessionInterface,
    private val addToCartUseCase: AddToCartUseCase
) : BaseViewModel(dispatchers.main) {

    private val _errorsToaster = MutableLiveData<Throwable>()
    val errorsToaster: LiveData<Throwable>
        get() = _errorsToaster

    private val _errorsToasterGetComparison = MutableLiveData<Throwable>()
    val errorsToasterGetComparison: LiveData<Throwable>
        get() = _errorsToasterGetComparison

    private val _catalogDetailDataModel = MutableLiveData<Result<CatalogDetailUiModel>>()
    val catalogDetailDataModel: LiveData<Result<CatalogDetailUiModel>>
        get() = _catalogDetailDataModel

    private val _totalCartItem = MutableLiveData<Int>()
    val totalCartItem: LiveData<Int>
        get() = _totalCartItem

    private val _comparisonUiModel = MutableLiveData<ComparisonUiModel?>()
    val comparisonUiModel: LiveData<ComparisonUiModel?>
        get() = _comparisonUiModel

    private val _scrollEvents = MutableStateFlow("")
    val scrollEvents: Flow<String> = _scrollEvents.asStateFlow()

    var atcModel = CatalogProductAtcUiModel()

    fun isUserLoggedIn(): Boolean {
        return getUserId().isNotBlank()
    }

    fun getUserId(): String {
        return userSession.userId
    }

    fun getProductCatalog(catalogId: String, comparedCatalogId: String) {
        launchCatchError(
            dispatchers.io,
            block = {
                catalogDetailUseCase.getCatalogDetailV4(
                    catalogId,
                    comparedCatalogId,
                    _catalogDetailDataModel
                )
            },
            onError = {
                _catalogDetailDataModel.postValue(Fail(it))
            }
        )
    }

    fun getProductCatalogComparisons(catalogId: String, comparedCatalogIds: List<String>) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = catalogDetailUseCase.getCatalogDetailV4Comparison(
                    catalogId,
                    comparedCatalogIds
                )
                _comparisonUiModel.postValue(result)
            },
            onError = {
                _errorsToasterGetComparison.postValue(it)
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

    fun addProductToCart(atcUiModel: CatalogProductAtcUiModel) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = AddToCartRequestParams(
                    productId = atcUiModel.productId,
                    shopId = atcUiModel.shopId,
                    quantity = atcUiModel.quantity,
                    warehouseId = atcUiModel.warehouseId
                )
                addToCartUseCase.setParams(param)
                addToCartUseCase.executeOnBackground()
                refreshNotification()
            },
            onError = {
                _errorsToaster.postValue(it)
            }
        )
    }

    fun emitScrollEvent(y: String) {
        _scrollEvents.value = y
    }
}
