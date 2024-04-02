package com.tokopedia.thankyou_native.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.thankyou_native.data.mapper.PurchaseInfoMapper
import com.tokopedia.thankyou_native.domain.model.Details
import com.tokopedia.thankyou_native.domain.model.PurchaseInfo
import com.tokopedia.thankyou_native.domain.model.PurchaseInfoResponse
import com.tokopedia.thankyou_native.domain.model.Section
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.domain.usecase.FetchPurchaseInfoUseCase
import com.tokopedia.thankyou_native.presentation.viewModel.DetailInvoiceViewModel
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailInvoiceViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val fetchPurchaseInfoUseCase = mockk<FetchPurchaseInfoUseCase>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: DetailInvoiceViewModel

    @Before
    fun setUp() {
        viewModel = DetailInvoiceViewModel(dispatcher, fetchPurchaseInfoUseCase)
    }

    @Test
    fun `fetch purchase info success`() {
        val purchaseInfoResponse = mockk<PurchaseInfoResponse>(relaxed = true)
        val thanksPageData = mockk<ThanksPageData>(relaxed = true)

        // given
        every {
            purchaseInfoResponse.purchaseInfo
        } answers {
            PurchaseInfo(
                listOf(
                    Section(
                        type = PurchaseInfoMapper.TYPE_HEADING_1,
                        details = Details()
                    ),
                    Section(
                        type = PurchaseInfoMapper.TYPE_NORMAL_TEXT,
                        details = Details()
                    )
                ),
                listOf(
                    Section(
                        type = PurchaseInfoMapper.TYPE_HEADING_2,
                        details = Details()
                    ),
                    Section(
                        type = PurchaseInfoMapper.TYPE_SHIPPING,
                        details = Details()
                    )
                )
            )
        }

        every {
            fetchPurchaseInfoUseCase.invoke(any(), any(), any(), any(), any())
        } answers {
            firstArg<(PurchaseInfo) ->Unit>().invoke(purchaseInfoResponse.purchaseInfo)
        }

        // when
        viewModel.fetchPurchaseInfo(thanksPageData)

        // assert
        Assert.assertEquals(viewModel.purchaseDetailVisitables.value?.size, 5)
    }

    @Test
    fun `fetch purchase info failed`() {
        val thanksPageData = mockk<ThanksPageData>(relaxed = true)
        val throwable = mockk<Throwable>(relaxed = true)

        // given
        every {
            fetchPurchaseInfoUseCase.invoke(any(), any(), any(), any(), any())
        } answers {
            secondArg<(Throwable) ->Unit>().invoke(throwable)
        }

        // when
        viewModel.fetchPurchaseInfo(thanksPageData)

        // assert
        Assert.assertEquals(viewModel.purchaseDetailVisitables.value, null)
    }
}
