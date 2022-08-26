package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleData
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetFlashSaleDetailForSellerUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val getFlashSaleListForSellerUseCase: GetFlashSaleListForSellerUseCase,
) : GraphqlUseCase<FlashSale>(repository)  {
    companion object {
        const val ONE = 1
        const val FIRST_PAGE = 0
    }

    suspend fun execute(
        tabName: String,
        campaignId: Long
    ): FlashSale {
        return coroutineScope {
            val params = GetFlashSaleListForSellerUseCase.Param(
                tabName = tabName,
                offset = FIRST_PAGE,
                rows = ONE,
                campaignIds = listOf(campaignId)
            )
            val campaignList = getFlashSaleListForSellerUseCase.execute(
                params
            )
            campaignList.flashSales.first()
        }
    }
}