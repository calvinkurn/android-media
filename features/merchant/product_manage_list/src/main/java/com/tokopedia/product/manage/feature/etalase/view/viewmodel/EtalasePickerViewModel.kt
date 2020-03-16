package com.tokopedia.product.manage.feature.etalase.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.manage.feature.etalase.data.model.EtalaseViewModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EtalasePickerViewModel @Inject constructor(
    private val getEtalaseUseCase: GetShopEtalaseByShopUseCase,
    mainDispatcher: CoroutineDispatcher
): BaseViewModel(mainDispatcher) {

    var selectedEtalase: EtalaseViewModel? = null

    val getEtalaseResult: LiveData<Result<List<EtalaseViewModel>>>
        get() = _getEtalaseResult

    private val _getEtalaseResult = MutableLiveData<Result<List<EtalaseViewModel>>>()

    fun getEtalaseList(shopId: String) {
        launchCatchError(block = {
            val etalaseList = withContext(Dispatchers.IO) {
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