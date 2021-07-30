package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.mapper.HotelCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.model.HotelCheckVoucher
import com.tokopedia.promocheckout.common.util.PromoQuery
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
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
class PromoCheckoutListHotelViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                         private val graphqlRepository: GraphqlRepository,
                                                         val checkVoucherMapper: HotelCheckVoucherMapper
): BaseViewModel(dispatcher.io) {

    private val _hotelCheckVoucherResult = MutableLiveData<Result<DataUiModel>>()
    val hotelCheckVoucherResult: LiveData<Result<DataUiModel>>
        get() = _hotelCheckVoucherResult

    val showLoadingPromoHotel = MutableLiveData<Boolean>()

    fun checkPromoCode(cartID: String, promoCode: String, hexColor: String){
        showLoadingPromoHotel.postValue(true)
        launchCatchError(block = {
            val data =  withContext(dispatcher.io){
                graphqlRepository.getReseponse(listOf(createRequest(cartID, promoCode)))
            }.getSuccessData<HotelCheckVoucher.Response>().response
            data.messageColor = hexColor
            showLoadingPromoHotel.postValue( false)
            if(data.isSuccess){
                _hotelCheckVoucherResult.postValue(Success(checkVoucherMapper.mapData(data)))
            }else{
                _hotelCheckVoucherResult.postValue(Fail(MessageErrorException(data.errorMessage)))
            }
        }){
            showLoadingPromoHotel.postValue( false)
            _hotelCheckVoucherResult.postValue(Fail(it))
        }
    }

    private fun createRequest(cartID: String, promoCode: String): GraphqlRequest{
        val requestParams = RequestParams.create()
        requestParams.putString(PARAMNAME_VOUCHER_CODE, promoCode)
        requestParams.putString(PARAMNAME_CART_ID, cartID)
        val variables = mapOf(PARAMNAME_DATA to requestParams.parameters)
        return GraphqlRequest(
            PromoQuery.promoCheckoutHotelCheckVoucher(),
            HotelCheckVoucher.Response::class.java, variables, false)
    }

    companion object{
        const val PARAMNAME_CART_ID = "cartID"
        const val PARAMNAME_VOUCHER_CODE = "voucherCode"
        const val PARAMNAME_DATA = "data"
    }
}