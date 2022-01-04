package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.home.beranda.domain.interactor.repository.HomeCloseChannelRepository
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import javax.inject.Inject

class HomeListCarouselUseCase @Inject constructor(
        private val homeCloseChannelRepository: HomeCloseChannelRepository,
        private val getAtcUseCase: AddToCartOccMultiUseCase
        ) {

    companion object {
        const val ATC = "atc"
        const val CHANNEL = "channel"
        const val GRID = "grid"
        const val QUANTITY = "quantity"
        const val POSITION = "position"
    }

    suspend fun onClickCloseListCarousel(channelId: String): Boolean {
        return try {
            homeCloseChannelRepository.setParams(channelId)
            val closeChannel = homeCloseChannelRepository.executeOnBackground()
            closeChannel.success
        } catch (e: Exception) {
            false
        }
    }

    suspend fun onOneClickCheckOut(channelModel: ChannelModel, grid: ChannelGrid, position: Int, userId: String): Map<String, Any> {
        val quantity = if(grid.minOrder < 1) "1" else grid.minOrder.toString()
        val addToCartResult = getAtcUseCase.setParams(AddToCartOccMultiRequestParams(
                carts = listOf(
                        AddToCartOccMultiCartParam(
                                productId = grid.id,
                                quantity = quantity,
                                shopId = grid.shopId,
                                warehouseId = grid.warehouseId,
                                productName = grid.name,
                                price = grid.price
                        )
                ),
                userId = userId
        )).executeOnBackground().mapToAddToCartDataModel()
        if(!addToCartResult.isStatusError()) {
            return mapOf(
                    ATC to addToCartResult,
                    CHANNEL to channelModel,
                    GRID to grid,
                    QUANTITY to quantity,
                    POSITION to position

            )
        }
        else {
            throw Throwable()
        }
    }
}