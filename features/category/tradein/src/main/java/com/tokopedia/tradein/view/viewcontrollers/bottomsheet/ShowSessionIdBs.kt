package com.tokopedia.tradein.view.viewcontrollers.bottomsheet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.tradein.R
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography


class ShowSessionIdBs(private val sid: String) : BottomSheetUnify() {
    private var contentView: View? = null
    private var etWrapper: TextAreaUnify? = null
    private var actionListener: ShowSessionIdBs.ActionListenerBs? = null

    companion object {
        @JvmStatic
        fun newInstance(sid: String): ShowSessionIdBs {
            return ShowSessionIdBs(sid)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initLayout() {
        setTitle(getString(R.string.tradein_sid))
        isDragable = true
        isHideable = true
        isKeyboardOverlap = false
        customPeekHeight = (getScreenHeight()).toDp()
        showCloseIcon = false
        showKnob = true

        contentView = View.inflate(context,
                R.layout.tradein_bs_show_session_id, null)

        val btnCopy = contentView?.findViewById<UnifyButton>(R.id.btn_sid_copy)
        val tvSid = contentView?.findViewById<Typography>(R.id.text_sid)

        tvSid?.text = sid

        btnCopy?.setOnClickListener {

            val sdk = Build.VERSION.SDK_INT
            if (sdk < Build.VERSION_CODES.HONEYCOMB) {
                val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager?
                clipboard?.text = sid
            } else {
                val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                val clip = ClipData.newPlainText("sid", sid)
                clipboard?.setPrimaryClip(clip)
            }

            dismiss()

            actionListener?.onClick(sid)

        }

        setChild(contentView)
    }

    fun setActionListener(actionListener: ShowSessionIdBs.ActionListenerBs?) {
        this.actionListener = actionListener
    }

    interface ActionListenerBs {
        fun onClick(sid: String?)
    }
}
