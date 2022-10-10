package com.tokopedia.chatbot.view.customview.chatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.tokopedia.chatbot.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class BigReplyBoxBottomSheet: BottomSheetUnify() {

    private lateinit var context: FragmentActivity

    init {
        isFullpage = false
        isCancelable = false
        showKnob = true
        showCloseIcon = false
    }

    companion object {
        @JvmStatic
        fun newInstance(context: FragmentActivity): BigReplyBoxBottomSheet {
            return BigReplyBoxBottomSheet().apply {
                this.context = context
            }
        }

        val LAYOUT = R.layout.bottom_sheet_big_reply_box

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = View.inflate(context, LAYOUT, null)
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
