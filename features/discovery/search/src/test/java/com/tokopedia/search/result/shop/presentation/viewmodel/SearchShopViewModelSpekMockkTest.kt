package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.data.DynamicFilterModel
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst
import com.tokopedia.search.result.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.common.EmptySearchCreator
import com.tokopedia.search.result.common.State
import com.tokopedia.search.result.common.State.Error
import com.tokopedia.search.result.common.State.Success
import com.tokopedia.search.result.domain.usecase.SearchUseCase
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.mapper.ShopHeaderViewModelMapper
import com.tokopedia.search.result.shop.presentation.mapper.ShopViewModelMapper
import com.tokopedia.search.result.shop.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class SearchShopViewModelSpekMockkTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    Feature("Search Shop") {
        val pagingWithNextPage = SearchShopModel.AceSearchShop.Paging(uriNext = "Some random string indicating has next page")
        val pagingWithoutNextPage = SearchShopModel.AceSearchShop.Paging(uriNext = "")

        val shopItemList: List<SearchShopModel.AceSearchShop.ShopItem> = mutableListOf<SearchShopModel.AceSearchShop.ShopItem>().also {
            it.add(SearchShopModel.AceSearchShop.ShopItem(id = "1"))
            it.add(SearchShopModel.AceSearchShop.ShopItem(id = "2"))
            it.add(SearchShopModel.AceSearchShop.ShopItem(id = "3"))
            it.add(SearchShopModel.AceSearchShop.ShopItem(id = "4"))
        }

        val moreShopItemList: List<SearchShopModel.AceSearchShop.ShopItem> = mutableListOf<SearchShopModel.AceSearchShop.ShopItem>().also {
            it.add(SearchShopModel.AceSearchShop.ShopItem(id = "5"))
            it.add(SearchShopModel.AceSearchShop.ShopItem(id = "6"))
        }

        val aceSearchShopWithNextPage = SearchShopModel.AceSearchShop(
                paging = pagingWithNextPage,
                shopList = shopItemList
        )

        val aceSearchShopWithoutNextPage = SearchShopModel.AceSearchShop(
                paging = pagingWithoutNextPage,
                shopList = shopItemList
        )

        val moreAceSearchShopWithNextPage = SearchShopModel.AceSearchShop(
                paging = pagingWithNextPage,
                shopList = moreShopItemList
        )

        val moreAceSearchShopWithoutNextPage = SearchShopModel.AceSearchShop(
                paging = pagingWithoutNextPage,
                shopList = moreShopItemList
        )

        val searchShopModel = SearchShopModel(aceSearchShopWithNextPage)
        val searchShopModelWithoutNextPage = SearchShopModel(aceSearchShopWithoutNextPage)
        val searchShopModelEmptyList = SearchShopModel()
        val searchMoreShopModel = SearchShopModel(moreAceSearchShopWithNextPage)
        val searchMoreShopModelWithoutNextPage = SearchShopModel(moreAceSearchShopWithoutNextPage)
        val dynamicFilterModel = DynamicFilterModel()

        val searchShopParameter = mapOf(
                SearchApiConst.Q to "samsung"
        )

        val shopHeaderViewModelMapper = ShopHeaderViewModelMapper()
        val shopViewModelMapper = ShopViewModelMapper()

        val emptySearchCreator = mockk<EmptySearchCreator>(relaxed = true)

        val userSession = mockk<UserSessionInterface>().also {
            every { it.isLoggedIn }.returns(true)
            every { it.userId }.returns("123456")
        }

        val localCacheHandler = mockk<LocalCacheHandler>().also {
            every { it.getString(eq(SearchConstant.GCM_ID), "") }.returns("MOCK_GCM_ID")
        }

        val searchShopUseCase by memoized {
            mockk<SearchUseCase<SearchShopModel>>(relaxed = true)
        }

        val searchMoreShopUseCase by memoized {
            mockk<SearchUseCase<SearchShopModel>>(relaxed = true)
        }

        val dynamicFilterUseCase by memoized {
            mockk<SearchUseCase<DynamicFilterModel>>(relaxed = true)
        }

        val searchShopViewModel by memoized {
            SearchShopViewModel(
                    Dispatchers.Unconfined, searchShopParameter,
                    searchShopUseCase, searchMoreShopUseCase, dynamicFilterUseCase,
                    shopHeaderViewModelMapper, shopViewModelMapper,
                    emptySearchCreator, userSession, localCacheHandler
            )
        }

        Scenario("Search Shop First Page Successful") {

            Given("search shop API call will be successful and return search shop data") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
            }

            When("execute search shop") {
                searchShopViewModel.searchShop()
            }

            Then("verify search shop API is called once") {
                searchShopUseCase.isExecuted()
            }

            Then("assert search shop state is success and contains search shop data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveHeaderAndLoadingMoreWithShopItemInBetween()
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }
        }

        Scenario("Search Shop First Page Error") {

            Given("search shop API call will fail") {
                val exception = Exception("Mock exception for testing error")
                searchShopUseCase.stubExecuteOnBackground().throws(exception)
            }

            When("execute search shop") {
                searchShopViewModel.searchShop()
            }

            Then("verify search shop API is called once") {
                searchShopUseCase.isExecuted()
            }

            Then("assert search shop state is error with no data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Error<*>>()
                searchShopState.shouldBeNullOrEmpty()
            }
        }

        Scenario("Search Shop and Search More Shop Successful") {

            Given("search shop and search more shop API call will be successful and return search shop data") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
                searchMoreShopUseCase.stubExecuteOnBackground().returns(searchMoreShopModel)
            }

            When("execute search shop and search more shop") {
                searchShopViewModel.searchShop()
                searchShopViewModel.searchMoreShop()
            }

            Then("verify search shop and search more shop API called once") {
                searchShopUseCase.isExecuted()
                searchMoreShopUseCase.isExecuted()
            }

            Then("assert search shop state is success and contains data from search shop and search more shop") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveHeaderAndLoadingMoreWithShopItemInBetween()
                searchShopState.shouldHaveShopItemCount(shopItemList.size + moreShopItemList.size)
            }
        }

        Scenario("Search Shop Successful, but Search More Shop Error") {

            Given("search shop API call will be successful, but search more shop API call will fail") {
                val exception = Exception("Mock exception for testing error")
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
                searchMoreShopUseCase.stubExecuteOnBackground().throws(exception)
            }

            When("execute search shop and search more shop") {
                searchShopViewModel.searchShop()
                searchShopViewModel.searchMoreShop()
            }

            Then("verify search shop and search more shop API called once") {
                searchShopUseCase.isExecuted()
                searchMoreShopUseCase.isExecuted()
            }

            Then("assert search shop state is error, but still contains data from search shop") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Error<*>>()
                searchShopState.shouldHaveHeaderAndLoadingMoreWithShopItemInBetween()
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }
        }
    }
})

