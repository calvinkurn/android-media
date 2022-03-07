package com.tokopedia.autocompletecomponent.util

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler

class OnScrollListenerAutocomplete(private val activity: Activity?) : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        val activity = activity ?: return

        when (newState) {
            SCROLL_STATE_DRAGGING -> KeyboardHandler.hideSoftKeyboard(activity)
        }
    }
}