package com.tokopedia.talk.feature.inbox.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.talk.feature.inbox.presentation.fragment.TalkInboxFragment

class TalkInboxActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return TalkInboxFragment.createNewInstance()
    }
}