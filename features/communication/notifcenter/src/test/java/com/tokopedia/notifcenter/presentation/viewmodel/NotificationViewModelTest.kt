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
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationViewModel.Companion.CLEAR_ALL_NOTIF_TYPE
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationViewModel.Companion.DEFAULT_SHOP_ID
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationViewModel.Companion.getRecommendationVisitables
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
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
    private val addToWishlistV2UseCase: AddToWishlistV2UseCase = mockk(relaxed = true)
    private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase = mockk(relaxed = true)

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
            removeWishListUseCase,
            addToWishlistV2UseCase,
            deleteWishlistV2UseCase,
            topAdsWishlishedUseCase,
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
    fun `loadNotificationFilter should throw throwable when error`() {
        runBlocking {
            // given
            val expectedThrowable = Throwable("Oops!")
            every { notifcenterFilterUseCase.getFilter(any()) } throws expectedThrowable

            // when
            viewModel.loadNotificationFilter(0)

            // then
            Assert.assertEquals(
                expectedThrowable.message,
                (viewModel.filterList.value as Resource).throwable?.message
            )
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
    fun `markNotificationAsRead should do nothing when error`() {
        // given
        val expectedThrowable = Throwable("Oops!")
        val role = RoleType.BUYER
        val element = NotificationUiModel()
        every { markAsReadUseCase.markAsRead(any(), any()) } throws expectedThrowable

        // when
        viewModel.markNotificationAsRead(role, element)

        // then
        verify(exactly = 1) { markAsReadUseCase.markAsRead(role, element.notifId) }
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
    fun `bumpReminder should throw when error`() {
        runBlocking {
            // given
            val expectedThrowable = Throwable("Oops!")

            every { bumpReminderUseCase.bumpReminder(any(), any()) } throws expectedThrowable

            // when
            viewModel.bumpReminder(ProductData(), NotificationUiModel())

            // then
            Assert.assertEquals(
                expectedThrowable.message,
                (viewModel.bumpReminder.value as Resource).throwable?.message
            )
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
    fun `deleteReminder should throw throwable when error`() {
        runBlocking {
            // given
            val expectedThrowable = Throwable("Oops!")

            every { deleteReminderUseCase.deleteReminder(any(), any()) } throws expectedThrowable

            // when
            viewModel.deleteReminder(ProductData(), NotificationUiModel())

            // then
            Assert.assertEquals(
                expectedThrowable.message,
                (viewModel.deleteReminder.value as Resource).throwable?.message
            )
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
        // given
        val viewModelSpyk = spyk(viewModel, recordPrivateCalls = true)
        val recommItem = RecommendationItem(isTopAds = false)
        val callback: (Boolean, Throwable?) -> Unit = { _, _ -> }

        // when
        viewModelSpyk.addWishlist(recommItem, callback)

        // then
        verify(exactly = 1) {
            viewModelSpyk.addWishListNormal(recommItem.productId.toString(), any())
        }
    }

    @Test
    fun `verify add to wishlistV2` () {
        val recommItem = RecommendationItem(isTopAds = false, productId = 12L)
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishlistV2(recommItem, mockListener)

        verify { addToWishlistV2UseCase.setParams(recommItem.productId.toString(), userSessionInterface.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `verify remove wishlistV2`(){
        val recommItem = RecommendationItem(isTopAds = false, productId = 12L)
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistRemoveV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeWishlistV2(recommItem, mockListener)

        verify { deleteWishlistV2UseCase.setParams(recommItem.productId.toString(), userSessionInterface.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun normal_wishlist_should_call_onSuccessAddWishlist_when_success() {
        // given
        val expectedResultId = "123"
        var result: String? = null
        val wishListActionListener = object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {}
            override fun onSuccessAddWishlist(productId: String?) {
                result = productId
            }
            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}
            override fun onSuccessRemoveWishlist(productId: String?) {}
        }

        every {
            addWishListUseCase.createObservable(any(), any(), any())
        } answers {
            wishListActionListener.onSuccessAddWishlist(expectedResultId)
        }

        // when
        viewModel.addWishListNormal("123", wishListActionListener)

        // then
        assertEquals(expectedResultId, result)
    }

    @Test
    fun normal_wishlist_should_call_onErrorAddWishList_when_error() {
        // given
        val expectedResultId = "123"
        val expectedResult = "Oops!"
        var result: String? = null
        var resultId: String? = null
        val wishListActionListener = object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                result = errorMessage
                resultId = productId
            }
            override fun onSuccessAddWishlist(productId: String?) {}
            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}
            override fun onSuccessRemoveWishlist(productId: String?) {}
        }

        every {
            addWishListUseCase.createObservable(any(), any(), any())
        } answers {
            wishListActionListener.onErrorAddWishList(expectedResult, expectedResultId)
        }

        // when
        viewModel.addWishListNormal("123", wishListActionListener)

        // then
        assertEquals(expectedResultId, resultId)
        assertEquals(expectedResult, result)
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
    fun `when success addProductToCart but status success is 0`() {
        // Given
        val onSuccess: (data: DataModel) -> Unit = mockk(relaxed = true)
        val onError: (msg: String) -> Unit = mockk(relaxed = true)
        val successAtc = getSuccessAtcModel().apply {
            this.data.success = 0
        }
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(successAtc)

        // When
        viewModel.addProductToCart(RequestParams(), onSuccess, onError)

        // Then
        verify(exactly = 1) {
            onError.invoke(any())
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
    fun `when error throwable addProductToCart but empty message`() {
        // Given
        val onError: (msg: String) -> Unit = mockk(relaxed = true)
        val expectedThrowable = Throwable(message = null)
        every {
            addToCartUseCase.createObservable(any())
        } throws expectedThrowable

        // When
        viewModel.addProductToCart(RequestParams(), {}, onError)

        // Then
        verify(exactly = 0) {
            onError.invoke(any())
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

    @Test
    fun clearNotifCounter_reset_type_zero_when_user_does_not_have_shop() {
        // Given
        val role = RoleType.BUYER
        val expectedValue = Resource.success(clearNotifCounterResponse)
        val flow = flow { emit(expectedValue) }
        every { clearNotifUseCase.clearNotifCounter(CLEAR_ALL_NOTIF_TYPE) } returns flow
        every { userSessionInterface.shopId } returns DEFAULT_SHOP_ID

        // when
        viewModel.clearNotifCounter(role)
        viewModel.clearNotif.observeForever(clearNotifObserver)

        // then
        verify {
            clearNotifObserver.onChanged(expectedValue)
        }
    }

    @Test
    fun should_load_not_change_topAdsBanner_value_when_empty_result() {
        val expectedResult: ArrayList<TopAdsImageViewModel> = arrayListOf()
        coEvery {
            topAdsImageViewUseCase.getImageData(any())
        } returns expectedResult

        viewModel.loadTopAdsBannerData()

        verify (exactly = 1) {
            getRecommendationUseCase.createObservable(any())
        }
        Assert.assertEquals(
            null,
            viewModel.topAdsBanner.value
        )
    }

    @Test
    fun should_load_first_page_recommendation_when_fail_get_topadds_banner() {
        val expectedThrowable = Throwable("Oops!")
        coEvery {
            topAdsImageViewUseCase.getImageData(any())
        } throws expectedThrowable

        viewModel.loadTopAdsBannerData()

        verify (exactly = 1) {
            getRecommendationUseCase.createObservable(any())
        }
    }

    @Test
    fun should_invoke_callback_when_success_get_topAds_wishlist() {
        //Given
        val testRecommendationItem = RecommendationItem()
        val testCallback: ((Boolean, Throwable?) -> Unit) = mockk(relaxed = true)
        val expectedResponse = WishlistModel().apply {
            val successData = WishlistModel.Data()
            successData.setSuccess(true)
            data = successData
        }
        every {
            topAdsWishlishedUseCase.createObservable(any<RequestParams>()).toBlocking().single()
        } returns expectedResponse

        //When
        viewModel.addWishListTopAds(testRecommendationItem, testCallback)

        //Then
        verify (exactly = 1) {
            testCallback.invoke(any(), any())
        }
    }

    @Test
    fun should_not_invoke_callback_when_success_get_topAds_wishlist_but_empty() {
        //Given
        val testRecommendationItem = RecommendationItem()
        val testCallback: ((Boolean, Throwable?) -> Unit) = mockk(relaxed = true)
        val expectedResponse = WishlistModel()
        every {
            topAdsWishlishedUseCase.createObservable(any<RequestParams>()).toBlocking().single()
        } returns expectedResponse

        //When
        viewModel.addWishListTopAds(testRecommendationItem, testCallback)

        //Then
        verify (exactly = 0) {
            testCallback.invoke(any(), any())
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