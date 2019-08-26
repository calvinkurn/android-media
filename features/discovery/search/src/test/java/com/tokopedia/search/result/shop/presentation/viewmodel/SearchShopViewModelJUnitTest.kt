package com.tokopedia.search.result.shop.presentation.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.data.DynamicFilterModel
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.search.result.common.EmptySearchCreator
import com.tokopedia.search.result.common.State
import com.tokopedia.search.result.domain.usecase.TestErrorSearchUseCase
import com.tokopedia.search.result.domain.usecase.TestSearchUseCase
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as whenever

@ExperimentalCoroutinesApi
class SearchShopViewModelJUnitTest {

    @Test
    fun `test search shop successful`() {
        `given search shop API call will be successful and return search shop data`()

        `when execute search shop`()

        `then assert search shop state is success with data contains search shop header and shop items`()
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

        `then assert search shop state is success with data from both search shop and search more shop`()
    }

    @Test
    fun `test search shop successful, but search more shop error`() {
        `given search shop API call will be successful, but search more shop API call will fail`()

        `when execute search shop, then search more shop`()

        `then assert search shop state is error, but still contains data from search shop`()
    }

    @Test
    fun `test search shop twice`() {
        `given search shop API call will return different values between first and second call`()

        `when execute search shop twice`()

        `then assert search shop state is success but only have data from the first search shop API call`()
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

        `then assert search shop state is success, without data from last search more shop API call`()
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private abstract class MockShopHeaderViewModelMapper : Mapper<SearchShopModel, ShopHeaderViewModel>
    private abstract class MockShopViewModelMapper : Mapper<SearchShopModel, ShopViewModel>

    private val aceSearchShopWithNextPage = SearchShopModel.AceSearchShop(
            paging = SearchShopModel.AceSearchShop.Paging(uriNext = "Some random string indicating has next page")
    )
    private val aceSearchShopWithoutNextPage = SearchShopModel.AceSearchShop(
            paging = SearchShopModel.AceSearchShop.Paging(uriNext = "")
    )
    private val searchShopModel = SearchShopModel(aceSearchShopWithNextPage)
    private val searchMoreShopModel = SearchShopModel(aceSearchShopWithNextPage)
    private val searchShopModelWithoutNextPage = SearchShopModel(aceSearchShopWithoutNextPage)
    private val searchMoreShopModelWithoutNextPage = SearchShopModel(aceSearchShopWithoutNextPage)

    private val shopHeaderViewModel = ShopHeaderViewModel()
    private val shopItemViewModelList = mutableListOf<ShopViewModel.ShopItem>().also {
        it.add(ShopViewModel.ShopItem())
        it.add(ShopViewModel.ShopItem())
        it.add(ShopViewModel.ShopItem())
        it.add(ShopViewModel.ShopItem())
    }

    private val moreShopItemViewModelList = mutableListOf<ShopViewModel.ShopItem>().also {
        it.add(ShopViewModel.ShopItem())
        it.add(ShopViewModel.ShopItem())
    }

    private val shopViewModel = ShopViewModel(
            shopItemList = shopItemViewModelList
    )

    private val moreShopViewModel = ShopViewModel(
            shopItemList = moreShopItemViewModelList
    )

    private val dynamicFilterModel = DynamicFilterModel()

    private val searchShopParameter = mapOf(
            SearchApiConst.Q to "samsung"
    )

    private val shopHeaderViewModelMapper = mock(MockShopHeaderViewModelMapper::class.java)

    private val shopViewModelMapper = mock(MockShopViewModelMapper::class.java)

    private val emptySearchCreator = mock(EmptySearchCreator::class.java)

    private val userSession = mock(UserSessionInterface::class.java).also {
        whenever(it.isLoggedIn).thenReturn(true)
        whenever(it.userId).thenReturn("123456")
    }

    private val localCacheHandler = mock(LocalCacheHandler::class.java).also {
        whenever(it.getString(SearchConstant.GCM_ID, "")).thenReturn("MOCK_GCM_ID")
    }

    private lateinit var searchShopViewModel: SearchShopViewModel

    private fun `given search shop API call will be successful and return search shop data`() {
        whenever(shopHeaderViewModelMapper.convert(searchShopModel)).thenReturn(shopHeaderViewModel)
        whenever(shopViewModelMapper.convert(searchShopModel)).thenReturn(shopViewModel)

        searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModel),
                TestSearchUseCase(searchMoreShopModel),
                TestSearchUseCase(dynamicFilterModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }

    private fun `when execute search shop`() {
        searchShopViewModel.searchShop()
    }

    private fun `then assert search shop state is success with data contains search shop header and shop items`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopData(searchShopState)
    }

