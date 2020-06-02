package com.tokopedia.talk_old.inboxtalk.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk_old.inboxtalk.view.adapter.InboxTalkTypeFactory

/**
 * @author by nisie on 8/29/18.
 */

class EmptyInboxTalkViewModel : Visitable<InboxTalkTypeFactory> {

    override fun type(typeFactory: InboxTalkTypeFactory): Int {
        return typeFactory.type(this)
    }

}