package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.inboxcommon.util.FileUtil
import com.tokopedia.notifcenter.data.entity.clearnotif.ClearNotifCounterResponse
import com.tokopedia.notifcenter.data.entity.filter.NotifcenterFilterResponse
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.mapper.NotifcenterDetailMapper
import com.tokopedia.notifcenter.data.model.RecommendationDataModel
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.data.uimodel.NotificationTopAdsBannerUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.domain.ClearNotifCounterUseCase
import com.tokopedia.notifcenter.domain.MarkNotificationAsReadUseCase
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase.Companion.FILTER_NONE
import com.tokopedia.notifcenter.domain.NotifcenterFilterV2UseCase
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationViewModel.Companion.getRecommendationVisitables
import com.tokopedia.notifcenter.util.coroutines.TestDispatcherProvider
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.data.mapper.RecommendationEntityMapper
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import kotlin.test.assertEquals
import com.tokopedia.recommendation_widget_common.data.mapper.RecommendationEntityMapper.Companion.mappingToRecommendationModel as mappingToRecommendationModel

class NotificationViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    private val notifcenterDetailUseCase: NotifcenterDetailUseCase = mockk(relaxed = true)
    private val notifcenterFilterUseCase: NotifcenterFilterV2UseCase = mockk(relaxed = true)
    private val clearNotifUseCase: ClearNotifCounterUseCase = mockk(relaxed = true)
    private val markAsReadUseCase: MarkNotificationAsReadUseCase = mockk(relaxed = true)
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase = mockk(relaxed = true)
    private val getRecommendationUseCase: GetRecommendationUseCase = mockk(relaxed = true)
    private val addWishListUseCase: AddWishListUseCase = mockk(relaxed = true)
    private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase = mockk(relaxed = true)
    private val removeWishListUseCase: RemoveWishListUseCase = mockk(relaxed = true)
    private val userSessionInterface: UserSessionInterface = mockk(relaxed = true)

    private val dispatcher = TestDispatcherProvider()

    private val notificationItemsObserver: Observer<Result<NotificationDetailResponseModel>> = mockk(relaxed = true)
    private val recommendationsObserver: Observer<RecommendationDataModel> = mockk(relaxed = true)
    private val topAdsBannerObserver: Observer<NotificationTopAdsBannerUiModel> = mockk(relaxed = true)
    private val filterListObserver: Observer<Resource<NotifcenterFilterResponse>> = mockk(relaxed = true)
    private val clearNotifObserver: Observer<Resource<ClearNotifCounterResponse>> = mockk(relaxed = true)

    private val viewModel = NotificationViewModel(
            notifcenterDetailUseCase,
            notifcenterFilterUseCase,
            clearNotifUseCase,
            markAsReadUseCase,
            topAdsImageViewUseCase,
            getRecommendationUseCase,
            addWishListUseCase,
            topAdsWishlishedUseCase,
            removeWishListUseCase,
            userSessionInterface,
            dispatcher
    )

    @Before fun setUp() {
        viewModel.notificationItems.observeForever(notificationItemsObserver)
        viewModel.recommendations.observeForever(recommendationsObserver)
        viewModel.topAdsBanner.observeForever(topAdsBannerObserver)
        viewModel.filterList.observeForever(filterListObserver)
        viewModel.clearNotif.observeForever(clearNotifObserver)
    }

    @Test fun `loadFirstPageNotification should return data properly`() {
        // given
        val expectedValue = NotifcenterDetailMapper().mapFirstPage(
                notifCenterDetailResponse,
                needSectionTitle = false,
                needLoadMoreButton = false
        )

        val role = RoleType.SELLER
        viewModel.reset() // filter id

        every {
            notifcenterDetailUseCase.getFirstPageNotification(
                    viewModel.filter,
                    role,
                    captureLambda(),
                    any()
            )
        } answers {
            val onSuccess = lambda<(NotificationDetailResponseModel) -> Unit>()
            onSuccess.invoke(expectedValue)
        }

        // when
        viewModel.loadFirstPageNotification(role)

        // then
        verify { notificationItemsObserver.onChanged(Success(expectedValue)) }
    }

    @Test fun `loadFirstPageNotification as buyer should return correct data`() {
        // given
        val topAdsImageView = arrayListOf(TopAdsImageViewModel())
        val expectedValue = NotifcenterDetailMapper().mapFirstPage(
                notifCenterDetailResponse,
                needSectionTitle = false,
                needLoadMoreButton = false
        )

        val role = RoleType.BUYER
        viewModel.reset() // filter id

        coEvery { topAdsImageViewUseCase.getImageData(any()) } returns topAdsImageView

        every {
            notifcenterDetailUseCase.getFirstPageNotification(
                    viewModel.filter,
                    role,
                    captureLambda(),
                    any()
            )
        } answers {
            val onSuccess = lambda<(NotificationDetailResponseModel) -> Unit>()
            onSuccess.invoke(expectedValue)
        }

        // when
        viewModel.loadFirstPageNotification(role)

        // then
        verifyOrder {
            notificationItemsObserver.onChanged(Success(expectedValue))
            topAdsBannerObserver.onChanged(NotificationTopAdsBannerUiModel(topAdsImageView.first()))
        }
    }

    @Test fun `loadFirstPageNotification should throw the Fail state`() {
        // given
        val expectedValue = Throwable("")

        every {
            notifcenterDetailUseCase.getFirstPageNotification(
                    any(),
                    any(),
                    any(),
                    captureLambda()
            )
        } answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(expectedValue)
        }

        // when
        viewModel.loadFirstPageNotification(RoleType.BUYER)

        // then
        verify { notificationItemsObserver.onChanged(Fail(expectedValue)) }
    }

    @Test fun `loadNotificationFilter should return filter list properly`() {
        runBlocking {
            // given
            val role = RoleType.BUYER
            val expectedValue = Resource.success(notifCenterFilterResponse)
            val flow = flow { emit(expectedValue) }

            every { notifcenterFilterUseCase.getFilter(role) } returns flow

            // when
            viewModel.loadNotificationFilter(role)

            // then
            coVerifyOrder {
                filterListObserver.onChanged(expectedValue)
                assert(viewModel.filterList.value == expectedValue)
            }
        }
    }

    @Test fun `loadNotificationFilter should throw the Resource error state`() {
        runBlocking {
            // given
            val expectedValue = Resource.error(Throwable(), null)
            val flow = flow { emit(expectedValue) }
            every { notifcenterFilterUseCase.getFilter(any()) } returns flow

            // when
            viewModel.loadNotificationFilter(0)

            // then
            verify { filterListObserver.onChanged(expectedValue) }
        }
    }

    @Test fun `markNotificationAsRead should called markAsRead() properly`() {
        // given
        val role = RoleType.BUYER
        val element = NotificationUiModel()

        // when
        viewModel.markNotificationAsRead(role, element)

        // then
        verify(exactly = 1) { markAsReadUseCase.markAsRead(role, "") }
    }

    @Test fun `loadMoreEarlier should return correctly`() {
        // given
        val expectedValue = NotifcenterDetailMapper().mapEarlierSection(
                notifCenterDetailResponse,
                needSectionTitle = false,
                needLoadMoreButton = false,
                needDivider = false
        )

        val role = RoleType.BUYER
        viewModel.reset() // filter id

        every {
            notifcenterDetailUseCase.getMoreEarlierNotifications(
                    viewModel.filter,
                    role,
                    captureLambda(),
                    any()
            )
        } answers {
            val onSuccess = lambda<(NotificationDetailResponseModel) -> Unit>()
            onSuccess.invoke(expectedValue)
        }

        // when
        viewModel.loadMoreEarlier(role)

        // then
        verify {
            val successState = Success(expectedValue)

            notificationItemsObserver.onChanged(successState)
            assertEquals(expectedValue, successState.data)
        }
    }

    @Test fun `loadMoreEarlier should throw the Fail state`() {
        // given
        val expectedValue = Throwable("")

        every {
            notifcenterDetailUseCase.getMoreEarlierNotifications(
                    any(),
                    any(),
                    any(),
                    captureLambda()
            )
        } answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(expectedValue)
        }

        // when
        viewModel.loadMoreEarlier(0)

        // then
        verify { notificationItemsObserver.onChanged(Fail(expectedValue)) }
    }

    @Test fun `loadMoreNew with lambda should called getMoreNewNotifications() in usecase`() {
        // given
        val role = RoleType.BUYER
        val onSuccess: (NotificationDetailResponseModel) -> Unit = {}
        val onError: (Throwable) -> Unit = {}

        // when
        viewModel.loadMoreNew(role, onSuccess, onError)

        // then
        verify(exactly = 1) {
            notifcenterDetailUseCase.getMoreNewNotifications(any(), any(), any(), any())
        }
    }

    @Test fun `loadMoreEarlier with lambda should called getMoreEarlierNotifications() in usecase`() {
        // given
        val role = RoleType.BUYER
        val onSuccess: (NotificationDetailResponseModel) -> Unit = {}
        val onError: (Throwable) -> Unit = {}

        // when
        viewModel.loadMoreEarlier(role, onSuccess, onError)

        // then
        verify(exactly = 1) {
            notifcenterDetailUseCase.getMoreEarlierNotifications(any(), any(), any(), any())
        }
    }

    @Test fun `loadRecommendations should return data properly`() {
        // given
        val listOfRecommWidget = productRecommResponse
                .productRecommendationWidget
                ?.data
                ?.let {
                    mappingToRecommendationModel(it)
                }?: listOf(RecommendationWidget())

        val expectedValue = listOfRecommWidget.first()

        every {
            getRecommendationUseCase.createObservable(any())
        } returns Observable.just(listOfRecommWidget)

        // when
        viewModel.loadRecommendations(0)

        // then
        coVerifyOrder{
            val asVisitable = getRecommendationVisitables(0, expectedValue)
            recommendationsObserver.onChanged(asVisitable)
            assert(viewModel.recommendations.value == asVisitable)
        }
    }

    @After fun tearDown() {
        viewModel.cancelAllUseCase()
    }

    companion object {
        private val notifCenterDetailResponse: NotifcenterDetailResponse = FileUtil.parse(
                "/success_notifcenter_detail_v3.json",
                NotifcenterDetailResponse::class.java
        )

        private val notifCenterFilterResponse: NotifcenterFilterResponse = FileUtil.parse(
                "/success_notifcenter_filter_as_buyer.json",
                NotifcenterFilterResponse::class.java
        )

        private val productRecommResponse: RecommendationEntity = FileUtil.parse(
                "/success_notifcenter_recomm_inbox.json",
                RecommendationEntity::class.java
        )
    }

}