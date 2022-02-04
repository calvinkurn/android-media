package com.tokopedia.media.picker.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.media.picker.common.di.DaggerTestPickerComponent
import com.tokopedia.media.picker.common.di.TestPickerComponent
import com.tokopedia.media.picker.common.di.common.DaggerTestBaseAppComponent
import com.tokopedia.media.picker.common.di.common.TestAppModule
import com.tokopedia.media.picker.common.di.common.TestBaseAppComponent
import com.tokopedia.media.picker.common.ui.activity.TestPickerActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class PickerTest {

    @get:Rule
    val activityTestRule = IntentsTestRule(
        TestPickerActivity::class.java, false, false
    )

    lateinit var mActivity: TestPickerActivity

    val context: Context = InstrumentationRegistry
        .getInstrumentation()
        .targetContext

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation()
            .context
            .applicationContext

    abstract fun createAndAppendUri(builder: Uri.Builder)

    @Before open fun setUp() {
        setupBaseDaggerComponent()
        setupPickerDaggerComponent()
    }

    @After open fun tearDown() {
        baseComponent = null
        pickerComponent = null
    }

    protected fun startPickerActivity(
        modifier: (Intent) -> Unit = {}
    ) {
        val uri = createIntent().build()
        val intent = Intent().apply {
            data = uri
        }
        modifier(intent)
        activityTestRule.launchActivity(intent)
        mActivity = activityTestRule.activity
    }

    private fun createIntent(): Uri.Builder {
        val builder = Uri.parse(
            ApplinkConst.MEDIA_PICKER
        ).buildUpon()

        createAndAppendUri(builder)

        return builder
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