package com.tokopedia.salam.umrah.checkout.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.data.entity.TravelContactListModel
import com.tokopedia.common.travel.data.entity.TravelUpsertContactModel
import com.tokopedia.common.travel.domain.GetContactListUseCase
import com.tokopedia.common.travel.domain.UpsertContactListUseCase
import com.tokopedia.common.travel.presentation.model.TravelContactData
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutMapperEntity
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutResultEntity
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutResultParams
import com.tokopedia.salam.umrah.checkout.presentation.usecase.UmrahCheckoutGetDataUseCase
import com.tokopedia.salam.umrah.checkout.presentation.usecase.UmrahCheckoutResultUseCase

import kotlinx.coroutines.launch
import com.tokopedia.salam.umrah.common.util.UmrahDispatchersProvider
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Result


/**
 * @author by firman on 4/11/2019
 */

class UmrahCheckoutViewModel @Inject constructor(val umrahCheckoutGetDataUseCase: UmrahCheckoutGetDataUseCase,
                                                 val umrahCheckoutResultUseCase: UmrahCheckoutResultUseCase,
                                                 coroutineDispatcher: UmrahDispatchersProvider)
    : BaseViewModel(coroutineDispatcher.Main) {

    private val checkoutMappedMutable = MutableLiveData<Result<UmrahCheckoutMapperEntity>>()
    val checkoutMapped: LiveData<Result<UmrahCheckoutMapperEntity>>
        get() = checkoutMappedMutable

    private val checkoutResultMutable = MutableLiveData<Result<UmrahCheckoutResultEntity>>()
    val checkoutResult: LiveData<Result<UmrahCheckoutResultEntity>>
        get() = checkoutResultMutable

    fun execute(rawQueryPDP: String, rawQuerySummaryPayment: String, rawQueryOptionPayment: String,
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
