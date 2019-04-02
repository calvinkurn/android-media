package com.tokopedia.groupchat.chatroom.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.ChannelPartnerChildViewModel;

/**
 * @author by milhamj on 20/03/18.
 */

public interface ChannelInfoFragmentListener {

    interface View extends CustomerView {
        Context getContext();

        void renderData(ChannelInfoViewModel channelInfoViewModel);

        interface ChannelPartnerViewHolderListener {
            void channelPartnerClicked(ChannelPartnerChildViewModel channelPartnerChildViewModel, int position);

            void onPartnerViewed(String partnerName);
        }
    }
}
