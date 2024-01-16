package com.tokopedia.editor.ui

import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.internal.ApplinkConstInternalMedia
import com.tokopedia.editor.common.FakeMainEditorActivity
import com.tokopedia.picker.common.EXTRA_UNIVERSAL_EDITOR_PARAM
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.picker.common.RESULT_UNIVERSAL_EDITOR
import com.tokopedia.picker.common.UniversalEditorParam
import org.junit.After
import org.junit.Before
import java.io.File


abstract class EditorTest {

    lateinit var mActivity: FakeMainEditorActivity

    private var activityScenario: ActivityScenario<FakeMainEditorActivity>? = null

    val context: Context = InstrumentationRegistry
        .getInstrumentation()
        .targetContext

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation()
            .context
            .applicationContext

    @Before
    open fun setUp() {
        Intents.init()
    }

    @After
    open fun tearDown() {
        Intents.release()
        activityScenario?.close()
    }

    protected fun startActivity(param: UniversalEditorParam.() -> Unit) {
        startActivity(UniversalEditorParam().apply(param).also {
            it.setPageSource(PageSource.Play)
        })
    }

    protected fun getImageResultRatio(): Float {
        val filePath = getActivityResult()
        val y = filePath?.resultData?.getParcelableExtra<PickerResult>(RESULT_UNIVERSAL_EDITOR)
        val editedPath = y?.editedPaths?.first() ?: ""


        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(File(editedPath).absolutePath, options)
        val imageHeight = options.outHeight.toFloat()
        val imageWidth = options.outWidth.toFloat()

        return imageWidth / imageHeight
    }

    private fun getActivityResult(): ActivityResult? {
        activityScenario?.result?.resultData?.setExtrasClassLoader(PickerResult::class.java.classLoader)
        return activityScenario?.result
    }

    private fun startActivity(param: UniversalEditorParam) {
        val intent = Intent().apply {
            data = createUriIntent().build()
        }

        intent.putExtra(EXTRA_UNIVERSAL_EDITOR_PARAM, param)
        activityScenario = ActivityScenario.launch(intent)
    }

    private fun createUriIntent(): Uri.Builder {
        return Uri.parse(ApplinkConstInternalMedia.INTERNAL_UNIVERSAL_MEDIA_EDITOR).buildUpon()
    }

}
