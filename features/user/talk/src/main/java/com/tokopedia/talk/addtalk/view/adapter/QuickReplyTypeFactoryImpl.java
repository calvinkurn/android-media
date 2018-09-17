package com.tokopedia.talk.addtalk.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.talk.addtalk.view.fragment.AddTalkFragment;
import com.tokopedia.talk.addtalk.view.listener.AddTalkContract;

/**
 * @author by StevenFredian on 07/06/18.
 */

public class QuickReplyTypeFactoryImpl extends BaseAdapterTypeFactory implements QuickReplyTypeFactory {


    private final AddTalkContract.View listener;

    public QuickReplyTypeFactoryImpl(AddTalkFragment fragment) {
        listener = fragment;
    }

    @Override
    public int type(TalkQuickReplyItemViewModel groupChatQuickReplyViewModel) {
        return QuickReplyItemViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;

        if (type == QuickReplyItemViewHolder.LAYOUT) {
            viewHolder = new QuickReplyItemViewHolder(parent, listener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }

        return viewHolder;
    }

}
