package com.tokopedia.settingnotif.usersetting.domain

import com.tokopedia.settingnotif.usersetting.base.SettingRepository
import com.tokopedia.settingnotif.usersetting.data.pojo.UserNotificationResponse
import com.tokopedia.settingnotif.util.stubRepository
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class GetUserSettingUseCaseTest {

    private val repository: SettingRepository = mockk(relaxed = true)
    private lateinit var useCase: GetUserSettingUseCase

    @Before fun setUp() {
        useCase = GetUserSettingUseCase(
                repository = repository,
                graphQuery = ""
        )
    }

    @Test fun `it should return a valid user notification setting`() {
        runBlocking {
            val expectedValue = UserNotificationResponse()
            repository.stubRepository<UserNotificationResponse>(
                    expectedValue = expectedValue,
                    onError = mapOf()
            )
            val request = useCase.executeOnBackground()
            Assertions.assertThat(request).isEqualTo(expectedValue)
        }
    }

    @Test fun `it should getting error when getting user setting`() {
        val expectedValue = UserNotificationResponse()
        repository.stubRepository<UserNotificationResponse>(
                expectedValue = expectedValue,
                onError = null
        )
        assertFailsWith<NullPointerException> {
            runBlocking { useCase.executeOnBackground() }
        }
    }

}