package com.tokopedia.notifcenter.view.viewmodel.test

import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.entity.affiliate.AffiliateEducationArticleResponse
import com.tokopedia.notifcenter.data.mapper.NotifcenterDetailMapper
import com.tokopedia.notifcenter.data.state.Resource
import com.tokopedia.notifcenter.data.uimodel.LoadMoreUiModel
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.view.viewmodel.base.NotificationViewModelTestFixture
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
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

        coEvery {
            notifcenterDetailUseCase(any())
        } returns expectedValue

        viewModel.filter = NotifcenterDetailUseCase.FILTER_NONE

        // when
        viewModel.loadFirstPageNotification(role)

        // then
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value?.result as Success).data
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

        coEvery {
            notifcenterDetailUseCase(any())
        } returns expectedValue

        // when
        viewModel.loadFirstPageNotification(role)

        // then
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value?.result as Success).data
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

        coEvery {
            notifcenterDetailUseCase(any())
        } returns expectedValue

        // when
        viewModel.loadFirstPageNotification(role)

        // then
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value?.result as Success).data
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

        coEvery {
            affiliateEducationArticleUseCase(Unit)
        } returns flow

        coEvery {
            notifcenterDetailUseCase(any())
        } returns notifResponse

        // when
        viewModel.loadFirstPageNotification(role)

        // then
        Assert.assertEquals(
            notifResponse,
            (viewModel.notificationItems.value?.result as Success).data
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

        coEvery {
            affiliateEducationArticleUseCase(Unit)
        } throws expectedThrowable

        coEvery {
            notifcenterDetailUseCase(any())
        } returns notifResponse

        // when
        viewModel.loadFirstPageNotification(role)

        // then
        Assert.assertEquals(
            notifResponse,
            (viewModel.notificationItems.value?.result as Success).data
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

        coEvery {
            notifcenterDetailUseCase(any())
        } throws expectedValue

        // when
        viewModel.loadFirstPageNotification(RoleType.BUYER)

        // then
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value?.result as Fail).throwable
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

        coEvery {
            notifcenterDetailUseCase(any())
        } returns expectedValue

        // when
        viewModel.loadMoreEarlier(role)

        // then
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value?.result as Success).data
        )
    }

    @Test
    fun `loadMoreEarlier with lastKnownPair should return correctly`() {
        // given
        val expectedValue = NotifcenterDetailMapper().mapEarlierSection(
            notifCenterDetailResponse,
            needSectionTitle = false,
            needLoadMoreButton = false,
            needDivider = false
        )
        val lastKnownPair = Pair(-1, LoadMoreUiModel(LoadMoreUiModel.LoadMoreType.EARLIER))
        val role = RoleType.BUYER
        viewModel.reset() // filter id

        coEvery {
            notifcenterDetailUseCase(any())
        } returns expectedValue

        // when
        viewModel.loadMoreEarlier(role, lastKnownPair.first, lastKnownPair.second)

        // then
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value?.result as Success).data
        )
    }

    @Test
    fun `loadMoreNewshould return correctly`() {
        // given
        val expectedValue = NotifcenterDetailMapper().mapEarlierSection(
            notifCenterDetailResponse,
            needSectionTitle = false,
            needLoadMoreButton = false,
            needDivider = false
        )
        val lastKnownPair = Pair(-1, LoadMoreUiModel(LoadMoreUiModel.LoadMoreType.NEW))
        val role = RoleType.BUYER
        viewModel.reset() // filter id

        coEvery {
            notifcenterDetailUseCase(any())
        } returns expectedValue

        // when
        viewModel.loadMoreNew(role, lastKnownPair.first, lastKnownPair.second)

        // then
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value?.result as Success).data
        )
    }

    @Test
    fun `loadMoreEarlier should throw the Fail state`() {
        // given
        val expectedValue = Throwable("")

        coEvery {
            notifcenterDetailUseCase(any())
        } throws expectedValue

        // when
        viewModel.loadMoreEarlier(0)

        // then
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value?.result as Fail).throwable
        )
    }

    @Test
    fun `loadMoreNew should throw the Fail state`() {
        // given
        val expectedValue = Throwable("")
        val lastKnownPair = Pair(-1, LoadMoreUiModel(LoadMoreUiModel.LoadMoreType.NEW))
        coEvery {
            notifcenterDetailUseCase(any())
        } throws expectedValue

        // when
        viewModel.loadMoreNew(0, lastKnownPair.first, lastKnownPair.second)

        // then
        Assert.assertEquals(
            expectedValue,
            (viewModel.notificationItems.value?.result as Fail).throwable
        )
    }
}
