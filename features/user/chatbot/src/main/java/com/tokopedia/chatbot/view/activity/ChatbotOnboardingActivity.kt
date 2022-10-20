package com.tokopedia.chatbot.view.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.di.ChatbotModule
import com.tokopedia.chatbot.di.DaggerChatbotComponent
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleOnBoarding
import com.tokopedia.chatbot.view.customview.video_onboarding.VideoUploadOnBoarding
import kotlinx.android.synthetic.main.activity_chatbot_onboarding.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class ChatbotOnboardingActivity : BaseSimpleActivity() {


    @Inject
    lateinit var videoUploadOnBoarding: VideoUploadOnBoarding

    @Inject
    lateinit var replyBubbleOnBoarding: ReplyBubbleOnBoarding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot_onboarding)

        initInjector()

  //      checkVideoUploadOnboardingStatus()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        Log.d("EREN", "height : $height width : $width")
        val givenX = intent.getIntExtra("x-coordinate", 0)
        val givenY = intent.getIntExtra("y-coordinate", 0)
        Log.d("EREN", "onCreate: givenX: $givenX givenY: $givenY")
        val ratioX: Float = (givenX.toFloat() / width.toFloat())
        val ratioY: Float = (givenY.toFloat() / height.toFloat())
        Log.d("EREN", "onCreate: ratioX: $ratioX ratioY: $ratioY")
        checkVideoUploadOnboardingStatus2()

        val params = guideline_reply_bubble?.layoutParams as ConstraintLayout.LayoutParams
        params.guidePercent = 0.8F
        guideline_reply_bubble?.layoutParams = params

    }

    private fun checkVideoUploadOnboardingStatus() {
        val hasBeenShown = videoUploadOnBoarding.hasBeenShown()
        if (!false) {

            val videoPauseButtonHandler = Handler(Looper.getMainLooper())
            videoUploadOnBoarding.showVideoBubbleOnBoarding(
                container_view,
                this
            )
        }


    }

    private fun checkVideoUploadOnboardingStatus2() {
        val hasBeenShown = videoUploadOnBoarding.hasBeenShown()
        if (!false) {
            lifecycleScope.launch {
                delay(2000L)
                videoUploadOnBoarding.showVideoBubbleOnBoarding(
                    guideline_reply_bubble2,
                    this@ChatbotOnboardingActivity
                )
            }

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
}
