package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.result.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.common.EmptySearchCreator
import com.tokopedia.search.result.common.State
import com.tokopedia.search.result.common.State.Error
import com.tokopedia.search.result.common.State.Success
import com.tokopedia.search.result.domain.usecase.SearchUseCase
import com.tokopedia.search.result.presentation.model.EmptySearchViewModel
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.domain.model.SearchShopModel.AceSearchShop
import com.tokopedia.search.result.shop.domain.model.SearchShopModel.AceSearchShop.ShopItem
import com.tokopedia.search.result.shop.domain.model.SearchShopModel.AceSearchShop.ShopItem.ShopItemProduct
import com.tokopedia.search.result.shop.presentation.mapper.ShopViewModelMapperModule
import com.tokopedia.search.result.shop.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.search.utils.betweenFirstAndLast
import com.tokopedia.search.utils.secondToLast
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.gherkin.FeatureBody

class SearchShopViewModelTest : Spek({

    InstantTaskExecutorRuleSpek(this)

    val pagingWithNextPage = AceSearchShop.Paging(uriNext = "Some random string indicating has next page")
    val pagingWithoutNextPage = AceSearchShop.Paging(uriNext = "")

    val shopItemProductList: List<ShopItemProduct> = mutableListOf<ShopItemProduct>().also {
        it.add(ShopItemProduct(id = 1))
        it.add(ShopItemProduct(id = 2))
        it.add(ShopItemProduct(id = 3))
    }

    val shopItemList: List<ShopItem> = mutableListOf<ShopItem>().also {
        it.add(ShopItem(id = "1", productList = shopItemProductList))
        it.add(ShopItem(id = "2"))
        it.add(ShopItem(id = "3", productList = shopItemProductList))
        it.add(ShopItem(id = "4"))
    }

    val moreShopItemList: List<ShopItem> = mutableListOf<ShopItem>().also {
        it.add(ShopItem(id = "5", productList = shopItemProductList))
        it.add(ShopItem(id = "6"))
    }

    val aceSearchShopWithNextPage = AceSearchShop(
            paging = pagingWithNextPage,
            shopList = shopItemList
    )

    val aceSearchShopWithoutNextPage = AceSearchShop(
            paging = pagingWithoutNextPage,
            shopList = shopItemList
    )

    val moreAceSearchShopWithNextPage = AceSearchShop(
            paging = pagingWithNextPage,
            shopList = moreShopItemList
    )

    val moreAceSearchShopWithoutNextPage = AceSearchShop(
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

    val shopViewModelMapperModule = ShopViewModelMapperModule()

    val shopHeaderViewModelMapper = shopViewModelMapperModule.provideShopHeaderViewModelMapper()
    val shopViewModelMapper = shopViewModelMapperModule.provideShopViewModelMapper()

    val emptySearchCreator = mockk<EmptySearchCreator>(relaxed = true)

    val userSession = mockk<UserSessionInterface>().also {
        every { it.isLoggedIn }.returns(true)
        every { it.userId }.returns("123456")
        every { it.deviceId }.returns("pixel 2")
    }

    val localCacheHandler = mockk<LocalCacheHandler>().also {
        every { it.getString(eq(SearchConstant.GCM.GCM_ID), "") }.returns("MOCK_GCM_ID")
    }

    fun FeatureBody.createTestInstance() {
        val searchShopUseCase by memoized {
            mockk<SearchUseCase<SearchShopModel>>(relaxed = true)
        }

        val searchMoreShopUseCase by memoized {
            mockk<SearchUseCase<SearchShopModel>>(relaxed = true)
        }

        val getDynamicFilterUseCase by memoized {
            mockk<SearchUseCase<DynamicFilterModel>>(relaxed = true)
        }

        @Suppress("UNUSED_VARIABLE")
        val searchShopViewModel by memoized {
            SearchShopViewModel(
                    Dispatchers.Unconfined, searchShopParameter,
                    searchShopUseCase, searchMoreShopUseCase, getDynamicFilterUseCase,
                    shopHeaderViewModelMapper, shopViewModelMapper,
                    emptySearchCreator, userSession, localCacheHandler
            )
        }
    }

    Feature("Handle View Visibility Changed") {
        createTestInstance()

        Scenario("View is visible and added") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("search shop API call will be successful") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("verify search shop API is called once") {
                searchShopUseCase.isExecuted()
            }
        }

        Scenario("View is not yet visible, or not yet added") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("search shop API call will be successful") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
            }

            When("handle view not visible or not added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = false)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = true)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = false)
            }

            Then("verify search shop API is never called") {
                searchShopUseCase.isNeverExecuted()
            }
        }

        Scenario("View is visible and added more than once") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("search shop API call will be successful") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
            }

            When("handle view is visible and added, and then not visible, then visible again") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = true)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("verify search shop API is only called once") {
                searchShopUseCase.isExecuted()
            }
        }
    }

    Feature("Search Shop First Page") {
        createTestInstance()

        Scenario("Search Shop First Page Successful") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("search shop API call will be successful and return search shop data") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success and contains search shop data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveHeaderAndLoadingMoreWithShopItemInBetween()
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }
        }

        Scenario("Search Shop First Page Error") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("search shop API call will fail") {
                val exception = Exception("Mock exception for testing error")

                searchShopUseCase.stubExecuteOnBackground().throws(exception)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is error with no data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Error<*>>()
                searchShopState.shouldBeNullOrEmpty()
            }
        }

        Scenario("Search Shop with Empty Result") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("search shop API will be successful and return empty search shop list") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModelEmptyList)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success and only contains empty search data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldOnlyHaveEmptySearchModel()
            }
        }
    }

    Feature("Get Dynamic Filter After Search Shop First Page Successful") {
        createTestInstance()

        Scenario("Search Shop First Page Successful") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()
            val getDynamicFilterUseCase by memoized<SearchUseCase<DynamicFilterModel>>()

            Given("search shop API will be successful and return search shop data") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert get dynamic filter API called once") {
                getDynamicFilterUseCase.isExecuted()
            }
        }

        Scenario("Search Shop First Page Error") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()
            val getDynamicFilterUseCase by memoized<SearchUseCase<DynamicFilterModel>>()

            Given("search shop API will be successful and return search shop data") {
                val exception = Exception("Mock exception for testing error")

                searchShopUseCase.stubExecuteOnBackground().throws(exception)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert get dynamic filter API never called") {
                getDynamicFilterUseCase.isNeverExecuted()
            }
        }
    }

    Feature("Handle View Scroll to Load More") {
        createTestInstance()

        Scenario("View load more and visible, and has next page") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchMoreShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page and has next page") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
                searchMoreShopUseCase.stubExecuteOnBackground().returns(searchMoreShopModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible =true)
            }

            Then("verify search more shop API is called once") {
                searchMoreShopUseCase.isExecuted()
            }
        }

        Scenario("View load more and visible, but does not have next page") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchMoreShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page and does not have next page") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModelWithoutNextPage)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
            }

            Then("verify search more shop API is never called") {
                searchMoreShopUseCase.isNeverExecuted()
            }
        }

        Scenario("View load more but not visible") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchMoreShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view has loaded first page and has next page") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view load more but not visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible = false)
            }

            Then("verify search more shop API is never called") {
                searchMoreShopUseCase.isNeverExecuted()
            }
        }

        Scenario("View load more twice and visible, but does not have next page after first load more") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchMoreShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page and has next page") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API will return data with has next page is false") {
                searchMoreShopUseCase.stubExecuteOnBackground()
                        .returns(searchMoreShopModelWithoutNextPage)
                        .andThen(searchMoreShopModel)
            }

            When("handle view load more twice and visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
            }

            Then("verify search more shop API is only called once") {
                searchMoreShopUseCase.isExecuted()
            }
        }
    }

    Feature("Search Shop Load More") {
        createTestInstance()

        Scenario("Search Shop and Search More Shop Successful") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchMoreShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page successfully") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API call will be successful and return search shop data") {
                searchMoreShopUseCase.stubExecuteOnBackground().returns(searchMoreShopModel)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(true)
            }

            Then("assert search shop state is success and contains data from search shop and search more shop") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveHeaderAndLoadingMoreWithShopItemInBetween()
                searchShopState.shouldHaveShopItemCount(shopItemList.size + moreShopItemList.size)
            }
        }

        Scenario("Search Shop Successful, but Search More Shop Error") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchMoreShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page successfully") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API call will fail") {
                val exception = Exception("Mock exception for testing error")
                searchMoreShopUseCase.stubExecuteOnBackground().throws(exception)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(true)
            }

            Then("assert search shop state is error, but still contains data from search shop") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Error<*>>()
                searchShopState.shouldHaveHeaderAndLoadingMoreWithShopItemInBetween()
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }
        }
    }

    Feature("Handle View Retry Search Shop") {
        createTestInstance()

        Scenario("Retry Search Shop After Error in Search Shop") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page error") {
                val exception = Exception("Mock exception for testing retry mechanism")

                searchShopUseCase.stubExecuteOnBackground()
                        .throws(exception)
                        .andThen(searchShopModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view retry search shop") {
                searchShopViewModel.onViewClickRetry()
            }

            Then("verify search shop API called twice") {
                searchShopUseCase.isExecuted(2)
            }

            Then("assert search shop state success after retry") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveHeaderAndLoadingMoreWithShopItemInBetween()
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }
        }

        Scenario("Retry Search Shop After Error in Search More Shop") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchMoreShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page successfully, but error during search shop second page") {
                val exception = Exception("Mock exception for testing retry mechanism")

                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
                searchMoreShopUseCase.stubExecuteOnBackground()
                        .throws(exception)
                        .andThen(searchMoreShopModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
                searchShopViewModel.onViewLoadMore(true)
            }

            When("handle view retry search more shop") {
                searchShopViewModel.onViewClickRetry()
            }

            Then("verify search shop API called once, and search more shop API called twice") {
                searchShopUseCase.isExecuted()
                searchMoreShopUseCase.isExecuted(2)
            }

            Then("assert search shop state success after retry") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveHeaderAndLoadingMoreWithShopItemInBetween()
                searchShopState.shouldHaveShopItemCount(shopItemList.size + moreShopItemList.size)
            }
        }
    }

    Feature("Handle View Reload Search Shop") {
        createTestInstance()

        Scenario("Reload Search Shop After Search Shop and Search More Shop") {
            val searchShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchMoreShopUseCase by memoized<SearchUseCase<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first and second page successfully") {
                searchShopUseCase.stubExecuteOnBackground().returns(searchShopModel)
                searchMoreShopUseCase.stubExecuteOnBackground().returns(searchMoreShopModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
            }

            When("handle view reload search shop") {
                searchShopViewModel.onViewReloadData()
            }

            Then("verify search shop API is called twice and search more shop API is called once") {
                searchShopUseCase.isExecuted(2)
                searchMoreShopUseCase.isExecuted()
            }

            Then("assert search shop state success after reload") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveHeaderAndLoadingMoreWithShopItemInBetween()
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }
        }
    }
})

