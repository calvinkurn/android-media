package com.tokopedia.privacycenter.domain

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.utils.permission.PermissionCheckerHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DevicePermissionUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: DevicePermissionUseCase
    private val preference = mockk<SharedPreferences>(relaxed = true)
    private val context = mockk<Context>(relaxed = true)
    private val permissionCheckerHelper = mockk<PermissionCheckerHelper>(relaxed = true)

    @Before
    fun setup() {
        useCase = DevicePermissionUseCase(
            context,
            preference,
            permissionCheckerHelper
        )
    }

    @Test
    fun `get location permission then return allowed`() {
        val isAllowed = true

        every { permissionCheckerHelper.hasPermission(any(), any()) } returns isAllowed

        val result = useCase.isLocationAllowed()
        assertTrue(result)
    }

    @Test
    fun `get location permission then return not allowed`() {
        val isAllowed = false

        every { permissionCheckerHelper.hasPermission(any(), any()) } returns isAllowed

        val result = useCase.isLocationAllowed()
        assertFalse(result)
    }

    @Test
    fun `get shake-shake permission then return allowed`() {
        val isAllowed = true
        val keyShakeShake = "notification_shake_shake"

        every { preference.getBoolean(keyShakeShake, false) } returns isAllowed

        val result = useCase.isLocationAllowed()
        assertFalse(result)
    }

    @Test
    fun `get shake-shake permission then return not allowed`() {
        val isAllowed = false
        val keyShakeShake = "notification_shake_shake"

        every { preference.getBoolean(keyShakeShake, false) } returns isAllowed

        val result = useCase.isLocationAllowed()
        assertFalse(result)
    }

    @Test
    fun `set shake-shake permission make sure function only called once`() {
        val isAllowed = false
        val keyShakeShake = "notification_shake_shake"

        useCase.setShakeShakePermission(isAllowed)

        verify(exactly = 1) {
            preference.edit().putBoolean(keyShakeShake, isAllowed).apply()
        }
    }
}
