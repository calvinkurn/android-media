package com.tokopedia.campaign.shake.landing.presenter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Vibrator
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.campaign.shake.landing.domain.GetCampaignUseCase
import com.tokopedia.campaign.shake.landing.view.activity.ShakeDetectCampaignActivity
import com.tokopedia.campaign.shake.landing.view.presenter.ShakeDetectContract
import com.tokopedia.campaign.shake.landing.view.presenter.ShakeDetectPresenter
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shakedetect.ShakeDetectManager
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.permission.PermissionCheckerHelper
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShakeDetectPresenterTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var getCampaignUseCase: GetCampaignUseCase

    @RelaxedMockK
    private lateinit var context: Context

    @RelaxedMockK
    private lateinit var view: ShakeDetectContract.View

    private lateinit var presenter: ShakeDetectPresenter

    @Before
    fun before() {
        MockKAnnotations.init(this)
        mockkStatic(ShakeDetectManager::class)
        setupShakeManagerTest()

        presenter =  spyk(ShakeDetectPresenter(getCampaignUseCase, context))
        presenter.attachView(view)
    }

    private fun setupVibratorTest() {
        val vibratorTest = mockk<Vibrator>()

        every {
            context.getSystemService(Context.VIBRATOR_SERVICE)
        } returns vibratorTest

        every {
            vibratorTest.vibrate(500)
        } returns mockk()
    }

    private fun setupShakeManagerTest() {
        every {
            ShakeDetectManager.getShakeDetectManager().disableShakeShake()
        } returns mockk(relaxed = true)
    }

    @Test
    fun `Successfully onShakeDetect with LongShakeTriggered` () {
        val isLongShake = true
        val isFirstShake = false
        val isDoubleShake = false

        setupVibratorTest()

        presenter.onShakeDetectTest(isLongShake, isFirstShake, isDoubleShake)

        verify {
            view.setInvisibleCounter()
            view.showDisableShakeShakeVisible()
        }
    }

    @Test
    fun `Successfully onShakeDetect with DoubleShakeShakeEnable` () {
        val isLongShake = false
        val isFirstShake = false
        val isDoubleShake = true

        setupVibratorTest()

        presenter.onShakeDetectTest(isLongShake, isFirstShake, isDoubleShake)

        assertTrue(presenter.firstShake)
    }

    @Test
    fun `Successfully onShakeDetect normal` () {
        val isLongShake = false
        val isFirstShake = true
        val isDoubleShake = false

        every {
            presenter.addLocationParameterBeforeRequest(any())
        } returns Unit

        presenter.onShakeDetectTest(isLongShake, isFirstShake, isDoubleShake)

        verify {
            view.setInvisibleCounter()
            view.setCancelButtonVisible()
        }

        assertFalse(presenter.firstShake)
    }

    @Test
    fun `Successfully onShakeDetect longShake` () {
        setupVibratorTest()

        every {
            view.isLongShakeTriggered
        } returns true

        presenter.onShakeDetect()

        verify {
            view.isLongShakeTriggered
        }
    }

    @Test
    fun `Successfully onShakeDetect` () {
        setupVibratorTest()

        every {
            presenter.firstShake
        } returns true

        presenter.onShakeDetect()

        verify {
            view.isLongShakeTriggered
        }
    }

    @Test
    fun `onDisableShakeShake logged in` () {
        setupUserSessionMock(true)

        presenter.onDisableShakeShake()

        verify {
            view.goToGeneralSetting()
        }
    }

    @Test
    fun `onDisableShakeShake not logged in` () {
        setupUserSessionMock(false)

        presenter.onDisableShakeShake()

        verify {
            view.makeInvisibleShakeShakeDisableView()
            view.setSnackBarErrorMessage()
        }
    }

    private fun setupUserSessionMock(loggedIn: Boolean) {
        val sharedPreferencesTest = mockk<SharedPreferences>()

        every {
            context.getSharedPreferences(any(), any())
        } returns sharedPreferencesTest

        every {
            context.applicationContext.getSharedPreferences(any(), any())
        } returns sharedPreferencesTest

        every {
            sharedPreferencesTest.getString(any(), any())
        } returns ""

        every {
            sharedPreferencesTest.getBoolean(any(), any())
        } returns loggedIn

        every {
            presenter.isLogin
        } returns loggedIn
    }

    @Test
    fun `onActivityResult` () {
        val requestCodeTest = 0
        val resultCodeTest = 0
        val data = mockk<Intent>()

        presenter.onActivityResult(requestCodeTest, resultCodeTest, data)

        verify {
            view.finish()
        }
    }

    @Test
    fun `onCancelClick` () {
        presenter.onCancelClick()

        verify {
            presenter.finishShake()
        }
    }

    @Test
    fun `finish shake not null subscription` () {
        val presenterTest = mockk<ShakeDetectPresenter>(relaxed = true)

        every {
            presenterTest.finishShake()
        } answers {
            presenterTest.subscription.unsubscribe()
        }

        presenterTest.subscription = mockk(relaxed = true)
        presenterTest.finishShake()

        verify {
            presenterTest.subscription.unsubscribe()
        }
    }

    @Test
    fun `finish shake and unsubscribe` () {
        presenter.subscription = mockk(relaxed = true)
        presenter.finishShake()
        verify {
            presenter.subscription.unsubscribe()
        }
    }

    @Test
    fun `onDestroyView` () {
        presenter.onDestroyView()
        verify {
            getCampaignUseCase.unsubscribe()
        }
    }

    @Test
    fun `is user loggedin` () {
        val userSessionTest = mockk<UserSession>(relaxed = true)
        presenter.changeUserSession(userSessionTest)
        every { userSessionTest.isLoggedIn } returns true
        val isLogin = presenter.isLogin
        verify { presenter.isLogin }
        assertEquals(true, isLogin)
    }

    @Test
    fun `is shake detected as normal shake is not first shake` () {
        val remoteConfigTest = mockk<RemoteConfig>(relaxed =  true)
        val isFirstShakeTest = true
        val permissionCheckerHelperTest = mockk<PermissionCheckerHelper>(relaxed = true)

        presenter.changeFirstShake(isFirstShakeTest)
        presenter.changeRemoteConfig(remoteConfigTest)
        presenter.setPermissionChecker(permissionCheckerHelperTest)

        givenLocationParameterBeforeRequest(remoteConfigTest, permissionCheckerHelperTest)

        presenter.onShakeDetect()

        verify {
            presenter.addLocationParameterBeforeRequest(any())
        }

        assertFalse(presenter.firstShake)
    }

    @Test
    fun `is shake detected as normal shake is first shake` () {
        val remoteConfigTest = mockk<RemoteConfig>(relaxed =  true)
        val isFirstShakeTest = true
        val permissionCheckerHelperTest = mockk<PermissionCheckerHelper>(relaxed = true)

        presenter.changeFirstShake(isFirstShakeTest)
        presenter.changeRemoteConfig(remoteConfigTest)
        presenter.setPermissionChecker(permissionCheckerHelperTest)

        givenLocationParameterBeforeRequest(remoteConfigTest, permissionCheckerHelperTest)

        presenter.onShakeDetect()

        verify {
            presenter.addLocationParameterBeforeRequest(any())
        }

        assertFalse(presenter.firstShake)

    }

    @After
    fun after() {
        presenter.changeRemoteConfig(null)
        presenter.changeFirstShake(false)
        presenter.setPermissionChecker(null)
        presenter.changeUserSession(null)
    }

    private fun givenLocationParameterBeforeRequest
                (remoteConfigTest: RemoteConfig,
                 permissionCheckerHelperTest: PermissionCheckerHelper) {
        val stringTest = "Dummy test string"
        val activity = spyk(ShakeDetectCampaignActivity())
        every {
            remoteConfigTest.getBoolean(any())
        } returns false
        every {
            view.isLongShakeTriggered
        } returns false

        every {
            view.currentActivity
        } returns activity

        every {
            activity.applicationContext
        } returns context

        every {
            permissionCheckerHelperTest.hasPermission(any(), any())
        } returns true

        every {
            activity.getString(any())
        } returns stringTest
    }
}