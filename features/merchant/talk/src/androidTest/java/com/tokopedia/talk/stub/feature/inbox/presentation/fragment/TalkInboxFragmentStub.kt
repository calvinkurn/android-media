package com.tokopedia.talk.stub.feature.inbox.presentation.fragment

import android.os.Bundle
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.inbox.di.TalkInboxComponent
import com.tokopedia.talk.feature.inbox.presentation.fragment.TalkInboxFragment
import com.tokopedia.talk.feature.inbox.presentation.listener.TalkInboxListener
import com.tokopedia.talk.stub.feature.inbox.di.component.TalkInboxComponentStubInstance

class TalkInboxFragmentStub : TalkInboxFragment() {
    companion object {
        fun createNewInstance(
            tab: TalkInboxTab? = null,
            talkInboxListener: TalkInboxListener? = null
        ): TalkInboxFragmentStub {
            return TalkInboxFragmentStub().apply {
                this.talkInboxListener = talkInboxListener
                arguments = Bundle().apply {
                    tab?.let {
                        putString(TAB_PARAM, it.tabParam)
                    }
                }
            }
        }
    }

    override fun getComponent(): TalkInboxComponent? {
        return activity?.let { TalkInboxComponentStubInstance.getComponent(it.application) }
    }
}
