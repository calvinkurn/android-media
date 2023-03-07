package com.tokopedia.media.picker.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.media.picker.common.di.DaggerTestPickerComponent
import com.tokopedia.media.picker.common.di.DaggerTestPreviewComponent
import com.tokopedia.media.picker.common.di.TestPickerComponent
import com.tokopedia.media.picker.common.di.TestPreviewComponent
import com.tokopedia.media.picker.common.di.common.DaggerTestBaseAppComponent
import com.tokopedia.media.picker.common.di.common.TestAppModule
import com.tokopedia.media.picker.common.di.common.TestBaseAppComponent
import com.tokopedia.media.picker.common.ui.activity.TestPreviewActivity
import com.tokopedia.media.picker.helper.utils.ImageGenerator
import com.tokopedia.media.picker.helper.utils.VideoGenerator
import com.tokopedia.picker.common.EXTRA_INTENT_PREVIEW
import com.tokopedia.picker.common.EXTRA_PICKER_PARAM
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class PreviewTest {
    @get:Rule
    val activityTestRule = IntentsTestRule(
        TestPreviewActivity::class.java, false, false
    )

    lateinit var mActivity: TestPreviewActivity

    val context: Context = InstrumentationRegistry
        .getInstrumentation()
        .targetContext

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation()
            .context
            .applicationContext

    private val imageCollection = ImageGenerator.getFiles(context).map {
        MediaUiModel(file = PickerFile(it.path))
    }

    private val videoCollection = VideoGenerator.getMultipleFiles(context).map {
        MediaUiModel(file = PickerFile(it.path))
    }

    private val combinationMediaCollection = listOf(
        imageCollection[0],
        imageCollection[1],
        videoCollection[0],
        videoCollection[1]
    )

    @Before
    open fun setUp() {
        setupBaseDaggerComponent()
        setupPreviewDaggerComponent()
    }

    @After
    open fun tearDown() {
        baseComponent = null
        previewComponent = null
    }

    protected fun startPreviewActivity(param: PickerParam, mediaType: Int) {
        val intent = Intent().apply {
            data = createUriIntent().build()
        }

        val mediaUiModel = when (mediaType) {
            IMAGE_MEDIA_ONLY -> imageCollection
            VIDEO_MEDIA_ONLY -> videoCollection
            else -> combinationMediaCollection
        }

        intent.putExtra(EXTRA_PICKER_PARAM, param)
        intent.putExtra(EXTRA_INTENT_PREVIEW, ArrayList(mediaUiModel))

        activityTestRule.launchActivity(intent)
        mActivity = activityTestRule.activity
    }

    private fun createUriIntent(): Uri.Builder {
        val builder = Uri.parse(ApplinkConst.MediaPicker.MEDIA_PICKER).buildUpon()
        return builder
    }

    private fun setupBaseDaggerComponent() {
        baseComponent = DaggerTestBaseAppComponent.builder()
            .testAppModule(TestAppModule(applicationContext))
            .build()
    }

    private fun setupPreviewDaggerComponent() {
        previewComponent = DaggerTestPreviewComponent.builder()
            .testBaseAppComponent(baseComponent)
            .build()
    }

    companion object {
        var baseComponent: TestBaseAppComponent? = null
        var previewComponent: TestPreviewComponent? = null

        const val IMAGE_MEDIA_ONLY = 0
        const val VIDEO_MEDIA_ONLY = 1
        const val COMBINATION_MEDIA = 2
    }
}