package com.tokopedia.shopdiscount.manage.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.manage.data.response.GetSlashPriceProductListResponse
import com.tokopedia.shopdiscount.manage.domain.usecase.GetSlashPriceProductListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductManageViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSlashPriceProductListUseCase: GetSlashPriceProductListUseCase
) : BaseViewModel(dispatchers.main) {

 /*   private val _products = MutableLiveData<List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct>>()
    val products: LiveData<List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct>>
        get() = _products
*/

    private val _products = MutableLiveData<Result<GetSlashPriceProductListResponse>>()
    val products: LiveData<Result<GetSlashPriceProductListResponse>>
        get() = _products

    fun getSlashPriceProducts() {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getSlashPriceProductListUseCase.execute(page = 1)
            }
            _products.value = Success(result)
        }, onError = {
            _products.value = Fail(it)
        })
    }
}