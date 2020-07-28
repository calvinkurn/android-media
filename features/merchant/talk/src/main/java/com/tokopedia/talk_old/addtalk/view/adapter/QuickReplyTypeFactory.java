package com.tokopedia.talk_old.addtalk.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

/**
 * @author by StevenFredian on 07/06/18.
 */

public interface QuickReplyTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    int type(TalkQuickReplyItemViewModel groupChatQuickReplyViewModel);
}
