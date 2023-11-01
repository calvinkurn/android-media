package com.tokopedia.shop.info.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop.common.di.GqlGetShopInfoForHeaderUseCaseQualifier
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopShipment
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.common.graphql.data.shopnote.gql.GetShopNoteUseCase
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHoursListResponse
import com.tokopedia.shop.info.domain.entity.ShopEpharmacyInfo
import com.tokopedia.shop.info.domain.entity.ShopInfo as ReimagineShopInfo
import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopPerformance
import com.tokopedia.shop.info.domain.entity.ShopSupportedShipment
import com.tokopedia.shop.info.domain.usecase.ProductRevGetShopRatingAndTopicsUseCase
import com.tokopedia.shop.info.domain.usecase.ProductRevGetShopReviewReadingListUseCase
import com.tokopedia.shop.info.view.model.ShopInfoUiState
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderLayoutResponse
import com.tokopedia.shop.pageheader.domain.interactor.GetShopPageHeaderLayoutUseCase
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
    private val getShopGqlGetShopOperationalHoursListUseCase: GqlGetShopOperationalHoursListUseCase,
    private val getShopPageHeaderLayoutUseCase: GetShopPageHeaderLayoutUseCase
) : BaseViewModel(coroutineDispatcherProvider.main) {

    private val _uiState = MutableStateFlow(ShopInfoUiState())
    val uiState = _uiState.asStateFlow()
    
    fun getShopInfo(shopId: String, localCacheModel: LocalCacheModel) {
        launchCatchError(
            context = coroutineDispatcherProvider.io,
            block = {
                _uiState.update { it.copy(isLoading = true) }

                val shopHeaderLayoutDeferred = async { getShopPageHeaderLayout(shopId, localCacheModel) }
                val shopRatingParam = ProductRevGetShopRatingAndTopicsUseCase.Param(shopId)
                val shopRatingDeferred = async { getShopRatingUseCase.execute(shopRatingParam) }

                val shopReviewParam = ProductRevGetShopReviewReadingListUseCase.Param(
                    shopID = shopId,
                    limit = 5,
                    page = 1,
                    filterBy = "",
                    sortBy = ""
                )
                val shopReviewDeferred = async { getShopReviewUseCase.execute(shopReviewParam) }
                
                val shopInfoDeferred = async { getShopInfo(shopId.toIntOrZero()) }
                val shopNotesDeferred = async { getShopNotes(shopId) }
                val shopOperationalHoursDeferred = async { getShopOperationalHours(shopId) }

                val shopHeaderLayout = shopHeaderLayoutDeferred.await()
                val shopRating = shopRatingDeferred.await()
                val shopReview = shopReviewDeferred.await()
                val shopInfo = shopInfoDeferred.await()
                val shopNotes = shopNotesDeferred.await()
                val shopOperationalHours = shopOperationalHoursDeferred.await()
                
                val orderProcessTime = shopHeaderLayout.shopPageGetHeaderLayout.toOrderProcessTime()
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        info = ReimagineShopInfo(
                            shopImageUrl = shopInfo.shopAssets.avatar,
                            shopBadgeUrl = shopInfo.goldOS.badge,
                            shopName = shopInfo.shopCore.name,
                            shopDescription = shopInfo.shopCore.description,
                            mainLocation = shopInfo.location,
                            otherLocations = listOf(),
                            operationalHours = shopOperationalHours.toFormattedOperationalHours(),
                            shopJoinDate = shopInfo.createdInfo.openSince,
                            totalProduct = 2
                        ),
                        rating = shopRating,
                        review = shopReview,
                        shopPerformance = ShopPerformance(
                            totalProductSoldCount = "",
                            chatPerformance = "",
                            orderProcessTime = orderProcessTime
                        ),
                        shopNotes = shopNotes.toShopNotes(),
                        shipments = shopInfo.shipments.toShipments(),
                        showEpharmacyInfo = shopInfo.isGoApotik,
                        epharmacy = ShopEpharmacyInfo(
                            nearestPickupAddress = "",
                            nearPickupAddressAppLink = "",
                            pharmacistOperationalHour = "",
                            pharmacistName = shopInfo.epharmacyInfo.apj,
                            siaNumber = shopInfo.epharmacyInfo.siaNumber,
                            sipaNumber = shopInfo.epharmacyInfo.sipaNumber,
                            collapseEpcharmacyInfo = true
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

    private suspend fun getShopPageHeaderLayout(
        shopId: String,
        widgetUserAddressLocalData: LocalCacheModel
    ): ShopPageHeaderLayoutResponse {
        val districtId: String
        val cityId: String
        if (!isMyShop(shopId)) {
            districtId = widgetUserAddressLocalData.district_id
            cityId = widgetUserAddressLocalData.city_id
        } else {
            districtId = ""
            cityId = ""
        }
        
        getShopPageHeaderLayoutUseCase.params = GetShopPageHeaderLayoutUseCase.createParams(shopId, districtId, cityId)
        getShopPageHeaderLayoutUseCase.isFromCloud = true
        return getShopPageHeaderLayoutUseCase.executeOnBackground()
    }
    
    private fun isMyShop(shopId: String) : Boolean {
        return shopId == userSessionInterface.shopId
    }
    
    private fun List<ShopNoteModel>.toShopNotes(): List<ShopNote> {
        return map {shopNote -> 
            ShopNote(id = shopNote.id.orEmpty(), title = shopNote.title.orEmpty())
        }
    }

    private fun List<ShopShipment>.toShipments(): List<ShopSupportedShipment> {
        return map { shipment ->
            ShopSupportedShipment(
                title = shipment.name,
                imageUrl = shipment.image,
                serviceNames = shipment.product.map { it.name }
            )
        }
    }

    private fun ShopOperationalHoursListResponse.toFormattedOperationalHours(): Map<String, String> {
        val operationalHours = mutableMapOf<String, String>()
        
        val daysDictionary = mapOf(
            1 to "Senin",
            2 to "Selasa",
            3 to "Rabu",
            4 to "Kamis",
            5 to "Jumat",
            6 to "Sabtu",
            7 to "Minggu"
        )
        
        getShopOperationalHoursList?.data?.forEach { operationalHour ->
            val formattedDay = daysDictionary[operationalHour.day].orEmpty()
            operationalHours[formattedDay] = "${operationalHour.startTime} - ${operationalHour.endTime}"
        }
        
        return operationalHours
    }
    
    private fun ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.toOrderProcessTime(): String {
        val shopPerformance= this.widgets.firstOrNull { it.name == "shop_performance" }
        if (shopPerformance == null) return ""
        
        val shopPerformanceListComponent = shopPerformance.listComponent
        val shopHandling = shopPerformanceListComponent.firstOrNull { it.name == "shop_handling" }
        if (shopHandling == null) return ""
        
        val listText = shopHandling.data.listText
        val textHtmls = listText.map { it.textHtml }
        return textHtmls.firstOrNull().orEmpty()
    }

}
