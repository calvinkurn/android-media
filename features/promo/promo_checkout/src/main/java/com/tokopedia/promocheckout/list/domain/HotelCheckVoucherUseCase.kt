package com.tokopedia.promocheckout.list.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.list.domain.mapper.HotelCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.model.HotelCheckVoucher
import com.tokopedia.promocheckout.common.util.PromoQuery
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author: astidhiyaa on 02/08/21.
 */
class HotelCheckVoucherUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                              val checkVoucherMapper: HotelCheckVoucherMapper
) :
    GraphqlUseCase<HotelCheckVoucher.Response>(graphqlRepository) {

    suspend fun execute(requestParams: RequestParams?, onMessageColorChange: String): Result<DataUiModel> {
        return try{
            this.setTypeClass(HotelCheckVoucher.Response::class.java)
            this.setGraphqlQuery(PromoQuery.promoCheckoutHotelCheckVoucher())
            this.setRequestParams(mapOf(PARAMNAME_DATA to requestParams?.parameters))

            val data = this.executeOnBackground()
            data.response.messageColor = onMessageColorChange
            if(data.response.isSuccess){
                Success(checkVoucherMapper.mapData(data.response))
            }else{
                Fail(MessageErrorException(data.response.errorMessage))
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
        const val PARAMNAME_DATA = "data"
    }
}