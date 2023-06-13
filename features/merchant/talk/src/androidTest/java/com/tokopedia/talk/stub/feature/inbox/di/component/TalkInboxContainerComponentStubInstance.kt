package com.tokopedia.talk.stub.feature.inbox.di.component

import android.app.Application
import com.tokopedia.talk.stub.common.di.component.TalkComponentStubInstance

object TalkInboxContainerComponentStubInstance {
    private var talkComponent: TalkInboxContainerComponentStub? = null

    fun getComponent(application: Application): TalkInboxContainerComponentStub {
        return talkComponent?.run {
            talkComponent
        } ?: DaggerTalkInboxContainerComponentStub.builder().talkComponentStub(
            TalkComponentStubInstance.getComponent(application)
        ).build()
    }
}
