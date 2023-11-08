package com.tokopedia.shop.info.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.extension.toDate
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopShipment
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.common.graphql.data.shopnote.gql.GetShopNoteUseCase
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHoursListResponse
import com.tokopedia.shop.common.util.DateTimeConstant
import com.tokopedia.shop.info.domain.entity.ShopEpharmacyInfo
import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopPerformance
import com.tokopedia.shop.info.domain.entity.ShopSupportedShipment
import com.tokopedia.shop.info.domain.usecase.ProductRevGetShopRatingAndTopicsUseCase
import com.tokopedia.shop.info.domain.usecase.ProductRevGetShopReviewReadingListUseCase
import com.tokopedia.shop.info.view.model.ShopInfoUiState
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderLayoutResponse
import com.tokopedia.shop.pageheader.domain.interactor.GetShopPageHeaderLayoutUseCase
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.tokopedia.shop.info.domain.entity.ShopInfo as ReimagineShopInfo

class ShopInfoReimagineViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    private val coroutineDispatcherProvider: CoroutineDispatchers,
    private val getShopInfoUseCase: GQLGetShopInfoUseCase,
    private val getShopNoteUseCase: GetShopNoteUseCase,
    private val getShopRatingUseCase: ProductRevGetShopRatingAndTopicsUseCase,
    private val getShopReviewUseCase: ProductRevGetShopReviewReadingListUseCase,
    private val getShopGqlGetShopOperationalHoursListUseCase: GqlGetShopOperationalHoursListUseCase,
    private val getShopPageHeaderLayoutUseCase: GetShopPageHeaderLayoutUseCase,
    private val getShopProductUseCase: GqlGetShopProductUseCase
) : BaseViewModel(coroutineDispatcherProvider.main) {

    companion object {
        private const val ID_FULFILLMENT_SERVICE_E_PHARMACY = 2
    }
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
                    sortBy = "rating desc"
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

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        info = ReimagineShopInfo(
                            shopImageUrl = shopInfo.shopAssets.avatar,
                            shopBadgeUrl = shopInfo.goldOS.badge,
                            shopName = shopInfo.shopCore.name,
                            shopDescription = shopInfo.shopCore.description,
                            mainLocation = shopInfo.location,
                            otherLocations = listOf(), // TODO replace with real data
                            operationalHours = shopOperationalHours.toFormattedOperationalHours(),
                            shopJoinDate = shopInfo.createdInfo.openSince,
                            totalProduct = 2, // TODO replace with real data
                            shopUsp = shopHeaderLayout.shopPageGetHeaderLayout.toShopUsp(),
                            showPharmacyLicenseBadge = shouldShowPharmacyLicenseBadge(shopInfo)
                        ),
                        rating = shopRating,
                        review = shopReview,
                        shopPerformance = ShopPerformance(
                            totalProductSoldCount = "", // TODO replace with real data
                            chatPerformance = "", // TODO replace with real data
                            orderProcessTime = shopHeaderLayout.shopPageGetHeaderLayout.toOrderProcessTime()
                        ),
                        shopNotes = shopNotes.toShopNotes(),
                        shipments = shopInfo.shipments.toShipments(),
                        showEpharmacyInfo = shouldShowPharmacyLicenseBadge(shopInfo),
                        epharmacy = ShopEpharmacyInfo(
                            nearestPickupAddress = "", // TODO replace with real data
                            nearPickupAddressAppLink = "", // TODO replace with real data
                            pharmacistOperationalHour = "", // TODO replace with real data
                            pharmacistName = shopInfo.epharmacyInfo.apj,
                            siaNumber = shopInfo.epharmacyInfo.siaNumber,
                            sipaNumber = shopInfo.epharmacyInfo.sipaNumber,
                            collapseEpcharmacyInfo = true
                        )
                    )
                }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )
    }

    fun handleCtaExpandPharmacyInfoClick() {
        _uiState.update {
            it.copy(epharmacy = it.epharmacy.copy(collapseEpcharmacyInfo = false))
        }
    }

    fun handleCtaViewPharmacyMapClick() {
        // TODO emit event redirect to gmap
    }

    fun handleRetryGetShopInfo(shopId: String, localCacheModel: LocalCacheModel) {
        getShopInfo(shopId, localCacheModel)
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

    private fun isMyShop(shopId: String): Boolean {
        return shopId == userSessionInterface.shopId
    }

    private fun List<ShopNoteModel>.toShopNotes(): List<ShopNote> {
        return map { shopNote ->
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

    private fun ShopOperationalHoursListResponse.toFormattedOperationalHours(): Map<String, List<String>> {
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

            val startTime = operationalHour.startTime.toDate(DateTimeConstant.TIME_SECOND_PRECISION).formatTo(DateTimeConstant.TIME_MINUTE_PRECISION)
            val endTime = operationalHour.endTime.toDate(DateTimeConstant.TIME_SECOND_PRECISION).formatTo(DateTimeConstant.TIME_MINUTE_PRECISION)
            val operationalHourFormat = "%s - %s"

            operationalHours[formattedDay] = String.format(operationalHourFormat, startTime, endTime)
        }

        val groupedByHours = operationalHours.groupByHours()

        return groupedByHours
    }

    private fun ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.toOrderProcessTime(): String {
        val shopPerformance = this.widgets.firstOrNull { it.name == "shop_performance" }
        if (shopPerformance == null) return ""

        val shopPerformanceListComponent = shopPerformance.listComponent
        val shopHandling = shopPerformanceListComponent.firstOrNull { it.name == "shop_handling" }
        if (shopHandling == null) return ""

        val listText = shopHandling.data.listText
        val textHtmls = listText.map { it.textHtml }
        return textHtmls.firstOrNull().orEmpty()
    }

    private fun ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.toShopUsp(): List<String> {
        val shopBasicInfo = this.widgets.firstOrNull { it.name == "shop_basic_info" }
        if (shopBasicInfo == null) return emptyList()

        val shopBasicInfoListComponent = shopBasicInfo.listComponent
        val shopAttributeList = shopBasicInfoListComponent.firstOrNull { it.name == "shop_attribute_list" }
        if (shopAttributeList == null) return emptyList()

        val listText = shopAttributeList.data.listText
        val textHtmls = listText.map { it.textHtml }
        return textHtmls
    }

    private fun shouldShowPharmacyLicenseBadge(shopInfo: ShopInfo): Boolean {
        if (!shopInfo.isGoApotik) {
            return false
        }

        val hasPharmacyFulfilmentService = shopInfo.partnerInfo.any { partnerInfo ->
            partnerInfo.fsType == ID_FULFILLMENT_SERVICE_E_PHARMACY
        }

        if (!hasPharmacyFulfilmentService) {
            return false
        }

        return true
    }

    private fun Map<String, String>.groupByHours(): Map<String, List<String>> {
        return this.entries.groupBy(
            keySelector = { it.value },
            valueTransform = { it.key }
        )
    }
}
