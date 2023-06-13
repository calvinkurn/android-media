package com.tokopedia.talk.stub.common.di.component

import android.app.Application

object TalkComponentStubInstance {
    private var talkComponent: TalkComponentStub? = null

    fun getComponent(application: Application): TalkComponentStub {
        return talkComponent?.run {
            talkComponent
        } ?: DaggerTalkComponentStub.builder().baseAppComponentStub(
            BaseAppComponentStubInstance.getBaseAppComponentStub(application)
        ).build()
    }
}
