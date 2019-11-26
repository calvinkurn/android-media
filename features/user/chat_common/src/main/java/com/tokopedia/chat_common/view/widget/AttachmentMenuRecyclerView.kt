package com.tokopedia.chat_common.view.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.view.adapter.AttachmentMenuAdapter
import com.tokopedia.unifycomponents.toPx

class AttachmentMenuRecyclerView : RecyclerView {

    private val manager = GridLayoutManager(context, 4)
    private val adapter = AttachmentMenuAdapter()
    private val marginBottom = 16.toPx()

    var isVisible = false
    var isShowing = false
    var showDelayed = false
    var isKeyboardOpened = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
            context,
            attrs,
            defStyle
    )

    init {
        setHasFixedSize(true)
        layoutManager = manager
        setAdapter(adapter)
    }

    fun toggle() {
        if (isShowing) return
        if (isVisible) {
            hideMenu()
        } else {
            showMenu()
        }
    }

    fun hideMenu() {
        if (isVisible && !isShowing) {
            Log.d("MENU_STATE", "hide menu")
            isVisible = false
            visibility = View.GONE
        }
    }

    private fun showMenu() {
        Log.d("MENU_STATE", "show menu")
        isShowing = true
        if (!isKeyboardOpened) {
            show()
        } else {
            showDelayed = true
            hideKeyboard()
        }
    }

    fun showDelayed() {
        show()
    }

    fun show() {
        isShowing = false
        showDelayed = false
        isVisible = true
        visibility = View.VISIBLE
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}