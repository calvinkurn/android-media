package com.tokopedia.hotel.screenshot.srp_map

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.hotel.search.data.model.HotelSearchModel
import com.tokopedia.hotel.search.presentation.activity.mock.HotelSearchMockResponseConfig
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchResultFragment
import com.tokopedia.hotel.search_map.presentation.activity.HotelSearchMapActivity
import com.tokopedia.hotel.search_map.presentation.fragment.HotelSearchMapFragment
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 07/05/21
 */
abstract class BaseHotelSearchMapScreenshotTesting {
    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @get:Rule
    var activityRule: IntentsTestRule<HotelSearchMapActivity> = object : IntentsTestRule<HotelSearchMapActivity>(HotelSearchMapActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupDarkModeTest(forceDarkMode())
            setupGraphqlMockResponse(HotelSearchMockResponseConfig())
            val localCacheHandler = LocalCacheHandler(targetContext, HotelSearchResultFragment.PREFERENCES_NAME)
            localCacheHandler.apply {
                putBoolean(HotelSearchResultFragment.SHOW_COACH_MARK_KEY, false)
                applyEditor()
            }
            login()
        }

        override fun getActivityIntent(): Intent {
            return Intent(targetContext, HotelSearchMapActivity::class.java).apply {
                putExtra(HotelSearchMapFragment.ARG_HOTEL_SEARCH_MODEL, getHotelSearchModel())
            }
        }
    }

    private fun getHotelSearchModel(): HotelSearchModel {
        return HotelSearchModel(
                searchId = "835",
                searchType = "regionOrigin",
                name = "Bali"
        )
    }

    @Test
    fun screenShot(){
        uiDevice.wait(Until.hasObject(By.desc("MAP READY")), 10000)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activityRule.activity.window.decorView, filePrefix(), "top")
        }
    }

    abstract fun filePrefix(): String

    abstract fun forceDarkMode(): Boolean

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        val userSession = UserSession(targetContext)
        userSession.setLoginSession(
                true,
                userSession.userId,
                userSession.name,
                userSession.shopId,
                true,
                userSession.shopName,
                userSession.email,
                userSession.isGoldMerchant,
                userSession.phoneNumber
        )
    }
}