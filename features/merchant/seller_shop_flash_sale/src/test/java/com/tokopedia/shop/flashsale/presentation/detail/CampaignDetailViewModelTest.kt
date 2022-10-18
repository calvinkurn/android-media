package com.tokopedia.shop.flashsale.presentation.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
import com.tokopedia.shop.flashsale.domain.entity.CampaignDetailMeta
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC.TncRequest
import com.tokopedia.shop.flashsale.domain.entity.aggregate.ShareComponent
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.GenerateCampaignBannerUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.GetCampaignDetailMetaUseCase
import com.tokopedia.shop.flashsale.domain.usecase.aggregate.GetShareComponentMetadataUseCase
import com.tokopedia.shop.flashsale.util.CampaignDataGenerator
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import java.util.concurrent.TimeUnit

class CampaignDetailViewModelTest {

    @RelaxedMockK
    lateinit var getCampaignDetailMetaUseCase: GetCampaignDetailMetaUseCase

    @RelaxedMockK
    lateinit var getShareComponentMetadataUseCase: GetShareComponentMetadataUseCase

    @RelaxedMockK
    lateinit var generateCampaignBannerUseCase: GenerateCampaignBannerUseCase

    @RelaxedMockK
    lateinit var tracker: ShopFlashSaleTracker

    @RelaxedMockK
    lateinit var campaignObserver: Observer<Result<CampaignDetailMeta>>

    @RelaxedMockK
    lateinit var tncClickEventObserver: Observer<TncRequest>

    @RelaxedMockK
    lateinit var editCampaignActionResultObserver: Observer<EditCampaignActionResult>

    @RelaxedMockK
    lateinit var cancelCampaignActionResultObserver: Observer<CancelCampaignActionResult>

    @RelaxedMockK
    lateinit var moreMenuEventObserver: Observer<CampaignUiModel>

    @RelaxedMockK
    lateinit var shareCampaignActionEventObserver: Observer<Result<ShareComponent>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: CampaignDetailViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = CampaignDetailViewModel(
            getCampaignDetailMetaUseCase = getCampaignDetailMetaUseCase,
            tracker = tracker,
            getShareComponentMetadataUseCase = getShareComponentMetadataUseCase,
            generateCampaignBannerUseCase = generateCampaignBannerUseCase,
            dispatchers = CoroutineTestDispatchersProvider
        )

        with(viewModel) {
            campaign.observeForever(campaignObserver)
            tncClickEvent.observeForever(tncClickEventObserver)
            editCampaignActionResult.observeForever(editCampaignActionResultObserver)
            cancelCampaignActionResult.observeForever(cancelCampaignActionResultObserver)
            moreMenuEvent.observeForever(moreMenuEventObserver)
            shareCampaignActionEvent.observeForever(shareCampaignActionEventObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            campaign.removeObserver(campaignObserver)
            tncClickEvent.removeObserver(tncClickEventObserver)
            editCampaignActionResult.removeObserver(editCampaignActionResultObserver)
            cancelCampaignActionResult.removeObserver(cancelCampaignActionResultObserver)
            moreMenuEvent.removeObserver(moreMenuEventObserver)
            shareCampaignActionEvent.removeObserver(shareCampaignActionEventObserver)
        }
    }

