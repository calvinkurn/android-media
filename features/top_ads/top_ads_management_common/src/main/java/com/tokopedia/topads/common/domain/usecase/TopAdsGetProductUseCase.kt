package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.response.OptionV3
import com.tokopedia.topads.common.data.response.TopAdsProductResponse
import com.tokopedia.topads.common.domain.query.GetTopadsProductV3
import javax.inject.Inject

class TopAdsGetProductUseCase @Inject constructor() : GraphqlUseCase<TopAdsProductResponse>() {

    companion object {
        const val PRODUCT_ID_KEY = "productID"
        const val OPTIONS_KEY = "options"
    }

    fun getProduct(
        productId: String?,
        success: (TopAdsProductResponse) -> Unit
    ) {
        setRequestParams(mapOf(PRODUCT_ID_KEY to productId, OPTIONS_KEY to OptionV3(false)))
        setTypeClass(TopAdsProductResponse::class.java)
        setGraphqlQuery(GetTopadsProductV3)
        execute({
            success.invoke(it)
        }, { throwable ->
            throwable.printStackTrace()
        })
    }
}
