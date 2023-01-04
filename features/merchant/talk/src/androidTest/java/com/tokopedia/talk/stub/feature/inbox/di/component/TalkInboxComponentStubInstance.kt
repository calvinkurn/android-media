package com.tokopedia.talk.stub.feature.inbox.di.component

import android.app.Application
import com.tokopedia.talk.stub.common.di.component.TalkComponentStubInstance

object TalkInboxComponentStubInstance {
    private var talkComponent: TalkInboxComponentStub? = null

    fun getComponent(application: Application): TalkInboxComponentStub {
        return talkComponent?.run {
            talkComponent
        } ?: DaggerTalkInboxComponentStub.builder().talkComponentStub(
            TalkComponentStubInstance.getComponent(application)
        ).build()
    }
}
