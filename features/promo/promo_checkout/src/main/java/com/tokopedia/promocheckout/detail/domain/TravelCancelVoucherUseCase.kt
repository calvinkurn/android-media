package com.tokopedia.promocheckout.detail.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.data.PromoCheckoutCommonQueryConst
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author: astidhiyaa on 02/08/21.
 */
class TravelCancelVoucherUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<FlightCancelVoucher.Response>(graphqlRepository){
    suspend fun execute(): Result<FlightCancelVoucher.Response> {
        return try{
            this.setTypeClass(FlightCancelVoucher.Response::class.java)
            this.setGraphqlQuery(PromoCheckoutCommonQueryConst.QUERY_FLIGHT_CANCEL_VOUCHER)
            val data = this.executeOnBackground()
            if(data.response.attributes.success){
                Success(data)
            }else{
                Fail(MessageErrorException(ERROR_CANCEL_VOUCHER_MESSAGE))
            }
        }catch (t: Throwable){
            Fail(t)
        }
    }

    companion object{
        const val ERROR_CANCEL_VOUCHER_MESSAGE = "Promo tidak berhasil dilepas"
    }
}