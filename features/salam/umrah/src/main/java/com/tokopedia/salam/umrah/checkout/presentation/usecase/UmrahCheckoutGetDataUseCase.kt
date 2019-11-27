package com.tokopedia.salam.umrah.checkout.presentation.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.salam.umrah.checkout.data.*
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import javax.inject.Inject
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface

class UmrahCheckoutGetDataUseCase @Inject constructor(private val useCase: MultiRequestGraphqlUseCase,
                                                      private val userSession: UserSessionInterface) {


    suspend fun execute(rawQueryPDP: String,
                        rawQuerySummaryPayment: String,
                        rawQueryOptionPayment: String,
                        rawQueryTermCondition: String,
                        slugName: String,
                        variantId: String,
                        pilgrimsCount: Int,
                        price: Int,
                        departDate: String,
                        idTermCondition: String
                        ): Result<UmrahCheckoutMapperEntity>{

        useCase.clearRequest()

        val requestPDPParam = mapOf(SLUG_NAME to slugName)

        val checkoutSummaryParam = UmrahCheckoutSummaryParams(variantId,pilgrimsCount)
        val requestSummaryParam = mapOf(PARAMS to checkoutSummaryParam)

        val checkoutOptionParam = UmrahCheckoutPaymentOptionsParams(price,pilgrimsCount,departDate)
        val requestOptionParam = mapOf(PARAMS to checkoutOptionParam)

        val checkoutTermConditions = UmrahCheckoutTermConditionParams(idTermCondition)
        val requestTermCondition = mapOf(PARAMS to checkoutTermConditions)

        try {

            val pdpRequest = GraphqlRequest(rawQueryPDP,UmrahProductModel::class.java,requestPDPParam)
            useCase.addRequest(pdpRequest)

            val summaryRequest = GraphqlRequest(rawQuerySummaryPayment, UmrahCheckoutSummaryEntity::class.java, requestSummaryParam)
            useCase.addRequest(summaryRequest)

            val optionRequest = GraphqlRequest(rawQueryOptionPayment, UmrahCheckoutPaymentOptionsEntity::class.java, requestOptionParam)
            useCase.addRequest(optionRequest)

            val termConditionRequest = GraphqlRequest(rawQueryTermCondition, UmrahCheckoutTermConditionsEntity::class.java, requestTermCondition)
            useCase.addRequest(termConditionRequest)


            val pdpData = useCase.executeOnBackground().getSuccessData<UmrahProductModel>()
            val termConditionData = useCase.executeOnBackground().getSuccessData<UmrahCheckoutTermConditionsEntity>()
            val summaryData = useCase.executeOnBackground().getSuccessData<UmrahCheckoutSummaryEntity>()
            val optionData = useCase.executeOnBackground().getSuccessData<UmrahCheckoutPaymentOptionsEntity>()

            return Success(mapCheckout(pdpData,summaryData,optionData,termConditionData))

        }catch (throwable: Throwable){
            return Fail(throwable)
        }

    }

    private fun mapCheckout(pdpData:UmrahProductModel, summaryData:UmrahCheckoutSummaryEntity,
                            optionData:UmrahCheckoutPaymentOptionsEntity,termConditionData: UmrahCheckoutTermConditionsEntity
    ): UmrahCheckoutMapperEntity{
        return UmrahCheckoutMapperEntity(
                pdpData.umrahProduct,
                mapUser(),
                summaryData,
                optionData,
                termConditionData
        )
    }


    private fun mapUser(): ContactUser{
        return ContactUser(
                userSession.userId,
                userSession.name,
                userSession.email,
                userSession.phoneNumber
        )
    }

    companion object {
        const val PARAMS = "params"
        const val SLUG_NAME = "slugName"
    }
}