private fun SearchUseCase<*>.stubExecuteOnBackground(): MockKStubScope<Any, Any> {
    val it = this
    return coEvery { it.executeOnBackground() }
}

private fun SearchUseCase<*>.isNeverExecuted() {
    return this.isExecuted(0)
}

private fun SearchUseCase<*>.isExecuted(executionCount: Int = 1) {
    val it = this
    coVerify(exactly = executionCount) { it.executeOnBackground() }
}

private inline fun <reified T> Any?.shouldBeInstanceOf() {
    if (this !is T) {
        val actualClassName = if (this == null) "null" else this::class.simpleName
        val expectedClassName = T::class.simpleName

        throw AssertionError("$actualClassName should be instance of $expectedClassName")
    }
}

private fun State<List<Visitable<*>>>?.shouldHaveHeaderAndLoadingMoreWithShopItemInBetween() {
    this?.data?.first().shouldBeInstanceOf<ShopHeaderViewModel>()
    this?.data?.last().shouldBeInstanceOf<LoadingMoreModel>()
    this?.data.betweenFirstAndLast().forEachIndexed { index, shopItem ->
        shopItem.shouldBeInstanceOf<ShopViewModel.ShopItem>()
        (shopItem as ShopViewModel.ShopItem).position = index + 1
    }
}

