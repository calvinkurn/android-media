package com.tokopedia.play.repo

import com.tokopedia.atc_common.domain.model.response.*
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.CheckUpcomingCampaign
import com.tokopedia.play.data.PostUpcomingCampaign
import com.tokopedia.play.data.UpcomingCampaignResponse
import com.tokopedia.play.data.repository.PlayViewerTagItemRepositoryImpl
import com.tokopedia.play.domain.CheckUpcomingCampaignReminderUseCase
import com.tokopedia.play.domain.GetProductTagItemSectionUseCase
import com.tokopedia.play.domain.PostUpcomingCampaignReminderUseCase
import com.tokopedia.play.domain.repository.PlayViewerTagItemRepository
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.util.*
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 11/03/22
 */
@ExperimentalCoroutinesApi
class PlayViewerTagItemsRepositoryTest {
    lateinit var tagItemRepo: PlayViewerTagItemRepository

    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val testDispatcher = coroutineTestRule.dispatchers

    private val mockGetProductTagUseCase: GetProductTagItemSectionUseCase = mockk(relaxed = true)
    private val mockAddToCartUseCase: AddToCartUseCase = mockk(relaxed = true)
    private val mockCheckUpcomingCampaignReminderUseCase: CheckUpcomingCampaignReminderUseCase = mockk(relaxed = true)
    private val mockPostUpcomingCampaignReminderUseCase: PostUpcomingCampaignReminderUseCase = mockk(relaxed = true)
    private val mockAtcOcc: AddToCartOccMultiUseCase = mockk(relaxed = true)

    private val modelBuilder = ModelBuilder()

    private val campaignId: String = 105L.toString()

    private val classBuilder = ClassBuilder()
    private val mockMapper = classBuilder.getPlayUiModelMapper()

    @Before
    fun setUp() {
        tagItemRepo = PlayViewerTagItemRepositoryImpl(
            getProductTagItemsUseCase = mockGetProductTagUseCase,
            addToCartUseCase = mockAddToCartUseCase,
            checkUpcomingCampaignReminderUseCase = mockCheckUpcomingCampaignReminderUseCase,
            postUpcomingCampaignReminderUseCase = mockPostUpcomingCampaignReminderUseCase,
            atcOccUseCase = mockAtcOcc,
            mapper = mockMapper,
            userSession = mockUserSession,
            dispatchers = testDispatcher
        )
    }

    @Before
    fun tearDown() {
        testDispatcher.coroutineDispatcher.cancelChildren()
        testDispatcher.coroutineDispatcher.cancel()
    }

    @Test
    fun `when get section success return success with complete config`() {
        testDispatcher.coroutineDispatcher.runBlockingTest {
            val mockResponse = modelBuilder.generateResponseSectionGql(gradient = null)

            coEvery { mockGetProductTagUseCase.executeOnBackground() } returns mockResponse

            val response = tagItemRepo.getTagItem(
                "12669",
                "",
                "PLAY",
                PlayChannelType.VOD
            )

            coVerify { mockGetProductTagUseCase.executeOnBackground() }
            response.resultState.assertEqualTo(ResultState.Success)
        }
    }

    @Test
    fun `when get section success return success with complete config -- live product numeration shown`() {
        testDispatcher.coroutineDispatcher.runBlockingTest {
            val mockResponse = modelBuilder.generateGqlProductNumeration()

            coEvery { mockGetProductTagUseCase.executeOnBackground() } returns mockResponse

            val response = tagItemRepo.getTagItem(
                "12669",
                "",
                "PLAY",
                PlayChannelType.Live
            )

            coVerify { mockGetProductTagUseCase.executeOnBackground() }
            response.resultState.assertEqualTo(ResultState.Success)
            response.product.productSectionList.filterIsInstance<ProductSectionUiModel.Section>().forEach {
                it.productList.forEach {
                        item ->
                    item.isNumerationShown.assertTrue()
                }
            }
        }
    }

