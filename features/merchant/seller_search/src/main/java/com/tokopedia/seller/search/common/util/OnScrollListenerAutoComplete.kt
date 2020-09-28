package com.tokopedia.seller.search.common.util

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler

class OnScrollListenerAutocomplete(val context: Context, val view : View) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        when (newState) {
            RecyclerView.SCROLL_STATE_DRAGGING -> {
                KeyboardHandler.DropKeyboard(view.context, view)
            }
        }
    }
}