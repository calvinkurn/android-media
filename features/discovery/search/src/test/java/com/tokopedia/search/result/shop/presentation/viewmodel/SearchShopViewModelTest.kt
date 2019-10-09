package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.coroutines.Repository
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.search.result.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.TestDispatcherProvider
import com.tokopedia.search.result.common.State
import com.tokopedia.search.result.common.State.Error
import com.tokopedia.search.result.common.State.Success
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.domain.model.SearchShopModel.AceSearchShop
import com.tokopedia.search.result.shop.domain.model.SearchShopModel.AceSearchShop.ShopItem
import com.tokopedia.search.result.shop.domain.model.SearchShopModel.AceSearchShop.ShopItem.ShopItemProduct
import com.tokopedia.search.result.shop.presentation.mapper.ShopViewModelMapperModule
import com.tokopedia.search.result.shop.presentation.model.EmptySearchViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopHeaderViewModel
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.search.shouldBe
import com.tokopedia.search.utils.betweenFirstAndLast
import com.tokopedia.search.utils.convertValuesToString
import com.tokopedia.search.utils.secondToLast
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.spekframework.spek2.style.gherkin.FeatureBody

internal class SearchShopViewModelTest : Spek({

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

    val officialOption = OptionHelper.generateOptionFromUniqueId(
            OptionHelper.constructUniqueId("official", "true", "Official Store")
    )
    val shopFilter = Filter().also { filter ->
        filter.options = mutableListOf<Option>().also { optionList ->
            optionList.add(officialOption)
        }
    }
    val jakartaOption = OptionHelper.generateOptionFromUniqueId(
            OptionHelper.constructUniqueId(SearchApiConst.FCITY, "1", "Jakarta")
    )
    val tangerangOption = OptionHelper.generateOptionFromUniqueId(
            OptionHelper.constructUniqueId(SearchApiConst.FCITY, "2", "Tangerang")
    )
    val bogorOption = OptionHelper.generateOptionFromUniqueId(
            OptionHelper.constructUniqueId(SearchApiConst.FCITY, "3", "Bogor")
    )
    val locationFilter = Filter().also { filter ->
        filter.options = mutableListOf<Option>().also { optionList ->
            optionList.add(jakartaOption)
            optionList.add(tangerangOption)
            optionList.add(bogorOption)
        }
    }
    val minPriceOption = OptionHelper.generateOptionFromUniqueId(
            OptionHelper.constructUniqueId(SearchApiConst.PMIN, "", "Harga minimum")
    )
    val maxPriceOption = OptionHelper.generateOptionFromUniqueId(
            OptionHelper.constructUniqueId(SearchApiConst.PMIN, "", "Harga minimum")
    )
    val priceFilter = Filter().also { filter ->
        filter.options = mutableListOf<Option>().also { optionList ->
            optionList.add(minPriceOption)
            optionList.add(maxPriceOption)
        }
    }

    val dynamicFilterModel = DynamicFilterModel()
    dynamicFilterModel.data = DataValue()
    dynamicFilterModel.data.filter = mutableListOf<Filter>().also {
        it.add(shopFilter)
        it.add(locationFilter)
        it.add(priceFilter)
    }

    val searchShopParameter = mapOf(
            SearchApiConst.Q to "samsung",
            SearchApiConst.OFFICIAL to true
    )

    val searchShopParameterWithActiveTab = mapOf(
            SearchApiConst.Q to "samsung",
            SearchApiConst.OFFICIAL to true,
            SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.SHOP
    )

    val shopViewModelMapperModule = ShopViewModelMapperModule()

    val shopHeaderViewModelMapper = shopViewModelMapperModule.provideShopHeaderViewModelMapper()
    val shopViewModelMapper = shopViewModelMapperModule.provideShopViewModelMapper()

    val userSession = mockk<UserSessionInterface>().also {
        every { it.isLoggedIn }.returns(true)
        every { it.userId }.returns("123456")
        every { it.deviceId }.returns("pixel 2")
    }

    val localCacheGCMIDValue = "GCM_ID"
    val localCacheHandler = mockk<LocalCacheHandler>().also {
        every { it.getString(eq(SearchConstant.GCM.GCM_ID), "") }.returns(localCacheGCMIDValue)
    }

    fun FeatureBody.createTestInstance() {
        val searchShopFirstPageRepository by memoized {
            mockk<Repository<SearchShopModel>>(relaxed = true)
        }

        val searchShopLoadMoreRepository by memoized {
            mockk<Repository<SearchShopModel>>(relaxed = true)
        }

        val dynamicFilterRepository by memoized {
            mockk<Repository<DynamicFilterModel>>(relaxed = true)
        }

        val searchLocalCacheHandler by memoized {
            mockk<SearchLocalCacheHandler>(relaxed = true)
        }

        fun createSearchShopViewModel(searchShopParameter: Map<String, Any>): SearchShopViewModel {
            return SearchShopViewModel(
                    TestDispatcherProvider(), searchShopParameter,
                    searchShopFirstPageRepository, searchShopLoadMoreRepository, dynamicFilterRepository,
                    shopHeaderViewModelMapper, shopViewModelMapper,
                    searchLocalCacheHandler, userSession, localCacheHandler
            )
        }

        @Suppress("UNUSED_VARIABLE")
        val searchShopViewModel by memoized { createSearchShopViewModel(searchShopParameter) }

        @Suppress("UNUSED_VARIABLE")
        val searchShopViewModelWithActiveTab by memoized { createSearchShopViewModel(searchShopParameterWithActiveTab) }
    }

    Feature("Handle View Created") {
        createTestInstance()

        Scenario("View is created and parameter active tab is ${ SearchConstant.ActiveTab.SHOP }") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModelWithActiveTab by memoized<SearchShopViewModel>()

            Given("search shop API call will be successful") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
            }

            When("handle view created") {
                searchShopViewModelWithActiveTab.onViewCreated()
            }

            Then("verify search shop and dynamic filter API is called once") {
                searchShopFirstPageRepository.isExecuted()
                dynamicFilterRepository.isExecuted()
            }
        }

        Scenario("View is created multiple times and parameter active tab is ${ SearchConstant.ActiveTab.SHOP }") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModelWithActiveTab by memoized<SearchShopViewModel>()

            Given("search shop API call will be successful") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
            }

            When("handle view created multiple times") {
                searchShopViewModelWithActiveTab.onViewCreated()
                searchShopViewModelWithActiveTab.onViewCreated()
                searchShopViewModelWithActiveTab.onViewCreated()
            }

            Then("verify search shop and dynamic filter API is called once") {
                searchShopFirstPageRepository.isExecuted()
                dynamicFilterRepository.isExecuted()
            }
        }
    }

    Feature("Handle View Visibility Changed") {
        createTestInstance()

        Scenario("View is visible and added") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("search shop API call will be successful") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("verify search shop and dynamic filter API is called once") {
                searchShopFirstPageRepository.isExecuted()
                dynamicFilterRepository.isExecuted()
            }
        }

        Scenario("View is not yet visible, or not yet added") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("search shop API call will be successful") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
            }

            When("handle view not visible or not added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = false)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = true)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = false)
            }

            Then("verify search shop API is never called") {
                searchShopFirstPageRepository.isNeverExecuted()
            }
        }

        Scenario("View is visible and added more than once") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("search shop API call will be successful") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
            }

            When("handle view is visible and added, and then not visible, then visible again") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = true)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("verify search shop and dynamic filter API is only called once") {
                searchShopFirstPageRepository.isExecuted()
                dynamicFilterRepository.isExecuted()
            }
        }

        Scenario("View is created with parameter active tab is ${ SearchConstant.ActiveTab.SHOP }, and then view is visible") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModelWithActiveTab by memoized<SearchShopViewModel>()

            Given("view is already created") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)

                searchShopViewModelWithActiveTab.onViewCreated()
            }

            When("handle view is visible and added") {
                searchShopViewModelWithActiveTab.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("verify search shop and dynamic filter API is called once") {
                searchShopFirstPageRepository.isExecuted()
                dynamicFilterRepository.isExecuted()
            }
        }
    }

    Feature("Search Shop First Page") {
        createTestInstance()

        Scenario("Search Shop First Page Successful") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("search shop API call will be successful and return search shop data") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
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

            Then("assert has next page is true") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe true
            }
        }

        Scenario("Search Shop First Page Successful Without Next Page") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("search shop API call will be successful and return search shop data without next page") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModelWithoutNextPage)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success and contains search shop data without loading more view model") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveHeaderAndShopItemFromSecondToLastElement()
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }
        }

        Scenario("Search Shop First Page Error") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("search shop API call will fail") {
                val exception = Exception("Mock exception for testing error")

                searchShopFirstPageRepository.stubGetResponse().throws(exception)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is error with no data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Error<*>>()
                searchShopState.shouldBeNullOrEmpty()
            }

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }
        }

        Scenario("Search Shop with Empty Result") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModelEmptyList)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success and only contains empty search data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldOnlyHaveEmptySearchModel()
            }

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }
        }
    }

    Feature("Get Dynamic Filter After Search Shop First Page Successful") {
        createTestInstance()

        Scenario("Search Shop First Page Successful") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()

            Given("search shop API will be successful and return search shop data") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert get dynamic filter API called once") {
                dynamicFilterRepository.isExecuted()
            }
        }

        Scenario("Search Shop First Page Error") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()

            Given("search shop API will be successful and return search shop data") {
                val exception = Exception("Mock exception for testing error")

                searchShopFirstPageRepository.stubGetResponse().throws(exception)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert get dynamic filter API never called") {
                dynamicFilterRepository.isNeverExecuted()
            }
        }
    }

    Feature("Get Dynamic Filter") {
        createTestInstance()

        Scenario("Get Dynamic Filter Successful") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()

            Given("dynamic filter API will be successful") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert save dynamic filter is executed") {
                verify(exactly = 1) {
                    searchLocalCacheHandler.saveDynamicFilterModelLocally(
                            SearchShopViewModel.SCREEN_SEARCH_PAGE_SHOP_TAB, dynamicFilterModel)
                }
            }

            Then("assert dynamic filter response event is success (true)") {
                val getDynamicFilterResponseEvent = searchShopViewModel.getDynamicFilterEventLiveData().value

                getDynamicFilterResponseEvent?.peekContent() shouldBe true
            }
        }

        Scenario("Get Dynamic Filter Failed") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()

            Given("dynamic filter API will fail") {
                val exception = Exception("Mock exception for dynamic filter API")

                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().throws(exception)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert save dynamic filter is not executed") {
                verify(exactly = 0) {
                    searchLocalCacheHandler.saveDynamicFilterModelLocally(any(), any())
                }
            }

            Then("assert dynamic filter response event is failed (false)") {
                val getDynamicFilterResponseEvent = searchShopViewModel.getDynamicFilterEventLiveData().value

                getDynamicFilterResponseEvent?.peekContent() shouldBe false
            }
        }

        Scenario("Get Dynamic Filter Successful and Search Shop Has Empty Result") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModelEmptyList)
            }

            Given("dynamic filter API will be successful") {
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert save dynamic filter is executed") {
                verify(exactly = 1) {
                    searchLocalCacheHandler.saveDynamicFilterModelLocally(
                            SearchShopViewModel.SCREEN_SEARCH_PAGE_SHOP_TAB, dynamicFilterModel)
                }
            }

            Then("assert dynamic filter response event is success (true)") {
                val getDynamicFilterResponseEvent = searchShopViewModel.getDynamicFilterEventLiveData().value

                getDynamicFilterResponseEvent?.peekContent() shouldBe true
            }

            Then("assert search shop state is success and have updated Empty Search Model with Filter Data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldOnlyHaveEmptySearchModel()
                searchShopState.shouldHaveEmptySearchModelWithExpectedIsFilter(true)
            }
        }
    }

    Feature("Handle View Scroll to Load More") {
        createTestInstance()

        Scenario("View load more and visible, and has next page") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopLoadMoreRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page and has next page") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
                searchShopLoadMoreRepository.stubGetResponse().returns(searchMoreShopModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible =true)
            }

            Then("verify search more shop API is called once") {
                searchShopLoadMoreRepository.isExecuted()
            }
        }

        Scenario("View load more and visible, but does not have next page") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopLoadMoreRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page and does not have next page") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModelWithoutNextPage)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
            }

            Then("verify search more shop API is never called") {
                searchShopLoadMoreRepository.isNeverExecuted()
            }
        }

        Scenario("View load more but not visible") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopLoadMoreRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view has loaded first page and has next page") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view load more but not visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible = false)
            }

            Then("verify search more shop API is never called") {
                searchShopLoadMoreRepository.isNeverExecuted()
            }
        }

        Scenario("View load more twice and visible, but does not have next page after first load more") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopLoadMoreRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page and has next page") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API will return data with has next page is false") {
                searchShopLoadMoreRepository.stubGetResponse()
                        .returns(searchMoreShopModelWithoutNextPage)
                        .andThen(searchMoreShopModel)
            }

            When("handle view load more twice and visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
            }

            Then("verify search more shop API is only called once") {
                searchShopLoadMoreRepository.isExecuted()
            }
        }
    }

    Feature("Search Shop Load More") {
        createTestInstance()

        Scenario("Search Shop and Search More Shop Successful") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopLoadMoreRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page successfully") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API call will be successful and return search shop data") {
                searchShopLoadMoreRepository.stubGetResponse().returns(searchMoreShopModel)
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

            Then("assert has next page is true") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe true
            }
        }

        Scenario("Search Shop Successful and Search More Shop Successful Without Next Page") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopLoadMoreRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page successfully") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API call will be successful and return search shop data without next page") {
                searchShopLoadMoreRepository.stubGetResponse().returns(searchMoreShopModelWithoutNextPage)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(true)
            }

            Then("assert search shop state is success and contains data from search shop and search more shop without loading more view model") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveHeaderAndShopItemFromSecondToLastElement()
                searchShopState.shouldHaveShopItemCount(shopItemList.size + moreShopItemList.size)
            }

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }
        }

        Scenario("Search Shop Successful, but Search More Shop Error") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopLoadMoreRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page successfully") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API call will fail") {
                val exception = Exception("Mock exception for testing error")
                searchShopLoadMoreRepository.stubGetResponse().throws(exception)
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

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }
        }
    }

    Feature("Handle View Retry Search Shop") {
        createTestInstance()

        Scenario("Retry Search Shop After Error in Search Shop") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page error") {
                val exception = Exception("Mock exception for testing retry mechanism")

                searchShopFirstPageRepository.stubGetResponse()
                        .throws(exception)
                        .andThen(searchShopModel)

                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view retry search shop") {
                searchShopViewModel.onViewClickRetry()
            }

            Then("verify search shop API called twice") {
                searchShopFirstPageRepository.isExecuted(2)
            }

            Then("assert search shop state success after retry") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveHeaderAndLoadingMoreWithShopItemInBetween()
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }
        }

        Scenario("Retry Search Shop After Error in Search More Shop") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopLoadMoreRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first page successfully, but error during search shop second page") {
                val exception = Exception("Mock exception for testing retry mechanism")

                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)

                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)

                searchShopLoadMoreRepository.stubGetResponse()
                        .throws(exception)
                        .andThen(searchMoreShopModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
                searchShopViewModel.onViewLoadMore(true)
            }

            When("handle view retry search more shop") {
                searchShopViewModel.onViewClickRetry()
            }

            Then("verify search shop API called once, and search more shop API called twice") {
                searchShopFirstPageRepository.isExecuted()
                searchShopLoadMoreRepository.isExecuted(2)
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
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopLoadMoreRepository by memoized<Repository<SearchShopModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view search shop first and second page successfully") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
                searchShopLoadMoreRepository.stubGetResponse().returns(searchMoreShopModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
            }

            When("handle view reload search shop") {
                searchShopViewModel.onViewReloadData()
            }

            Then("verify search shop API is called twice and search more shop API is called once") {
                searchShopFirstPageRepository.isExecuted(2)
                searchShopLoadMoreRepository.isExecuted()
            }

            Then("verify dynamic filter API is called once") {
                dynamicFilterRepository.isExecuted(2)
            }

            Then("assert search shop state success after reload") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveHeaderAndLoadingMoreWithShopItemInBetween()
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }
        }
    }

    Feature("Handle View Open Filter Page") {
        createTestInstance()

        Scenario("Open Filter Page successfully") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view get dynamic filter successfully") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view open filter page") {
                searchShopViewModel.onViewOpenFilterPage()
            }

            Then("should post event success open filter page") {
                val openFilterPageEvent = searchShopViewModel.getOpenFilterPageEventLiveData().value

                openFilterPageEvent?.peekContent() shouldBe true
            }
        }

        Scenario("Open Filter Page but Filter Data does not exists") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view get dynamic filter successfully without filter data") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)

                val dynamicFilterModelWithoutFilterData = DynamicFilterModel()
                dynamicFilterModelWithoutFilterData.data = DataValue()
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModelWithoutFilterData)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view open filter page") {
                searchShopViewModel.onViewOpenFilterPage()
            }

            Then("should show error message indicating no filter data exists") {
                val openFilterPageEvent = searchShopViewModel.getOpenFilterPageEventLiveData().value

                openFilterPageEvent?.peekContent() shouldBe false
            }
        }

        Scenario("Open Filter Page after Get Dynamic Filter Successful and then Failed") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()

            Given("view get dynamic filter successfully") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("view get dynamic filter failed on second try") {
                val exception = Exception("Mock exception for dynamic filter API")

                dynamicFilterRepository.stubGetResponse().throws(exception)

                searchShopViewModel.onViewReloadData()
            }

            When("handle view open filter page") {
                searchShopViewModel.onViewOpenFilterPage()
            }

            Then("should show error message indicating no filter data exists") {
                val openFilterPageEvent = searchShopViewModel.getOpenFilterPageEventLiveData().value

                openFilterPageEvent?.peekContent() shouldBe false
            }
        }
    }

    Feature("Handle View Applying Filter") {
        createTestInstance()

        Scenario("Apply filter with query parameters") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()
            val queryParametersFromFilter = mutableMapOf<String, String>()

            Given("search shop and dynamic filter API call will be successful") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
            }

            Given("Query parameters from filter, simulate remove and add filter") {
                queryParametersFromFilter.putAll(searchShopViewModel.getSearchParameter().convertValuesToString())
                queryParametersFromFilter.remove(SearchApiConst.OFFICIAL)
                queryParametersFromFilter[SearchApiConst.FCITY] = "1,2,3"
            }

            When("handle view applying filter") {
                searchShopViewModel.onViewApplyFilter(queryParametersFromFilter)
            }

            Then("Search Parameter should be updated with query params from filter (Except START value)") {
                val searchParameterWithoutStart = searchShopViewModel.getSearchParameter().toMutableMap()
                searchParameterWithoutStart.remove(SearchApiConst.START)
                val queryParametersFromFilterWithoutStart = queryParametersFromFilter.toMutableMap()
                queryParametersFromFilterWithoutStart.remove(SearchApiConst.START)

                searchParameterWithoutStart shouldBe queryParametersFromFilterWithoutStart
            }

            Then("assert Search Parameter START parameter is 0") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter[SearchApiConst.START] shouldBe 0
            }

            Then("verify search shop and get dynamic filter API is called") {
                searchShopFirstPageRepository.isExecuted()
                dynamicFilterRepository.isExecuted()
            }
        }

        Scenario("Apply filter with null query parameters") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()
            lateinit var initialSearchParameter: Map<String, Any>

            Given("search shop and dynamic filter API call will be successful") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle view applying filter") {
                searchShopViewModel.onViewApplyFilter(null)
            }

            Then("Search Parameter does not get updated") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter shouldBe initialSearchParameter
            }
        }
    }

    Feature("Handle View Remove Selected Filter") {
        createTestInstance()

        Scenario("Remove selected filter with Option unique id") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()
            val selectedFilterOptionUniqueId = OptionHelper.constructUniqueId("official", "true", "Official Store")

            Given("search shop and dynamic filter API call will be successful") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilter(selectedFilterOptionUniqueId)
            }

            Then("Search Parameter should not contain the selected filter anymore") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter.containsKey("official") shouldBe false
            }

            Then("verify search shop and get dynamic filter API is called") {
                searchShopFirstPageRepository.isExecuted()
                dynamicFilterRepository.isExecuted()
            }
        }

        Scenario("Remove selected filter with null unique id") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopViewModel by memoized<SearchShopViewModel>()
            lateinit var initialSearchParameter: Map<String, Any>

            Given("search shop and dynamic filter API call will be successful") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilter(null)
            }

            Then("Search Parameter does not get updated") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter shouldBe initialSearchParameter
            }
        }

        Scenario("Remove selected option from filter with multiple options") {
            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
            val searchShopLoadMoreRepository by memoized<Repository<SearchShopModel>>()
            val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()
            val selectedFilterOptionUniqueId = jakartaOption.uniqueId

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model with search parameter contains location filter (${SearchApiConst.FCITY} = 1,2,3)") {
                val searchShopParameterWithCategoryFilter = mapOf<String, Any>(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.FCITY to "1,2,3"
                )

                searchShopViewModel = SearchShopViewModel(
                        TestDispatcherProvider(), searchShopParameterWithCategoryFilter,
                        searchShopFirstPageRepository, searchShopLoadMoreRepository, dynamicFilterRepository,
                        shopHeaderViewModelMapper, shopViewModelMapper,
                        searchLocalCacheHandler, userSession, localCacheHandler
                )
            }

            Given("search shop and dynamic filter API call will be successful") {
                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilter(selectedFilterOptionUniqueId)
            }

            Then("Search Parameter should only contain the remaining location filter (${SearchApiConst.FCITY} = 2,3)") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                val remainingLocationFilter = searchParameter[SearchApiConst.FCITY].toString().split(OptionHelper.VALUE_SEPARATOR)
                remainingLocationFilter.contains("1") shouldBe false
                remainingLocationFilter.contains("2") shouldBe true
                remainingLocationFilter.contains("3") shouldBe true
            }

            Then("verify search shop and get dynamic filter API is called") {
                searchShopFirstPageRepository.isExecuted()
                dynamicFilterRepository.isExecuted()
            }
        }