    private fun assertSuccessSearchShopData(state: State<List<Visitable<*>>>?) {
        if(state != null) {
            assertStateIsSuccess(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertShopItemAtSecondIndexAndAboveOfData(state.data!!)
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
        assert(data[0] is ShopHeaderViewModel) {
            "First element ${data[0].javaClass.kotlin.qualifiedName} is not of type ShopHeaderViewModel"
        }
    }

    private fun assertShopItemAtSecondIndexAndAboveOfData(data: List<Visitable<*>>) {
        for(i in 1 until data.size) {
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

    private fun `given search shop API call will fail`() {
        val exception = Exception("Mock exception for testing error")
        searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestErrorSearchUseCase(exception),
                TestSearchUseCase(searchMoreShopModel),
                TestSearchUseCase(dynamicFilterModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }

    private fun `then assert search shop state is error with no data`() {
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
        whenever(shopHeaderViewModelMapper.convert(searchShopModel)).thenReturn(shopHeaderViewModel)
        whenever(shopViewModelMapper.convert(searchShopModel)).thenReturn(shopViewModel)
        whenever(shopViewModelMapper.convert(searchMoreShopModel)).thenReturn(moreShopViewModel)

        searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModel),
                TestSearchUseCase(searchMoreShopModel),
                TestSearchUseCase(dynamicFilterModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }

    private fun `when execute search shop, then search more shop`() {
        searchShopViewModel.searchShop()
        searchShopViewModel.searchMoreShop()
    }

    private fun `then assert search shop state is success with data from both search shop and search more shop`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchMoreShopData(searchShopState)
    }

    private fun assertSuccessSearchMoreShopData(state: State<List<Visitable<*>>>?) {
        if(state != null) {
            assertStateIsSuccess(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertShopItemAtSecondIndexAndAboveOfData(state.data!!)
            assertShopItemSize(state.data!!, shopViewModel.shopItemList.size + moreShopViewModel.shopItemList.size)
        }
        else {
            assert(false) {
                "State is null"
            }
        }
    }

    private fun `given search shop API call will be successful, but search more shop API call will fail`() {
        whenever(shopHeaderViewModelMapper.convert(searchShopModel)).thenReturn(shopHeaderViewModel)
        whenever(shopViewModelMapper.convert(searchShopModel)).thenReturn(shopViewModel)

        val exception = Exception("Mock exception for testing error")
        searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModel),
                TestErrorSearchUseCase(exception),
                TestSearchUseCase(dynamicFilterModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }

    private fun `then assert search shop state is error, but still contains data from search shop`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertErrorSearchMoreShopData(searchShopState)
    }

    private fun assertErrorSearchMoreShopData(state: State<List<Visitable<*>>>?) {
        if (state != null) {
            assertStateIsError(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertShopItemAtSecondIndexAndAboveOfData(state.data!!)
            assertShopItemSize(state.data!!, shopItemViewModelList.size)
        }
        else {
            assert (false) {
                "State is null"
            }
        }
    }

    private fun assertShopItemSize(data: List<Visitable<*>>, expectedShopItemCount: Int) {
        val actualShopItemCount = data.count { it is ShopViewModel.ShopItem }

        assert(actualShopItemCount == expectedShopItemCount) {
            "Actual shop item count is $actualShopItemCount, expected shop item count is $expectedShopItemCount"
        }
    }

    private fun `given search shop API call will return different values between first and second call`() {
        whenever(shopHeaderViewModelMapper.convert(searchShopModel))
                .thenReturn(shopHeaderViewModel)
                .thenReturn(ShopHeaderViewModel())

        whenever(shopViewModelMapper.convert(searchShopModel))
                .thenReturn(shopViewModel)
                .thenReturn(ShopViewModel())

        searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModel),
                TestSearchUseCase(searchMoreShopModel),
                TestSearchUseCase(dynamicFilterModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }

    private fun `when execute search shop twice`() {
        searchShopViewModel.searchShop()
        searchShopViewModel.searchShop()
    }

    private fun `then assert search shop state is success but only have data from the first search shop API call`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopDataOnlyOnce(searchShopState)
    }

    private fun assertSuccessSearchShopDataOnlyOnce(state: State<List<Visitable<*>>>?) {
        if(state != null) {
            assertStateIsSuccess(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertShopItemAtSecondIndexAndAboveOfData(state.data!!)
            assertShopItemSize(state.data!!, shopItemViewModelList.size)
        }
        else {
            assert(false) {
                "State is null"
            }
        }
    }

    private fun `given search shop API call will return data with has next page is false`() {
        whenever(shopHeaderViewModelMapper.convert(searchShopModelWithoutNextPage)).thenReturn(shopHeaderViewModel)
        whenever(shopViewModelMapper.convert(searchShopModelWithoutNextPage)).thenReturn(shopViewModel)
        whenever(shopViewModelMapper.convert(searchMoreShopModel)).thenReturn(moreShopViewModel)

        searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModelWithoutNextPage),
                TestSearchUseCase(searchMoreShopModel),
                TestSearchUseCase(dynamicFilterModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }

    private fun `then assert search shop state is success, but only have search shop data`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopWithoutNextPage(searchShopState)
    }

    private fun assertSuccessSearchShopWithoutNextPage(state: State<List<Visitable<*>>>?) {
        if (state != null) {
            assertStateIsSuccess(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertShopItemAtSecondIndexAndAboveOfData(state.data!!)
            assertShopItemSize(state.data!!, shopItemViewModelList.size)
        }
        else {
            assert (false) {
                "State is null"
            }
        }
    }

    private fun `given search more shop API will return data with has next page is false`() {
        whenever(shopHeaderViewModelMapper.convert(searchShopModel)).thenReturn(shopHeaderViewModel)
        whenever(shopViewModelMapper.convert(searchShopModel)).thenReturn(shopViewModel)
        whenever(shopViewModelMapper.convert(searchMoreShopModelWithoutNextPage))
                .thenReturn(moreShopViewModel)
                .thenReturn(ShopViewModel(shopItemList = moreShopItemViewModelList.map { it.copy() }.toList()))

        searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModel),
                TestSearchUseCase(searchMoreShopModelWithoutNextPage),
                TestSearchUseCase(dynamicFilterModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }

    private fun `when execute search shop, search more shop, and then search more shop again`() {
        searchShopViewModel.searchShop()
        searchShopViewModel.searchMoreShop()
        searchShopViewModel.searchMoreShop()
    }

    private fun `then assert search shop state is success, without data from last search more shop API call`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchMoreShopWithoutNextPage(searchShopState)
    }

    private fun assertSuccessSearchMoreShopWithoutNextPage(state: State<List<Visitable<*>>>?) {
        if (state != null) {
            val expectedShopItemCount = shopItemViewModelList.size + moreShopItemViewModelList.size

            assertStateIsSuccess(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertShopItemAtSecondIndexAndAboveOfData(state.data!!)
            assertShopItemSize(state.data!!, expectedShopItemCount)
        }
        else {
            assert (false) {
                "State is null"
            }
        }
    }

    @After
    fun tearDown() {

    }
}