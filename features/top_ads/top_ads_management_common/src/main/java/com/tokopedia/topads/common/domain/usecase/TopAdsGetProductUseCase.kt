package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.topads.common.data.raw.TOP_ADS_PRODUCT_GQL
import com.tokopedia.topads.common.data.response.OptionV3
import com.tokopedia.topads.common.data.response.TopAdsProductResponse
import javax.inject.Inject

class TopAdsGetProductUseCase @Inject constructor() : GraphqlUseCase<TopAdsProductResponse>()  {

    companion object{
        const val PRODUCT_ID_KEY = "productID"
        const val OPTIONS_KEY = "options"
    }

    fun getProduct(
        productId : String,
        success:(TopAdsProductResponse) -> Unit,
        failure:(Throwable) -> Unit
    ){
        setRequestParams(mapOf(PRODUCT_ID_KEY to productId, OPTIONS_KEY to OptionV3(true)))
        setTypeClass(TopAdsProductResponse::class.java)
        setGraphqlQuery(TOP_ADS_PRODUCT_GQL)
        execute({
                it.result[0]
            it.result[0]
//            if(it.result[0].errors?.isNotEmpty().orFalse()){
//                failure.invoke(Throwable(it.result[0].errors!![0].message))
//            }
//            else success.invoke(it)
        },failure)
    }
}
