package com.tokopedia.universal_sharing.test.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.universal_sharing.di.ActivityComponentFactory
import com.tokopedia.universal_sharing.stub.common.ActivityScenarioTestRule
import com.tokopedia.universal_sharing.stub.common.NetworkUtilStub
import com.tokopedia.universal_sharing.stub.di.FakeActivityComponentFactory
import com.tokopedia.universal_sharing.util.NetworkUtil
import com.tokopedia.universal_sharing.view.activity.UniversalSharingPostPurchaseSharingActivity
import com.tokopedia.user.session.UserSessionInterface
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseUniversalSharingPostPurchaseBottomSheetTest {
    @get:Rule
    var activityScenarioRule =
        ActivityScenarioTestRule<UniversalSharingPostPurchaseSharingActivity>()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var networkUtil: NetworkUtil

    @Before
    open fun beforeTest() {
        Intents.init()
        setupDaggerComponent()
        (networkUtil as NetworkUtilStub).isConnectedToNetwork = true
    }

    private fun setupDaggerComponent() {
        val fakeComponent = FakeActivityComponentFactory()
        ActivityComponentFactory.instance = fakeComponent
        fakeComponent.universalSharingComponent.inject(this)
    }

    @After
    open fun afterTest() {
        Intents.release()
    }

    protected fun launchActivity(
        intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent(context, UniversalSharingPostPurchaseSharingActivity::class.java)
        intentModifier(intent)
        activityScenarioRule.launchActivity(intent)
    }
}
