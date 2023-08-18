package com.tokopedia.tokopedianow.oldcategory.presentation.viewmodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.domain.model.SetUserPreference
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert
import org.junit.Test

class CategorySwitchServiceTest: CategoryTestFixtures() {

    @Test
    fun `when service type is 20m and bottomsheet not yet been shown should show bottomsheet`() {
        val currentServiceType = "15m"
        val localCacheModel = LocalCacheModel(warehouse_id = "123", service_type = currentServiceType)

        `Given choose address data`(localCacheModel)

        `When view reload page`()

        val needToShowOnBoardBottomSheet = tokoNowCategoryViewModel.needToShowOnBoardBottomSheet(has20mBottomSheetBeenShown = false)
        Assert.assertEquals(true, needToShowOnBoardBottomSheet)
    }

    @Test
    fun `when service type is 20m and bottomsheet already been shown should not show bottomsheet`() {
        val currentServiceType = "15m"
        val localCacheModel = LocalCacheModel(service_type = currentServiceType)

        `Given choose address data`(localCacheModel)

        `When view reload page`()

        val needToShowOnBoardBottomSheet = tokoNowCategoryViewModel.needToShowOnBoardBottomSheet(has20mBottomSheetBeenShown = true)
        Assert.assertEquals(false, needToShowOnBoardBottomSheet)
    }

    @Test
    fun `when service type is 2h and bottomsheet already been shown should not show bottomsheet`() {
        val currentServiceType = "2h"
        val localCacheModel = LocalCacheModel(service_type = currentServiceType)

        `Given choose address data`(localCacheModel)

        `When view reload page`()

        val needToShowOnBoardBottomSheet = tokoNowCategoryViewModel.needToShowOnBoardBottomSheet(has20mBottomSheetBeenShown = true)
        Assert.assertEquals(false, needToShowOnBoardBottomSheet)
    }

    @Test
    fun `when service type is 2h and bottomsheet not yet been shown should not show bottomsheet`() {
        val currentServiceType = "2h"
        val localCacheModel = LocalCacheModel(service_type = currentServiceType)

        `Given choose address data`(localCacheModel)

        `When view reload page`()

        val needToShowOnBoardBottomSheet = tokoNowCategoryViewModel.needToShowOnBoardBottomSheet(has20mBottomSheetBeenShown = false)
        Assert.assertEquals(false, needToShowOnBoardBottomSheet)
    }

    @Test
    fun `when address data is null should not show bottomsheet`() {
        `Given address data null`()

        val needToShowOnBoardBottomSheet = tokoNowCategoryViewModel.needToShowOnBoardBottomSheet(has20mBottomSheetBeenShown = true)

        Assert.assertEquals(false, needToShowOnBoardBottomSheet)
    }

    @Test
    fun `switch service with current service type 2h`() {
        val currentServiceType = "2h"
        val userPreferenceResponse = "userpreference/set-user-preference-15m.json"
            .jsonToObject<SetUserPreference.SetUserPreferenceResponse>()

        val localCacheModel = LocalCacheModel(service_type = currentServiceType)

        `Given choose address data`(localCacheModel)
        `Given user preference data`(userPreferenceResponse.data)

        `When view reload page`()
        `When switch service called`()

        `Then verify user preference use case called`(localCacheModel, "15m")
        `Then verify the data`(userPreferenceResponse.data)
    }

    @Test
    fun `switch service with current service type 15m`() {
        val currentServiceType = "15m"
        val userPreferenceResponse = "userpreference/set-user-preference-2h.json"
            .jsonToObject<SetUserPreference.SetUserPreferenceResponse>()

        val localCacheModel = LocalCacheModel(service_type = currentServiceType)

        `Given choose address data`(localCacheModel)
        `Given user preference data`(userPreferenceResponse.data)

        `When view reload page`()
        `When switch service called`()

        `Then verify user preference use case called`(localCacheModel, "2h")
        `Then verify the data`(userPreferenceResponse.data)
    }

    @Test
    fun `switch service with current service type ooc`() {
        val currentServiceType = "ooc"
        val userPreferenceResponse = "userpreference/set-user-preference-2h.json"
            .jsonToObject<SetUserPreference.SetUserPreferenceResponse>()

        val localCacheModel = LocalCacheModel(service_type = currentServiceType)

        `Given choose address data`(localCacheModel)
        `Given user preference data`(userPreferenceResponse.data)

        `When view reload page`()
        `When switch service called`()

        `Then verify user preference use case called`(localCacheModel, "2h")
        `Then verify the data`(userPreferenceResponse.data)
    }

    @Test
    fun `switch service with null address data`() {
        `Given address data null`()

        `When switch service called`()

        `Then verify user preference use case not called`()
    }

    @Test
    fun `switch service returns error`() {
        val localCacheModel = LocalCacheModel(service_type = "2h")

        `Given choose address data`(localCacheModel)
        `Given an error result`(Throwable(message = "something went right"))

        `When view reload page`()
        `When switch service called`()

        `Then verify user preference use case called`(localCacheModel, "15m")
        `Then verify the data`(Throwable(message = "something went right"))
    }

    private fun `Given user preference data`(userPreferenceData: SetUserPreference.SetUserPreferenceData) {
        coEvery { setUserPreferenceUseCase.execute(any(), any()) } returns userPreferenceData
    }

    private fun `Given an error result`(error: Throwable) {
        coEvery { setUserPreferenceUseCase.execute(any(), any()) } throws error
    }

    private fun `When switch service called`() {
        tokoNowCategoryViewModel.switchService()
    }

    private fun `Then verify user preference use case called`(
        localCacheModel: LocalCacheModel,
        serviceType: String
    ) {
        coVerify { setUserPreferenceUseCase.execute(localCacheModel, serviceType) }
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
