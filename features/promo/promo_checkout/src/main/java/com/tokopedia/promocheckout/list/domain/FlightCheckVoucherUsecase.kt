package com.tokopedia.promocheckout.list.domain

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.data.PromoCheckoutCommonQueryConst
import com.tokopedia.promocheckout.list.domain.mapper.FlightCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.model.FlightCheckVoucher
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author: astidhiyaa on 02/08/21.
 */
class FlightCheckVoucherUsecase  @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                     val checkVoucherMapper: FlightCheckVoucherMapper
) :
    GraphqlUseCase<FlightCheckVoucher.Response>(graphqlRepository) {

    suspend fun execute(requestParams: RequestParams?, onMessageColorChange: String): Result<DataUiModel> {
        return try{
            val gqlRequest = GraphqlRequest(PromoCheckoutCommonQueryConst.QUERY_FLIGHT_CHECK_VOUCHER, FlightCheckVoucher.Response::class.java, requestParams?.parameters)
            val data = graphqlRepository.getReseponse(listOf(gqlRequest))
            val errors = data.getError(FlightCheckVoucher.Response::class.java)
            if(!errors.isNullOrEmpty()){
                val rawErrorMessage = errors[0].message
                val errorMessage = Gson().fromJson(rawErrorMessage.substring(1, rawErrorMessage.length - 1), FlightCheckVoucherError::class.java)
                Fail(MessageErrorException(errorMessage.title))
            }else{
                val checkVoucherData = data.getData<FlightCheckVoucher.Response>(FlightCheckVoucher.Response::class.java).response
                checkVoucherData.messageColor = onMessageColorChange
                Success(checkVoucherMapper.mapData(checkVoucherData))
            }
        }catch (t: Throwable){
            Fail(t)
        }
    }

    fun createRequestParams(voucherCode: String, cartID: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAMNAME_VOUCHER_CODE, voucherCode)
        requestParams.putString(PARAMNAME_CART_ID, cartID)
        return requestParams
    }

    companion object{
        const val PARAMNAME_CART_ID = "cartID"
        const val PARAMNAME_VOUCHER_CODE = "voucherCode"
        data class FlightCheckVoucherError(val id: String, val status: String, val title: String)
    }
}