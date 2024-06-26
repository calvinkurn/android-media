package com.tokopedia.topchat.chatsetting.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topchat.chatsetting.di.ChatSettingComponent
import com.tokopedia.topchat.chatsetting.di.ChatSettingModule
import com.tokopedia.topchat.chatsetting.di.DaggerChatSettingComponent
import com.tokopedia.topchat.chatsetting.view.fragment.BubbleChatActivationGuideFragment
import com.tokopedia.topchat.chatsetting.view.fragment.BubbleChatActivationIntroFragment
import com.tokopedia.topchat.common.analytics.TopChatAnalyticsKt
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.common.util.BubbleChat
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class BubbleChatActivationActivity : BaseSimpleActivity(),
    BubbleChatActivationIntroFragment.Listener, HasComponent<ChatSettingComponent> {

    @Inject
    lateinit var topChatCacheManager: TopchatCacheManager

    @Inject
    lateinit var userSession: UserSessionInterface

    private var introFragment: Fragment? = null
    private var guideFragment: Fragment? = null

    override fun getNewFragment(): Fragment {
        return if (getIsBubbleIntroFinished()) {
            val currentGuideFragment = BubbleChatActivationGuideFragment()
            guideFragment = currentGuideFragment
            currentGuideFragment
        } else {
            val currentIntroFragment = BubbleChatActivationIntroFragment.createInstance(this)
            introFragment = currentIntroFragment
            currentIntroFragment
        }
    }

    override fun getComponent(): ChatSettingComponent {
        return DaggerChatSettingComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .chatSettingModule(ChatSettingModule(this))
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        getToolbarTitle()?.let { toolbarTitle ->
            title = toolbarTitle
        }
        super.onCreate(savedInstanceState)
        setupBackground()
    }

    override fun onDestroy() {
        super.onDestroy()
        introFragment = null
        guideFragment = null
    }

    override fun onIntroButtonClicked() {
        navigateToGuideFragment()
        setIntroButtonFinishClicked()
        trackIntroButtonFinishClicked()
    }

    override fun onPageLoaded() {
        TopChatAnalyticsKt.eventImpressionBubbleChatIntroduction(userSession.shopId)
    }

    private fun getToolbarTitle(): String? {
        return try {
            getString(com.tokopedia.topchat.R.string.topchat_bubble_settings_title)
        } catch (ex: Exception) {
            null
        }
    }

    private fun setupBackground() {
        window?.decorView?.setBackgroundColor(
            MethodChecker.getColor(
                this,
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        )
    }

    private fun getIsBubbleIntroFinished(): Boolean {
        return try {
            topChatCacheManager.loadCache(BubbleChat.Key.FINISH_INTRO, Boolean::class.java) ?: false
        } catch (ex: Exception) {
            false
        }
    }

    private fun navigateToGuideFragment() {
        val targetGuideFragment = guideFragment ?: BubbleChatActivationGuideFragment()
        supportFragmentManager.beginTransaction()
            .replace(parentViewResourceID, targetGuideFragment, BUBBLE_CHAT_GUIDE_TAG)
            .commit()
    }

    private fun setIntroButtonFinishClicked() {
        topChatCacheManager.saveCache(BubbleChat.Key.FINISH_INTRO, true.toString())
    }

    private fun trackIntroButtonFinishClicked() {
        TopChatAnalyticsKt.eventClickBubbleHelpPage(userSession.shopId)
    }

    companion object {
        private const val BUBBLE_CHAT_GUIDE_TAG = "bubble_chat_guide"
    }

}
