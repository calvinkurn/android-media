package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.mapper.DigitalCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.model.CheckVoucherDigital
import com.tokopedia.promocheckout.common.util.PromoQuery
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author: astidhiyaa on 02/08/21.
 */
class PromoCheckoutListDigitalViewModel  @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                             private val graphqlRepository: GraphqlRepository,
                                                             val checkVoucherMapper: DigitalCheckVoucherMapper
): BaseViewModel(dispatcher.io) {

    private val _digitalCheckVoucherResult = MutableLiveData<Result<DataUiModel>>()
    val digitalCheckVoucherResult: LiveData<Result<DataUiModel>>
        get() = _digitalCheckVoucherResult

    val showLoadingPromoDigital = MutableLiveData<Boolean>()

    fun checkPromoCode(promoCode: String, promoDigitalModel: PromoDigitalModel){
        showLoadingPromoDigital.postValue(true)
        launchCatchError(block = {
            val data =  withContext(dispatcher.io){
                graphqlRepository.getReseponse(listOf(createRequest(promoCode, promoDigitalModel)))
            }.getSuccessData<CheckVoucherDigital.Response>().response
            showLoadingPromoDigital.postValue( false)
            if(data.voucherData.success){
                _digitalCheckVoucherResult.postValue(Success(checkVoucherMapper.mapData(data.voucherData)))
            }else{
                _digitalCheckVoucherResult.postValue(Fail(MessageErrorException(data.voucherData.message.text)))
            }
        }){
            showLoadingPromoDigital.postValue( false)
            _digitalCheckVoucherResult.postValue(Fail(it))
        }
    }

    private fun createRequest(promoCode: String, promoDigitalModel: PromoDigitalModel): GraphqlRequest {
        val requestParams = RequestParams.create()
        requestParams.putString(INPUT_CODE, promoCode)
        requestParams.putInt(PRODUCT_ID, promoDigitalModel.productId)
        if (promoDigitalModel.clientNumber.isNotEmpty()) {
            requestParams.putString(CLIENT_NUMBER, promoDigitalModel.clientNumber)
        }
        if (promoDigitalModel.price > 0) requestParams.putLong(PRICE, promoDigitalModel.price)
        val variables = mapOf(LABEL_DATA to requestParams.parameters)
        return GraphqlRequest(
            PromoQuery.promoCheckoutDigitalCheckVoucher(),
            CheckVoucherDigital.Response::class.java, variables, false)
    }

    companion object{
        const val INPUT_CODE = "code"
        const val PRODUCT_ID = "product_id"
        const val CLIENT_NUMBER = "client_number"
        const val PRICE = "price"
        const val LABEL_DATA = "data"
    }
}