private fun State<List<Visitable<*>>>?.shouldHaveHeaderAndShopItemFromSecondToLastElement() {
    this?.data?.first().shouldBeInstanceOf<ShopHeaderViewModel>()
    this?.data.secondToLast().forEachIndexed { index, visitable ->
        visitable.verifyShopItemIsCorrect(index)
    }
}

private fun Visitable<*>.verifyShopItemIsCorrect(index: Int) {
    this.shouldBeInstanceOf<ShopViewModel.ShopItem>()

    val shopItem = this as ShopViewModel.ShopItem
    shopItem.shouldHaveCorrectPosition(index + 1)
    shopItem.shouldHaveCorrectProductItemPosition()
}

private fun ShopViewModel.ShopItem.shouldHaveCorrectPosition(expectedPosition: Int) {
    this.position shouldBe expectedPosition
}

private infix fun Any?.shouldBe(expectedValue: Any) {
    if (this != expectedValue) {
        throw AssertionError("$this should be $expectedValue")
    }
}

private fun ShopViewModel.ShopItem.shouldHaveCorrectProductItemPosition() {
    this.productList.forEachIndexed { index, productItem ->
        productItem.position shouldBe index + 1
    }
}

private fun State<List<Visitable<*>>>?.shouldHaveShopItemCount(size: Int) {
    this?.data?.count { it is ShopViewModel.ShopItem } shouldBe size
}

private fun State<List<Visitable<*>>>?.shouldBeNullOrEmpty() {
    this?.data.isNullOrEmpty() shouldBe true
}

private fun State<List<Visitable<*>>>?.shouldOnlyHaveEmptySearchModel() {
    this?.data?.shouldHaveSize(1)
    this?.data?.first().shouldBeInstanceOf<EmptySearchViewModel>()
}

private fun List<*>.shouldHaveSize(expectedSize: Int) {
    if (this.size != expectedSize) {
        throw AssertionError("Size should be $expectedSize. Actual size: ${this.size}")
    }
}