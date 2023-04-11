package com.tokopedia.talk.stub.feature.inbox.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.inbox.presentation.activity.TalkInboxActivity
import com.tokopedia.talk.stub.feature.inbox.presentation.fragment.TalkInboxContainerFragmentStub
import com.tokopedia.talk.stub.feature.inbox.presentation.fragment.TalkInboxFragmentStub

class TalkInboxActivityStub : TalkInboxActivity() {

    companion object {
        fun createIntent(context: Context) : Intent {
            return Intent(context, TalkInboxActivityStub::class.java)
        }
    }

    override fun getNewFragment(): Fragment {
        if (GlobalConfig.isSellerApp()) {
            return TalkInboxFragmentStub.createNewInstance(TalkInboxTab.TalkShopInboxTab())
        }
        if (userSession.hasShop()) {
            return TalkInboxContainerFragmentStub.createNewInstance()
        }
        return TalkInboxFragmentStub.createNewInstance(TalkInboxTab.TalkBuyerInboxTab())
    }
}
