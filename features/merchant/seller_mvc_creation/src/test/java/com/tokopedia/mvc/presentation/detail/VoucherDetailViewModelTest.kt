package com.tokopedia.mvc.presentation.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.mvc.data.mapper.GetInitiateVoucherPageMapper
import com.tokopedia.mvc.data.mapper.ProductListMapper
import com.tokopedia.mvc.data.mapper.ShopBasicDataMapper
import com.tokopedia.mvc.data.response.GetInitiateVoucherPageResponse
import com.tokopedia.mvc.data.response.ProductListResponse
import com.tokopedia.mvc.data.response.ShopBasicDataResponse
import com.tokopedia.mvc.data.response.UpdateStatusVoucherDataModel
import com.tokopedia.mvc.domain.entity.GenerateVoucherImageMetadata
import com.tokopedia.mvc.domain.entity.VoucherDetailData
import com.tokopedia.mvc.domain.entity.VoucherDetailWithVoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.enums.UpdateVoucherAction
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.usecase.CancelVoucherUseCase
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.MerchantPromotionGetMVDataByIDUseCase
import com.tokopedia.mvc.domain.usecase.ProductListUseCase
import com.tokopedia.mvc.domain.usecase.ShopBasicDataUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class VoucherDetailViewModelTest {

    private lateinit var viewModel: VoucherDetailViewModel

    @RelaxedMockK
    lateinit var merchantPromotionGetMVDataByIDUseCase: MerchantPromotionGetMVDataByIDUseCase

    @RelaxedMockK
    lateinit var getProductsUseCase: ProductListUseCase

    @RelaxedMockK
    lateinit var getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase

    @RelaxedMockK
    lateinit var cancelVoucherUseCase: CancelVoucherUseCase

    @RelaxedMockK
    lateinit var shopBasicDataUseCase: ShopBasicDataUseCase

    @RelaxedMockK
    lateinit var voucherDetailObserver: Observer<in com.tokopedia.usecase.coroutines.Result<VoucherDetailWithVoucherCreationMetadata>>

    @RelaxedMockK
    lateinit var generateVoucherImageMetaDataObserver: Observer<in com.tokopedia.usecase.coroutines.Result<GenerateVoucherImageMetadata>>

    @RelaxedMockK
    lateinit var openDownloadVoucherImageBottomSheetObserver: Observer<in VoucherDetailData>

    @RelaxedMockK
    lateinit var redirectToProductListPageObserver: Observer<in VoucherDetailData>

    @RelaxedMockK
    lateinit var updateVoucherStatusDataObserver: Observer<in com.tokopedia.usecase.coroutines.Result<UpdateStatusVoucherDataModel>>

    @RelaxedMockK
    lateinit var initiateVoucherMapper: GetInitiateVoucherPageMapper

    @RelaxedMockK
    lateinit var shopBasicDataMapper: ShopBasicDataMapper

    @RelaxedMockK
    lateinit var productListMapper: ProductListMapper

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = VoucherDetailViewModel(
            CoroutineTestDispatchersProvider,
            merchantPromotionGetMVDataByIDUseCase,
            getProductsUseCase,
            getInitiateVoucherPageUseCase,
            cancelVoucherUseCase,
            shopBasicDataUseCase
        )
        with(viewModel) {
            voucherDetail.observeForever(voucherDetailObserver)
            generateVoucherImageMetadata.observeForever(generateVoucherImageMetaDataObserver)
            openDownloadVoucherImageBottomSheet.observeForever(
                openDownloadVoucherImageBottomSheetObserver
            )
            redirectToProductListPage.observeForever(redirectToProductListPageObserver)
            updateVoucherStatusData.observeForever(updateVoucherStatusDataObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            voucherDetail.removeObserver(voucherDetailObserver)
            generateVoucherImageMetadata.removeObserver(generateVoucherImageMetaDataObserver)
            openDownloadVoucherImageBottomSheet.removeObserver(
                openDownloadVoucherImageBottomSheetObserver
            )
            redirectToProductListPage.removeObserver(redirectToProductListPageObserver)
            updateVoucherStatusData.removeObserver(updateVoucherStatusDataObserver)
        }
    }

    @Test
    fun `when getVoucherDetail() is Called, should return the correct voucher detail data`() {
        runBlockingTest {
            // Given
            val voucherId = 10L
            mockGetVoucherDetailByIDGQLCall(voucherId)
            mockGetInitiateVoucherPageGqlCall()

            val expectedResult = Success(
                VoucherDetailWithVoucherCreationMetadata(
                    voucherDetail = VoucherDetailData(voucherId = 10),
                    creationMetadata = initiateVoucherMapper.map(GetInitiateVoucherPageResponse()),
                    tickerWording = ""
                )
            )

            // When
            viewModel.getVoucherDetail(voucherId)

            // Then
            val actual = viewModel.voucherDetail.getOrAwaitValue()
            assertEquals(expectedResult, actual)
        }
    }

    @Test
    fun `when getVoucherDetail() is error, should throw the error`() {
        runBlockingTest {
            // Given
            val voucherId = 10L
            val error = MessageErrorException("Error")

            coEvery { getInitiateVoucherPageUseCase.execute() } throws error

            val expected = Fail(error)

            // When
            viewModel.getVoucherDetail(voucherId)

            // Then
            val actual = viewModel.voucherDetail.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when getCurrentVoucherDetailData() is called, should return the latest voucher detail data`() {
        runBlockingTest {
            // Given
            val voucherId = 10L
            mockGetVoucherDetailByIDGQLCall(voucherId)
            mockGetInitiateVoucherPageGqlCall()

            val expectedResult = Success(
                VoucherDetailWithVoucherCreationMetadata(
                    voucherDetail = VoucherDetailData(voucherId = 10),
                    creationMetadata = initiateVoucherMapper.map(GetInitiateVoucherPageResponse()),
                    tickerWording = ""
                )
            )

            viewModel.getVoucherDetail(voucherId)

            // When
            viewModel.getCurrentVoucherDetailData()

            // Then
            val actual = viewModel.voucherDetail.getOrAwaitValue()
            assertEquals(expectedResult, actual)
        }
    }

    @Test
    fun `when getSpendingEstimation() is called, should return the corresponding formatted spending estimation`() {
        // Given
        val expectedResult = "Rp50.000"
        val voucherDiscount = 10000L
        val voucherQuota = 5L
        val voucherDetailData = VoucherDetailData(
            voucherDiscountAmount = voucherDiscount,
            voucherQuota = voucherQuota
        )

        // When
        val actual = viewModel.getSpendingEstimation(voucherDetailData)

        // Then
        assertEquals(expectedResult, actual)
    }

    @Test
    fun `when getPercentage() is called, should return the data accordingly`() {
        // Given
        val expectedResult = 25
        val availableQuota = 5L
        val remainingQuota = 20L

        // When
        val actual = viewModel.getPercentage(availableQuota, remainingQuota)
        val actualZeroResult = viewModel.getPercentage(availableQuota, 0)

        // Then
        assertEquals(expectedResult, actual)
        assertEquals(0, actualZeroResult)
    }

    @Test
    fun `when getThreeDotsBottomSheetType() is called, should return the cirrect voucher status data`() {
        // Given
        val expected = VoucherStatus.ONGOING
        val voucherDetailData = VoucherDetailData(voucherStatus = VoucherStatus.ONGOING)

        // When
        val actual = viewModel.getThreeDotsBottomSheetType(voucherDetailData)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `when generateVoucherImage() is called, should set the voucher image data accordingly`() {
        runBlocking {
            // Given
            val voucherId = 10L
            val voucherDetail = VoucherDetailData(voucherId)
            val shopData = shopBasicDataMapper.map(ShopBasicDataResponse())
            val products = productListMapper.map(ProductListResponse())
            val topSellingProductImageUrls = products.products
                .sortedByDescending { it.txStats.sold }
                .take(3)
                .map { it.picture }

            mockGetVoucherDetailByIDGQLCall(voucherId)
            mockGetInitiateVoucherPageGqlCall()
            mockShopBasicDataGQLCall()
            mockGetProductGQLCall()

            val expectedResult = Success(
                GenerateVoucherImageMetadata(
                    voucherDetail = voucherDetail,
                    shopData = shopData,
                    topSellingProductImageUrls
                )
            )

            viewModel.getVoucherDetail(voucherId)

            // When
            viewModel.generateVoucherImage()

            // Then
            val actual = viewModel.generateVoucherImageMetadata.getOrAwaitValue()
            assertEquals(expectedResult, actual)
        }
    }

    @Test
    fun `when onTapDownloadVoucherImage() is called, should set _openDownloadVoucherImageBottomSheet value accordingly`() {
        runBlockingTest {
            // Given
            val voucherId = 10L
            mockGetVoucherDetailByIDGQLCall(voucherId)
            mockGetInitiateVoucherPageGqlCall()

            val expectedResult = VoucherDetailData(voucherId = 10)

            viewModel.getVoucherDetail(voucherId)

            // When
            viewModel.onTapDownloadVoucherImage()

            // Then
            val actual = viewModel.openDownloadVoucherImageBottomSheet.getOrAwaitValue()
            assertEquals(expectedResult, actual)
        }
    }

    @Test
    fun `when onTapViewAllProductCta is called, should set _redirectToProductListPage value accordingly`() {
        runBlockingTest {
            // Given
            val voucherId = 10L
            mockGetVoucherDetailByIDGQLCall(voucherId)
            mockGetInitiateVoucherPageGqlCall()

            val expectedResult = VoucherDetailData(voucherId = 10)

            viewModel.getVoucherDetail(voucherId)

            // When
            viewModel.onTapViewAllProductCta()

            // Then
            val actual = viewModel.redirectToProductListPage.getOrAwaitValue()
            assertEquals(expectedResult, actual)
        }
    }

    private fun mockGetInitiateVoucherPageGqlCall() {
        val result = initiateVoucherMapper.map(GetInitiateVoucherPageResponse())
        coEvery { getInitiateVoucherPageUseCase.execute() } returns result
    }

    private fun mockGetVoucherDetailByIDGQLCall(voucherId: Long) {
        val result = VoucherDetailData(voucherId = voucherId)
        coEvery { merchantPromotionGetMVDataByIDUseCase.execute(any()) } returns result
    }

    private fun mockUpdateVoucherStatusGQLCall(data: VoucherDetailData) {
        val voucherId = 10
        val result = UpdateStatusVoucherDataModel()
        val couponStatus = if (data.voucherStatus == VoucherStatus.NOT_STARTED) UpdateVoucherAction.DELETE else UpdateVoucherAction.STOP

        mockGetInitiateVoucherPageGqlCall()

        coEvery { cancelVoucherUseCase.execute(voucherId, couponStatus, "token") } returns result
    }

    private fun mockShopBasicDataGQLCall() {
        val result = shopBasicDataMapper.map(ShopBasicDataResponse())
        coEvery { shopBasicDataUseCase.execute() } returns result
    }

    private fun mockGetProductGQLCall() {
        val result = productListMapper.map(ProductListResponse())
        coEvery { getProductsUseCase.execute(any()) } returns result
    }
}
