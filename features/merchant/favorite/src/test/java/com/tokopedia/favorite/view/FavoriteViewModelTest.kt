package com.tokopedia.favorite.view

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.paging.PagingHandler
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.favorite.domain.interactor.GetAllDataFavoriteUseCaseWithCoroutine
import com.tokopedia.favorite.domain.interactor.GetFavoriteShopUseCaseWithCoroutine
import com.tokopedia.favorite.domain.interactor.GetInitialDataPageUseCaseWithCoroutine
import com.tokopedia.favorite.domain.model.DataFavorite
import com.tokopedia.favorite.domain.model.FavoriteShop
import com.tokopedia.favorite.domain.model.TopAdsShop
import com.tokopedia.favorite.dummyFavoriteShopItemList
import com.tokopedia.favorite.dummyTopAdsShopItemList
import com.tokopedia.favorite.randomString
import com.tokopedia.favorite.view.viewmodel.FavoriteShopUiModel
import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem
import com.tokopedia.favorite.view.viewmodel.TopAdsShopViewModel
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import rx.Observable

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class FavoriteViewModelTest {

    @RelaxedMockK
    lateinit var getInitialDataPageUseCase: GetInitialDataPageUseCaseWithCoroutine

    @RelaxedMockK
    lateinit var toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase

    @RelaxedMockK
    lateinit var getAllDataFavoriteUseCase: GetAllDataFavoriteUseCaseWithCoroutine

    @RelaxedMockK
    lateinit var getFavoriteShopUseCaseWithCoroutine: GetFavoriteShopUseCaseWithCoroutine

    @RelaxedMockK
    lateinit var pagingHandler: PagingHandler

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy {
        FavoriteViewModel(
                CoroutineTestDispatchersProvider,
                getInitialDataPageUseCase,
                toggleFavouriteShopUseCase,
                getAllDataFavoriteUseCase,
                getFavoriteShopUseCaseWithCoroutine,
                pagingHandler
        )
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
    }

    /**
     * loadInitialData section
     */

    @Test
    fun `when loadInitialData and use case return empty DataFavorite, refresh should be true then false`() {
        runBlocking {
            initialDataPageUseCase_returnEmptyDataFavorite()
            val (refreshObserver, refreshValues) = observeLiveData(viewModel.refresh)

            viewModel.loadInitialData()
            assertEquals(refreshValues.size, 2)
            assertEquals(refreshValues[0], true)
            assertEquals(refreshValues[1], false)

            viewModel.refresh.removeObserver(refreshObserver)
        }
    }

    @Test
    fun `when loadInitialData and use case return empty DataFavorite, initialData should be empty`() {
        runBlocking {
            initialDataPageUseCase_returnEmptyDataFavorite()
            val (initialDataObserver, visitables) = observeLiveData(viewModel.initialData)

            viewModel.loadInitialData()
            assertEquals(visitables.size, 1)
            assertTrue(visitables[0].isEmpty())

            viewModel.initialData.removeObserver(initialDataObserver)
        }
    }

    @Test
    fun `when loadInitialData and exception occurs in use case, refresh should be true then false`() {
        runBlocking {
            initialDataPageUseCase_throwException()
            val (refreshObserver, refreshValues) = observeLiveData(viewModel.refresh)

            viewModel.loadInitialData()
            assertTrue(refreshValues.size == 2)
            assertTrue(refreshValues[0])
            assertFalse(refreshValues[1])

            observeIsErrorLoad_return(true)

            viewModel.refresh.removeObserver(refreshObserver)
        }
    }

    @Test
    fun `when loadInitialData and exception occurs in use case, network failed should be true`() {
        runBlocking {
            initialDataPageUseCase_throwException()
            val (observer, values) = observeLiveData(viewModel.refresh)

            viewModel.loadInitialData()
            assertTrue(values.size == 2)
            assertTrue(values[0])
            assertFalse(values[1])

            observeIsErrorLoad_return(true)

            viewModel.refresh.removeObserver(observer)
        }
    }

    @Test
    fun `when loadInitialData and use case return top ads shop with network error, network failed should be true`() {
        runBlocking {
            initialDataPageUseCase_returnDataFavoriteWithTopAdsShopWithNetworkError()
            val (networkFailedObserver, networkFailedUpdates) = observeLiveData(viewModel.isNetworkFailed)

            viewModel.loadInitialData()

            // There is 1 network failed update and its values is true
            assertTrue(networkFailedUpdates.size == 1)
            assertTrue(networkFailedUpdates[0])

            viewModel.isNetworkFailed.removeObserver(networkFailedObserver)
        }
    }

    @Test
    fun `when loadInitialData and use case return top ads shop with item list, should return data with that list`() {
        runBlocking {
            val numOfItems = 2
            val itemList = dummyTopAdsShopItemList(numOfItems)
            val topAdsShop = TopAdsShop(topAdsShopItemList = itemList)
            val dataFavorite = DataFavorite(topAdsShop = topAdsShop)
            coEvery { getInitialDataPageUseCase.executeOnBackground() } returns dataFavorite

            val (observer, liveDataUpdates) = observeLiveData(viewModel.initialData)
            viewModel.loadInitialData()

            // There is 1 update
            assertTrue(liveDataUpdates.size == 1)

            // That update has 1 item in it
            assertTrue(liveDataUpdates[0].size == 1)

            // That 1 item is a TopAdsShopViewModel
            assertTrue(liveDataUpdates[0][0] is TopAdsShopViewModel)

            // That 1 item has 2 TopAdsShopItem in it and the data are correct
            val item = liveDataUpdates[0][0] as TopAdsShopViewModel
            assertTrue(item.adsShopItems!!.size == numOfItems)
            for ((i, adsShopItem) in item.adsShopItems!!.withIndex()) {
                val dummyItem = itemList[i]
                assertEquals(dummyItem.shopId, adsShopItem.shopId)
                assertEquals(dummyItem.shopDomain, adsShopItem.shopDomain)
                assertEquals(dummyItem.shopName, adsShopItem.shopName)
                assertEquals(dummyItem.adRefKey, adsShopItem.adKey)
                assertEquals(dummyItem.shopClickUrl, adsShopItem.shopClickUrl)
                assertEquals(dummyItem.shopImageCover, adsShopItem.shopCoverUrl)
                assertEquals(dummyItem.shopImageCoverEcs, adsShopItem.shopCoverEcs)
                assertEquals(dummyItem.shopImageUrl, adsShopItem.shopImageUrl)
                assertEquals(dummyItem.shopImageEcs, adsShopItem.shopImageEcs)
                assertEquals(dummyItem.shopLocation, adsShopItem.shopLocation)
                assertEquals(dummyItem.isSelected, adsShopItem.isFav)
            }

            viewModel.initialData.removeObserver(observer)
        }
    }

    @Test
    fun `when loadInitialData and use case return favorite shop with network error, network failed should be true`() {
        runBlocking {
            initialDataPageUseCase_returnDataFavoriteWithFavoriteShopWithNetworkError()
            val (networkFailedObserver, networkFailedUpdates) = observeLiveData(viewModel.isNetworkFailed)

            viewModel.loadInitialData()

            // There is 1 network failed update and its value is true
            assertTrue(networkFailedUpdates.size == 1)
            assertTrue(networkFailedUpdates[0])

            viewModel.isNetworkFailed.removeObserver(networkFailedObserver)
        }
    }

    @Test
    fun `when loadInitialData and use case return favorite shop with data not null and paging null, loading favorite shop should be false`() {
        runBlocking {
            val dataFavorite = DataFavorite(FavoriteShop(data = emptyList(), pagingModel = null), null)
            coEvery { getInitialDataPageUseCase.executeOnBackground() } returns dataFavorite

            val (isLoadingObserver, isLoadingUpdates) = observeLiveData(viewModel.isLoadingFavoriteShop)

            viewModel.loadInitialData()

            // There is 1 isLoadingFavoriteShop update and its value is false
            assertTrue(isLoadingUpdates.size == 1)
            assertFalse(isLoadingUpdates[0])

            viewModel.isLoadingFavoriteShop.removeObserver(isLoadingObserver)
        }
    }

    @Test
    fun `when loadInitialData and use case return favorite shop with item list, should return data with that list`() {
        runBlocking {
            val numOfItems = 2
            val favoriteShopItems = dummyFavoriteShopItemList(numOfItems)
            val favoriteShop = FavoriteShop(data = favoriteShopItems)
            val dataFavorite = DataFavorite(favoriteShop = favoriteShop)
            coEvery { getInitialDataPageUseCase.executeOnBackground() } returns dataFavorite

            val (observer, liveDataUpdates) = observeLiveData(viewModel.initialData)
            viewModel.loadInitialData()

            // There is 1 update
            assertTrue(liveDataUpdates.size == 1)

            // That update has correct number of items
            assertTrue(liveDataUpdates[0].size == 2)

            // That update has correct data
            for ((index, item) in liveDataUpdates[0].withIndex()) {
                assertTrue(item is FavoriteShopUiModel)

                val uiModel = item as FavoriteShopUiModel
                val favoriteShopItem = favoriteShopItems[index]
                assertEquals(favoriteShopItem.id, uiModel.shopId)
                assertEquals(favoriteShopItem.name, uiModel.shopName)
                assertEquals(favoriteShopItem.iconUri, uiModel.shopAvatarImageUrl)
                assertEquals(favoriteShopItem.location, uiModel.shopLocation)
                assertEquals(favoriteShopItem.isFav, uiModel.isFavoriteShop)
                assertEquals(favoriteShopItem.badgeUrl, uiModel.badgeUrl)
            }

            viewModel.initialData.removeObserver(observer)
        }
    }

    /**
     * end of loadInitialData section
     */

    /**
     * addFavoriteShop section
     */

    private fun mockViewForAddFavoriteShop(): View {
        val view = mockk<View>()
        every { view.clearAnimation() } returns Unit
        return view
    }

    @Test
    fun `when addFavoriteShop and error occurs then view clear animation should be called`() {
        runBlocking {
            every { toggleFavouriteShopUseCase.createObservable(any()) } answers { throw Exception() }
            val view = mockViewForAddFavoriteShop()
            viewModel.addFavoriteShop(view, TopAdsShopItem())
            verify(exactly = 1) { view.clearAnimation() }
        }
    }

    @Test
    fun `when addFavoriteShop and success then view clear animation should be called`() {
        runBlocking {
            every { toggleFavouriteShopUseCase.createObservable(any()) } returns Observable.just(true)
            val view = mockViewForAddFavoriteShop()
            viewModel.addFavoriteShop(view, TopAdsShopItem())
            verify(exactly = 1) { view.clearAnimation() }
        }
    }

    @Test
    fun `when addFavoriteShop and error occurs then isErrorAddFavoriteShop should be true`() {
        runBlocking {
            val view = mockViewForAddFavoriteShop()
            every { toggleFavouriteShopUseCase.createObservable(any()) } answers { throw Exception() }

            val (observer, updates) = observeLiveData(viewModel.isErrorAddFavoriteShop)
            viewModel.addFavoriteShop(view, TopAdsShopItem())

            assertTrue(updates.size == 1)
            assertTrue(updates[0])

            viewModel.isErrorAddFavoriteShop.removeObserver(observer)
        }
    }

    @Test
    fun `when addFavoriteShop and success, return correct data`() {
        runBlocking {
            val view = mockViewForAddFavoriteShop()
            every { toggleFavouriteShopUseCase.createObservable(any()) } returns Observable.just(true)

            val (addedFavoriteShopObserver, addedFavoriteShops) = observeLiveData(viewModel.addedFavoriteShop)
            val (impressionObserver, impressions) = observeLiveData(viewModel.favoriteShopImpression)

            val topAdsShopItem = TopAdsShopItem(
                    shopId = randomString(),
                    shopName = randomString(20),
                    shopImageUrl = randomString(50),
                    shopLocation = randomString(10),
                    isFav = true
            )
            viewModel.addFavoriteShop(view, topAdsShopItem)

            assertTrue(addedFavoriteShops.size == 1)
            assertTrue(impressions.size == 1)

            assertEquals(topAdsShopItem.shopId, addedFavoriteShops[0].shopId)
            assertEquals(topAdsShopItem.shopName, addedFavoriteShops[0].shopName)
            assertEquals(topAdsShopItem.shopImageUrl, addedFavoriteShops[0].shopAvatarImageUrl)
            assertEquals(topAdsShopItem.shopLocation, addedFavoriteShops[0].shopLocation)
            assertEquals(topAdsShopItem.isFav, addedFavoriteShops[0].isFavoriteShop)

            assertEquals(topAdsShopItem.shopClickUrl, impressions[0])

            viewModel.addedFavoriteShop.removeObserver(addedFavoriteShopObserver)
            viewModel.favoriteShopImpression.removeObserver(impressionObserver)
        }
    }

    /**
     * Load more favorite shop
     */

    @Test
    fun `when loadMoreFavoriteShop and pagingHandler's CheckNextPage return false, viewModel should do nothing`() {
        every { pagingHandler.CheckNextPage() } returns false
        val (observer, liveDataUpdates) = observeLiveData(viewModel.isLoadingFavoriteShop)

        viewModel.loadMoreFavoriteShop()

        assertTrue(liveDataUpdates.isEmpty())
        verify(exactly = 0) { pagingHandler.nextPage() }

        viewModel.isLoadingFavoriteShop.removeObserver(observer)
    }

    @Test
    fun `when loadMoreFavoriteShop, pagingHandler's CheckNextPage return true, and use case success, isLoadingFavoriteShop should be true then false`() {
        runBlocking {
            every { pagingHandler.CheckNextPage() } returns true
            every { pagingHandler.page } returns 1
            coEvery { getFavoriteShopUseCaseWithCoroutine.executeOnBackground() } returns FavoriteShop()

            val (observer, loadingFavoriteShopUpdates) = observeLiveData(viewModel.isLoadingFavoriteShop)
            viewModel.loadMoreFavoriteShop()

            assertTrue(loadingFavoriteShopUpdates.size == 2)
            assertTrue(loadingFavoriteShopUpdates[0])
            assertFalse(loadingFavoriteShopUpdates[1])

            viewModel.isLoadingFavoriteShop.removeObserver(observer)
        }
    }

    @Test
    fun `when loadMoreFavoriteShop, pagingHandler's CheckNextPage return true, and use case error, isLoadingFavoriteShop should be true then false`() {
        runBlocking {
            every { pagingHandler.CheckNextPage() } returns true
            every { pagingHandler.page } returns 1
            coEvery { getFavoriteShopUseCaseWithCoroutine.executeOnBackground() } coAnswers  { throw Exception() }

            val (observer, loadingFavoriteShopUpdates) = observeLiveData(viewModel.isLoadingFavoriteShop)
            viewModel.loadMoreFavoriteShop()

            assertTrue(loadingFavoriteShopUpdates.size == 2)
            assertTrue(loadingFavoriteShopUpdates[0])
            assertFalse(loadingFavoriteShopUpdates[1])

            viewModel.isLoadingFavoriteShop.removeObserver(observer)
        }
    }

    @Test
    fun `when loadMoreFavoriteShop, pagingHandler's CheckNextPage return true, currentPage is 1, and use case error, resetPage should be called`() {
        runBlocking {
            every { pagingHandler.CheckNextPage() } returns true
            every { pagingHandler.page } returns 1
            coEvery { getFavoriteShopUseCaseWithCoroutine.executeOnBackground() } coAnswers  { throw Exception() }

            viewModel.loadMoreFavoriteShop()
            verify(exactly = 1) { pagingHandler.resetPage() }
        }
    }

    @Test
    fun `when loadMoreFavoriteShop, currentPage is greater than 1, and use case error, should set page to currentPage - 1`() {
        runBlocking {
            every { pagingHandler.CheckNextPage() } returns true
            every { pagingHandler.page } returns 2
            coEvery { getFavoriteShopUseCaseWithCoroutine.executeOnBackground() } coAnswers  { throw Exception() }

            viewModel.loadMoreFavoriteShop()
            verify(exactly = 1) { pagingHandler.page = 1 }
            viewModel.isErrorLoadMore.observeForever {
                assertTrue(it)
            }
        }
    }

    @Test
    fun `when loadMoreFavoriteShop, success, and use case return favoriteShop with valid data, should call pagingHandler's setHasNext with correct parameter`() {
        runBlocking {
            every { pagingHandler.CheckNextPage() } returns true

            val pagingHandlerModel = PagingHandler.PagingHandlerModel()
            val uri = randomString(10)
            pagingHandlerModel.setUriNext(randomString(10))

            val favoriteShop = FavoriteShop(isDataValid = true, pagingModel = pagingHandlerModel)
            coEvery { getFavoriteShopUseCaseWithCoroutine.executeOnBackground() } returns favoriteShop

            viewModel.loadMoreFavoriteShop()
            verify(exactly = 1) { pagingHandler.setHasNext(PagingHandler.CheckHasNext(uri)) }
        }
    }

    @Test
    fun `when loadMoreFavoriteShop and use case return favoriteShop with valid data and pagingModel null, isLoadingFavoriteShop should be true then false`() {
        runBlocking {
            every { pagingHandler.CheckNextPage() } returns true
            val favoriteShop = FavoriteShop(isDataValid = true)
            coEvery { getFavoriteShopUseCaseWithCoroutine.executeOnBackground() } returns favoriteShop
            val (observer, isLoadingUpdates) = observeLiveData(viewModel.isLoadingFavoriteShop)

            viewModel.loadMoreFavoriteShop()

            assertTrue(isLoadingUpdates.size == 2)
            assertTrue(isLoadingUpdates[0])
            assertFalse(isLoadingUpdates[1])

            viewModel.isLoadingFavoriteShop.removeObserver(observer)
        }
    }

    @Test
    fun `when loadMoreFavoriteShop and use case return favoriteShop with valid data and pagingModel null, should call pagingHandler's setHasNext false`() {
        runBlocking {
            every { pagingHandler.CheckNextPage() } returns true
            val favoriteShop = FavoriteShop(isDataValid = true)
            coEvery { getFavoriteShopUseCaseWithCoroutine.executeOnBackground() } returns favoriteShop

            viewModel.loadMoreFavoriteShop()

            verify(exactly = 1) { pagingHandler.setHasNext(false) }
        }
    }

    @Test
    fun `when loadMoreFavoriteShop and use case return favoriteShop with valid data and null FavoriteShopItem list, moreDataFavoriteShop should updates with empty list`() {
        runBlocking {
            every { pagingHandler.CheckNextPage() } returns true
            val favoriteShop = FavoriteShop(isDataValid = true)
            coEvery { getFavoriteShopUseCaseWithCoroutine.executeOnBackground() } returns favoriteShop

            val (observer, updates) = observeLiveData(viewModel.moreDataFavoriteShop)
            viewModel.loadMoreFavoriteShop()

            assertTrue(updates.size == 1)
            assertTrue(updates[0].isEmpty())

            viewModel.moreDataFavoriteShop.removeObserver(observer)
        }
    }

    @Test
    fun `when loadMoreFavoriteShop and use case return favoriteShop with valid data and empty FavoriteShopItem list, moreDataFavoriteShop should updates with empty list`() {
        runBlocking {
            every { pagingHandler.CheckNextPage() } returns true
            val favoriteShop = FavoriteShop(isDataValid = true, data = emptyList())
            coEvery { getFavoriteShopUseCaseWithCoroutine.executeOnBackground() } returns favoriteShop

            val (observer, updates) = observeLiveData(viewModel.moreDataFavoriteShop)
            viewModel.loadMoreFavoriteShop()

            assertTrue(updates.size == 1)
            assertTrue(updates[0].isEmpty())

            viewModel.moreDataFavoriteShop.removeObserver(observer)
        }
    }

    @Test
    fun `when loadMoreFavoriteShop and use case return favoriteShop with valid data and FavoriteShopItem list, moreDataFavoriteShop should updates with correct list`() {
        runBlocking {
            every { pagingHandler.CheckNextPage() } returns true

            val numOfItems = 2
            val favoriteShop = FavoriteShop(isDataValid = true, data = dummyFavoriteShopItemList(numOfItems))
            coEvery { getFavoriteShopUseCaseWithCoroutine.executeOnBackground() } returns favoriteShop

            val (observer, updates) = observeLiveData(viewModel.moreDataFavoriteShop)
            viewModel.loadMoreFavoriteShop()

            assertTrue(updates.size == 1)
            assertTrue(updates[0].size == numOfItems)
            for (item in updates[0]) {
                assertTrue(item is FavoriteShopUiModel)
                if (item is FavoriteShopUiModel) {
                    assertTrue(item.isFavoriteShop)
                }
            }

            viewModel.moreDataFavoriteShop.removeObserver(observer)
        }
    }

    /**
     * Refresh all data
     */

    @Test
    fun `when refreshAllDataFavoritePage and use case error, refresh should be set to true then false`() {
        runBlocking {
            coEvery { getAllDataFavoriteUseCase.executeOnBackground() } coAnswers { throw Exception() }

            val (observer, updates) = observeLiveData(viewModel.refresh)
            viewModel.refreshAllDataFavoritePage()

            assertTrue(updates.size == 2)
            assertTrue(updates[0])
            assertFalse(updates[1])

            viewModel.refresh.removeObserver(observer)
        }
    }

    @Test
    fun `when refreshAllDataFavoritePage and use case success, refresh should be set to true then false`() {
        runBlocking {
            coEvery { getAllDataFavoriteUseCase.executeOnBackground() } returns DataFavorite()

            val (observer, updates) = observeLiveData(viewModel.refresh)
            viewModel.refreshAllDataFavoritePage()

            assertTrue(updates.size == 2)
            assertTrue(updates[0])
            assertFalse(updates[1])

            viewModel.refresh.removeObserver(observer)
        }
    }

    @Test
    fun `when refreshAllDataFavoritePage and use case success, should call pagingHandler's resetPage`() {
        runBlocking {
            coEvery { getAllDataFavoriteUseCase.executeOnBackground() } returns DataFavorite()

            viewModel.refreshAllDataFavoritePage()

            verify(exactly = 1) { pagingHandler.resetPage() }
        }
    }

    @Test
    fun `when refreshAllDataFavoritePage and use case success with topAdsShop with isNetworkError true, should update isTopAdsShopNetworkFailed to true`() {
        runBlocking {
            coEvery { getAllDataFavoriteUseCase.executeOnBackground() } returns DataFavorite(topAdsShop = TopAdsShop(isNetworkError = true))
            val (observer, updates) = observeLiveData(viewModel.isNetworkFailed)
            viewModel.refreshAllDataFavoritePage()
            assertTrue(updates.size == 1)
            assertTrue(updates[0])
            viewModel.isNetworkFailed.removeObserver(observer)
        }
    }

    @Test
    fun `when refreshAllDataFavoritePage and use case success with topAdsShop, should update refreshData with correct list`() {
        runBlocking {
            val numOfItems = 2
            val itemList = dummyTopAdsShopItemList(numOfItems)
            val topAdsShop = TopAdsShop(topAdsShopItemList = itemList)
            coEvery { getAllDataFavoriteUseCase.executeOnBackground() } returns DataFavorite(topAdsShop = topAdsShop)

            val (observer, updates) = observeLiveData(viewModel.refreshData)
            viewModel.refreshAllDataFavoritePage()

            assertTrue(updates.size == 1)
            assertTrue(updates[0].size == 1)
            assertTrue(updates[0][0] is TopAdsShopViewModel)

            val adsShopItems = (updates[0][0] as TopAdsShopViewModel).adsShopItems
            for ((index, item) in adsShopItems!!.withIndex()) {
                val dummyItem = itemList[index]
                assertEquals(item.shopId, dummyItem.shopId)
                assertEquals(item.shopDomain, dummyItem.shopDomain)
                assertEquals(item.shopName, dummyItem.shopName)
                assertEquals(item.adKey, dummyItem.adRefKey)
                assertEquals(item.shopClickUrl, dummyItem.shopClickUrl)
                assertEquals(item.shopCoverUrl, dummyItem.shopImageCover)
                assertEquals(item.shopCoverEcs, dummyItem.shopImageCoverEcs)
                assertEquals(item.shopImageUrl, dummyItem.shopImageUrl)
                assertEquals(item.shopImageEcs, dummyItem.shopImageEcs)
                assertEquals(item.shopLocation, dummyItem.shopLocation)
                assertEquals(item.isFav, dummyItem.isSelected)
            }

            viewModel.refreshData.removeObserver(observer)
        }
    }
    @Test
    fun `when refreshAllDataFavoritePage and use case success with favoriteShop, should update refreshData with correct list`() {
        runBlocking {
            val numOfItems = 2
            val dummyList = dummyFavoriteShopItemList(numOfItems)
            val dummyFavoriteShop = FavoriteShop(data = dummyList)
            coEvery { getAllDataFavoriteUseCase.executeOnBackground() } returns DataFavorite(favoriteShop = dummyFavoriteShop)

            val (observer, updates) = observeLiveData(viewModel.refreshData)
            viewModel.refreshAllDataFavoritePage()

            assertTrue(updates.size == 1)
            assertTrue(updates[0].size == numOfItems)

            for ((index, item) in updates[0].withIndex()) {
                assertTrue(item is FavoriteShopUiModel)
                val dummyItem = dummyList[index]
                if (item is FavoriteShopUiModel) {
                    assertEquals(item.shopId, dummyItem.id)
                    assertEquals(item.shopName, dummyItem.name)
                    assertEquals(item.shopAvatarImageUrl, dummyItem.iconUri)
                    assertEquals(item.shopLocation, dummyItem.location)
                    assertEquals(item.isFavoriteShop, dummyItem.isFav)
                    assertEquals(item.badgeUrl, dummyItem.badgeUrl)
                }
            }

            viewModel.refreshData.removeObserver(observer)
        }
    }

    private fun initialDataPageUseCase_returnEmptyDataFavorite() {
        coEvery { getInitialDataPageUseCase.executeOnBackground() } returns DataFavorite()
    }

    private fun initialDataPageUseCase_returnDataFavoriteWithTopAdsShopWithNetworkError() {
        val dataFavorite = DataFavorite(null, TopAdsShop(isNetworkError = true))
        coEvery { getInitialDataPageUseCase.executeOnBackground() } returns dataFavorite
    }

    private fun initialDataPageUseCase_returnDataFavoriteWithFavoriteShopWithNetworkError() {
        val dataFavorite = DataFavorite(FavoriteShop(isNetworkError = true), null)
        coEvery { getInitialDataPageUseCase.executeOnBackground() } returns dataFavorite
    }

    private fun <T> observeLiveData(liveData: LiveData<T>): Pair<Observer<T>, ArrayList<T>> {
        val updates = ArrayList<T>()
        val observer: Observer<T> = Observer {
            updates.add(it)
        }
        liveData.observeForever(observer)
        return Pair(observer, updates)
    }

    private fun initialDataPageUseCase_throwException() {
        coEvery { getInitialDataPageUseCase.executeOnBackground() } coAnswers { throw Exception() }
    }

    private fun observeIsErrorLoad_return(isError:Boolean){
        viewModel.isErrorLoad.observeForever {
            assert(it == isError)
        }
    }
}
