package com.tokopedia.tkpd.flashsale.presentation.chooseproduct.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductListToReserveUseCase
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.MAX_PER_PAGE
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

    val hasNextPage: Boolean get() = productList.value?.size == MAX_PER_PAGE

    fun getProductList(page: Int, perPage: Int, keyword: String) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetFlashSaleProductListToReserveUseCase.Param(
                    campaignId = 885090,
                    filterKeyword = keyword,
                    row = perPage,
                    offset = page,
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