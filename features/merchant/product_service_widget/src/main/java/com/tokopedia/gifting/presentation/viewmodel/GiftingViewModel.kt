package com.tokopedia.gifting.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gifting.domain.model.GetAddOnByProduct
import com.tokopedia.gifting.domain.usecase.GetAddOnUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.network.exception.MessageErrorException
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GiftingViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getAddOnUseCase: GetAddOnUseCase
) : BaseViewModel(dispatchers.main) {

    private val mGetWarehouseIdResult = MutableLiveData<String>()
    val getWarehouseIdResult: LiveData<String> get() = mGetWarehouseIdResult

    private val mGetAddOnByProduct = MutableLiveData<GetAddOnByProduct>()
    val getAddOnByProduct: LiveData<GetAddOnByProduct> get() = mGetAddOnByProduct

    private val mErrorThrowable = MutableLiveData<Throwable>()
    val errorThrowable: LiveData<Throwable> get() = mErrorThrowable

    fun getWarehouseId(context: Context) {
        val addressData = ChooseAddressUtils.getLocalizingAddressData(context)
        if (addressData != null) {
            mGetWarehouseIdResult.value = addressData.warehouse_id
        }
    }

    fun getAddOn(productId: Long, warehouseId: String) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getAddOnUseCase.setParams(productId.toString(), warehouseId)
                getAddOnUseCase.executeOnBackground().data.getAddOnByProduct
            }
            mGetAddOnByProduct.value = result
            if (result.error.messages.isNotEmpty()) {
                mErrorThrowable.value = MessageErrorException(result.error.messages)
            }
        }, onError = {
            mErrorThrowable.value = it
        })
    }
}