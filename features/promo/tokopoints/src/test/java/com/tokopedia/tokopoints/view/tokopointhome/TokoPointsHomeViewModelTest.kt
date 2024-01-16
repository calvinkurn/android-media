package com.tokopedia.tokopoints.view.tokopointhome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopoints.notification.PopupNotifUsecase
import com.tokopedia.tokopoints.notification.model.popupnotif.PopupNotif
import com.tokopedia.tokopoints.notification.model.popupnotif.PopupNotifResponse
import com.tokopedia.tokopoints.notification.model.popupnotif.Tokopoints
import com.tokopedia.tokopoints.view.model.LuckyEggEntity
import com.tokopedia.tokopoints.view.model.ResultStatusEntity
import com.tokopedia.tokopoints.view.model.TokenDetailOuter
import com.tokopedia.tokopoints.view.model.homeresponse.RecommendationWrapper
import com.tokopedia.tokopoints.view.model.homeresponse.RewardsRecommendation
import com.tokopedia.tokopoints.view.model.homeresponse.TokopointSuccess
import com.tokopedia.tokopoints.view.model.homeresponse.TopSectionResponse
import com.tokopedia.tokopoints.view.model.rewardintro.IntroResponse
import com.tokopedia.tokopoints.view.model.rewardintro.TokopediaRewardIntroPage
import com.tokopedia.tokopoints.view.model.rewardtopsection.RewardResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.TokopediaRewardTopSection
import com.tokopedia.tokopoints.view.model.rewrdsStatusMatching.RewardTickerListResponse
import com.tokopedia.tokopoints.view.model.rewrdsStatusMatching.RewardsTickerList
import com.tokopedia.tokopoints.view.model.rewrdsStatusMatching.TickerListItem
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.model.section.TokopointsSection
import com.tokopedia.tokopoints.view.model.section.TokopointsSectionOuter
import com.tokopedia.tokopoints.view.model.usersaving.TokopointsUserSaving
import com.tokopedia.tokopoints.view.model.usersaving.UserSavingInfo
import com.tokopedia.tokopoints.view.model.usersaving.UserSavingResponse
import com.tokopedia.tokopoints.view.recommwidget.RewardsRecommUsecase
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import junit.framework.Assert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass

class TokoPointsHomeViewModelTest {

