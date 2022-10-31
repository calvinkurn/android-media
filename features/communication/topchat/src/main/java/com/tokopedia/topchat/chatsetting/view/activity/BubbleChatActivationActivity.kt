package com.tokopedia.topchat.chatsetting.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.di.ChatListContextModule
import com.tokopedia.topchat.chatlist.di.DaggerChatListComponent
import com.tokopedia.topchat.chatsetting.view.fragment.BubbleChatActivationGuideFragment
import com.tokopedia.topchat.chatsetting.view.fragment.BubbleChatActivationIntroFragment
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.common.util.BubbleChat
import javax.inject.Inject

class BubbleChatActivationActivity : BaseSimpleActivity(),
    BubbleChatActivationIntroFragment.Listener, HasComponent<ChatListComponent> {

    @Inject
    lateinit var topChatCacheManager: TopchatCacheManager

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

    override fun getComponent(): ChatListComponent {
        return DaggerChatListComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .chatListContextModule(ChatListContextModule(this))
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        introFragment = null
        guideFragment = null
    }

    override fun onIntroButtonClicked() {
        navigateToGuideFragment()
        setIntroButtonFinishClicked()
    }

    private fun getIsBubbleIntroFinished(): Boolean {
        return topChatCacheManager.loadCache(BubbleChat.Key.FINISH_INTRO, Boolean::class.java)
    }

    private fun navigateToGuideFragment() {
        val targetGuideFragment = guideFragment ?: BubbleChatActivationGuideFragment()
        supportFragmentManager.beginTransaction()
            .replace(parentViewResourceID, targetGuideFragment, BUBBLE_CHAT_GUIDE_TAG)
            .commit()
    }

    private fun setIntroButtonFinishClicked() {
        topChatCacheManager.saveState(BubbleChat.Key.FINISH_INTRO, true)
    }

    companion object {
        private const val BUBBLE_CHAT_GUIDE_TAG = "bubble_chat_guide"
    }

}
