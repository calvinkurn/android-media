package com.tokopedia.search.result.shop.presentation.viewmodel

import android.arch.core.executor.ArchTaskExecutor
import android.arch.core.executor.TaskExecutor
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchConstant
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
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when` as whenever

@ExperimentalCoroutinesApi
@RunWith(JUnitPlatform::class)
class SearchShopViewModelSpekTest : Spek({

    abstract class MockShopHeaderViewModelMapper : Mapper<SearchShopModel, ShopHeaderViewModel>
    abstract class MockShopViewModelMapper : Mapper<SearchShopModel, ShopViewModel>

    val aceSearchShopWithNextPage = SearchShopModel.AceSearchShop(
            paging = SearchShopModel.AceSearchShop.Paging(uriNext = "Some random string indicating has next page")
    )
    val aceSearchShopWithoutNextPage = SearchShopModel.AceSearchShop(
            paging = SearchShopModel.AceSearchShop.Paging(uriNext = "")
    )
    val searchShopModel = SearchShopModel(aceSearchShopWithNextPage)
    val searchMoreShopModel = SearchShopModel(aceSearchShopWithNextPage)
    val searchShopModelWithoutNextPage = SearchShopModel(aceSearchShopWithoutNextPage)
    val searchMoreShopModelWithoutNextPage = SearchShopModel(aceSearchShopWithoutNextPage)

    val shopHeaderViewModel = ShopHeaderViewModel()
    val shopItemViewModelList = mutableListOf<ShopViewModel.ShopItem>().also {
        it.add(ShopViewModel.ShopItem())
        it.add(ShopViewModel.ShopItem())
        it.add(ShopViewModel.ShopItem())
        it.add(ShopViewModel.ShopItem())
    }

    val moreShopItemViewModelList = mutableListOf<ShopViewModel.ShopItem>().also {
        it.add(ShopViewModel.ShopItem())
        it.add(ShopViewModel.ShopItem())
    }

    val shopViewModel = ShopViewModel(
            shopItemList = shopItemViewModelList
    )

    val moreShopViewModel = ShopViewModel(
            shopItemList = moreShopItemViewModelList
    )

    val searchShopParameter = mapOf(
            SearchApiConst.Q to "samsung"
    )

    val shopHeaderViewModelMapper = Mockito.mock(MockShopHeaderViewModelMapper::class.java)

    val shopViewModelMapper = Mockito.mock(MockShopViewModelMapper::class.java)

    val emptySearchCreator = Mockito.mock(EmptySearchCreator::class.java)

    val userSession = Mockito.mock(UserSessionInterface::class.java).also {
        whenever(it.isLoggedIn).thenReturn(true)
        whenever(it.userId).thenReturn("123456")
    }

    val localCacheHandler = Mockito.mock(LocalCacheHandler::class.java).also {
        whenever(it.getString(SearchConstant.GCM_ID, "")).thenReturn("MOCK_GCM_ID")
    }

    lateinit var searchShopViewModel: SearchShopViewModel

    beforeEachTest {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }
        })
    }

    afterEachTest { ArchTaskExecutor.getInstance().setDelegate(null) }

    fun `given search shop API call will be successful and return search shop data`() {
        whenever(shopHeaderViewModelMapper.convert(searchShopModel)).thenReturn(shopHeaderViewModel)
        whenever(shopViewModelMapper.convert(searchShopModel)).thenReturn(shopViewModel)

        searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModel),
                TestSearchUseCase(searchMoreShopModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }

    fun `when execute search shop`() {
        searchShopViewModel.searchShop()
    }

    fun assertStateIsSuccess(state: State<List<Visitable<*>>>?) {
        assert(state is State.Success) {
            "$state is not State.Success"
        }
    }

    fun assertDataIsNotEmpty(data: List<Visitable<*>>?) {
        assert(!data.isNullOrEmpty()) {
            "Data is null or empty."
        }
    }

    fun assertShopHeaderAtFirstIndexOfData(data: List<Visitable<*>>) {
        assert(data[0] is ShopHeaderViewModel) {
            "First element ${data[0].javaClass.kotlin.qualifiedName} is not of type ShopHeaderViewModel"
        }
    }

    fun assertShopItemType(index: Int, shopItem: Visitable<*>) {
        assert(shopItem is ShopViewModel.ShopItem) {
            "Element $index ${shopItem.javaClass.kotlin.qualifiedName} is not of type ShopViewModel.ShopItem"
        }
    }

    fun assertShopItemPosition(index: Int, shopItem: ShopViewModel.ShopItem) {
        assert(shopItem.position == index) {
            "Element $index of ShopViewModel.ShopItem position is ${shopItem.position}, expected $index"
        }
    }

    fun assertShopItemAtSecondIndexAndAboveOfData(data: List<Visitable<*>>) {
        for(i in 1 until data.size) {
            assertShopItemType(i, data[i])
            assertShopItemPosition(i, data[i] as ShopViewModel.ShopItem)
        }
    }
    
    fun assertSuccessSearchShopData(state: State<List<Visitable<*>>>?) {
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

    fun `then assert search shop state is success with data contains search shop header and shop items`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopData(searchShopState)
    }

    fun `given search shop API call will fail`() {
        val exception = Exception("Mock exception for testing error")
        searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestErrorSearchUseCase(exception),
                TestSearchUseCase(searchMoreShopModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }

    fun assertStateIsError(state: State<List<Visitable<*>>>?) {
        assert(state is State.Error) {
            "$state is not State.Error"
        }
    }

    fun assertDataIsNull(data: List<Visitable<*>>?) {
        assert(data.isNullOrEmpty()) {
            "Data is not null and not empty."
        }
    }

    fun assertErrorSearchShopData(state: State<List<Visitable<*>>>?) {
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

    fun `then assert search shop state is error with no data`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertErrorSearchShopData(searchShopState)
    }

    fun `given search shop and search more shop API call will be successful and return search shop data`() {
        whenever(shopHeaderViewModelMapper.convert(searchShopModel)).thenReturn(shopHeaderViewModel)
        whenever(shopViewModelMapper.convert(searchShopModel)).thenReturn(shopViewModel)
        whenever(shopViewModelMapper.convert(searchMoreShopModel)).thenReturn(moreShopViewModel)

        searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModel),
                TestSearchUseCase(searchMoreShopModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }

    fun `when execute search shop, then search more shop`() {
        searchShopViewModel.searchShop()
        searchShopViewModel.searchMoreShop()
    }

    fun assertShopItemSize(data: List<Visitable<*>>, expectedShopItemCount: Int) {
        val actualShopItemCount = data.count { it is ShopViewModel.ShopItem }

        assert(actualShopItemCount == expectedShopItemCount) {
            "Actual shop item count is $actualShopItemCount, expected shop item count is $expectedShopItemCount"
        }
    }

    fun assertSuccessSearchMoreShopData(state: State<List<Visitable<*>>>?) {
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

    fun `then assert search shop state is success with data from both search shop and search more shop`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchMoreShopData(searchShopState)
    }

    fun `given search shop API call will be successful, but search more shop API call will fail`() {
        whenever(shopHeaderViewModelMapper.convert(searchShopModel)).thenReturn(shopHeaderViewModel)
        whenever(shopViewModelMapper.convert(searchShopModel)).thenReturn(shopViewModel)

        val exception = Exception("Mock exception for testing error")
        searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModel),
                TestErrorSearchUseCase(exception),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }

    fun assertErrorSearchMoreShopData(state: State<List<Visitable<*>>>?) {
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

    fun `then assert search shop state is error, but still contains data from search shop`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertErrorSearchMoreShopData(searchShopState)
    }

    fun `given search shop API call will return different values between first and second call`() {
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
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }

    fun `when execute search shop twice`() {
        searchShopViewModel.searchShop()
        searchShopViewModel.searchShop()
    }

    fun assertSuccessSearchShopDataOnlyOnce(state: State<List<Visitable<*>>>?) {
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

    fun `then assert search shop state is success but only have data from the first search shop API call`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopDataOnlyOnce(searchShopState)
    }

    fun `given search shop API call will return data with has next page is false`() {
        whenever(shopHeaderViewModelMapper.convert(searchShopModelWithoutNextPage)).thenReturn(shopHeaderViewModel)
        whenever(shopViewModelMapper.convert(searchShopModelWithoutNextPage)).thenReturn(shopViewModel)
        whenever(shopViewModelMapper.convert(searchMoreShopModel)).thenReturn(moreShopViewModel)

        searchShopViewModel = SearchShopViewModel(
                Dispatchers.Unconfined,
                searchShopParameter,
                TestSearchUseCase(searchShopModelWithoutNextPage),
                TestSearchUseCase(searchMoreShopModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }

    fun assertSuccessSearchShopWithoutNextPage(state: State<List<Visitable<*>>>?) {
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

    fun `then assert search shop state is success, but only have search shop data`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopWithoutNextPage(searchShopState)
    }

    fun `given search more shop API will return data with has next page is false`() {
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
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                emptySearchCreator,
                userSession,
                localCacheHandler
        )
    }

    fun `when execute search shop, search more shop, and then search more shop again`() {
        searchShopViewModel.searchShop()
        searchShopViewModel.searchMoreShop()
        searchShopViewModel.searchMoreShop()
    }

    fun assertSuccessSearchMoreShopWithoutNextPage(state: State<List<Visitable<*>>>?) {
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

    fun `then assert search shop state is success, without data from last search more shop API call`() {
        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchMoreShopWithoutNextPage(searchShopState)
    }

    given("Search Shop View Model") {

        on("search shop success") {
            `given search shop API call will be successful and return search shop data`()
            `when execute search shop`()

            it("should have search shop state as success with data contains search shop header and shop items") {
                `then assert search shop state is success with data contains search shop header and shop items`()
            }
        }

        on("search shop error") {
            `given search shop API call will fail`()
            `when execute search shop`()

            it("should have search shop state as error with no data") {
                `then assert search shop state is error with no data`()
            }
        }

        on("search shop and search more shop successful") {
            `given search shop and search more shop API call will be successful and return search shop data`()
            `when execute search shop, then search more shop`()

            it("should have search shop state as success with data from both search shop and search more shop") {
                `then assert search shop state is success with data from both search shop and search more shop`()
            }
        }

        on("search shop successful, but search more shop error") {
            `given search shop API call will be successful, but search more shop API call will fail`()
            `when execute search shop, then search more shop`()

            it("should have search shop state as error, but still contains data from search shop") {
                `then assert search shop state is error, but still contains data from search shop`()
            }
        }

        on("search shop successful twice") {
            `given search shop API call will return different values between first and second call`()
            `when execute search shop twice`()

            it("should have search shop state as success but only have data from the first search shop API call") {
                `then assert search shop state is success but only have data from the first search shop API call`()
            }
        }

        on("search more shop without next page after search shop") {
            `given search shop API call will return data with has next page is false`()
            `when execute search shop, then search more shop`()

            it("should have search shop state as success, but only have search shop data") {
                `then assert search shop state is success, but only have search shop data`()
            }
        }

        on("search more shop but without next page after search more shop") {
            `given search more shop API will return data with has next page is false`()
            `when execute search shop, search more shop, and then search more shop again`()

            it("should have search shop state as success, without data from last search more shop API call") {
                `then assert search shop state is success, without data from last search more shop API call`()
            }
        }
    }
})