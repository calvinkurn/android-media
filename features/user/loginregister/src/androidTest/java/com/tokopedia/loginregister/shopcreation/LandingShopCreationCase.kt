package com.tokopedia.loginregister.shopcreation

import android.Manifest
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.di.FakeActivityComponentFactory
import com.tokopedia.loginregister.di.UserSessionStub
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.shopcreation.view.landingshop.LandingShopCreationActivity
import com.tokopedia.loginregister.utils.respondWithOk
import com.tokopedia.test.application.annotations.UiTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
@UiTest
class LandingShopCreationCase {

    @Inject
    lateinit var userSession: UserSessionStub

    @get:Rule
    var grantPermission: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.READ_PHONE_STATE
    )

    @get:Rule
    var activityTestRule = IntentsTestRule(
        LandingShopCreationActivity::class.java,
        false,
        false
    )

    @Before
    fun before() {
        val fakeComponentFactory = FakeActivityComponentFactory()
        ActivityComponentFactory.instance = fakeComponentFactory
        fakeComponentFactory.shopCreationComponent.inject(this)
    }

    @After
    fun tear() {
        activityTestRule.finishActivity()
    }

    private fun setupActivity(
        intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent()

        intentModifier(intent)
        activityTestRule.launchActivity(intent)
    }

    @Test
    fun gotoShopAdminPage_userlogin_ifClickOnOpenShopButton() {
        userSession.setUserLoginStatus(true)
        userSession.setFakeUserId("12345")
        setupActivity()

        Intents.intending(
            IntentMatchers.hasData(
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.ADMIN_REDIRECTION
                )
            )
        ).respondWithOk()

        shopCreationRobot {
            clickAtOpenShop()
        } validate {
            Intents.intended(
                IntentMatchers.hasData(
                    UriUtil.buildUri(
                        ApplinkConstInternalMarketplace.ADMIN_REDIRECTION
                    )
                )
            )
        }
    }

    @Test
    fun gotoPhoneShopCreationPage_userNonlogin_ifClickOnOpenShopButton() {
        userSession.setUserLoginStatus(false)
        userSession.setFakeUserId("0")
        setupActivity()

        Intents.intending(
            IntentMatchers.hasData(
                UriUtil.buildUri(
                    ApplinkConstInternalUserPlatform.PHONE_SHOP_CREATION
                )
            )
        ).respondWithOk()

        shopCreationRobot {
            clickAtOpenShop()
        } validate {
            Intents.intended(
                IntentMatchers.hasData(
                    UriUtil.buildUri(
                        ApplinkConstInternalUserPlatform.PHONE_SHOP_CREATION
                    )
                )
            )
        }
    }

}
