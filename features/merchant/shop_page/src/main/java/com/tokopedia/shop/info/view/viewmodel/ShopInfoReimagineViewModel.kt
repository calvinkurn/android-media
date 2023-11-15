package com.tokopedia.shop.info.view.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.common.domain.GetMessageIdChatUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.extension.toDate
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopShipment
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.common.graphql.data.shopnote.gql.GetShopNoteUseCase
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHoursListResponse
import com.tokopedia.shop.common.util.DateTimeConstant
import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopPerformance
import com.tokopedia.shop.info.domain.entity.ShopPharmacyInfo
import com.tokopedia.shop.info.domain.entity.ShopStatsRawData
import com.tokopedia.shop.info.domain.entity.ShopSupportedShipment
import com.tokopedia.shop.info.domain.usecase.GetEpharmacyShopInfoUseCase
import com.tokopedia.shop.info.domain.usecase.GetNearestEpharmacyWarehouseLocationUseCase
import com.tokopedia.shop.info.domain.usecase.GetShopStatsRawDataUseCase
import com.tokopedia.shop.info.domain.usecase.ProductRevGetShopRatingAndTopicsUseCase
import com.tokopedia.shop.info.domain.usecase.ProductRevGetShopReviewReadingListUseCase
import com.tokopedia.shop.info.view.model.ShopInfoUiEffect
import com.tokopedia.shop.info.view.model.ShopInfoUiEvent
import com.tokopedia.shop.info.view.model.ShopInfoUiState
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderLayoutResponse
import com.tokopedia.shop.pageheader.domain.interactor.GetShopPageHeaderLayoutUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.supervisorScope
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
    private val getShopStatsRawDataUseCase: GetShopStatsRawDataUseCase
) : ViewModel() {

    companion object {
        private const val ID_FULFILLMENT_SERVICE_E_PHARMACY = 2
        private const val FIVE_REVIEW = 5
    }

    private val _uiState = MutableStateFlow(ShopInfoUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ShopInfoUiEffect>(replay = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val currentState: ShopInfoUiState
        get() = _uiState.value

    fun processEvent(event: ShopInfoUiEvent) {
        when (event) {
            is ShopInfoUiEvent.Setup -> handleSetup(event.shopId, event.districtId, event.cityId)
            is ShopInfoUiEvent.GetShopInfo -> handleGetShopInfo()
            ShopInfoUiEvent.TapCtaExpandShopPharmacyInfo -> handleCtaExpandShopPharmacyInfo()
            ShopInfoUiEvent.TapCtaViewPharmacyLocation -> handleCtaViewPharmacyLocation()
            ShopInfoUiEvent.TapIconViewAllShopReview -> handleViewAllReviewClick()
            is ShopInfoUiEvent.RetryGetShopInfo -> handleRetryGetShopInfo()
            is ShopInfoUiEvent.TapShopNote -> handleTapShopNote(event.noteId)
            ShopInfoUiEvent.ReportShop -> handleReportShop()
            is ShopInfoUiEvent.TapReviewImage -> handleTapReviewImage(event.productId)
            is ShopInfoUiEvent.TapReviewImageViewAll -> handleTapReviewImageViewAll(event.productId)
        }
    }

    private fun handleSetup(shopId: String, districtId: String, cityId: String) {
        _uiState.update {
            it.copy(
                shopId = shopId,
                districtId = districtId,
                cityId = cityId
            )
        }
    }
    private fun handleGetShopInfo() {
        viewModelScope.launchCatchError(
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
                        page = Int.ONE,
                        filterBy = "topic=pelayanan",
                        sortBy = "informative_score desc"
                    )

                    val shopReviewDeferred = async { getShopReviewUseCase.execute(shopReviewParam) }

                    val shopInfoDeferred = async { handleGetShopInfo(shopId.toIntOrZero()) }
                    val shopNotesDeferred = async { getShopNotes(shopId) }
                    val shopOperationalHoursDeferred = async { getShopOperationalHours(shopId) }
                    val shopChatPerformanceDeferred = async { getShopChatPerformance(shopId) }

                    val shopHeaderLayout = shopHeaderLayoutDeferred.await()
                    val shopRating = shopRatingDeferred.await()
                    val shopReview = shopReviewDeferred.await()
                    val shopInfo = shopInfoDeferred.await()
                    val shopNotes = shopNotesDeferred.await()
                    val shopOperationalHours = shopOperationalHoursDeferred.await()
                    val shopChatPerformance = shopChatPerformanceDeferred.await()

                    val pharmacyInfo = if (isEpharmacy(shopInfo)) {
                        getPharmacyInfo(shopId.toLongOrZero(), currentState.districtId.toLongOrZero())
                    } else {
                        ShopPharmacyInfo(
                            showPharmacyInfoSection = false,
                            nearPickupAddressGmapsUrl = "",
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
                                mainLocation = shopInfo.location,
                                operationalHours = shopOperationalHours.toFormattedOperationalHours(),
                                shopJoinDate = shopInfo.createdInfo.shopCreated.toShopJoinDate(),
                                totalProduct = shopInfo.activeProduct.toIntOrZero(),
                                shopUsp = shopHeaderLayout.shopPageGetHeaderLayout.toShopUsp(),
                                showPharmacyLicenseBadge = isEpharmacy(shopInfo)
                            ),
                            rating = shopRating,
                            review = shopReview,
                            shopPerformance = ShopPerformance(
                                totalProductSoldCount = shopInfo.shopStats.productSold,
                                chatPerformance = shopChatPerformance.toChatPerformance(), // TODO replace with real data
                                orderProcessTime = shopHeaderLayout.shopPageGetHeaderLayout.toOrderProcessTime()
                            ),
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
        _uiState.update {
            it.copy(pharmacy = it.pharmacy.copy(expandPharmacyInfo = true))
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun handleCtaViewPharmacyLocation() {
        val effect =
            ShopInfoUiEffect.RedirectToGmaps(currentState.pharmacy.nearPickupAddressGmapsUrl)
        _uiEffect.tryEmit(effect)
    }

    private fun handleRetryGetShopInfo() {
        handleGetShopInfo()
    }

    private fun handleViewAllReviewClick() {
        val effect = ShopInfoUiEffect.RedirectToShopReviewPage(currentState.shopId)
        _uiEffect.tryEmit(effect)
    }

    private fun handleTapShopNote(noteId: String) {
        val effect = ShopInfoUiEffect.RedirectToShopNoteDetailPage(currentState.shopId, noteId)
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
                GQLGetShopInfoUseCase.FIELD_SHOP_STATS
            )
        )
        return getShopInfoUseCase.executeOnBackground()
    }

    private fun handleTapReviewImage(productId: String) {
        val effect = ShopInfoUiEffect.RedirectToProductReviewPage(productId)
        _uiEffect.tryEmit(effect)
    }

    private fun handleTapReviewImageViewAll(productId: String) {
        val effect = ShopInfoUiEffect.RedirectToProductReviewPage(productId)
        _uiEffect.tryEmit(effect)
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
        return try {
            getNearestEpharmacyWarehouseLocationUseCase.params = GetNearestEpharmacyWarehouseLocationUseCase.createParams(shopId = shopId, districtId = districtId)
            val nearestWarehouseLocation = getNearestEpharmacyWarehouseLocationUseCase.executeOnBackground()
            val nearestWarehouseId = nearestWarehouseLocation.getNearestEpharmacyWarehouseLocation.data.warehouseID

            getEpharmacyShopInfoUseCase.params = GetEpharmacyShopInfoUseCase.createParams(shopId, nearestWarehouseId)
            val ePharmacyInfo = getEpharmacyShopInfoUseCase.executeOnBackground()

            ShopPharmacyInfo(
                showPharmacyInfoSection = true,
                nearestPickupAddress = nearestWarehouseLocation.getNearestEpharmacyWarehouseLocation.data.address,
                nearPickupAddressGmapsUrl = nearestWarehouseLocation.getNearestEpharmacyWarehouseLocation.data.gMapsURL,
                pharmacistOperationalHour = ePharmacyInfo.getEpharmacyShopInfo.dataEpharm.epharmacyWorkingHoursFmt,
                pharmacistName = ePharmacyInfo.getEpharmacyShopInfo.dataEpharm.apj,
                siaNumber = ePharmacyInfo.getEpharmacyShopInfo.dataEpharm.siaNumber,
                sipaNumber = ePharmacyInfo.getEpharmacyShopInfo.dataEpharm.sipaNumber,
                expandPharmacyInfo = false
            )
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            ShopPharmacyInfo(
                showPharmacyInfoSection = false,
                nearPickupAddressGmapsUrl = "",
                nearestPickupAddress = "",
                pharmacistName = "",
                pharmacistOperationalHour = emptyList(),
                siaNumber = "",
                sipaNumber = "",
                expandPharmacyInfo = false
            )
        }
    }

    private suspend fun getShopChatPerformance(shopId: String): ShopStatsRawData {
        val param = GetShopStatsRawDataUseCase.Param(shopId = shopId, source = "shop_info_page")
        return getShopStatsRawDataUseCase.execute(param)
    }

    private fun handleReportShop() {
        if (!userSessionInterface.isLoggedIn) {
            _uiEffect.tryEmit(ShopInfoUiEffect.RedirectToLoginPage)
            return
        }

        viewModelScope.launchCatchError(
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
            ShopNote(id = shopNote.id.orEmpty(), title = shopNote.title.orEmpty())
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

    private fun ShopOperationalHoursListResponse.toFormattedOperationalHours(): Map<String, List<String>> {
        val operationalHours = getShopOperationalHoursList?.data ?: emptyList()

        val formattedOperationalHours = mutableMapOf<String, String>()

        val daysDictionary = mapOf(
            1 to "Senin",
            2 to "Selasa",
            3 to "Rabu",
            4 to "Kamis",
            5 to "Jumat",
            6 to "Sabtu",
            7 to "Minggu"
        )

        operationalHours.forEach { operationalHour ->
            val formattedDay = daysDictionary[operationalHour.day].orEmpty()

            val startTime = operationalHour.startTime.hourAndMinuteOnly()
            val endTime = operationalHour.endTime.hourAndMinuteOnly()
            val operationalHourFormat = "%s - %s"

            formattedOperationalHours[formattedDay] = String.format(operationalHourFormat, startTime, endTime)
        }

        val result = formattedOperationalHours.groupByHours()

        return result
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

    private fun isEpharmacy(shopInfo: ShopInfo): Boolean {
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

    private fun String.hourAndMinuteOnly(): String {
        return toDate(DateTimeConstant.TIME_SECOND_PRECISION).formatTo(DateTimeConstant.TIME_MINUTE_PRECISION)
    }

    private fun String.toShopJoinDate(): String {
        return toDate(DateTimeConstant.DATE_TIME_DAY_PRECISION).formatTo(DateTimeConstant.DATE_TIME_YEAR_PRECISION)
    }

    private fun ShopStatsRawData.toChatPerformance(): String {
        val chatAndDiscussionReplySpeedMinute = chatAndDiscussionReplySpeed.toInt()
        val chatAndDiscussionReplySpeedHour = chatAndDiscussionReplySpeedMinute / 60
        val chatAndDiscussionReplySpeedDay = chatAndDiscussionReplySpeedHour / 24

        return when {
            chatAndDiscussionReplySpeedMinute in 0..1 -> "±1 menit"
            chatAndDiscussionReplySpeedMinute in 1..59 -> "±$chatAndDiscussionReplySpeedMinute menit"
            chatAndDiscussionReplySpeedMinute == 60 -> "1 jam"
            chatAndDiscussionReplySpeedHour in 1..23 -> "±$chatAndDiscussionReplySpeedHour jam"
            chatAndDiscussionReplySpeedHour == 24 -> "1 hari"
            chatAndDiscussionReplySpeedDay > 24 -> "$chatAndDiscussionReplySpeedDay hari"
            else -> ""
        }
    }
}
