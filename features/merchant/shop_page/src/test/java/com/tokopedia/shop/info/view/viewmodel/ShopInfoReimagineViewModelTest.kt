package com.tokopedia.shop.info.view.viewmodel

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.domain.GetMessageIdChatUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ChatExistingChat
import com.tokopedia.shop.common.graphql.data.shopinfo.ChatMessageId
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopShipment
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.common.graphql.data.shopnote.gql.GetShopNoteUseCase
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.GetShopOperationalHoursList
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHour
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHoursListResponse
import com.tokopedia.shop.info.data.response.GetEpharmacyShopInfoResponse
import com.tokopedia.shop.info.data.response.GetNearestEpharmacyWarehouseLocationResponse
import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.shop.info.domain.entity.ShopOperationalHourWithStatus
import com.tokopedia.shop.info.domain.entity.ShopRating
import com.tokopedia.shop.info.domain.entity.ShopReview
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
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ShopInfoReimagineViewModelTest {
    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var getShopInfoUseCase: GQLGetShopInfoUseCase

    @RelaxedMockK
    lateinit var getShopNoteUseCase: GetShopNoteUseCase

    @RelaxedMockK
    lateinit var getShopRatingUseCase: ProductRevGetShopRatingAndTopicsUseCase

    @RelaxedMockK
    lateinit var getShopReviewUseCase: ProductRevGetShopReviewReadingListUseCase

    @RelaxedMockK
    lateinit var getShopGqlGetShopOperationalHoursListUseCase: GqlGetShopOperationalHoursListUseCase

    @RelaxedMockK
    lateinit var getShopPageHeaderLayoutUseCase: GetShopPageHeaderLayoutUseCase

    @RelaxedMockK
    lateinit var getEpharmacyShopInfoUseCase: GetEpharmacyShopInfoUseCase

    @RelaxedMockK
    lateinit var getNearestEpharmacyWarehouseLocationUseCase: GetNearestEpharmacyWarehouseLocationUseCase

    @RelaxedMockK
    lateinit var getMessageIdChatUseCase: GetMessageIdChatUseCase

    @RelaxedMockK
    lateinit var getShopStatsRawDataUseCase: GetShopStatsRawDataUseCase

    private lateinit var viewModel: ShopInfoReimagineViewModel

    private val shopId = "955452"
    private val districtId = "12"
    private val cityId = "1"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopInfoReimagineViewModel(
            userSessionInterface,
            CoroutineTestDispatchersProvider,
            getShopInfoUseCase,
            getShopNoteUseCase,
            getShopRatingUseCase,
            getShopReviewUseCase,
            getShopGqlGetShopOperationalHoursListUseCase,
            getShopPageHeaderLayoutUseCase,
            getEpharmacyShopInfoUseCase,
            getNearestEpharmacyWarehouseLocationUseCase,
            getMessageIdChatUseCase
        )
    }

    @Test
    fun `when Setup called, should set shopId, districtId and cityId correctly`() {
        runBlockingTest {
            // Given
            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))

            // Then
            val actual = emittedValues.last()

            assertEquals(shopId, actual.shopId)
            assertEquals(districtId, actual.districtId)
            assertEquals(cityId, actual.cityId)

            job.cancel()
        }
    }

    //region Shop info GQL
    @Test
    fun `when GetShopInfo event triggered and all GQL calls success, isLoading should be false and error should be null`() {
        runBlockingTest {
            // Given
            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)

            // Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isLoading)
            assertEquals(null, actual.error)

            job.cancel()
        }
    }

    @Test
    fun `when get shop info success, should return active shop supported shipment only`() {
        runBlockingTest {
            // Given
            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall(
                response = ShopInfo(
                    shipments = listOf(
                        ShopShipment(
                            name = "Gosend",
                            image = "https://images.tokopedia.net/gosend.jpg",
                            product = listOf(
                                ShopShipment.ShipmentProduct(name = "Same Day", uiHidden = false),
                                ShopShipment.ShipmentProduct(name = "Instant", uiHidden = false),
                                ShopShipment.ShipmentProduct(name = "Saver", uiHidden = true)
                            )
                        )
                    )
                )
            )
            mockGetShopNoteGqlCall(response = listOf(ShopNoteModel(id = "1", title = "Syarat dan ketentuan pengiriman")))
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)

            // Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    ShopSupportedShipment(
                        title = "Gosend",
                        imageUrl = "https://images.tokopedia.net/gosend.jpg",
                        serviceNames = listOf("Same Day", "Instant")
                    )
                ),
                actual.shipments
            )

            job.cancel()
        }
    }

    @Test
    fun `when GetShopInfo event triggered and review GQL call failed, error should be set`() {
        runBlockingTest {
            // Given
            val error = MessageErrorException("Internal server error")
            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()

            val requestParam = ProductRevGetShopReviewReadingListUseCase.Param(
                shopID = shopId,
                limit = ShopInfoReimagineViewModel.FIVE_REVIEW,
                page = ShopInfoReimagineViewModel.FIRST_PAGE,
                filterBy = ShopInfoReimagineViewModel.SHOP_TOP_REVIEW_FILTER_BY_SELLER_SERVICE,
                sortBy = ShopInfoReimagineViewModel.SHOP_TOP_REVIEW_SORT_BY_HELPFULNESS
            )
            coEvery { getShopReviewUseCase.execute(requestParam) } throws error

            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)

            // Then
            val actual = emittedValues.last()
            assertEquals(false, actual.isLoading)
            assertEquals(error, actual.error)

            job.cancel()
        }
    }
    //endregion

    //region Shop notes
    @Test
    fun `when get shop notes success, should return shop notes`() {
        runBlockingTest {
            // Given
            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall(
                response = listOf(
                    ShopNoteModel(id = "1", title = "Syarat dan ketentuan pengiriman", content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"),
                    ShopNoteModel(id = "2", title = "Kebijakan pengembalian", content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua")
                )
            )
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)

            // Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    ShopNote(id = "1", title = "Syarat dan ketentuan pengiriman", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"),
                    ShopNote(id = "2", title = "Kebijakan pengembalian", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua")
                ),
                actual.shopNotes
            )

            job.cancel()
        }
    }

    @Test
    fun `when get shop notes success but id and title is null then should convert id and title to empty string`() {
        runBlockingTest {
            // Given
            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall(
                response = listOf(
                    ShopNoteModel(id = "1", title = "Syarat dan ketentuan pengiriman", content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"),
                    ShopNoteModel(id = null, title = null, content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua")
                )
            )
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)

            // Then
            val actual = emittedValues.last()

            assertEquals(
                listOf(
                    ShopNote(id = "1", title = "Syarat dan ketentuan pengiriman", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"),
                    ShopNote(id = "", title = "", description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua")
                ),
                actual.shopNotes
            )

            job.cancel()
        }
    }
    //endregion

    //region Shop pharmacy
    @Test
    fun `When receive isGoApotik = true from backend while fulfilment service type not GoApotik then showPharmacyLicenseBadge should be false`() {
        runBlockingTest {
            // Given
            val incorrectGoApotikFulfillmentServiceId = 3
            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall(
                response = ShopInfo(
                    isGoApotik = true,
                    partnerInfo = listOf(
                        ShopInfo.PartnerInfoData(
                            partnerName = "GoApotik",
                            fsType = incorrectGoApotikFulfillmentServiceId
                        )
                    )
                )
            )
            mockGetShopNoteGqlCall()
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)

            // Then
            val actual = emittedValues.last()
            assertEquals(false, actual.info.showPharmacyLicenseBadge)

            job.cancel()
        }
    }

    @Test
    fun `When shop is pharmacy should trigger nearest pharmacy and pharmacy info gql call`() {
    }

    @Test
    fun `When shop is not pharmacy should not trigger nearest pharmacy and pharmacy info gql call`() {
    }

    @Test
    fun `When shop is pharmacy and call to nearest pharmacy and pharmacy info gql call return success should update correct to ui state`() {
    }

    //endregion

    //region Shop operational hours

    @Test
    fun `When successfully get operational hours, should group days by its start and end time`() {
        runBlockingTest {
            // Given

            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()
            
            //Mock the data to simulate following condition: 
            // Mon, Tue, Wed, Sat, Sund: Open, 
            // Thu: Open 24 Hours, 
            // Fri: Closed 
            
            val response = ShopOperationalHoursListResponse(
                getShopOperationalHoursList = GetShopOperationalHoursList(
                    data = listOf(
                        ShopOperationalHour(day = 1, startTime = "08:00:00", endTime = "17:00:00"),
                        ShopOperationalHour(day = 2, startTime = "08:00:00", endTime = "17:00:00"),
                        ShopOperationalHour(day = 3, startTime = "08:00:00", endTime = "17:00:00"),
                        ShopOperationalHour(day = 4, startTime = "00:00:00", endTime = "23:59:59"),
                        ShopOperationalHour(day = 5, startTime = "00:00:00", endTime = "00:00:00"),
                        ShopOperationalHour(day = 6, startTime = "10:00:00", endTime = "15:00:00"),
                        ShopOperationalHour(day = 7, startTime = "10:00:00", endTime = "12:00:00")
                    )
                )
            )
            mockGetShopOperationListGqlCall(response = response)
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)

            // Then
            val actual = emittedValues.last()
            assertEquals(
                mapOf(
                    "08:00:17:00" to listOf(
                        ShopOperationalHourWithStatus(
                            ShopOperationalHourWithStatus.Day.MONDAY,
                            "08:00",
                            "17:00",
                            ShopOperationalHourWithStatus.Status.OPEN
                        ),
                        ShopOperationalHourWithStatus(
                            ShopOperationalHourWithStatus.Day.TUESDAY,
                            "08:00",
                            "17:00",
                            ShopOperationalHourWithStatus.Status.OPEN
                        ),
                        ShopOperationalHourWithStatus(
                            ShopOperationalHourWithStatus.Day.WEDNESDAY,
                            "08:00",
                            "17:00",
                            ShopOperationalHourWithStatus.Status.OPEN
                        )
                    ),
                    "00:00:23:59" to listOf(
                        ShopOperationalHourWithStatus(
                            ShopOperationalHourWithStatus.Day.THURSDAY,
                            "00:00",
                            "23:59",
                            ShopOperationalHourWithStatus.Status.OPEN24HOURS
                        )
                    ),
                    "00:00:00:00" to listOf(
                        ShopOperationalHourWithStatus(
                            ShopOperationalHourWithStatus.Day.FRIDAY,
                            "00:00",
                            "00:00",
                            ShopOperationalHourWithStatus.Status.CLOSED
                        )
                    ),
                    "10:00:15:00" to listOf(
                        ShopOperationalHourWithStatus(
                            ShopOperationalHourWithStatus.Day.SATURDAY,
                            "10:00",
                            "15:00",
                            ShopOperationalHourWithStatus.Status.OPEN
                        )
                    ),
                    "10:00:12:00" to listOf(
                        ShopOperationalHourWithStatus(
                            ShopOperationalHourWithStatus.Day.SUNDAY,
                            "10:00",
                            "12:00",
                            ShopOperationalHourWithStatus.Status.OPEN
                        )
                    )
                ),
                actual.info.operationalHours
            )

            job.cancel()
        }
    }


    @Test
    fun `When get operational hours success but it returns null for data then should return empty operational hours map`() {
        runBlockingTest {
            // Given

            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()
            val response = ShopOperationalHoursListResponse(
                getShopOperationalHoursList = GetShopOperationalHoursList(data = null)
            )
            mockGetShopOperationListGqlCall(response = response)
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)

            // Then
            val actual = emittedValues.last()
            assertEquals(
                emptyMap<String, List<String>>(),
                actual.info.operationalHours
            )

            job.cancel()
        }
    }

    @Test
    fun `When get operational hours success but it returns null then should return empty operational hours map`() {
        runBlockingTest {
            // Given

            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()
            val response = ShopOperationalHoursListResponse(
                getShopOperationalHoursList = null
            )
            mockGetShopOperationListGqlCall(response = response)
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)

            // Then
            val actual = emittedValues.last()
            assertEquals(
                emptyMap<String, List<String>>(),
                actual.info.operationalHours
            )

            job.cancel()
        }
    }

    @Test
    fun ` When get operational hours success but the returned day not defined on app days dictionary then return empty string`() {
        runBlockingTest {
            // Given

            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()

            val response = ShopOperationalHoursListResponse(
                getShopOperationalHoursList = GetShopOperationalHoursList(
                    data = listOf(
                        ShopOperationalHour(day = 4, startTime = "00:00:00", endTime = "23:59:59"),
                        ShopOperationalHour(day = 5, startTime = "00:00:00", endTime = "00:00:00"),
                        ShopOperationalHour(day = 10, startTime = "10:00:00", endTime = "12:00:00")
                    )
                )
            )
            mockGetShopOperationListGqlCall(response = response)
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)

            // Then
            val actual = emittedValues.last()
            assertEquals(
                mapOf(
                    "00:00:23:59" to listOf(
                        ShopOperationalHourWithStatus(
                            ShopOperationalHourWithStatus.Day.THURSDAY,
                            "00:00",
                            "23:59",
                            ShopOperationalHourWithStatus.Status.OPEN24HOURS
                        )
                    ),
                    "00:00:00:00" to listOf(
                        ShopOperationalHourWithStatus(
                            ShopOperationalHourWithStatus.Day.FRIDAY,
                            "00:00",
                            "00:00",
                            ShopOperationalHourWithStatus.Status.CLOSED
                        )
                    ),
                    "10:00:12:00" to listOf(
                        ShopOperationalHourWithStatus(
                            ShopOperationalHourWithStatus.Day.UNDEFINED,
                            "10:00",
                            "12:00",
                            ShopOperationalHourWithStatus.Status.OPEN
                        )
                    )
                ),
                actual.info.operationalHours
            )

            job.cancel()
        }
    }

    //endregion

    //region Format Shop USP
    @Test
    fun `When get shop info success and there are shop_basic info and shop_attribute_list then should return dynamic usp value correctly`() {
        runBlockingTest {
            // Given
            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            val response = ShopPageHeaderLayoutResponse(
                shopPageGetHeaderLayout = ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout(
                    generalComponentConfigList = emptyList(),
                    isOverrideTheme = false,
                    widgets = listOf(
                        ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget(
                            name = "shop_basic_info",
                            listComponent = listOf(
                                ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component(
                                    name = "shop_attribute_list",
                                    data = ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component.Data(
                                        listText = listOf(
                                            ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component.Text(
                                                textHtml = "Toko pilihan"
                                            ),
                                            ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component.Text(
                                                textHtml = "Dijamin Original"
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
            mockGetShopPageHeaderGqlCall(response = response)
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)

            // Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isLoading)
            assertEquals(listOf("Toko pilihan", "Dijamin Original"), actual.info.shopUsp)

            job.cancel()
        }
    }

    @Test
    fun `When get shop info success and there are no shop_basic_info then should return empty list of dynamic usp`() {
        runBlockingTest {
            // Given
            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            val response = ShopPageHeaderLayoutResponse(
                shopPageGetHeaderLayout = ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout(
                    generalComponentConfigList = emptyList(),
                    isOverrideTheme = false,
                    widgets = listOf(
                        ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget(
                            name = "shop_basic_info",
                            listComponent = listOf(
                                ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component(
                                    name = "shop_component",
                                    data = ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component.Data(
                                        listText = listOf(
                                            ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component.Text(
                                                textHtml = "Toko pilihan"
                                            ),
                                            ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget.Component.Text(
                                                textHtml = "Dijamin Original"
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
            mockGetShopPageHeaderGqlCall(response = response)
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)

            // Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isLoading)
            assertEquals(emptyList<String>(), actual.info.shopUsp)

            job.cancel()
        }
    }

    @Test
    fun `When get shop info success and there are no shop_attribute_list then should return empty list of dynamic usp`() {
        runBlockingTest {
            // Given
            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            val response = ShopPageHeaderLayoutResponse(
                shopPageGetHeaderLayout = ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout(
                    generalComponentConfigList = emptyList(),
                    isOverrideTheme = false,
                    widgets = listOf(
                        ShopPageHeaderLayoutResponse.ShopPageGetHeaderLayout.Widget(
                            name = "shop_info",
                            listComponent = listOf()
                        )
                    )
                )
            )
            mockGetShopPageHeaderGqlCall(response = response)
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)

            // Then
            val actual = emittedValues.last()

            assertEquals(false, actual.isLoading)
            assertEquals(emptyList<String>(), actual.info.shopUsp)

            job.cancel()
        }
    }
    //endregion

    //region Click event
    @Test
    fun `When TapCtaExpandShopPharmacyInfo event triggered, should update expandPharmacyInfo to true`() {
        runBlockingTest {
            // Given
            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)
            viewModel.processEvent(ShopInfoUiEvent.TapCtaExpandShopPharmacyInfo)

            // Then
            val actual = emittedValues.last()
            assertEquals(true, actual.pharmacy.expandPharmacyInfo)

            job.cancel()
        }
    }

    @Test fun `When TapCtaViewPharmacyLocation event triggered, UI should receive RedirectToGmaps effect`() {
        runBlockingTest {
            // Given
            val gmapsUrl = "https://map.google.com/apotekkimiafarma"
            val emittedEffects = arrayListOf<ShopInfoUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall(
                response = ShopInfo(
                    isGoApotik = true,
                    partnerInfo = listOf(ShopInfo.PartnerInfoData(partnerName = "GoApotik", fsType = 2))
                )
            )
            mockGetShopNoteGqlCall()
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()

            val nearestPharmacyResponse = GetNearestEpharmacyWarehouseLocationResponse(
                getNearestEpharmacyWarehouseLocation = GetNearestEpharmacyWarehouseLocationResponse.NearestEpharmacyData(
                    data = GetNearestEpharmacyWarehouseLocationResponse.NearestEpharmacyData.GetNearestEpharmacyWarehouseLocationDetailData(
                        gMapsURL = gmapsUrl
                    )
                )
            )

            mockGetNearestPharmacyGqlCall(response = nearestPharmacyResponse)
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)
            viewModel.processEvent(
                ShopInfoUiEvent.TapCtaViewPharmacyLocation
            )

            // Then
            val actual = emittedEffects.last()

            assertEquals(ShopInfoUiEffect.RedirectToGmaps(gmapsUrl), actual)

            job.cancel()
        }
    }

    @Test
    fun `When RetryGetShopInfo event triggered, should call get shop info gql`() {
        runBlockingTest {
            // Given
            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.RetryGetShopInfo)

            // Then
            coVerify { getShopInfoUseCase.executeOnBackground() }
        }
    }

    @Test
    fun `When TapIconViewAllShopReview event triggered, UI should receive RedirectToShopReviewPage effect`() {
        runBlockingTest {
            // Given
            val emittedEffects = arrayListOf<ShopInfoUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)
            viewModel.processEvent(
                ShopInfoUiEvent.TapIconViewAllShopReview
            )

            // Then
            val actual = emittedEffects.last()
            assertEquals(ShopInfoUiEffect.RedirectToShopReviewPage(shopId), actual)
            job.cancel()
        }
    }

    @Test
    fun `When TapShopNote event triggered, UI should receive RedirectToShopNoteDetailPage effect`() {
        runBlockingTest {
            // Given
            val shopNote = ShopNote(
                id = "1",
                title = "Syarat dan ketentuan pengiriman",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua"
            )
            val selectedNoteId = "122"
            val emittedEffects = arrayListOf<ShopInfoUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)
            viewModel.processEvent(ShopInfoUiEvent.TapShopNote(shopNote))

            // Then
            val actual = emittedEffects.last()
            assertEquals(ShopInfoUiEffect.ShowShopNoteDetailBottomSheet(shopNote), actual)
            job.cancel()
        }
    }

    @Test
    fun `When TapReviewImage event triggered, UI should receive RedirectToProductReviewPage effect`() {
        runBlockingTest {
            // Given
            val selectedProductId = "133"
            val emittedEffects = arrayListOf<ShopInfoUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)
            viewModel.processEvent(ShopInfoUiEvent.TapReviewImage(selectedProductId))

            // Then
            val actual = emittedEffects.last()
            assertEquals(ShopInfoUiEffect.RedirectToProductReviewPage(selectedProductId), actual)
            job.cancel()
        }
    }

    @Test
    fun `When TapReviewImageViewAll event triggered, UI should receive RedirectToProductReviewPage effect`() {
        runBlockingTest {
            // Given
            val selectedProductId = "133"
            val emittedEffects = arrayListOf<ShopInfoUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            mockGetShopPageHeaderGqlCall()
            mockGetShopRatingGqlCall()
            mockGetShopReviewGqlCall()
            mockGetShopInfoGqlCall()
            mockGetShopNoteGqlCall()
            mockGetShopOperationListGqlCall()
            mockGetShopStatsRawDataGqlCall()
            mockGetNearestPharmacyGqlCall()
            mockGetPharmacyShopInfoGqlCall()
            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.Setup(shopId, districtId, cityId))
            viewModel.processEvent(ShopInfoUiEvent.GetShopInfo)
            viewModel.processEvent(ShopInfoUiEvent.TapReviewImageViewAll(selectedProductId))

            // Then
            val actual = emittedEffects.last()
            assertEquals(ShopInfoUiEffect.RedirectToProductReviewPage(selectedProductId), actual)
            job.cancel()
        }
    }
    //endregion

    //region Report shop
    @Test
    fun `When report shop while user not logged in, UI should receive RedirectToLoginPage effect`() {
        runBlockingTest {
            // Given
            val emittedEffects = arrayListOf<ShopInfoUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            coEvery { userSessionInterface.isLoggedIn } returns false

            mockReportShopGqlCall()

            // When
            viewModel.processEvent(ShopInfoUiEvent.ReportShop)

            // Then
            val actual = emittedEffects.last()
            assertEquals(ShopInfoUiEffect.RedirectToLoginPage, actual)

            coVerify(exactly = 0) { getMessageIdChatUseCase.executeOnBackground() }
            job.cancel()
        }
    }

    @Test
    fun `When report shop and user already logged in, UI should receive RedirectToChatWebView effect`() {
        runBlockingTest {
            // Given
            val messageId = "111"
            val emittedEffects = arrayListOf<ShopInfoUiEffect>()
            val job = launch {
                viewModel.uiEffect.toList(emittedEffects)
            }

            coEvery { userSessionInterface.isLoggedIn } returns true

            mockReportShopGqlCall(messageIdResponse = messageId)

            // When
            viewModel.processEvent(ShopInfoUiEvent.ReportShop)

            // Then
            val actual = emittedEffects.last()
            assertEquals(ShopInfoUiEffect.RedirectToChatWebView(messageId), actual)

            coVerify { getMessageIdChatUseCase.executeOnBackground() }
            job.cancel()
        }
    }

    @Test
    fun `When report shop gql call return success, should update isLoadingShopReport to false`() {
        runBlockingTest {
            // Given
            val messageId = "111"
            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            coEvery { userSessionInterface.isLoggedIn } returns true

            mockReportShopGqlCall(messageIdResponse = messageId)

            // When
            viewModel.processEvent(ShopInfoUiEvent.ReportShop)

            // Then
            val actual = emittedValues.last()
            assertEquals(false, actual.isLoadingShopReport)

            job.cancel()
        }
    }

    @Test
    fun `When report shop gql call return error, should update isLoadingShopReport to false`() {
        runBlockingTest {
            // Given
            val error = MessageErrorException("Internal server error")
            val emittedValues = arrayListOf<ShopInfoUiState>()
            val job = launch {
                viewModel.uiState.toList(emittedValues)
            }

            coEvery { userSessionInterface.isLoggedIn } returns true
            coEvery { getMessageIdChatUseCase.executeOnBackground() } throws error

            // When
            viewModel.processEvent(ShopInfoUiEvent.ReportShop)

            // Then
            val actual = emittedValues.last()
            assertEquals(false, actual.isLoadingShopReport)

            job.cancel()
        }
    }
    //endregion

    //region helper function
    private fun mockGetShopPageHeaderGqlCall(response: ShopPageHeaderLayoutResponse = ShopPageHeaderLayoutResponse()) {
        coEvery { getShopPageHeaderLayoutUseCase.executeOnBackground() } returns response
    }
    private fun mockGetShopRatingGqlCall() {
        val requestParam = ProductRevGetShopRatingAndTopicsUseCase.Param(shopId)
        val response = ShopRating(
            detail = emptyList(),
            positivePercentageFmt = "99% pembeli puas",
            ratingScore = "4.8",
            totalRating = 2_500,
            totalRatingFmt = "2.5rb",
            totalRatingTextAndImage = 1_000,
            totalRatingTextAndImageFmt = "1rb"
        )

        coEvery { getShopRatingUseCase.execute(requestParam) } returns response
    }

    private fun mockGetShopReviewGqlCall() {
        val requestParam = ProductRevGetShopReviewReadingListUseCase.Param(
            shopID = shopId,
            limit = ShopInfoReimagineViewModel.FIVE_REVIEW,
            page = ShopInfoReimagineViewModel.FIRST_PAGE,
            filterBy = ShopInfoReimagineViewModel.SHOP_TOP_REVIEW_FILTER_BY_SELLER_SERVICE,
            sortBy = ShopInfoReimagineViewModel.SHOP_TOP_REVIEW_SORT_BY_HELPFULNESS
        )
        val response = ShopReview(totalReviews = 100, reviews = listOf())

        coEvery { getShopReviewUseCase.execute(requestParam) } returns response
    }
    private fun mockGetShopInfoGqlCall(response: ShopInfo = ShopInfo()) {
        coEvery { getShopInfoUseCase.executeOnBackground() } returns response
    }

    private fun mockGetShopNoteGqlCall(response: List<ShopNoteModel> = listOf()) {
        coEvery { getShopNoteUseCase.executeOnBackground() } returns response
    }

    private fun mockGetShopOperationListGqlCall(response: ShopOperationalHoursListResponse = ShopOperationalHoursListResponse()) {
        coEvery {
            getShopGqlGetShopOperationalHoursListUseCase.executeOnBackground()
        } returns response
    }

    private fun mockGetShopStatsRawDataGqlCall(chatAndDiscussionReplySpeed: Float = 60f) {
        val response = ShopStatsRawData(chatAndDiscussionReplySpeed = chatAndDiscussionReplySpeed)
        coEvery {
            getShopStatsRawDataUseCase.execute(
                GetShopStatsRawDataUseCase.Param(
                    shopId,
                    ShopInfoReimagineViewModel.PAGE_SOURCE
                )
            )
        } returns response
    }

    private fun mockGetNearestPharmacyGqlCall(response: GetNearestEpharmacyWarehouseLocationResponse = GetNearestEpharmacyWarehouseLocationResponse()) {
        coEvery {
            getNearestEpharmacyWarehouseLocationUseCase.executeOnBackground()
        } returns response
    }

    private fun mockGetPharmacyShopInfoGqlCall(response: GetEpharmacyShopInfoResponse = GetEpharmacyShopInfoResponse()) {
        coEvery {
            getEpharmacyShopInfoUseCase.executeOnBackground()
        } returns response
    }

    private fun mockReportShopGqlCall(messageIdResponse: String = "") {
        val response = ChatExistingChat(chatExistingChat = ChatMessageId(messageIdResponse))

        coEvery {
            getMessageIdChatUseCase.executeOnBackground()
        } returns response
    }

    //endregion
}
