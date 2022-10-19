package com.tokopedia.chatbot.view.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.di.ChatbotModule
import com.tokopedia.chatbot.di.DaggerChatbotComponent
import com.tokopedia.chatbot.view.customview.video_onboarding.VideoUploadOnBoarding
import kotlinx.android.synthetic.main.activity_chatbot_onboarding.*
import javax.inject.Inject

class ChatbotOnboardingActivity : BaseSimpleActivity() {


    @Inject
    lateinit var videoUploadOnBoarding: VideoUploadOnBoarding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot_onboarding)

        initInjector()

        checkVideoUploadOnboardingStatus()
    }

    private fun checkVideoUploadOnboardingStatus() {
        val hasBeenShown = videoUploadOnBoarding.hasBeenShown()
        if (!false) {

            val videoPauseButtonHandler = Handler(Looper.getMainLooper())
            videoPauseButtonHandler.postDelayed(
                {
                    videoUploadOnBoarding.showVideoBubbleOnBoarding(
                        container_view,
                        this
                    )
                },
                500L
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
}
