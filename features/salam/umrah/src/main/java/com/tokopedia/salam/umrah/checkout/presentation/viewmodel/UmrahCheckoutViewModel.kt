package com.tokopedia.salam.umrah.checkout.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutMapperEntity
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutResultEntity
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutResultParams
import com.tokopedia.salam.umrah.checkout.presentation.usecase.UmrahCheckoutGetDataUseCase
import com.tokopedia.salam.umrah.checkout.presentation.usecase.UmrahCheckoutResultUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * @author by firman on 4/11/2019
 */

class UmrahCheckoutViewModel @Inject constructor(val umrahCheckoutGetDataUseCase: UmrahCheckoutGetDataUseCase,
                                                 val umrahCheckoutResultUseCase: UmrahCheckoutResultUseCase,
                                                 coroutineDispatcher: CoroutineDispatchers)
    : BaseViewModel(coroutineDispatcher.main) {

    private val checkoutMappedMutable = MutableLiveData<Result<UmrahCheckoutMapperEntity>>()
    val checkoutMapped: LiveData<Result<UmrahCheckoutMapperEntity>>
        get() = checkoutMappedMutable

    private val checkoutResultMutable = MutableLiveData<Result<UmrahCheckoutResultEntity>>()
    val checkoutResult: LiveData<Result<UmrahCheckoutResultEntity>>
        get() = checkoutResultMutable

    fun getDataCheckout(rawQueryPDP: String, rawQuerySummaryPayment: String, rawQueryOptionPayment: String,
                rawQueryTermCondition: String, slugName: String, variantId: String,
                pilgrimsCount: Int, price: Int, departDate: String, idTermCondition: String,downPaymentPrice:Int) {
        launch {
            val result = umrahCheckoutGetDataUseCase.execute(
                    rawQueryPDP, rawQuerySummaryPayment, rawQueryOptionPayment,
                    rawQueryTermCondition, slugName, variantId, pilgrimsCount,
                    price, departDate, idTermCondition,downPaymentPrice)
            checkoutMappedMutable.value = result

        }
    }

    fun executeCheckout(rawQuery: String, umrahCheckoutResultParams: UmrahCheckoutResultParams){
        launch {
            val result = umrahCheckoutResultUseCase.execute(rawQuery, umrahCheckoutResultParams)
            checkoutResultMutable.value = result
        }
    }

}
