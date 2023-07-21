package com.tokopedia.mvc.presentation.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.campaign.data.response.GetTargetedTickerResponse
import com.tokopedia.campaign.mapper.GetTargetedTickerMapper
import com.tokopedia.campaign.usecase.GetTargetedTickerUseCase
import com.tokopedia.mvc.R
import com.tokopedia.mvc.data.mapper.GetInitiateVoucherPageMapper
import com.tokopedia.mvc.data.mapper.ProductListMapper
import com.tokopedia.mvc.data.mapper.ShopBasicDataMapper
import com.tokopedia.mvc.data.response.GetInitiateVoucherPageResponse
import com.tokopedia.mvc.data.response.ProductListResponse
import com.tokopedia.mvc.data.response.ShopBasicDataResponse
import com.tokopedia.mvc.data.response.UpdateStatusVoucherDataModel
import com.tokopedia.mvc.domain.entity.ShareComponentMetaData
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadataWithRemoteTickerMessage
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import com.tokopedia.mvc.domain.entity.VoucherDetailData
import com.tokopedia.mvc.domain.entity.enums.UpdateVoucherAction
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.usecase.CancelVoucherUseCase
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.GetVoucherListChildUseCase
import com.tokopedia.mvc.domain.usecase.GetVoucherListUseCase
import com.tokopedia.mvc.domain.usecase.GetVoucherQuotaUseCase
import com.tokopedia.mvc.domain.usecase.MerchantPromotionGetMVDataByIDUseCase
import com.tokopedia.mvc.domain.usecase.ProductListUseCase
import com.tokopedia.mvc.domain.usecase.ShopBasicDataUseCase
import com.tokopedia.mvc.presentation.list.constant.PageState
import com.tokopedia.mvc.presentation.list.viewmodel.MvcListViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MvcListViewModelTest {

    private lateinit var viewModel: MvcListViewModel

    @RelaxedMockK
    lateinit var getVoucherListUseCase: GetVoucherListUseCase

    @RelaxedMockK
    lateinit var getVoucherQuotaUseCase: GetVoucherQuotaUseCase

    @RelaxedMockK
    lateinit var getVoucherListChildUseCase: GetVoucherListChildUseCase

    @RelaxedMockK
    lateinit var cancelVoucherUseCase: CancelVoucherUseCase

    @RelaxedMockK
    lateinit var getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase

    @RelaxedMockK
    lateinit var merchantPromotionGetMVDataByIDUseCase: MerchantPromotionGetMVDataByIDUseCase

    @RelaxedMockK
    lateinit var shopBasicDataUseCase: ShopBasicDataUseCase

    @RelaxedMockK
    lateinit var getProductUseCase: ProductListUseCase

    @RelaxedMockK
    lateinit var getTargetedTickerUseCase: GetTargetedTickerUseCase

    @RelaxedMockK
    lateinit var voucherListObserver: Observer<in List<Voucher>>

    @RelaxedMockK
    lateinit var voucherChildsObserver: Observer<in List<Voucher>>

    @RelaxedMockK
    lateinit var voucherQuotaObserver: Observer<in VoucherCreationQuota>

    @RelaxedMockK
    lateinit var isLoadingObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var errorObserver: Observer<in Throwable>

    @RelaxedMockK
    lateinit var voucherCreationMetaDataObserver: Observer<in Result<VoucherCreationMetadataWithRemoteTickerMessage>>

    @RelaxedMockK
    lateinit var generateShareComponentMetaDataObserver: Observer<in Result<ShareComponentMetaData>>

    @RelaxedMockK
    lateinit var pageStateObserver: Observer<in PageState>

    @RelaxedMockK
    lateinit var shopBasicMapper: ShopBasicDataMapper

    @RelaxedMockK
    lateinit var productListMapper: ProductListMapper

    @RelaxedMockK
    lateinit var remoteTickerMapper: GetTargetedTickerMapper

    @RelaxedMockK
    lateinit var getInitiateVoucherPageMapper: GetInitiateVoucherPageMapper

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MvcListViewModel(
            CoroutineTestDispatchersProvider,
            getVoucherListUseCase,
            getVoucherQuotaUseCase,
            getVoucherListChildUseCase,
            cancelVoucherUseCase,
            getInitiateVoucherPageUseCase,
            merchantPromotionGetMVDataByIDUseCase,
            shopBasicDataUseCase,
            getProductUseCase,
            getTargetedTickerUseCase
        )
        with(viewModel) {
            voucherList.observeForever(voucherListObserver)
            voucherChilds.observeForever(voucherChildsObserver)
            voucherQuota.observeForever(voucherQuotaObserver)
            isLoading.observeForever(isLoadingObserver)
            error.observeForever(errorObserver)
            voucherCreationMetadata.observeForever(voucherCreationMetaDataObserver)
            generateShareComponentMetaData.observeForever(generateShareComponentMetaDataObserver)
            pageState.observeForever(pageStateObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            voucherList.removeObserver(voucherListObserver)
            voucherChilds.removeObserver(voucherChildsObserver)
            voucherQuota.removeObserver(voucherQuotaObserver)
            isLoading.removeObserver(isLoadingObserver)
            error.removeObserver(errorObserver)
            voucherCreationMetadata.removeObserver(voucherCreationMetaDataObserver)
            generateShareComponentMetaData.removeObserver(generateShareComponentMetaDataObserver)
            pageState.removeObserver(pageStateObserver)
        }
    }

    @Test
    fun `when setFilterKeyword() is called, should set filter keyword value accordingly`() {
        // Given
        val keyword = "Tokopedia"
        val expectedKeyword = "Tokopedia"

        // When
        viewModel.setFilterKeyword(keyword)

        // Then
        val actual = viewModel.filter.keyword
        assertEquals(expectedKeyword, actual)
    }

    @Test
    fun `when setFilterStatus() is called, should set filter status value accordingly`() {
        // Given
        val status = listOf(VoucherStatus.ONGOING)
        val expectedStatus = listOf(VoucherStatus.ONGOING)

        // When
        viewModel.setFilterStatus(status)

        // Then
        val actual = viewModel.filter.status
        assertEquals(expectedStatus, actual)
    }

    @Test
    fun `when setFilterType() is called, and is enabling is true should add filter type value accordingly`() {
        // Given
        val type = VoucherServiceType.SHOP_VOUCHER
        val expectedType = listOf(
            VoucherServiceType.SHOP_VOUCHER,
            VoucherServiceType.PRODUCT_VOUCHER,
            VoucherServiceType.SHOP_VOUCHER
        )

        // When
        viewModel.setFilterType(type, true)

        // Then
        val actual = viewModel.filter.voucherType
        assertEquals(expectedType, actual)
    }

    @Test
    fun `when voucher status is set to NOT_STARTED then getSelectedStatusText() is called, should return the correct string res id`() {
        // Given
        val voucherNotStarted = listOf(VoucherStatus.NOT_STARTED)
        val expectedVoucherNotStarted = R.string.smvc_bottomsheet_filter_voucher_notstarted

        viewModel.setFilterStatus(voucherNotStarted)

        // When
        val actual = viewModel.getSelectedStatusText()

        // Then
        assertEquals(expectedVoucherNotStarted, actual)
    }

    @Test
    fun `when voucher status is set to ONGOING then getSelectedStatusText() is called, should return the correct string res id`() {
        // Given
        val voucherOnGoing = listOf(VoucherStatus.ONGOING)
        val expectedVoucherOnGoing = R.string.smvc_bottomsheet_filter_voucher_ongoing

        viewModel.setFilterStatus(voucherOnGoing)

        // When
        val actual = viewModel.getSelectedStatusText()

        // Then
        assertEquals(expectedVoucherOnGoing, actual)
    }

    @Test
    fun `when voucher status is set to NOT_STARTED & ONGOING then getSelectedStatusText() is called, should return the correct string res id`() {
        // Given
        val voucherActive = listOf(VoucherStatus.NOT_STARTED, VoucherStatus.ONGOING)
        val expectedVoucherActive = R.string.smvc_bottomsheet_filter_voucher_active

        viewModel.setFilterStatus(voucherActive)

        // When
        val actual = viewModel.getSelectedStatusText()

        // Then
        assertEquals(expectedVoucherActive, actual)
    }

    @Test
    fun `when voucher status is set to ENDED & STOPPED then getSelectedStatusText() is called, should return the correct string res id`() {
        // Given
        val voucherFinished = listOf(VoucherStatus.ENDED, VoucherStatus.STOPPED)
        val expectedVoucherFinishied = R.string.smvc_bottomsheet_filter_voucher_finished

        viewModel.setFilterStatus(voucherFinished)

        // When
        val actual = viewModel.getSelectedStatusText()

        // Then
        assertEquals(expectedVoucherFinishied, actual)
    }

    @Test
    fun `when voucher status is set to all status then getSelectedStatusText() is called, should return the correct string res id`() {
        // Given
        val allVoucher = listOf(VoucherStatus.ONGOING, VoucherStatus.NOT_STARTED, VoucherStatus.ENDED, VoucherStatus.STOPPED)
        val expectedAllVoucher = R.string.smvc_bottomsheet_filter_voucher_all

        viewModel.setFilterStatus(allVoucher)

        // When
        val actual = viewModel.getSelectedStatusText()

        // Then
        assertEquals(expectedAllVoucher, actual)
    }

    @Test
    fun `when setFilterType() is called, and is enabling is false should remove filter type value accordingly`() {
        // Given
        val type = VoucherServiceType.PRODUCT_VOUCHER
        val expectedType = listOf(VoucherServiceType.SHOP_VOUCHER)
        viewModel.setFilterType(type, true)

        // When
        viewModel.setFilterType(type, false)

        // Then
        val actual = viewModel.filter.voucherType
        assertEquals(expectedType, actual)
    }

    @Test
    fun `when getVoucherList() is called, should set voucherList value accordingly`() {
        runBlocking {
            // Given
            val result = listOf(Voucher(name = "vc discount"))
            coEvery { getVoucherListUseCase.execute(any()) } returns result
            val page = 1
            val pageSize = 10
            val expected = listOf(Voucher(name = "vc discount"))

            // When
            viewModel.getVoucherList(page, pageSize)

            // Then
            val actual = viewModel.voucherList.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when getVoucherList() is countering error, should set the error value accordingly`() {
        runBlocking {
            // Given
            val error = MessageErrorException("Error")
            coEvery { getVoucherListUseCase.execute(any()) } throws error
            val page = 1
            val pageSize = 10
            val expected = MessageErrorException("Error")

            // When
            viewModel.getVoucherList(page, pageSize)

            // Then
            val actual = viewModel.error.getOrAwaitValue()
            assertEquals(expected.localizedMessage, actual.localizedMessage)
        }
    }

    @Test
    fun `when getVoucherQuota() is called, should set the voucher quota value accordingly`() {
        runBlocking {
            // Given
            val result = VoucherCreationQuota(tickerTitle = "available")
            coEvery { getVoucherQuotaUseCase.execute() } returns result
            val expected = VoucherCreationQuota(tickerTitle = "available")

            // When
            viewModel.getVoucherQuota()

            // Then
            val actual = viewModel.voucherQuota.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when getVoucherQuota() is countering error, should set the error value accordingly`() {
        runBlocking {
            // Given
            val error = MessageErrorException("Error")
            coEvery { getVoucherQuotaUseCase.execute() } throws error
            val expected = MessageErrorException("Error")

            // When
            viewModel.getVoucherQuota()

            // Then
            val actual = viewModel.error.getOrAwaitValue()
            assertEquals(expected.localizedMessage, actual.localizedMessage)
        }
    }

    @Test
    fun `when getVoucherListChild() is called, should set voucherChilds value accordingly`() {
        runBlocking {
            // Given
            val voucherId = 10L
            val parentId = 100L
            val voucherStatus = arrayListOf(
                VoucherStatus.NOT_STARTED,
                VoucherStatus.ONGOING
            )
            val result = listOf(Voucher(name = "voucher promo tkpd"))
            coEvery { getVoucherListChildUseCase.execute(any(), voucherStatus) } returns result
            val expected = listOf(Voucher(name = "voucher promo tkpd"))

            // When
            viewModel.getVoucherListChild(voucherId, parentId)

            // Then
            val actual = viewModel.voucherChilds.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when getVoucherListChild() is countering error, should set error value accordingly`() {
        runBlocking {
            // Given
            val error = MessageErrorException("Server Error")
            val voucherId = 10L
            val parentId = 100L
            val voucherStatus = arrayListOf(
                VoucherStatus.NOT_STARTED,
                VoucherStatus.ONGOING
            )
            coEvery { getVoucherListChildUseCase.execute(any(), voucherStatus) } throws error
            val expected = MessageErrorException("Server Error")

            // When
            viewModel.getVoucherListChild(voucherId, parentId)

            // Then
            val actual = viewModel.error.getOrAwaitValue()
            assertEquals(expected.localizedMessage, actual.localizedMessage)
        }
    }

    @Test
    fun `when getFilterCount() is called, should return the count value accordingly`() {
        // Given
        viewModel.setFilterType(VoucherServiceType.SHOP_VOUCHER, true)
        viewModel.setFilterStatus(listOf(VoucherStatus.ONGOING))
        val expected = 2

        // When
        val actual = viewModel.getFilterCount()

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `when generateShareComponentMetaData() is called, should set the data accordingly`() {
        runBlocking {
            // Given
            val voucher = Voucher()
            val shopData = shopBasicMapper.map(ShopBasicDataResponse())
            val products = productListMapper.map(ProductListResponse())

            mockShopBasicDataGqlCall()
            mockGetProductGqlCall()

            val expected = Success(
                ShareComponentMetaData(
                    voucher = voucher,
                    shopData = shopData,
                    topSellingProductImageUrls = products.products
                        .sortedByDescending { it.txStats.sold }
                        .take(3)
                        .map { it.picture }
                )
            )

            // When
            viewModel.generateShareComponentMetaData(voucher)

            // Then
            val actual = viewModel.generateShareComponentMetaData.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when generateShareComponentMetaData() is countering an error, should set error value accordingly`() {
        runBlocking {
            // Given
            val voucher = Voucher()
            val error = MessageErrorException("Server Error")
            coEvery { shopBasicDataUseCase.execute() } throws error
            val expected = Fail(error)

            // When
            viewModel.generateShareComponentMetaData(voucher)

            // Then
            val actual = viewModel.generateShareComponentMetaData.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when getVoucherCreationMetaData() is called, shoud set the voucherCreationMetadata accordingly`() {
        runBlocking {
            // Given
            mockVoucherCreationMetaDataGqlCall()
            mockGetTargetedTickerGqlCall()
            val voucherCreationMetaData =
                getInitiateVoucherPageMapper.map(GetInitiateVoucherPageResponse())
            val tickers =
                remoteTickerMapper.map(GetTargetedTickerResponse(getTargetedTicker = GetTargetedTickerResponse.GetTargetedTicker()))

            val expected = Success(
                VoucherCreationMetadataWithRemoteTickerMessage(
                    voucherCreationMetaData,
                    tickers
                )
            )

            // When
            viewModel.getVoucherCreationMetadata()

            // Then
            val actual = viewModel.voucherCreationMetadata.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when getVoucherCreationMetadata() is countering an error, should return error accordingly`() {
        runBlocking {
            // Given
            val error = MessageErrorException("Server Error")
            coEvery { getInitiateVoucherPageUseCase.execute() } throws error
            val expected = Fail(error)

            // When
            viewModel.getVoucherCreationMetadata()

            // Then
            val actual = viewModel.voucherCreationMetadata.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    private fun mockShopBasicDataGqlCall() {
        val result = shopBasicMapper.map(ShopBasicDataResponse())
        coEvery { shopBasicDataUseCase.execute() } returns result
    }

    private fun mockGetProductGqlCall() {
        val result = productListMapper.map(ProductListResponse())
        coEvery { getProductUseCase.execute(any()) } returns result
    }

    private fun mockVoucherCreationMetaDataGqlCall() {
        val result = getInitiateVoucherPageMapper.map(GetInitiateVoucherPageResponse())
        coEvery { getInitiateVoucherPageUseCase.execute() } returns result
    }

    private fun mockGetTargetedTickerGqlCall() {
        val result =
            remoteTickerMapper.map(GetTargetedTickerResponse(getTargetedTicker = GetTargetedTickerResponse.GetTargetedTicker()))
        coEvery { getTargetedTickerUseCase.execute(any()) } returns result
    }

    private fun mockUpdateVoucherStatusGQLCall(data: VoucherDetailData) {
        val voucherId = 10
        val result = UpdateStatusVoucherDataModel()
        val couponStatus = if (data.voucherStatus == VoucherStatus.NOT_STARTED) UpdateVoucherAction.DELETE else UpdateVoucherAction.STOP

        coEvery { cancelVoucherUseCase.execute(voucherId, couponStatus, "token") } returns result
    }
}
