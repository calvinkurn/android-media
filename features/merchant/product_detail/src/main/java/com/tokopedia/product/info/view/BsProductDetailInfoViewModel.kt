package com.tokopedia.product.info.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.view.util.DynamicProductDetailDispatcherProvider
import com.tokopedia.product.detail.view.util.asFail
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.product.info.model.productdetail.response.PdpGetDetailBottomSheet
import com.tokopedia.product.info.usecase.GetProductDetailBottomSheetUseCase
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject


/**
 * Created by Yehezkiel on 13/10/20
 */
class BsProductDetailInfoViewModel @Inject constructor(dispatchers: DynamicProductDetailDispatcherProvider,
                                                       private val getProductDetailBottomSheetUseCase: GetProductDetailBottomSheetUseCase)
    : BaseViewModel(dispatchers.io()) {

    private val productShopId = MutableLiveData<Pair<String, String>>()

    private val _bottomSheetDetailData = MediatorLiveData<Result<PdpGetDetailBottomSheet>>()
    val bottomSheetDetailData: LiveData<Result<PdpGetDetailBottomSheet>>
        get() = _bottomSheetDetailData

    init {
        _bottomSheetDetailData.addSource(productShopId) {
            launchCatchError(block = {
                val requestParams = GetProductDetailBottomSheetUseCase.createParams(it.first, it.second)
                _bottomSheetDetailData.postValue(getProductDetailBottomSheetUseCase.executeOnBackground(requestParams).asSuccess())
            }) {
                _bottomSheetDetailData.postValue(it.asFail())
            }
        }
    }

    fun setParams(productId: String, shopId: String) {
        productShopId.value = productId to shopId
    }
}