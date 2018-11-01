package com.tokopedia.flashsale.management.product.domain.usecase

import com.tokopedia.flashsale.management.data.FlashSaleConstant.NAMED_GQL_RAW_ELIGIBLE_SELLER_PRODUCT
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetSellerStatusLabelUseCase @Inject
constructor(@Named(NAMED_GQL_RAW_ELIGIBLE_SELLER_PRODUCT) private val gqlRawString: String,
            private val multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase)
    : UseCase<String>() {

    private val graphQLUseCase: GraphqlUseCase<String> = GraphqlUseCase(multiRequestGraphqlUseCase, String::class.java).apply {
        setGraphqlQuery(gqlRawString)
    }

    override suspend fun executeOnBackground(): String {
        return graphQLUseCase.executeOnBackground()
    }

}
