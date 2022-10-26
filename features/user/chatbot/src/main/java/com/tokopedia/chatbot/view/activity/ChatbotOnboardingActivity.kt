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
import com.tokopedia.chatbot.view.customview.video_onboarding.VideoUploadOnBoarding
import com.tokopedia.chatbot.view.util.OnboardingDismissListener
import javax.inject.Inject


class ChatbotOnboardingActivity : BaseSimpleActivity(), OnboardingDismissListener {

    @Inject
    lateinit var videoUploadOnBoarding: VideoUploadOnBoarding
    @Inject
    lateinit var replyBubbleOnBoarding: ReplyBubbleOnBoarding

    //TODO change them
    var replyBubbleOnboardingDismissed: Boolean = true
    var videoBubbleOnBoardingDismissed: Boolean = true

    private var _viewBinding: ActivityChatbotOnboardingBinding? = null
    private fun getBindingView() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityChatbotOnboardingBinding.inflate(layoutInflater)
        setContentView(_viewBinding!!.root)
        initInjector()
        setUpListeners()
        checkVideoUploadOnboardingStatus()
        val ratioY = calculateRatiosForGuideline()
        setUpReplyBubbleGuideline(ratioY)
        checkReplyBubbleOnboardingStatus()
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
        val width = displayMetrics.widthPixels
        val givenX = intent.getIntExtra(X_COORDINATE, DIMEN_DEFAULT)
        val givenY = intent.getIntExtra(Y_COORDINATE, DIMEN_DEFAULT)
        val ratioX: Float = (givenX.toFloat() / width.toFloat())
        val ratioY: Float = (givenY.toFloat() / height.toFloat())
        return ratioY
    }

    private fun setUpReplyBubbleGuideline(ratioY: Float) {
        if (ratioY == ZERO_RATIO)
            return
        val params = getBindingView().guidelineReplyBubble.layoutParams as ConstraintLayout.LayoutParams
        params.guidePercent = ratioY
        getBindingView().guidelineReplyBubble.layoutParams = params
    }

    private fun setUpListeners(){
        videoUploadOnBoarding.onboardingDismissListener = this
        replyBubbleOnBoarding.onboardingDismissListener = this
    }

    private fun checkVideoUploadOnboardingStatus() {
        val hasBeenShown = videoUploadOnBoarding.hasBeenShown()
        videoBubbleOnBoardingDismissed = hasBeenShown
        if (!hasBeenShown) {
            videoUploadOnBoarding.showVideoBubbleOnBoarding(
                getBindingView().containerView,
                this
            )
        }
    }

    private fun checkReplyBubbleOnboardingStatus() {
        val hasBeenShown = replyBubbleOnBoarding.hasBeenShown()
        replyBubbleOnboardingDismissed = hasBeenShown
        if (!hasBeenShown) {
            replyBubbleOnBoarding.showReplyBubbleOnBoarding(
                getBindingView().replyBubbleHolder,
                this
            )
        }
    }

    private fun initInjector() {
        val chatbotComponent = DaggerChatbotComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        )
            .chatbotModule(ChatbotModule(this))
            .build()

        chatbotComponent.inject(this)
    }

    private fun checkToCloseOnboardingActivity() {
        if (replyBubbleOnboardingDismissed && videoBubbleOnBoardingDismissed) {
            val intent = Intent(this, ChatbotActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun dismissReplyBubbleOnBoarding() {
        replyBubbleOnboardingDismissed = true
        checkToCloseOnboardingActivity()
    }

    override fun dismissVideoUploadOnBoarding() {
        videoBubbleOnBoardingDismissed = true
        checkToCloseOnboardingActivity()
    }

    /**
     * Back Button is disabled as the coachmarks needs to be closed only with the dismiss button
     * on the coachmarks
     * */
    override fun onBackPressed() = Unit

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    companion object {
        private const val X_COORDINATE = "x-coordinate"
        private const val Y_COORDINATE = "y-coordinate"
        private const val ZERO_RATIO = 0F
        private const val DIMEN_DEFAULT = 0
    }
}
