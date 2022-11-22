package com.tokopedia.usercomponents.explicit

import com.tokopedia.usercomponents.test.R
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.usercomponents.common.ViewIdGenerator
import com.tokopedia.usercomponents.common.stub.di.FakeAppModule
import com.tokopedia.usercomponents.explicit.di.DaggerFakeExplicitComponent
import com.tokopedia.usercomponents.explicit.fake_view.ExplicitDebugActivity
import com.tokopedia.usercomponents.explicit.fake_view.ExplicitDebugFragment
import com.tokopedia.usercomponents.explicit.stub.data.ExplicitRepositoryState
import com.tokopedia.usercomponents.explicit.stub.data.ExplicitRepositoryStub
import com.tokopedia.usercomponents.explicit.stub.data.UserSessionState
import com.tokopedia.usercomponents.explicit.stub.data.UserSessionStub
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class ExplicitGenerateId {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        ExplicitDebugActivity::class.java,
        false,
        false
    )

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    private lateinit var repositoryStub: ExplicitRepositoryStub
    private lateinit var userSessionStub: UserSessionStub

    @Before
    fun before() {
        ExplicitDebugFragment.component = DaggerFakeExplicitComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .build()

        repositoryStub = ExplicitDebugFragment.component?.repository() as ExplicitRepositoryStub
        userSessionStub = ExplicitDebugFragment.component?.userSession() as UserSessionStub
    }

    @Test
    fun generate_view_id_file() {
        userSessionStub.setLoggedIn(UserSessionState.AUTHORIZED)
        repositoryStub.setState(ExplicitRepositoryState.SHOW_QUESTION)

        activityTestRule.launchActivity(Intent())

        val rootView = findRootView(activityTestRule.activity).findViewById<View>(R.id.fake_explicit)
        ViewIdGenerator.createViewIdFile(rootView, "explicit_widget.csv")
    }

}
