package com.tokopedia.talk.stub.feature.inbox.presentation.fragment

import androidx.fragment.app.Fragment
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.inbox.di.TalkInboxContainerComponent
import com.tokopedia.talk.feature.inbox.presentation.fragment.TalkInboxContainerFragment
import com.tokopedia.talk.stub.feature.inbox.di.component.TalkInboxContainerComponentStubInstance

class TalkInboxContainerFragmentStub : TalkInboxContainerFragment() {

    companion object {
        fun createNewInstance(): TalkInboxContainerFragmentStub {
            return TalkInboxContainerFragmentStub()
        }
    }

    override fun getComponent(): TalkInboxContainerComponent? {
        return activity?.let { TalkInboxContainerComponentStubInstance.getComponent(it.application) }
    }

    override fun getFragmentList(): List<Fragment> {
        return listOf(
            TalkInboxFragmentStub.createNewInstance(TalkInboxTab.TalkShopInboxTab(), this),
            TalkInboxFragmentStub.createNewInstance(TalkInboxTab.TalkBuyerInboxTab(), this)
        )
    }
}
