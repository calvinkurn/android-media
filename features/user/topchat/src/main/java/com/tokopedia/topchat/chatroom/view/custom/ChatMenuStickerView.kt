package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.topchat.R

class ChatMenuStickerView : LinearLayout {

    var listener: StickerMenuListener? = null

    interface StickerMenuListener {
        fun onStickerOpened()
        fun onStickerClosed()
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initViewLayout()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (isVisible) {
            listener?.onStickerOpened()
        } else {
            listener?.onStickerClosed()
        }
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    companion object {
        private val LAYOUT = R.layout.partial_chat_menu_sticker_view
    }

}