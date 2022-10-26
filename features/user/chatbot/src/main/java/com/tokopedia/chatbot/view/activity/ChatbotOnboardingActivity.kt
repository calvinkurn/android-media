package com.tokopedia.chatbot.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.di.ChatbotModule
import com.tokopedia.chatbot.di.DaggerChatbotComponent
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleOnBoarding
import com.tokopedia.chatbot.view.customview.video_onboarding.VideoUploadOnBoarding
import com.tokopedia.chatbot.view.util.OnboardingDismissListener
import kotlinx.android.synthetic.main.activity_chatbot_onboarding.*
import javax.inject.Inject


class ChatbotOnboardingActivity : BaseSimpleActivity(), OnboardingDismissListener {


    @Inject
    lateinit var videoUploadOnBoarding: VideoUploadOnBoarding
    @Inject
    lateinit var replyBubbleOnBoarding: ReplyBubbleOnBoarding

    var replyBubbleOnboardingDismissed: Boolean = false
    var videoBubbleOnBoardingDismissed: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot_onboarding)

        initInjector()

        checkVideoUploadOnboardingStatus()
        setUpListeners()


        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        Log.d("EREN", "height : $height width : $width")
        val givenX = intent.getIntExtra(X_COORDINATE, 0)
        val givenY = intent.getIntExtra(Y_COORDINATE, 0)
        Log.d("EREN", "onCreate: givenX: $givenX givenY: $givenY")
        val ratioX: Float = (givenX.toFloat() / width.toFloat())
        val ratioY: Float = (givenY.toFloat() / height.toFloat())
        Log.d("EREN", "onCreate: ratioX: $ratioX ratioY: $ratioY")

        val params = guideline_reply_bubble?.layoutParams as ConstraintLayout.LayoutParams
        params.guidePercent = ratioY
        guideline_reply_bubble?.layoutParams = params
        checkReplyBubbleOnboardingStatus()
    }

    private fun setUpListeners(){
        videoUploadOnBoarding.onboardingDismissListener = this
        replyBubbleOnBoarding.onboardingDismissListener = this
    }


    private fun checkVideoUploadOnboardingStatus() {
        val hasBeenShown = videoUploadOnBoarding.hasBeenShown()
        videoBubbleOnBoardingDismissed = hasBeenShown
        if (!false) {

            videoUploadOnBoarding.showVideoBubbleOnBoarding(
                container_view,
                this
            )
        }


    }

    private fun checkReplyBubbleOnboardingStatus() {
        val hasBeenShown = replyBubbleOnBoarding.hasBeenShown()
        replyBubbleOnboardingDismissed = hasBeenShown
        if (!hasBeenShown) {
            replyBubbleOnBoarding.showReplyBubbleOnBoarding(
                guideline_reply_bubble,
                this@ChatbotOnboardingActivity
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

    override fun getNewFragment(): Fragment? = null

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

    override fun onBackPressed() {

    }

    companion object {
        const val X_COORDINATE = "x-coordinate"
        const val Y_COORDINATE = "y-coordinate"
    }
}
