package com.tokopedia.discovery.autocomplete

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler

class OnScrollListenerAutocomplete(val context: Context, val view : View) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        when (newState) {
            RecyclerView.SCROLL_STATE_DRAGGING -> {
                KeyboardHandler.DropKeyboard(view.context, view)
            }
        }
    }
}