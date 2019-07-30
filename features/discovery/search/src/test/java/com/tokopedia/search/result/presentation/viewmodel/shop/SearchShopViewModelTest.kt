package com.tokopedia.search.result.presentation.viewmodel.shop

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.domain.usecase.TestUseCase
import com.tokopedia.search.result.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.presentation.model.ShopViewModel
import com.tokopedia.search.result.presentation.view.typefactory.ShopListTypeFactory
import com.tokopedia.search.result.presentation.viewmodel.State
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as mockitoWhen

class SearchShopViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private abstract class MockShopHeaderViewModelMapper : Mapper<SearchShopModel, ShopHeaderViewModel>
    private abstract class MockShopViewModelMapper : Mapper<SearchShopModel, ShopViewModel>

    private val searchShopModel = SearchShopModel()
    private val shopHeaderViewModel = ShopHeaderViewModel()
    private val shopItemViewModelList = mutableListOf<ShopViewModel.ShopItem>().also {
        it.add(ShopViewModel.ShopItem())
    }
    private val shopViewModel = ShopViewModel(
            shopItemList = shopItemViewModelList
    )

    private val searchShopParameter = mapOf(
            SearchApiConst.Q to "samsung"
    )

    private val shopHeaderViewModelMapper = mock(MockShopHeaderViewModelMapper::class.java).also {
        mockitoWhen(it.convert(searchShopModel)).thenReturn(shopHeaderViewModel)
    }

    private val shopViewModelMapper = mock(MockShopViewModelMapper::class.java).also {
        mockitoWhen(it.convert(searchShopModel)).thenReturn(shopViewModel)
    }

    private val userSession = mock(UserSessionInterface::class.java).also {
        mockitoWhen(it.isLoggedIn).thenReturn(true)
        mockitoWhen(it.userId).thenReturn("123456")
    }

    private val localCacheHandler = mock(LocalCacheHandler::class.java).also {
        mockitoWhen(it.getString(SearchConstant.GCM_ID, "")).thenReturn("MOCK_GCM_ID")
    }

    @Test
    fun `when searchShop() is successful, validate data is correct`() {
        val searchShopViewModel = SearchShopViewModel(
                Dispatchers.Default,
                searchShopParameter,
                TestUseCase(searchShopModel),
                TestUseCase(searchShopModel),
                shopHeaderViewModelMapper,
                shopViewModelMapper,
                userSession,
                localCacheHandler
        )

        searchShopViewModel.searchShop()

        val searchShopState = searchShopViewModel.getSearchShopLiveData().value
        assertSuccessSearchShopData(searchShopState)
    }

    private fun assertSuccessSearchShopData(state: State<List<Visitable<ShopListTypeFactory>>>?) {
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

    private fun assertStateIsSuccess(state: State<List<Visitable<ShopListTypeFactory>>>?) {
        assert(state is State.Success) {
            "$state is not success"
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
        for(i in 1..data.size - 1) {
            assert(data[i] is ShopViewModel.ShopItem) {
                "Element $i ${data[i].javaClass.kotlin.qualifiedName} is not of type ShopViewModel.ShopItem"
            }
        }
    }

    @After
    fun tearDown() {

    }
}