package com.tokopedia.talk.feature.reply.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class TalkReplyAttachProductBottomSheet : BottomSheetUnify() {

    companion object {

        fun createInstance(context: Context) : TalkReplyAttachProductBottomSheet {
            return TalkReplyAttachProductBottomSheet().apply{
                val view = View.inflate(context, R.layout.widget_talk_reply_attach_product_bottom_sheet,null)
                setChild(view)
                setTitle(context.getString(R.string.reply_attach_product_bottom_sheet_title))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {

    }

    private fun updateButtonText() {
        
    }
}