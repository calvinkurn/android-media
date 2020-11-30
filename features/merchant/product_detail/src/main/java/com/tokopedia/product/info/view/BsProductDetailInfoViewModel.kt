package com.tokopedia.product.info.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.detail.view.util.asFail
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoVisitable
import com.tokopedia.product.info.usecase.GetProductDetailBottomSheetUseCase
import com.tokopedia.product.info.util.ProductDetailInfoMapper
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


/**
 * Created by Yehezkiel on 13/10/20
 */
class BsProductDetailInfoViewModel @Inject constructor(dispatchers: CoroutineDispatchers,
                                                       private val getProductDetailBottomSheetUseCase: GetProductDetailBottomSheetUseCase,
                                                       val userSession: UserSessionInterface)
    : BaseViewModel(dispatchers.io) {

    private val parcelData = MutableLiveData<ProductInfoParcelData>()

    val bottomSheetDetailData: LiveData<Result<List<ProductDetailInfoVisitable>>> = Transformations.switchMap(parcelData) {
        val bottomSheetData = MutableLiveData<Result<List<ProductDetailInfoVisitable>>>()
        launchCatchError(block = {
            val requestParams = GetProductDetailBottomSheetUseCase.createParams(it.productId, it.shopId)
            val responseData = getProductDetailBottomSheetUseCase.executeOnBackground(requestParams, it.forceRefresh)
            val visitableData = ProductDetailInfoMapper.generateVisitable(responseData, it)

            bottomSheetData.postValue(visitableData.asSuccess())
        }) {
            bottomSheetData.postValue(it.asFail())
        }
        bottomSheetData
    }

    fun setParams(parcelData: ProductInfoParcelData) {
        this.parcelData.value = parcelData
    }
}