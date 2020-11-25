package com.tokopedia

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent

class TalkInstance {
    companion object {
        private var talkComponent: TalkComponent? = null

        fun getComponent(application: Application): TalkComponent {
            return talkComponent?.run {
                talkComponent
            } ?: DaggerTalkComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }
    }
}