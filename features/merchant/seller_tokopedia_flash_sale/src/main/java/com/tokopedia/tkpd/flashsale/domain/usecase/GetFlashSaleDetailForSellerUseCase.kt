package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class  GetFlashSaleDetailForSellerUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val getFlashSaleListForSellerUseCase: GetFlashSaleListForSellerUseCase,
) : GraphqlUseCase<FlashSale>(repository)  {
    companion object {
        private const val ONE = 1
        private const val FIRST_PAGE = 0
        private const val TAB_NAME = "detail"
    }

    suspend fun execute(
        campaignId: Long
    ): FlashSale {
        return coroutineScope {
            val params = GetFlashSaleListForSellerUseCase.Param(
                tabName = TAB_NAME,
                offset = FIRST_PAGE,
                rows = ONE,
                campaignIds = listOf(campaignId),
                requestProductMetaData = true,
                checkProductEligibility = true
            )
            val campaignList = getFlashSaleListForSellerUseCase.execute(
                params
            )
            campaignList.flashSales.first()
        }
    }
}
