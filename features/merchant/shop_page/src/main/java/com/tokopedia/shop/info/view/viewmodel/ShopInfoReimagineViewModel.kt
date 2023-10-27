package com.tokopedia.shop.info.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopShipment
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.common.graphql.data.shopnote.gql.GetShopNoteUseCase
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHoursListResponse
import com.tokopedia.shop.info.domain.entity.ShopEpharmacyInfo
import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopOperationalHour
import com.tokopedia.shop.info.domain.entity.ShopSupportedShipment
import com.tokopedia.shop.info.domain.usecase.ProductRevGetShopRatingAndTopicsUseCase
import com.tokopedia.shop.info.domain.usecase.ProductRevGetShopReviewReadingListUseCase
import com.tokopedia.shop.info.view.model.ShopInfoUiState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ShopInfoReimagineViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val coroutineDispatcherProvider: CoroutineDispatchers,
    private val getShopInfoUseCase: GQLGetShopInfoUseCase,
    private val getShopNoteUseCase: GetShopNoteUseCase,
    private val getShopRatingUseCase: ProductRevGetShopRatingAndTopicsUseCase,
    private val getShopReviewUseCase: ProductRevGetShopReviewReadingListUseCase,
    private val getShopGqlGetShopOperationalHoursListUseCase: GqlGetShopOperationalHoursListUseCase
) : BaseViewModel(coroutineDispatcherProvider.main) {

    private val _uiState = MutableStateFlow(ShopInfoUiState())
    val uiState = _uiState.asStateFlow()
    
    
    fun getShopRating(shopId: String) {
        launchCatchError(
            context = coroutineDispatcherProvider.io,
            block = {
                _uiState.update { it.copy(isLoading = true) }
                
                val shopRatingParam = ProductRevGetShopRatingAndTopicsUseCase.Param(shopId)
                val shopReviewParam = ProductRevGetShopReviewReadingListUseCase.Param(
                    shopID = shopId,
                    limit = 5,
                    page = 1,
                    filterBy = "",
                    sortBy = ""
                )
                val shopRatingDeferred = async { getShopRatingUseCase.execute(shopRatingParam) }
                val shopReviewDeferred = async { getShopReviewUseCase.execute(shopReviewParam) }
                val shopInfoDeferred = async { getShopInfo(shopId.toIntOrZero()) }
                val shopNotesDeferred = async { getShopNotes(shopId) }
                val shopOperationalHoursDeferred = async { getShopOperationalHours(shopId) }
                
                val shopRating = shopRatingDeferred.await()
                val shopReview = shopReviewDeferred.await()
                val shopInfo = shopInfoDeferred.await()
                val shopNotes = shopNotesDeferred.await()
                val shopOperationalHours = shopOperationalHoursDeferred.await()
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        shopImageUrl = shopInfo.shopAssets.avatar,
                        shopBadgeUrl = shopInfo.gold.badge,
                        shopName = shopInfo.shopCore.name,
                        shopDescription = shopInfo.shopCore.description,
                        mainLocation = shopInfo.location,
                        otherLocation = "",
                        operationalHours = shopOperationalHours.toFormattedOperationalHours(),
                        shopJoinDate = shopInfo.createdInfo.openSince,
                        rating = shopRating,
                        review = shopReview,
                        shopPerformanceMetrics = listOf(),
                        shopNotes = shopNotes.toShopNotes(),
                        shipments = shopInfo.shipments.toShipments(),
                        showEpharmacyInfo = shopInfo.isGoApotik,
                        epharmacy = ShopEpharmacyInfo(
                            nearestPickupAddress = "",
                            nearPickupAddressAppLink = "",
                            pharmacistOperationalHour = "",
                            pharmacistName = shopInfo.epharmacyInfo.apj,
                            siaNumber = shopInfo.epharmacyInfo.siaNumber,
                            sipaNumber = shopInfo.epharmacyInfo.sipaNumber
                        )
                    )
                }

            },
            onError = {error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        ) 
    }

    private suspend fun getShopInfo(shopId: Int): ShopInfo {
        getShopInfoUseCase.isFromCacheFirst = false
        getShopInfoUseCase.params = GQLGetShopInfoUseCase.createParams(
            shopIds = listOf(shopId),
            source = GQLGetShopInfoUseCase.SHOP_PAGE_SOURCE,
            fields = GQLGetShopInfoUseCase.getDefaultShopFields() + listOf(
                GQLGetShopInfoUseCase.FIELD_OS,
                GQLGetShopInfoUseCase.FIELD_GOLD_OS,
                GQLGetShopInfoUseCase.FIELD_GOLD
            )
        )
        return getShopInfoUseCase.executeOnBackground()
    }

    private suspend fun getShopNotes(shopId: String): List<ShopNoteModel> {
        getShopNoteUseCase.params = GetShopNoteUseCase.createParams(shopId)
        getShopNoteUseCase.isFromCacheFirst = false
        return getShopNoteUseCase.executeOnBackground()
    }

    private suspend fun getShopOperationalHours(shopId: String): ShopOperationalHoursListResponse {
        getShopGqlGetShopOperationalHoursListUseCase.params = GqlGetShopOperationalHoursListUseCase.createRequestParams(shopId)
        getShopNoteUseCase.isFromCacheFirst = false
        return getShopGqlGetShopOperationalHoursListUseCase.executeOnBackground()
    }
    
    private fun List<ShopNoteModel>.toShopNotes(): List<ShopNote> {
        return map {shopNote -> 
            ShopNote(id = shopNote.id.orEmpty(), title = shopNote.title.orEmpty())
        }
    }

    private fun List<ShopShipment>.toShipments(): List<ShopSupportedShipment> {
        return map { shipment ->
            ShopSupportedShipment(title = shipment.name, imageUrl = shipment.image)
        }
    }

    private fun ShopOperationalHoursListResponse.toFormattedOperationalHours(): List<ShopOperationalHour> {
        return getShopOperationalHoursList?.data?.map { 
            ShopOperationalHour(it.day, it.startTime, it.endTime, it.status)
        }.orEmpty()
    }
}
