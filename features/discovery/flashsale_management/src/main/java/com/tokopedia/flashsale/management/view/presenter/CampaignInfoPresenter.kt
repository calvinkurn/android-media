package com.tokopedia.flashsale.management.view.presenter

import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.common.data.SellerStatus
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject
import javax.inject.Named

class CampaignInfoPresenter @Inject
    constructor(private val userSession: UserSession,
                @Named(FlashSaleConstant.NAMED_REQUEST_SELLER_STATUS)
                private val useCase: GraphqlUseCase<SellerStatus.Response>){

    fun getSellerStatus(rawQuery: String, campaignSlug: String,
                        onSucces: (SellerStatus) -> Unit, onError: (Throwable) -> Unit){
        useCase.setGraphqlQuery(rawQuery)
        val params = mapOf(FlashSaleConstant.PARAM_SHOP_ID to userSession.shopId.toInt(),
                            FlashSaleConstant.PARAM_SLUG to campaignSlug)
        useCase.setRequestParams(params)
        useCase.execute({onSucces(it.getMojitoSellerStatus.sellerStatus)}, onError)
    }

    fun detachView(){
        useCase.cancelJobs()
    }
}