    lateinit var viewModel: TokoPointsHomeViewModel
    lateinit var repository: TokopointsHomeUsecase
    lateinit var recomUsecase: RewardsRecommUsecase
    lateinit var popupNotifUsecase: PopupNotifUsecase

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        repository = mockk(relaxed = true)
        recomUsecase = mockk(relaxed = true)
        popupNotifUsecase = mockk(relaxed = true)
        viewModel = TokoPointsHomeViewModel(repository, recomUsecase , popupNotifUsecase)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getTokoPointDetail for token detail success and error in detail`() {
        val tokopointObserver = mockk<Observer<Resources<TokopointSuccess>>>() {
            every { onChanged(any()) } just Runs
        }

        val tokenData = LuckyEggEntity(resultStatus = ResultStatusEntity(code = 200))
        val tokopediaRewardTopSectionResponse = TokopediaRewardTopSection(
            title = "Reward", isShowSavingPage = true
        )
        val tokopointsSectionResponse = TokopointsSection(
            sectionContent = mutableListOf(SectionContent(sectionTitle = "Section"))
        )
        val userSavingResponse = UserSavingResponse(
            tokopointsUserSaving = TokopointsUserSaving(
                userSavingInfo = UserSavingInfo(title = "User Saving")
            )
        )
        val rewardTickerListResponse = RewardTickerListResponse(
            rewardsTickerList = RewardsTickerList(
                tickerList = listOf(TickerListItem(id = 1))
            )
        )
        val popupNotifResponse = PopupNotifResponse(
            tokopoints = Tokopoints(
                popupNotif = PopupNotif(title = "Popup Notif")
            )
        )

        val recomendationItem = RecommendationItem(productId = 151)
        val recommendationWrapperList = listOf(
            RecommendationWrapper(
                recomendationItem = recomendationItem,
                recomData = createProductModel(recomendationItem)
            )
        )
        val recommendationWidgetResponse = RecommendationWidget(
            title = "Recom Title",
            recommendationItemList = listOf(recomendationItem),
            seeMoreAppLink = "tokopedia://tokopoints"
        )
        val requestParams = RequestParams()

        coEvery { repository.getTokoPointDetailData() } returns mockk {
            every { getData<RewardResponse>(RewardResponse::class.java) } returns mockk {
                every { tokopediaRewardTopSection } returns tokopediaRewardTopSectionResponse
            }
            every { getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java) } returns mockk {
                every { sectionContent } returns tokopointsSectionResponse
            }
            every { getData<TokenDetailOuter>(TokenDetailOuter::class.java) } returns mockk {
                every { tokenDetail } returns tokenData
            }
        }

        coEvery { repository.getUserSavingData() } returns mockk {
            every { getData<UserSavingResponse>(UserSavingResponse::class.java) } returns userSavingResponse
        }

        coEvery { repository.getUserStatusMatchingData() } returns mockk {
            every { getData<RewardTickerListResponse>(RewardTickerListResponse::class.java) } returns rewardTickerListResponse
        }

        coEvery { popupNotifUsecase.getPopupNotif(any()) } returns mockk {
            every { getData<PopupNotifResponse>(PopupNotifResponse::class.java) } returns popupNotifResponse
        }

        coEvery { recomUsecase.getRequestParams(any(), any()) } returns requestParams
        coEvery { recomUsecase.getData(requestParams) } returns listOf(recommendationWidgetResponse)
        coEvery { recomUsecase.mapper.recommWidgetToListOfVisitables(recommendationWidgetResponse) } returns recommendationWrapperList

        viewModel.tokopointDetailLiveData.observeForever(tokopointObserver)
        viewModel.getTokoPointDetail()

        val recomData = RewardsRecommendation(recommendationWrapperList, "Recom Title", "tokopedia://tokopoints")

        val expectedLiveDataValue = Success(
            TokopointSuccess(
                TopSectionResponse(
                    tokopediaRewardTopSectionResponse,
                    userSavingResponse.tokopointsUserSaving,
                    rewardTickerListResponse,
                    popupNotifResponse.tokopoints
                ),
                tokopointsSectionResponse.sectionContent,
                recomData
            )
        )

//        viewModel.tokopointDetailLiveData
//            .verifyValueEquals()
        verify(ordering = Ordering.ORDERED) {
            tokopointObserver.onChanged(expectedLiveDataValue)
        }
    }

    @Test
    fun `getTokoPointDetail null pointer error`(){
        val tokenData = mockk<LuckyEggEntity> {
            every { resultStatus.code } returns CommonConstant.CouponRedemptionCode.SUCCESS
        }
        coEvery { repository.getTokoPointDetailData() } returns mockk {
            every { getData<RewardResponse>(RewardResponse::class.java) } returns mockk {
                every { tokopediaRewardTopSection } returns null
            }
            every { getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java) } returns mockk {
                every { sectionContent } returns TokopointsSection()
            }
            every { getData<TokenDetailOuter>(TokenDetailOuter::class.java) } returns mockk {
                every { tokenDetail } returns tokenData
            }
        }
        viewModel.getTokoPointDetail()
        Assert.assertEquals((viewModel.tokopointDetailLiveData.value as ErrorMessage).data,"error in data")
    }

    @Test
    fun `getTokoPointDetail for error`() {
        val tokopointObserver = mockk<Observer<Resources<TokopointSuccess>>>() {
            every { onChanged(any()) } just Runs
        }
        viewModel.tokopointDetailLiveData.observeForever(tokopointObserver)
        viewModel.getTokoPointDetail()
        verify(ordering = Ordering.ORDERED) {
            tokopointObserver.onChanged(ofType(Loading::class as KClass<Loading<TokopointSuccess>>))
            tokopointObserver.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<TokopointSuccess>>))
        }
    }

    @Test
    fun tokopointIntro() {
        val tokopointIntroObserver = mockk<Observer<Resources<IntroResponse>>>()
        val data = mockk<TokopediaRewardIntroPage>()
        every { tokopointIntroObserver.onChanged(any()) } just runs
        coEvery { repository.getRewardIntroData() } returns mockk {
            every { getData<IntroResponse>(IntroResponse::class.java) } returns mockk {
                every { tokopediaRewardIntroPage } returns data
            }
        }
        viewModel.rewardIntroData.observeForever(tokopointIntroObserver)
        viewModel.getRewardIntroData()
        verify(exactly = 1) { tokopointIntroObserver.onChanged(any()) }
    }

    @Test
    fun `given getRewardIntroData throws exception when getRewardIntroData should not crash`() {
        coEvery { repository.getRewardIntroData() } throws NullPointerException()

        viewModel.getRewardIntroData()

        viewModel.rewardIntroData
            .verifyValueEquals(null)
    }

    @Test
    fun `given isShowIntroActivity true when getTokoPointDetail should update rewardIntroData live data`() {
        val isShowIntroActivity = true

        val sectionOuterResponse = TokopointsSectionOuter(
            sectionContent = TokopointsSection()
        )
        val rewardTopSectionResponse = RewardResponse(
            tokopediaRewardTopSection = TokopediaRewardTopSection(isShowIntroActivity = isShowIntroActivity)
        )
        val rewardIntroResponse = IntroResponse(
            tokopediaRewardIntroPage = TokopediaRewardIntroPage(title = "Reward")
        )

        coEvery { repository.getTokoPointDetailData() } returns mockk {
            every { getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java) } returns sectionOuterResponse
            every { getData<RewardResponse>(RewardResponse::class.java) } returns rewardTopSectionResponse
        }

        coEvery { repository.getRewardIntroData() } returns mockk {
            every { getData<IntroResponse>(IntroResponse::class.java) } returns rewardIntroResponse
        }

        viewModel.getTokoPointDetail()

        viewModel.rewardIntroData
            .verifyValueEquals(Success(rewardIntroResponse))
    }

    @Test
    fun `given isShowSavingPage true when getTokoPointDetail should update deferredSavingData`() {
        runTest {
            val isShowSavingPage = true

            val sectionOuterResponse = TokopointsSectionOuter(
                sectionContent = TokopointsSection()
            )
            val rewardTopSectionResponse = RewardResponse(
                tokopediaRewardTopSection = TokopediaRewardTopSection(isShowSavingPage = isShowSavingPage)
            )
            val userSavingResponse = UserSavingResponse(
                tokopointsUserSaving = TokopointsUserSaving(userSavingInfo = UserSavingInfo(title = "Saving Info"))
            )

            coEvery { repository.getTokoPointDetailData() } returns mockk {
                every { getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java) } returns sectionOuterResponse
                every { getData<RewardResponse>(RewardResponse::class.java) } returns rewardTopSectionResponse
            }

            coEvery { repository.getUserSavingData() } returns mockk {
                every { getData<UserSavingResponse>(UserSavingResponse::class.java) } returns userSavingResponse
            }

            viewModel.getTokoPointDetail()

            val actualSavingData = viewModel.deferredSavingData?.await()

            assertEquals(userSavingResponse, actualSavingData)
        }
    }

    @Test
    fun `given getUserSavingData throws exception when getTokoPointDetail should update deferredSavingData with default response`() {
        runTest {
            val sectionOuterResponse = TokopointsSectionOuter(
                sectionContent = TokopointsSection()
            )
            val rewardTopSectionResponse = RewardResponse(
                tokopediaRewardTopSection = TokopediaRewardTopSection(isShowSavingPage = true)
            )

            coEvery { repository.getTokoPointDetailData() } returns mockk {
                every { getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java) } returns sectionOuterResponse
                every { getData<RewardResponse>(RewardResponse::class.java) } returns rewardTopSectionResponse
            }

            coEvery { repository.getUserSavingData() } returns mockk {
                every { getData<UserSavingResponse>(UserSavingResponse::class.java) } throws NullPointerException()
            }

            viewModel.getTokoPointDetail()

            val actualSavingData = viewModel.deferredSavingData?.await()

            assertEquals(UserSavingResponse(), actualSavingData)
        }
    }

    @Test
    fun `given get recommendation success when getTokoPointDetail should update defferedRecomData`() {
        runTest {
            val title = "Rekomendasi Belanja"
            val appLink = "tokopedia://tokopoints/home"

            val requestParams = RequestParams()
            val recommendationItem = RecommendationItem(productId = 1, name = "Buah Jeruk")
            val recommendationWidgetResponse = RecommendationWidget(
                title = title,
                recommendationItemList = listOf(recommendationItem),
                seeMoreAppLink = appLink
            )
            val recommendationWrapperList = listOf(
                RecommendationWrapper(
                    recomendationItem = recommendationItem,
                    recomData = createProductModel(recommendationItem)
                )
            )

            coEvery { repository.getTokoPointDetailData() } returns mockk {
                every { getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java) } returns TokopointsSectionOuter()
                every { getData<RewardResponse>(RewardResponse::class.java) } returns RewardResponse()
            }

            coEvery { recomUsecase.getRequestParams(any(), any()) } returns requestParams
            coEvery { recomUsecase.getData(requestParams) } returns listOf(recommendationWidgetResponse)
            coEvery { recomUsecase.mapper.recommWidgetToListOfVisitables(recommendationWidgetResponse) } returns recommendationWrapperList

            viewModel.getTokoPointDetail()

            val expectedRecomData = RewardsRecommendation(
                recommendationWrapper = recommendationWrapperList,
                title = title,
                appLink = appLink
            )
            val actualRecomData = viewModel.defferedRecomData?.await()

            assertEquals(expectedRecomData, actualRecomData)
        }
    }

    @Test
    fun `given reward ticker response when getTokoPointDetail should update defferedRewardTickerResponse`() {
        runTest {
            val rewardTickerResponse = RewardTickerListResponse(
                rewardsTickerList = RewardsTickerList(tickerList = listOf(TickerListItem(id = 1)))
            )

            coEvery { repository.getTokoPointDetailData() } returns mockk {
                every { getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java) } returns TokopointsSectionOuter()
                every { getData<RewardResponse>(RewardResponse::class.java) } returns RewardResponse()
            }

            coEvery { repository.getUserStatusMatchingData() } returns mockk {
                every { getData<RewardTickerListResponse>(RewardTickerListResponse::class.java) } returns rewardTickerResponse
            }

            viewModel.getTokoPointDetail()

            val actualRewardTickerResponse = viewModel.defferedRewardTickerResponse?.await()

            assertEquals(rewardTickerResponse, actualRewardTickerResponse)
        }
    }

    @Test
    fun `when getPopNotifData success should return pop up notif response`() {
        runTest {
            val popUpNotifResponse = PopupNotifResponse(tokopoints = Tokopoints(popupNotif = PopupNotif(buttonText = "Click Here")))

            coEvery { popupNotifUsecase.getPopupNotif(any()) } returns mockk {
                every { getData<PopupNotifResponse>(PopupNotifResponse::class.java) } returns popUpNotifResponse
            }

            val actualTokopointDetailEntity = viewModel.getPopNotifData().await()

            assertEquals(popUpNotifResponse, actualTokopointDetailEntity)
        }
    }

    @Test
    fun `given getPopUpNotif throws exception when getPopNotifData should catch exception and return default response`() {
        runTest {
            val defaultPopUpNotifResponse = PopupNotifResponse()

            coEvery { popupNotifUsecase.getPopupNotif(any()) } throws NullPointerException()

            val actualTokopointDetailEntity = viewModel.getPopNotifData().await()

            assertEquals(defaultPopUpNotifResponse, actualTokopointDetailEntity)
        }
    }

    private fun createProductModel(element: RecommendationItem): ProductCardModel {
        return ProductCardModel(
            productName = element.name,
            formattedPrice = element.price,
            productImageUrl = element.imageUrl,
            isTopAds = element.isTopAds,
            ratingCount = element.rating,
            hasThreeDots = false,
            shopRating = element.ratingAverage,
            isShopRatingYellow = true,
            shopLocation = element.location,
            shopBadgeList = element.badgesUrl.map {
                ProductCardModel.ShopBadge(imageUrl = it)
            },
            freeOngkir = ProductCardModel.FreeOngkir(
                isActive = element.isFreeOngkirActive,
                imageUrl = element.freeOngkirImageUrl
            )
        )
    }
}
