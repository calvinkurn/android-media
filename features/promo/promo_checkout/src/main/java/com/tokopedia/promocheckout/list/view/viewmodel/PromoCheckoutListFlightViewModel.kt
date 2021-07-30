package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.data.PromoCheckoutCommonQueryConst
import com.tokopedia.promocheckout.common.domain.mapper.FlightCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.model.FlightCheckVoucher
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailFlightPresenter
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author: astidhiyaa on 31/07/21.
 */
class PromoCheckoutListFlightViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                           private val graphqlRepository: GraphqlRepository,
                                                           val checkVoucherMapper: FlightCheckVoucherMapper
): BaseViewModel(dispatcher.io) {

    private val _flightCheckVoucherResult = MutableLiveData<Result<DataUiModel>>()
    val flightCheckVoucherResult: LiveData<Result<DataUiModel>>
        get() = _flightCheckVoucherResult

    val showLoadingPromoFlight = MutableLiveData<Boolean>()

    fun checkPromoCode(cartID: String, promoCode: String, hexColor: String){
        showLoadingPromoFlight.postValue(true)
        launchCatchError(block = {
            val data =  withContext(dispatcher.io){
                graphqlRepository.getReseponse(listOf(createRequest(cartID, promoCode)))
            }
            val errors = data.getError(FlightCheckVoucher.Response::class.java)
            showLoadingPromoFlight.postValue( false)
            if(!errors.isNullOrEmpty()){
                val rawErrorMessage = errors[0].message
                val errorMessage = Gson().fromJson(rawErrorMessage.substring(1, rawErrorMessage.length - 1), PromoCheckoutDetailFlightPresenter.Companion.FlightCheckVoucherError::class.java)
                _flightCheckVoucherResult.postValue(Fail(MessageErrorException(errorMessage.title)))
            }else{
                val checkVoucherData = data.getData<FlightCheckVoucher.Response>(FlightCheckVoucher.Response::class.java).response
                checkVoucherData.messageColor = hexColor
                _flightCheckVoucherResult.postValue(Success(checkVoucherMapper.mapData(checkVoucherData)))
            }
        }){
            showLoadingPromoFlight.postValue( false)
            _flightCheckVoucherResult.postValue(Fail(it))
        }
    }

    private fun createRequest(cartID: String, promoCode: String): GraphqlRequest {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAMNAME_VOUCHER_CODE, promoCode)
        requestParams.putString(PARAMNAME_CART_ID, cartID)
        return GraphqlRequest(
            PromoCheckoutCommonQueryConst.QUERY_FLIGHT_CHECK_VOUCHER,
            FlightCheckVoucher.Response::class.java, requestParams.parameters, false)
    }

    companion object{
        const val PARAMNAME_CART_ID = "cartID"
        const val PARAMNAME_VOUCHER_CODE = "voucherCode"
    }
}