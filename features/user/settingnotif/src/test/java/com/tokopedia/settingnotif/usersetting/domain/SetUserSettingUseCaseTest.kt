package com.tokopedia.settingnotif.usersetting.domain

import com.tokopedia.settingnotif.usersetting.base.SettingRepository
import com.tokopedia.settingnotif.usersetting.data.pojo.UserNotificationResponse
import com.tokopedia.settingnotif.usersetting.data.pojo.setusersetting.SetUserSettingResponse
import com.tokopedia.settingnotif.usersetting.data.pojo.setusersetting.SetUserSettingStatus
import com.tokopedia.settingnotif.util.stubRepository
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class SetUserSettingUseCaseTest {

    private val repository: SettingRepository = mockk(relaxed = true)
    private lateinit var useCase: SetUserSettingUseCase

    @Before fun setUp() {
        useCase = SetUserSettingUseCase(
                repository = repository,
                graphQuery = ""
        )
    }

    @Test fun `it should success update user setting`() {
        runBlocking {
            val params = SetUserSettingUseCase.params("", listOf())
            val expectedValue = SetUserSettingResponse(SetUserSettingStatus())
            useCase.params = params
            repository.stubRepository<SetUserSettingResponse>(
                    expectedValue = expectedValue,
                    onError = mapOf()
            )
            val request = useCase.executeOnBackground()
            Assertions.assertThat(request).isEqualTo(expectedValue)
        }
    }

    @Test fun `it should getting error when update user setting`() {
        val params = SetUserSettingUseCase.params("", listOf())
        val expectedValue = SetUserSettingResponse(SetUserSettingStatus())
        useCase.params = params
        repository.stubRepository<SetUserSettingResponse>(
                expectedValue = expectedValue,
                onError = null
        )
        assertFailsWith<NullPointerException> {
            runBlocking { useCase.executeOnBackground() }
        }
    }

    @Test fun `it should getting error when param not found`() {
        val expectedValue = SetUserSettingResponse(SetUserSettingStatus())
        repository.stubRepository<SetUserSettingResponse>(
                expectedValue = expectedValue,
                onError = mapOf()
        )
        assertFailsWith<Exception> {
            runBlocking { useCase.executeOnBackground() }
        }
    }

}