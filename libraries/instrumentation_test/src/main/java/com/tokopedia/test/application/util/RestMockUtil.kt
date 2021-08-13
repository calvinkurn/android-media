package com.tokopedia.test.application.util

import android.view.View
import android.widget.Checkable
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.core.Is
import java.io.IOException


object RestMockUtil {

    fun getJsonFromResource(path: String): String {
        var json = ""
        try {
            val inputStream = this.javaClass.classLoader!!.getResourceAsStream(path)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }

    fun setChecked(checked: Boolean): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): BaseMatcher<View?> {
                return object : BaseMatcher<View?>() {
                    override fun matches(item: Any?): Boolean {
                        return Is.isA(Checkable::class.java).matches(item)
                    }

                    override fun describeMismatch(item: Any?, mismatchDescription: Description?) {}
                    override fun describeTo(description: Description?) {}
                }
            }

            override fun getDescription(): String {
                return "error set check on checkbox"
            }

            override fun perform(uiController: UiController?, view: View) {
                val checkableView = view as Checkable
                checkableView.isChecked = checked
            }
        }
    }
}