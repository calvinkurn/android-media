package com.tokopedia.picker.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.picker.fake.di.DaggerTestPickerComponent
import com.tokopedia.picker.fake.di.TestPickerComponent
import com.tokopedia.picker.fake.di.common.DaggerTestBaseAppComponent
import com.tokopedia.picker.fake.di.common.TestAppModule
import com.tokopedia.picker.fake.di.common.TestBaseAppComponent
import com.tokopedia.picker.fake.ui.activity.TestPickerActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class PickerTest {

    @get:Rule
    val activityTestRule = IntentsTestRule(
        TestPickerActivity::class.java, false, false
    )

    protected lateinit var activity: TestPickerActivity

    protected val context: Context = InstrumentationRegistry
        .getInstrumentation()
        .targetContext

    protected val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @Before
    open fun before() {
        setupBaseDaggerComponent()
        setupPickerDaggerComponent()
    }

    @After
    open fun tearDown() {
        baseComponent = null
        pickerComponent = null
    }

    abstract fun buildUri(builder: Uri.Builder)

    protected fun buildIntent(): Uri.Builder {
        val builder = Uri.parse(
            ApplinkConst.MEDIA_PICKER
        ).buildUpon()

        buildUri(builder)

        return builder
    }

    protected fun startPickerActivity(
        modifier: (Intent) -> Unit = {}
    ) {
        val uri = buildIntent().build()
        val intent = Intent().apply {
            data = uri
        }
        modifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
    }

    private fun setupBaseDaggerComponent() {
        baseComponent = DaggerTestBaseAppComponent.builder()
            .testAppModule(TestAppModule(applicationContext))
            .build()
    }

    private fun setupPickerDaggerComponent() {
        pickerComponent = DaggerTestPickerComponent.builder()
            .testBaseAppComponent(baseComponent)
            .build()
    }

    companion object {
        var baseComponent: TestBaseAppComponent? = null
        var pickerComponent: TestPickerComponent? = null
    }

}