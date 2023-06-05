package com.tokopedia.mvc.presentation.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.mvc.domain.entity.ShareComponentMetaData
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadataWithRemoteTickerMessage
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.usecase.CancelVoucherUseCase
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.mvc.domain.usecase.GetVoucherListChildUseCase
import com.tokopedia.mvc.domain.usecase.GetVoucherListUseCase
import com.tokopedia.mvc.domain.usecase.GetVoucherQuotaUseCase
import com.tokopedia.mvc.domain.usecase.MerchantPromotionGetMVDataByIDUseCase
import com.tokopedia.mvc.domain.usecase.ProductListUseCase
import com.tokopedia.mvc.domain.usecase.ShopBasicDataUseCase
import com.tokopedia.mvc.presentation.list.constant.PageState
import com.tokopedia.mvc.presentation.list.viewmodel.MvcListViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
}
