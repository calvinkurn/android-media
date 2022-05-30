package com.tokopedia.centralizedpromo.domain.usecase

import com.tokopedia.centralizedpromo.domain.mapper.MerchantPromotionListEligibleMapper
import com.tokopedia.centralizedpromo.domain.model.MerchantPromotionGetPromoListResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class SlashPriceEligibleUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val mapper: MerchantPromotionListEligibleMapper
) : GraphqlUseCase<MerchantPromotionGetPromoListResponse>(repository) {

    init {
        setGraphqlQuery(VoucherCashbackEligibleUseCase.QUERY)
        setTypeClass(MerchantPromotionGetPromoListResponse::class.java)
    }

    suspend fun execute(shopId: String): Boolean {
        setRequestParams(VoucherCashbackEligibleUseCase.createRequestParams(shopId.toLongOrZero()).parameters)
        val response = executeOnBackground().merchantPromotionGetPromoList
        val errors = response.header.messages
        if (errors.isNullOrEmpty()) {
            return mapper.isSlashPriceEligible(response.data.pages)
        } else {
            throw MessageErrorException(errors.joinToString { it })
        }
    }

}