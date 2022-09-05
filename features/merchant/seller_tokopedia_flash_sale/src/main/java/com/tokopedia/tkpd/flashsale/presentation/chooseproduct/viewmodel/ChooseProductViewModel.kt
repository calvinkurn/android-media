package com.tokopedia.tkpd.flashsale.presentation.chooseproduct.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductListToReserveUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ChooseProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleProductListToReserveUseCase: GetFlashSaleProductListToReserveUseCase
) : BaseViewModel(dispatchers.main){

    private val _productList = MutableLiveData<List<ChooseProductItem>>()
    val productList: LiveData<List<ChooseProductItem>> get() = _productList

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    fun getProductList(page: Int, offset: Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetFlashSaleProductListToReserveUseCase.Param(
                    campaignId = 885090,
                    filterKeyword = "",
                    row = 10,
                    offset = 1,
                )
                val result = getFlashSaleProductListToReserveUseCase.execute(param)
                _productList.postValue(result)
            },
            onError = { error ->
                _error.postValue(error)
            }
        )
    }
}