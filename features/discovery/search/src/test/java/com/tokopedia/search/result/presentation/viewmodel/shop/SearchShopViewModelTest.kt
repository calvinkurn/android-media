package com.tokopedia.search.result.presentation.viewmodel.shop

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.domain.usecase.TestErrorSearchUseCase
import com.tokopedia.search.result.domain.usecase.TestSearchUseCase
import com.tokopedia.search.result.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.presentation.model.ShopViewModel
import com.tokopedia.search.result.presentation.viewmodel.State
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as whenever

@ExperimentalCoroutinesApi
class SearchShopViewModelTest {

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
        it.add(ShopViewModel.ShopItem())
        it.add(ShopViewModel.ShopItem())
    }

    private val shopViewModel = ShopViewModel(
            shopItemList = shopItemViewModelList
    )

    private val moreShopViewModel = ShopViewModel(
            shopItemList = moreShopItemViewModelList
    )

    private val searchShopParameter = mapOf(
            SearchApiConst.Q to "samsung"
    )

    private val shopHeaderViewModelMapper = mock(MockShopHeaderViewModelMapper::class.java).also {
        whenever(it.convert(searchShopModel)).thenReturn(shopHeaderViewModel)
        whenever(it.convert(searchShopModelWithoutNextPage)).thenReturn(shopHeaderViewModel)
    }

    private val shopViewModelMapper = mock(MockShopViewModelMapper::class.java).also {
        whenever(it.convert(searchShopModel)).thenReturn(shopViewModel)
        whenever(it.convert(searchShopModelWithoutNextPage)).thenReturn(shopViewModel)
        whenever(it.convert(searchMoreShopModel)).thenReturn(moreShopViewModel)
        whenever(it.convert(searchMoreShopModelWithoutNextPage)).thenReturn(moreShopViewModel)
    }

    private val userSession = mock(UserSessionInterface::class.java).also {
        whenever(it.isLoggedIn).thenReturn(true)
        whenever(it.userId).thenReturn("123456")
    }

    private val localCacheHandler = mock(LocalCacheHandler::class.java).also {
        whenever(it.getString(SearchConstant.GCM_ID, "")).thenReturn("MOCK_GCM_ID")
    }

    @Test
    fun `when searchShop() is successful, validate data is correct`() {
        val searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModel),
                TestSearchUseCase(searchMoreShopModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                userSession,
                localCacheHandler
        )

        searchShopViewModel.searchShop()

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

    @Test
    fun `when searchShop() fails, validate state is error`() {
        val exception = Exception("Mock exception for testing error")
        val searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestErrorSearchUseCase(exception),
                TestSearchUseCase(searchMoreShopModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                userSession,
                localCacheHandler
        )

        searchShopViewModel.searchShop()

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertErrorSearchShopData(searchShopState)
    }

    private fun assertErrorSearchShopData(state: State<List<Visitable<*>>>?) {
        if(state != null) {
            assertStateIsError(state)
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

    @Test
    fun `when searchMoreShop() is successful, validate data is correct`() {
        val searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModel),
                TestSearchUseCase(searchMoreShopModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                userSession,
                localCacheHandler
        )

        searchShopViewModel.searchShop()
        searchShopViewModel.searchMoreShop()

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopData(searchShopState)
    }

    @Test
    fun `when searchMoreShop() is fails, validate previous data still exists with state error`() {
        val exception = Exception("Mock exception for testing error")
        val searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModel),
                TestErrorSearchUseCase(exception),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                userSession,
                localCacheHandler
        )

        searchShopViewModel.searchShop()
        searchShopViewModel.searchMoreShop()

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertErrorSearchMoreShopData(searchShopState)
    }

    private fun assertErrorSearchMoreShopData(state: State<List<Visitable<*>>>?) {
        if (state != null) {
            assertStateIsError(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertShopItemAtSecondIndexAndAboveOfData(state.data!!)
            assertShopItemCountDoesNotIncrease(state.data!!, shopItemViewModelList.size)
        }
        else {
            assert (false) {
                "State is null"
            }
        }
    }

    private fun assertShopItemCountDoesNotIncrease(data: List<Visitable<*>>, expectedShopItemCount: Int) {
        val actualShopItemCount = data.count { it is ShopViewModel.ShopItem }

        assert(actualShopItemCount == expectedShopItemCount) {
            "Actual shop item count is $actualShopItemCount, expected shop item count is $expectedShopItemCount"
        }
    }

    @Test
    fun `searchShop() should not do anything if searchShopLiveData has value`() {
        whenever(shopViewModelMapper.convert(searchShopModel))
                .thenReturn(shopViewModel)
                .thenReturn(ShopViewModel())

        whenever(shopHeaderViewModelMapper.convert(searchShopModel))
                .thenReturn(shopHeaderViewModel)
                .thenReturn(ShopHeaderViewModel())

        val searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModel),
                TestSearchUseCase(searchMoreShopModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                userSession,
                localCacheHandler
        )

        searchShopViewModel.searchShop()
        searchShopViewModel.searchShop()

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopData(searchShopState)
    }

    @Test
    fun `searchMoreShop() should not do anything if hasNextPage is false after searchShop()`() {
        val searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModelWithoutNextPage),
                TestSearchUseCase(searchMoreShopModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                userSession,
                localCacheHandler
        )

        searchShopViewModel.searchShop()
        searchShopViewModel.searchMoreShop()

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopWithoutNextPage(searchShopState)
    }

    private fun assertSuccessSearchShopWithoutNextPage(state: State<List<Visitable<*>>>?) {
        if (state != null) {
            assertStateIsSuccess(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertShopItemAtSecondIndexAndAboveOfData(state.data!!)
            assertShopItemCountDoesNotIncrease(state.data!!, shopItemViewModelList.size)
        }
        else {
            assert (false) {
                "State is null"
            }
        }
    }

    @Test
    fun `searchMoreShop() should not do anything if hasNextPage is false after searchMoreShop()`() {
        val searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModel),
                TestSearchUseCase(searchMoreShopModelWithoutNextPage),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                userSession,
                localCacheHandler
        )

        searchShopViewModel.searchShop()
        searchShopViewModel.searchMoreShop()

        whenever(shopViewModelMapper.convert(searchMoreShopModelWithoutNextPage)).thenReturn(
            ShopViewModel(
                    shopItemList = shopItemViewModelList.map { it.copy() }.toList()
            )
        )

        searchShopViewModel.searchMoreShop()

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchMoreShopWithoutNextPage(searchShopState)
    }

    private fun assertSuccessSearchMoreShopWithoutNextPage(state: State<List<Visitable<*>>>?) {
        if (state != null) {
            assertStateIsSuccess(state)
            assertDataIsNotEmpty(state.data)
            assertShopHeaderAtFirstIndexOfData(state.data!!)
            assertShopItemAtSecondIndexAndAboveOfData(state.data!!)
            assertShopItemCountDoesNotIncrease(state.data!!, shopItemViewModelList.size * 2)
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