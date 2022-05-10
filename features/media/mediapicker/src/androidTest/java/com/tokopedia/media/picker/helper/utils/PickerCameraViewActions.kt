package com.tokopedia.media.picker.helper.utils

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.otaliastudios.cameraview.CameraView
import com.tokopedia.unifycomponents.TabsUnify
import org.hamcrest.Matcher

object PickerCameraViewActions {
    fun clickGalleryTabAction(index: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(TabsUnify::class.java)
            }

            override fun getDescription(): String {
                return "get picker bottom nav tab"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val tab = view as TabsUnify
                tab.getUnifyTabLayout().getTabAt(index)
                    ?.select()
            }
        }
    }

    fun getFlashViewAction(setCameraRef: (cameraRef: CameraView) -> Unit): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(CameraView::class.java)
            }

            override fun getDescription(): String {
                return "get camera view reference"
            }

            override fun perform(uiController: UiController?, view: View?) {
                setCameraRef(view as CameraView)
            }
        }
    }
}