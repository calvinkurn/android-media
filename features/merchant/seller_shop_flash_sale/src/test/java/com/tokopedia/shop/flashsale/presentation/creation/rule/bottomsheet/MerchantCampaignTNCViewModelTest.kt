package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.domain.usecase.GetMerchantCampaignTNCUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantCampaignTNCViewModelTest {

    @RelaxedMockK
    lateinit var getMerchantTNCUseCase: GetMerchantCampaignTNCUseCase

    @RelaxedMockK
    lateinit var merchantCampaignTNCObserver: Observer<in Result<MerchantCampaignTNC>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MerchantCampaignTNCViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = MerchantCampaignTNCViewModel(
            CoroutineTestDispatchersProvider,
            getMerchantTNCUseCase
        )

        with(viewModel) {
            merchantCampaignTNC.observeForever(merchantCampaignTNCObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            merchantCampaignTNC.removeObserver(merchantCampaignTNCObserver)
        }
    }

    @Test
    fun `when getMerchantCampaignTNC success, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                //given
                val campaignId: Long = 1001
                val isUniqueBuyer = true
                val isCampaignRelation = true
                val paymentType = PaymentType.REGULAR
                val result = MerchantCampaignTNC()
                val expected = Success(result)

                coEvery {
                    getMerchantTNCUseCase.execute(
                        campaignId,
                        isUniqueBuyer,
                        isCampaignRelation,
                        paymentType
                    )
                } returns result

                //when
                getMerchantCampaignTNC(
                    campaignId,
                    isUniqueBuyer,
                    isCampaignRelation,
                    paymentType
                )

                val actual = merchantCampaignTNC.getOrAwaitValue()

                //then
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `when getMerchantCampaignTNC error, observer will receive error result`() {
        runBlocking {
            with(viewModel) {
                //given
                val dummyThrowable = MessageErrorException("Error")
                val campaignId: Long = 1001
                val isUniqueBuyer = true
                val isCampaignRelation = true
                val paymentType = PaymentType.REGULAR
                val expected = Fail(dummyThrowable)

                coEvery {
                    getMerchantTNCUseCase.execute(
                        campaignId,
                        isUniqueBuyer,
                        isCampaignRelation,
                        paymentType
                    )
                } throws dummyThrowable

                //when
                getMerchantCampaignTNC(
                    campaignId,
                    isUniqueBuyer,
                    isCampaignRelation,
                    paymentType
                )

                val actual = merchantCampaignTNC.getOrAwaitValue()

                //then
                assertEquals(expected, actual)
            }
        }
    }

}