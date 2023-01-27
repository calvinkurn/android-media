package com.tokopedia.talk.stub.feature.reply.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reply.presentation.activity.TalkReplyActivity
import com.tokopedia.talk.stub.common.di.component.TalkComponentStubInstance
import com.tokopedia.talk.stub.feature.reply.presentation.fragment.TalkReplyFragmentStub

class TalkReplyActivityStub : TalkReplyActivity() {
    companion object {
        @JvmStatic
        fun createIntent(context: Context, questionId: String, shopId: String): Intent {
            return Intent(context, TalkReplyActivityStub::class.java).apply {
                putExtra(TalkConstants.QUESTION_ID, questionId)
                putExtra(TalkConstants.PARAM_SHOP_ID, shopId)
            }
        }
    }

    override fun getComponent(): TalkComponent {
        return TalkComponentStubInstance.getComponent(application)
    }

    override fun getNewFragment(): Fragment? {
        talkReplyFragment = TalkReplyFragmentStub.createNewInstance(
            questionId = questionId,
            shopId = shopId,
            source = source,
            inboxType = inboxType
        )
        return talkReplyFragment
    }
}
