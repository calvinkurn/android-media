package com.tokopedia.promocheckout.list.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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
import javax.inject.Inject

/**
 * @author: astidhiyaa on 03/08/21.
 */
class DigitalCheckVoucherUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                     val checkVoucherMapper: DigitalCheckVoucherMapper
) :
    GraphqlUseCase<CheckVoucherDigital.Response>(graphqlRepository) {

    suspend fun execute(requestParams: RequestParams): Result<DataUiModel> {
        return try{
            this.setTypeClass(CheckVoucherDigital.Response::class.java)
            this.setGraphqlQuery(PromoQuery.promoCheckoutDigitalCheckVoucher())
            this.setRequestParams( mapOf(LABEL_DATA to requestParams.parameters))

            val data = this.executeOnBackground()
            if(data.response.voucherData.success){
                Success(checkVoucherMapper.mapData(data.response.voucherData))
            }else{
                Fail(MessageErrorException(data.response.voucherData.message.text))
            }
        }catch (t: Throwable){
            Fail(t)
        }
    }

    fun createRequest(promoCode: String, promoDigitalModel: PromoDigitalModel): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(INPUT_CODE, promoCode)
        requestParams.putInt(PRODUCT_ID, promoDigitalModel.productId)
        if (promoDigitalModel.clientNumber.isNotEmpty()) {
            requestParams.putString(CLIENT_NUMBER, promoDigitalModel.clientNumber)
        }
        if (promoDigitalModel.price > 0) requestParams.putLong(PRICE, promoDigitalModel.price)
       return requestParams
    }

    companion object{
        const val INPUT_CODE = "code"
        const val PRODUCT_ID = "product_id"
        const val CLIENT_NUMBER = "client_number"
        const val PRICE = "price"
        const val LABEL_DATA = "data"
    }
}