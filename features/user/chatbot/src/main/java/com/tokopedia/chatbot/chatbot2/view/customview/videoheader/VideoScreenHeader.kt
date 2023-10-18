package com.tokopedia.chatbot.chatbot2.view.customview.videoheader

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chatbot.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifyprinciples.Typography

class VideoScreenHeader(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var backIcon: IconUnify? = null
    private var screenTitle: Typography? = null
    var backClickListener: OnClickBackButton? = null

    init {
        initBindings()
        initClickListener()
    }

    private fun initClickListener() {
        backIcon?.setOnClickListener {
            backClickListener?.navigateToChatbotActivity()
        }
    }

    private fun initBindings() {
        val view = View.inflate(context, LAYOUT, this)
        with(view) {
            backIcon = findViewById(R.id.back_icon)
            screenTitle = findViewById(R.id.title)
        }
        screenTitle?.apply {
            text = context.getString(R.string.chatbot_video_title)
            setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0))
            setType(Typography.DISPLAY_1)
            setWeight(Typography.BOLD)
        }
    }

    companion object {
        val LAYOUT = R.layout.customview_chatbot_video_header_layout
    }

    interface OnClickBackButton {
        fun navigateToChatbotActivity()
    }
}
