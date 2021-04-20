package com.tokopedia.product.manage.feature.etalase.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product.manage.feature.etalase.data.model.EtalaseViewModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EtalasePickerViewModel @Inject constructor(
    private val getEtalaseUseCase: GetShopEtalaseByShopUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    companion object {
        // Currently update data on server is not realtime.
        // Client need to add request delay in order to receive updated data.
        private const val REQUEST_DELAY = 1000L
    }

    var selectedEtalase: EtalaseViewModel? = null

    val getEtalaseResult: LiveData<Result<List<EtalaseViewModel>>>
        get() = _getEtalaseResult

    private val _getEtalaseResult = MutableLiveData<Result<List<EtalaseViewModel>>>()

    fun getEtalaseList(shopId: String, withDelay: Boolean = false) {
        launchCatchError(block = {
            val etalaseList = withContext(dispatchers.io) {
                if(withDelay) { delay(REQUEST_DELAY) }
                val requestParams = GetShopEtalaseByShopUseCase.createRequestParams(
                    shopId = shopId,
                    hideNoCount = false,
                    hideShowCaseGroup = true,
                    isOwner = true
                )
                getEtalaseUseCase.createObservable(requestParams)
                    .toBlocking().first()
                    .mapIndexed { index, product ->
                        EtalaseViewModel(product.id, product.name, index)
                    }
            }

            _getEtalaseResult.value = Success(etalaseList)
        }, onError = {
            _getEtalaseResult.value = Fail(it)
        })
    }

    fun clearGetEtalaseCache() {
        getEtalaseUseCase.clearCache()
    }
}