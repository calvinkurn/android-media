package com.tokopedia.universal_sharing.test.base

import android.content.Context
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.universal_sharing.di.ActivityComponentFactory
import com.tokopedia.universal_sharing.stub.common.ActivityScenarioTestRule
import com.tokopedia.universal_sharing.stub.di.FakeActivityComponentFactory
import com.tokopedia.universal_sharing.view.activity.UniversalSharingPostPurchaseSharingActivity
import com.tokopedia.user.session.UserSessionInterface
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class UniversalSharingPostPurchaseBottomSheetTest {
    @get:Rule
    var activityScenarioRule =
        ActivityScenarioTestRule<UniversalSharingPostPurchaseSharingActivity>()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @Inject
    lateinit var userSession: UserSessionInterface

    @Before
    open fun beforeTest() {
        Intents.init()
        setupDaggerComponent()
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
}
