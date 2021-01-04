package com.tokopedia.shop.favourite.view.presenter

import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.favourite.data.pojo.shopfollowerlist.GetShopFollowerListData
import com.tokopedia.shop.favourite.data.pojo.shopfollowerlist.ShopFollowerData
import com.tokopedia.shop.favourite.data.pojo.shopfollowerlist.ShopFollowerList
import com.tokopedia.shop.favourite.view.model.ShopFollowerListResultUiModel
import com.tokopedia.shop.favourite.view.model.ShopFollowerUiModel
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import rx.Subscriber
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

class ShopFavouriteListPresenterTest: ShopFavouriteListPresenterTestFixtures() {

    @Test
    fun `when toggleFavouriteShop success should trigger onSuccessToggleFavourite`() {
        // given
        every { userSessionInterface.isLoggedIn } returns true
        every { toggleFavouriteShopAndDeleteCacheUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<Boolean>>().onStart()
            secondArg<Subscriber<Boolean>>().onCompleted()
            secondArg<Subscriber<Boolean>>().onNext(true)
        }

        //when
        shopFavouriteListPresenter.toggleFavouriteShop("117")

        //then
        verify { shopFavouriteListView.onSuccessToggleFavourite(true) }
        confirmVerified(shopFavouriteListView)
    }

    @Test
    fun `when toggleFavouriteShop not loggedin should trigger onErrorToggleFavourite`() {
        // given
        every { userSessionInterface.isLoggedIn } returns false

        //when
        shopFavouriteListPresenter.toggleFavouriteShop("117")

        //then
        verify { shopFavouriteListView.onErrorToggleFavourite(any()) }
        confirmVerified(shopFavouriteListView)
    }

    @Test
    fun `when toggleFavouriteShop Error should trigger onErrorToggleFavourite`() {
        // given
        val throwable = Throwable()
        every { userSessionInterface.isLoggedIn } returns true
        every { toggleFavouriteShopAndDeleteCacheUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<Boolean>>().onStart()
            secondArg<Subscriber<Boolean>>().onError(throwable)
        }

        //when
        shopFavouriteListPresenter.toggleFavouriteShop("117")

        //then
        verify { shopFavouriteListView.onErrorToggleFavourite(throwable) }
        confirmVerified(shopFavouriteListView)
    }

    @Test
    fun `when detach view should unsubscribe use case`() {
        shopFavouriteListPresenter.detachView()
        coVerify { toggleFavouriteShopAndDeleteCacheUseCase.unsubscribe() }
        coVerify { gqlGetShopInfoUseCase.cancelJobs() }
    }

    @Test
    fun `when isMyShop called should return valid logged in shop`() {
        // given
        every { userSessionInterface.shopId } returns "117"
        // when
        val resultPositive = shopFavouriteListPresenter.isMyShop("117")
        val resultNegative = shopFavouriteListPresenter.isMyShop("118")
        // then
        Assert.assertTrue(resultPositive)
        Assert.assertFalse(resultNegative)
    }

    @Test
    fun `when get isLoggedIn should return valid logged in status`() {
        // given
        every { userSessionInterface.isLoggedIn } returns true
        // when
        val resultPositive = shopFavouriteListPresenter.isLoggedIn
        // then
        Assert.assertTrue(resultPositive)

        // given
        every { userSessionInterface.isLoggedIn } returns false
        // when
        val resultNegative = shopFavouriteListPresenter.isLoggedIn
        // then
        Assert.assertFalse(resultNegative)
    }

    @Test
    fun `when getShopFavouriteList success should return UI model`() {
        runBlocking {
            // given
            val shopFollowerData = ShopFollowerData("id", "name", "photo", "url")
            val shopFollowerDataList = listOf(shopFollowerData)
            val shopFollowerList = ShopFollowerList(
                    shopFollowerDataList,
                    Error(),
                    false)
            val outputData = GetShopFollowerListData(shopFollowerList)
            val expectedData = outputData.convertToUiModel(1)
            coEvery { getShopFollowerListUseCase.executeOnBackground() } returns outputData

            // when
            shopFavouriteListPresenter.getShopFavouriteList("117", 1)

            // then
            coVerify { getShopFollowerListUseCase.executeOnBackground() }
            verify { shopFavouriteListView.renderList(expectedData.followerUiModelList, expectedData.isCanLoadMore) }
            confirmVerified(shopFavouriteListView)
        }
    }

    @Test
    fun `when getShopInfo success should return UI model`() {
        // given
        val shopInfo = ShopInfo()
        every { gqlGetShopInfoUseCase.execute(any(), any()) }.answers {
            firstArg<(ShopInfo) -> Unit>().invoke(shopInfo)
        }

        //when
        shopFavouriteListPresenter.getShopInfo("117")

        //then
        verify { shopFavouriteListView.onSuccessGetShopInfo(shopInfo) }
        confirmVerified(shopFavouriteListView)
    }

    @Test
    fun `when getShopInfo error should return UI model`() {
        // given
        val throwable = Throwable()
        every { gqlGetShopInfoUseCase.execute(any(), any()) }.answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        //when
        shopFavouriteListPresenter.getShopInfo("117")

        //then
        verify { shopFavouriteListView.showGetListError(throwable) }
        confirmVerified(shopFavouriteListView)
    }

    private fun GetShopFollowerListData.convertToUiModel(currentPage: Int) = shopFollowerList.let { result ->
        ShopFollowerListResultUiModel(
                isCanLoadMore = result.haveNext,
                currentPage = currentPage,
                followerUiModelList = result.shopFollowerData.map { it.convertToUiModel() }
        )
    }

    private fun ShopFollowerData.convertToUiModel(): ShopFollowerUiModel = ShopFollowerUiModel(
            id = followerID,
            name = followerName,
            imageUrl = photo,
            profileUrl = profileURL
    )
}