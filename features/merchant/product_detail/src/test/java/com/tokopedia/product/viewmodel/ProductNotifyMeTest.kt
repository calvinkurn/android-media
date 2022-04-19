package com.tokopedia.product.viewmodel

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.data.model.upcoming.Result
import com.tokopedia.product.detail.data.model.upcoming.TeaserNotifyMe
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

/**
 * Created by Yehezkiel on 04/10/21
 * Using 2 products Ids :
 *  - 518076293 with intial value not registered ingatkan saya
 *  - 518076286 with initial value registered ingatkan saya
 */
@ExperimentalCoroutinesApi
class ProductNotifyMeTest : DynamicProductDetailViewModelTest() {

    @Test
    fun `on success toggle notify me register`() = runBlockingTest {
        //use product id 518076293 as non active notify me (register action)
        `on success get product info login`()
        val requestParamsSlot = slot<RequestParams>()
        val isNotifyMe = false

        val mockSuccessData = TeaserNotifyMe(
                result = Result(
                        isSuccess = true,
                        errorMessage = "",
                        message = "Sukses register notify me"
                )
        )

        coEvery {
            toggleNotifyMeUseCase.executeOnBackground(any())
        } returns mockSuccessData

        viewModel.toggleTeaserNotifyMe(
                isNotifyMeActive = isNotifyMe,
                campaignId = 1234L,
                productId = 518076293L)

        coVerify { toggleNotifyMeUseCase.executeOnBackground(capture(requestParamsSlot)) }

        val requestParamsSlotResult = requestParamsSlot.captured
        Assert.assertEquals(requestParamsSlotResult.getLong("campaignId", 0L), 1234L)
        Assert.assertEquals(requestParamsSlotResult.getLong("product_id_64", 0L), 518076293L)
        Assert.assertEquals(requestParamsSlotResult.getString("action", ""), "REGISTER")
        Assert.assertEquals(requestParamsSlotResult.getString("source", ""), "pdp")

        Assert.assertTrue(viewModel.toggleTeaserNotifyMe.value is Success)
        Assert.assertEquals((viewModel.toggleTeaserNotifyMe.value as Success).data.successMessage, "Sukses register notify me")
        Assert.assertEquals((viewModel.toggleTeaserNotifyMe.value as Success).data.isSuccess, true)
        Assert.assertEquals((viewModel.toggleTeaserNotifyMe.value as Success).data.notifyMeAction, "REGISTER")

        //make sure the notify me changed
        val upcomingData = viewModel.p2Data.value?.upcomingCampaigns?.get(
                requestParamsSlotResult.getLong("product_id_64", 0L).toString()
        )

        Assert.assertNotNull(upcomingData)
        Assert.assertNotNull(upcomingData?.notifyMe)
        Assert.assertEquals(upcomingData?.notifyMe, true)
    }

    @Test
    fun `on success toggle notify me un-register`() = runBlockingTest {
        //use product id 518076286 as non active notify me (register action)
        `on success get product info login`()
        val requestParamsSlot = slot<RequestParams>()
        val isNotifyMe = true

        val mockSuccessData = TeaserNotifyMe(
                result = Result(
                        isSuccess = true,
                        errorMessage = "",
                        message = "Sukses un-register notify me"
                )
        )

        coEvery {
            toggleNotifyMeUseCase.executeOnBackground(any())
        } returns mockSuccessData

        viewModel.toggleTeaserNotifyMe(
                isNotifyMeActive = isNotifyMe,
                campaignId = 1234L,
                productId = 518076286L)

        coVerify { toggleNotifyMeUseCase.executeOnBackground(capture(requestParamsSlot)) }

        val requestParamsSlotResult = requestParamsSlot.captured
        Assert.assertEquals(requestParamsSlotResult.getLong("campaignId", 0L), 1234L)
        Assert.assertEquals(requestParamsSlotResult.getLong("product_id_64", 0L), 518076286L)
        Assert.assertEquals(requestParamsSlotResult.getString("action", ""), "UNREGISTER")
        Assert.assertEquals(requestParamsSlotResult.getString("source", ""), "pdp")

        Assert.assertTrue(viewModel.toggleTeaserNotifyMe.value is Success)
        Assert.assertEquals((viewModel.toggleTeaserNotifyMe.value as Success).data.successMessage, "Sukses un-register notify me")
        Assert.assertEquals((viewModel.toggleTeaserNotifyMe.value as Success).data.isSuccess, true)
        Assert.assertEquals((viewModel.toggleTeaserNotifyMe.value as Success).data.notifyMeAction, "UNREGISTER")

        //make sure the notify me changed
        val upcomingData = viewModel.p2Data.value?.upcomingCampaigns?.get(
                requestParamsSlotResult.getLong("product_id_64", 0L).toString()
        )

        Assert.assertNotNull(upcomingData)
        Assert.assertEquals(upcomingData?.notifyMe, false)
    }

    @Test
    fun `on error toggle notify me`() = runBlockingTest {
        //use product id 518076286 as non active notify me (register action)
        `on success get product info login`()

        val requestParamsSlot = slot<RequestParams>()
        val isNotifyMe = true

        coEvery {
            toggleNotifyMeUseCase.executeOnBackground(any())
        } throws MessageErrorException("Gagal gan")

        viewModel.toggleTeaserNotifyMe(
                isNotifyMeActive = isNotifyMe,
                campaignId = 1234L,
                productId = 518076286L)

        coVerify { toggleNotifyMeUseCase.executeOnBackground(capture(requestParamsSlot)) }

        val requestParamsSlotResult = requestParamsSlot.captured
        Assert.assertEquals(requestParamsSlotResult.getLong("campaignId", 0L), 1234L)
        Assert.assertEquals(requestParamsSlotResult.getLong("product_id_64", 0L), 518076286L)
        Assert.assertEquals(requestParamsSlotResult.getString("action", ""), "UNREGISTER")
        Assert.assertEquals(requestParamsSlotResult.getString("source", ""), "pdp")

        Assert.assertTrue(viewModel.toggleTeaserNotifyMe.value is Fail)
        Assert.assertEquals((viewModel.toggleTeaserNotifyMe.value as Fail).throwable.message, "Gagal gan")

        //make sure the notify me NOT changed
        val upcomingData = viewModel.p2Data.value?.upcomingCampaigns?.get(
                requestParamsSlotResult.getLong("product_id_64", 0L).toString()
        )

        Assert.assertNotNull(upcomingData)
        Assert.assertEquals(upcomingData?.notifyMe, true)
    }
}