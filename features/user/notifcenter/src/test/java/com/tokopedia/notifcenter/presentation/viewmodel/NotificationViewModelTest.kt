package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.inboxcommon.util.FileUtil
import com.tokopedia.notifcenter.data.entity.bumpreminder.BumpReminderResponse
import com.tokopedia.notifcenter.data.entity.clearnotif.ClearNotifCounterResponse
import com.tokopedia.notifcenter.data.entity.deletereminder.DeleteReminderResponse
import com.tokopedia.notifcenter.data.entity.filter.NotifcenterFilterResponse
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.notifcenter.data.mapper.NotifcenterDetailMapper
import com.tokopedia.notifcenter.data.model.RecommendationDataModel
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.data.uimodel.NotificationTopAdsBannerUiModel
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.domain.*
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationViewModel.Companion.getRecommendationVisitables
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import kotlin.test.assertEquals

class NotificationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val notifcenterDetailUseCase: NotifcenterDetailUseCase = mockk(relaxed = true)
    private val notifcenterFilterUseCase: NotifcenterFilterV2UseCase = mockk(relaxed = true)
    private val clearNotifUseCase: ClearNotifCounterUseCase = mockk(relaxed = true)
    private val bumpReminderUseCase: NotifcenterSetReminderBumpUseCase = mockk(relaxed = true)
    private val deleteReminderUseCase: NotifcenterDeleteReminderBumpUseCase = mockk(relaxed = true)
    private val markAsReadUseCase: MarkNotificationAsReadUseCase = mockk(relaxed = true)
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase = mockk(relaxed = true)
    private val getRecommendationUseCase: GetRecommendationUseCase = mockk(relaxed = true)
    private val addWishListUseCase: AddWishListUseCase = mockk(relaxed = true)
    private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase = mockk(relaxed = true)
    private val removeWishListUseCase: RemoveWishListUseCase = mockk(relaxed = true)
    private val userSessionInterface: UserSessionInterface = mockk(relaxed = true)
    private val addToCartUseCase: AddToCartUseCase = mockk(relaxed = true)
    private val notifOrderListUseCase: NotifOrderListUseCase = mockk(relaxed = true)

    private val dispatcher = CoroutineTestDispatchersProvider

    private val notificationItemsObserver: Observer<Result<NotificationDetailResponseModel>> = mockk(relaxed = true)
    private val bumpReminderObserver: Observer<Resource<BumpReminderResponse>> = mockk(relaxed = true)
    private val deleteReminderObserver: Observer<Resource<DeleteReminderResponse>> = mockk(relaxed = true)
    private val recommendationsObserver: Observer<RecommendationDataModel> = mockk(relaxed = true)
    private val topAdsBannerObserver: Observer<NotificationTopAdsBannerUiModel> = mockk(relaxed = true)
    private val filterListObserver: Observer<Resource<NotifcenterFilterResponse>> = mockk(relaxed = true)
    private val clearNotifObserver: Observer<Resource<ClearNotifCounterResponse>> = mockk(relaxed = true)
    private val orderListObserver: Observer<Resource<NotifOrderListResponse>> = mockk(relaxed = true)

    private val viewModel = NotificationViewModel(
            notifcenterDetailUseCase,
            notifcenterFilterUseCase,
            bumpReminderUseCase,
            deleteReminderUseCase,
            clearNotifUseCase,
            markAsReadUseCase,
            topAdsImageViewUseCase,
            getRecommendationUseCase,
            addWishListUseCase,
            topAdsWishlishedUseCase,
            removeWishListUseCase,
            userSessionInterface,
            addToCartUseCase,
            notifOrderListUseCase,
            dispatcher
    )

    @Before
    fun setUp() {
        viewModel.notificationItems.observeForever(notificationItemsObserver)
        viewModel.recommendations.observeForever(recommendationsObserver)
        viewModel.deleteReminder.observeForever(deleteReminderObserver)
        viewModel.bumpReminder.observeForever(bumpReminderObserver)
        viewModel.topAdsBanner.observeForever(topAdsBannerObserver)
        viewModel.filterList.observeForever(filterListObserver)
        viewModel.clearNotif.observeForever(clearNotifObserver)
    }

    @Test
    fun `hasFilter should return true if filter is not FILTER_NONE`() {
        // given
        val expectedValue = true
        viewModel.filter = -1

        // when
        val hasFilter = viewModel.hasFilter()

        assert(hasFilter == expectedValue)
    }

    @Test
    fun `loadFirstPageNotification verify haven't interaction`() {
        // when
        viewModel.loadFirstPageNotification(null)

        // then
        verify(exactly = 0) {
            notifcenterDetailUseCase.getFirstPageNotification(
                    any(),
                    any(),
                    any(),
                    any()
            )
        }
    }

    @Test
    fun `loadFirstPageNotification that haven't filter should return only notifItems properly`() {
        // given
        val role = RoleType.SELLER
        val expectedValue = NotifcenterDetailMapper().mapFirstPage(
                notifCenterDetailResponse,
                needSectionTitle = false,
                needLoadMoreButton = false
        )

        every {
            notifcenterDetailUseCase.getFirstPageNotification(
                    any(),
                    any(),
                    captureLambda(),
                    any()
            )
        } answers {
            val onSuccess = lambda<(NotificationDetailResponseModel) -> Unit>()
            onSuccess.invoke(expectedValue)
        }

        viewModel.filter = NotifcenterDetailUseCase.FILTER_NONE

        // when
        viewModel.loadFirstPageNotification(role)

        // then
        verify(exactly = 1) { notificationItemsObserver.onChanged(Success(expectedValue)) }
        coVerify(exactly = 0) { topAdsImageViewUseCase.getImageData(any()) }
    }

    @Test
    fun `loadFirstPageNotification as seller should return data properly`() {
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
        verify(exactly = 1) { notificationItemsObserver.onChanged(Success(expectedValue)) }
    }

    @Test
    fun `loadFirstPageNotification as buyer should return data properly`() {
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

    @Test
    fun `loadFirstPageNotification should throw the Fail state`() {
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
        verify(exactly = 1) { notificationItemsObserver.onChanged(Fail(expectedValue)) }
    }

    @Test
    fun `loadNotificationFilter verify haven't interaction`() {
        // when
        viewModel.loadNotificationFilter(null)

        // then
        verify(exactly = 0) { notifcenterFilterUseCase.getFilter(any()) }
    }

    @Test
    fun `loadNotificationFilter should return filter list properly`() {
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

    @Test
    fun `loadNotificationFilter should throw the Resource error state`() {
        runBlocking {
            // given
            val expectedValue = Resource.error(Throwable(), null)
            val flow = flow { emit(expectedValue) }
            every { notifcenterFilterUseCase.getFilter(any()) } returns flow

            // when
            viewModel.loadNotificationFilter(0)

            // then
            verify(exactly = 1) { filterListObserver.onChanged(expectedValue) }
        }
    }

    @Test
    fun `markNotificationAsRead verify haven't interaction`() {
        // given
        val element = NotificationUiModel()

        // when
        viewModel.markNotificationAsRead(null, element)

        // then
        verify(exactly = 0) { markAsReadUseCase.markAsRead(any(), any()) }
    }

    @Test
    fun `markNotificationAsRead should called markAsRead() properly`() {
        // given
        val role = RoleType.BUYER
        val element = NotificationUiModel()

        // when
        viewModel.markNotificationAsRead(role, element)

        // then
        verify(exactly = 1) { markAsReadUseCase.markAsRead(role, "") }
    }

    @Test
    fun `loadMoreEarlier verify haven't interaction`() {
        // when
        viewModel.loadMoreEarlier(null)

        // then
        verify(exactly = 0) {
            notifcenterDetailUseCase.getMoreEarlierNotifications(
                    any(),
                    any(),
                    any(),
                    any()
            )
        }
    }

    @Test
    fun `loadMoreEarlier should return correctly`() {
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
        verify(exactly = 1) {
            val successState = Success(expectedValue)
            notificationItemsObserver.onChanged(successState)
            assertEquals(expectedValue, successState.data)
        }
    }

    @Test
    fun `loadMoreEarlier should throw the Fail state`() {
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
        verify(exactly = 1) { notificationItemsObserver.onChanged(Fail(expectedValue)) }
    }

    @Test
    fun `bumpReminder should return correctly`() {
        runBlocking {
            // given
            val expectedValue = Resource.success(bumpReminderResponse)
            val flow = flow { emit(expectedValue) }

            every { bumpReminderUseCase.bumpReminder(any(), any()) } returns flow

            // when
            viewModel.bumpReminder(ProductData(), NotificationUiModel())

            // then
            verify(exactly = 1) { bumpReminderObserver.onChanged(expectedValue) }
        }
    }

    @Test
    fun `bumpReminder should throw the Fail state`() {
        runBlocking {
            // given
            val expectedValue = Resource.error(Throwable(), null)
            val flow = flow { emit(expectedValue) }

            every { bumpReminderUseCase.bumpReminder(any(), any()) } returns flow

            // when
            viewModel.bumpReminder(ProductData(), NotificationUiModel())

            // then
            verify(exactly = 1) { bumpReminderObserver.onChanged(expectedValue) }
        }
    }

    @Test
    fun `deleteReminder should return correctly`() {
        runBlocking {
            // given
            val expectedValue = Resource.success(deleteReminderResponse)
            val flow = flow { emit(expectedValue) }

            every { deleteReminderUseCase.deleteReminder(any(), any()) } returns flow

            // when
            viewModel.deleteReminder(ProductData(), NotificationUiModel())

            // then
            verify(exactly = 1) { deleteReminderObserver.onChanged(expectedValue) }
        }
    }

    @Test
    fun `deleteReminder should throw the Fail state`() {
        runBlocking {
            // given
            val expectedValue = Resource.error(Throwable(), null)
            val flow = flow { emit(expectedValue) }

            every { deleteReminderUseCase.deleteReminder(any(), any()) } returns flow

            // when
            viewModel.deleteReminder(ProductData(), NotificationUiModel())

            // then
            verify(exactly = 1) { deleteReminderObserver.onChanged(expectedValue) }
        }
    }

    @Test
    fun `loadMoreNew verify haven't interaction`() {
        // given
        val onSuccess: (NotificationDetailResponseModel) -> Unit = {}
        val onError: (Throwable) -> Unit = {}

        // when
        viewModel.loadMoreNew(null, onSuccess, onError)

        // then
        verify(exactly = 0) {
            notifcenterDetailUseCase.getMoreNewNotifications(
                    any(),
                    any(),
                    any(),
                    any()
            )
        }
    }

    @Test
    fun `loadMoreNew with lambda should called getMoreNewNotifications() in usecase`() {
        // given
        val role = RoleType.BUYER
        val onSuccess: (NotificationDetailResponseModel) -> Unit = {}
        val onError: (Throwable) -> Unit = {}

        // when
        viewModel.loadMoreNew(role, onSuccess, onError)

        // then
        verify(exactly = 1) { notifcenterDetailUseCase.getMoreNewNotifications(any(), any(), any(), any()) }
    }

    @Test
    fun `loadMoreEarlier with lambda verify haven't interaction`() {
        // given
        val onSuccess: (NotificationDetailResponseModel) -> Unit = {}
        val onError: (Throwable) -> Unit = {}

        // when
        viewModel.loadMoreEarlier(null, onSuccess, onError)

        // then
        verify(exactly = 0) {
            notifcenterDetailUseCase.getMoreEarlierNotifications(
                    any(),
                    any(),
                    any(),
                    any()
            )
        }
    }

    @Test
    fun `loadMoreEarlier with lambda should called getMoreEarlierNotifications() in usecase`() {
        // given
        val role = RoleType.BUYER
        val onSuccess: (NotificationDetailResponseModel) -> Unit = {}
        val onError: (Throwable) -> Unit = {}

        // when
        viewModel.loadMoreEarlier(role, onSuccess, onError)

        // then
        verify(exactly = 1) { notifcenterDetailUseCase.getMoreEarlierNotifications(any(), any(), any(), any()) }
    }

    @Test
    fun `loadRecommendations should return data properly`() {
        // given
        val listOfRecommWidget = productRecommResponse
                .productRecommendationWidget
                ?.data
                ?.let {
                    it.mappingToRecommendationModel()
                }

        val expectedValue = listOfRecommWidget.first()

        every {
            getRecommendationUseCase.createObservable(any())
        } returns Observable.just(listOfRecommWidget)

        // when
        viewModel.loadRecommendations(0)

        // then
        coVerifyOrder {
            val asVisitable = getRecommendationVisitables(0, expectedValue)
            recommendationsObserver.onChanged(asVisitable)
            assert(viewModel.recommendations.value == asVisitable)
        }
    }

    @Test
    fun `addWishList test if is topAds and should return called addWishListTopAds`() {
        testAddWishList(true, "addWishListTopAds")
    }

    @Test
    fun `addWishList test if is not topAds and should return called addWishListNormal`() {
        testAddWishList(false, "addWishListNormal")
    }

    @Test
    fun `removeWishList should remove a wish list item properly`() {
        // given
        val callback: (Boolean, Throwable?) -> Unit = { _, _ -> }
        val recommItem = RecommendationItem()

        every { removeWishListUseCase.createObservable(any(), any(), any()) } returns Unit

        // when
        viewModel.removeWishList(recommItem, callback)

        // then
        verify(exactly = 1) { removeWishListUseCase.createObservable(any(), any(), any()) }
    }

    @Test
    fun `when success addProductToCart`() {
        // Given
        val onSuccess: (data: DataModel) -> Unit = mockk(relaxed = true)
        val successAtc = getSuccessAtcModel()
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(successAtc)

        // When
        viewModel.addProductToCart(RequestParams(), onSuccess, {})

        // Then
        verify(exactly = 1) {
            onSuccess.invoke(successAtc.data)
        }
    }

    @Test
    fun `when error addProductToCart`() {
        // Given
        val onError: (msg: String) -> Unit = mockk(relaxed = true)
        val errorAtc = getErrorAtcModel()
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(errorAtc)

        // When
        viewModel.addProductToCart(RequestParams(), {}, onError)

        // Then
        verify(exactly = 1) {
            onError.invoke("Gagal menambahkan produk")
        }
    }

    @Test
    fun `when error throwable addProductToCart`() {
        // Given
        val onError: (msg: String) -> Unit = mockk(relaxed = true)
        val errorMsg = "Gagal menambahkan produk"
        every {
            addToCartUseCase.createObservable(any())
        } throws IllegalStateException(errorMsg)

        // When
        viewModel.addProductToCart(RequestParams(), {}, onError)

        // Then
        verify(exactly = 1) {
            onError.invoke(errorMsg)
        }
    }

    @Test
    fun `loadNotifOrderList verify no interaction if role is null`() {
        // when
        viewModel.loadNotifOrderList(null)

        // then
        verify(exactly = 0) {
            notifOrderListUseCase.getOrderList(any())
        }
    }

    @Test
    fun `loadNotifOrderList propagate success data`() {
        // given
        val role = RoleType.BUYER
        val expectedValue = Resource.success(notifOrderListResponse)
        val flow = flow { emit(expectedValue) }

        every { notifOrderListUseCase.getOrderList(role) } returns flow

        // when
        viewModel.loadNotifOrderList(role)
        viewModel.orderList.observeForever(orderListObserver)

        // then
        verifyOrder {
            notifOrderListUseCase.getOrderList(role)
            orderListObserver.onChanged(expectedValue)
        }
    }

    @Test
    fun `loadNotifOrderList propagate error data`() {
        // given
        val role = RoleType.BUYER
        val throwable: Throwable = IllegalStateException()
        every { notifOrderListUseCase.getOrderList(role) } throws throwable

        // when
        viewModel.loadNotifOrderList(role)
        viewModel.orderList.observeForever(orderListObserver)

        // then
        verifyOrder {
            notifOrderListUseCase.getOrderList(role)
            orderListObserver.onChanged(any())
        }
        assertThat(viewModel.orderList.value?.throwable, `is`(throwable))
    }

    @Test
    fun `clearNotifCounter do nothing if role is null`() {
        // when
        viewModel.clearNotifCounter(null)

        // then
        verify(exactly = 0) {
            clearNotifUseCase.clearNotifCounter(any())
        }
    }

    @Test
    fun `clearNotifCounter propagate success data to liveData`() {
        // Given
        val role = RoleType.BUYER
        val expectedValue = Resource.success(clearNotifCounterResponse)
        val flow = flow { emit(expectedValue) }
        every { clearNotifUseCase.clearNotifCounter(role) } returns flow

        // when
        viewModel.clearNotifCounter(role)
        viewModel.clearNotif.observeForever(clearNotifObserver)

        // then
        verify {
            clearNotifObserver.onChanged(expectedValue)
        }
    }

    private fun getErrorAtcModel(): AddToCartDataModel {
        return AddToCartDataModel().apply {
            data.success = 0
            data.message.add("Gagal menambahkan produk")
        }
    }

    private fun getSuccessAtcModel(): AddToCartDataModel {
        return AddToCartDataModel().apply {
            data.success = 1
        }
    }

    @After
    fun tearDown() {
        viewModel.cancelAllUseCase()
    }

    private fun testAddWishList(isTopAds: Boolean, methodName: String) {
        // given
        val viewModelSpyk = spyk(viewModel, recordPrivateCalls = true)
        val recommItem = RecommendationItem(isTopAds = isTopAds)
        val callback: (Boolean, Throwable?) -> Unit = { _, _ -> }

        // when
        viewModelSpyk.addWishlist(recommItem, callback)

        // then
        verify(exactly = 1) { viewModelSpyk[methodName](recommItem, callback) }
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

        private val bumpReminderResponse: BumpReminderResponse = FileUtil.parse(
                "/success_notifcenter_bump_reminder.json",
                BumpReminderResponse::class.java
        )

        private val deleteReminderResponse: DeleteReminderResponse = FileUtil.parse(
                "/success_notifcenter_delete_reminder.json",
                DeleteReminderResponse::class.java
        )

        private val notifOrderListResponse = NotifOrderListResponse()
        private val clearNotifCounterResponse = ClearNotifCounterResponse()
    }

}