fun SearchUseCase<*>.stubExecuteOnBackground(): MockKStubScope<Any, Any> {
    val it = this
    return coEvery { it.executeOnBackground() }
}

fun SearchUseCase<*>.isExecuted(executionCount: Int = 1) {
    val it = this
    coVerify(exactly = executionCount) { it.executeOnBackground() }
}

fun State<List<Visitable<*>>>?.shouldHaveHeaderAndLoadingMoreWithShopItemInBetween() {
    this?.data?.first().shouldBeInstanceOf<ShopHeaderViewModel>()
    this?.data?.last().shouldBeInstanceOf<LoadingMoreModel>()
    this?.data?.betweenFirstAndLast()?.let {
        it.forEachIndexed { index, shopItem ->
            shopItem.shouldBeInstanceOf<ShopViewModel.ShopItem>()
            (shopItem as ShopViewModel.ShopItem).position = index + 1
        }
    }
}

fun List<*>?.betweenFirstAndLast(): List<*>? {
    if(this?.size ?: 0 < 3) return null

    return this?.subList(1, this.size - 1)
}

fun State<List<Visitable<*>>>?.shouldHaveShopItemCount(size: Int) {
    this?.data?.count { it is ShopViewModel.ShopItem } shouldBe size
}

fun State<List<Visitable<*>>>?.shouldBeNullOrEmpty() {
    this?.data.isNullOrEmpty() shouldBe true
}