    @Test
    fun `when get section success return success with bg config null`() {
        testDispatcher.coroutineDispatcher.runBlockingTest {
            val mockResponse = modelBuilder.generateResponseSectionGql(gradient = listOf("3fffff", "#45a5aa"))

            coEvery { mockGetProductTagUseCase.executeOnBackground() } returns mockResponse

            val response = tagItemRepo.getTagItem(
                "12669",
                "0",
                "PLAY",
                PlayChannelType.VOD
            )

            coVerify { mockGetProductTagUseCase.executeOnBackground() }
            response.resultState.assertEqualTo(ResultState.Success)
        }
    }

    @Test
    fun `when ATC success return success response`() {
        testDispatcher.coroutineDispatcher.runBlockingTest {
            val mockCartId = "1"
            val mockResponse = AddToCartDataModel(
                errorMessage = arrayListOf(),
                data = DataModel(
                    success = 1,
                    cartId = mockCartId
                )
            )
            coEvery { mockAddToCartUseCase.executeOnBackground() } returns mockResponse

            val response = tagItemRepo.addProductToCart(
                "1",
                "Product Test",
                "1",
                1,
                12000.0
            )

            coVerify { mockAddToCartUseCase.executeOnBackground() }
            response.assertEqualTo(mockCartId)
        }
    }

    @Test
    fun `when ATC error return failed response = exception`() {
        testDispatcher.coroutineDispatcher.runBlockingTest {
            val mockCartId = "1"
            val mockErrorResponse = AddToCartDataModel(
                errorMessage = arrayListOf("Error Message"),
                data = DataModel(
                    success = 0,
                    cartId = mockCartId
                )
            )
            coEvery { mockAddToCartUseCase.executeOnBackground() } returns mockErrorResponse

            try {
                val response = tagItemRepo.addProductToCart(
                    "1",
                    "Product Test",
                    "1",
                    1,
                    12000.0
                )
                coVerify { mockAddToCartUseCase.executeOnBackground() }
            } catch (e: Exception) {
                (e is MessageErrorException).assertTrue()
            }
        }
    }

    @Test
    fun `when upco campaign is exist check if user has reminded, if user has reminded return true`() {
        testDispatcher.coroutineDispatcher.runBlockingTest {
            val mockResponse = CheckUpcomingCampaign(
                response = UpcomingCampaignResponse(isAvailable = true)
            )
            coEvery { mockCheckUpcomingCampaignReminderUseCase.executeOnBackground() } returns mockResponse

            val result = tagItemRepo.checkUpcomingCampaign(campaignId)

            coVerify { mockCheckUpcomingCampaignReminderUseCase.executeOnBackground() }

            result.assertEqualTo(mockResponse.response.isAvailable)
        }
    }

    @Test
    fun `when upco campaign is exist check if user has reminded, if user has not reminded return false`() {
        testDispatcher.coroutineDispatcher.runBlockingTest {
            val mockResponse = CheckUpcomingCampaign(
                response = UpcomingCampaignResponse(isAvailable = false)
            )
            coEvery { mockCheckUpcomingCampaignReminderUseCase.executeOnBackground() } returns mockResponse

            val result = tagItemRepo.checkUpcomingCampaign(campaignId)

            coVerify { mockCheckUpcomingCampaignReminderUseCase.executeOnBackground() }

            result.assertEqualTo(mockResponse.response.isAvailable)
        }
    }

