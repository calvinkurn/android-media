package com.tokopedia.editor.ui.main

import android.widget.ImageView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.editor.ui.EditorTest
import com.tokopedia.editor.util.FileLoader
import com.tokopedia.editor.R
import com.tokopedia.picker.common.R as pickercommonR
import com.tokopedia.editor.data.mapper.NavigationTypeMapper
import com.tokopedia.editor.util.FontAlignment
import com.tokopedia.editor.util.isAlignmentMatch
import com.tokopedia.kotlin.extensions.view.getBitmap
import com.tokopedia.picker.common.types.ToolType
import com.tokopedia.test.application.annotations.UiTest
import org.hamcrest.core.AllOf
import org.junit.Test
import org.junit.Assert

@UiTest
class EditorPageUITest : EditorTest() {

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
        Thread.sleep(200)
        onView(ViewMatchers.withId(R.id.add_text_input)).perform(ViewActions.typeText(typedText))
        onView(ViewMatchers.withId(R.id.alignment_icon)).perform(ViewActions.click())
        onView(AllOf.allOf(ViewMatchers.withParent(ViewMatchers.withId(R.id.font_color_container)), ViewMatchers.withParentIndex(2))).perform(ViewActions.click())
        Thread.sleep(1000)
        onView(ViewMatchers.withText("Simpan")).perform(ViewActions.click())

        // Then
        Thread.sleep(1000)
        assertTextDisplayedWith(typedText)
        onView(ViewMatchers.withText(typedText)).check(ViewAssertions.matches(ViewMatchers.hasTextColor(
            R.color.dms_universal_editor_yn_500
        )))
        onView(ViewMatchers.withText(typedText)).check(ViewAssertions.matches(isAlignmentMatch(FontAlignment.LEFT)))
    }

    @Test
    fun should_zoom_image() {
        // Given
        val expectedRatio = 9/16f // width/height
        val file = FileLoader.imageFile(context)
        val actionText = context.getString(NavigationTypeMapper.invoke(ToolType.PLACEMENT).second)

        // When
        startActivity {
            filePaths(listOf(file.path))
        }
        onView(ViewMatchers.withText(actionText)).perform(ViewActions.click())
        Thread.sleep(300)
        onView(ViewMatchers.withId(R.id.crop_area)).perform(ViewActions.click(), ViewActions.doubleClick())
        Thread.sleep(300)
        onView(ViewMatchers.withId(pickercommonR.id.action_text_done)).perform(ViewActions.click(), ViewActions.doubleClick())

        // Then
        Thread.sleep(2000)
        Assert.assertEquals(getImageResultRatio(), expectedRatio)
    }

    // ============================================================================

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
}
