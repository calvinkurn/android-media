package com.tokopedia.notifcenter.ui.viewmodel.test

import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.entity.affiliate.AffiliateEducationArticleResponse
import com.tokopedia.notifcenter.data.entity.notification.NotificationDetailResponseModel
import com.tokopedia.notifcenter.data.mapper.NotifcenterDetailMapper
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.ui.viewmodel.base.NotificationViewModelTestFixture
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.invoke
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Test
import kotlin.test.assertNotNull

class NotificationDetailViewModelTest : NotificationViewModelTestFixture() {
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
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value as Success).data
        )
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
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value as Success).data
        )
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
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value as Success).data
        )
        Assert.assertEquals(
            topAdsImageView,
            viewModel.topAdsBanner.value?.ads
        )
    }

    @Test
    fun `loadFirstPageNotification as affiliate should return data properly`() {
        // given
        val cardsItem = AffiliateEducationArticleResponse.CardsArticle.Data.CardsItem()
        val eduResponse = AffiliateEducationArticleResponse(
            AffiliateEducationArticleResponse.CardsArticle(
                AffiliateEducationArticleResponse.CardsArticle.Data(
                    listOf(
                        cardsItem
                    )
                )
            )
        )
        val notifResponse = NotifcenterDetailMapper().mapFirstPage(
            notifCenterDetailResponse,
            needSectionTitle = false,
            needLoadMoreButton = false
        )
        val flow = flow { emit(Resource.success(eduResponse)) }

        val role = RoleType.AFFILIATE
        viewModel.reset() // filter id

        coEvery { affiliateEducationArticleUseCase.getEducationArticles() } returns flow

        every {
            notifcenterDetailUseCase.getFirstPageNotification(
                viewModel.filter,
                role,
                captureLambda(),
                any()
            )
        } answers {
            val onSuccess = lambda<(NotificationDetailResponseModel) -> Unit>()
            onSuccess.invoke(notifResponse)
        }

        // when
        viewModel.loadFirstPageNotification(role)

        // then
        Assert.assertEquals(
            notifResponse,
            (viewModel.notificationItems.value as Success).data
        )
        assertNotNull(viewModel.affiliateEducationArticle.value)
    }

    @Test
    fun `loadFirstPageNotification as affiliate should return data properly even without education articles`() {
        // given
        val expectedThrowable = Throwable("Oops!")
        val notifResponse = NotifcenterDetailMapper().mapFirstPage(
            notifCenterDetailResponse,
            needSectionTitle = false,
            needLoadMoreButton = false
        )

        val role = RoleType.AFFILIATE
        viewModel.reset() // filter id

        coEvery { affiliateEducationArticleUseCase.getEducationArticles() } throws expectedThrowable

        every {
            notifcenterDetailUseCase.getFirstPageNotification(
                viewModel.filter,
                role,
                captureLambda(),
                any()
            )
        } answers {
            val onSuccess = lambda<(NotificationDetailResponseModel) -> Unit>()
            onSuccess.invoke(notifResponse)
        }

        // when
        viewModel.loadFirstPageNotification(role)

        // then
        Assert.assertEquals(
            notifResponse,
            (viewModel.notificationItems.value as Success).data
        )
        Assert.assertEquals(
            null,
            viewModel.affiliateEducationArticle.value
        )
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
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value as Fail).throwable
        )
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
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value as Success).data
        )
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
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value as Fail).throwable
        )
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
}
