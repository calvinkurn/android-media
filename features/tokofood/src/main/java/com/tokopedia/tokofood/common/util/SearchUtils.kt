package com.tokopedia.tokofood.common.util

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.unifycomponents.SearchBarUnify

internal class OnScrollListenerSearch(val view : View) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        when (newState) {
            RecyclerView.SCROLL_STATE_DRAGGING -> {
                KeyboardHandler.DropKeyboard(view.context, view)
            }
        }
    }
}

@SuppressLint("ClickableViewAccessibility")
internal fun Fragment.hideKeyboardOnTouchListener()  {
    view?.setOnTouchListener(object : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (v == null) return false
            if (v !is SearchBarUnify || v !is EditText) {
                KeyboardHandler.DropKeyboard(v.context, v)
            }
            return true
        }
    })
}