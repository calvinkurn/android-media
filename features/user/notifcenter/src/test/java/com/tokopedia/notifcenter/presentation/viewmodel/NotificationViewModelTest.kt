package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.notifcenter.domain.ClearNotifCounterUseCase
import com.tokopedia.notifcenter.domain.MarkNotificationAsReadUseCase
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.domain.NotifcenterFilterV2UseCase
import com.tokopedia.notifcenter.util.coroutines.TestDispatcherProvider
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Rule

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

}