    @Test
    fun `When fetchCampaignDetail success, observer will receive the data`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val result = CampaignDataGenerator.generateCampaignDetailMetaData()
                val expected = Success(result)

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } returns result

                getCampaignDetail(campaignId)

                val actual = campaign.getOrAwaitValue()
                assertEquals(expected, actual)
                assertEquals(this.campaignId, campaignId)
            }
        }
    }

    @Test
    fun `When fetchCampaignDetail failed, observer will receive the error`() {
        runBlocking {
            with(viewModel) {
                val dummyThrowable = MessageErrorException("Error Occurred")
                val campaignId: Long = 1001
                val expected = Fail(dummyThrowable)

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } throws dummyThrowable

                getCampaignDetail(campaignId)

                val actual = campaign.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onShareButtonClicked success, tracker will be sent and getShareComponent success`() {
        runBlocking {
            with(viewModel) {
                val campaign = CampaignDataGenerator.generateCampaignUiModel()
                val campaignId: Long = 1001
                val campaignResult = CampaignDataGenerator.generateCampaignDetailMetaData()
                val metadataResult = CampaignDataGenerator.generateShareComponentMetaData()
                val imageResult = "https://images.tokopedia.net/img/cache/500-square/hDjmkQ/2022/9/26/894a8962-6c1c-411b-a8c9-5e50230c8529.jpg.webp?ect=4g"
                val expected = Success(ShareComponent(thumbnailImageUrl = imageResult, metaData = metadataResult))

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } returns campaignResult

                coEvery {
                    generateCampaignBannerUseCase.execute(campaignId)
                } returns imageResult

                coEvery {
                    getShareComponentMetadataUseCase.execute(campaignId)
                } returns metadataResult

                getCampaignDetail(campaignId)

                onShareButtonClicked()

                coVerify {
                    tracker.sendClickButtonShareCampaign(
                        campaignId = campaign.campaignId,
                        campaignName = campaign.campaignName,
                        campaignStatus = campaign.status,
                    )
                }

                val actual = shareCampaignActionEvent.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onShareButtonClicked success, tracker will be sent and getShareComponent failed`() {
        runBlocking {
            with(viewModel) {
                val campaign = CampaignDataGenerator.generateCampaignUiModel()
                val campaignResult = CampaignDataGenerator.generateCampaignDetailMetaData()
                val dummyThrowable = MessageErrorException("Error Occurred")
                val campaignId: Long = 1001
                val expected = Fail(dummyThrowable)

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } returns campaignResult

                coEvery {
                    generateCampaignBannerUseCase.execute(campaignId)
                } throws dummyThrowable

                coEvery {
                    getShareComponentMetadataUseCase.execute(campaignId)
                } throws dummyThrowable

                getCampaignDetail(campaignId)

                onShareButtonClicked()

                coVerify {
                    tracker.sendClickButtonShareCampaign(
                        campaignId = campaign.campaignId,
                        campaignName = campaign.campaignName,
                        campaignStatus = campaign.status,
                    )
                }

                val actual = shareCampaignActionEvent.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When onShareButtonClicked, but the campaign detail data is not loaded`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val dummyThrowable = MessageErrorException("Error Occurred")
                val expected = Fail(dummyThrowable)

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } throws dummyThrowable

                getCampaignDetail(campaignId)

                onShareButtonClicked()

                val actual = campaign.getOrAwaitValue()
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When user pressed reload page, reFetchCampaignDetail will succeed`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val result = CampaignDataGenerator.generateCampaignDetailMetaData()
                val expected = Success(result)

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } returns result

                getCampaignDetail(campaignId)

                reFetchCampaignDetail()

                val actual = campaign.getOrAwaitValue()
                assertEquals(expected, actual)
                assertEquals(this.campaignId, campaignId)
            }
        }
    }

    @Test
    fun `When user initially reFetchCampaignDetail`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = -1L

                reFetchCampaignDetail()

                assertEquals(this.campaignId, campaignId)
            }
        }
    }

    @Test
    fun `When tnc button is clicked, tncClickEvent will be updated`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaignData = CampaignDataGenerator.generateCampaignDetailMetaData()
                val expected = CampaignDataGenerator.generateMerchantCampaignTNCRequest()

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } returns campaignData

                getCampaignDetail(campaignId)

                onTNCButtonClicked()

                val actual = tncClickEvent.getOrAwaitValue()
                assertEquals(expected, actual)
                assertEquals(this.campaignId, campaignId)
            }
        }
    }

    @Test
    fun `When getCampaignDetail is failed, but onTNCButtonClicked invoked`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val dummyThrowable = MessageErrorException("Error Occurred")
                val expectedResult = Fail(dummyThrowable)

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } throws dummyThrowable

                getCampaignDetail(campaignId)

                onTNCButtonClicked()

                val actualResult = campaign.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expectedResult, actualResult)
                assertEquals(this.campaignId, campaignId)
            }
        }
    }

    @Test
    fun `When edit button is clicked, thematicParticipation = true, editCampaignActionResult will be updated`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaignData = CampaignDataGenerator.generateCampaignDetailMetaData()
                val expected = EditCampaignActionResult.RegisteredEventCampaign

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } returns campaignData

                getCampaignDetail(campaignId)

                onEditCampaignClicked()

                coVerify {
                    tracker.sendClickTextEditCampaign(
                        campaignData.campaign.campaignId,
                        campaignData.campaign.campaignName,
                        campaignData.campaign.status
                    )
                }

                val actual = editCampaignActionResult.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expected, actual)
                assert(campaignData.campaign.thematicParticipation)
            }
        }
    }

    @Test
    fun `When edit button is clicked, thematicParticipation = false, editCampaignActionResult will be updated`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaignData = CampaignDataGenerator.generateCampaignDetailMetaData(thematicParticipation = false)
                val expected = EditCampaignActionResult.Allowed(campaignId)

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } returns campaignData

                getCampaignDetail(campaignId)

                onEditCampaignClicked()

                coVerify {
                    tracker.sendClickTextEditCampaign(
                        campaignData.campaign.campaignId,
                        campaignData.campaign.campaignName,
                        campaignData.campaign.status
                    )
                }

                val actual = editCampaignActionResult.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expected, actual)
                assert(!campaignData.campaign.thematicParticipation)
            }
        }
    }

    @Test
    fun `When getCampaignDetail is failed, but onEditCampaignClicked invoked`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val dummyThrowable = MessageErrorException("Error Occurred")
                val expectedResult = Fail(dummyThrowable)

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } throws dummyThrowable

                getCampaignDetail(campaignId)

                onEditCampaignClicked()

                val actualResult = campaign.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expectedResult, actualResult)
                assertEquals(this.campaignId, campaignId)
            }
        }
    }

    @Test
    fun `When MoreMenu is clicked, moreMenuEvent will be updated`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaignData = CampaignDataGenerator.generateCampaignDetailMetaData()
                val expected = campaignData.campaign

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } returns campaignData

                getCampaignDetail(campaignId)

                onMoreMenuClicked()

                val actual = moreMenuEvent.getOrAwaitValue()
                assertEquals(expected, actual)
                assertEquals(this.campaignId, campaignId)
            }
        }
    }

    @Test
    fun `When getCampaignDetail is failed, but onMoreMenuClicked invoked`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val dummyThrowable = MessageErrorException("Error Occurred")
                val expectedResult = Fail(dummyThrowable)

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } throws dummyThrowable

                getCampaignDetail(campaignId)

                onMoreMenuClicked()

                val actualResult = campaign.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expectedResult, actualResult)
                assertEquals(this.campaignId, campaignId)
            }
        }
    }

    @Test
    fun `When CampaignCancel Menu is clicked, isCancellable = false, cancelCampaignActionResult will be updated`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaignData = CampaignDataGenerator.generateCampaignDetailMetaData(isCancellable = false)
                val expected = CancelCampaignActionResult.RegisteredEventCampaign(campaignData.campaign)

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } returns campaignData

                getCampaignDetail(campaignId)

                onCampaignCancelMenuClicked()

                val actual = cancelCampaignActionResult.getOrAwaitValue()
                assertEquals(expected, actual)
                assertEquals(this.campaignId, campaignId)
            }
        }
    }

    @Test
    fun `When CampaignCancel Menu is clicked, isCancellable = true, cancelCampaignActionResult will be updated`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaignData = CampaignDataGenerator.generateCampaignDetailMetaData()
                val expected = CancelCampaignActionResult.ActionAllowed(campaignData.campaign)

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } returns campaignData

                getCampaignDetail(campaignId)

                onCampaignCancelMenuClicked()

                coVerify { tracker.sendClickCancelCampaignEvent(campaignData.campaign) }

                val actual = cancelCampaignActionResult.getOrAwaitValue()
                assertEquals(expected, actual)
                assertEquals(this.campaignId, campaignId)
            }
        }
    }

    @Test
    fun `When CampaignCancel Menu is clicked, isCancellable and status is active, cancelCampaignActionResult will be updated`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaignData = CampaignDataGenerator.generateCampaignDetailMetaData(status = CampaignStatus.ONGOING)
                val expected = CancelCampaignActionResult.ActionAllowed(campaignData.campaign)

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } returns campaignData

                getCampaignDetail(campaignId)

                onCampaignCancelMenuClicked()

                coVerify { tracker.sendClickStopCampaignEvent(campaignData.campaign) }

                val actual = cancelCampaignActionResult.getOrAwaitValue()
                assertEquals(expected, actual)
                assertEquals(this.campaignId, campaignId)
            }
        }
    }

    @Test
    fun `When CampaignCancel Menu is clicked, isCancellable and status is Finished, cancelCampaignActionResult will be updated`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val campaignData = CampaignDataGenerator.generateCampaignDetailMetaData(status = CampaignStatus.FINISHED)

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } returns campaignData

                getCampaignDetail(campaignId)

                onCampaignCancelMenuClicked()

                assertEquals(this.campaignId, campaignId)
            }
        }
    }

    @Test
    fun `When getCampaignDetail is failed, but onCampaignCancelMenuClicked invoked`() {
        runBlocking {
            with(viewModel) {
                val campaignId: Long = 1001
                val dummyThrowable = MessageErrorException("Error Occurred")
                val expectedResult = Fail(dummyThrowable)

                coEvery {
                    getCampaignDetailMetaUseCase.execute(campaignId)
                } throws dummyThrowable

                getCampaignDetail(campaignId)

                onCampaignCancelMenuClicked()

                val actualResult = campaign.getOrAwaitValue(time = 0L, timeUnit = TimeUnit.SECONDS)
                assertEquals(expectedResult, actualResult)
                assertEquals(this.campaignId, campaignId)
            }
        }
    }
}
