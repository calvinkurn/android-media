package com.tokopedia.editor.ui.main

import android.widget.ImageView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.editor.ui.EditorTest
import com.tokopedia.editor.util.FileLoader
import com.tokopedia.editor.R
import com.tokopedia.editor.data.mapper.NavigationTypeMapper
import com.tokopedia.editor.ui.placement.PlacementImageActivity
import com.tokopedia.editor.ui.text.InputTextActivity
import com.tokopedia.kotlin.extensions.view.getBitmap
import com.tokopedia.picker.common.types.ToolType
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class MainEditorTest : EditorTest() {

    @Test
    fun should_able_to_open_main_editor_activity() {
        // Given
        val file = FileLoader.imageFile(context)

        // When
        startActivity {
            filePaths(listOf(file.path))
        }
    }

    @Test
    fun should_able_to_set_configurable_toolbar_component() {
        // Given
        val file = FileLoader.imageFile(context)
        val headerTitle = "CustomCtaText"

        // When
        startActivity {
            filePaths(listOf(file.path))
            custom.setHeaderTitle(headerTitle)
        }

        // Then
        assertTextDisplayedWith(headerTitle)
    }

    @Test
    fun should_able_to_render_image_nav_tool() {
        // Given
        val file = FileLoader.imageFile(context)

        // When
        startActivity {
            filePaths(listOf(file.path))
        }

        // Then
        assertRecyclerViewDisplayed(R.id.lst_tool)
    }

    @Test
    fun should_able_to_render_video_nav_tool() {
        // Given
        val file = FileLoader.videoFile(context)

        // When
        startActivity {
            filePaths(listOf(file.path))
        }

        // Then
        assertRecyclerViewDisplayed(R.id.lst_tool)
    }

    @Test
    fun should_able_to_render_image_preview() {
        // Given
        val file = FileLoader.imageFile(context)

        // When
        startActivity {
            filePaths(listOf(file.path))
        }

        // Then
        onImageView(R.id.img_sample) {
            assert(it.drawable.getBitmap() != null)
        }
        assertRecyclerViewDisplayed(R.id.lst_tool)
    }

//    @Test
//    fun should_open_input_text_page() {
//        // Given
//        val file = FileLoader.imageFile(context)
//        val text = context.getString(NavigationTypeMapper.invoke(ToolType.TEXT).second)
//
//        // When
//        startActivity {
//            filePaths(listOf(file.path))
//        }
//        onView(ViewMatchers.withText(text)).perform(ViewActions.click())
//
//        // Then
//        Thread.sleep(100)
//        checkIntentCallSuccess(InputTextActivity::class.java.name)
//    }

    @Test
    fun should_open_crop_placement_page() {
        // Given
        val file = FileLoader.imageFile(context)
        val text = context.getString(NavigationTypeMapper.invoke(ToolType.PLACEMENT).second)

        // When
        startActivity {
            filePaths(listOf(file.path))
        }
        onView(ViewMatchers.withText(text)).perform(ViewActions.click())

        // Then
        Thread.sleep(100)
        checkIntentCallSuccess(PlacementImageActivity::class.java.name)
    }

    @Test
    fun should_able_mute_video() {
        // Given
        val file = FileLoader.videoFile(context)
        val text = context.getString(NavigationTypeMapper.invoke(ToolType.AUDIO_MUTE).second)

        // When
        startActivity {
            filePaths(listOf(file.path))
        }
        onView(ViewMatchers.withText(text)).perform(ViewActions.click())

        // Then
        Thread.sleep(100)
        assertRecyclerViewDisplayed(R.id.ic_audio_state)
    }

    @Test
    fun should_render_input_text() {
        // Given
        val file = FileLoader.imageFile(context)
        val actionText = context.getString(NavigationTypeMapper.invoke(ToolType.TEXT).second)
        val typedText = "Test Input Text"

        // When
        startActivity {
            filePaths(listOf(file.path))
        }
        onView(ViewMatchers.withText(actionText)).perform(ViewActions.click())
        Thread.sleep(2000)
        onView(ViewMatchers.withId(R.id.add_text_input)).perform(ViewActions.typeText(typedText))
        Thread.sleep(2000)
        onView(ViewMatchers.withText("Simpan")).perform(ViewActions.click())

        // Then
        Thread.sleep(2000)
        assertTextDisplayedWith(typedText)
    }

    private fun onImageView(resId: Int, imgView: (ImageView) -> Unit) {
        onView(ViewMatchers.withId(resId))
            .check { view, _ -> imgView(view as ImageView) }
    }

    private fun assertTextDisplayedWith(text: String) {
        // set waiting for 500ms before assertion
        Thread.sleep(500)

        onView(
            ViewMatchers.withText(text)
        ).check(
            ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        )
    }

    private fun assertRecyclerViewDisplayed(viewId: Int) {
        onView(
            ViewMatchers.withId(viewId)
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun checkIntentCallSuccess(className: String) {
        Intents.intended(IntentMatchers.hasComponent(className))
    }
}
