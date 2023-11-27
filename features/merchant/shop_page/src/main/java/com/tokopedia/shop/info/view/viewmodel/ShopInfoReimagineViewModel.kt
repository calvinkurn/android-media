package com.tokopedia.shop.info.view.viewmodel

import android.annotation.SuppressLint
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.common.data.model.ShopInfoPageTracker
import com.tokopedia.shop.common.domain.GetMessageIdChatUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.extension.toDate
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopShipment
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.common.graphql.data.shopnote.gql.GetShopNoteUseCase
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHoursListResponse
import com.tokopedia.shop.common.util.DateTimeConstant
import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopOperationalHourWithStatus
import com.tokopedia.shop.info.domain.entity.ShopPharmacyInfo
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.shop.info.domain.entity.ShopSupportedShipment
import com.tokopedia.shop.info.domain.usecase.GetEpharmacyShopInfoUseCase
import com.tokopedia.shop.info.domain.usecase.GetNearestEpharmacyWarehouseLocationUseCase
import com.tokopedia.shop.info.domain.usecase.ProductRevGetShopRatingAndTopicsUseCase
import com.tokopedia.shop.info.domain.usecase.ProductRevGetShopReviewReadingListUseCase
import com.tokopedia.shop.info.view.model.ShopInfoUiEffect
import com.tokopedia.shop.info.view.model.ShopInfoUiEvent
import com.tokopedia.shop.info.view.model.ShopInfoUiState
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderLayoutResponse
import com.tokopedia.shop.pageheader.domain.interactor.GetShopPageHeaderLayoutUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.supervisorScope
import java.util.concurrent.TimeUnit
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
    private val getEpharmacyShopInfoUseCase: GetEpharmacyShopInfoUseCase,
    private val getNearestEpharmacyWarehouseLocationUseCase: GetNearestEpharmacyWarehouseLocationUseCase,
    private val getMessageIdChatUseCase: GetMessageIdChatUseCase,
    private val tracker: ShopInfoPageTracker
) : BaseViewModel(coroutineDispatcherProvider.main) {

    companion object {
        private const val ID_FULFILLMENT_SERVICE_E_PHARMACY = 2

        const val FIVE_REVIEW = 5
        const val FIRST_PAGE = 1
        const val SHOP_TOP_REVIEW_SORT_BY_HELPFULNESS = "informative_score desc"
        const val SHOP_TOP_REVIEW_FILTER_BY_SELLER_SERVICE = "topic=pelayanan"
        const val PAGE_SOURCE = "shop_info_page"
        const val TWENTY_THREE_HOURS_FIFTY_NINE_MINUTE_IN_SECOND = 86340 // 23 hour 59 minute and 0 second difference
        const val TWENTY_THREE_HOURS_FIFTY_NINE_MINUTE_AND_FIFTY_NINE_SECOND_IN_SECOND = 86399 // 23 hour 59 minute and 59 second difference
        const val MAX_SHOP_DYNAMIC_USP_TO_DISPLAY = 3
    }

    private val _uiState = MutableStateFlow(ShopInfoUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ShopInfoUiEffect>(replay = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val currentState: ShopInfoUiState
        get() = _uiState.value

    private val impressedReviewIndex = mutableSetOf<Int>()

    fun processEvent(event: ShopInfoUiEvent) {
        when (event) {
            is ShopInfoUiEvent.Setup -> handleSetup(event.shopId, event.districtId, event.cityId)
            is ShopInfoUiEvent.GetShopInfo -> handleGetShopInfo()
            ShopInfoUiEvent.TapCtaExpandShopPharmacyInfo -> handleCtaExpandShopPharmacyInfo()
            ShopInfoUiEvent.TapCtaViewPharmacyLocation -> handleCtaViewPharmacyLocation()
            ShopInfoUiEvent.TapShopRating -> handleTapShopRating()
            is ShopInfoUiEvent.RetryGetShopInfo -> handleRetryGetShopInfo()
            is ShopInfoUiEvent.TapShopNote -> handleTapShopNote(event.shopNote)
            ShopInfoUiEvent.ReportShop -> handleReportShop()
            is ShopInfoUiEvent.TapReviewImage -> handleTapReviewImage(event.review, event.reviewImageIndex)
            is ShopInfoUiEvent.TapReviewImageViewAll -> handleTapReviewImageViewAll(event.productId)
            is ShopInfoUiEvent.SwipeReview -> handleSwipeReview(event.reviewIndex)
        }
    }

    private fun handleSetup(shopId: String, districtId: String, cityId: String) {
        tracker.sendShopInfoOpenScreenImpression(shopId)
        _uiState.update {
            it.copy(
                shopId = shopId,
                districtId = districtId,
                cityId = cityId
            )
        }
    }
    private fun handleGetShopInfo() {
        launchCatchError(
            context = coroutineDispatcherProvider.io,
            block = {
                supervisorScope {
                    _uiState.update { it.copy(isLoading = true, error = null) }

                    val shopId = currentState.shopId

                    val shopHeaderLayoutDeferred = async { getShopPageHeaderLayout() }
                    val shopRatingParam = ProductRevGetShopRatingAndTopicsUseCase.Param(shopId)
                    val shopRatingDeferred = async { getShopRatingUseCase.execute(shopRatingParam) }

                    val shopReviewParam = ProductRevGetShopReviewReadingListUseCase.Param(
                        shopID = shopId,
                        limit = FIVE_REVIEW,
                        page = FIRST_PAGE,
                        filterBy = SHOP_TOP_REVIEW_FILTER_BY_SELLER_SERVICE,
                        sortBy = SHOP_TOP_REVIEW_SORT_BY_HELPFULNESS
                    )

                    val shopReviewDeferred = async { getShopReviewUseCase.execute(shopReviewParam) }

                    val shopInfoDeferred = async { handleGetShopInfo(shopId.toIntOrZero()) }
                    val shopNotesDeferred = async { getShopNotes(shopId) }
                    val shopOperationalHoursDeferred = async { getShopOperationalHours(shopId) }

                    val shopHeaderLayout = shopHeaderLayoutDeferred.await()
                    val shopRating = shopRatingDeferred.await()
                    val shopReview = shopReviewDeferred.await()
                    val shopInfo = shopInfoDeferred.await()
                    val shopNotes = shopNotesDeferred.await()
                    val shopOperationalHours = shopOperationalHoursDeferred.await()

                    val pharmacyInfo = if (isEpharmacy(shopInfo)) {
                        getPharmacyInfo(
                            shopId = shopId.toLongOrZero(),
                            districtId = currentState.districtId.toLongOrZero()
                        )
                    } else {
                        ShopPharmacyInfo(
                            showPharmacyInfoSection = false,
                            nearestPickupAddressGmapsUrl = "",
                            nearestPickupAddress = "",
                            pharmacistName = "",
                            pharmacistOperationalHour = emptyList(),
                            siaNumber = "",
                            sipaNumber = "",
                            expandPharmacyInfo = false
                        )
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            info = ReimagineShopInfo(
                                shopImageUrl = shopInfo.shopAssets.avatar,
                                shopBadgeUrl = shopInfo.goldOS.badge,
                                shopName = shopInfo.shopCore.name,
                                shopDescription = shopInfo.shopCore.description,
                                mainLocation = shopInfo.shippingLoc.cityName,
                                operationalHours = shopOperationalHours.toFormattedOperationalHours(),
                                shopJoinDate = shopInfo.createdInfo.shopCreated.toShopJoinDate(),
                                totalProduct = shopInfo.activeProduct.toIntOrZero(),
                                shopUsp = shopHeaderLayout.shopPageGetHeaderLayout.toShopUsp(),
                                showPharmacyLicenseBadge = isEpharmacy(shopInfo)
                            ),
                            rating = shopRating,
                            review = shopReview,
                            shopNotes = shopNotes.toShopNotes(),
                            shipments = shopInfo.shipments.toShipments(),
                            pharmacy = pharmacyInfo
                        )
                    }
                }
            },
            onError = { error ->
                _uiState.update { it.copy(isLoading = false, error = error) }
            }
        )
    }

    private fun handleCtaExpandShopPharmacyInfo() {
        tracker.sendCtaExpandPharmacyInformationEvent(currentState.shopId)
        _uiState.update {
            it.copy(pharmacy = it.pharmacy.copy(expandPharmacyInfo = true))
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun handleCtaViewPharmacyLocation() {
        tracker.sendCtaViewPharmacyLocationEvent(currentState.shopId)
        val effect = ShopInfoUiEffect.RedirectToGmaps(currentState.pharmacy.nearestPickupAddressGmapsUrl)
        _uiEffect.tryEmit(effect)
    }

    private fun handleRetryGetShopInfo() {
        handleGetShopInfo()
    }

    private fun handleTapShopRating() {
        tracker.sendTapShopRatingEvent(currentState.shopId)
        val effect = ShopInfoUiEffect.RedirectToShopReviewPage(currentState.shopId)
        _uiEffect.tryEmit(effect)
    }

    private fun handleTapShopNote(shopNote: ShopNote) {
        tracker.sendClickShopNoteEvent(shopNote, currentState.shopId)
        val effect = ShopInfoUiEffect.ShowShopNoteDetailBottomSheet(shopNote)
        _uiEffect.tryEmit(effect)
    }

    private suspend fun handleGetShopInfo(shopId: Int): ShopInfo {
        getShopInfoUseCase.isFromCacheFirst = false
        getShopInfoUseCase.params = GQLGetShopInfoUseCase.createParams(
            shopIds = listOf(shopId),
            source = GQLGetShopInfoUseCase.SHOP_PAGE_SOURCE,
            fields = GQLGetShopInfoUseCase.getDefaultShopFields() + listOf(
                GQLGetShopInfoUseCase.FIELD_OS,
                GQLGetShopInfoUseCase.FIELD_GOLD_OS,
                GQLGetShopInfoUseCase.FIELD_GOLD,
                GQLGetShopInfoUseCase.FIELD_ACTIVE_PRODUCT,
                GQLGetShopInfoUseCase.FIELD_SHOP_STATS,
                GQLGetShopInfoUseCase.FIELD_OTHER_SHIPPING_LOCATION
            )
        )
        return getShopInfoUseCase.executeOnBackground()
    }

    private fun handleTapReviewImage(review: ShopReview.Review, reviewImageIndex: Int) {
        val effect = ShopInfoUiEffect.RedirectToProductReviewPage(
            review,
            currentState.shopId,
            reviewImageIndex
        )
        _uiEffect.tryEmit(effect)
    }

    private fun handleTapReviewImageViewAll(productId: String) {
        val effect = ShopInfoUiEffect.RedirectToProductReviewGallery(productId)
        _uiEffect.tryEmit(effect)
    }

    private fun handleSwipeReview(reviewIndex: Int) {
        val isReviewAlreadyImpressed = reviewIndex in impressedReviewIndex

        if (!isReviewAlreadyImpressed) {
            impressedReviewIndex.add(reviewIndex)
            tracker.sendReviewImpression(reviewIndex, currentState.shopId)
        }
    }

    private suspend fun getShopNotes(shopId: String): List<ShopNoteModel> {
        getShopNoteUseCase.params = GetShopNoteUseCase.createParams(shopId)
        getShopNoteUseCase.isFromCacheFirst = false
        return getShopNoteUseCase.executeOnBackground()
    }

    private suspend fun getShopOperationalHours(shopId: String): ShopOperationalHoursListResponse {
        getShopGqlGetShopOperationalHoursListUseCase.params = GqlGetShopOperationalHoursListUseCase.createRequestParams(shopId)
        return getShopGqlGetShopOperationalHoursListUseCase.executeOnBackground()
    }

    private suspend fun getShopPageHeaderLayout(): ShopPageHeaderLayoutResponse {
        getShopPageHeaderLayoutUseCase.params = GetShopPageHeaderLayoutUseCase.createParams(
            currentState.shopId,
            currentState.districtId,
            currentState.cityId
        )
        getShopPageHeaderLayoutUseCase.isFromCloud = true
        return getShopPageHeaderLayoutUseCase.executeOnBackground()
    }

    @SuppressLint("PII Data Exposure")
    private suspend fun getPharmacyInfo(shopId: Long, districtId: Long): ShopPharmacyInfo {
        getNearestEpharmacyWarehouseLocationUseCase.params = GetNearestEpharmacyWarehouseLocationUseCase.createParams(shopId = shopId, districtId = districtId)
        val nearestWarehouseLocation = getNearestEpharmacyWarehouseLocationUseCase.executeOnBackground()
        val nearestWarehouseId = nearestWarehouseLocation.getNearestEpharmacyWarehouseLocation.data.warehouseID

        getEpharmacyShopInfoUseCase.params = GetEpharmacyShopInfoUseCase.createParams(shopId, nearestWarehouseId)
        val ePharmacyInfo = getEpharmacyShopInfoUseCase.executeOnBackground()

        return ShopPharmacyInfo(
            showPharmacyInfoSection = true,
            nearestPickupAddress = nearestWarehouseLocation.getNearestEpharmacyWarehouseLocation.data.address,
            nearestPickupAddressGmapsUrl = nearestWarehouseLocation.getNearestEpharmacyWarehouseLocation.data.gMapsURL,
            pharmacistOperationalHour = ePharmacyInfo.getEpharmacyShopInfo.dataEpharm.epharmacyWorkingHoursFmt,
            pharmacistName = ePharmacyInfo.getEpharmacyShopInfo.dataEpharm.apj,
            siaNumber = ePharmacyInfo.getEpharmacyShopInfo.dataEpharm.siaNumber,
            sipaNumber = ePharmacyInfo.getEpharmacyShopInfo.dataEpharm.sipaNumber,
            expandPharmacyInfo = false
        )
    }

    private fun handleReportShop() {
        if (!userSessionInterface.isLoggedIn) {
            _uiEffect.tryEmit(ShopInfoUiEffect.RedirectToLoginPage)
            return
        }

        launchCatchError(
            coroutineDispatcherProvider.io,
            block = {
                _uiState.update { it.copy(isLoadingShopReport = true) }

                getMessageIdChatUseCase.params = GetMessageIdChatUseCase.createParams(currentState.shopId)
                val response = getMessageIdChatUseCase.executeOnBackground()
                val messageId = response.chatExistingChat.messageId

                _uiEffect.tryEmit(ShopInfoUiEffect.RedirectToChatWebView(messageId))
                _uiState.update { it.copy(isLoadingShopReport = false) }
            },
            onError = {
                _uiState.update { it.copy(isLoadingShopReport = false) }
            }
        )
    }

    private fun List<ShopNoteModel>.toShopNotes(): List<ShopNote> {
        return map { shopNote ->
            ShopNote(
                id = shopNote.id.orEmpty(),
                title = shopNote.title.orEmpty(),
                description = shopNote.content.orEmpty()
            )
        }
    }

    private fun List<ShopShipment>.toShipments(): List<ShopSupportedShipment> {
        return map { shipment ->
            ShopSupportedShipment(
                title = shipment.name,
                imageUrl = shipment.image,
                serviceNames = shipment.product.filter { it.uiHidden.not() }.map { it.name }
            )
        }
    }

    private fun ShopOperationalHoursListResponse.toFormattedOperationalHours(): Map<String, List<ShopOperationalHourWithStatus>> {
        val unformattedOperationalHours = getShopOperationalHoursList?.data

        return when {
            unformattedOperationalHours == null -> emptyMap()
            unformattedOperationalHours.isEmpty() -> emptyMap()
            else -> determineOperationalHours(unformattedOperationalHours)
        }
    }

    private fun determineOperationalHours(
        unformattedOperationalHours: List<ShopOperationalHour>
    ): Map<String, List<ShopOperationalHourWithStatus>> {
        val formattedOperationalHours = mutableListOf<ShopOperationalHourWithStatus>()
        unformattedOperationalHours.forEach { operationalHour ->
            val day = ShopOperationalHourWithStatus.Day.values().firstOrNull { it.id == operationalHour.day } ?: ShopOperationalHourWithStatus.Day.UNDEFINED

            val isShopOpenTwentyFourHours = isShopOpenTwentyFourHours(
                operationalHour.startTime,
                operationalHour.endTime
            )
            val isShopClosed = isShopClosed(operationalHour.startTime, operationalHour.endTime)

            val startTime = operationalHour.startTime.hourAndMinuteOnly()
            val endTime = operationalHour.endTime.hourAndMinuteOnly()

            val status = if (isShopOpenTwentyFourHours) {
                ShopOperationalHourWithStatus(day, startTime, endTime, ShopOperationalHourWithStatus.Status.OPEN24HOURS)
            } else if (isShopClosed) {
                ShopOperationalHourWithStatus(day, startTime, endTime, ShopOperationalHourWithStatus.Status.CLOSED)
            } else {
                ShopOperationalHourWithStatus(day, startTime, endTime, ShopOperationalHourWithStatus.Status.OPEN)
            }

            formattedOperationalHours.add(status)
        }

        val groupedResult = formattedOperationalHours.groupByShopOpenAndCloseTime()

        return groupedResult
    }

    private fun ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.toShopUsp(): List<String> {
        val shopBasicInfo = this.widgets.firstOrNull { it.name == "shop_basic_info" }
        if (shopBasicInfo == null) return emptyList()

        val shopBasicInfoListComponent = shopBasicInfo.listComponent
        val shopAttributeList = shopBasicInfoListComponent.firstOrNull { it.name == "shop_attribute_list" }
        if (shopAttributeList == null) return emptyList()

        val listText = shopAttributeList.data.listText.take(MAX_SHOP_DYNAMIC_USP_TO_DISPLAY)
        val textHtmls = listText.map { it.textHtml }
        return textHtmls
    }

    private fun isEpharmacy(shopInfo: ShopInfo): Boolean {
        val hasPharmacyFulfilmentService = shopInfo.partnerInfo.any { partnerInfo ->
            partnerInfo.fsType == ID_FULFILLMENT_SERVICE_E_PHARMACY
        }

        return hasPharmacyFulfilmentService || shopInfo.isGoApotik
    }

    private fun List<ShopOperationalHourWithStatus>.groupByShopOpenAndCloseTime(): Map<String, List<ShopOperationalHourWithStatus>> {
        return this.groupBy {
            val openCloseTime = it.startTime + ":" + it.endTime
            openCloseTime
        }
    }

    private fun String.hourAndMinuteOnly(): String {
        return toDate(DateTimeConstant.TIME_SECOND_PRECISION).formatTo(DateTimeConstant.TIME_MINUTE_PRECISION)
    }

    private fun String.toShopJoinDate(): String {
        return toDate(DateUtil.YYYY_MM_DD).formatTo(DateUtil.DEFAULT_VIEW_FORMAT)
    }

    private fun isShopOpenTwentyFourHours(startTime: String, endTime: String): Boolean {
        val start = startTime.toDate(DateTimeConstant.TIME_SECOND_PRECISION)
        val end = endTime.toDate(DateTimeConstant.TIME_SECOND_PRECISION)

        val timeDifferenceMillis = end.time - start.time
        val timeDifferenceSecond = TimeUnit.MILLISECONDS.toSeconds(timeDifferenceMillis).toInt()

        return timeDifferenceSecond == TWENTY_THREE_HOURS_FIFTY_NINE_MINUTE_IN_SECOND || timeDifferenceSecond == TWENTY_THREE_HOURS_FIFTY_NINE_MINUTE_AND_FIFTY_NINE_SECOND_IN_SECOND
    }

    private fun isShopClosed(startTime: String, endTime: String): Boolean {
        val start = startTime.toDate(DateTimeConstant.TIME_SECOND_PRECISION)
        val end = endTime.toDate(DateTimeConstant.TIME_SECOND_PRECISION)

        val timeDifferenceMillis = end.time - start.time
        val timeDifferenceSecond = TimeUnit.MILLISECONDS.toSeconds(timeDifferenceMillis).toInt()

        return timeDifferenceSecond == Int.ZERO
    }
}
