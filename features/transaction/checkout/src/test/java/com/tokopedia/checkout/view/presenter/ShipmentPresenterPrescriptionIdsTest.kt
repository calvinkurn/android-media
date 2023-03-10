package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.model.cartshipmentform.EpharmacyData
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV3UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model.EthicalDrugDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.GetPrescriptionIdsResponse
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterPrescriptionIdsTest {

    @MockK
    private lateinit var validateUsePromoRevampUseCase: OldValidateUsePromoRevampUseCase

    @MockK(relaxed = true)
    private lateinit var compositeSubscription: CompositeSubscription

    @MockK
    private lateinit var checkoutUseCase: CheckoutGqlUseCase

    @MockK
    private lateinit var editAddressUseCase: EditAddressUseCase

    @MockK
    private lateinit var changeShippingAddressGqlUseCase: ChangeShippingAddressGqlUseCase

    @MockK
    private lateinit var saveShipmentStateGqlUseCase: SaveShipmentStateGqlUseCase

    @MockK
    private lateinit var getRatesUseCase: GetRatesUseCase

    @MockK
    private lateinit var getRatesApiUseCase: GetRatesApiUseCase

    @MockK
    private lateinit var getRatesWithScheduleUseCase: GetRatesWithScheduleUseCase

    @MockK
    private lateinit var clearCacheAutoApplyStackUseCase: OldClearCacheAutoApplyStackUseCase

    @MockK
    private lateinit var ratesStatesConverter: RatesResponseStateConverter

    @MockK
    private lateinit var shippingCourierConverter: ShippingCourierConverter

    @MockK(relaxed = true)
    private lateinit var userSessionInterface: UserSessionInterface

    @MockK(relaxed = true)
    private lateinit var analyticsPurchaseProtection: CheckoutAnalyticsPurchaseProtection

    @MockK
    private lateinit var checkoutAnalytics: CheckoutAnalyticsCourierSelection

    @MockK
    private lateinit var shipmentAnalyticsActionListener: ShipmentContract.AnalyticsActionListener

    @MockK
    private lateinit var releaseBookingUseCase: ReleaseBookingUseCase

    @MockK(relaxed = true)
    private lateinit var view: ShipmentContract.View

    @MockK(relaxed = true)
    private lateinit var getShipmentAddressFormV3UseCase: GetShipmentAddressFormV3UseCase

    @MockK(relaxed = true)
    private lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    @MockK
    private lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCase

    @MockK(relaxed = true)
    private lateinit var epharmacyUseCase: EPharmacyPrepareProductsGroupUseCase

    private var shipmentDataConverter = ShipmentDataConverter()

    private lateinit var presenter: ShipmentPresenter

    private var gson = Gson()

    companion object {
        const val CHECKOUT_ID = "100"
    }

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = ShipmentPresenter(
            compositeSubscription,
            checkoutUseCase,
            getShipmentAddressFormV3UseCase,
            editAddressUseCase,
            changeShippingAddressGqlUseCase,
            saveShipmentStateGqlUseCase,
            getRatesUseCase,
            getRatesApiUseCase,
            clearCacheAutoApplyStackUseCase,
            ratesStatesConverter,
            shippingCourierConverter,
            shipmentAnalyticsActionListener,
            userSessionInterface,
            analyticsPurchaseProtection,
            checkoutAnalytics,
            shipmentDataConverter,
            releaseBookingUseCase,
            prescriptionIdsUseCase,
            epharmacyUseCase,
            validateUsePromoRevampUseCase,
            gson,
            TestSchedulers,
            eligibleForAddressUseCase,
            getRatesWithScheduleUseCase
        )
        presenter.attachView(view)
    }

    @Test
    fun `WHEN fetch prescription then should hit fetch prescription use case with checkout id`() {
        // Given
        every { prescriptionIdsUseCase.execute(any()) } returns Observable.just(
            GetPrescriptionIdsResponse(
                detailData = GetPrescriptionIdsResponse.EPharmacyCheckoutData(
                    prescriptionData = GetPrescriptionIdsResponse.EPharmacyCheckoutData.EPharmacyPrescriptionDetailData(
                        prescriptions = listOf(
                            GetPrescriptionIdsResponse.EPharmacyCheckoutData.Prescription("123"),
                            GetPrescriptionIdsResponse.EPharmacyCheckoutData.Prescription("321")
                        ),
                        checkoutId = CHECKOUT_ID
                    )
                )
            )
        )
        presenter.shipmentCartItemModelList = listOf()
        presenter.setUploadPrescriptionData(
            UploadPrescriptionUiModel(
                false,
                "",
                "",
                checkoutId = CHECKOUT_ID,
                arrayListOf()
            )
        )

        // When
        presenter.fetchPrescriptionIds(
            EpharmacyData(
                showImageUpload = true,
                checkoutId = CHECKOUT_ID,
                consultationFlow = false
            )
        )

        // Then
        verify { prescriptionIdsUseCase.execute(CHECKOUT_ID) }
        verify(exactly = 1) { view.updateUploadPrescription(match { it.uploadedImageCount == 2 }) }
    }

    @Test
    fun `GIVEN empty checkout id WHEN fetch prescription ids THEN should not hit fetch prescription use case`() {
        // Given
        val checkoutId = ""

        // When
        presenter.fetchPrescriptionIds(
            EpharmacyData(
                showImageUpload = true,
                checkoutId = checkoutId,
                consultationFlow = false
            )
        )

        // Then
        verify(inverse = true) { prescriptionIdsUseCase.execute(any()) }
    }

    @Test
    fun `GIVEN not need to upload prescription WHEN fetch prescription THEN should not hit fetch prescription use case`() {
        // Given
        every { prescriptionIdsUseCase.execute(any()) } returns Observable.just(
            mockk<GetPrescriptionIdsResponse>(
                relaxed = true
            )
        )
        presenter.setUploadPrescriptionData(null)

        // When
        presenter.fetchPrescriptionIds(
            EpharmacyData(
                showImageUpload = false,
                checkoutId = CHECKOUT_ID,
                consultationFlow = false
            )
        )

        // Then
        verify(inverse = true) { prescriptionIdsUseCase.execute(any()) }
    }

    @Test
    fun `GIVEN consultation flow prescription WHEN fetch prescription THEN should not hit fetch prescription use case`() {
        // Given
        every { prescriptionIdsUseCase.execute(any()) } returns Observable.just(
            mockk<GetPrescriptionIdsResponse>(
                relaxed = true
            )
        )
        presenter.setUploadPrescriptionData(null)

        // When
        presenter.fetchPrescriptionIds(
            EpharmacyData(
                showImageUpload = false,
                checkoutId = CHECKOUT_ID,
                consultationFlow = false
            )
        )

        // Then
        verify(inverse = true) { prescriptionIdsUseCase.execute(any()) }
    }

    @Test
    fun `GIVEN error item WHEN fetch prescription THEN should not update prescription data`() {
        // Given
        every { prescriptionIdsUseCase.execute(any()) } returns Observable.error(Throwable())
        presenter.setUploadPrescriptionData(
            UploadPrescriptionUiModel(
                false,
                "",
                "",
                checkoutId = CHECKOUT_ID,
                arrayListOf()
            )
        )

        // When
        presenter.fetchPrescriptionIds(
            EpharmacyData(
                showImageUpload = true,
                checkoutId = CHECKOUT_ID,
                consultationFlow = false
            )
        )

        // Then
        verify { prescriptionIdsUseCase.execute(CHECKOUT_ID) }
        verify(exactly = 0) { view.updateUploadPrescription(any()) }
    }

    @Test
    fun `CHECK upload prescription data initialization`() {
        // When
        presenter.setUploadPrescriptionData(
            UploadPrescriptionUiModel(
                false,
                "",
                "",
                checkoutId = CHECKOUT_ID,
                arrayListOf()
            )
        )

        // Then
        assert(presenter.uploadPrescriptionUiModel != null)
    }

    @Test
    fun `WHEN set prescription ids THEN should set upload prescription image count`() {
        // Given
        presenter.shipmentCartItemModelList = arrayListOf(
            ShipmentCartItemModel()
        )
        val prescriptions = arrayListOf("123", "456")
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())

        // When
        presenter.setPrescriptionIds(prescriptions)

        // Then
        assertEquals(prescriptions.size, presenter.uploadPrescriptionUiModel.uploadedImageCount)
    }

    @Test
    fun `Given null shipment cart data WHEN set prescription ids THEN should not set upload prescription image count`() {
        // Given
        val prescriptions = arrayListOf("123", "456")
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())

        // When
        presenter.setPrescriptionIds(prescriptions)

        // Then
        assertEquals(0, presenter.uploadPrescriptionUiModel.uploadedImageCount)
    }

    @Test
    fun `Given null upload prescription model WHEN set prescription ids THEN should not set upload prescription image count`() {
        // Given
        presenter.shipmentCartItemModelList = arrayListOf(
            ShipmentCartItemModel()
        )
        val prescriptions = arrayListOf("123", "456")

        // When
        presenter.setPrescriptionIds(prescriptions)

        // Then
        assertEquals(null, presenter.uploadPrescriptionUiModel)
    }

    @Test
    fun `WHEN set prescription ids THEN should set prescription ids to each cart with ethical products and not error`() {
        // Given
        presenter.shipmentCartItemModelList = arrayListOf(
            ShipmentCartItemModel(
                isError = true,
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                isError = true,
                hasEthicalProducts = false
            ),
            ShipmentCartItemModel(
                isError = false,
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                isError = false,
                hasEthicalProducts = false
            )
        )
        val prescriptions = arrayListOf("123", "456")
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())

        // When
        presenter.setPrescriptionIds(prescriptions)

        // Then
        assertEquals(emptyList<String>(), presenter.shipmentCartItemModelList[0].prescriptionIds)
        assertEquals(emptyList<String>(), presenter.shipmentCartItemModelList[1].prescriptionIds)
        assertEquals(prescriptions, presenter.shipmentCartItemModelList[2].prescriptionIds)
        assertEquals(emptyList<String>(), presenter.shipmentCartItemModelList[3].prescriptionIds)
    }

    @Test
    fun `GIVEN failed prepare epharmacy data WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (secondArg() as (Throwable) -> Unit).invoke(Throwable())
        }

        // When
        presenter.fetchEpharmacyData()

        // Then
        assertEquals(null, presenter.uploadPrescriptionUiModel)
    }

    @Test
    fun `GIVEN null epharmacy data WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(null)
            )
        }

        // When
        presenter.fetchEpharmacyData()

        // Then
        assertEquals(UploadPrescriptionUiModel(), presenter.uploadPrescriptionUiModel)
    }

    @Test
    fun `GIVEN null upload prescription model WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        presenter.setUploadPrescriptionData(null)
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        GroupData(null, null, listOf(), null, null)
                    )
                )
            )
        }

        // When
        presenter.fetchEpharmacyData()

        // Then
        assertEquals(null, presenter.uploadPrescriptionUiModel)
    }

    @Test
    fun `GIVEN null view WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        GroupData(null, null, listOf(), null, null)
                    )
                )
            )
        }
        every { epharmacyUseCase.cancelJobs() } just Runs
        presenter.detachView()

        // When
        presenter.fetchEpharmacyData()

        // Then
        assertEquals(UploadPrescriptionUiModel(), presenter.uploadPrescriptionUiModel)
    }

    @Test
    fun `GIVEN null epharmacy group data WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        null
                    )
                )
            )
        }
        presenter.shipmentCartItemModelList = arrayListOf()

        // When
        presenter.fetchEpharmacyData()

        // Then
        assertEquals(UploadPrescriptionUiModel(), presenter.uploadPrescriptionUiModel)
    }

    @Test
    fun `GIVEN null epharmacy groups WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        GroupData(
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    )
                )
            )
        }
        presenter.shipmentCartItemModelList = arrayListOf()

        // When
        presenter.fetchEpharmacyData()

        // Then
        assertEquals(UploadPrescriptionUiModel(), presenter.uploadPrescriptionUiModel)
    }

    @Test
    fun `GIVEN null shipment cart data WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        GroupData(
                            null,
                            null,
                            listOf(),
                            null,
                            null
                        )
                    )
                )
            )
        }

        // When
        presenter.fetchEpharmacyData()

        // Then
        assertEquals(UploadPrescriptionUiModel(), presenter.uploadPrescriptionUiModel)
    }

    @Test
    fun `GIVEN rejected consultation WHEN fetch epharmacy data THEN should set error`() {
        // Given
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns 1
        val errorWording = "error wording"
        every { view.activityContext.getString(any(), any()) } returns errorWording
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    detailData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        groupsData = GroupData(
                            epharmacyGroups = listOf(
                                GroupData.EpharmacyGroup(
                                    epharmacyGroupId = "123",
                                    shopInfo = listOf(
                                        ProductsInfo(
                                            shopId = "6554231",
                                            products = arrayListOf(
                                                ProductsInfo.Product(
                                                    productId = 2150389388,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        ),
                                        ProductsInfo(
                                            shopId = "6554232",
                                            products = arrayListOf(
                                                ProductsInfo.Product(
                                                    productId = 2150389387,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        )
                                    ),
                                    numberPrescriptionImages = 0,
                                    prescriptionImages = listOf(),
                                    consultationData = GroupData.EpharmacyGroup.ConsultationData(
                                        consultationString = "",
                                        tokoConsultationId = "123",
                                        partnerConsultationId = "321",
                                        consultationStatus = 4,
                                        doctorDetails = null,
                                        endTime = null,
                                        medicalRecommendation = null,
                                        prescription = listOf(),
                                        startTime = ""
                                    ),
                                    consultationSource = null,
                                    prescriptionSource = null,
                                    prescriptionCTA = null
                                )
                            ),
                            attachmentPageTickerText = null,
                            attachmentPageTickerLogoUrl = null,
                            toaster = null,
                            papPrimaryCTA = null
                        )
                    )
                )
            )
        }
        presenter.shipmentCartItemModelList = arrayListOf(
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389388,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554232,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389387,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        productId = 2150389386
                    )
                ),
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            )
        )
        val rejectedWording = "rejectedWording"
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel(rejectedWording = rejectedWording))

        // When
        presenter.fetchEpharmacyData()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = true,
                rejectedWording = rejectedWording,
                epharmacyGroupIds = arrayListOf("123"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232"),
                cartIds = listOf("0", "0")
            ),
            presenter.uploadPrescriptionUiModel
        )

        assertEquals(true, presenter.shipmentCartItemModelList[0].isError)
        assertEquals(errorWording, presenter.shipmentCartItemModelList[0].errorTitle)
        assertEquals(true, presenter.shipmentCartItemModelList[0].isCustomEpharmacyError)

        assertEquals(false, presenter.shipmentCartItemModelList[1].isError)
        assertEquals(true, presenter.shipmentCartItemModelList[1].cartItemModels[0].isError)
        assertEquals(
            rejectedWording,
            presenter.shipmentCartItemModelList[1].cartItemModels[0].errorMessage
        )
        assertEquals(false, presenter.shipmentCartItemModelList[1].cartItemModels[1].isError)
    }

    @Test
    fun `GIVEN cannot get position WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns -1
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    detailData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        groupsData = GroupData(
                            epharmacyGroups = listOf(
                                GroupData.EpharmacyGroup(
                                    epharmacyGroupId = "123",
                                    shopInfo = listOf(
                                        ProductsInfo(
                                            shopId = "6554231",
                                            products = arrayListOf(
                                                ProductsInfo.Product(
                                                    productId = 2150389388,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        )
                                    ),
                                    numberPrescriptionImages = 0,
                                    prescriptionImages = listOf(),
                                    consultationData = GroupData.EpharmacyGroup.ConsultationData(
                                        consultationString = "",
                                        tokoConsultationId = "123",
                                        partnerConsultationId = "321",
                                        consultationStatus = 4,
                                        doctorDetails = null,
                                        endTime = null,
                                        medicalRecommendation = null,
                                        prescription = listOf(),
                                        startTime = ""
                                    ),
                                    consultationSource = null,
                                    prescriptionSource = null,
                                    prescriptionCTA = null
                                )
                            ),
                            attachmentPageTickerText = null,
                            attachmentPageTickerLogoUrl = null,
                            toaster = null,
                            papPrimaryCTA = null
                        )
                    )
                )
            )
        }
        presenter.shipmentCartItemModelList = arrayListOf(
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389388,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    )
                ),
                hasEthicalProducts = true
            )
        )
        val rejectedWording = "rejectedWording"
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel(rejectedWording = rejectedWording))

        // When
        presenter.fetchEpharmacyData()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                rejectedWording = rejectedWording,
                shopIds = listOf("6554231"),
                enablerNames = listOf(""),
                cartIds = listOf("0")
            ),
            presenter.uploadPrescriptionUiModel
        )
        assertEquals(false, presenter.shipmentCartItemModelList[0].isError)
        assertEquals(null, presenter.shipmentCartItemModelList[0].errorTitle)
    }

    @Test
    fun `GIVEN approved consultation WHEN fetch epharmacy data THEN should set consultation data`() {
        // Given
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns 1
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    detailData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        groupsData = GroupData(
                            epharmacyGroups = listOf(
                                GroupData.EpharmacyGroup(
                                    epharmacyGroupId = "123",
                                    shopInfo = listOf(
                                        ProductsInfo(
                                            shopId = "6554231",
                                            products = arrayListOf(
                                                ProductsInfo.Product(
                                                    productId = 2150389388,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                ),
                                                ProductsInfo.Product(
                                                    productId = 2150389389,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        )
                                    ),
                                    numberPrescriptionImages = 0,
                                    prescriptionImages = listOf(),
                                    consultationData = GroupData.EpharmacyGroup.ConsultationData(
                                        consultationString = "qwerty",
                                        tokoConsultationId = "123",
                                        partnerConsultationId = "321",
                                        consultationStatus = 2,
                                        doctorDetails = null,
                                        endTime = null,
                                        medicalRecommendation = null,
                                        prescription = listOf(),
                                        startTime = ""
                                    ),
                                    consultationSource = null,
                                    prescriptionSource = null,
                                    prescriptionCTA = null
                                )
                            ),
                            attachmentPageTickerText = null,
                            attachmentPageTickerLogoUrl = null,
                            toaster = null,
                            papPrimaryCTA = null
                        )
                    )
                )
            )
        }
        presenter.shipmentCartItemModelList = arrayListOf(
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389388,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        productId = 2150389389,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389387,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        productId = 2150389386,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554232,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389385
                    ),
                    CartItemModel(
                        productId = 2150389384
                    )
                )
            )
        )
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())

        // When
        presenter.fetchEpharmacyData()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = false,
                uploadedImageCount = 1,
                epharmacyGroupIds = arrayListOf("123"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554231"),
                cartIds = listOf("0", "0", "0", "0")
            ),
            presenter.uploadPrescriptionUiModel
        )
        assertEquals(false, presenter.shipmentCartItemModelList[0].isError)
        assertEquals("qwerty", presenter.shipmentCartItemModelList[0].consultationDataString)
        assertEquals("123", presenter.shipmentCartItemModelList[0].tokoConsultationId)
        assertEquals("321", presenter.shipmentCartItemModelList[0].partnerConsultationId)

        assertEquals(false, presenter.shipmentCartItemModelList[1].isError)
        assertEquals("", presenter.shipmentCartItemModelList[1].consultationDataString)
        assertEquals("", presenter.shipmentCartItemModelList[1].tokoConsultationId)
        assertEquals("", presenter.shipmentCartItemModelList[1].partnerConsultationId)

        assertEquals(false, presenter.shipmentCartItemModelList[2].isError)
        assertEquals("", presenter.shipmentCartItemModelList[2].consultationDataString)
        assertEquals("", presenter.shipmentCartItemModelList[2].tokoConsultationId)
        assertEquals("", presenter.shipmentCartItemModelList[2].partnerConsultationId)
    }

    @Test
    fun `GIVEN mixed epharmacy data WHEN fetch epharmacy data THEN should set epharmacy data correctly`() {
        // Given
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns 1
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    detailData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        groupsData = GroupData(
                            epharmacyGroups = listOf(
                                GroupData.EpharmacyGroup(
                                    epharmacyGroupId = "123",
                                    shopInfo = listOf(
                                        ProductsInfo(
                                            shopId = "6554231",
                                            products = arrayListOf(
                                                ProductsInfo.Product(
                                                    productId = 2150389389,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                ),
                                                ProductsInfo.Product(
                                                    productId = 2150389388,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        ),
                                        ProductsInfo(
                                            shopId = "6554234",
                                            products = arrayListOf(
                                                ProductsInfo.Product(
                                                    productId = 2150389382,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        )
                                    ),
                                    numberPrescriptionImages = 0,
                                    prescriptionImages = listOf(),
                                    consultationData = GroupData.EpharmacyGroup.ConsultationData(
                                        consultationString = "qwerty",
                                        tokoConsultationId = "123",
                                        partnerConsultationId = "321",
                                        consultationStatus = 2,
                                        doctorDetails = null,
                                        endTime = null,
                                        medicalRecommendation = null,
                                        prescription = listOf(),
                                        startTime = ""
                                    ),
                                    consultationSource = null,
                                    prescriptionSource = null,
                                    prescriptionCTA = null
                                ),
                                GroupData.EpharmacyGroup(
                                    epharmacyGroupId = "124",
                                    shopInfo = listOf(
                                        ProductsInfo(
                                            shopId = "6554231",
                                            products = arrayListOf(
                                                ProductsInfo.Product(
                                                    productId = 2150389387,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                ),
                                                ProductsInfo.Product(
                                                    productId = 2150389386,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        )
                                    ),
                                    numberPrescriptionImages = 0,
                                    prescriptionImages = listOf(
                                        GroupData.EpharmacyGroup.PrescriptionImage(
                                            expiredAt = null,
                                            prescriptionId = "1",
                                            rejectReason = null,
                                            status = null
                                        ),
                                        GroupData.EpharmacyGroup.PrescriptionImage(
                                            expiredAt = null,
                                            prescriptionId = "2",
                                            rejectReason = null,
                                            status = null
                                        )
                                    ),
                                    consultationData = GroupData.EpharmacyGroup.ConsultationData(
                                        consultationString = "",
                                        tokoConsultationId = "",
                                        partnerConsultationId = "",
                                        consultationStatus = 0,
                                        doctorDetails = null,
                                        endTime = null,
                                        medicalRecommendation = null,
                                        prescription = listOf(),
                                        startTime = ""
                                    ),
                                    consultationSource = null,
                                    prescriptionSource = null,
                                    prescriptionCTA = null
                                ),
                                GroupData.EpharmacyGroup(
                                    epharmacyGroupId = "125",
                                    shopInfo = listOf(
                                        ProductsInfo(
                                            shopId = "6554232",
                                            products = arrayListOf(
                                                ProductsInfo.Product(
                                                    productId = 2150389384,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                ),
                                                ProductsInfo.Product(
                                                    productId = 2150389385,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        )
                                    ),
                                    numberPrescriptionImages = 0,
                                    prescriptionImages = listOf(),
                                    consultationData = GroupData.EpharmacyGroup.ConsultationData(
                                        consultationString = "qwerty",
                                        tokoConsultationId = "125",
                                        partnerConsultationId = "521",
                                        consultationStatus = 4,
                                        doctorDetails = null,
                                        endTime = null,
                                        medicalRecommendation = null,
                                        prescription = listOf(),
                                        startTime = ""
                                    ),
                                    consultationSource = null,
                                    prescriptionSource = null,
                                    prescriptionCTA = null
                                )
                            ),
                            attachmentPageTickerText = null,
                            attachmentPageTickerLogoUrl = null,
                            toaster = null,
                            papPrimaryCTA = null
                        )
                    )
                )
            )
        }
        presenter.shipmentCartItemModelList = arrayListOf(
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389388,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        productId = 2150389389,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389387,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        productId = 2150389386,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554232,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389385,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        productId = 2150389384,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554233,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389383
                    )
                ),
                hasEthicalProducts = false
            ),
            ShipmentCartItemModel(
                shopId = 6554234,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389382
                    )
                ),
                hasEthicalProducts = false
            ),
            ShipmentCartItemModel(
                shopId = 6554235,
                isError = true,
                cartItemModels = listOf(
                    CartItemModel(
                        isError = true,
                        productId = 2150389381,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554236,
                isError = true,
                cartItemModels = listOf(
                    CartItemModel(
                        isError = true,
                        productId = 2150389380
                    )
                ),
                hasEthicalProducts = false
            )
        )
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())

        // When
        presenter.fetchEpharmacyData()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = true,
                uploadedImageCount = 3,
                epharmacyGroupIds = arrayListOf("123", "124", "125"),
                enablerNames = listOf(""),
                shopIds = listOf(
                    "6554231",
                    "6554231",
                    "6554232",
                    "6554235"
                ),
                cartIds = listOf("0", "0", "0", "0", "0", "0", "0")
            ),
            presenter.uploadPrescriptionUiModel
        )
        assertEquals(false, presenter.shipmentCartItemModelList[0].isError)
        assertEquals("qwerty", presenter.shipmentCartItemModelList[0].consultationDataString)
        assertEquals("123", presenter.shipmentCartItemModelList[0].tokoConsultationId)
        assertEquals("321", presenter.shipmentCartItemModelList[0].partnerConsultationId)
        assertEquals(emptyList<String>(), presenter.shipmentCartItemModelList[0].prescriptionIds)

        assertEquals(false, presenter.shipmentCartItemModelList[1].isError)
        assertEquals("", presenter.shipmentCartItemModelList[1].consultationDataString)
        assertEquals("", presenter.shipmentCartItemModelList[1].tokoConsultationId)
        assertEquals("", presenter.shipmentCartItemModelList[1].partnerConsultationId)
        assertEquals(listOf("1", "2"), presenter.shipmentCartItemModelList[1].prescriptionIds)

        assertEquals(true, presenter.shipmentCartItemModelList[2].isError)
        assertEquals("", presenter.shipmentCartItemModelList[2].consultationDataString)
        assertEquals("", presenter.shipmentCartItemModelList[2].tokoConsultationId)
        assertEquals("", presenter.shipmentCartItemModelList[2].partnerConsultationId)
        assertEquals(emptyList<String>(), presenter.shipmentCartItemModelList[2].prescriptionIds)

        assertEquals(false, presenter.shipmentCartItemModelList[3].isError)
        assertEquals("", presenter.shipmentCartItemModelList[3].consultationDataString)
        assertEquals("", presenter.shipmentCartItemModelList[3].tokoConsultationId)
        assertEquals("", presenter.shipmentCartItemModelList[3].partnerConsultationId)
        assertEquals(emptyList<String>(), presenter.shipmentCartItemModelList[3].prescriptionIds)
    }

    @Test
    fun `GIVEN null shipment cart data WHEN set mini consultation result THEN should do nothing`() {
        // Given
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns 1
        val results = arrayListOf(
            EPharmacyMiniConsultationResult(
                "123",
                arrayListOf(
                    ProductsInfo(
                        "",
                        arrayListOf(
                            ProductsInfo.Product(
                                false,
                                0.0,
                                "",
                                2150389388,
                                "",
                                "",
                                "1"
                            )
                        ),
                        "6554231",
                        "",
                        "",
                        "",
                        ""
                    )
                ),
                2,
                "qwerty",
                arrayListOf(
                    GroupData.EpharmacyGroup.ConsultationData.Prescription(
                        "",
                        "",
                        ""
                    ),
                    GroupData.EpharmacyGroup.ConsultationData.Prescription(
                        "",
                        "",
                        ""
                    )
                ),
                "321",
                "123",
                null
            )
        )
        presenter.shipmentCartItemModelList = null
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())

        // When
        presenter.setMiniConsultationResult(results)

        // Then
        assertEquals(
            UploadPrescriptionUiModel(),
            presenter.uploadPrescriptionUiModel
        )
    }

    @Test
    fun `GIVEN null view WHEN set mini consultation result THEN should do nothing`() {
        // Given
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns 1
        val results = arrayListOf(
            EPharmacyMiniConsultationResult(
                "123",
                arrayListOf(
                    ProductsInfo(
                        "",
                        arrayListOf(
                            ProductsInfo.Product(
                                false,
                                0.0,
                                "",
                                2150389388,
                                "",
                                "",
                                "1"
                            )
                        ),
                        "6554231",
                        "",
                        "",
                        "",
                        ""
                    )
                ),
                2,
                "qwerty",
                arrayListOf(
                    GroupData.EpharmacyGroup.ConsultationData.Prescription(
                        "",
                        "",
                        ""
                    ),
                    GroupData.EpharmacyGroup.ConsultationData.Prescription(
                        "",
                        "",
                        ""
                    )
                ),
                "321",
                "123",
                null
            )
        )
        presenter.shipmentCartItemModelList = listOf()
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())
        every { epharmacyUseCase.cancelJobs() } just Runs
        presenter.detachView()

        // When
        presenter.setMiniConsultationResult(results)

        // Then
        assertEquals(
            UploadPrescriptionUiModel(),
            presenter.uploadPrescriptionUiModel
        )
    }

    @Test
    fun `GIVEN cannot get position WHEN set mini consultation result THEN should do nothing`() {
        // Given
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns -1
        val results = arrayListOf(
            EPharmacyMiniConsultationResult(
                "123",
                arrayListOf(
                    ProductsInfo(
                        "",
                        arrayListOf(
                            ProductsInfo.Product(
                                false,
                                0.0,
                                "",
                                2150389388,
                                "",
                                "",
                                "1"
                            )
                        ),
                        "6554231",
                        "",
                        "",
                        "",
                        ""
                    )
                ),
                2,
                "qwerty",
                arrayListOf(
                    GroupData.EpharmacyGroup.ConsultationData.Prescription(
                        "",
                        "",
                        ""
                    ),
                    GroupData.EpharmacyGroup.ConsultationData.Prescription(
                        "",
                        "",
                        ""
                    )
                ),
                "321",
                "123",
                null
            )
        )
        presenter.shipmentCartItemModelList = arrayListOf(
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389388,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        productId = 2150389389
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389387,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        productId = 2150389386
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554232,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389385
                    ),
                    CartItemModel(
                        productId = 2150389384
                    )
                ),
                hasEthicalProducts = false
            )
        )
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())

        // When
        presenter.setMiniConsultationResult(results)

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                shopIds = listOf("6554231", "6554231"),
                enablerNames = listOf(""),
                cartIds = listOf("0", "0")
            ),
            presenter.uploadPrescriptionUiModel
        )
    }

    @Test
    fun `GIVEN approved consultation WHEN set mini consultation result THEN should set correct data`() {
        // Given
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns 1
        val results = arrayListOf(
            EPharmacyMiniConsultationResult(
                "123",
                arrayListOf(
                    ProductsInfo(
                        "",
                        arrayListOf(
                            ProductsInfo.Product(
                                false,
                                0.0,
                                "",
                                2150389388,
                                "",
                                "",
                                "1"
                            )
                        ),
                        "6554231",
                        "",
                        "",
                        "",
                        ""
                    )
                ),
                2,
                "qwerty",
                arrayListOf(),
                "321",
                "123",
                null
            )
        )
        presenter.shipmentCartItemModelList = arrayListOf(
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389388,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        productId = 2150389389
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389387,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        productId = 2150389386
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554232,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389385
                    ),
                    CartItemModel(
                        productId = 2150389384
                    )
                ),
                hasEthicalProducts = false
            ),
            ShipmentCartItemModel(
                shopId = 6554233,
                isError = true,
                cartItemModels = listOf(
                    CartItemModel(
                        isError = true,
                        productId = 2150389385
                    ),
                    CartItemModel(
                        isError = true,
                        productId = 2150389384
                    )
                ),
                hasEthicalProducts = false
            ),
            ShipmentCartItemModel(
                shopId = 6554234,
                isError = true,
                cartItemModels = listOf(
                    CartItemModel(
                        isError = true,
                        productId = 2150389385,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        isError = true,
                        productId = 2150389384
                    )
                ),
                hasEthicalProducts = true
            )
        )
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())

        // When
        presenter.setMiniConsultationResult(results)

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = false,
                uploadedImageCount = 1,
                epharmacyGroupIds = arrayListOf("123"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554231", "6554234"),
                cartIds = listOf("0", "0", "0")
            ),
            presenter.uploadPrescriptionUiModel
        )
        assertEquals(false, presenter.shipmentCartItemModelList[0].isError)
        assertEquals("qwerty", presenter.shipmentCartItemModelList[0].consultationDataString)
        assertEquals("123", presenter.shipmentCartItemModelList[0].tokoConsultationId)
        assertEquals("321", presenter.shipmentCartItemModelList[0].partnerConsultationId)

        assertEquals(false, presenter.shipmentCartItemModelList[1].isError)
        assertEquals("", presenter.shipmentCartItemModelList[1].consultationDataString)
        assertEquals("", presenter.shipmentCartItemModelList[1].tokoConsultationId)
        assertEquals("", presenter.shipmentCartItemModelList[1].partnerConsultationId)

        assertEquals(false, presenter.shipmentCartItemModelList[2].isError)
        assertEquals("", presenter.shipmentCartItemModelList[2].consultationDataString)
        assertEquals("", presenter.shipmentCartItemModelList[2].tokoConsultationId)
        assertEquals("", presenter.shipmentCartItemModelList[2].partnerConsultationId)
    }

    @Test
    fun `GIVEN rejected consultation in the mixed order WHEN set mini consultation result THEN should set error`() {
        // Given
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns 1
        val errorWording = "error wording"
        every { view.activityContext.getString(any(), any()) } returns errorWording
        val result = arrayListOf(
            EPharmacyMiniConsultationResult(
                epharmacyGroupId = "123",
                shopInfo = listOf(
                    ProductsInfo(
                        shopId = "6554231",
                        products = arrayListOf(
                            ProductsInfo.Product(
                                productId = 2150389388,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            )
                        ),
                        partnerLogoUrl = "",
                        shopLocation = "",
                        shopLogoUrl = "",
                        shopName = "",
                        shopType = ""
                    ),
                    ProductsInfo(
                        shopId = "6554232",
                        products = arrayListOf(
                            ProductsInfo.Product(
                                productId = 2150389387,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            )
                        ),
                        partnerLogoUrl = "",
                        shopLocation = "",
                        shopLogoUrl = "",
                        shopName = "",
                        shopType = ""
                    )
                ),
                prescriptionImages = listOf(),
                consultationStatus = 4,
                consultationString = "",
                tokoConsultationId = "123",
                partnerConsultationId = "321",
                prescription = listOf()
            )
        )
        presenter.shipmentCartItemModelList = arrayListOf(
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389388,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554232,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389387,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        productId = 2150389386
                    )
                ),
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554233,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389385
                    )
                ),
                hasNonEthicalProducts = true
            )
        )
        val rejectedWording = "rejectedWording"
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel(rejectedWording = rejectedWording))

        // When
        presenter.setMiniConsultationResult(result)

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                rejectedWording = rejectedWording,
                hasInvalidPrescription = true,
                epharmacyGroupIds = arrayListOf("123"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232"),
                cartIds = listOf("0", "0")
            ),
            presenter.uploadPrescriptionUiModel
        )

        assertEquals(true, presenter.shipmentCartItemModelList[0].isError)
        assertEquals(errorWording, presenter.shipmentCartItemModelList[0].errorTitle)
        assertEquals(true, presenter.shipmentCartItemModelList[0].isCustomEpharmacyError)

        assertEquals(false, presenter.shipmentCartItemModelList[1].isError)
        assertEquals(true, presenter.shipmentCartItemModelList[1].cartItemModels[0].isError)
        assertEquals(
            rejectedWording,
            presenter.shipmentCartItemModelList[1].cartItemModels[0].errorMessage
        )
        assertEquals(false, presenter.shipmentCartItemModelList[1].cartItemModels[1].isError)

        assertEquals(false, presenter.shipmentCartItemModelList[2].isError)
    }

    @Test
    fun `GIVEN mixed epharmacy data WHEN set mini consultation result THEN should set epharmacy data correctly`() {
        // Given
        every { view.getShipmentCartItemModelAdapterPositionByUniqueId(any()) } returns 1
        val result = arrayListOf(
            EPharmacyMiniConsultationResult(
                epharmacyGroupId = "123",
                shopInfo = listOf(
                    ProductsInfo(
                        shopId = "6554231",
                        products = arrayListOf(
                            ProductsInfo.Product(
                                productId = 2150389389,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            ),
                            ProductsInfo.Product(
                                productId = 2150389388,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            )
                        ),
                        partnerLogoUrl = "",
                        shopLocation = "",
                        shopLogoUrl = "",
                        shopName = "",
                        shopType = ""
                    ),
                    ProductsInfo(
                        shopId = "6554234",
                        products = arrayListOf(
                            ProductsInfo.Product(
                                productId = 2150389382,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            )
                        ),
                        partnerLogoUrl = "",
                        shopLocation = "",
                        shopLogoUrl = "",
                        shopName = "",
                        shopType = ""
                    )
                ),
                prescriptionImages = listOf(),
                consultationString = "qwerty",
                tokoConsultationId = "123",
                partnerConsultationId = "321",
                consultationStatus = 2,
                prescription = listOf()
            ),
            EPharmacyMiniConsultationResult(
                epharmacyGroupId = "124",
                shopInfo = listOf(
                    ProductsInfo(
                        shopId = "6554231",
                        products = arrayListOf(
                            ProductsInfo.Product(
                                productId = 2150389387,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            ),
                            ProductsInfo.Product(
                                productId = 2150389386,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            )
                        ),
                        partnerLogoUrl = "",
                        shopLocation = "",
                        shopLogoUrl = "",
                        shopName = "",
                        shopType = ""
                    )
                ),
                prescriptionImages = listOf(
                    GroupData.EpharmacyGroup.PrescriptionImage(
                        expiredAt = null,
                        prescriptionId = "1",
                        rejectReason = null,
                        status = null
                    ),
                    GroupData.EpharmacyGroup.PrescriptionImage(
                        expiredAt = null,
                        prescriptionId = "2",
                        rejectReason = null,
                        status = null
                    )
                ),
                consultationString = "",
                tokoConsultationId = "",
                partnerConsultationId = "",
                consultationStatus = 0,
                prescription = listOf()
            ),
            EPharmacyMiniConsultationResult(
                epharmacyGroupId = "125",
                shopInfo = listOf(
                    ProductsInfo(
                        shopId = "6554232",
                        products = arrayListOf(
                            ProductsInfo.Product(
                                productId = 2150389384,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            ),
                            ProductsInfo.Product(
                                productId = 2150389385,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            )
                        ),
                        partnerLogoUrl = "",
                        shopLocation = "",
                        shopLogoUrl = "",
                        shopName = "",
                        shopType = ""
                    )
                ),
                prescriptionImages = listOf(),
                consultationString = "qwerty",
                tokoConsultationId = "125",
                partnerConsultationId = "521",
                consultationStatus = 4,
                prescription = listOf()
            )
        )
        presenter.shipmentCartItemModelList = arrayListOf(
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389388,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        productId = 2150389389,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389387,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        productId = 2150389386,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554232,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389385,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    ),
                    CartItemModel(
                        productId = 2150389384,
                        ethicalDrugDataModel = EthicalDrugDataModel(true)
                    )
                ),
                hasEthicalProducts = true
            ),
            ShipmentCartItemModel(
                shopId = 6554233,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389383
                    )
                ),
                hasEthicalProducts = false
            ),
            ShipmentCartItemModel(
                shopId = 6554234,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389382
                    )
                ),
                hasEthicalProducts = false
            )
        )
        presenter.setUploadPrescriptionData(UploadPrescriptionUiModel())

        // When
        presenter.setMiniConsultationResult(result)

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = true,
                uploadedImageCount = 3,
                epharmacyGroupIds = arrayListOf("123", "124", "125"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554231", "6554232"),
                cartIds = listOf("0", "0", "0", "0", "0", "0")
            ),
            presenter.uploadPrescriptionUiModel
        )
        assertEquals(false, presenter.shipmentCartItemModelList[0].isError)
        assertEquals("qwerty", presenter.shipmentCartItemModelList[0].consultationDataString)
        assertEquals("123", presenter.shipmentCartItemModelList[0].tokoConsultationId)
        assertEquals("321", presenter.shipmentCartItemModelList[0].partnerConsultationId)
        assertEquals(emptyList<String>(), presenter.shipmentCartItemModelList[0].prescriptionIds)

        assertEquals(false, presenter.shipmentCartItemModelList[1].isError)
        assertEquals("", presenter.shipmentCartItemModelList[1].consultationDataString)
        assertEquals("", presenter.shipmentCartItemModelList[1].tokoConsultationId)
        assertEquals("", presenter.shipmentCartItemModelList[1].partnerConsultationId)
        assertEquals(listOf("1", "2"), presenter.shipmentCartItemModelList[1].prescriptionIds)

        assertEquals(true, presenter.shipmentCartItemModelList[2].isError)
        assertEquals("", presenter.shipmentCartItemModelList[2].consultationDataString)
        assertEquals("", presenter.shipmentCartItemModelList[2].tokoConsultationId)
        assertEquals("", presenter.shipmentCartItemModelList[2].partnerConsultationId)
        assertEquals(emptyList<String>(), presenter.shipmentCartItemModelList[2].prescriptionIds)

        assertEquals(false, presenter.shipmentCartItemModelList[3].isError)
        assertEquals("", presenter.shipmentCartItemModelList[3].consultationDataString)
        assertEquals("", presenter.shipmentCartItemModelList[3].tokoConsultationId)
        assertEquals("", presenter.shipmentCartItemModelList[3].partnerConsultationId)
        assertEquals(emptyList<String>(), presenter.shipmentCartItemModelList[3].prescriptionIds)
    }

    @Test
    fun `GIVEN has uploaded prescription WHEN validate on back pressed THEN should show reminder`() {
        // Given
        val uploadPrescriptionUiModel = UploadPrescriptionUiModel(
            showImageUpload = true,
            uploadedImageCount = 1
        )
        presenter.setUploadPrescriptionData(
            uploadPrescriptionUiModel
        )
        presenter.shipmentCartItemModelList = listOf()

        // When
        val result = presenter.validatePrescriptionOnBackPressed()

        // Then
        assertEquals(false, result)
        verify {
            view.showPrescriptionReminderDialog(uploadPrescriptionUiModel)
        }
    }

    @Test
    fun `GIVEN has invalid prescription WHEN validate on back pressed THEN should show reminder`() {
        // Given
        val uploadPrescriptionUiModel = UploadPrescriptionUiModel(
            showImageUpload = true,
            hasInvalidPrescription = true
        )
        presenter.setUploadPrescriptionData(
            uploadPrescriptionUiModel
        )
        presenter.shipmentCartItemModelList = listOf()

        // When
        val result = presenter.validatePrescriptionOnBackPressed()

        // Then
        assertEquals(false, result)
        verify {
            view.showPrescriptionReminderDialog(uploadPrescriptionUiModel)
        }
    }

    @Test
    fun `GIVEN has both valid & invalid prescriptions WHEN validate on back pressed THEN should show reminder`() {
        // Given
        val uploadPrescriptionUiModel = UploadPrescriptionUiModel(
            showImageUpload = true,
            hasInvalidPrescription = true,
            uploadedImageCount = 1
        )
        presenter.setUploadPrescriptionData(
            uploadPrescriptionUiModel
        )
        presenter.shipmentCartItemModelList = listOf()

        // When
        val result = presenter.validatePrescriptionOnBackPressed()

        // Then
        assertEquals(false, result)
        verify {
            view.showPrescriptionReminderDialog(uploadPrescriptionUiModel)
        }
    }

    @Test
    fun `GIVEN has not upload any prescription WHEN validate on back pressed THEN should not show reminder`() {
        // Given
        val uploadPrescriptionUiModel = UploadPrescriptionUiModel(
            showImageUpload = true,
            hasInvalidPrescription = false,
            uploadedImageCount = 0
        )
        presenter.setUploadPrescriptionData(
            uploadPrescriptionUiModel
        )
        presenter.shipmentCartItemModelList = listOf()

        // When
        val result = presenter.validatePrescriptionOnBackPressed()

        // Then
        assertEquals(true, result)
        verify(inverse = true) {
            view.showPrescriptionReminderDialog(any())
        }
    }

    @Test
    fun `GIVEN not show upload widget WHEN validate on back pressed THEN should not show reminder`() {
        // Given
        val uploadPrescriptionUiModel = UploadPrescriptionUiModel(
            showImageUpload = false,
            hasInvalidPrescription = true,
            uploadedImageCount = 1
        )
        presenter.setUploadPrescriptionData(
            uploadPrescriptionUiModel
        )
        presenter.shipmentCartItemModelList = listOf()

        // When
        val result = presenter.validatePrescriptionOnBackPressed()

        // Then
        assertEquals(true, result)
        verify(inverse = true) {
            view.showPrescriptionReminderDialog(any())
        }
    }

    @Test
    fun `GIVEN null shipment data WHEN validate on back pressed THEN should not show reminder`() {
        // Given
        val uploadPrescriptionUiModel = UploadPrescriptionUiModel(
            showImageUpload = true,
            hasInvalidPrescription = true,
            uploadedImageCount = 1
        )
        presenter.setUploadPrescriptionData(
            uploadPrescriptionUiModel
        )
        presenter.shipmentCartItemModelList = null

        // When
        val result = presenter.validatePrescriptionOnBackPressed()

        // Then
        assertEquals(true, result)
        verify(inverse = true) {
            view.showPrescriptionReminderDialog(any())
        }
    }

    @Test
    fun `GIVEN null upload prescription model WHEN validate on back pressed THEN should not show reminder`() {
        // Given
        val uploadPrescriptionUiModel = null
        presenter.setUploadPrescriptionData(
            uploadPrescriptionUiModel
        )
        presenter.shipmentCartItemModelList = null

        // When
        val result = presenter.validatePrescriptionOnBackPressed()

        // Then
        assertEquals(true, result)
        verify(inverse = true) {
            view.showPrescriptionReminderDialog(any())
        }
    }

    @Test
    fun `GIVEN null view WHEN validate on back pressed THEN should not show reminder`() {
        // Given
        val uploadPrescriptionUiModel = UploadPrescriptionUiModel(
            showImageUpload = true,
            hasInvalidPrescription = true,
            uploadedImageCount = 1
        )
        presenter.setUploadPrescriptionData(
            uploadPrescriptionUiModel
        )
        presenter.shipmentCartItemModelList = listOf()
        presenter.detachView()

        // When
        val result = presenter.validatePrescriptionOnBackPressed()

        // Then
        assertEquals(true, result)
        verify(inverse = true) {
            view.showPrescriptionReminderDialog(any())
        }
    }
}
