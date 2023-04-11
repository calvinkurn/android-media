package com.tokopedia.chatbot.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.chatbot.databinding.ActivityChatbotOnboardingBinding
import com.tokopedia.chatbot.di.ChatbotModule
import com.tokopedia.chatbot.di.DaggerChatbotComponent
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleOnBoarding
import com.tokopedia.chatbot.view.util.OnboardingReplayDismissListener
import javax.inject.Inject


class ChatbotOnboardingActivity : BaseSimpleActivity(), OnboardingReplayDismissListener {

    @Inject
    lateinit var replyBubbleOnBoarding: ReplyBubbleOnBoarding

    private var _viewBinding: ActivityChatbotOnboardingBinding? = null
    private fun getBindingView() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityChatbotOnboardingBinding.inflate(layoutInflater)
        setContentView(_viewBinding!!.root)
        initInjector()
        setUpListeners()
        val ratioY = calculateRatiosForGuideline()
        setUpReplyBubbleGuideline(ratioY)
        checkReplyBubbleOnboardingStatus()
        getBindingView().parentLayout.isClickable = true
        getBindingView().parentLayout.setOnClickListener {
            replyBubbleOnBoarding.dismiss()
        }
        window.statusBarColor = Color.TRANSPARENT
    }

    /**
     * Getting the x and y coordinates from ChatbotFragment , calculate the ratioY and align the
     * guideline accordingly. The guideline will change the position of the reply_bubble_holder view
     * which is used to show the pointer on the coachmark
     * */
    private fun calculateRatiosForGuideline(): Float {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val givenY = intent.getIntExtra(Y_COORDINATE, DIMEN_DEFAULT)
        val ratioY: Float = (givenY.toFloat() / height.toFloat())
        return ratioY
    }

    private fun setUpReplyBubbleGuideline(ratioY: Float) {
        if (ratioY == ZERO_RATIO)
            return
        val params =
            getBindingView().guidelineReplyBubble.layoutParams as ConstraintLayout.LayoutParams
        params.guidePercent = ratioY
        getBindingView().guidelineReplyBubble.layoutParams = params
    }

    private fun setUpListeners() {
        replyBubbleOnBoarding.onboardingDismissListener = this
    }

    private fun checkReplyBubbleOnboardingStatus() {
        replyBubbleOnBoarding.showReplyBubbleOnBoarding(getBindingView().replyBubbleHolder, this)
    }

    private fun initInjector() {
        val chatbotComponent = DaggerChatbotComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        )
            .chatbotModule(ChatbotModule(this))
            .build()

        chatbotComponent.inject(this)
    }

    override fun dismissReplyBubbleOnBoarding() {
        closeOnboardingActivity()
    }

    private fun closeOnboardingActivity() {
        val intent = Intent(this, ChatbotActivity::class.java)
        startActivity(intent)
        setResult(RESULT_OK)
        finish()
    }

    override fun onBackPressed() {
        replyBubbleOnBoarding.dismiss()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
        replyBubbleOnBoarding.flush()
    }

    /**
     * Coachmark with position as TOP shows odd behaviour of changing it's place -> therefore adding
     * a constant value to the position if we get the coachmark by scrolling to user
     * */
    companion object {
        private const val Y_COORDINATE = "y-coordinate"
        private const val ZERO_RATIO = 0F
        private const val DIMEN_DEFAULT = 0
    }
}
