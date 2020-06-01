package com.tokopedia.talk_old.inboxtalk.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk_old.inboxtalk.view.viewmodel.EmptyInboxTalkViewModel
import com.tokopedia.talk_old.inboxtalk.view.viewmodel.InboxTalkItemViewModel

/**
 * @author by nisie on 8/29/18.
 */
interface InboxTalkTypeFactory {

    fun type(inboxTalkItemViewModel: InboxTalkItemViewModel): Int

    fun type(emptyInboxTalkViewModel: EmptyInboxTalkViewModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}