//        Scenario("Remove selected category filter") {
//            val searchShopFirstPageRepository by memoized<Repository<SearchShopModel>>()
//            val dynamicFilterRepository by memoized<Repository<DynamicFilterModel>>()
//            val searchShopLoadMoreRepository by memoized<Repository<SearchShopModel>>()
//            val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()
//            val selectedFilterOptionUniqueId = OptionHelper.constructUniqueId(SearchApiConst.SC, "13", "Handphone")
//
//            lateinit var searchShopViewModel: SearchShopViewModel
//
//            Given("search shop view model with search parameter contains category filter") {
//                val searchShopParameterWithCategoryFilter = mapOf<String, Any>(
//                        SearchApiConst.Q to "samsung",
//                        SearchApiConst.FCITY to "1,2,3"
//                )
//
//                searchShopViewModel = SearchShopViewModel(
//                        TestDispatcherProvider(), searchShopParameterWithCategoryFilter,
//                        searchShopFirstPageRepository, searchShopLoadMoreRepository, dynamicFilterRepository,
//                        shopHeaderViewModelMapper, shopViewModelMapper,
//                        searchLocalCacheHandler, userSession, localCacheHandler
//                )
//            }
//
//            Given("search shop and dynamic filter API call will be successful") {
//                searchShopFirstPageRepository.stubGetResponse().returns(searchShopModel)
//                dynamicFilterRepository.stubGetResponse().returns(dynamicFilterModel)
//            }
//
//            When("handle remove selected filter") {
//                searchShopViewModel.onViewRemoveSelectedFilter(selectedFilterOptionUniqueId)
//            }
//
//            Then("Search Parameter should not contain the category filter anymore") {
//                val searchParameter = searchShopViewModel.getSearchParameter()
//
//                searchParameter.containsKey("official") shouldBe false
//            }
//
//            Then("verify search shop and get dynamic filter API is called") {
//                searchShopFirstPageRepository.isExecuted()
//                dynamicFilterRepository.isExecuted()
//            }
//        }
    }
})

private fun Repository<*>.stubGetResponse(): MockKStubScope<Any?, Any?> {
    val it = this
    return coEvery { it.getResponse(any()) }
}

private fun Repository<*>.isNeverExecuted() {
    return this.isExecuted(0)
}

private fun Repository<*>.isExecuted(executionCount: Int = 1) {
    val it = this
    coVerify(exactly = executionCount) { it.getResponse(any()) }
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

private fun State<List<Visitable<*>>>?.shouldHaveEmptySearchModelWithExpectedIsFilter(expectedIsFilterActive: Boolean) {
    if (this?.data?.size == 0) {
        throw AssertionError("Search Shop State has no data, expected one EmptySearchViewModel with isFilter = $expectedIsFilterActive")
    }

    this?.data?.forEach {
        if (it is EmptySearchViewModel) {
            if (it.isFilterActive != expectedIsFilterActive) {
                throw AssertionError("EmptySearchViewModel isFilterActive = ${it.isFilterActive}, expected = $expectedIsFilterActive")
            }

            return
        }
    }
}