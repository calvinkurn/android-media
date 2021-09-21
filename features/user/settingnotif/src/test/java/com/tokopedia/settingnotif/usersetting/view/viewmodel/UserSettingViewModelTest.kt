package com.tokopedia.settingnotif.usersetting.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.CommonUtils.fromJson
import com.tokopedia.settingnotif.data.MockResponse.pushNotificationResponse
import com.tokopedia.settingnotif.usersetting.analytics.MoengageManager
import com.tokopedia.settingnotif.usersetting.data.pojo.UserNotificationResponse
import com.tokopedia.settingnotif.usersetting.data.pojo.setusersetting.SetUserSettingResponse
import com.tokopedia.settingnotif.usersetting.data.pojo.setusersetting.SetUserSettingStatus
import com.tokopedia.settingnotif.usersetting.domain.GetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.domain.SetUserSettingUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView
import com.tokopedia.settingnotif.usersetting.view.state.UserSettingErrorState
import com.tokopedia.settingnotif.util.isEqualsTo
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.settingnotif.usersetting.data.mapper.UserSettingMapper.map as userSettingMap

class UserSettingViewModelTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    // useCase
    private var getUserSettingUseCase: GetUserSettingUseCase = mockk(relaxed = true)
    private var setUserSettingUseCase: SetUserSettingUseCase = mockk(relaxed = true)
    private var moengageManager: MoengageManager = mockk(relaxed = true)

    // observable
    private val getUserSettingObservable: Observer<UserSettingDataView> = mockk(relaxed = true)
    private val setUserSettingObservable: Observer<SetUserSettingResponse> = mockk(relaxed = true)
    private val errorStateObservable: Observer<UserSettingErrorState> = mockk(relaxed = true)

    // viewModel
    private lateinit var viewModel: UserSettingViewModel

    @Before fun setUp() {
        val testDispatcher = CoroutineTestDispatchersProvider
        viewModel = UserSettingViewModel(
                getUserSettingUseCase,
                setUserSettingUseCase,
                moengageManager,
                testDispatcher
        )

        viewModel.userSetting.observeForever(getUserSettingObservable)
        viewModel.setUserSetting.observeForever(setUserSettingObservable)
        viewModel.errorErrorState.observeForever(errorStateObservable)
    }

    @Test fun `it should load user setting properly`() = runBlocking {
        val userNotificationResponse = fromJson<UserNotificationResponse>(
                pushNotificationResponse,
                UserNotificationResponse::class.java
        )
        val expectedReturnValue = userSettingMap(
                userNotificationResponse
        )

        coEvery {
            getUserSettingUseCase.executeOnBackground()
        } returns userNotificationResponse

        viewModel.loadUserSettings()

        verify { getUserSettingObservable.onChanged(expectedReturnValue) }
        viewModel.userSetting isEqualsTo expectedReturnValue
    }

    @Test fun `it should getting fail when load user data`() = runBlocking {
        val expectedReturnValue = UserSettingErrorState.GetSettingError

        coEvery {
            getUserSettingUseCase.executeOnBackground()
        } throws Exception("nothing")

        viewModel.loadUserSettings()

        verify { errorStateObservable.onChanged(expectedReturnValue) }
        viewModel.errorErrorState isEqualsTo expectedReturnValue
    }

    @Test fun `it should update user setting correctly`() = runBlocking {
        val setNotificationResponse = SetUserSettingResponse(
                SetUserSettingStatus()
        )

        coEvery {
            setUserSettingUseCase.executeOnBackground()
        } returns setNotificationResponse

        viewModel.requestUpdateUserSetting("", listOf())

        verify { setUserSettingObservable.onChanged(setNotificationResponse) }
        viewModel.setUserSetting isEqualsTo setNotificationResponse
    }

    @Test fun `it should fail update user setting`() = runBlocking {
        val expectedReturnValue = UserSettingErrorState.SetSettingError

        coEvery {
            setUserSettingUseCase.executeOnBackground()
        } throws Exception("nothing")

        viewModel.requestUpdateUserSetting("", listOf())

        verify { errorStateObservable.onChanged(expectedReturnValue) }
        viewModel.errorErrorState isEqualsTo expectedReturnValue
    }

    @Test fun `it should fail update user setting when param is empty`() = runBlocking {
        val expectedReturnValue = UserSettingErrorState.SetSettingError

        coEvery {
            setUserSettingUseCase.params
        } returns RequestParams.EMPTY

        coEvery {
            setUserSettingUseCase.executeOnBackground()
        } throws Exception("nothing")

        viewModel.requestUpdateUserSetting("", listOf())


        verify { errorStateObservable.onChanged(expectedReturnValue) }
        viewModel.errorErrorState isEqualsTo expectedReturnValue
    }

    @Test fun `it should didnt called moengage tracker for push notification`() {
        every { moengageManager.setPushPreference(true) } returns Unit

        viewModel.requestUpdateMoengageUserSetting("", true)

        verify(exactly = 0) { moengageManager.setPushPreference(true) }
    }

    @Test fun `it should update moengage for push notification correctly`() {
        every { moengageManager.setPushPreference(true) } returns Unit

        viewModel.requestUpdateMoengageUserSetting("promo", true)

        verify(exactly = 1) { moengageManager.setPushPreference(true) }
    }

    @Test fun `it should didnt called moengage tracker for email newsletter`() {
        every { moengageManager.setNewsletterEmailPref(true) } returns Unit

        viewModel.requestUpdateMoengageUserSetting("", true)

        verify(exactly = 0) { moengageManager.setNewsletterEmailPref(true) }
    }

    @Test fun `it should update moengage for email newsletter correctly`() {
        every { moengageManager.setNewsletterEmailPref(true) } returns Unit

        viewModel.requestUpdateMoengageUserSetting("bulletin_newsletter", true)

        verify(exactly = 1) { moengageManager.setNewsletterEmailPref(true) }
    }

    @After fun tearDown() {

    }

}