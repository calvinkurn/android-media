package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class CategorySetUserPreferenceTest: CategoryTestFixtures() {

    @Test
    fun `set user preference should give success result`() {
        val userPreferenceResponse = "userpreference/set-user-preference-2h.json"
            .jsonToObject<SetUserPreference.SetUserPreferenceResponse>()

        `Given user preference data`(userPreferenceResponse.data)

        `When user preference use case called`()

        `Then verify user preference use case called`()
        `Then verify the data`(userPreferenceResponse.data)
    }

    @Test
    fun `set user preference with null address data`() {
        `Given address data null`()

        `When user preference use case called`()

        `Then verify user preference use case not called`()
    }

    @Test
    fun `set user preference should give failed result`() {
        `Given an error result`(Throwable(message = "error guys"))

        `When user preference use case called`()

        `Then verify user preference use case called`()
        `Then verify the data`(Throwable(message = "error guys"))
    }

    private fun `Given user preference data`(userPreferenceData: SetUserPreference.SetUserPreferenceData) {
        coEvery { setUserPreferenceUseCase.execute(any(), any()) } returns userPreferenceData
    }

    private fun `Given an error result`(error: Throwable) {
        coEvery { setUserPreferenceUseCase.execute(any(), any()) } throws error
    }

    private fun `When user preference use case called`() {
        tokoNowCategoryViewModel.setUserPreference(anyString())
    }

    private fun `Then verify user preference use case called`() {
        coVerify { setUserPreferenceUseCase.execute(any(), any()) }
    }

    private fun `Then verify user preference use case not called`() {
        coVerify(exactly = 0) { setUserPreferenceUseCase.execute(any(), any()) }
    }

    private fun `Then verify the data`(data: SetUserPreference.SetUserPreferenceData) {
        tokoNowCategoryViewModel.setUserPreferenceLiveData
            .verifySuccessEquals(Success(data))
    }

    private fun `Then verify the data`(data: Throwable) {
        tokoNowCategoryViewModel.setUserPreferenceLiveData
            .verifyErrorEquals(Fail(data))
    }
}