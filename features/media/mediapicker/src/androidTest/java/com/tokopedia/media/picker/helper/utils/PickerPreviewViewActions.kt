package com.tokopedia.media.picker.helper.utils

import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.MotionEvents
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.exoplayer2.ui.DefaultTimeBar
import com.tokopedia.media.R
import com.tokopedia.media.picker.ui.widget.thumbnail.MediaThumbnailWidget
import org.hamcrest.Matcher

object PickerPreviewViewActions {
    fun clickThumbnailItemClose(drawerItemIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(FrameLayout::class.java)
            }

            override fun getDescription(): String {
                return "get preview drawer selection"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val recyclerView = view?.findViewById<RecyclerView>(R.id.rv_thumbnail)
                recyclerView?.get(drawerItemIndex)?.findViewById<AppCompatImageView>(R.id.iv_delete)
                    ?.performClick()
            }
        }
    }

    fun clickAllThumbnailItemClose(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(FrameLayout::class.java)
            }

            override fun getDescription(): String {
                return "get preview drawer selection"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val recyclerView = view?.findViewById<RecyclerView>(R.id.rv_thumbnail)
                for (i in 0 until (recyclerView?.adapter?.itemCount ?: 0)) {
                    recyclerView?.get(i)?.findViewById<AppCompatImageView>(R.id.iv_delete)
                        ?.performClick()
                }
            }
        }
    }

    fun clickThumbnailImage(drawerItemIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(FrameLayout::class.java)
            }

            override fun getDescription(): String {
                return "get preview drawer selection"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val recyclerView = view?.findViewById<RecyclerView>(R.id.rv_thumbnail)
                recyclerView?.get(drawerItemIndex)
                    ?.findViewById<MediaThumbnailWidget>(R.id.img_thumbnail)?.performClick()
            }
        }
    }

    fun clickScrubberTimeBar(videoDuration: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(DefaultTimeBar::class.java)
            }

            override fun getDescription(): String {
                return "get"
            }

            override fun perform(uiController: UiController?, view: View?) {
                view?.let {
                    val timeStep = videoDuration.convertExoPlayerDuration().toFloat()

                    val targetX = (view.width / timeStep) * (timeStep - 1)
                    val targetY = view.height.div(2f)

                    val location = IntArray(2)
                    view.getLocationOnScreen(location)

                    val coordinates = floatArrayOf(targetX + location[0], targetY + location[1])
                    val precision = floatArrayOf(1f, 1f)

                    val down = MotionEvents.sendDown(uiController, coordinates, precision).down
                    uiController!!.loopMainThreadForAtLeast(200)
                    MotionEvents.sendUp(uiController, down, coordinates)
                }
            }
        }
    }

    fun clickIn(x: Int, y: Int): ViewAction {
        return GeneralClickAction(
            Tap.SINGLE,
            { view ->
                val screenPos = IntArray(2)
                view?.getLocationOnScreen(screenPos)

                val screenX = (screenPos[0] + x).toFloat()
                val screenY = (screenPos[1] + y).toFloat()

                floatArrayOf(screenX, screenY)
            },
            Press.FINGER,
            InputDevice.SOURCE_MOUSE,
            MotionEvent.BUTTON_PRIMARY
        )
    }
}