package com.tokopedia.play.broadcaster.view.custom.pinnedmessage

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroPinnedMsgBinding
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroPinnedMsgEmptyBinding
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroPinnedMsgFilledBinding

/**
 * Created by jegul on 11/10/21
 */
class PinnedMessageView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewPlayBroPinnedMsgBinding.inflate(
        LayoutInflater.from(context),
        this,
        true,
    )

    private var mOnPinnedClickedListener: OnPinnedClickedListener? = null

    init {
        setMode(Mode.Empty)
    }

    fun setMode(mode: Mode) {
        removeAllViews()
        when (mode) {
            Mode.Empty -> setEmptyMode()
            is Mode.Filled -> setFilledMode(mode.message)
        }
    }

    fun setOnPinnedClickedListener(listener: OnPinnedClickedListener?) {
        mOnPinnedClickedListener = listener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mOnPinnedClickedListener = null
    }

    private fun setEmptyMode() {
        val layout = ViewPlayBroPinnedMsgEmptyBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        ).root

        layout.setOnClickListener {
            mOnPinnedClickedListener?.onPinnedClicked(this@PinnedMessageView, "")
        }

        addView(layout)
    }

    private fun setFilledMode(message: String) {
        val layout = ViewPlayBroPinnedMsgFilledBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        )

        layout.root.setOnClickListener {
            mOnPinnedClickedListener?.onPinnedClicked(this@PinnedMessageView, message)
        }
        layout.tvPinnedMsg.text = message

        addView(layout.root)
    }

    sealed class Mode {
        object Empty : Mode()
        data class Filled(val message: String) : Mode()
    }

    fun interface OnPinnedClickedListener {

        fun onPinnedClicked(view: PinnedMessageView, message: String)
    }
}