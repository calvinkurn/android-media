package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.authentication.AuthHelper
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.shouldBe
import io.mockk.every
import org.junit.Test

internal class CreateSearchShopDataViewTest: SearchShopDataViewTestFixtures() {

    private val userIdLoggedIn = "123456"
    private val userIdNonLogin = "0"
    private val localCacheGCMIDValue = "GCM_ID"

    override fun setUp() { /* no setup required */ }

    @Test
    fun `Create Search Shop View Model with non-logged in user`() {
        `Given User has not logged in`()
        `Given GCM ID value`()

        `When Create Search Shop View Model`()

        `Then Verify search parameter for non-login user`()
    }

    private fun `Given User has not logged in`() {
        every { userSession.isLoggedIn }.returns(false)
    }

    private fun `Given GCM ID value`() {
        every { userSession.deviceId }.returns(localCacheGCMIDValue)
    }

    private fun `When Create Search Shop View Model`() {
        searchShopViewModel = createSearchShopViewModel()
    }

    private fun `Then Verify search parameter for non-login user`() {
        val searchParameter = searchShopViewModel.getSearchParameter()

        searchParameter[SearchApiConst.UNIQUE_ID] shouldBe AuthHelper.getMD5Hash(localCacheGCMIDValue)
        searchParameter[SearchApiConst.USER_ID] shouldBe userIdNonLogin
        searchParameter[SearchApiConst.START] shouldBe SearchShopViewModel.START_ROW_FIRST_TIME_LOAD
    }

    @Test
    fun `Create Search Shop View Model with logged in user`() {
        `Given User has logged in`()
        `Given GCM ID value`()

        `When Create Search Shop View Model`()

        `Then Verify search parameter for logged in user`()
    }

    private fun `Given User has logged in`() {
        every { userSession.isLoggedIn }.returns(true)
        every { userSession.userId }.returns(userIdLoggedIn)
    }

    private fun `Then Verify search parameter for logged in user`() {
        val searchParameter = searchShopViewModel.getSearchParameter()

        searchParameter[SearchApiConst.UNIQUE_ID] shouldBe AuthHelper.getMD5Hash(userIdLoggedIn)
        searchParameter[SearchApiConst.USER_ID] shouldBe userIdLoggedIn
        searchParameter[SearchApiConst.START] shouldBe SearchShopViewModel.START_ROW_FIRST_TIME_LOAD
    }

    @Test
    fun `Create Search Shop View Model with empty parameter`() {
        val searchParameterWithoutQuery = mapOf<String, Any>()

        `Given User has logged in`()

        `When Create Search Shop View Model`(searchParameterWithoutQuery)

        `Then Verify search parameter for logged in user`()
        `Then verify getSearchParameterQuery is empty String`()
    }

    private fun `When Create Search Shop View Model`(parameter: Map<String, Any>) {
        searchShopViewModel = createSearchShopViewModel(parameter)
    }

    private fun `Then verify getSearchParameterQuery is empty String`() {
        searchShopViewModel.getSearchParameterQuery() shouldBe ""
    }
}