    @Test
    fun `when upco campaign is exist when user click reminder return true if success`() {
        testDispatcher.coroutineDispatcher.runBlockingTest {
            val successResponse = UpcomingCampaignResponse(isAvailable = true)
            val mockResponse = PostUpcomingCampaign(
                response = successResponse
            )
            coEvery { mockPostUpcomingCampaignReminderUseCase.executeOnBackground() } returns mockResponse
            coEvery { mockCheckUpcomingCampaignReminderUseCase.executeOnBackground() } returns CheckUpcomingCampaign(
                response = successResponse
            )

            val result = tagItemRepo.subscribeUpcomingCampaign(campaignId, true)

            coVerify { mockPostUpcomingCampaignReminderUseCase.executeOnBackground() }
            coVerify { mockCheckUpcomingCampaignReminderUseCase.executeOnBackground() }

            result.isReminded.assertEqualTo(mockResponse.response.isAvailable)
        }
    }

    @Test
    fun `given voucher from gql when there is public voucher show ticker`() {
        testDispatcher.coroutineDispatcher.runBlockingTest {
            val mockResponse = modelBuilder.buildProductTagging()
            coEvery { mockGetProductTagUseCase.executeOnBackground() } returns mockResponse

            val partnerName = "PLAY"

            val response = tagItemRepo.getTagItem(
                "12669",
                "0",
                partnerName,
                PlayChannelType.VOD
            )

            coVerify { mockGetProductTagUseCase.executeOnBackground() }
            response.resultState.assertEqualTo(ResultState.Success)
            response.voucher.voucherList.isNotEmpty().assertTrue()
            response.voucher.voucherList.first().assertInstanceOf<PlayVoucherUiModel.InfoHeader>()
            response.voucher.voucherList.first().assertType<PlayVoucherUiModel.InfoHeader> {
                it.shopName.assertEqualTo(partnerName)
            }
        }
    }

    @Test
    fun `given voucher from gql when there is no public voucher hide ticker`() {
        testDispatcher.coroutineDispatcher.runBlockingTest {
            val mockResponse = modelBuilder.buildNonPublic()
            coEvery { mockGetProductTagUseCase.executeOnBackground() } returns mockResponse

            val partnerName = "PLAY"

            val response = tagItemRepo.getTagItem(
                "12669",
                "0",
                partnerName,
                PlayChannelType.VOD
            )

            coVerify { mockGetProductTagUseCase.executeOnBackground() }
            response.resultState.assertEqualTo(ResultState.Success)
            response.voucher.voucherList.isNotEmpty().assertTrue()
            response.voucher.voucherList.first().assertInstanceOf<PlayVoucherUiModel.Merchant>()
            response.voucher.voucherList.filterIsInstance<PlayVoucherUiModel.InfoHeader>().assertEmpty()
            response.voucher.voucherList.size.assertEqualTo(mockResponse.playGetTagsItem.voucherList.size)
        }
    }

    @Test
    fun `when ATC to OCC success return success response`() {
        testDispatcher.coroutineDispatcher.runBlockingTest {
            val mockResponse = AddToCartOccMultiDataModel(
                errorMessage = arrayListOf(),
                status = AddToCartOccMultiDataModel.STATUS_OK // if OK -> success
            )
            coEvery { mockAtcOcc.executeOnBackground() } returns mockResponse

            try {
                val response = tagItemRepo.addProductToCartOcc(
                    "1",
                    "Product Test",
                    "1",
                    1,
                    12000.0
                )
                response.assertEqualTo(mockResponse)
            } catch (e: Exception) { } finally {
                coVerify { mockAtcOcc.executeOnBackground() }
            }
        }
    }

    @Test
    fun `when ATC occ error return failed response = exception`() {
        testDispatcher.coroutineDispatcher.runBlockingTest {
            val mockResponse = AddToCartOccMultiDataModel(
                status = AddToCartOccMultiDataModel.STATUS_ERROR // if OK -> success
            )
            coEvery { mockAtcOcc.executeOnBackground() } returns mockResponse

            try {
                val response = tagItemRepo.addProductToCartOcc(
                    "1",
                    "Product Test",
                    "1",
                    1,
                    12000.0
                )
                coVerify { mockAtcOcc.executeOnBackground() }
            } catch (e: Exception) {
                (e is MessageErrorException).assertTrue()
            }
        }
    }
}
