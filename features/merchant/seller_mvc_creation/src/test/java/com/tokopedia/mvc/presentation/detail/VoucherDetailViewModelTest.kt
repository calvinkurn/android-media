package com.tokopedia.mvc.presentation.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.mvc.data.mapper.GetInitiateVoucherPageMapper
import com.tokopedia.mvc.data.response.GetInitiateVoucherPageResponse
import com.tokopedia.mvc.data.response.UpdateStatusVoucherDataModel
import com.tokopedia.mvc.domain.entity.GenerateVoucherImageMetadata
import com.tokopedia.mvc.domain.entity.VoucherDetailData
import com.tokopedia.mvc.domain.entity.VoucherDetailWithVoucherCreationMetadata
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
    lateinit var mapper: GetInitiateVoucherPageMapper

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
            openDownloadVoucherImageBottomSheet.observeForever(openDownloadVoucherImageBottomSheetObserver)
            redirectToProductListPage.observeForever(redirectToProductListPageObserver)
            updateVoucherStatusData.observeForever(updateVoucherStatusDataObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            voucherDetail.removeObserver(voucherDetailObserver)
            generateVoucherImageMetadata.removeObserver(generateVoucherImageMetaDataObserver)
            openDownloadVoucherImageBottomSheet.removeObserver(openDownloadVoucherImageBottomSheetObserver)
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
                    creationMetadata = mapper.map(GetInitiateVoucherPageResponse()),
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

    private fun mockGetInitiateVoucherPageGqlCall() {
        val result = mapper.map(GetInitiateVoucherPageResponse())
        coEvery { getInitiateVoucherPageUseCase.execute() } returns result
    }

    private fun mockGetVoucherDetailByIDGQLCall(voucherId: Long) {
        val result = VoucherDetailData(voucherId = voucherId)
        coEvery { merchantPromotionGetMVDataByIDUseCase.execute(any()) } returns result
    }
}
