package com.tokopedia.tokochat.stub.common.matcher

import android.widget.TextView
import androidx.test.espresso.ViewAssertion
import org.junit.Assert.assertTrue

fun hasTextFormat(pattern: Regex): ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val textView = view as TextView
        val text = textView.text.toString()
        assertTrue(pattern.matches(text))
    }
}
