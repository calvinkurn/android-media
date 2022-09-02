package com.tokopedia.centralizedpromoold.domain.usecase

import com.tokopedia.centralizedpromoold.domain.model.MerchantPromotionGetPromoListResponseOld
import com.tokopedia.centralizedpromoold.domain.mapper.MerchantPromotionListEligibleMapperOld
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class SlashPriceEligibleUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val mapper: MerchantPromotionListEligibleMapperOld
) : GraphqlUseCase<MerchantPromotionGetPromoListResponseOld>(repository) {

    init {
        setGraphqlQuery(VoucherCashbackEligibleUseCase.QUERY)
        setTypeClass(MerchantPromotionGetPromoListResponseOld::class.java)
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