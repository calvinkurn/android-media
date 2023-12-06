package com.tokopedia.chatbot.chatbot2.view.customview.chatroom

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.customview.chatroom.CircleAnimation
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography

class SlowModeSendButton(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private lateinit var circleAnimation: CircleAnimation
    private lateinit var textTimer: Typography
    private lateinit var iconSend: IconUnify
    private var countDownTimer: CountDownTimer? = null

    init {
        inflateLayout()
    }

    private fun inflateLayout() {
        val view = View.inflate(context, LAYOUT, this)
        circleAnimation = view.findViewById(R.id.circle_animation)
        textTimer = view.findViewById(R.id.text_timer)
        iconSend = view.findViewById(R.id.icon_send)

//        startSlowDown(3000)
//        enableSendButton()
//        disableSendButton()
    }

    private fun initCountDownTimer(durationInMillis: Long) {
        countDownTimer = object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val text = (millisUntilFinished / 1000) + 1
                textTimer.text = text.toString()
            }

            override fun onFinish() {
                enableSendButton()
            }
        }
    }

    fun enableSendButton() {
        circleAnimation.enable()
        iconSend.show()
        textTimer.gone()
    }

    fun disableSendButton() {
        circleAnimation.disable()
        iconSend.show()
        textTimer.gone()
    }

    fun startSlowDown(durationInMillis: Long) {
        circleAnimation.loading(durationInMillis)
        iconSend.gone()
        textTimer.show()
        initCountDownTimer(durationInMillis)
        countDownTimer?.start()
    }

    fun cancelSlowDown() {
        countDownTimer?.cancel()
    }

    companion object {
        val LAYOUT = R.layout.customview_slow_mode_send_button
    }
}
