package com.tokopedia.play.broadcaster.view.custom.pinnedmessage

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroPinnedMsgBinding
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroPinnedMsgEmptyBinding
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroPinnedMsgFilledBinding
import com.tokopedia.unifyprinciples.R as unifyR

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

    private val offset14 by lazy { resources.getDimensionPixelSize(R.dimen.play_dp_14) }

    private val editDrawable: Drawable? by lazy {
        getIconUnifyDrawable(
            context,
            IconUnify.EDIT,
            MethodChecker.getColor(context, unifyR.color.Unify_Static_White)
        )?.apply drawable@ {
            this@drawable.setBounds(
                0, 0, offset14, offset14,
            )
        }
    }

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
        layout.tvPinnedMsg.text = SpannableStringBuilder().apply {
            append(message)
            val editDrawable = this@PinnedMessageView.editDrawable
            if (editDrawable != null) {
                append(' ')
                append(" ", ImageSpan(editDrawable), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

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