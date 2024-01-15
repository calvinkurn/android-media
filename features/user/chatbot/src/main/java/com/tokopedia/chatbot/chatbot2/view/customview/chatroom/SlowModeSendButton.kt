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

    private var circleAnimation: CircleAnimation? = null
    private var textTimer: Typography? = null
    private var iconSend: IconUnify? = null
    private var countDownTimer: CountDownTimer? = null

    var isSlowModeEnabled: Boolean = false
    var initialSlowModeDurationInSecond: Int = 0
    var currentSlowModeDurationInSecond: Int = 0
    var isSlowModeRunning: Boolean = false

    init {
        inflateLayout()
    }

    private fun inflateLayout() {
        val view = View.inflate(context, LAYOUT, this)
        circleAnimation = view.findViewById(R.id.circle_animation)
        textTimer = view.findViewById(R.id.text_timer)
        iconSend = view.findViewById(R.id.icon_send)
    }

    private fun initCountDownTimer(durationInSecond: Int = initialSlowModeDurationInSecond) {
        countDownTimer = object : CountDownTimer((durationInSecond * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val text = (millisUntilFinished / 1000) + 1
                textTimer?.text = text.toString()
                currentSlowModeDurationInSecond = ((millisUntilFinished / 1000) + 1).toInt()
            }

            override fun onFinish() {
                isSlowModeRunning = false
                enableSendButton()
            }
        }
        countDownTimer?.start()
    }

    fun enableSendButton() {
        if (isSlowModeRunning) return
        circleAnimation?.enable()
        iconSend?.show()
        textTimer?.gone()
    }

    fun disableSendButton() {
        if (isSlowModeRunning) return
        circleAnimation?.disable()
        iconSend?.show()
        textTimer?.gone()
    }

    fun startSlowDown(durationInSecond: Int = initialSlowModeDurationInSecond) {
        if (isSlowModeRunning) return
        if (isSlowModeEnabled) {
            isSlowModeRunning = true
            circleAnimation?.loading(durationInSecond)
            iconSend?.gone()
            textTimer?.show()
            initCountDownTimer(durationInSecond)
        }
    }

    fun cancelSlowDown() {
        if (isSlowModeEnabled) {
            countDownTimer?.cancel()
        }
    }

    companion object {
        val LAYOUT = R.layout.customview_slow_mode_send_button
    }
}
