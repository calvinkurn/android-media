package com.tokopedia.search.result.shop.presentation.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.search.result.common.EmptySearchCreator
import com.tokopedia.search.result.common.State
import com.tokopedia.search.result.domain.usecase.SearchUseCase
import com.tokopedia.search.result.presentation.model.EmptySearchViewModel
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.mapper.ShopHeaderViewModelMapper
import com.tokopedia.search.result.shop.presentation.mapper.ShopViewModelMapper
import com.tokopedia.search.result.shop.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchShopViewModelMockKTest {

    @Test
    fun `test search shop successful`() {
        `given search shop API call will be successful and return search shop data`()

        `when execute search shop`()

        `then assert search shop state is success and contains search shop data`()
    }

    @Test
    fun `test search shop error`() {
        `given search shop API call will fail`()

        `when execute search shop`()

        `then assert search shop state is error with no data`()
    }

    @Test
    fun `test search shop and search more shop successful`() {
        `given search shop and search more shop API call will be successful and return search shop data`()

        `when execute search shop, then search more shop`()

        `then assert search shop state is success and contains data from search shop and search more shop`()
    }

    @Test
    fun `test search shop successful, but search more shop error`() {
        `given search shop API call will be successful, but search more shop API call will fail`()

        `when execute search shop, then search more shop`()

        `then assert search shop state is error, but still contains data from search shop`()
    }

    @Test
    fun `test search shop twice`() {
        `given search shop API call will be successful and return search shop data`()

        `when execute search shop twice`()

        `then assert search shop state is success and contains search shop data from first API call`()
    }

    @Test
    fun `test search more shop but without next page after search shop`() {
        `given search shop API call will return data with has next page is false`()

        `when execute search shop, then search more shop`()

        `then assert search shop state is success, but only have search shop data`()
    }

    @Test
    fun `test search more shop but without next page after search more shop`() {
        `given search more shop API will return data with has next page is false`()

        `when execute search shop, search more shop, and then search more shop again`()

        `then assert search shop state is success, without data from second search more shop API call`()
    }

    @Test
    fun `test retry search shop after error in search shop`() {
        `given search more shop API will fail first, and then success`()

        `when execute search shop, and then retry`()

        `then assert search shop success after retry`()
    }

    @Test
    fun `test retry search shop after error in search more shop`() {
        `given search more shop API will success, and search more shop API will fail first, and then success`()

        `when execute search shop, search more shop, and then retry`()

        `then assert search more shop success after retry`()
    }

    @Test
    fun `test reload search shop`() {
        `given search shop API call will be successful and return search shop data`()

        `when reload search shop`()

        `then assert search shop state is success and contains search shop data`()
    }

    @Test
    fun `test reload search shop after search shop and search more shop`() {
        `given search shop and search more shop API call will be successful and return search shop data`()

        `when search shop, search more shop, and then reload`()

        `then assert reload search shop success and search shop state contains search shop data`()
    }

    @Test
    fun `test empty result search shop`() {
        `given search shop API will be successful and return empty search shop list`()

        `when execute search shop`()

        `then assert search shop state is success and contains empty search data`()
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val pagingWithNextPage = SearchShopModel.AceSearchShop.Paging(uriNext = "Some random string indicating has next page")
    private val pagingWithoutNextPage = SearchShopModel.AceSearchShop.Paging(uriNext = "")

    private val shopItemList = mutableListOf<SearchShopModel.AceSearchShop.ShopItem>().also {
        it.add(SearchShopModel.AceSearchShop.ShopItem(id = "1"))
        it.add(SearchShopModel.AceSearchShop.ShopItem(id = "2"))
        it.add(SearchShopModel.AceSearchShop.ShopItem(id = "3"))
        it.add(SearchShopModel.AceSearchShop.ShopItem(id = "4"))
    }

    private val moreShopItemList = mutableListOf<SearchShopModel.AceSearchShop.ShopItem>().also {
        it.add(SearchShopModel.AceSearchShop.ShopItem(id = "5"))
        it.add(SearchShopModel.AceSearchShop.ShopItem(id = "6"))
    }

    private val aceSearchShopWithNextPage = SearchShopModel.AceSearchShop(
            paging = pagingWithNextPage,
            shopList = shopItemList
    )

    private val aceSearchShopWithoutNextPage = SearchShopModel.AceSearchShop(
            paging = pagingWithoutNextPage,
            shopList = shopItemList
    )

    private val moreAceSearchShopWithNextPage = SearchShopModel.AceSearchShop(
            paging = pagingWithNextPage,
            shopList = moreShopItemList
    )

    private val moreAceSearchShopWithoutNextPage = SearchShopModel.AceSearchShop(
            paging = pagingWithoutNextPage,
            shopList = moreShopItemList
    )

    private val searchShopModel = SearchShopModel(aceSearchShopWithNextPage)
    private val searchShopModelWithoutNextPage = SearchShopModel(aceSearchShopWithoutNextPage)
    private val searchShopModelEmptyList = SearchShopModel()
    private val searchMoreShopModel = SearchShopModel(moreAceSearchShopWithNextPage)
    private val searchMoreShopModelWithoutNextPage = SearchShopModel(moreAceSearchShopWithoutNextPage)

    private val searchShopUseCase = mockk<SearchUseCase<SearchShopModel>>(relaxed = true)
    private val searchMoreShopUseCase = mockk<SearchUseCase<SearchShopModel>>(relaxed = true)

    private val searchShopParameter = mapOf(
            SearchApiConst.Q to "samsung"
    )

    private val shopHeaderViewModelMapper = ShopHeaderViewModelMapper()
    private val shopViewModelMapper = ShopViewModelMapper()

    private val emptySearchCreator = mockk<EmptySearchCreator>(relaxed = true)

    private val userSession = mockk<UserSessionInterface>().also {
        every { it.isLoggedIn }.returns(true)
        every { it.userId }.returns("123456")
    }

    private val localCacheHandler = mockk<LocalCacheHandler>().also {
        every { it.getString(eq(SearchConstant.GCM_ID), "") }.returns("MOCK_GCM_ID")
    }

    private val searchShopViewModel = SearchShopViewModel(
            Dispatchers.Unconfined,
            searchShopParameter,
            searchShopUseCase,
            searchMoreShopUseCase,
            shopHeaderViewModelMapper,
            shopViewModelMapper,
            emptySearchCreator,
            userSession,
            localCacheHandler
    )

    private fun `given search shop API call will be successful and return search shop data`() {
        coEvery { searchShopUseCase.executeOnBackground() }.returns(searchShopModel)
    }

    private fun `when execute search shop`() {
        searchShopViewModel.searchShop()
    }

    private fun `then assert search shop state is success and contains search shop data`() {
        assertUseCaseExecutionCount(searchShopUseCase, 1)

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopData(searchShopState)
    }

    private fun assertUseCaseExecutionCount(useCase: SearchUseCase<SearchShopModel>, count: Int) {
        coVerify(exactly = count) { useCase.executeOnBackground() }
    }

    private fun assertSuccessSearchShopData(state: State<List<Visitable<*>>>?) {
        if(state != null) {
            assertStateIsSuccess(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertLoadingMoreAtLastIndexOfData(state.data!!)
            assertShopItemBetweenFirstAndLastIndexOfData(state.data!!)
            assertShopItemSize(state.data!!, shopItemList.size)
        }
        else {
            assert(false) {
                "State is null"
            }
        }
    }

    private fun assertStateIsSuccess(state: State<List<Visitable<*>>>?) {
        assert(state is State.Success) {
            "$state is not State.Success"
        }
    }

    private fun assertDataIsNotEmpty(data: List<Visitable<*>>?) {
        assert(!data.isNullOrEmpty()) {
            "Data is null or empty."
        }
    }

    private fun assertShopHeaderAtFirstIndexOfData(data: List<Visitable<*>>) {
        assert(data.first() is ShopHeaderViewModel) {
            "First element ${data.first().javaClass.kotlin.qualifiedName} is not of type ShopHeaderViewModel"
        }
    }

    private fun assertLoadingMoreAtLastIndexOfData(data: List<Visitable<*>>) {
        assert(data.last() is LoadingMoreModel) {
            "Last element ${data.last().javaClass.kotlin.qualifiedName} is not of type LoadingMoreModel"
        }
    }

    private fun assertShopItemBetweenFirstAndLastIndexOfData(data: List<Visitable<*>>) {
        for(i in 1 until data.size - 1) {
            assertShopItemType(i, data[i])
            assertShopItemPosition(i, data[i] as ShopViewModel.ShopItem)
        }
    }

    private fun assertShopItemType(index: Int, shopItem: Visitable<*>) {
        assert(shopItem is ShopViewModel.ShopItem) {
            "Element $index ${shopItem.javaClass.kotlin.qualifiedName} is not of type ShopViewModel.ShopItem"
        }
    }

    private fun assertShopItemPosition(index: Int, shopItem: ShopViewModel.ShopItem) {
        assert(shopItem.position == index) {
            "Element $index of ShopViewModel.ShopItem position is ${shopItem.position}, expected $index"
        }
    }

    private fun assertShopItemSize(data: List<Visitable<*>>, expectedShopItemCount: Int) {
        val actualShopItemCount = data.count { it is ShopViewModel.ShopItem }

        assert(actualShopItemCount == expectedShopItemCount) {
            "Actual shop item count is $actualShopItemCount, expected shop item count is $expectedShopItemCount"
        }
    }

    private fun `given search shop API call will fail`() {
        val exception = Exception("Mock exception for testing error")
        coEvery { searchShopUseCase.executeOnBackground() }.throws(exception)
    }

    private fun `then assert search shop state is error with no data`() {
        assertUseCaseExecutionCount(searchShopUseCase, 1)

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertErrorSearchShopData(searchShopState)
    }

    private fun assertErrorSearchShopData(state: State<List<Visitable<*>>>?) {
        if(state != null) {
            assertStateIsError(state)
            assertDataIsNull(state.data)
        }
        else {
            assert(false) {
                "State is null"
            }
        }
    }

    private fun assertStateIsError(state: State<List<Visitable<*>>>?) {
        assert(state is State.Error) {
            "$state is not State.Error"
        }
    }

    private fun assertDataIsNull(data: List<Visitable<*>>?) {
        assert(data.isNullOrEmpty()) {
            "Data is not null and not empty."
        }
    }

    private fun `given search shop and search more shop API call will be successful and return search shop data`() {
        coEvery { searchShopUseCase.executeOnBackground() }.returns(searchShopModel)
        coEvery { searchMoreShopUseCase.executeOnBackground() }.returns(searchMoreShopModel)
    }

    private fun `when execute search shop, then search more shop`() {
        searchShopViewModel.searchShop()
        searchShopViewModel.searchMoreShop()
    }

    private fun `then assert search shop state is success and contains data from search shop and search more shop`() {
        assertUseCaseExecutionCount(searchShopUseCase, 1)
        assertUseCaseExecutionCount(searchMoreShopUseCase, 1)

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchMoreShopData(searchShopState)
    }

    private fun assertSuccessSearchMoreShopData(state: State<List<Visitable<*>>>?) {
        if(state != null) {
            assertStateIsSuccess(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertLoadingMoreAtLastIndexOfData(state.data!!)
            assertShopItemBetweenFirstAndLastIndexOfData(state.data!!)
            assertShopItemSize(state.data!!, shopItemList.size + moreShopItemList.size)
        }
        else {
            assert(false) {
                "State is null"
            }
        }
    }

    private fun `given search shop API call will be successful, but search more shop API call will fail`() {
        val exception = Exception("Mock exception for testing error")
        coEvery { searchShopUseCase.executeOnBackground() }.returns(searchShopModel)
        coEvery { searchMoreShopUseCase.executeOnBackground() }.throws(exception)
    }

    private fun `then assert search shop state is error, but still contains data from search shop`() {
        assertUseCaseExecutionCount(searchShopUseCase, 1)
        assertUseCaseExecutionCount(searchMoreShopUseCase, 1)

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertErrorSearchMoreShopData(searchShopState)
    }

    private fun assertErrorSearchMoreShopData(state: State<List<Visitable<*>>>?) {
        if (state != null) {
            assertStateIsError(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertLoadingMoreAtLastIndexOfData(state.data!!)
            assertShopItemBetweenFirstAndLastIndexOfData(state.data!!)
            assertShopItemSize(state.data!!, shopItemList.size)
        }
        else {
            assert (false) {
                "State is null"
            }
        }
    }

    private fun assertShopItemFromSecondToLastIndexOfData(data: List<Visitable<*>>) {
        for(i in 1 until data.size) {
            assertShopItemType(i, data[i])
            assertShopItemPosition(i, data[i] as ShopViewModel.ShopItem)
        }
    }

    private fun `when execute search shop twice`() {
        searchShopViewModel.searchShop()
        searchShopViewModel.searchShop()
    }

    private fun `then assert search shop state is success and contains search shop data from first API call`() {
        assertUseCaseExecutionCount(searchShopUseCase, 1)

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopDataOnlyOnce(searchShopState)
    }

    private fun assertSuccessSearchShopDataOnlyOnce(state: State<List<Visitable<*>>>?) {
        if(state != null) {
            assertStateIsSuccess(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertLoadingMoreAtLastIndexOfData(state.data!!)
            assertShopItemBetweenFirstAndLastIndexOfData(state.data!!)
            assertShopItemSize(state.data!!, shopItemList.size)
        }
        else {
            assert(false) {
                "State is null"
            }
        }
    }

    private fun `given search shop API call will return data with has next page is false`() {
        coEvery { searchShopUseCase.executeOnBackground() }.returns(searchShopModelWithoutNextPage)
    }

    private fun `then assert search shop state is success, but only have search shop data`() {
        assertUseCaseExecutionCount(searchShopUseCase, 1)
        assertUseCaseExecutionCount(searchMoreShopUseCase, 0)

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopWithoutNextPage(searchShopState)
    }

    private fun assertSuccessSearchShopWithoutNextPage(state: State<List<Visitable<*>>>?) {
        if (state != null) {
            assertStateIsSuccess(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertShopItemFromSecondToLastIndexOfData(state.data!!)
            assertShopItemSize(state.data!!, shopItemList.size)
        }
        else {
            assert (false) {
                "State is null"
            }
        }
    }

    private fun `given search more shop API will return data with has next page is false`() {
        coEvery { searchShopUseCase.executeOnBackground() }.returns(searchShopModel)
        coEvery { searchMoreShopUseCase.executeOnBackground() }
                .returns(searchMoreShopModelWithoutNextPage)
                .andThen(SearchShopModel(SearchShopModel.AceSearchShop(shopList = moreShopItemList.map { it.copy() }.toList())))
    }

    private fun `when execute search shop, search more shop, and then search more shop again`() {
        searchShopViewModel.searchShop()
        searchShopViewModel.searchMoreShop()
        searchShopViewModel.searchMoreShop()
    }

    private fun `then assert search shop state is success, without data from second search more shop API call`() {
        assertUseCaseExecutionCount(searchShopUseCase, 1)
        assertUseCaseExecutionCount(searchMoreShopUseCase, 1)

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchMoreShopWithoutNextPage(searchShopState)
    }

    private fun assertSuccessSearchMoreShopWithoutNextPage(state: State<List<Visitable<*>>>?) {
        if (state != null) {
            assertStateIsSuccess(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertShopItemFromSecondToLastIndexOfData(state.data!!)
            assertShopItemSize(state.data!!, shopItemList.size + moreShopItemList.size)
        }
        else {
            assert (false) {
                "State is null"
            }
        }
    }

    private fun `given search more shop API will fail first, and then success`() {
        val exception = Exception("Mock exception for testing retry mechanism")

        coEvery { searchShopUseCase.executeOnBackground() }
                .throws(exception)
                .andThen(searchShopModel)
    }

    private fun `when execute search shop, and then retry`() {
        searchShopViewModel.searchShop()
        searchShopViewModel.retrySearchShop()
    }

    private fun `then assert search shop success after retry`() {
        assertUseCaseExecutionCount(searchShopUseCase, 2)

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopData(searchShopState)
    }

    private fun `given search more shop API will success, and search more shop API will fail first, and then success`() {
        val exception = Exception("Mock exception for testing retry mechanism")

        coEvery { searchShopUseCase.executeOnBackground() }.returns(searchShopModel)
        coEvery { searchMoreShopUseCase.executeOnBackground() }
                .throws(exception)
                .andThen(searchMoreShopModel)
    }

    private fun `when execute search shop, search more shop, and then retry`() {
        searchShopViewModel.searchShop()
        searchShopViewModel.searchMoreShop()
        searchShopViewModel.retrySearchShop()
    }

    private fun `then assert search more shop success after retry`() {
        assertUseCaseExecutionCount(searchShopUseCase, 1)
        assertUseCaseExecutionCount(searchMoreShopUseCase, 2)

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchMoreShopData(searchShopState)
    }

    private fun `when reload search shop`() {
        searchShopViewModel.reloadSearchShop()
    }

    private fun `when search shop, search more shop, and then reload`() {
        searchShopViewModel.searchShop()
        searchShopViewModel.searchMoreShop()
        searchShopViewModel.reloadSearchShop()
    }

    private fun `then assert reload search shop success and search shop state contains search shop data`() {
        assertUseCaseExecutionCount(searchShopUseCase, 2)
        assertUseCaseExecutionCount(searchMoreShopUseCase, 1)

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessReloadSearchShopData(searchShopState)
    }

    private fun assertSuccessReloadSearchShopData(state: State<List<Visitable<*>>>?) {
        if(state != null) {
            assertStateIsSuccess(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertLoadingMoreAtLastIndexOfData(state.data!!)
            assertShopItemBetweenFirstAndLastIndexOfData(state.data!!)
            assertShopItemSize(state.data!!, shopItemList.size)
        }
        else {
            assert(false) {
                "State is null"
            }
        }
    }

    private fun `given search shop API will be successful and return empty search shop list`() {
        coEvery { searchShopUseCase.executeOnBackground() }.returns(searchShopModelEmptyList)
    }

    private fun `then assert search shop state is success and contains empty search data`() {
        assertUseCaseExecutionCount(searchShopUseCase, 1)

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopDataEmpty(searchShopState)
    }

    private fun assertSuccessSearchShopDataEmpty(state: State<List<Visitable<*>>>?) {
        if(state != null) {
            assertStateIsSuccess(state)
            assertDataIsNotEmpty(state.data)
            assertDataOnlyContainsEmptySearchViewModel(state.data!!)
        }
        else {
            assert(false) {
                "State is null"
            }
        }
    }

    private fun assertDataOnlyContainsEmptySearchViewModel(data: List<Visitable<*>>) {
        assertDataSizeForEmptySearch(data)
        assertEmptySearchData(data)
    }

    private fun assertDataSizeForEmptySearch(data: List<Visitable<*>>) {
        assert(data.size == 1) {
            "Data should only contain 1 instance of EmptySearchViewModel. Actual data size: ${data.size}"
        }
    }

    private fun assertEmptySearchData(data: List<Visitable<*>>) {
        assert(data.first() is EmptySearchViewModel) {
            "Element ${data.first().javaClass.kotlin.qualifiedName} is not of type EmptySearchViewModel"
        }
    }

    @After
    fun tearDown() {

    }
}