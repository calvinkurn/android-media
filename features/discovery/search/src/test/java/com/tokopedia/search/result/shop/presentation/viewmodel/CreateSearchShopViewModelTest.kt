package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.shouldBe
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class CreateSearchShopViewModelTest: Spek({

    InstantTaskExecutorRuleSpek(this)

    val userIdLoggedIn = "123456"
    val userIdNonLogin = "0"

    val localCacheGCMIDValue = "GCM_ID"

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
})