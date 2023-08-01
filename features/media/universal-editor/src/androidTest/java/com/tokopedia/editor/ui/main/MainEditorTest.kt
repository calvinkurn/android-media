package com.tokopedia.editor.ui.main

import android.widget.ImageView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.editor.ui.EditorTest
import com.tokopedia.editor.util.FileLoader
import com.tokopedia.editor.R
import com.tokopedia.kotlin.extensions.view.getBitmap
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
        val headerTitle = R.string.universal_editor_nav_bar_add_text

        // When
        startActivity {
            filePaths(listOf(file.path))
            setHeaderTitle(headerTitle)
        }

        // Then
        assertTextDisplayedWith(context.getString(headerTitle))
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

    private fun onImageView(resId: Int, imgView: (ImageView) -> Unit) {
        Espresso.onView(ViewMatchers.withId(resId))
            .check { view, _ -> imgView(view as ImageView) }
    }

    private fun assertTextDisplayedWith(text: String) {
        // set waiting for 500ms before assertion
        Thread.sleep(500)

        Espresso.onView(
            ViewMatchers.withText(text)
        ).check(
            ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        )
    }

    private fun assertRecyclerViewDisplayed(viewId: Int) {
        Espresso.onView(
            ViewMatchers.withId(viewId)
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
