package com

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener

class RecyclerViewLayoutChangedAssertion(val onLayoutChanges: () -> Unit) : ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        Log.d("EspressoAja", "is not ready yet!")
        val recyclerView: RecyclerView = view as RecyclerView
        recyclerView.addOneTimeGlobalLayoutListener {
            assert(true)
            Log.d("EspressoAja", "YES!")
            onLayoutChanges.invoke()
        }
    }
}