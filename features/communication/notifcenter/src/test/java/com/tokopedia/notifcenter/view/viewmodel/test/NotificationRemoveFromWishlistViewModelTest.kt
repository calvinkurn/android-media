package com.tokopedia.notifcenter.view.viewmodel.test

import com.tokopedia.notifcenter.view.viewmodel.base.NotificationViewModelTestFixture
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class NotificationRemoveFromWishlistViewModelTest : NotificationViewModelTestFixture() {
    @Test
    fun `verify remove wishlistV2 returns success`() {
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
    fun `verify remove wishlistV2 returns fail`() {
        val recommItem = RecommendationItem(isTopAds = false, productId = 12L)
        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeWishlistV2(recommItem, mockListener)

        verify { deleteWishlistV2UseCase.setParams(recommItem.productId.toString(), userSessionInterface.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }
}
