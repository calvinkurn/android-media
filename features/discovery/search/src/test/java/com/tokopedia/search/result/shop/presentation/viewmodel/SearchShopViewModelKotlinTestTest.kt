package com.tokopedia.search.result.shop.presentation.viewmodel

import android.arch.core.executor.ArchTaskExecutor
import android.arch.core.executor.TaskExecutor
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
import io.kotlintest.Spec
import io.kotlintest.TestCase
import io.kotlintest.TestResult
import io.kotlintest.extensions.TopLevelTest
import io.kotlintest.matchers.asClue
import io.kotlintest.matchers.collections.shouldNotBeEmpty
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.matchers.types.shouldNotBeNull
import io.kotlintest.specs.BehaviorSpec
import kotlinx.coroutines.Dispatchers
import org.mockito.Mockito

fun <T> List<T>?.secondElementAndAbove(): List<T>? {
    if(this?.size ?: 0 < 2) return this

    return this?.subList(1, this.size)
}

class SearchShopViewModelKotlinTestTest: BehaviorSpec() {

    init {
        Given("search shop API call will be successful and return search shop data") {
            `given search shop API call will be successful and return search shop data`()

            When("execute search shop") {
                `when execute search shop`()

                Then("assert search shop state is success with data contains search shop header and shop items") {

                    val state = searchShopViewModel.getSearchShopLiveData()
                    state.asClue {

                        it.value.shouldNotBeNull()
                        it.value?.data.shouldNotBeNull()
                        it.value?.data?.shouldNotBeEmpty()
                        it.value?.data?.first().shouldBeInstanceOf<ShopHeaderViewModel>()
                        it.value?.data?.secondElementAndAbove().asClue { secondIndexAndAbove ->
                            secondIndexAndAbove.shouldBeInstanceOf<List<ShopViewModel.ShopItem>>()

                            secondIndexAndAbove?.forEachIndexed { index, shopItem ->
                                (shopItem as ShopViewModel.ShopItem).position = index + 1
                            }
                        }
                    }
                }
            }
        }
    }

    override fun beforeSpecClass(spec: Spec, tests: List<TopLevelTest>) {
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

    override fun afterSpecClass(spec: Spec, results: Map<TestCase, TestResult>) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }

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

    private val shopHeaderViewModelMapper = Mockito.mock(MockShopHeaderViewModelMapper::class.java)

    private val shopViewModelMapper = Mockito.mock(MockShopViewModelMapper::class.java)

    private val emptySearchCreator = Mockito.mock(EmptySearchCreator::class.java)

    private val userSession = Mockito.mock(UserSessionInterface::class.java).also {
        Mockito.`when`(it.isLoggedIn).thenReturn(true)
        Mockito.`when`(it.userId).thenReturn("123456")
    }

    private val localCacheHandler = Mockito.mock(LocalCacheHandler::class.java).also {
        Mockito.`when`(it.getString(SearchConstant.GCM_ID, "")).thenReturn("MOCK_GCM_ID")
    }

    private lateinit var searchShopViewModel: SearchShopViewModel

    private fun `given search shop API call will be successful and return search shop data`() {
        Mockito.`when`(shopHeaderViewModelMapper.convert(searchShopModel)).thenReturn(shopHeaderViewModel)
        Mockito.`when`(shopViewModelMapper.convert(searchShopModel)).thenReturn(shopViewModel)

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
        Mockito.`when`(shopHeaderViewModelMapper.convert(searchShopModel)).thenReturn(shopHeaderViewModel)
        Mockito.`when`(shopViewModelMapper.convert(searchShopModel)).thenReturn(shopViewModel)
        Mockito.`when`(shopViewModelMapper.convert(searchMoreShopModel)).thenReturn(moreShopViewModel)

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
        Mockito.`when`(shopHeaderViewModelMapper.convert(searchShopModel)).thenReturn(shopHeaderViewModel)
        Mockito.`when`(shopViewModelMapper.convert(searchShopModel)).thenReturn(shopViewModel)

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
        Mockito.`when`(shopHeaderViewModelMapper.convert(searchShopModel))
                .thenReturn(shopHeaderViewModel)
                .thenReturn(ShopHeaderViewModel())

        Mockito.`when`(shopViewModelMapper.convert(searchShopModel))
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
        Mockito.`when`(shopHeaderViewModelMapper.convert(searchShopModelWithoutNextPage)).thenReturn(shopHeaderViewModel)
        Mockito.`when`(shopViewModelMapper.convert(searchShopModelWithoutNextPage)).thenReturn(shopViewModel)
        Mockito.`when`(shopViewModelMapper.convert(searchMoreShopModel)).thenReturn(moreShopViewModel)

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
        Mockito.`when`(shopHeaderViewModelMapper.convert(searchShopModel)).thenReturn(shopHeaderViewModel)
        Mockito.`when`(shopViewModelMapper.convert(searchShopModel)).thenReturn(shopViewModel)
        Mockito.`when`(shopViewModelMapper.convert(searchMoreShopModelWithoutNextPage))
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
}
