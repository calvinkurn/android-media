package com.tokopedia.flashsale.management.product.domain.usecase

import com.tokopedia.flashsale.management.data.FlashSaleConstant.NAMED_GQL_RAW_ELIGIBLE_SELLER_PRODUCT
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetFlashSaleProductDetailUseCase @Inject
constructor(@Named(NAMED_GQL_RAW_ELIGIBLE_SELLER_PRODUCT) private val gqlRawString: String)
    : UseCase<String>() {

    override suspend fun executeOnBackground(): String {
        return ""
    }
}
