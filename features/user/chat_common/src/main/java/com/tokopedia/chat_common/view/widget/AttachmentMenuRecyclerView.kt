package com.tokopedia.chat_common.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.domain.pojo.attachmentmenu.AttachmentMenu
import com.tokopedia.chat_common.view.adapter.AttachmentMenuAdapter
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachmentItemViewHolder

class AttachmentMenuRecyclerView : RecyclerView, AttachmentItemViewHolder.AttachmentViewHolderListener {

    var container: FrameLayout? = null

    private val manager = GridLayoutManager(context, 4)
    private val adapter = AttachmentMenuAdapter(this)

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

    override fun closeMenu() {
        hideMenu()
    }

    fun toggle() {
        if (isShowing || container == null) return
        if (isVisible) {
            hideMenu()
        } else {
            showMenu()
        }
    }

    fun hideMenu() {
        if (isVisible && !isShowing) {
            isVisible = false
            container?.visibility = View.GONE
        }
    }

    private fun showMenu() {
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
        container?.visibility = View.VISIBLE
    }

    fun setAttachmentMenuListener(listener: AttachmentMenu.AttachmentMenuListener) {
        adapter.attachmentMenuListener = listener
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun addVoucherAttachmentMenu() {
        if (!adapter.alreadyHasAttachVoucherMenu()) {
            adapter.addVoucherAttachmentMenu()
        }
    }

}