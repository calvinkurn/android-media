package com.tokopedia.flashsale.management.product.domain.usecase

import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject
import javax.inject.Named

class GetSellerStatusLabelUseCase @Inject
constructor(@Named(FlashSaleConstant.NAMED_REQUEST_SELLER_STATUS_CHIP) private val gqlRawString: String,
            graphqlRepository: GraphqlRepository): GraphqlUseCase<String>(graphqlRepository) {

    init {
        setTypeClass(String::class.java)
        setGraphqlQuery(gqlRawString)
    }

}

