package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV3UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
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

    @MockK
    private lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    @MockK
    private lateinit var epharmacyUseCase: EPharmacyPrepareProductsGroupUseCase

    private var shipmentDataConverter = ShipmentDataConverter()

    private lateinit var presenter: ShipmentPresenter

    private var gson = Gson()

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
            epharmacyUseCase,
            validateUsePromoRevampUseCase,
            gson,
            TestSchedulers,
            eligibleForAddressUseCase
        )
        presenter.attachView(view)
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
            ),
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
    fun `GIVEN rejected consultation WHEN fetch epharmacy data THEN should set error`() {
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
                                        GroupData.EpharmacyGroup.ProductsInfo(
                                            shopId = "6554231",
                                            products = listOf(
                                                GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389388,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = 1,
                                                    quantityString = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = "",
                                            orderName = null
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
                                    cta = null
                                )
                            ),
                            attachmentPageTickerText = null
                        ),
                    )
                )
            )
        }
        presenter.shipmentCartItemModelList = arrayListOf(
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389388
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
                hasInvalidPrescription = true,
                rejectedWording = rejectedWording
            ),
            presenter.uploadPrescriptionUiModel
        )
        assertEquals(true, presenter.shipmentCartItemModelList[0].isError)
        assertEquals(rejectedWording, presenter.shipmentCartItemModelList[0].errorTitle)
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
                                        GroupData.EpharmacyGroup.ProductsInfo(
                                            shopId = "6554231",
                                            products = listOf(
                                                GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389388,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = 1,
                                                    quantityString = "1"
                                                ),
                                                GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389389,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = 1,
                                                    quantityString = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = "",
                                            orderName = null
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
                                        prescription = listOf(
                                            GroupData.EpharmacyGroup.ConsultationData.Prescription(
                                                documentUrl = null,
                                                id = null,
                                                type = null
                                            ),
                                            GroupData.EpharmacyGroup.ConsultationData.Prescription(
                                                documentUrl = null,
                                                id = null,
                                                type = null
                                            )
                                        ),
                                        startTime = ""
                                    ),
                                    consultationSource = null,
                                    prescriptionSource = null,
                                    cta = null
                                )
                            ),
                            attachmentPageTickerText = null
                        ),
                    )
                )
            )
        }
        presenter.shipmentCartItemModelList = arrayListOf(
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389388
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
                        productId = 2150389387
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
                uploadedImageCount = 2
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
                                        GroupData.EpharmacyGroup.ProductsInfo(
                                            shopId = "6554231",
                                            products = listOf(
                                                GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389389,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = 1,
                                                    quantityString = "1"
                                                ),
                                                GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389388,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = 1,
                                                    quantityString = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = "",
                                            orderName = null
                                        ),
                                        GroupData.EpharmacyGroup.ProductsInfo(
                                            shopId = "6554234",
                                            products = listOf(
                                                GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389382,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = 1,
                                                    quantityString = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = "",
                                            orderName = null
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
                                        prescription = listOf(
                                            GroupData.EpharmacyGroup.ConsultationData.Prescription(
                                                documentUrl = null,
                                                id = null,
                                                type = null
                                            ),
                                            GroupData.EpharmacyGroup.ConsultationData.Prescription(
                                                documentUrl = null,
                                                id = null,
                                                type = null
                                            )
                                        ),
                                        startTime = ""
                                    ),
                                    consultationSource = null,
                                    prescriptionSource = null,
                                    cta = null
                                ),
                                GroupData.EpharmacyGroup(
                                    epharmacyGroupId = "124",
                                    shopInfo = listOf(
                                        GroupData.EpharmacyGroup.ProductsInfo(
                                            shopId = "6554231",
                                            products = listOf(
                                                GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389387,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = 1,
                                                    quantityString = "1"
                                                ),
                                                GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389386,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = 1,
                                                    quantityString = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = "",
                                            orderName = null
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
                                    cta = null
                                ),
                                GroupData.EpharmacyGroup(
                                    epharmacyGroupId = "125",
                                    shopInfo = listOf(
                                        GroupData.EpharmacyGroup.ProductsInfo(
                                            shopId = "6554232",
                                            products = listOf(
                                                GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389384,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = 1,
                                                    quantityString = "1"
                                                ),
                                                GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389385,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = 1,
                                                    quantityString = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = "",
                                            orderName = null
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
                                    cta = null
                                )
                            ),
                            attachmentPageTickerText = null
                        ),
                    )
                )
            )
        }
        presenter.shipmentCartItemModelList = arrayListOf(
            ShipmentCartItemModel(
                shopId = 6554231,
                cartItemModels = listOf(
                    CartItemModel(
                        productId = 2150389388
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
                        productId = 2150389387
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
        presenter.fetchEpharmacyData()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = true,
                uploadedImageCount = 4
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
}
