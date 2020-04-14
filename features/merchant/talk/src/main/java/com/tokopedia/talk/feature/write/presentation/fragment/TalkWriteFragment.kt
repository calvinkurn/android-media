package com.tokopedia.talk.feature.write.presentation.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.write.di.DaggerTalkWriteComponent
import com.tokopedia.talk.feature.write.di.TalkWriteComponent

class TalkWriteFragment : BaseDaggerFragment(), HasComponent<TalkWriteComponent> {

    companion object {

        @JvmStatic
        fun createNewInstance(): TalkWriteFragment = TalkWriteFragment()

    }

    override fun getComponent(): TalkWriteComponent {
        return DaggerTalkWriteComponent.builder().talkComponent(
                getComponent(TalkComponent::class.java))
                .build()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component.inject(this)
    }
}