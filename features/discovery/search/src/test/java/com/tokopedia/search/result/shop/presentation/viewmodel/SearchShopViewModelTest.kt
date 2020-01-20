package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.search.*
import com.tokopedia.search.result.*
import com.tokopedia.search.result.common.EventObserver
import com.tokopedia.search.result.common.State.Error
import com.tokopedia.search.result.common.State.Success
import com.tokopedia.search.result.common.UseCase
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandler
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.domain.model.SearchShopModel.AceSearchShop
import com.tokopedia.search.result.shop.domain.model.SearchShopModel.AceSearchShop.ShopItem
import com.tokopedia.search.result.shop.domain.model.SearchShopModel.AceSearchShop.ShopItem.ShopItemProduct
import com.tokopedia.search.result.shop.presentation.mapper.ShopViewModelMapperModule
import com.tokopedia.search.utils.convertValuesToString
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.TestBody
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
        it.add(ShopItem(id = "2", productList = shopItemProductList))
        it.add(ShopItem(id = "3", productList = shopItemProductList))
        it.add(ShopItem(id = "4", productList = shopItemProductList))
    }

    val moreShopItemList: List<ShopItem> = mutableListOf<ShopItem>().also {
        it.add(ShopItem(id = "5", productList = shopItemProductList))
        it.add(ShopItem(id = "6", productList = shopItemProductList))
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

    val cpmModel = CpmModel(cpmJSONObject)
    val notCpmShopModel = CpmModel(notCpmShopJsonObject)

    val searchShopModel = SearchShopModel(aceSearchShopWithNextPage, cpmModel)
    val searchShopModelWithoutNextPage = SearchShopModel(aceSearchShopWithoutNextPage, cpmModel)
    val searchShopModelWithoutCpm = SearchShopModel(aceSearchShopWithNextPage)
    val searchShopModelWithoutValidCpmShop = SearchShopModel(aceSearchShopWithNextPage, notCpmShopModel)
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
            OptionHelper.constructUniqueId(SearchApiConst.PMAX, "", "Harga maximum")
    )
    val priceFilter = Filter().also { filter ->
        filter.options = mutableListOf<Option>().also { optionList ->
            optionList.add(minPriceOption)
            optionList.add(maxPriceOption)
        }
    }
    val handphoneOption = OptionHelper.generateOptionFromUniqueId(
            OptionHelper.constructUniqueId(SearchApiConst.SC, "13", "Handphone")
    )
    val tvOption = OptionHelper.generateOptionFromUniqueId(
            OptionHelper.constructUniqueId(SearchApiConst.SC, "14", "TV")
    )
    val kulkasOption = OptionHelper.generateOptionFromUniqueId(
            OptionHelper.constructUniqueId(SearchApiConst.SC, "15", "Kulkas")
    )
    val categoryFilter = Filter().also { filter ->
        filter.options = mutableListOf<Option>().also { optionList ->
            optionList.add(handphoneOption)
            optionList.add(tvOption)
            optionList.add(kulkasOption)
        }
    }

    val dynamicFilterModel = DynamicFilterModel()
    dynamicFilterModel.data = DataValue()
    dynamicFilterModel.data.filter = mutableListOf<Filter>().also {
        it.add(shopFilter)
        it.add(locationFilter)
        it.add(priceFilter)
        it.add(categoryFilter)
    }

    val searchShopParameter = mapOf(
            SearchApiConst.Q to "samsung",
            SearchApiConst.OFFICIAL to true
    )

    val shopViewModelMapperModule = ShopViewModelMapperModule()

    val shopCpmViewModelMapper = shopViewModelMapperModule.provideShopCpmViewModelMapper()
    val shopTotalCountViewModelMapper = shopViewModelMapperModule.provideShopTotalCountViewModelMapper()
    val shopViewModelMapper = shopViewModelMapperModule.provideShopViewModelMapper()

    val userIdLoggedIn = "123456"
    val userIdNonLogin = "0"

    val localCacheGCMIDValue = "GCM_ID"

    @Suppress("UNUSED_VARIABLE")
    fun FeatureBody.createTestInstance() {
        val searchShopFirstPageUseCase by memoized {
            mockk<UseCase<SearchShopModel>>(relaxed = true)
        }

        val searchShopLoadMoreUseCase by memoized {
            mockk<UseCase<SearchShopModel>>(relaxed = true)
        }

        val getDynamicFilterUseCase by memoized {
            mockk<UseCase<DynamicFilterModel>>(relaxed = true)
        }

        val searchLocalCacheHandler by memoized {
            mockk<SearchLocalCacheHandler>(relaxed = true)
        }

        val userSession by memoized {
            mockk<UserSessionInterface>(relaxed = true)
        }

        val localCacheHandler by memoized {
            mockk<LocalCacheHandler>(relaxed = true)
        }
    }

    fun TestBody.createSearchShopViewModel(parameter: Map<String, Any> = searchShopParameter): SearchShopViewModel {
        val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
        val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
        val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()
        val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()
        val userSession by memoized<UserSessionInterface>()
        val localCacheHandler by memoized<LocalCacheHandler>()

        return SearchShopViewModel(
                TestDispatcherProvider(), parameter,
                searchShopFirstPageUseCase, searchShopLoadMoreUseCase, getDynamicFilterUseCase,
                shopCpmViewModelMapper, shopTotalCountViewModelMapper, shopViewModelMapper,
                searchLocalCacheHandler, userSession, localCacheHandler
        )
    }

    Feature("Create Search Shop View Model") {
        createTestInstance()

        Scenario("Create Search Shop View Model with non-logged in user") {
            val userSession by memoized<UserSessionInterface>()
            val localCacheHandler by memoized<LocalCacheHandler>()
            lateinit var searchShopViewModel: SearchShopViewModel

            Given("User has not logged in") {
                every { userSession.isLoggedIn }.returns(false)
            }

            Given("GCM ID value from local cache") {
                every { localCacheHandler.getString(eq(SearchConstant.GCM.GCM_ID), "") }.returns(localCacheGCMIDValue)
            }

            When("Create Search Shop View Model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Then("Verify search parameter is updated properly") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter[SearchApiConst.UNIQUE_ID] shouldBe AuthHelper.getMD5Hash(localCacheGCMIDValue)
                searchParameter[SearchApiConst.USER_ID] shouldBe userIdNonLogin
                searchParameter[SearchApiConst.START] shouldBe SearchShopViewModel.START_ROW_FIRST_TIME_LOAD
            }
        }

        Scenario("Create Search Shop View Model with logged in user") {
            val userSession by memoized<UserSessionInterface>()
            val localCacheHandler by memoized<LocalCacheHandler>()
            lateinit var searchShopViewModel: SearchShopViewModel

            Given("User has logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userIdLoggedIn)
            }

            Given("GCM ID value from local cache") {
                every { localCacheHandler.getString(eq(SearchConstant.GCM.GCM_ID), "") }.returns(localCacheGCMIDValue)
            }

            When("Create Search Shop View Model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Then("Verify search parameter is updated properly") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter[SearchApiConst.UNIQUE_ID] shouldBe AuthHelper.getMD5Hash(userIdLoggedIn)
                searchParameter[SearchApiConst.USER_ID] shouldBe userIdLoggedIn
                searchParameter[SearchApiConst.START] shouldBe SearchShopViewModel.START_ROW_FIRST_TIME_LOAD
            }
        }

        Scenario("Create Search Shop View Model with empty parameter") {
            val userSession by memoized<UserSessionInterface>()
            val searchParameterWithoutQuery = mapOf<String, Any>()
            lateinit var searchShopViewModel: SearchShopViewModel

            Given("User has logged in") {
                every { userSession.isLoggedIn }.returns(true)
                every { userSession.userId }.returns(userIdLoggedIn)
            }

            When("Create Search Shop View Model") {
                searchShopViewModel = createSearchShopViewModel(searchParameterWithoutQuery)
            }

            Then("Verify search parameter is updated properly") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter[SearchApiConst.UNIQUE_ID] shouldBe AuthHelper.getMD5Hash(userIdLoggedIn)
                searchParameter[SearchApiConst.USER_ID] shouldBe userIdLoggedIn
                searchParameter[SearchApiConst.START] shouldBe SearchShopViewModel.START_ROW_FIRST_TIME_LOAD
            }

            Then("verify getSearchParameterQuery is empty String") {
                searchShopViewModel.getSearchParameterQuery() shouldBe ""
            }
        }
    }

    Feature("Handle View Created") {
        createTestInstance()

        Scenario("View is created and parameter active tab is ${ SearchConstant.ActiveTab.SHOP }") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model with parameter has active tab key = shop") {
                val searchShopParameterWithActiveTab = mapOf(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.OFFICIAL to true,
                        SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.SHOP
                )

                searchShopViewModel = createSearchShopViewModel(searchShopParameterWithActiveTab)
            }

            Given("search shop API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view created") {
                searchShopViewModel.onViewCreated()
            }

            Then("verify search shop and dynamic filter API is called once") {
                searchShopFirstPageUseCase.isExecuted()
                getDynamicFilterUseCase.isExecuted()
            }
        }

        Scenario("View is created multiple times and parameter active tab is ${ SearchConstant.ActiveTab.SHOP }") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model with parameter has active tab key = shop") {
                val searchShopParameterWithActiveTab = mapOf(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.OFFICIAL to true,
                        SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.SHOP
                )

                searchShopViewModel = createSearchShopViewModel(searchShopParameterWithActiveTab)
            }

            Given("search shop API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view created multiple times") {
                searchShopViewModel.onViewCreated()
                searchShopViewModel.onViewCreated()
                searchShopViewModel.onViewCreated()
            }

            Then("verify search shop and dynamic filter API is called once") {
                searchShopFirstPageUseCase.isExecuted()
                getDynamicFilterUseCase.isExecuted()
            }
        }
    }

    Feature("Handle View Visibility Changed") {
        createTestInstance()

        Scenario("View is visible and added") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("verify search shop and dynamic filter API is called once") {
                searchShopFirstPageUseCase.isExecuted()
                getDynamicFilterUseCase.isExecuted()
            }
        }

        Scenario("View is not yet visible, or not yet added") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
            }

            When("handle view not visible or not added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = false)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = true)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = false)
            }

            Then("verify search shop API is never called") {
                searchShopFirstPageUseCase.isNeverExecuted()
            }
        }

        Scenario("View is visible and added more than once") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added, and then not visible, then visible again") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = false, isViewAdded = true)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("verify search shop and dynamic filter API is only called once") {
                searchShopFirstPageUseCase.isExecuted()
                getDynamicFilterUseCase.isExecuted()
            }
        }

        Scenario("View is created with parameter active tab is ${ SearchConstant.ActiveTab.SHOP }, and then view is visible") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model with parameter has active tab key = shop") {
                val searchShopParameterWithActiveTab = mapOf(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.OFFICIAL to true,
                        SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.SHOP
                )

                searchShopViewModel = createSearchShopViewModel(searchShopParameterWithActiveTab)
            }

            Given("view is already created") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

                searchShopViewModel.onViewCreated()
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("verify search shop and dynamic filter API is called once") {
                searchShopFirstPageUseCase.isExecuted()
                getDynamicFilterUseCase.isExecuted()
            }
        }
    }

    Feature("Search Shop First Page") {
        createTestInstance()

        Scenario("Search Shop First Page Successful") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            var searchShopFirstPagePerformanceMonitoringIsStarted = false
            var searchShopFirstPagePerformanceMonitoringIsEnded = false
            val searchShopFirstPagePerformanceMonitoringEventObserver = EventObserver<Boolean> {
                when(it) {
                    true -> searchShopFirstPagePerformanceMonitoringIsStarted = true
                    false -> searchShopFirstPagePerformanceMonitoringIsEnded = true
                }
            }

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will be successful and return search shop data") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("search shop first page performance monitoring observer") {
                searchShopViewModel.getSearchShopFirstPagePerformanceMonitoringEventLiveData().observeForever(
                        searchShopFirstPagePerformanceMonitoringEventObserver
                )
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop performance monitoring is started and ended") {
                searchShopFirstPagePerformanceMonitoringIsStarted shouldBe true
                searchShopFirstPagePerformanceMonitoringIsEnded shouldBe true
            }

            Then("assert search shop state is success and contains search shop data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }

            Then("should post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking?.size shouldBe shopItemList.size
            }

            Then("should post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking?.size shouldBe shopItemList.size * shopItemProductList.size
            }

            Then("should not post empty search tracking event") {
                val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

                emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe null
            }

            Then("assert has next page is true") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe true
            }
        }

        Scenario("Search Shop First Page Successful Without Next Page") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will be successful and return search shop data without next page") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelWithoutNextPage)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success and contains search shop data without loading more view model") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithoutLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }

            Then("should post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking?.size shouldBe shopItemList.size
            }

            Then("should post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking?.size shouldBe shopItemList.size * shopItemProductList.size
            }

            Then("should not post empty search tracking event") {
                val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

                emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe null
            }

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }
        }

        Scenario("Search Shop First Page Successful Without CPM") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will be successful and return search shop data without CPM") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelWithoutCpm)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success and contains search shop data without CPM") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithoutCpmViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }

            Then("should post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking?.size shouldBe shopItemList.size
            }

            Then("should post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking?.size shouldBe shopItemList.size * shopItemProductList.size
            }

            Then("should not post empty search tracking event") {
                val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

                emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe null
            }

            Then("assert has next page is true") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe true
            }
        }

        Scenario("Search Shop First Page Successful Without Valid CPM Shop") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will be successful and return search shop data without Valid CPM Shop") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelWithoutValidCpmShop)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success and contains search shop data without CPM Shop") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithoutCpmViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }

            Then("should post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking?.size shouldBe shopItemList.size
            }

            Then("should post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking?.size shouldBe shopItemList.size * shopItemProductList.size
            }

            Then("should not post empty search tracking event") {
                val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

                emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe null
            }

            Then("assert has next page is true") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe true
            }
        }

        Scenario("Search Shop First Page Error") {
            val exception = TestException()
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API call will fail") {
                searchShopFirstPageUseCase.stubExecute().throws(exception)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert exception print stack trace is called") {
                exception.isStackTracePrinted shouldBe true
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
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert search shop state is success and only contains empty search data") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldOnlyHaveEmptySearchModel()
            }

            Then("should post empty search tracking event") {
                val emptySearchTrackingEvent = searchShopViewModel.getEmptySearchTrackingEventLiveData().value

                emptySearchTrackingEvent?.getContentIfNotHandled() shouldBe true
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
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return search shop data") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert get dynamic filter API called once") {
                getDynamicFilterUseCase.isExecuted()
            }
        }

        Scenario("Search Shop First Page Error") {
            val exception = TestException()
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return search shop data") {
                searchShopFirstPageUseCase.stubExecute().throws(exception)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert exception print stack trace is called") {
                exception.isStackTracePrinted shouldBe true
            }

            Then("assert get dynamic filter API never called") {
                getDynamicFilterUseCase.isNeverExecuted()
            }
        }
    }

    Feature("Get Dynamic Filter") {
        createTestInstance()

        Scenario("Get Dynamic Filter Successful") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("dynamic filter API will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
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

                getDynamicFilterResponseEvent?.getContentIfNotHandled() shouldBe true
            }
        }

        Scenario("Get Dynamic Filter Failed") {
            val exception = TestException()
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("dynamic filter API will fail") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().throws(exception)
            }

            When("handle view is visible and added") {
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Then("assert exception print stack trace is called") {
                exception.isStackTracePrinted shouldBe true
            }

            Then("assert save dynamic filter is not executed") {
                verify(exactly = 0) {
                    searchLocalCacheHandler.saveDynamicFilterModelLocally(any(), any())
                }
            }

            Then("assert dynamic filter response event is failed (false)") {
                val getDynamicFilterResponseEvent = searchShopViewModel.getDynamicFilterEventLiveData().value

                getDynamicFilterResponseEvent?.getContentIfNotHandled() shouldBe false
            }
        }

        Scenario("Get Dynamic Filter Successful and Search Shop Has Empty Result") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchLocalCacheHandler by memoized<SearchLocalCacheHandler>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
            }

            Given("dynamic filter API will be successful") {
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
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

                getDynamicFilterResponseEvent?.getContentIfNotHandled() shouldBe true
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
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page and has next page") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopLoadMoreUseCase.stubExecute().returns(searchMoreShopModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible =true)
            }

            Then("verify search more shop API is called once") {
                searchShopLoadMoreUseCase.isExecuted()
            }
        }

        Scenario("View load more and visible, but does not have next page") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page and does not have next page") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelWithoutNextPage)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
            }

            Then("verify search more shop API is never called") {
                searchShopLoadMoreUseCase.isNeverExecuted()
            }
        }

        Scenario("View load more but not visible") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view has loaded first page and has next page") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view load more but not visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible = false)
            }

            Then("verify search more shop API is never called") {
                searchShopLoadMoreUseCase.isNeverExecuted()
            }
        }

        Scenario("View load more twice and visible, but does not have next page after first load more") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page and has next page") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API will return data with has next page is false") {
                searchShopLoadMoreUseCase.stubExecute()
                        .returns(searchMoreShopModelWithoutNextPage)
                        .andThen(searchMoreShopModel)
            }

            When("handle view load more twice and visible") {
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
            }

            Then("verify search more shop API is only called once") {
                searchShopLoadMoreUseCase.isExecuted()
            }
        }
    }

    Feature("Search Shop Load More") {
        createTestInstance()

        Scenario("Search Shop and Search More Shop Successful") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page successfully") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API call will be successful and return search shop data") {
                searchShopLoadMoreUseCase.stubExecute().returns(searchMoreShopModel)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(true)
            }

            Then("assert search shop state is success and contains data from search shop and search more shop") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size + moreShopItemList.size)
            }

            Then("should post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking?.size shouldBe moreShopItemList.size
            }

            Then("should post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking?.size shouldBe moreShopItemList.size * shopItemProductList.size
            }

            Then("assert has next page is true") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe true
            }
        }

        Scenario("Search Shop Successful and Search More Shop Successful Without Next Page") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page successfully") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API call will be successful and return search shop data without next page") {
                searchShopLoadMoreUseCase.stubExecute().returns(searchMoreShopModelWithoutNextPage)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(true)
            }

            Then("assert search shop state is success and contains data from search shop and search more shop without loading more view model") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithoutLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size + moreShopItemList.size)
            }

            Then("should post shop item impression tracking event") {
                val shopItemImpressionTrackingEventLiveData = searchShopViewModel.getShopItemImpressionTrackingEventLiveData().value

                val shopItemImpressionTracking = shopItemImpressionTrackingEventLiveData?.getContentIfNotHandled()
                shopItemImpressionTracking?.size shouldBe moreShopItemList.size
            }

            Then("should post product preview impression tracking event") {
                val productPreviewImpressionTrackingEventLiveData = searchShopViewModel.getProductPreviewImpressionTrackingEventLiveData().value

                val productPreviewImpressionTracking = productPreviewImpressionTrackingEventLiveData?.getContentIfNotHandled()
                productPreviewImpressionTracking?.size shouldBe moreShopItemList.size * shopItemProductList.size
            }

            Then("assert has next page is false") {
                val hasNextPage = searchShopViewModel.getHasNextPage()

                hasNextPage shouldBe false
            }
        }

        Scenario("Search Shop Successful, but Search More Shop Error") {
            val exception = TestException()
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page successfully") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("search more shop API call will fail") {
                searchShopLoadMoreUseCase.stubExecute().throws(exception)
            }

            When("handle view load more and visible") {
                searchShopViewModel.onViewLoadMore(true)
            }

            Then("assert search shop state is error, but still contains data from search shop") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<Error<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }

            Then("assert exception print stack trace is called") {
                exception.isStackTracePrinted shouldBe true
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
            val exception = TestException()
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page error") {
                searchShopFirstPageUseCase.stubExecute()
                        .throws(exception)
                        .andThen(searchShopModel)

                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view retry search shop") {
                searchShopViewModel.onViewClickRetry()
            }

            Then("verify search shop API called twice") {
                searchShopFirstPageUseCase.isExecuted(2)
            }

            Then("assert search shop state success after retry") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }
        }

        Scenario("Retry Search Shop After Error in Search More Shop") {
            val exception = TestException()
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page successfully, but error during search shop second page") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)

                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

                searchShopLoadMoreUseCase.stubExecute()
                        .throws(exception)
                        .andThen(searchMoreShopModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
                searchShopViewModel.onViewLoadMore(true)
            }

            When("handle view retry search more shop") {
                searchShopViewModel.onViewClickRetry()
            }

            Then("verify search shop API called once, and search more shop API called twice") {
                searchShopFirstPageUseCase.isExecuted()
                searchShopLoadMoreUseCase.isExecuted(2)
            }

            Then("assert search shop state success after retry") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size + moreShopItemList.size)
            }
        }
    }

    Feature("Handle View Reload Search Shop") {
        createTestInstance()

        Scenario("Reload Search Shop After Search Shop and Search More Shop") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val searchShopLoadMoreUseCase by memoized<UseCase<SearchShopModel>>()
            var searchShopFirstPagePerformanceMonitoringIsStarted = false
            var searchShopFirstPagePerformanceMonitoringIsEnded = false
            val searchShopFirstPagePerformanceMonitoringEventObserver = EventObserver<Boolean> {
                when(it) {
                    true -> searchShopFirstPagePerformanceMonitoringIsStarted = true
                    false -> searchShopFirstPagePerformanceMonitoringIsEnded = true
                }
            }

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first and second page successfully") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
                searchShopLoadMoreUseCase.stubExecute().returns(searchMoreShopModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
                searchShopViewModel.onViewLoadMore(isViewVisible = true)
            }

            Given("search shop first page performance monitoring observer") {
                searchShopViewModel.getSearchShopFirstPagePerformanceMonitoringEventLiveData().observeForever(
                        searchShopFirstPagePerformanceMonitoringEventObserver
                )
            }

            When("handle view reload search shop") {
                searchShopViewModel.onViewReloadData()
            }

            Then("verify search shop API is called twice and search more shop API is called once") {
                searchShopFirstPageUseCase.isExecuted(2)
                searchShopLoadMoreUseCase.isExecuted()
            }

            Then("verify dynamic filter API is called once") {
                getDynamicFilterUseCase.isExecuted(2)
            }

            Then("assert search shop performance monitoring is started and ended") {
                searchShopFirstPagePerformanceMonitoringIsStarted shouldBe true
                searchShopFirstPagePerformanceMonitoringIsEnded shouldBe true
            }

            Then("assert search shop state success after reload") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }
        }

        Scenario("Reload Search Shop After Search Shop First Page Gives Empty Result") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view search shop first page successfully with empty result") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList).andThen(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view reload search shop") {
                searchShopViewModel.onViewReloadData()
            }

            Then("verify search shop API is called twice and search more shop API is called once") {
                searchShopFirstPageUseCase.isExecuted(2)
            }

            Then("verify dynamic filter API is called once") {
                getDynamicFilterUseCase.isExecuted(2)
            }

            Then("assert search shop state success after reload") {
                val searchShopState = searchShopViewModel.getSearchShopLiveData().value
                val query = searchShopViewModel.getSearchParameterQuery()

                searchShopState.shouldBeInstanceOf<Success<*>>()
                searchShopState.shouldHaveCorrectVisitableListWithLoadingMoreViewModel(query)
                searchShopState.shouldHaveShopItemCount(shopItemList.size)
            }
        }
    }

    Feature("Handle View Open Filter Page") {
        createTestInstance()

        Scenario("Open Filter Page successfully") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view get dynamic filter successfully") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view open filter page") {
                searchShopViewModel.onViewOpenFilterPage()
            }

            Then("should post event success open filter page") {
                val openFilterPageEvent = searchShopViewModel.getOpenFilterPageEventLiveData().value

                openFilterPageEvent?.getContentIfNotHandled() shouldBe true
            }
        }

        Scenario("Open Filter Page but Filter Data does not exists") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view get dynamic filter successfully without filter data") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)

                val dynamicFilterModelWithoutFilterData = DynamicFilterModel()
                dynamicFilterModelWithoutFilterData.data = DataValue()
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModelWithoutFilterData)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            When("handle view open filter page") {
                searchShopViewModel.onViewOpenFilterPage()
            }

            Then("should show error message indicating no filter data exists") {
                val openFilterPageEvent = searchShopViewModel.getOpenFilterPageEventLiveData().value

                openFilterPageEvent?.getContentIfNotHandled() shouldBe false
            }
        }

        Scenario("Open Filter Page after Get Dynamic Filter Successful and then Failed") {
            val exception = TestException()
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view get dynamic filter successfully") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)

                searchShopViewModel.onViewVisibilityChanged(isViewVisible = true, isViewAdded = true)
            }

            Given("view get dynamic filter failed on second try") {
                getDynamicFilterUseCase.stubExecute().throws(exception)

                searchShopViewModel.onViewReloadData()
            }

            When("handle view open filter page") {
                searchShopViewModel.onViewOpenFilterPage()
            }

            Then("should show error message indicating no filter data exists") {
                val openFilterPageEvent = searchShopViewModel.getOpenFilterPageEventLiveData().value

                openFilterPageEvent?.getContentIfNotHandled() shouldBe false
            }
        }
    }

    Feature("Handle View Applying Filter") {
        createTestInstance()

        Scenario("Apply filter with query parameters") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val queryParametersFromFilter = mutableMapOf<String, String>()

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("query parameters from filter, simulate remove and add filter") {
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

            Then("verify search shop and get dynamic filter API is called twice for load first page and apply filter") {
                searchShopFirstPageUseCase.isExecuted(2)
                getDynamicFilterUseCase.isExecuted(2)
            }
        }

        Scenario("Apply filter with null query parameters") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            lateinit var initialSearchParameter: Map<String, Any>
            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop and dynamic filter API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
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

    Feature("Handle View Remove Selected Filter after Empty Search") {
        createTestInstance()

        Scenario("Remove selected filter with Option's unique id") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val selectedFilterOptionUniqueId = OptionHelper.constructUniqueId(SearchApiConst.OFFICIAL, "true", "Official Store")
            lateinit var initialSearchParameter: Map<String, Any>

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
            }

            Given("dynamic filter API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilterAfterEmptySearch(selectedFilterOptionUniqueId)
            }

            Then("Search Parameter should not contain the selected filter anymore") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter shouldNotContain SearchApiConst.OFFICIAL
            }

            Then("Search Parameter should still contain other non-selected filter") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                initialSearchParameter.filter { it.key != SearchApiConst.OFFICIAL }.forEach { (key, value) ->
                    searchParameter[key] shouldBe value
                }
            }

            Then("verify search shop and get dynamic filter API is called twice for load first page and remove filter") {
                searchShopFirstPageUseCase.isExecuted(2)
                getDynamicFilterUseCase.isExecuted(2)
            }
        }

        Scenario("Remove selected filter with null unique id") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            lateinit var initialSearchParameter: Map<String, Any>

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
            }

            Given("dynamic filter API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilterAfterEmptySearch(null)
            }

            Then("Search Parameter should not change") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter shouldBe initialSearchParameter
            }
        }

        Scenario("Remove selected option from filter with multiple options") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val selectedFilterOptionUniqueId = jakartaOption.uniqueId
            lateinit var initialSearchParameter: Map<String, Any>

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model with search parameter contains location filter (${SearchApiConst.FCITY} = 1,2,3)") {
                val searchShopParameterWithLocationFilter = mapOf<String, Any>(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.FCITY to "1,2,3"
                )

                searchShopViewModel = createSearchShopViewModel(searchShopParameterWithLocationFilter)
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
            }

            Given("dynamic filter API call will be successful") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilterAfterEmptySearch(selectedFilterOptionUniqueId)
            }

            Then("Search Parameter should only contain the remaining location filter (${SearchApiConst.FCITY} = 2,3)") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                val remainingLocationFilter = searchParameter[SearchApiConst.FCITY].toString().split(OptionHelper.VALUE_SEPARATOR)

                remainingLocationFilter shouldNotContain "1"
                remainingLocationFilter shouldContain "2"
                remainingLocationFilter shouldContain "3"
            }

            Then("Search Parameter should still contain other non-selected filter") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                initialSearchParameter.filter { it.key != SearchApiConst.FCITY }.forEach { (key, value) ->
                    searchParameter[key] shouldBe value
                }
            }

            Then("verify search shop and get dynamic filter API is called twice for load first page and remove filter") {
                searchShopFirstPageUseCase.isExecuted(2)
                getDynamicFilterUseCase.isExecuted(2)
            }
        }

        Scenario("Remove selected category filter option") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val selectedFilterOptionUniqueId = handphoneOption.uniqueId
            lateinit var initialSearchParameter: Map<String, Any>

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model with search parameter contains category filter (${SearchApiConst.SC} = 13,14,15))") {
                val searchShopParameterWithCategoryFilter = mapOf<String, Any>(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.SC to "13,14,15"
                )

                searchShopViewModel = createSearchShopViewModel(searchShopParameterWithCategoryFilter)
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
            }

            Given("dynamic filter API call will be successful") {
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilterAfterEmptySearch(selectedFilterOptionUniqueId)
            }

            Then("Search Parameter should not contain any category filter anymore") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter shouldNotContain SearchApiConst.SC
            }

            Then("Search Parameter should still contain other non-selected filter") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                initialSearchParameter.filter { it.key != SearchApiConst.SC }.forEach { (key, value) ->
                    searchParameter[key] shouldBe value
                }
            }

            Then("verify search shop and get dynamic filter API is called twice for load first page and remove filter") {
                searchShopFirstPageUseCase.isExecuted(2)
                getDynamicFilterUseCase.isExecuted(2)
            }
        }

        Scenario("Remove selected price filter option") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val selectedFilterOptionUniqueId = OptionHelper.constructUniqueId(SearchApiConst.PMIN, "", "Filter Harga")
            lateinit var initialSearchParameter: Map<String, Any>

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model with search parameter contains Price filter (${SearchApiConst.PMIN} and ${SearchApiConst.PMAX})") {
                val searchShopParameterWithPriceFilter = mapOf(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.PMIN to 1300,
                        SearchApiConst.PMAX to 1000000
                )

                searchShopViewModel = createSearchShopViewModel(searchShopParameterWithPriceFilter)
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
            }

            Given("dynamic filter API call will be successful") {
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilterAfterEmptySearch(selectedFilterOptionUniqueId)
            }

            Then("Search Parameter should not contain any price filter") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter shouldNotContain SearchApiConst.PMIN
                searchParameter shouldNotContain SearchApiConst.PMAX
            }

            Then("Search Parameter should still contain other non-selected filter") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                initialSearchParameter.filter { it.key != SearchApiConst.PMIN && it.key != SearchApiConst.PMAX }.forEach { (key, value) ->
                    searchParameter[key] shouldBe value
                }
            }

            Then("verify search shop and get dynamic filter API is called twice for load first page and remove filter") {
                searchShopFirstPageUseCase.isExecuted(2)
                getDynamicFilterUseCase.isExecuted(2)
            }
        }

        Scenario("Remove selected filter but search shop is not empty search") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            val selectedFilterOptionUniqueId = OptionHelper.constructUniqueId(SearchApiConst.OFFICIAL, "true", "Official Store")
            lateinit var initialSearchParameter: Map<String, Any>

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return data") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
            }

            Given("dynamic filter API call will be successful and return data") {
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            Given("retrieve initial search parameter") {
                initialSearchParameter = searchShopViewModel.getSearchParameter()
            }

            When("handle remove selected filter") {
                searchShopViewModel.onViewRemoveSelectedFilterAfterEmptySearch(selectedFilterOptionUniqueId)
            }

            Then("Search Parameter should not change. Remove selected filter only applicable during empty search") {
                val searchParameter = searchShopViewModel.getSearchParameter()

                searchParameter shouldBe initialSearchParameter
            }
        }
    }

    Feature("Handle View Get Active Filter as List of Option After Empty Search") {
        createTestInstance()

        Scenario("Get active filter as option list") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            lateinit var activeFilterOptionList: List<Option>
            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return empty search shop list") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModelEmptyList)
            }

            Given("dynamic filter API call will be successful and return data") {
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            When("get active filter as option list") {
                activeFilterOptionList = searchShopViewModel.getActiveFilterOptionListForEmptySearch()
            }

            Then("Active Filter Option List should be generated by comparing search parameter map and filter list from API") {
                val expectedActiveFilterOptionList = mutableListOf<Option>().also {
                    it.add(officialOption)
                }

                activeFilterOptionList shouldHaveSize expectedActiveFilterOptionList.size
                expectedActiveFilterOptionList.forEach { expectedOption ->
                    activeFilterOptionList shouldContain expectedOption
                }
            }
        }

        Scenario("Get active filter as option list but search shop is not empty search") {
            val searchShopFirstPageUseCase by memoized<UseCase<SearchShopModel>>()
            val getDynamicFilterUseCase by memoized<UseCase<DynamicFilterModel>>()
            lateinit var activeFilterOptionList: List<Option>
            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            Given("search shop API will be successful and return data") {
                searchShopFirstPageUseCase.stubExecute().returns(searchShopModel)
            }

            Given("dynamic filter API call will be successful and return data") {
                getDynamicFilterUseCase.stubExecute().returns(dynamicFilterModel)
            }

            Given("view get search shop first page and get dynamic filter successfully") {
                searchShopViewModel.onViewVisibilityChanged(isViewAdded = true, isViewVisible = true)
            }

            When("get active filter as option list") {
                activeFilterOptionList = searchShopViewModel.getActiveFilterOptionListForEmptySearch()
            }

            Then("Active Filter Option List should be empty. Get Active Filter as Option List only applicable during empty search") {
                activeFilterOptionList.size shouldBe 0
            }
        }
    }
})

