package com.tokopedia.oneclickcheckout.preference.list.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.oneclickcheckout.common.domain.FakeGetPreferenceListUseCase
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccEvent
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.common.view.model.preference.PreferenceListResponseModel
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.oneclickcheckout.preference.list.domain.FakeSetDefaultPreferenceUseCase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PreferenceListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var getPreferenceListUseCase: FakeGetPreferenceListUseCase
    private lateinit var setDefaultPreferenceUseCase: FakeSetDefaultPreferenceUseCase

    private lateinit var preferenceListViewModel: PreferenceListViewModel

    @Before
    fun setUp() {
        getPreferenceListUseCase = FakeGetPreferenceListUseCase()
        setDefaultPreferenceUseCase = FakeSetDefaultPreferenceUseCase()

        preferenceListViewModel = PreferenceListViewModel(getPreferenceListUseCase, setDefaultPreferenceUseCase)
    }

    @Test
    fun `Get All Preference Success`() {
        val response = PreferenceListResponseModel()

        preferenceListViewModel.getAllPreference()
        assertEquals(OccState.Loading, preferenceListViewModel.preferenceList.value)

        getPreferenceListUseCase.invokeOnSuccess(response)

        assertEquals(OccState.Success(response), preferenceListViewModel.preferenceList.value)
    }

    @Test
    fun `Get All Preference Failed`() {
        val response = Throwable()

        preferenceListViewModel.getAllPreference()
        assertEquals(OccState.Loading, preferenceListViewModel.preferenceList.value)

        getPreferenceListUseCase.invokeOnError(response)

        assertEquals(OccState.Failed(Failure(response)), preferenceListViewModel.preferenceList.value)
    }

    @Test
    fun `Change Default Preference Success`() {
        val response = "Success"
        getPreferenceListUseCase.onFinish = { PreferenceListResponseModel() }

        preferenceListViewModel.changeDefaultPreference(ProfilesItemModel(profileId = 0))
        assertEquals(OccState.Loading, preferenceListViewModel.setDefaultPreference.value)

        setDefaultPreferenceUseCase.invokeOnSuccess(response)

        assertEquals(OccState.Success(OccEvent(response)), preferenceListViewModel.setDefaultPreference.value)
    }

    @Test
    fun `Change Default Preference Failed`() {
        val response = Throwable()

        preferenceListViewModel.changeDefaultPreference(ProfilesItemModel(profileId = 0))

        assertEquals(OccState.Loading, preferenceListViewModel.setDefaultPreference.value)

        setDefaultPreferenceUseCase.invokeOnError(response)

        assertEquals(OccState.Failed(Failure(response)), preferenceListViewModel.setDefaultPreference.value)
    }

    @Test
    fun `Consume Preference List Fail`() {
        val response = Throwable()
        getPreferenceListUseCase.onFinish = { throw response }

        preferenceListViewModel.getAllPreference()

        //consume failure
        (preferenceListViewModel.preferenceList.value as OccState.Failed).getFailure()

        assertEquals(null, (preferenceListViewModel.preferenceList.value as OccState.Failed).getFailure())
    }
}
