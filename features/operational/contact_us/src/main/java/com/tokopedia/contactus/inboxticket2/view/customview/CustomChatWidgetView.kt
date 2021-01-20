package com.tokopedia.contactus.inboxticket2.view.customview

import android.content.Context
import android.graphics.Color
import android.text.Spanned
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import com.tokopedia.contactus.R
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.unifycomponents.ImageUnify

class CustomChatWidgetView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private var chatWidgetButton: ImageUnify
    private var widgetCardView: CardView
    private var customChatWidgetListener: CustomChatWidgetListener? = null
    private var toolTip: ChatWidgetToolTip

    init {
        val view = View.inflate(context, R.layout.custom_chat_widget_view, this)
        widgetCardView = view.findViewById(R.id.widget_card_view)
        chatWidgetButton = view.findViewById(R.id.chat_widget_view)
        customChatWidgetListener = context as? CustomChatWidgetListener
        toolTip = ChatWidgetToolTip(context, attrs, defStyleAttr)
        setUpChatWidgetCardView()
        setUpCHatWidgetButton()
    }

    private fun setUpCHatWidgetButton() {
        chatWidgetButton.setImageDrawable(context.getResDrawable(R.drawable.tanya))
        chatWidgetButton.setOnClickListener {
            if (!toolTip.isShowing) {
                toolTip.showAtTop(this)
            }
            customChatWidgetListener?.onChatWidgetClick()
        }
    }

    private fun setUpChatWidgetCardView() {
        widgetCardView.setCardBackgroundColor(Color.TRANSPARENT)
        widgetCardView.cardElevation = 0F
    }

    fun setToolTipDescription(description: CharSequence){
        toolTip.setToolTipDescription(description)
    }

    interface CustomChatWidgetListener {
        fun onChatWidgetClick()
    }
}