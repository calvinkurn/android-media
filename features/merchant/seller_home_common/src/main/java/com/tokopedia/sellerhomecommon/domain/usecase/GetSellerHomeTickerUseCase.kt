package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 25/04/22.
 */

class GetSellerHomeTickerUseCase @Inject constructor(
    private val shopInfoTickerUseCase: GetShopInfoTickerUseCase,
    private val tickerUseCase: GetTickerUseCase,
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun execute(
        shopId: String,
        page: String,
        isFromCache: Boolean
    ): List<TickerItemUiModel> {
        val shopInfoTickerAsync = getShopInfoTickerAsync(shopId, isFromCache)
        val homeTickerAsync = getHomeTickerAsync(page, isFromCache)

        val shopTickerData = shopInfoTickerAsync.await()
        val homeTickerData = homeTickerAsync.await()
        return shopTickerData.plus(homeTickerData)
    }

    private suspend fun getHomeTickerAsync(
        page: String,
        useCache: Boolean
    ): Deferred<List<TickerItemUiModel>> {
        return withContext(dispatchers.io) {
            try {
                tickerUseCase.params = GetTickerUseCase.createParams(page)
                tickerUseCase.setUseCache(useCache)
                async { tickerUseCase.executeOnBackground() }
            } catch (e: Exception) {
                async { listOf() }
            }
        }
    }

    private suspend fun getShopInfoTickerAsync(
        shopId: String,
        useCache: Boolean
    ): Deferred<List<TickerItemUiModel>> {
        return withContext(dispatchers.io) {
            try {
                shopInfoTickerUseCase.params = GetShopInfoTickerUseCase.createParams(shopId)
                shopInfoTickerUseCase.setUseCache(useCache)
                async { shopInfoTickerUseCase.executeOnBackground() }
            } catch (e: Exception) {
                async { emptyList() }
            }
        }
    }
}