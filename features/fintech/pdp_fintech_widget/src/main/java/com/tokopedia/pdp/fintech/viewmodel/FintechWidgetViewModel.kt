package com.tokopedia.pdp.fintech.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pdp.fintech.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.pdp.fintech.domain.usecase.ProductDetailUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FintechWidgetViewModel @Inject constructor
    (@CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
     val productDetailUseCase:ProductDetailUseCase):
    BaseViewModel(dispatcher) {


        fun getProductDetail(productId:String)
        {

        }

}