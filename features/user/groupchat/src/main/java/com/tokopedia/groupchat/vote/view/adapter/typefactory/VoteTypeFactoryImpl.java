package com.tokopedia.groupchat.vote.view.adapter.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.groupchat.chatroom.view.fragment.ChannelVoteFragment;
import com.tokopedia.groupchat.chatroom.view.fragment.GroupChatFragment;
import com.tokopedia.groupchat.chatroom.view.listener.ChannelVoteContract;
import com.tokopedia.groupchat.vote.view.adapter.viewholder.VoteBarViewHolder;
import com.tokopedia.groupchat.vote.view.adapter.viewholder.VoteImageViewHolder;
import com.tokopedia.groupchat.vote.view.model.VoteViewModel;

/**
 * @author by StevenFredian on 21/02/18.
 */

public class VoteTypeFactoryImpl extends BaseAdapterTypeFactory implements VoteTypeFactory{

    ChannelVoteContract.View.VoteOptionListener viewListener;

    public VoteTypeFactoryImpl(GroupChatFragment context) {
    }

    public VoteTypeFactoryImpl(ChannelVoteFragment context) {
        viewListener = context;
    }

    public int type(VoteViewModel voteViewModel) {
        if(voteViewModel.getType().equals( VoteViewModel.IMAGE_TYPE)) {
            return VoteImageViewHolder.LAYOUT;
        }else{
            return VoteBarViewHolder.LAYOUT;
        }
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == VoteBarViewHolder.LAYOUT){
            return new VoteBarViewHolder(parent, viewListener);
        }else if(type == VoteImageViewHolder.LAYOUT){
            return new VoteImageViewHolder(parent, viewListener);
        }else {
            return super.createViewHolder(parent, type);
        }
    }
}
