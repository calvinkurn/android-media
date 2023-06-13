package com.tokopedia.talk.stub.feature.reply.presentation.fragment

import android.os.Bundle
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.reply.di.TalkReplyComponent
import com.tokopedia.talk.feature.reply.presentation.fragment.TalkReplyFragment
import com.tokopedia.talk.stub.common.di.component.TalkComponentStubInstance
import com.tokopedia.talk.stub.feature.reply.di.component.DaggerTalkReplyComponentStub

class TalkReplyFragmentStub : TalkReplyFragment() {
    companion object {
        @JvmStatic
        fun createNewInstance(
            questionId: String,
            shopId: String,
            source: String,
            inboxType: String
        ): TalkReplyFragmentStub =
            TalkReplyFragmentStub().apply {
                arguments = Bundle().apply {
                    putString(TalkConstants.QUESTION_ID, questionId)
                    putString(TalkConstants.PARAM_SHOP_ID, shopId)
                    putString(TalkConstants.PARAM_SOURCE, source)
                    putString(TalkConstants.PARAM_TYPE, inboxType)
                }
            }
    }

    override fun getComponent(): TalkReplyComponent? {
        return activity?.run {
            DaggerTalkReplyComponentStub
                .builder()
                .talkComponentStub(TalkComponentStubInstance.getComponent(application))
                .build()
        }
    }
}
